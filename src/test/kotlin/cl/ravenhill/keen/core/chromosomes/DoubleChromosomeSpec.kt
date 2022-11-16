package cl.ravenhill.keen.core.chromosomes

import cl.ravenhill.keen.core.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.signals.configuration.ChromosomeConfigurationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.math.max
import kotlin.math.min


class DoubleChromosomeSpec : WordSpec({
    "A DoubleChromosome" When {
        "building" should {
            "be created with a given size" {
                checkAll(arbitraryChromosome()) { chromosomeData ->
                    assume(!chromosomeData.range.start.isNaN())
                    assume(!chromosomeData.range.endInclusive.isNaN())
                    assume(chromosomeData.range.start != chromosomeData.range.endInclusive)
                    val chromosome = chromosomeData.toChromosome()
                    chromosome.size shouldBe chromosomeData.size
                }
            }

            "throw an exception if the size is not positive" {
                checkAll(
                    arbitraryChromosome(Arb.nonPositiveInt())
                ) { chromosomeData ->
                    shouldThrow<ChromosomeConfigurationException> {
                        chromosomeData.toChromosome()
                    }.message shouldContain "The size of a chromosome must be greater than 0."
                }
            }

            "throw an exception if the range start is greater than the range end" {
                checkAll(Arb.positiveInt(100), Arb.double(), Arb.double()) { size, x, y ->
                    assume(!x.isNaN())
                    assume(!y.isNaN())
                    shouldThrow<ChromosomeConfigurationException> {
                        DoubleChromosome.Builder(size, max(x, y)..min(x, y))
                    }.message shouldContain "The range of a chromosome must be valid."
                }
            }

            "throw an exception if the range start is equal to the range end" {
                checkAll(Arb.positiveInt(100), Arb.double()) { size, x ->
                    assume(!x.isNaN())
                    shouldThrow<ChromosomeConfigurationException> {
                        DoubleChromosome.Builder(size, x..x)
                    }.message shouldContain "The range of a chromosome must be valid."
                }
            }

            "throw an exception if the range start or end is NaN" {
                checkAll(Arb.positiveInt(100), Arb.double()) { size, x ->
                    shouldThrow<ChromosomeConfigurationException> {
                        DoubleChromosome.Builder(size, x..Double.NaN)
                        DoubleChromosome.Builder(size, Double.NaN..x)
                    }.message shouldContain "The range of a chromosome must be valid."
                }
            }
        }
    }
})

private data class ChromosomeData(
    val size: Int,
    val range: ClosedFloatingPointRange<Double>
) {
    fun toChromosome(): DoubleChromosome = DoubleChromosome.Builder(size, range).build()
}

private fun arbitraryChromosome(sizeGenerator: Arb<Int> = Arb.positiveInt(100)) = Arb.bind(
    sizeGenerator,
    Arb.double(),
    Arb.double()
) { size, x, y ->
    ChromosomeData(size, min(x, y)..max(x, y))
}