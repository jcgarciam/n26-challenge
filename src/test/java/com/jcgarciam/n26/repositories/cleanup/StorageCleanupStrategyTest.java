package com.jcgarciam.n26.repositories.cleanup;

import com.jcgarciam.n26.domain.StatisticCounter;
import com.jcgarciam.n26.repositories.InMemoryStorage;
import com.jcgarciam.n26.repositories.TimeSeriePartitionEnum;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * StorageCleanupStrategyTest
 */
public class StorageCleanupStrategyTest {

    @Test
    public void verifyElementsAreRemovedBasedOnThePolicy() {
        //given
        InMemoryStorage storage = new InMemoryStorage();
        ManagedTimer timer = Mockito.mock(ManagedTimer.class);

        MaxTimeSeriePartitionForCleanup mockMaxCleanup = Mockito.mock(MaxTimeSeriePartitionForCleanup.class);
        Mockito.when(mockMaxCleanup.getPartition()).thenReturn(TimeSeriePartitionEnum.LAST_15_SECONDS);

        StorageCleanupStrategy strategy = new StorageCleanupStrategy(storage, timer, mockMaxCleanup);
        strategy.start();

        //when
        storage.computeIfAbsent(TimeSeriePartitionEnum.LAST_30_SECONDS.toMillis() / 1000, x -> new StatisticCounter()).update(200);
        strategy.run();

        //then
        Assert.assertThat(storage.size(), Is.is(0));
    }

    @Test
    public void verifyValidElementsRemainAfterCleanupPolicyIsExecuted() {
        //given
        InMemoryStorage storage = new InMemoryStorage();
        ManagedTimer timer = Mockito.mock(ManagedTimer.class);
        MaxTimeSeriePartitionForCleanup mockMaxCleanup = Mockito.mock(MaxTimeSeriePartitionForCleanup.class);
        Mockito.when(mockMaxCleanup.getPartition()).thenReturn(TimeSeriePartitionEnum.LAST_30_SECONDS);

        StorageCleanupStrategy strategy = new StorageCleanupStrategy(storage, timer, mockMaxCleanup);

        //when
        storage.computeIfAbsent(TimeSeriePartitionEnum.LAST_15_SECONDS.toMillis() / 1000, x -> new StatisticCounter()).update(200);
        storage.computeIfAbsent(TimeSeriePartitionEnum.LAST_30_SECONDS.toMillis() / 1000, x -> new StatisticCounter()).update(200);
        storage.computeIfAbsent(TimeSeriePartitionEnum.LAST_60_SECONDS.toMillis() / 1000, x -> new StatisticCounter()).update(200);
        strategy.run();

        //then
        Assert.assertThat(storage.size(), Is.is(2));
    }
}
