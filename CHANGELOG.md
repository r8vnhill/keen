# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.0.0] - TBD

### Added
- `CHANGELOG.md` with initial structure for documenting notable changes to the project.
- New interface `PermutationCrossover` in `cl.ravenhill.keen.operators.crossover.permutation` for a structured approach to implementing permutation crossover in genetic algorithms.

### Changed
- Moved utility classes for range and standard output from `cl.ravenhill.utils` to `cl.ravenhill.keen.util` to centralize utility functions within the Keen library.
- Upgraded the `strait-jakt` library dependency to version `1.1.0`.
- Renamed `Match` class to `MatchLimit` to better reflect its purpose as a termination condition for the evolutionary engine. Updated all references in the codebase to the new class name for consistency and improved clarity.
- Modified the `Limit` interface to accept an `Evolver` parameter for broader applicability.

### Removed
- Removed legacy log-related code utilizing the deprecated `Logger` class in preparation for a new logging approach.
- Deleted outdated code pertaining to logging and constraints following recent updates and refactorings.


### Deprecated
- Marked old utility classes as deprecated after the introduction of improved alternatives.

### Refactored
- Refactored constraints in test classes, such as `RangesTest`, to enhance testing robustness.
- Overhauled constraints for greater intuitiveness and alignment with new utility functions.
- Revised failing tests caused by reliance on deprecated code, ensuring all tests are up-to-date with the current codebase.
- Refactored `AbstractPermutationCrossover` class:
  - Introduced a mandatory implementation of the `performPermutationCrossover` method in subclasses.
  - Enhanced the `crossoverChromosomes` method to validate offspring as proper permutations and prevent gene duplication.
