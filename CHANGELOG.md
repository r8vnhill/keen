# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.1.0] - TBD

### Added
- [EXPERIMENTAL] **UniformCrossover Class in Keen-Core Module** (UniformCrossover.kt)
    - **Overview**: A new class, `UniformCrossover`, has been introduced to the keen-core module. It is implemented in the file `UniformCrossover.kt`.
    - **Functionality**: This class serves as a crossover operator, essential in genetic algorithms. It randomly selects genes from parent chromosomes for generating offspring.
    - **Purpose**: The primary objective of `UniformCrossover` is to enhance genetic diversity within populations during the evolutionary process in genetic algorithms. It achieves this by ensuring a random yet equitable selection of genes from multiple parents.
    - **Usage**: Ideal for applications in genetic algorithms where maintaining a diverse genetic pool is crucial. This class is designed to be both robust and flexible, catering to a wide range of scenarios where random gene selection is desired.
