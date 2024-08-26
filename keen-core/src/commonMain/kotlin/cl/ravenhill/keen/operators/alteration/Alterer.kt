package cl.ravenhill.keen.operators.alteration

import cl.ravenhill.keen.operators.Operator
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

interface Alterer<T, F, R> : Operator<T, F, R> where F : Feature<T, F>, R : Representation<T, F>
