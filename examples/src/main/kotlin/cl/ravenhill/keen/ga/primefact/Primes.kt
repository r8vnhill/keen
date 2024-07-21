/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.primefact

import kotlin.math.sqrt


/**
 * The number of prime numbers to be generated by the [primes] function.
 *
 * This constant defines the total count of prime numbers that the `primes` function will generate. It is used to set a
 * limit on the number of primes calculated, ensuring that the function returns a fixed-size list of the first prime
 * numbers. The value is set to 50, meaning that the `primes` function will compute and return the first 50 prime
 * numbers starting from 2.
 *
 * ## Usage:
 * The `NUM_PRIMES` constant is used in the `primes` function to determine the size of the list of prime numbers that
 * the function will generate. This allows for consistent and predictable output from the function.
 *
 * ### Example:
 * ```kotlin
 * // In the primes function
 * while (primes.size < NUM_PRIMES) {
 *     // Generate prime numbers
 * }
 * ```
 * In this context, `NUM_PRIMES` controls the loop within the `primes` function to ensure that exactly 50 prime numbers
 * are generated and added to the list.
 *
 * @return The number of prime numbers to be generated, set to 50.
 */
private const val NUM_PRIMES = 20

/**
 * Generates a list of the first [NUM_PRIMES] prime numbers.
 *
 * This function computes prime numbers starting from 2 and adds them to a list until the list contains NUM_PRIMES prime
 * numbers.
 * It uses the `isPrime` function to check whether a number is prime. The generated list is particularly useful in
 * scenarios where a fixed set of prime numbers is needed, such as in certain mathematical computations or
 * cryptographic algorithms.
 *
 * ## Process:
 * - The function starts with the first prime number candidate, 2.
 * - It then enters a loop, incrementing a counter `n` and checking if `n` is prime using the `isPrime` function.
 * - If `n` is found to be prime, it is added to the list of primes.
 * - The loop continues until the list contains NUM_PRIMES prime numbers.
 *
 * ## Example Usage:
 * ```kotlin
 * val firstPrimes = primes()
 * println(firstPrimes) // Outputs the first NUM_PRIMES prime numbers
 * ```
 *
 * In this example, `primes` is called to generate a list of the first NUM_PRIMES prime numbers.
 *
 * @return A list containing the first NUM_PRIMES prime numbers.
 */
fun primes(): List<Int> {
    val primes = mutableListOf<Int>()
    var n = 2
    while (primes.size < NUM_PRIMES) {
        if (isPrime(n)) {
            primes += n
        }
        n++
    }
    return primes.filter { it > 0 }
}

/**
 * Determines whether a given number is prime.
 *
 * A prime number is a natural number greater than 1 that has no positive divisors other than 1 and itself.
 * This function checks if the given number `n` is a prime number. The check is performed using several
 * steps to optimize performance:
 *
 * 1. **Initial Conditions**: It first checks if `n` is less than or equal to 1 or an even number greater than 2.
 *    In these cases, `n` is not prime.
 * 2. **Trial Division**: For other numbers, the function performs a trial division by checking divisibility
 *    of `n` with all odd numbers from 3 to the square root of `n`. If any of these numbers divide `n` evenly,
 *    `n` is not prime.
 *
 * The use of the square root of `n` as the upper limit for the loop significantly improves performance,
 * especially for larger numbers, by reducing the number of divisibility checks.
 *
 * ## Example Usage:
 * ```kotlin
 * val number = 29
 * val isNumberPrime = isPrime(number)
 * println("Is $number prime? $isNumberPrime") // Output: Is 29 prime? true
 * ```
 *
 * In this example, the function `isPrime` is used to check if 29 is a prime number, which it is.
 *
 *
 * @param n The number to be checked for primality.
 * @return `true` if `n` is a prime number; `false` otherwise.
 */
fun isPrime(n: Int) = if (n <= 1) {
    false
} else {
    (2..sqrt(n.toDouble()).toInt()).none { n % it == 0 }
}
