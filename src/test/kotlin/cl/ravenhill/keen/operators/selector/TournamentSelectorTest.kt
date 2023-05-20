//package cl.ravenhill.keen.operators.selector
//
//import cl.ravenhill.keen.Core
//import cl.ravenhill.enforcer.IntRequirementException
//import cl.ravenhill.enforcer.EnforcementException
//import cl.ravenhill.keen.intChromosomeFactory
//import cl.ravenhill.keen.population
//import cl.ravenhill.keen.shouldBeOfClass
//import cl.ravenhill.keen.util.optimizer.FitnessMaximizer
//import io.kotest.assertions.throwables.shouldThrow
//import io.kotest.core.spec.style.FreeSpec
//import io.kotest.matchers.shouldBe
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.long
//import io.kotest.property.arbitrary.nonPositiveInt
//import io.kotest.property.arbitrary.positiveInt
//import io.kotest.property.checkAll
//import kotlin.random.Random
//
//class TournamentSelectorTest : FreeSpec({
//    afterAny { Core.random = Random.Default }
//    "Creating a selector should" - {
//        "throw an exception if sample size is non-positive" {
//            checkAll(Arb.nonPositiveInt()) { sampleSize ->
//                shouldThrow<EnforcementException> {
//                    TournamentSelector<Int>(sampleSize)
//                }.violations.forEach {
//                    it shouldBeOfClass IntRequirementException::class
//                }
//            }
//        }
//    }
//    "Equality should" - {
//        "be true for the same instance" {
//            val selector = TournamentSelector<Int>(1)
//            selector shouldBe selector
//        }
//        "be true for two instances with the same sample size" {
//            checkAll(Arb.positiveInt()) { sampleSize ->
//                TournamentSelector<Int>(sampleSize) shouldBe TournamentSelector(
//                    sampleSize
//                )
//            }
//        }
//    }
//    "Selecting one individual from a population should" - {
//        "return a random individual when sample size is 1" {
//            checkAll(
//                Arb.population(Arb.intChromosomeFactory()),
//                Arb.long()
//            ) { population, seed ->
//                Core.random = Random(seed)
//                val random = Random(seed)
//                val selector = TournamentSelector<Int>(1)
//                val selected = selector.selectOneFrom(population, FitnessMaximizer())
//                val expected = population[random.nextInt(population.size)]
//                selected shouldBe expected
//            }
//        }
//        "return the only individual when the population has only one individual" {
//            checkAll(
//                Arb.population(Arb.intChromosomeFactory(), 1),
//                Arb.positiveInt(100)
//            ) { population, sampleSize ->
//                val selector = TournamentSelector<Int>(sampleSize)
//                val selected = selector.selectOneFrom(population, FitnessMaximizer())
//                selected shouldBe population.first()
//            }
//        }
//        "return the winner of a tournament when sample size is greater than 1" {
//            checkAll(
//                Arb.population(Arb.intChromosomeFactory(1), 4),
//                Arb.positiveInt(100),
//                Arb.long()
//            ) { population, sampleSize, seed ->
//                Core.random = Random(seed)
//                val random = Random(seed)
//                val selector = TournamentSelector<Int>(sampleSize)
//                val selected = selector.selectOneFrom(population, FitnessMaximizer())
//                val expected = (0 until sampleSize).map {
//                    population[random.nextInt(population.size)]
//                }.maxByOrNull { it.fitness }!!
//                selected shouldBe expected
//            }
//        }
//    }
//})
