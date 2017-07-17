package com.jcgarciam.n26.repositories.cleanup;

import com.jcgarciam.n26.repositories.TimeSeriePartitionEnum;
import org.springframework.stereotype.Component;

/**
 * MaxTimeSeriePartitionForCleanupImpl
 */
@Component
public class MaxTimeSeriePartitionForCleanupImpl implements MaxTimeSeriePartitionForCleanup {

    @Override
    public TimeSeriePartitionEnum getPartition() {
        return TimeSeriePartitionEnum.LAST_120_SECONDS;
    }
}
