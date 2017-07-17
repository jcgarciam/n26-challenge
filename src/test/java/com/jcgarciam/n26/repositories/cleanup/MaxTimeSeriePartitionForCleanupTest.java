package com.jcgarciam.n26.repositories.cleanup;

import com.jcgarciam.n26.repositories.TimeSeriePartitionEnum;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

/**
 * MaxTimeSeriePartitionForCleanupTest
 */
public class MaxTimeSeriePartitionForCleanupTest {

    @Test
    public void verifyProducerValueMatchEnum() {
        Assert.assertThat(new MaxTimeSeriePartitionForCleanupImpl().getPartition(), Is.is(TimeSeriePartitionEnum.LAST_120_SECONDS));
    }

}
