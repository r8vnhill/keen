/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.arbEvolutionRecord
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.FitnessMinRanker
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.SimpleFeature
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element
import io.kotest.property.checkAll
import kotlin.time.Duration
import kotlin.time.TestTimeSource
import kotlin.time.TimeSource

private typealias SimpleFitnessMaxRanker =
        FitnessMaxRanker<Int, SimpleFeature<Int>, Representation<Int, SimpleFeature<Int>>>

private typealias SimpleListenerConfiguration =
        ListenerConfiguration<Int, SimpleFeature<Int>, Representation<Int, SimpleFeature<Int>>>

class ListenerConfigurationTest : FreeSpec({
    "A ListenerConfiguration" - {
        "should have a ranker property that" - {
            "should be a FitnessMaxRanker by default" {
                val config = ListenerConfiguration<Int, SimpleFeature<Int>, Representation<Int, SimpleFeature<Int>>>()
                config.ranker.shouldBeInstanceOf<SimpleFitnessMaxRanker>()
            }

            "should be the specified ranker" {
                checkAll(
                    Arb.element(
                        FitnessMaxRanker<Int, SimpleFeature<Int>, Representation<Int, SimpleFeature<Int>>>(),
                        FitnessMinRanker()
                    )
                ) { ranker ->
                    val config = ListenerConfiguration(ranker = ranker)
                    config.ranker shouldBe ranker
                }
            }
        }

        "should have an evolution property that" - {
            "should be an empty EvolutionRecord by default" {
                val config = ListenerConfiguration<Int, SimpleFeature<Int>, Representation<Int, SimpleFeature<Int>>>()
                config.evolution shouldBe EvolutionRecord()
            }

            "should be the specified EvolutionRecord" {
                checkAll(
                    arbEvolutionRecord<Int, SimpleFeature<Int>, Representation<Int, SimpleFeature<Int>>>()
                ) { record ->
                    val config = ListenerConfiguration(evolution = record)
                    config.evolution shouldBe record
                }
            }
        }

        "should have a timeSource property that" - {
            "should be TimeSource.Monotonic by default" {
                val config = ListenerConfiguration<Int, SimpleFeature<Int>, Representation<Int, SimpleFeature<Int>>>()
                config.timeSource shouldBe TimeSource.Monotonic
            }

            "should be the specified TimeSource" {
                checkAll(
                    Arb.element(TimeSource.Monotonic, TestTimeSource())
                ) { timeSource ->
                    val config = SimpleListenerConfiguration(timeSource = timeSource)
                    config.timeSource shouldBe timeSource
                }
            }
        }

        "should have a precision property that" - {
            "should be Duration::inWholeMilliseconds by default" {
                val config = ListenerConfiguration<Int, SimpleFeature<Int>, Representation<Int, SimpleFeature<Int>>>()
                config.precision shouldBe Duration::inWholeMilliseconds
            }

            "should be the specified precision function" {
                checkAll(
                    Arb.element(Duration::inWholeMilliseconds, Duration::inWholeMicroseconds)
                ) { precision ->
                    val config = SimpleListenerConfiguration(precision = precision)
                    config.precision shouldBe precision
                }
            }
        }
    }
})
