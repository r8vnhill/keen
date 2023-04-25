package cl.ravenhill.keen.util.logging

import cl.ravenhill.keen.InvalidStateException
import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.result.FailureMatcher
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
import io.kotest.property.checkAll


@OptIn(ExperimentalKotest::class)
class StdOutputChannelSpec : FreeSpec({
    "StdOutputChannel should" - {
        "write to stdout" {
            checkAll(
                PropTestConfig(iterations = 100),
                Arb.string().chunked(1..500)
            ) { messages ->
                assume(messages.none { it.isBlank() })
                val stdoutStream = StdoutChannel()
                val out = tapSystemOut { messages.forEach { stdoutStream.write(it) } }
                val expected = tapSystemOut { messages.forEach { println(it) } }
                out shouldBe expected
            }
        }

        "return a failure when trying to add a child" {
            val stdoutStream = StdoutChannel()
            val child = StdoutChannel()
            stdoutStream.add(child) should FailureMatcher(InvalidStateException(
                "${child::class.simpleName}"
            ) { "Cannot add channel to this channel." })
        }
    }
})