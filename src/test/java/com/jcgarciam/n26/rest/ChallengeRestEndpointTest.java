package com.jcgarciam.n26.rest;

import com.jcgarciam.n26.domain.StatisticCounter;
import com.jcgarciam.n26.dto.Transaction;
import com.jcgarciam.n26.repositories.TimeSeriePartitionEnum;
import com.jcgarciam.n26.repositories.TimeSeriesRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletResponse;

/**
 * ChallengeRestEndpointTest
 */
public class ChallengeRestEndpointTest {

    HttpServletResponse mockHttpResponse;
    ChallengeRestEndpoint restEndpoint;
    TimeSeriesRepository mockTimeRepository;

    @Before
    public void setup(){
        mockHttpResponse = Mockito.mock(HttpServletResponse.class);
        mockTimeRepository = Mockito.mock(TimeSeriesRepository.class);
        restEndpoint = new ChallengeRestEndpoint(mockTimeRepository);
    }
    @Test
    public void registrationReturn400WhenTransactionIsNull() {
        //when
        restEndpoint.registerTransaction(null, mockHttpResponse);

        //then
        Mockito.verify(mockHttpResponse, Mockito.times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void registrationReturn204WhenTimestampIsBiggerThan60Seconds(){
        //given
        Transaction trx = new Transaction();
        trx.setAmount(200);
        trx.setTimestamp(TimeSeriePartitionEnum.LAST_120_SECONDS.toMillis());

        //when
        restEndpoint.registerTransaction(trx, mockHttpResponse);

        //then
        Mockito.verify(mockHttpResponse, Mockito.times(1)).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    public void registrationReturn200WhenTimestampIsBelowThan60Seconds(){
        //given
        Transaction trx = new Transaction();
        trx.setAmount(200);
        trx.setTimestamp(TimeSeriePartitionEnum.LAST_15_SECONDS.toMillis());

        //when
        restEndpoint.registerTransaction(trx, mockHttpResponse);

        //then
        Mockito.verify(mockHttpResponse, Mockito.times(1)).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void verifyStatisticFor60SecondsIsCall(){
        Mockito.when(mockTimeRepository.get(TimeSeriePartitionEnum.LAST_60_SECONDS)).thenReturn(new StatisticCounter());
        restEndpoint.getStatistics();
        Mockito.verify(mockTimeRepository, Mockito.times(1)).get(TimeSeriePartitionEnum.LAST_60_SECONDS);
    }
}
