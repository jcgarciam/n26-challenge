package com.jcgarciam.n26.repositories;

import com.jcgarciam.n26.domain.StatisticCounter;
import com.jcgarciam.n26.repositories.cleanup.ManagedTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * DataPartitioner is the responsible for partitioning the data per TimeSeriePartitionEnum, it also has a refresh rate of 5 secs
 */
@Component
@ApplicationScope
public class DataPartitioner extends TimerTask {
    private final ConcurrentHashMap<TimeSeriePartitionEnum, StatisticCounter> seriePartitions;

    private final AtomicBoolean updating;
    private final InMemoryStorage storage;
    private final ManagedTimer managedTimer;

    @Autowired
    public DataPartitioner(InMemoryStorage storage, ManagedTimer managedTimer){
        this.updating = new AtomicBoolean(false);
        this.storage = storage;
        this.managedTimer = managedTimer;
        this.seriePartitions = new ConcurrentHashMap<>();
        for (TimeSeriePartitionEnum p : TimeSeriePartitionEnum.values()) {
            this.seriePartitions.put(p, new StatisticCounter());
        }

        this.managedTimer.schedule(this,
                TimeUnit.SECONDS.toMillis(TimeSeriePartitionEnum.LAST_15_SECONDS.getSeconds()),
                TimeUnit.SECONDS.toMillis(5));
    }

    public void update(){
        if(!updating.getAndSet(true)){
            try{
                // As ConcurrentSkipListMap operate at O(log(n)) we create small views of the data on each incoming transaction to
                // recalculate the last (15, 30, 60) seconds.
                ConcurrentNavigableMap<Long, StatisticCounter> view15 = storage.tailMap(TimeSeriePartitionEnum.LAST_15_SECONDS.toMillis() / 1000);
                ConcurrentNavigableMap<Long, StatisticCounter> view30 = storage.tailMap(TimeSeriePartitionEnum.LAST_30_SECONDS.toMillis() / 1000);
                ConcurrentNavigableMap<Long, StatisticCounter> view60 = storage.tailMap(TimeSeriePartitionEnum.LAST_60_SECONDS.toMillis() / 1000);

                //aggregate
                {
                    StatisticCounter newCounter = new StatisticCounter();
                    view15.entrySet().stream().forEach(entry -> newCounter.update(entry.getValue()));
                    seriePartitions.put(TimeSeriePartitionEnum.LAST_15_SECONDS, newCounter);
                }

                //aggregate
                {
                    StatisticCounter newCounter = new StatisticCounter();
                    view30.entrySet().stream().forEach(entry -> newCounter.update(entry.getValue()));
                    seriePartitions.put(TimeSeriePartitionEnum.LAST_30_SECONDS, newCounter);
                }

                //aggregate
                {
                    StatisticCounter newCounter = new StatisticCounter();
                    view60.entrySet().stream().forEach(entry -> newCounter.update(entry.getValue()));
                    seriePartitions.put(TimeSeriePartitionEnum.LAST_60_SECONDS, newCounter);
                }

            }finally {
                updating.set(false);
            }
        }
    }

    public StatisticCounter byPartition(TimeSeriePartitionEnum partitionEnum){
        return seriePartitions.get(partitionEnum);
    }

    @Override
    public void run() {
        update();
    }
}
