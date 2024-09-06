//[examples](../../index.md)/[[root]](index.md)/[[jvm]main]([jvm]main.md)

# main

[jvm]\
fun [main]([jvm]main.md)()

Entry point for running the OneMax genetic algorithm.

#### See also

| |
|---|
| [oneMax](one-max.md) |

[jvm]\
fun [main]([jvm]main.md)()

Entry point for running the [oneMaxMinimal](one-max-minimal.md) genetic algorithm example.

The `main` function serves as the entry point for the application. It uses Kotlin's runBlocking coroutine builder to execute the `oneMaxMinimal` function, which runs a minimal implementation of the OneMax genetic algorithm. This example demonstrates how to execute a genetic algorithm using the Keen framework in a blocking context.

## Notes:

- 
   The use of `runBlocking` ensures that the main function waits for the completion of the `oneMaxMinimal` coroutine before exiting.
- 
   This setup is typical for applications that are not running in a purely asynchronous environment and require the main thread to wait for the completion of coroutines.
