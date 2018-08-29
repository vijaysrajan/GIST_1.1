package com.fratics.precis.fis.util;

import org.apache.lucene.util.OpenBitSet;

//public class BitSet extends com.googlecode.javaewah.datastructure.BitSet {
public class BitSet extends OpenBitSet {

    private static final long serialVersionUID = 1L;

    public BitSet() {
        super(1024);
    }

    public BitSet(int x) {
        super(x);
    }

    public int previousSetBit(int x) {
        for (int i = x; i >= 0; i--) {
            if (this.get(i)) return i;
        }
        return -1;
    }

}
