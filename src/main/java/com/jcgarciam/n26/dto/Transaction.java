package com.jcgarciam.n26.dto;

/**
 * Transaction
 */
public class Transaction {
    private double amount;
    private long timestamp;

    public double getAmount(){
        return amount;
    }
    public void setAmount(double value){
        this.amount = value;
    }

    public long getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(long value){
        this.timestamp = value;
    }
}