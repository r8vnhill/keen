/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.listeners.records

import kotlinx.serialization.Serializable

@Serializable
data class PhenotypeRecord(val genotype: String, val fitness: Double) : AbstractRecord()
