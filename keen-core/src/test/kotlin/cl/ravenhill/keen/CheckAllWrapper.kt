/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen

import io.kotest.property.Gen
import io.kotest.property.PropTestConfig
import io.kotest.property.PropertyContext
import io.kotest.property.checkAll as kotestCheckAll

// THIS FILE IS NEEDED TO AVOID A BUG IN INTELLIJ IDEA
suspend fun <A, B, C, D, E, F, G, H, I, J> checkAll(
    genA: Gen<A>,
    genB: Gen<B>,
    genC: Gen<C>,
    genD: Gen<D>,
    genE: Gen<E>,
    genF: Gen<F>,
    genG: Gen<G>,
    genH: Gen<H>,
    genI: Gen<I>,
    genJ: Gen<J>,
    property: suspend PropertyContext.(A, B, C, D, E, F, G, H, I, J) -> Unit,
) = kotestCheckAll(genA, genB, genC, genD, genE, genF, genG, genH, genI, genJ, property)

suspend fun <A, B, C, D, E, F, G, H, I, J> checkAll(
    config: PropTestConfig,
    genA: Gen<A>,
    genB: Gen<B>,
    genC: Gen<C>,
    genD: Gen<D>,
    genE: Gen<E>,
    genF: Gen<F>,
    genG: Gen<G>,
    genH: Gen<H>,
    genI: Gen<I>,
    genJ: Gen<J>,
    property: suspend PropertyContext.(A, B, C, D, E, F, G, H, I, J) -> Unit,
) = kotestCheckAll(config, genA, genB, genC, genD, genE, genF, genG, genH, genI, genJ, property)

suspend inline fun <
      reified A, reified B, reified C, reified D, reified E, reified F, reified G, reified H, reified I, reified J,
      > checkAll(
    config: PropTestConfig,
    noinline property: suspend PropertyContext.(A, B, C, D, E, F, G, H, I, J) -> Unit,
) = kotestCheckAll(config, property)

suspend fun <A, B, C, D, E, F, G, H, I, J> checkAll(
    iterations: Int,
    genA: Gen<A>,
    genB: Gen<B>,
    genC: Gen<C>,
    genD: Gen<D>,
    genE: Gen<E>,
    genF: Gen<F>,
    genG: Gen<G>,
    genH: Gen<H>,
    genI: Gen<I>,
    genJ: Gen<J>,
    property: suspend PropertyContext.(A, B, C, D, E, F, G, H, I, J) -> Unit,
) = kotestCheckAll(iterations, genA, genB, genC, genD, genE, genF, genG, genH, genI, genJ, property)

suspend fun <A, B, C, D, E, F, G, H, I, J> checkAll(
    iterations: Int,
    config: PropTestConfig,
    genA: Gen<A>,
    genB: Gen<B>,
    genC: Gen<C>,
    genD: Gen<D>,
    genE: Gen<E>,
    genF: Gen<F>,
    genG: Gen<G>,
    genH: Gen<H>,
    genI: Gen<I>,
    genJ: Gen<J>,
    property: suspend PropertyContext.(A, B, C, D, E, F, G, H, I, J) -> Unit,
) = kotestCheckAll(iterations, config, genA, genB, genC, genD, genE, genF, genG, genH, genI, genJ, property)

suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F, reified G, reified H, reified I, reified J> checkAll(
    iterations: Int,
    config: PropTestConfig,
    noinline property: suspend PropertyContext.(A, B, C, D, E, F, G, H, I, J) -> Unit,
) = kotestCheckAll(iterations, config, property)

suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F, reified G, reified H, reified I, reified J> checkAll(
    iterations: Int,
    noinline property: suspend PropertyContext.(A, B, C, D, E, F, G, H, I, J) -> Unit,
) = kotestCheckAll(iterations, property)

suspend inline fun <reified A, reified B, reified C, reified D, reified E, reified F, reified G, reified H, reified I, reified J> checkAll(
    noinline property: suspend PropertyContext.(A, B, C, D, E, F, G, H, I, J) -> Unit,
) = kotestCheckAll(property)
