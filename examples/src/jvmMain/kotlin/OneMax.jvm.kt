/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

import kotlinx.coroutines.runBlocking

/**
 * Entry point for running the OneMax genetic algorithm.
 *
 * @see oneMax
 */
fun main() {
    runBlocking {
        oneMax()
    }
}
