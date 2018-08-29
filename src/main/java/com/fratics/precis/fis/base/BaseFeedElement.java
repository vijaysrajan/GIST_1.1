package com.fratics.precis.fis.base;

import com.fratics.precis.fis.util.BitSet;

/*
 * This class is the actual bit representation of a record in the input feed. 
 * 
 */

public class BaseFeedElement {

    protected BitSet b;
    protected double metric;

    public BaseFeedElement() {
        b = new BitSet();
    }

    public BaseFeedElement(int noOfBits) {
        b = new BitSet(noOfBits);
    }

    public BitSet getBitSet() {
        return b;
    }

    public void setBitSet(BitSet b) {
        this.b = b;
    }

    public BitSet and(BaseFeedElement b) {
        BitSet bs = (BitSet) this.b.clone();
        bs.and(b.getBitSet());
        return bs;
    }

    public BitSet or(BaseFeedElement b) {
        BitSet bs = (BitSet) this.b.clone();
        bs.or(b.getBitSet());
        return bs;
    }

    public BitSet xor(BaseFeedElement b) {
        BitSet bs = (BitSet) this.b.clone();
        bs.xor(b.getBitSet());
        return bs;
    }

    public BitSet and(BitSet b) {
        BitSet bs = (BitSet) this.b.clone();
        bs.and(b);
        return bs;
    }

    public BitSet or(BitSet b) {
        BitSet bs = (BitSet) this.b.clone();
        bs.or(b);
        return bs;
    }

    public BitSet xor(BitSet b) {
        BitSet bs = (BitSet) this.b.clone();
        bs.xor(b);
        return bs;
    }

    public void setBit(int e) {
        this.b.set(e);
    }

    public double getMetric() {
        return metric;
    }

    public void setMetric(double metric) {
        this.metric = metric;
    }

    public int getCardinality() {
        return (int) this.b.cardinality();
    }

	/*
     * Cardinality divided by 2 gives you the actual number of dimensions or
	 * values in the input record.
	 */

    public int getNumberofDimVals() {
        return (int) (this.b.cardinality() / 2);
    }

    public String toString() {
        return this.b.toString() + "," + metric;
    }
}
