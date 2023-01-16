package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.numerical.intChromosome
import cl.ravenhill.keen.genetic.genes.intGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.probability
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

class MutatorSpec : WordSpec({
    afterAny {
        Core.random = Random.Default
        Core.Dice.random = Random.Default
    }
    "Chromosome" When {
        "mutating an Int chromosome" should {
            "return the same chromosome if the probability is 0" {
                checkAll(
                    Arb.intChromosome()
                ) { chromosome ->
                    val mutator = Mutator<Int>(0.0)
                    val (mutated, mutations) = mutator.mutateChromosome(chromosome)
                    mutations shouldBe 0
                    mutated shouldBe chromosome
                }
            }
            "return a chromosome with all genes mutated if the probability is 1" {
                checkAll(
                    Arb.intChromosome(),
                    Arb.long()
                ) { chromosome, seed ->
                    Core.random = Random(seed)
                    val random = Random(seed)
                    val mutator = Mutator<Int>(1.0)
                    val (mutated, mutations) = mutator.mutateChromosome(chromosome)
                    mutations shouldBe chromosome.genes.size
                    mutated shouldBe chromosome.duplicate(
                        chromosome.genes.map { gene ->
                            gene as IntGene
                            IntGene(
                                random.nextInt(gene.start, gene.end),
                                gene.range,
                                gene.filter
                            )
                        }
                    )
                }
            }
            "return a mutated chromosome according to the probability" {
                checkAll(
                    Arb.intChromosome(),
                    Arb.probability(),
                    Arb.long(),
                    Arb.long()
                ) { chromosome, probability, diceSeed, coreSeed ->
                    val mutator = Mutator<Int>(probability)
                    Core.Dice.random = Random(diceSeed)
                    val dice = Random(diceSeed)
                    Core.random = Random(coreSeed)
                    val random = Random(coreSeed)
                    val (mutated, mutations) = mutator.mutateChromosome(chromosome)
                    val rolls = chromosome.genes.map { dice.nextDouble() }
                    mutations shouldBe rolls.count { it < probability }
                    mutated shouldBe chromosome.duplicate(
                        chromosome.genes.mapIndexed { index, gene ->
                            gene as IntGene
                            if (rolls[index] < probability) IntGene(
                                random.nextInt(gene.start, gene.end),
                                gene.range,
                                gene.filter
                            ) else gene
                        }
                    )
                }
            }
        }
    }
    "Convert to String" should {
        "return the correct string representation" {
            checkAll(
                Arb.probability()
            ) { probability ->
                Mutator<Int>(probability).toString() shouldBe "Mutator { probability: $probability }"
            }
        }
    }
    "Gene" When {
        "mutating an Int gene" should {
            "return the same gene if the probability is 0" {
                checkAll(
                    Arb.intGene()
                ) { gene ->
                    val mutator = Mutator<Int>(0.0)
                    val (mutated, mutations) = mutator.mutateGene(gene)
                    mutations shouldBe 0
                    mutated shouldBe gene
                }
            }
            "return a random value between its range if the probability is 1" {
                checkAll(
                    Arb.intGene(),
                    Arb.long()
                ) { gene, seed ->
                    val mutator = Mutator<Int>(1.0)
                    Core.random = Random(seed)
                    val random = Random(seed)
                    val (mutated, mutations) = mutator.mutateGene(gene)
                    mutations shouldBe 1
                    mutated shouldBe IntGene(
                        random.nextInt(gene.start, gene.end),
                        gene.range,
                        gene.filter
                    )
                }
            }
            "return a mutated gene according to the probability" {
                checkAll(
                    Arb.intGene(),
                    Arb.probability(),
                    Arb.long(),
                    Arb.long()
                ) { gene, probability, diceSeed, coreSeed ->
                    val mutator = Mutator<Int>(probability)
                    Core.Dice.random = Random(diceSeed)
                    val dice = Random(diceSeed)
                    Core.random = Random(coreSeed)
                    val random = Random(coreSeed)
                    val (mutated, mutations) = mutator.mutateGene(gene)
                    val roll = dice.nextDouble()
                    mutations shouldBe if (roll < probability) 1 else 0
                    mutated shouldBe if (roll < probability) IntGene(
                        random.nextInt(gene.start, gene.end),
                        gene.range,
                        gene.filter
                    ) else gene
                }
            }
        }
    }
})
