package com.jcgarciam.n26.repositories;

import com.jcgarciam.n26.domain.StatisticCounter;
import com.jcgarciam.n26.dto.Transaction;
import com.jcgarciam.n26.repositories.cleanup.StorageCleanupStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * TimeSerieRepository with an internal CleanupStrategy to prevent OOM
 */
@Repository
public class TimeSerieRepositoryImpl implements TimeSeriesRepository<Transaction, StatisticCounter> {

    /**
     * repositories represent a time serie of aggregated counters.
     */
    private final InMemoryStorage storage;
    private DataPartitioner dataPartitioner;

    private final StorageCleanupStrategy storageCleanupStrategy;

    @Autowired
    public TimeSerieRepositoryImpl(StorageCleanupStrategy storageCleanupStrategy, InMemoryStorage inMemoryStorage, DataPartitioner dataPartitioner) {
        this.storageCleanupStrategy = storageCleanupStrategy;
        this.storage = inMemoryStorage;
        this.dataPartitioner = dataPartitioner;
        this.storageCleanupStrategy.start();
    }

    /**
     * Adds a transaction to the internal storage and recalculate the aggregate for the {@link TimeSeriePartitionEnum}
     * @param data
     */
    public void add(final Transaction data) {
        //As timestamp is specified in milliseconds We aggregate them by seconds.
        final Long point = data.getTimestamp() / 1000;
        storage.computeIfAbsent(point, i -> new StatisticCounter()).update(data.getAmount());

        //try to update the partition on this round.
        dataPartitioner.update();
    }

    /**
     * Get the statistic counter for a given partition, O(1)
     */
    public StatisticCounter get(final TimeSeriePartitionEnum partition) {
        return this.dataPartitioner.byPartition(partition).copy();
    }
}