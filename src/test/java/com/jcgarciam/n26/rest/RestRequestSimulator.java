package com.jcgarciam.n26.rest;

import com.jcgarciam.n26.dto.Transaction;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * RestRequestSimulator
 */
public class RestRequestSimulator {
    public static void main(String[] args) throws IOException {
        Random rnd = new Random();
        RestTemplate client = new RestTemplate();
        IntStream.range(0, 500).forEach(x->{

            Transaction trx = new Transaction();
            trx.setAmount(Math.random() * 2000);
            trx.setTimestamp(System.currentTimeMillis());
            client.postForObject("http://localhost:8080/transaction", trx, Void.class);

            if(rnd.nextBoolean()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
