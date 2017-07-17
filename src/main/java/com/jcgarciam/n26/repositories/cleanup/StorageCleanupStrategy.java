package com.jcgarciam.n26.repositories.cleanup;

import com.jcgarciam.n26.repositories.InMemoryStorage;
import com.jcgarciam.n26.repositories.TimeSeriePartitionEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TimerTask that remove elements which are pass the specified {@link TimeSeriePartitionEnum} from the {@link MaxTimeSeriePartitionForCleanupImpl}
 */

@Component
@ApplicationScope
public class StorageCleanupStrategy extends TimerTask {
    private static final Logger LOGGER = Logger.getLogger(StorageCleanupStrategy.class);
    private final InMemoryStorage storage;
    private final ManagedTimer timer;
    private final MaxTimeSeriePartitionForCleanup partitionForCleanup;
    private final AtomicBoolean running;

    @Autowired
    public StorageCleanupStrategy(final InMemoryStorage inMemoryStorage,
                                  final ManagedTimer managedTimer,
                                  final MaxTimeSeriePartitionForCleanup maxTimeSeriePartitionForCleanup) {
        this.storage = inMemoryStorage;
        this.timer = managedTimer;
        this.partitionForCleanup = maxTimeSeriePartitionForCleanup;
        this.running = new AtomicBoolean(false);
    }

    public void start() {
        if (!running.getAndSet(true)) {
            timer.schedule(this,
                    TimeUnit.SECONDS.toMillis(this.partitionForCleanup.getPartition().getSeconds()),
                    TimeUnit.SECONDS.toMillis(this.partitionForCleanup.getPartition().getSeconds()));
        }
    }

    @Override
    public void run() {
        boolean removed = storage.keySet().removeIf(key -> key < (this.partitionForCleanup.getPartition().toMillis() / 1000));
        LOGGER.info(String.format("Executing cleanup job to remove items past %s, items deleted(%s)",
                this.partitionForCleanup.getPartition().name(), removed));
    }

}
