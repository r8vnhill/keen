/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.kuro

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll


class BufferedOutputChannelSpec : FreeSpec({
    "BufferedOutputChannel should" - {
        "write to buffer" {
            // Generates a list of 0 to 1000 strings
            checkAll(Arb.string(1, 1000).chunked(0, 100)) { messages ->
                val bufferedOutputChannel = BufferedOutputChannel()
                messages.forEach { bufferedOutputChannel.write(it) }
                bufferedOutputChannel.toString() shouldBe messages.joinToString("")
            }
        }

        "clear buffer" {
            // Generates a list of 0 to 1000 strings
            checkAll(Arb.string(1, 1000).chunked(0, 100)) { messages ->
                val bufferedOutputChannel = BufferedOutputChannel()
                messages.forEach { bufferedOutputChannel.write(it) }
                bufferedOutputChannel.clear()
                bufferedOutputChannel.toString() shouldBe ""
            }
        }

        "return a failure when trying to add a child" {
            `check that trying to add a child channel returns a failure` {
                BufferedOutputChannel()
            }
        }
    }
})