/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.logging

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
import io.kotest.property.checkAll


class CompositeOutputChannelTest : FreeSpec({
    "CompositeOutputChannel should" - {
        "have an empty list of output channels by default" {
            CompositeOutputChannel().outputChannels.shouldBeEmpty()
        }

        "be able to add an output channel" {
            checkAll(
                Arb.compositeOutputChannel(),
                Arb.outputChannel()
            ) { compositeChannel, childChannel ->
                val size = compositeChannel.outputChannels.size
                compositeChannel.add(childChannel).shouldBeSuccess()
                compositeChannel.outputChannels shouldContain childChannel
                compositeChannel.outputChannels.size shouldBe size + 1
            }
        }

        "write to all output channels" {
            checkAll(
                Arb.list(Arb.bufferedOutputChannel()),
                Arb.string()
            ) { childChannels, message ->
                assume { message.shouldNotBeBlank() }
                val compositeChannel = CompositeOutputChannel()
                childChannels.forEach { compositeChannel.add(it) }
                compositeChannel.write(message)
                childChannels.forEach { it.toString() shouldContain message }
            }
        }
    }
})