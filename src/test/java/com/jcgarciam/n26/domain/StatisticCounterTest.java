package com.jcgarciam.n26.domain;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

/**
 * StatisticsCounterTest
 */
public class StatisticCounterTest {

    @Test
    public void verifyAllStatisticCounterProperties() {
        //Given
        StatisticCounter counter = new StatisticCounter();

        //when
        counter.update(100);
        counter.update(200);
        counter.update(100);
        counter.update(10);
        counter.update(500);
        counter.update(300);

        //then
        Assert.assertThat("Sum must be 1210", 1210.0, Is.is(counter.getSum()));
        Assert.assertThat("Min must be 10", 10.0, Is.is(counter.getMin()));
        Assert.assertThat("Max must be 500", 500.0, Is.is(counter.getMax()));
        Assert.assertThat("Count must be 6", 6, Is.is(counter.getCount()));
        Assert.assertThat("Avg must be 201.6667", 201.6667, Is.is(counter.getAvg()));
    }

    @Test
    public void veriyCopiedValueNotChanged(){
        //Given
        StatisticCounter counter = new StatisticCounter();

        //when
        counter.update(100);
        counter.update(200);
        counter.update(100);
        counter.update(10);
        counter.update(500);
        counter.update(300);

        //then
        StatisticCounter copied = counter.copy();
        Assert.assertTrue("Have difference reference", copied != counter);
        Assert.assertTrue(copied.getSum() == counter.getSum());
        Assert.assertTrue(copied.getCount() == counter.getCount());
        Assert.assertTrue(copied.getMax() == counter.getMax());
        Assert.assertTrue(copied.getMin() == counter.getMin());
        Assert.assertTrue(copied.getAvg() == counter.getAvg());
    }

    @Test
    public void verifyAggregation(){
        //Given
        StatisticCounter counter = new StatisticCounter();

        //when
        counter.update(100);
        counter.update(200);

        StatisticCounter newAggregate = new StatisticCounter();
        newAggregate.update(counter);

        //then
        Assert.assertThat(newAggregate.getSum(), Is.is(300.0));
        Assert.assertThat(newAggregate.getCount(), Is.is(2));
        Assert.assertThat(newAggregate.getMax(), Is.is(200.0));
        Assert.assertThat(newAggregate.getMin(), Is.is(100.0));

        //When
        newAggregate.update(500);

        //then
        Assert.assertThat(newAggregate.getSum(), Is.is(800.0));
        Assert.assertThat(newAggregate.getCount(), Is.is(3));
        Assert.assertThat(newAggregate.getMax(), Is.is(500.0));
        Assert.assertThat(newAggregate.getMin(), Is.is(100.0));

    }

}