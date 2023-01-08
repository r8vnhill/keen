package cl.ravenhill.keen.util.logging

import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
import io.kotest.property.checkAll


class StdOutputChannelSpec : WordSpec({
    "StdOutputChannel" should {
        "write to stdout" {
            // Generates a list of 1 to 1000 strings
            checkAll(Arb.string(1, 1000).chunked(1, 100)) { messages ->
                assume(messages.none { it.isBlank() })
                val stdoutStream = StdoutChannel()
                val out = tapSystemOut { messages.forEach { stdoutStream.write(it) } }
                val expected = tapSystemOut { messages.forEach { println(it) } }
                out shouldBe expected
            }
        }
    }
})