package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.features.Feature

interface PhaseListener<T, F> where F : Feature<T, F>
