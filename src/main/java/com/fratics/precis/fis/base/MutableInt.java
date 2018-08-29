package com.fratics.precis.fis.base;

/*
 * A envelop for the int data type.
 * Added my helper methods to provide functions like increments, comparator etc.
 * Can be extended to provide many more technical functionalities to primitive data types.
 * 
 */

public class MutableInt implements Comparable<MutableInt> {
    int value = 1;

    public MutableInt() {
    }

    public MutableInt(int val) {
        value = val;
    }

    public void inc() {
        ++value;
    }

    public void incBy(int inc) {
        value += inc;
    }

    public int get() {
        return value;
    }

    public String toString() {
        return "" + value;
    }

    public int compareTo(MutableInt o) {
        return this.value - o.value;
    }
}