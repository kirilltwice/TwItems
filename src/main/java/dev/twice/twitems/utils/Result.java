package dev.twice.twitems.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@AllArgsConstructor
public final class Result<T> {

    @Getter
    private final T value;

    @Getter
    private final Exception error;

    @Getter
    private final boolean success;

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null, true);
    }

    public static <T> Result<T> failure(Exception error) {
        return new Result<>(null, error, false);
    }

    public static <T> Result<T> of(SupplierWithException<T> supplier) {
        try {
            return success(supplier.get());
        } catch (Exception e) {
            return failure(e);
        }
    }

    public boolean isPresent() {
        return success && value != null;
    }

    public Result<T> onPresent(Consumer<T> consumer) {
        if (isPresent()) {
            consumer.accept(value);
        }
        return this;
    }

    public Result<T> onError(Consumer<Exception> consumer) {
        if (!success && error != null) {
            consumer.accept(error);
        }
        return this;
    }

    public <U> Result<U> map(Function<T, U> mapper) {
        if (isPresent()) {
            try {
                return success(mapper.apply(value));
            } catch (Exception e) {
                return failure(e);
            }
        }
        return failure(error);
    }

    public T orElse(T defaultValue) {
        return isPresent() ? value : defaultValue;
    }

    @FunctionalInterface
    public interface SupplierWithException<T> {
        T get() throws Exception;
    }
}