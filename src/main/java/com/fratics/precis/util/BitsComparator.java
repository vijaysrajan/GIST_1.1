package com.fratics.precis.util;

import com.googlecode.javaewah.datastructure.BitSet;

import java.util.Comparator;

public class BitsComparator implements Comparator<BitSet> {
    public static void main(String[] args) {

        BitsComparator bc = new BitsComparator();

        BitSet x = new BitSet();
        x.set(0);
        x.set(1);
        x.set(2);
        // x.set(4);
        x.set(7);

        BitSet y = new BitSet();
        y.set(0);
        y.set(1);
        y.set(2);
        y.set(4);
        y.set(7);

        System.err.println(bc.compare(x, y));

    }

    public int compare(BitSet b1, BitSet b2) {
        if (b1.equals(b2))
            return 0;
        BitSet b3 = (BitSet) b1.clone();
        BitSet b4 = (BitSet) b2.clone();
        BitSet b5 = (BitSet) b1.clone();
        b3.and(b4); // b3 has the intersection.
        b4.xor(b3); // clear the intersect bits.
        b5.xor(b3); // clear the intersect bits.
        return b5.nextSetBit(0) - b4.nextSetBit(0);
    }
}