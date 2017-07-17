package com.jcgarciam.n26.repositories.cleanup;

import com.jcgarciam.n26.repositories.TimeSeriePartitionEnum;

/**
 * MaxTimeSeriePartitionForCleanup interface to identify the MaxTime for Cleanup
 */
public interface MaxTimeSeriePartitionForCleanup {
    TimeSeriePartitionEnum getPartition();
}
