# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - 2023-12-23

### Added
- **Travelling Salesman Problem Implementation using Genetic Algorithm**

### Changed
- **`EvolutionPlotter` Open Class**
  - **Changes**: The `EvolutionPlotter` class has been made open.
  - **Rationale**: This change allows for the extension of the `EvolutionPlotter` class, enabling the creation of 
  - custom plotting logic for evolutionary computation problems.
  - **File Affected**: `EvolutionPlotter.kt`
  
## [1.0.3] - 2023-12-22

### Added

- **Zero-One Knapsack Problem Implementation using Genetic Algorithm**
  - **Files Added**:
    - `MainZeroOne.kt`: Contains the main logic and execution flow for the Zero-One Knapsack Problem using genetic algorithms.
    - `ZeroOneKnapsackProblem.kt`: Includes detailed functions and documentation related to the implementation of evolutionary computation for the Zero-One Knapsack Problem.
  - **Overview**: This update introduces a comprehensive implementation of the Zero-One Knapsack Problem, leveraging the principles of genetic algorithms. It expands the framework's capability in solving classical optimization problems using evolutionary computation techniques.
  - **Package Update**: The existing package structure, particularly those related to the Unbounded Knapsack Problem, has been updated to accommodate and integrate the new implementation, ensuring consistency and coherence in the framework's overall design and functionality.

## [1.0.2] - 2023-12-22

### Added

- **Unbounded Knapsack Problem Implementation in Kotlin**
  - **Overview**: Implemented a solution for the unbounded knapsack problem as a practical example of using the Keen
    genetic algorithm library in Kotlin. This includes the creation of specific classes and the computation of a
    fitness function tailored for this problem.
  - **Details**: The solution features a custom fitness function, specialized chromosomes and genes designed for the 
    knapsack problem, as well as evolutionary constraints and execution logic integral to the genetic algorithm.

## [1.0.1] - 2023-12-21

### Added
- [EXPERIMENTAL] **UniformCrossover Class in Keen-Core Module** (UniformCrossover.kt)
  - **Overview**: A new class, `UniformCrossover`, has been introduced to the keen-core module. It is implemented in the file `UniformCrossover.kt`.
  - **Functionality**: This class serves as a crossover operator, essential in genetic algorithms. It randomly selects genes from parent chromosomes for generating offspring.
  - **Purpose**: The primary objective of `UniformCrossover` is to enhance genetic diversity within populations during the evolutionary process in genetic algorithms. It achieves this by ensuring a random yet equitable selection of genes from multiple parents.
  - **Usage**: Ideal for applications in genetic algorithms where maintaining a diverse genetic pool is crucial. This class is designed to be both robust and flexible, catering to a wide range of scenarios where random gene selection is desired.

- **ToStringMode Enum in Keen-Core Module** (ToStringMode.kt)
  - **Overview**: A new enum, `ToStringMode`, has been added to the keen-core module, implemented in `ToStringMode.kt`.
  - **Functionality**: This enum provides different modes for converting objects to their string representation, offering flexibility in displaying object information.
  - **Purpose**: The introduction of `ToStringMode` allows for controlled verbosity in object string representations, catering to various requirements like concise summaries, standard details, or in-depth information.
  - **Usage**: Useful in scenarios requiring varied levels of detail in string representations, such as logging, debugging, or displaying data in user interfaces.

### Deprecated
- **MultiStringFormat Interface Methods**
  - **Changes**: The `toSimpleString()` and `toDetailedString()` methods in the `MultiStringFormat` interface have been marked as deprecated.
  - **Rationale**: This change encourages the use of the standard `toString()` method for string representations, aligning with more conventional practices in Kotlin.

### Updated
- **Jakt Version in Gradle Properties**
  - **Details**: The `jakt` version in the `gradle.properties` file has been updated to `1.4.0`.
  - **Impact**: This update ensures that the project is aligned with the latest features, optimizations, and bug fixes available in the `jakt` version `1.4.0`.
  - **File Affected**: `gradle.properties`
