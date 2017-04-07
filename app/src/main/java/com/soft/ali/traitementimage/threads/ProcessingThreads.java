package com.soft.ali.traitementimage.threads;

import android.util.Log;

import com.soft.ali.traitementimage.processing.InterProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by ali on 29/03/2017.
 */

public class ProcessingThreads {

    private int threadsNumber;
    private int numberPixels;
    private int interval;
    private int lower;
    private int upper;
    private  InterProcessing process;

    private ExecutorService pool;
    List<Callable<Object>> calls = new ArrayList<>();


    public ProcessingThreads(int threadsNumber, int numberPixels, InterProcessing process){
        this.threadsNumber = threadsNumber;
        this.numberPixels = numberPixels;
        this.interval = this.numberPixels/threadsNumber;
        this.lower = 0;
        this.upper = interval;
        this.process = process;
        pool = Executors.newFixedThreadPool(threadsNumber);
    }

    public  void startThreads() throws InterruptedException {
        for (int i = 0 ; i < threadsNumber; i++) {
            calls.add(Executors.callable( new Thread(new Worker(lower, upper, process))));
            lower += interval;
            upper += interval;
        }
        pool.invokeAll(calls);

    }
}
