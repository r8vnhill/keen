package cl.ravenhill.keen.genetic.genes


interface NumberGene<DNA: Number> : Gene<DNA> {
    fun mean(gene: NumberGene<DNA>): NumberGene<DNA>
}