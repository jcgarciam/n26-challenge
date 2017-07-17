package com.jcgarciam.n26.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * StatisticCounter
 */
public class StatisticCounter {

    private double sum;
    private double max;
    private double min;
    private int count;

    private StatisticCounter(final double sumValue, final double maxValue, final double minValue,
            final int countValue) {
        this.sum = sumValue;
        this.max = maxValue;
        this.min = minValue;
        this.count = countValue;
    }

    public StatisticCounter() {
        this.max = Double.MIN_VALUE;
        this.min = Double.MAX_VALUE;
    }

    public double getSum() {
        return this.sum;
    }

    /**
     * Get the avg value using 4 decimal points
     */
    public double getAvg() {
        if (this.count == 0) {
            return 0;
        }
        BigDecimal bd = new BigDecimal(this.sum / this.count);
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getMax() {
        return this.max;
    }

    public double getMin() {
        return this.min;
    }

    public int getCount() {
        return this.count;
    }

    /**
     * Update all internal values (sum/min/max/count)
     */
    public synchronized void update(final double value) {
        this.sum += value;
        this.min = Math.min(min, value);
        this.max = Math.max(max, value);
        this.count += 1;
    }

    /**
     * Update all internal values (sum/min/max/count)
     */
    public synchronized void update(final StatisticCounter aggregated) {
        this.sum += aggregated.getSum();
        this.min = Math.min(min, aggregated.getMin());
        this.max = Math.max(max, aggregated.getMax());
        this.count += aggregated.getCount();
    }

    /**
     * Create a copy of the current object.
     */
    public synchronized StatisticCounter copy() {
        StatisticCounter copied = new StatisticCounter(this.sum, this.max, this.min, this.count);
        return copied;
    }

    @Override
    public String toString() {
        return "StatisticCounter{" +
                "sum=" + sum +
                ", max=" + max +
                ", min=" + min +
                ", count=" + count +
                '}';
    }
}