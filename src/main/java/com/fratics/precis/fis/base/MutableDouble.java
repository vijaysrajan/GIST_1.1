package com.fratics.precis.fis.base;

/*
 * A envelop for the double data type.
 * Added my helper methods to provide functions like increments, comparator etc.
 * Can be extended to provide many more technical functionalities to primitive data types.
 * 
 */

public class MutableDouble implements Comparable<MutableDouble> {
    double value = 1.0;

    public MutableDouble() {
    }

    public MutableDouble(double val) {
        value = val;
    }

    public void inc() {
        ++value;
    }

    public void incBy(double inc) {
        value += inc;
    }

    public double get() {
        return value;
    }

    public String toString() {
        return "" + value;
    }

    public int compareTo(MutableDouble o) {
        return (int) ((this.value - o.value) * 100);
    }
}