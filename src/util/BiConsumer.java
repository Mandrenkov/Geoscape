package util;

import java.util.Objects;

/**
 * The BiConsumer interface is an adaption of the java.util.function.Consumer
 * interface that accepts two parameters.
 */
@FunctionalInterface
interface BiConsumer<T, U> {
    /**
     * This function carries the same semantic spirit as {@link java.util.function.Consumer#accept(Object)}.
     */
    void accept(T t, U u);

    /**
     * This function carries the same semantic spirit as {@link java.util.function.Consumer#andThen(java.util.function.Consumer)}.
     */
    default BiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> after) {
        Objects.requireNonNull(after);
        return (T t, U u) -> {
            this.accept(t, u);
            after.accept(t, u);
        };
    }
}