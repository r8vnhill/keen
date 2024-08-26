package cl.ravenhill.keen.repr

interface RepresentationFactory<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
    fun make(): R
}
