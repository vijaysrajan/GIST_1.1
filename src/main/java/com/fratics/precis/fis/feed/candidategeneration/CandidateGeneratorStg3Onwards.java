package com.fratics.precis.fis.feed.candidategeneration;

import com.fratics.precis.fis.base.BaseCandidateElement;
import com.fratics.precis.fis.base.BaseFeedElement;
import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.feed.BaseFeedPartitioner;
import com.fratics.precis.fis.feed.BaseFeedPartitioner.BaseFeedPartitionerReader;
import com.fratics.precis.fis.feed.dimval.DimValIndex;
import com.fratics.precis.fis.util.BitSet;
import com.fratics.precis.fis.util.Util;
import com.fratics.precis.util.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/*
 * This is the Candidate Generator from 3rd Stage Onwards. This is developed as a flow processor.
 * 
 * The Main functionalities are:-
 * 1) cross product on the previous stage candidates to generate potential candidates.
 * 2) Applies the Base feed data on the potential candidates
 * 3) Applies the threshold handler to filter the successful candidates.
 * 
 */

public class CandidateGeneratorStg3Onwards extends PrecisProcessor {

    private int currStage = 3;
    private ValueObject o;
    private HashMap<BitSet, ArrayList<BaseCandidateElement>> cdParttion;
    private HashSet<BitSet> cdSet;
    private Logger logger = Logger.getInstance();

    public CandidateGeneratorStg3Onwards(int stage) {
        this.currStage = stage;
    }

    // Checks if all the subset of the generated candidates
    // are present in the previous stage candidate set.
    public boolean isPresent(BitSet bs1) {
        boolean result = true;
        int valIndex = -1;
        for (int i = 0; i <= currStage; i++) {
            valIndex = bs1.nextSetBit(valIndex + 1);
        }

        int dimIndex = -1;
        for (int i = 0; i < currStage; i++) {
            dimIndex = bs1.nextSetBit(dimIndex + 1);
            if (i != 0) {
                valIndex = bs1.nextSetBit(valIndex + 1);
            }
            bs1.clear(dimIndex);
            bs1.clear(valIndex);

            if (cdSet.contains(bs1)) {
                bs1.set(dimIndex);
                bs1.set(valIndex);
            } else {
                result = false;
                bs1.set(dimIndex);
                bs1.set(valIndex);
                break;
            }
        }
        return result;
    }

    // Produces the cross product of the previous stage candidates to
    // generate the current Stage potential candidates.
    private void crossProduct() {
        int size = DimValIndex.getPrecisBitSetLength();
        BitSet allOneBS1 = new BitSet(size);
        allOneBS1.flip(0, size);

        BitSet allOneBS2 = new BitSet(size);
        allOneBS2.flip(0, size);

        for (ArrayList<BaseCandidateElement> bsList : cdParttion.values()) {
            for (int i = 0; i < bsList.size() - 1; i++) {
                allOneBS1.and(bsList.get(i).getBitSet());
                for (int j = i + 1; j < bsList.size(); j++) {
                    allOneBS1.xor(bsList.get(j).getBitSet());
                    if (allOneBS1.cardinality() == 4) {
                        BitSet newCand = (BitSet) bsList.get(i).getBitSet().clone();
                        newCand.or(bsList.get(j).getBitSet());
                        // Check all combinations of the current candidate in
                        // the
                        // previous stage candidates.
                        if (isPresent(newCand)) {
                            o.inputObject.addCandidate(newCand);
                        }
                    }
                    allOneBS1.or(allOneBS2);
                    allOneBS1.and(bsList.get(i).getBitSet());
                }
                allOneBS1.or(allOneBS2);
            }
        }
    }

    public boolean process(ValueObject o) throws Exception {
        this.o = o;
        cdParttion = o.inputObject.prevCandidatePart;
        cdSet = o.inputObject.prevCandidateSet;
        o.inputObject.currentStage = currStage;
        BaseFeedPartitioner bp = o.inputObject.getPartitioner();
        logger.info("Current Stage ::" + this.currStage);
        logger.info("No of Candidates from Previous Stage ::" + o.inputObject.prevCandidateSet.size());

        // Initialize Reader
        bp.initReader(this.currStage);

        // Generate Cross Product
        long milliSec1 = new Date().getTime();
        crossProduct();
        long milliSec2 = new Date().getTime();
        logger.info("No of Candidates Before Applying Threshold::" + o.inputObject.currCandidateSet.size());
        logger.info("Time taken in MilliSec for Candidate Gen ::" + (milliSec2 - milliSec1));

        BaseFeedPartitionerReader bpr = bp.getReader();
        boolean countPrecis = o.inputObject.isCountPrecis();
        milliSec1 = new Date().getTime();
        // Read the Input feed from Partitioner as bit set feed and apply the
        // metrics
        // to the candidates.
        while (bpr.hasNext()) {
            BaseFeedElement be = bpr.getNext();
            // logger.info(be);
            for (ArrayList<BaseCandidateElement> al : o.inputObject.currCandidatePart.values()) {
                for (BaseCandidateElement bce : al) {
                    if (bce.and(be.getBitSet()).equals(bce.getBitSet())) {
                        if (countPrecis)
                            bce.incrMetric();
                        else
                            bce.incrMetricBy(be.getMetric());
                    }
                }
            }
        }
        bp.closeReader();

        // Apply the threshold handler.
        boolean ret = o.inputObject.applyThreshold();
        milliSec2 = new Date().getTime();
        logger.info("No of Candidates After Applying Threshold::" + o.inputObject.currCandidateSet.size());
        logger.info("Time taken in MilliSec for Applying Threshold ::" + (milliSec2 - milliSec1));
        // Dump the Candidate Stage.
        if (ret)
            Util.dump(this.currStage, o);

        // Move the precis context to next stage - 3
        o.inputObject.moveToNextStage();
        return ret;
    }
}
