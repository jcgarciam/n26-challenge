package com.jcgarciam.n26.repositories;

/**
 * TimeSeriesRepository
 */
public interface TimeSeriesRepository<T, K> {
    void add(final T data);
    K get(final TimeSeriePartitionEnum partition);
}