package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.genetic.genes.Gene

interface PhaseListener<T, U> where U : Gene<T, U>
