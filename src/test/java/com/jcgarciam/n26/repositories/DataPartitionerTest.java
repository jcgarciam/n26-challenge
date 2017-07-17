package com.jcgarciam.n26.repositories;

import com.jcgarciam.n26.domain.StatisticCounter;
import com.jcgarciam.n26.dto.Transaction;
import com.jcgarciam.n26.repositories.cleanup.ManagedTimer;
import com.jcgarciam.n26.repositories.cleanup.StorageCleanupStrategy;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

/**
 * DataPartitionerTest
 */
public class DataPartitionerTest {

    @Test
    public void transactionAvailableOn60SecondsPartition(){
        //Given
        InMemoryStorage storage = new InMemoryStorage();
        ManagedTimer timer = Mockito.mock(ManagedTimer.class);
        StorageCleanupStrategy cleanupStrategy = Mockito.mock(StorageCleanupStrategy.class);

        DataPartitioner partitioner = new DataPartitioner(storage, timer);
        TimeSerieRepositoryImpl repository = new TimeSerieRepositoryImpl(cleanupStrategy, storage, partitioner);

        //when
        {//transaction in the 60seconds window
            Transaction trx = new Transaction();
            trx.setAmount(1);
            trx.setTimestamp(System.currentTimeMillis());
            repository.add(trx);
        }

        {//transaction in the 60seconds window
            Transaction trx = new Transaction();
            trx.setAmount(1);
            trx.setTimestamp(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(10));
            repository.add(trx);
        }

        {//transaction too old
            Transaction trx = new Transaction();
            trx.setAmount(1);
            trx.setTimestamp(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(100));
            repository.add(trx);
        }
        //then
        StatisticCounter statisticCounter = partitioner.byPartition(TimeSeriePartitionEnum.LAST_60_SECONDS);
        Assert.assertThat(statisticCounter.getCount(), Is.is(2));
    }

    @Test
    public void noTransactionOnLast60SecondsPartition(){
        //Given
        InMemoryStorage storage = new InMemoryStorage();
        ManagedTimer timer = Mockito.mock(ManagedTimer.class);
        StorageCleanupStrategy cleanupStrategy = Mockito.mock(StorageCleanupStrategy.class);

        DataPartitioner partitioner = new DataPartitioner(storage, timer);
        TimeSerieRepositoryImpl repository = new TimeSerieRepositoryImpl(cleanupStrategy, storage, partitioner);

        //when
        {
            Transaction trx = new Transaction();
            trx.setAmount(1);
            trx.setTimestamp(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(100));
            repository.add(trx);
        }

        {
            Transaction trx = new Transaction();
            trx.setAmount(1);
            trx.setTimestamp(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(120));
            repository.add(trx);
        }
        //then
        StatisticCounter statisticCounter = partitioner.byPartition(TimeSeriePartitionEnum.LAST_60_SECONDS);
        Assert.assertThat(statisticCounter.getCount(), Is.is(0));
    }
}
