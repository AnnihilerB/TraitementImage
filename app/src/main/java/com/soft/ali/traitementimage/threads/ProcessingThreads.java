package com.soft.ali.traitementimage.threads;

import com.soft.ali.traitementimage.processing.InterProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class builds the architecture needed to process an image entirely.
 * The image is splitted in parts and each threads has a part of it assigned.
 *
 */
public class ProcessingThreads {

    private int threadsNumber;
    private int numberPixels;
    private int interval;
    private int lower;
    private int upper;
    private  InterProcessing process;

    private ExecutorService pool;
    private List<Callable<Object>> calls = new ArrayList<>();


    /**
     * Creates some threads that runs the processing method.
     * The pixel array is divided according to the number of cores int he device.
     * @param threadsNumber number of threads to create
     * @param numberPixels number of pixles in the image.
     * @param process the processing method to execute.
     */
    public ProcessingThreads(int threadsNumber, int numberPixels, InterProcessing process){
        this.threadsNumber = threadsNumber;
        this.numberPixels = numberPixels;
        this.interval = this.numberPixels/threadsNumber;
        this.lower = 0;
        this.upper = interval;
        this.process = process;
        pool = Executors.newFixedThreadPool(threadsNumber);
    }

    /**
     * Start the created threads.
     * The method ends only when all threads have finished their jobs.
     * @throws InterruptedException exception if the thread is suddenly stopped
     */
    public  void startThreads() throws InterruptedException {
        for (int i = 0 ; i < threadsNumber; i++) {
            calls.add(Executors.callable( new Thread(new Worker(lower, upper, process))));
            lower += interval;
            upper += interval;
        }
        pool.invokeAll(calls);

    }
}
