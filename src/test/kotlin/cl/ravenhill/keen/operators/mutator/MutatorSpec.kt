package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.numerical.intChromosome
import cl.ravenhill.keen.genetic.genes.doubleGene
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
    }
    "Mutating a gene" When {
        "the gene is an Int" should {
            "return a random value between its range" {
                checkAll(
                    Arb.intGene(),
                    Arb.probability(),
                    Arb.long()
                ) { gene, probability, seed ->
                    Core.random = Random(seed)
                    val random = Random(seed)
                    val mutated = Mutator<Int>(probability).mutateGene(gene)
                    val expected = IntGene(
                        random.nextInt(gene.range.first, gene.range.second),
                        gene.range,
                        gene.filter
                    )
                    mutated shouldBe expected
                }
            }
        }
        "the gene is a Double" should {
            "return a random value between its range" {
                checkAll(
                    Arb.doubleGene(),
                    Arb.probability(),
                    Arb.long()
                ) { gene, probability, seed ->
                    Core.random = Random(seed)
                    val random = Random(seed)
                    val mutated = Mutator<Double>(probability).mutateGene(gene)
                    val expected = gene.duplicate(
                        random.nextDouble(gene.range.first, gene.range.second)
                    )
                    mutated shouldBe expected
                }
            }
        }
    }
    "Chromosome" When {
        "mutating an Int chromosome" should {
            "return the same chromosome if the probability is 0" {
                checkAll(
                    Arb.intChromosome()
                ) { chromosome ->
                    val mutated = Mutator<Int>(0.0).mutateChromosome(chromosome)
                    mutated shouldBe MutatorResult(chromosome, 0)
                }
            }
        }
    }
})
