//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.summary](../index.md)/[EvolutionSummary](index.md)

# EvolutionSummary

class [EvolutionSummary](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; : [EvolutionListener](../../cl.ravenhill.keen.listeners/-evolution-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; , [GenerationListener](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; , [InitializationListener](../../cl.ravenhill.keen.listeners.mixins/-initialization-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; , [EvaluationListener](../../cl.ravenhill.keen.listeners.mixins/-evaluation-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; , [ParentSelectionListener](../../cl.ravenhill.keen.listeners.mixins/-parent-selection-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; , [SurvivorSelectorListener](../../cl.ravenhill.keen.listeners.mixins/-survivor-selector-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; , [AlterationListener](../../cl.ravenhill.keen.listeners.mixins/-alteration-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; 

A comprehensive listener that summarizes the evolution process, including initialization, evaluation, selection, and alteration phases.

The `EvolutionSummary` class aggregates and summarizes key metrics and times for each phase of the evolutionary algorithm. It implements various listeners for different stages of the algorithm and consolidates their data into a single summary. The summary is displayed as a formatted table, showing statistics like time taken for initialization, evaluation, selection, and alterations, as well as the best fitness and steady generations.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md)<br>Companion object to provide a factory function for creating an `EvolutionSummary` listener. |

## Functions

| Name | Summary |
|---|---|
| [copy](copy.md) | [common]<br>open override fun [copy](copy.md)(): [EvolutionSummary](index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;<br>Creates a copy of the `EvolutionSummary` with the same configuration. |
| [display](display.md) | [common]<br>open suspend override fun [display](display.md)()<br>Displays a summary of the evolution process, including times for each stage and results. |
| [onAlterationEnd](../../cl.ravenhill.keen.listeners.mixins/-alteration-listener/on-alteration-end.md) | [common]<br>open override fun [onAlterationEnd](../../cl.ravenhill.keen.listeners.mixins/-alteration-listener/on-alteration-end.md)(state: [S](index.md))<br>Called at the end of the alteration phase in the evolutionary process. |
| [onAlterationStart](../../cl.ravenhill.keen.listeners.mixins/-alteration-listener/on-alteration-start.md) | [common]<br>open override fun [onAlterationStart](../../cl.ravenhill.keen.listeners.mixins/-alteration-listener/on-alteration-start.md)(state: [S](index.md))<br>Called at the start of the alteration phase in the evolutionary process. |
| [onEvaluationEnd](../../cl.ravenhill.keen.listeners.mixins/-evaluation-listener/on-evaluation-end.md) | [common]<br>open override fun [onEvaluationEnd](../../cl.ravenhill.keen.listeners.mixins/-evaluation-listener/on-evaluation-end.md)(state: [S](index.md))<br>Called when the evaluation process ends. |
| [onEvaluationStart](../../cl.ravenhill.keen.listeners.mixins/-evaluation-listener/on-evaluation-start.md) | [common]<br>open override fun [onEvaluationStart](../../cl.ravenhill.keen.listeners.mixins/-evaluation-listener/on-evaluation-start.md)(state: [S](index.md))<br>Called when the evaluation process starts. |
| [onEvolutionEnd](on-evolution-end.md) | [common]<br>open override fun [onEvolutionEnd](on-evolution-end.md)(state: [S](index.md))<br>Triggered at the end of the evolutionary process, recording the total duration. |
| [onEvolutionStart](on-evolution-start.md) | [common]<br>open override fun [onEvolutionStart](on-evolution-start.md)()<br>Triggered at the start of the evolutionary process. |
| [onGenerationEnd](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/on-generation-end.md) | [common]<br>open suspend override fun [onGenerationEnd](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/on-generation-end.md)(state: [S](index.md))<br>Called at the end of each generation. |
| [onGenerationStart](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/on-generation-start.md) | [common]<br>open override fun [onGenerationStart](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/on-generation-start.md)(state: [S](index.md))<br>Called at the start of each generation. |
| [onInitializationEnd](../../cl.ravenhill.keen.listeners.mixins/-initialization-listener/on-initialization-end.md) | [common]<br>open override fun [onInitializationEnd](../../cl.ravenhill.keen.listeners.mixins/-initialization-listener/on-initialization-end.md)(state: [S](index.md))<br>Called when the initialization process ends. |
| [onInitializationStart](../../cl.ravenhill.keen.listeners.mixins/-initialization-listener/on-initialization-start.md) | [common]<br>open override fun [onInitializationStart](../../cl.ravenhill.keen.listeners.mixins/-initialization-listener/on-initialization-start.md)(state: [S](index.md))<br>Called when the initialization process starts. |
| [onParentSelectionEnd](../../cl.ravenhill.keen.listeners.mixins/-parent-selection-listener/on-parent-selection-end.md) | [common]<br>open override fun [onParentSelectionEnd](../../cl.ravenhill.keen.listeners.mixins/-parent-selection-listener/on-parent-selection-end.md)(state: [S](index.md))<br>Called at the end of the parent selection process. |
| [onParentSelectionStart](../../cl.ravenhill.keen.listeners.mixins/-parent-selection-listener/on-parent-selection-start.md) | [common]<br>open override fun [onParentSelectionStart](../../cl.ravenhill.keen.listeners.mixins/-parent-selection-listener/on-parent-selection-start.md)(state: [S](index.md))<br>Called at the start of the parent selection process. |
| [onSurvivorSelectionEnd](../../cl.ravenhill.keen.listeners.mixins/-survivor-selector-listener/on-survivor-selection-end.md) | [common]<br>open override fun [onSurvivorSelectionEnd](../../cl.ravenhill.keen.listeners.mixins/-survivor-selector-listener/on-survivor-selection-end.md)(state: [S](index.md))<br>Called at the end of the survivor selection process. |
| [onSurvivorSelectionStart](../../cl.ravenhill.keen.listeners.mixins/-survivor-selector-listener/on-survivor-selection-start.md) | [common]<br>open override fun [onSurvivorSelectionStart](../../cl.ravenhill.keen.listeners.mixins/-survivor-selector-listener/on-survivor-selection-start.md)(state: [S](index.md))<br>Called at the start of the survivor selection process. |
