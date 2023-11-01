/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.util.listeners.records

import kotlinx.serialization.Serializable

@Serializable
data class PhenotypeRecord(val genotype: String, val fitness: Double) : AbstractRecord()
