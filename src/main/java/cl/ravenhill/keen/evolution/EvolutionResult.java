package cl.ravenhill.keen.evolution;

import cl.ravenhill.keen.genetic.Phenotype;
import cl.ravenhill.keen.util.math.MinMax;
import cl.ravenhill.keen.util.optimizer.Optimizer;
import jdk.jshell.spi.ExecutionControl;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collector;

public final class EvolutionResult<DNA> implements Comparable<EvolutionResult<DNA>> {

  private Optimizer _optimize;
  private List<Phenotype<DNA>> population;

  public EvolutionStart<DNA> next() {
    return null;
  }

  private Phenotype<DNA> _best;

  public Phenotype<DNA> bestPhenotype() {
    return _best;
  }

  @Override
  public int compareTo(@NotNull final EvolutionResult<DNA> other) {
    return 0;
  }


  public static <G> Collector<EvolutionResult<G>, ?, Phenotype<G>> toBestPhenotype() {
    return Collector.of(
        MinMax::of,
        MinMax::accept,
        MinMax::combine,
        (MinMax<EvolutionResult<G>> mm) -> mm.max() != null
                                           ? mm.max().bestPhenotype()
                                           : null
    );
  }
}
