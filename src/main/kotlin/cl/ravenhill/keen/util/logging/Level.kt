package cl.ravenhill.keen.util.logging

sealed interface Level {
    /** Returns an empty string.  */
    fun trace(message: () -> String) = ""

    /** Returns an empty string.  */
    fun debug(message: () -> String) = ""

    /** Returns an empty string.  */
    fun info(message: () -> String) = ""

    /** Returns an empty string.  */
    fun warn(message: () -> String) = ""

    /** Returns an empty string.  */
    fun error(message: () -> String) = ""

    /** Returns an empty string.  */
    fun fatal(message: () -> String) = ""

    /** A _trace_ level.  */
    class Trace : Debug() {
        /** Returns the trace message.  */
        override fun trace(message: () -> String) = message()

        override fun toString() = "TRACE"
    }

    /** A _debug_ level.  */
    open class Debug : Info() {
        /** Returns the debug message.  */
        override fun debug(message: () -> String) = message()

        override fun toString() = "DEBUG"
    }

    /** An _info_ level.  */
    open class Info : Warn() {
        /** Returns the info message.  */
        override fun info(message: () -> String) = message()

        override fun toString() = "INFO"
    }

    /** A _warn_ level.  */
    open class Warn : Error() {
        /** Returns the warn message.  */
        override fun warn(message: () -> String) = message()

        override fun toString() = "WARN"
    }

    /** An _error_ level.  */
    open class Error : Fatal() {
        /** Returns the error message.  */
        override fun error(message: () -> String) = message()

        override fun toString() = "ERROR"
    }

    /** A _fatal_ level.  */
    open class Fatal : Level {
        /** Returns the fatal message.  */
        override fun fatal(message: () -> String) = message()

        override fun toString() = "FATAL"

        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }
}