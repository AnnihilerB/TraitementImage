package com.soft.ali.traitementimage.processing;

/**
 * Interface used for multithreading.
 * It allows us to split the array in multiple part and call the proper method on a part of the array.
 * Each processing method implements this class/
 */

public interface InterProcessing {
    void process(int lower, int upper);
}
