/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.kuro

import cl.ravenhill.keen.shouldBeOfClass
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
import io.kotest.property.checkAll

@OptIn(ExperimentalKotest::class)
class CompositeOutputChannelTest : FreeSpec({
    "CompositeOutputChannel should" - {
        "have an empty list of output channels by default" {
            CompositeOutputChannel().outputChannels.shouldBeEmpty()
        }

        "be able to add an output channel" {
            checkAll(
                PropTestConfig(iterations = 20),
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

        "have an iterator that iterates over the output channels" {
            checkAll(Arb.compositeOutputChannel(1..10)) { composite ->
                composite.iterator().asSequence().toList().forEachIndexed { index, outputChannel ->
                    outputChannel shouldBeOfClass composite[index]::class
                }
            }
        }
    }
})
