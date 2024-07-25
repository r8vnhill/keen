/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.evolution

import cl.ravenhill.keen.arb.KeenArb
import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.config.AlterationConfig
import cl.ravenhill.keen.evolution.config.EvolutionConfig
import cl.ravenhill.keen.evolution.config.PopulationConfig
import cl.ravenhill.keen.evolution.config.SelectionConfig
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.operators.selection.Selector
import cl.ravenhill.keen.ranking.FitnessRanker
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

/**
 * Generates an arbitrary `PopulationConfig` for property-based testing, suitable for configuring
 * genetic algorithm populations. This function leverages arbitrary generators for `Genotype.Factory`
 * and population size to construct varied `PopulationConfig` instances. It's an extension function
 * of the `Arb` companion object.
 *
 * @param genotypeFactory An `Arb<Genotype.Factory<T, G>>` instance for generating the genotype factory
 *                        used in population configurations. The genotype factory determines how individuals
 *                        within the population are created.
 * @param populationSize An `Arb<Int>` instance for generating the size of the population. The population size
 *                       dictates how many individuals will be present in the population.
 * @return An `Arb<PopulationConfig<T, G>>` instance that generates `PopulationConfig` objects with the
 *         specified genotype factory and population size.
 * @param T The type of the value that the Gene represents.
 * @param G The type of the Gene, constrained to be a subclass of `Gene<T, G>`.
 */
fun <T, G> arbPopulationConfig(
    genotypeFactory: Arb<Genotype.Factory<T, G>>,
    populationSize: Arb<Int>,
): Arb<PopulationConfig<T, G>> where G : Gene<T, G> = arbitrary {
    PopulationConfig(genotypeFactory = genotypeFactory.bind(), populationSize = populationSize.bind())
}

/**
 * Constructs an arbitrary generator for `SelectionConfig` instances, tailored for property-based testing in
 * evolutionary algorithms. This generator uses arbitrary values for survival rate, parent selector, and survivor
 * selector to produce diverse selection configurations. It's an extension function of the `Arb` companion object.
 *
 * @param survivalRate An `Arb<Double>` instance for generating the survival rate, representing the proportion of
 *                     individuals surviving to the next generation.
 * @param parentSelector An `Arb<Selector<T, G>>` instance for generating the parent selection strategy, which
 *                       determines how individuals are chosen to reproduce.
 * @param survivorSelector An `Arb<Selector<T, G>>` instance for generating the survivor selection strategy, which determines
 *                         how individuals are chosen to survive to the next generation.
 * @return An `Arb<SelectionConfig<T, G>>` instance that generates `SelectionConfig` objects with the specified survival rate and selectors.
 * @param T The type of the value that the Gene represents.
 * @param G The type of the Gene, constrained to be a subclass of `Gene<T, G>`.
 */
fun <T, G> arbSelectionConfig(
    survivalRate: Arb<Double>,
    parentSelector: Arb<Selector<T, G>>,
    survivorSelector: Arb<Selector<T, G>>,
): Arb<SelectionConfig<T, G>> where G : Gene<T, G> = arbitrary {
    SelectionConfig(
        survivalRate = survivalRate.bind(),
        parentSelector = parentSelector.bind(),
        survivorSelector = survivorSelector.bind()
    )
}

fun <T, G> arbAlterationConfig(
    alterers: Arb<List<Alterer<T, G>>>
): Arb<AlterationConfig<T, G>> where G : Gene<T, G> = arbitrary {
    AlterationConfig(alterers.bind())
}

fun <T, G> KeenArb.evolutionConfig(
    limits: Arb<List<Limit<T, G>>>,
    ranker: Arb<FitnessRanker<T, G>>,
    listeners: Arb<List<EvolutionListener<T, G>>>,
    evaluator: Arb<EvaluationExecutor<T, G>>,
    interceptor: Arb<EvolutionInterceptor<T, G>>,
): Arb<EvolutionConfig<T, G>> where G : Gene<T, G> = arbitrary {
    EvolutionConfig(
        limits = limits.bind(),
        ranker = ranker.bind(),
        listeners = listeners.bind(),
        evaluator = evaluator.bind(),
        interceptor = interceptor.bind()
    )
}

//fun <T, G> Arb.Companion.engine(
//    genotypeFactory: Arb<Genotype.Factory<T, G>>,
//) where G : Gene<T, G> = arbitrary {
//    EvolutionEngine(
//        genotypeFactory = genotypeFactory.bind()
//
//    )
//}
