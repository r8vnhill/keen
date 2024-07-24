/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.annotations.ExperimentalKeen
import cl.ravenhill.keen.arb.any
import cl.ravenhill.keen.arb.arbEnvironment
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.assume
import io.kotest.property.checkAll

@OptIn(ExperimentalKeen::class)
class EnvironmentTest : FreeSpec({

    afterEach { Domain.environments.clear() }

    "An Environment" - {
        "when created" - {
            "should add itself to the list of environments" {
                Domain.environments.shouldBeEmpty()
                checkAll(Arb.list(arbEnvironment<Any?>(Arb.map(Arb.int(), Arb.any())), 1..10)) { envs ->
                    envs.forEach {
                        Domain.environments shouldContain (it.id to it)
                    }
                    Domain.environments.clear()
                }
            }
        }

        "should have a set of entries that" - {
            "is empty by default" {
                checkAll(arbEnvironment<Any?>(null)) { env ->
                    env.entries.shouldBeEmpty()
                }
            }

            "contains the entries added to the environment" {
                checkAll(arbEnvironment<Any?>(null), Arb.map(Arb.int(), Arb.any())) { env, map ->
                    map.forEach { (key, value) ->
                        env[key] = value
                    }
                    env.entries shouldContainAll map.entries
                }
            }
        }

        "should have a set of keys that" - {
            "is empty by default" {
                checkAll(arbEnvironment<Any?>(null)) { env ->
                    env.keys.shouldBeEmpty()
                }
            }

            "contains the keys added to the environment" {
                checkAll(arbEnvironment<Any?>(null), Arb.map(Arb.int(), Arb.any())) { env, map ->
                    map.forEach { (key, value) ->
                        env[key] = value
                    }
                    env.keys shouldContainAll map.keys
                }
            }
        }

        "should have a size property that" - {
            "is 0 by default" {
                checkAll(arbEnvironment<Any?>(null)) { env ->
                    env.size shouldBe 0
                }
            }

            "is the number of entries in the environment" {
                checkAll(arbEnvironment<Any?>(null), Arb.map(Arb.int(), Arb.any())) { env, map ->
                    map.forEach { (key, value) ->
                        env[key] = value
                    }
                    env.size shouldBe map.size
                }
            }
        }

        "should have a values property that" - {
            "is empty by default" {
                checkAll(arbEnvironment<Any?>(null)) { env ->
                    env.values.shouldBeEmpty()
                }
            }

            "contains the values added to the environment" {
                checkAll(arbEnvironment<Any?>(null), Arb.map(Arb.int(), Arb.any())) { env, map ->
                    map.forEach { (key, value) ->
                        env[key] = value
                    }
                    env.values shouldContainAll map.values
                }
            }
        }

        "can be accessed using the [] operator" - {
            "and returns null if the key is not present" {
                checkAll(arbEnvironment<Any?>(null), Arb.int()) { env, key ->
                    env[key].shouldBeNull()
                }
            }

            "and returns the value associated with the key" {
                checkAll(arbEnvironment<Any?>(null), Arb.map(Arb.int(), Arb.any())) { env, map ->
                    map.forEach { (key, value) ->
                        env[key] = value
                    }
                    map.forEach { (key, value) ->
                        env[key] shouldBe value
                    }
                }
            }
        }

        "can be cleared" - {
            "and removes all the entries" {
                checkAll(arbEnvironment<Any?>(Arb.map(Arb.int(), Arb.any()))) { env ->
                    env.clear()
                    env.entries.shouldBeEmpty()
                }
            }
        }

        "can be checked for emptiness" - {
            "and returns true if the environment is empty" {
                checkAll(arbEnvironment<Any?>(null)) { env ->
                    env.isEmpty().shouldBeTrue()
                }
            }

            "and returns false if the environment is not empty" {
                checkAll(arbEnvironment<Any?>(Arb.map(Arb.int(), Arb.any(), 1, 10))) { env ->
                    env.isEmpty().shouldBeFalse()
                }
            }
        }

        "can remove a key" - {
            "and returns null if the key is not present" {
                checkAll(arbEnvironment<Any?>(Arb.map(Arb.int(), Arb.any())), Arb.int()) { env, key ->
                    env.remove(key).shouldBeNull()
                }
            }

            "and returns the value associated with the key" {
                checkAll(arbEnvironment<Any?>(Arb.map(Arb.int(), Arb.any()))) { env ->
                    val keys = env.keys.toList()
                    keys.forEach { key ->
                        env[key] shouldBe env.remove(key)
                    }
                }
            }
        }

        "can put a key-value pair" - {
            "and returns null if the key was not present" {
                checkAll(
                    arbEnvironment<Any?>(Arb.map(Arb.int(), Arb.any())),
                    Arb.int(),
                    Arb.any()
                ) { env, key, value ->
                    env.put(key, value).shouldBeNull()
                }
            }

            "and returns the previous value associated with the key" {
                checkAll(arbEnvironment<Any?>(Arb.map(Arb.int(), Arb.any()))) { env ->
                    env.forEach { t, u ->
                        env.put(t, u) shouldBe u
                    }
                }
            }
        }

        "can put all the entries in a map" - {
            "and replaces the previous entries" {
                checkAll(
                    arbEnvironment<Any?>(Arb.map(Arb.int(), Arb.any())),
                    Arb.map(Arb.int(), Arb.any())
                ) { env, map ->
                    env.putAll(map)
                    env.entries shouldContainAll map.entries
                }
            }
        }

        "can check if it contains a key" - {
            "and returns false if the key is not present" {
                checkAll(
                    arbEnvironment<Any?>(
                        Arb.map(
                            keyArb = Arb.int(),
                            valueArb = Arb.any(),
                            minSize = 0,
                            maxSize = 25
                        )
                    ), Arb.int()
                ) { env, key ->
                    assume {
                        env.keys shouldNotContain key
                    }
                    env.containsKey(key).shouldBeFalse()
                }
            }

            "and returns true if the key is present" {
                checkAll(arbEnvironment<Any?>(Arb.map(
                    keyArb = Arb.int(),
                    valueArb = Arb.any(),
                    minSize = 0,
                    maxSize = 25
                ))) { env ->
                    env.forEach { t, _ ->
                        env.containsKey(t).shouldBeTrue()
                    }
                }
            }
        }

        "can check if it contains a value" - {
            "and returns false if the value is not present" {
                checkAll(arbEnvironment<Any?>(Arb.map(Arb.int(), Arb.any())), Arb.any()) { env, value ->
                    assume {
                        env.values shouldNotContain value
                    }
                    env.containsValue(value).shouldBeFalse()
                }
            }

            "and returns true if the value is present" {
                checkAll(arbEnvironment<Any?>(Arb.map(Arb.int(), Arb.any()))) { env ->
                    env.forEach { _, u ->
                        env.containsValue(u).shouldBeTrue()
                    }
                }
            }
        }
    }
})
