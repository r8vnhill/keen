package cl.ravenhill.keen.exceptions


class InvalidReceiverException(lazyMessage: () -> String) :
    KeenException("Invalid receiver: ", lazyMessage)