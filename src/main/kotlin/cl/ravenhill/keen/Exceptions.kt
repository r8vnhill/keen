package cl.ravenhill.keen

open class KeenException(prefix: String, lazyMessage: () -> String) :
        Exception("$prefix ${lazyMessage()}")

class InvalidReceiverException(lazyMessage: () -> String) :
        KeenException("Invalid receiver: ", lazyMessage)

class InvalidArgumentException(lazyMessage: () -> String) :
        KeenException("Invalid argument: ", lazyMessage)

class EngineConfigurationException(lazyMessage: () -> String) :
        KeenException("Engine configuration error:", lazyMessage)

class GenotypeConfigurationException(lazyMessage: () -> String) :
        KeenException("Genotype configuration error: ", lazyMessage)

class LimitConfigurationException(lazyMessage: () -> String) :
        KeenException("Genotype configuration error:", lazyMessage)