/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.construction

/**
 * The number of available processors on the current system.
 *
 * The `numProcessors` property retrieves the number of processors (or CPU cores) that are available to the Java
 * Virtual Machine (JVM) on the current system. This value is often used to optimize the performance of concurrent
 * and parallel tasks by determining the appropriate level of parallelism.
 *
 * @return The number of available processors on the current system.
 */
internal actual val numProcessors: Int = Runtime.getRuntime().availableProcessors()
