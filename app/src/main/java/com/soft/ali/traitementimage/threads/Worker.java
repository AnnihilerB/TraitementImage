package com.soft.ali.traitementimage.threads;

import android.util.Log;

import com.soft.ali.traitementimage.processing.InterProcessing;

/**
 * Created by ali on 29/03/2017.
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
