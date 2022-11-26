package cl.ravenhill.keen.exceptions


class InvalidArgumentException(lazyMessage: () -> String) :
    KeenException("Invalid argument: ", lazyMessage)