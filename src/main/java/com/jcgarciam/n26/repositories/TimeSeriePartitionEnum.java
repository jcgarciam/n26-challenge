package com.jcgarciam.n26.repositories;

import java.util.concurrent.TimeUnit;

/**
 * Define all available partitions to aggregate
 */
public enum TimeSeriePartitionEnum {
    LAST_15_SECONDS(15),
    LAST_30_SECONDS(30),
    LAST_60_SECONDS(60),
    LAST_120_SECONDS (120);

    private int seconds;
    TimeSeriePartitionEnum(final int seconds){
        this.seconds = seconds;
    }
    public int getSeconds(){
        return seconds;
    }
    public long toMillis(){
        return System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(seconds);
    }
}