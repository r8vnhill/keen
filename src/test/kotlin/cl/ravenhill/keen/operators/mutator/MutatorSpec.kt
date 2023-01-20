package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.*
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.intChromosome
import cl.ravenhill.keen.genetic.genes.intGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.operators.crossover.population
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.positiveInt
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
                `mutating a chromosome with probability 0 returns the same chromosome`(
                    Mutator(0.0),
                    Arb.intChromosome()
                )
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
                Mutator<Int>(probability).toString() shouldBe
                        "Mutator { probability: $probability }"
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
    "Mutating a GENOTYPE" When {
        "mutating an Int genotype" should {
            "return the same genotype if the probability is 0" {
                checkAll(
                    Arb.genotype(Arb.intChromosomeFactory())
                ) { genotype ->
                    val mutator = Mutator<Int>(0.0)
                    val (mutated, mutations) = mutator.mutateGenotype(genotype)
                    mutations shouldBe 0
                    mutated shouldBe genotype
                }
            }
            "return a genotype with all chromosomes mutated if the probability is 1" {
                checkAll(
                    Arb.genotype(Arb.intChromosomeFactory()),
                    Arb.long()
                ) { genotype, seed ->
                    Core.random = Random(seed)
                    val random = Random(seed)
                    val mutator = Mutator<Int>(1.0)
                    val (mutated, mutations) = mutator.mutateGenotype(genotype)
                    mutations shouldBe genotype.chromosomes.sumOf { it.genes.size }
                    mutated shouldBe genotype.duplicate(
                        genotype.chromosomes.map { chromosome ->
                            chromosome.duplicate(
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
                    )
                }
            }
            "return a mutated genotype according to the probability" {
                checkAll(
                    Arb.genotype(Arb.intChromosomeFactory()),
                    Arb.probability(),
                    Arb.long(),
                    Arb.long()
                ) { genotype, probability, diceSeed, coreSeed ->
                    val mutator = Mutator<Int>(probability)
                    Core.Dice.random = Random(diceSeed)
                    val dice = Random(diceSeed)
                    Core.random = Random(coreSeed)
                    val random = Random(coreSeed)
                    val (mutated, mutations) = mutator.mutateGenotype(genotype)
                    val rolls = genotype.chromosomes.map { chromosome ->
                        chromosome.genes.map { dice.nextDouble() }
                    }
                    mutations shouldBe rolls.sumOf { roll ->
                        roll.count { it < probability }
                    }
                    mutated shouldBe genotype.duplicate(
                        genotype.chromosomes.mapIndexed { chromosomeIndex, chromosome ->
                            chromosome.duplicate(
                                chromosome.genes.mapIndexed { geneIndex, gene ->
                                    gene as IntGene
                                    if (rolls[chromosomeIndex][geneIndex] < probability) {
                                        IntGene(
                                            random.nextInt(gene.start, gene.end),
                                            gene.range,
                                            gene.filter
                                        )
                                    } else gene
                                }
                            )
                        }
                    )
                }
            }
        }
    }
    "Mutating a PHENOTYPE" When {
        "composed of INT genes" should {
            "return the same phenotype if the probability is 0" {
                checkAll(
                    Arb.phenotype(Arb.intChromosomeFactory())
                ) { phenotype ->
                    val mutator = Mutator<Int>(0.0)
                    val (mutated, mutations) = mutator.mutatePhenotype(
                        phenotype,
                        phenotype.generation
                    )
                    mutations shouldBe 0
                    mutated shouldBe phenotype
                }
            }
            "return a phenotype with all chromosomes mutated if the probability is 1" {
                checkAll(
                    Arb.phenotype(Arb.intChromosomeFactory()),
                    Arb.long()
                ) { phenotype, seed ->
                    Core.random = Random(seed)
                    val random = Random(seed)
                    val mutator = Mutator<Int>(1.0)
                    val (mutated, mutations) = mutator.mutatePhenotype(
                        phenotype,
                        phenotype.generation
                    )
                    mutations shouldBe phenotype.genotype.chromosomes.sumOf { it.genes.size }
                    mutated shouldBe Phenotype(
                        phenotype.genotype.duplicate(
                            phenotype.genotype.chromosomes.map { chromosome ->
                                chromosome.duplicate(
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
                        ), phenotype.generation
                    )
                }
            }
            "return a mutated phenotype according to the probability" {
                checkAll(
                    Arb.phenotype(Arb.intChromosomeFactory()),
                    Arb.probability(),
                    Arb.long(),
                    Arb.long()
                ) { phenotype, probability, diceSeed, coreSeed ->
                    val mutator = Mutator<Int>(probability)
                    Core.Dice.random = Random(diceSeed)
                    val dice = Random(diceSeed)
                    Core.random = Random(coreSeed)
                    val random = Random(coreSeed)
                    val (mutated, mutations) = mutator.mutatePhenotype(
                        phenotype,
                        phenotype.generation
                    )
                    val rolls = phenotype.genotype.chromosomes.map { chromosome ->
                        chromosome.genes.map { dice.nextDouble() }
                    }
                    mutations shouldBe rolls.sumOf { roll ->
                        roll.count { it < probability }
                    }
                    mutated shouldBe Phenotype(
                        phenotype.genotype.duplicate(
                            phenotype.genotype.chromosomes.mapIndexed { chromosomeIndex, chromosome ->
                                chromosome.duplicate(
                                    chromosome.genes.mapIndexed { geneIndex, gene ->
                                        gene as IntGene
                                        if (rolls[chromosomeIndex][geneIndex] < probability) {
                                            IntGene(
                                                random.nextInt(gene.start, gene.end),
                                                gene.range,
                                                gene.filter
                                            )
                                        } else gene
                                    }
                                )
                            }
                        ), phenotype.generation
                    )
                }
            }
        }
    }
    "Invoking" should {
        "return the same Population if the probability is 0" {
            checkAll(
                Arb.population(Arb.intChromosomeFactory()),
                Arb.positiveInt()
            ) { population, generation ->
                val mutator = Mutator<Int>(0.0)
                val (mutated, mutations) = mutator(population, generation)
                mutations shouldBe 0
                mutated shouldBe population
            }
        }
        "return a Population with all individuals mutated if the probability is 1" {
            checkAll(
                PropTestConfig(100),
                Arb.population(Arb.intChromosomeFactory(100), 10),
                Arb.positiveInt(),
                Arb.long()
            ) { population, generation, seed ->
                Core.random = Random(seed)
                val random = Random(seed)
                val mutator = Mutator<Int>(1.0)
                val (mutated, mutations) = mutator(population, generation)
                mutations shouldBe population.sumOf { it.flatten().size }
                mutated shouldBe population.map { individual ->
                    Phenotype(
                        individual.genotype.duplicate(
                            individual.genotype.chromosomes.map { chromosome ->
                                chromosome.duplicate(
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
                        ), generation
                    )
                }
            }
        }
        "return a mutated Population according to the probability" {
            checkAll(
                Arb.population(Arb.intChromosomeFactory(100), 10),
                Arb.probability(),
                Arb.positiveInt(),
                Arb.long(),
                Arb.long()
            ) { population, probability, generation, diceSeed, coreSeed ->
                val mutator = Mutator<Int>(probability)
                Core.Dice.random = Random(diceSeed)
                val dice = Random(diceSeed)
                Core.random = Random(coreSeed)
                val random = Random(coreSeed)
                val (mutated, mutations) = mutator(population, generation)
                val rolls = population.map { individual ->
                    individual.genotype.chromosomes.map { chromosome ->
                        chromosome.genes.map { dice.nextDouble() }
                    }
                }
                mutations shouldBe rolls.sumOf { individualRolls ->
                    individualRolls.sumOf { chromosomeRolls ->
                        chromosomeRolls.count { it < probability }
                    }
                }
                mutated shouldBe population.mapIndexed { individualIndex, individual ->
                    Phenotype(
                        individual.genotype.duplicate(
                            individual.genotype.chromosomes
                                .mapIndexed { chromosomeIndex, chromosome ->
                                    chromosome.duplicate(
                                        chromosome.genes.mapIndexed { geneIndex, gene ->
                                            gene as IntGene
                                            if (rolls[individualIndex][chromosomeIndex][
                                                    geneIndex] < probability
                                            ) {
                                                IntGene(
                                                    random.nextInt(gene.start, gene.end),
                                                    gene.range,
                                                    gene.filter
                                                )
                                            } else gene
                                        }
                                    )
                                }
                        ), generation
                    )
                }
            }
        }
    }
})
