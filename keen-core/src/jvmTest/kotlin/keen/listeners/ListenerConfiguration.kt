package keen.listeners

import cl.ravenhill.arbRanker
import cl.ravenhill.keen.listeners.precision.TimePrecision
import cl.ravenhill.keen.listeners.precision.arbTimePrecision
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.bind
import kotlin.time.TestTimeSource

fun <T, F, R> arbListenerConfiguration(
    rankerArb: Arb<IndividualRanker<T, F, R>> = arbRanker(),
    evolutionArb: Arb<EvolutionRecord<T, F, R>>,
    precisionArb: Arb<TimePrecision> = arbTimePrecision()
) where F : Feature<T, F>,
        R : Representation<T, F> = arbitrary {
    Arb.bind(rankerArb, evolutionArb, precisionArb) { ranker, evolution, precision ->
        ListenerConfiguration(
            ranker = ranker,
            evolution = evolution,
            timeSource = TestTimeSource(),
            precision = precision
        )
    }.bind()
}
