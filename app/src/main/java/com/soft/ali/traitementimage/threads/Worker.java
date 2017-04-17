package com.soft.ali.traitementimage.threads;

import com.soft.ali.traitementimage.processing.InterProcessing;

/**
 * Class running the thread on a part of the array.
 */
public class Worker implements Runnable {

    private int lower;
    private int upper;
    private InterProcessing process;

    public Worker(int lower, int upper, InterProcessing process){
        this.process = process;
        this.lower = lower;
        this.upper = upper;
    }


    @Override
    public void run() {
        process.process(lower, upper);
    }

}
