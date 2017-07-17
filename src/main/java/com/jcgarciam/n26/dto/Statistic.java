package com.jcgarciam.n26.dto;

import com.jcgarciam.n26.domain.StatisticCounter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Statistics
 */
public class Statistic {
    private double sum;
    private double max;
    private double min;
    private double avg;
    private int count;

    public Statistic(final StatisticCounter counter) {
        this.sum = counter.getSum();
        this.max = counter.getMax();
        this.min = counter.getMin();
        this.count = counter.getCount();
        this.avg = counter.getAvg();
    }

    public double getSum() {
        return round(this.sum);
    }

    public double getAvg() {
        return round(this.avg);
    }

    public double getMax() {
        return round(this.max);
    }

    public double getMin() {
        return round(this.min);
    }

    private double round(double min) {
        BigDecimal bd = new BigDecimal(min);
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public int getCount() {
        return this.count;
    }

}