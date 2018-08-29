package com.fratics.precis.util;

import com.googlecode.javaewah.datastructure.BitSet;

import java.util.ArrayList;
import java.util.Comparator;

public class BitSetComparator implements Comparator<BitSet> {
    public static void main(String[] args) {

        ArrayList<BitSet> al = new ArrayList<BitSet>();
        BitSet b1 = new BitSet();
        b1.set(0);
        b1.set(2);
        b1.set(3);
        b1.set(5);

        BitSet b2 = new BitSet();
        b2.set(0);
        b2.set(2);
        b2.set(3);

        BitSet b3 = new BitSet();
        b3.set(0);
        b3.set(2);
        b3.set(3);
        b3.set(7);
        b3.set(9);

        al.add(b1);
        al.add(b2);
        al.add(b3);

        al.sort(new BitSetComparator());

        System.err.println(al);

    }

    public int compare(BitSet o1, BitSet o2) {
        return Integer.valueOf(o1.cardinality()).compareTo(o2.cardinality());
    }
}