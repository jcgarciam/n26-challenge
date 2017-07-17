package com.jcgarciam.n26.rest;

import com.jcgarciam.n26.domain.StatisticCounter;
import com.jcgarciam.n26.dto.Statistic;
import com.jcgarciam.n26.dto.Transaction;
import com.jcgarciam.n26.repositories.TimeSeriePartitionEnum;
import com.jcgarciam.n26.repositories.TimeSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * ChallengeRestEndpoint
 */
@RestController
public class ChallengeRestEndpoint {


    private TimeSeriesRepository<Transaction, StatisticCounter> timeSeriesRepository;

    @Autowired
    public ChallengeRestEndpoint(TimeSeriesRepository<Transaction, StatisticCounter> timeSeriesRepository) {
        this.timeSeriesRepository = timeSeriesRepository;
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    public void registerTransaction(@RequestBody final Transaction model, final HttpServletResponse response) {
        if (model == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (model.getTimestamp() > TimeSeriePartitionEnum.LAST_60_SECONDS.toMillis()) {
            timeSeriesRepository.add(model);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    @RequestMapping("/statistics")
    public Statistic getStatistics() {
        return new Statistic(timeSeriesRepository.get(TimeSeriePartitionEnum.LAST_60_SECONDS));
    }

    @RequestMapping("/statistics/15sec")
    public Statistic getStatistics15Seconds() {
        return new Statistic(timeSeriesRepository.get(TimeSeriePartitionEnum.LAST_15_SECONDS));
    }

    @RequestMapping("/statistics/30sec")
    public Statistic getStatistics30Seconds() {
        return new Statistic(timeSeriesRepository.get(TimeSeriePartitionEnum.LAST_30_SECONDS));
    }
}