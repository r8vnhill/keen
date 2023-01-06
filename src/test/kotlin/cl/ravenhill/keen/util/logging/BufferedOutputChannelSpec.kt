package cl.ravenhill.keen.util.logging

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll


class BufferedOutputChannelSpec : WordSpec({
    "BufferedOutputChannel" should {
        "write to buffer" {
            // Generates a list of 0 to 1000 strings
            checkAll(Arb.string().chunked(0, 1000)) { messages ->
                val bufferedOutputChannel = BufferedOutputChannel()
                messages.forEach { bufferedOutputChannel.write(it) }
                bufferedOutputChannel.toString() shouldBe messages.joinToString("")
            }
        }
        "clear buffer" {
            // Generates a list of 0 to 1000 strings
            checkAll(Arb.string().chunked(0, 1000)) { messages ->
                val bufferedOutputChannel = BufferedOutputChannel()
                messages.forEach { bufferedOutputChannel.write(it) }
                bufferedOutputChannel.clear()
                bufferedOutputChannel.toString() shouldBe ""
            }
        }
    }
})