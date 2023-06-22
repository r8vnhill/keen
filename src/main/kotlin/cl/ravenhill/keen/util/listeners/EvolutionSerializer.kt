/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import java.io.File


class EvolutionSerializer<DNA, G : Gene<DNA, G>>(val outputFile: File) :
        AbstractEvolutionListener<DNA, G>() {
    constructor(outputFilePath: String) : this(File(outputFilePath))

    override fun onEvolutionEnded() {

    }
}