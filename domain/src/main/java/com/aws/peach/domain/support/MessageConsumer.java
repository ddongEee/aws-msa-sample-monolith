package com.aws.peach.domain.support;

public interface MessageConsumer<V> {
    void consume(V value);
}
