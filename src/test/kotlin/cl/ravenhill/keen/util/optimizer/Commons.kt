package cl.ravenhill.keen.util.optimizer

import cl.ravenhill.keen.genetic.phenotype
import io.kotest.core.spec.style.wordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll


fun `invoking should be consistent with compare`(optimizer: PhenotypeOptimizer<Double>) =
    wordSpec {
        "Invoking" should {
            "be consistent with compare" {
                checkAll(Arb.phenotype(), Arb.phenotype()) { phenotype1, phenotype2 ->
                    optimizer(phenotype1, phenotype2) shouldBe optimizer.compare(
                        phenotype1,
                        phenotype2
                    )
                }
            }
        }
    }

fun `a comparator can be created from the optimizer`(optimizer: PhenotypeOptimizer<Double>) =
    wordSpec {
        "a comparator" should {
            "be created from the optimizer" {
                val comparator = optimizer.comparator
                checkAll(Arb.phenotype(), Arb.phenotype()) { phenotype1, phenotype2 ->
                    comparator.compare(phenotype1, phenotype2) shouldBe optimizer.compare(
                        phenotype1,
                        phenotype2
                    )
                }
            }
        }
    }