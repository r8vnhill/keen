package cl.ravenhill.keen.util.math;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public final class MinMax<C> implements Consumer<C> {

  private final Comparator<? super C> _comparator;

  private C _min;
  private C _max;
  private long _count = 0L;

  private MinMax(final Comparator<? super C> comparator) {
    _comparator = requireNonNull(comparator);
  }

  @Override
  public void accept(final C object) {
    _min = min(_comparator, _min, object);
    _max = max(_comparator, _max, object);
    ++_count;
  }

  @Contract("_ -> this")
  public MinMax<C> combine(@NotNull final MinMax<C> other) {
    _min = min(_comparator, _min, other._min);
    _max = max(_comparator, _max, other._max);
    _count += other._count;

    return this;
  }

  public long count() {
    return _count;
  }

  public C max() {
    return _max;
  }

  @Override
  public String toString() {
    return format("MinMax[count=%d, min=%s, max=%s]", _count, _min, _max);
  }

  public static <T> T min(final Comparator<? super T> comp, final T a, final T b) {
    return a != null ? b != null ? comp.compare(a, b) <= 0 ? a : b : a : b;
  }

  public static <T> T max(final Comparator<? super T> comp, final T a, final T b) {
    return a != null ? b != null ? comp.compare(a, b) >= 0 ? a : b : a : b;
  }

  @NotNull
  @Contract(value = "_ -> new", pure = true)
  public static <T> MinMax<T> of(final Comparator<? super T> comparator) {
    return new MinMax<>(comparator);
  }

  @NotNull
  @Contract(" -> new")
  public static <C extends Comparable<? super C>> MinMax<C> of() {
    return of(Comparator.naturalOrder());
  }
}
