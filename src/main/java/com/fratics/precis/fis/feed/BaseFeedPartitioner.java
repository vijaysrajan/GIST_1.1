package com.fratics.precis.fis.feed;

import com.fratics.precis.fis.base.BaseFeedElement;
import com.fratics.precis.fis.util.PrecisConfigProperties;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
 * The input data feed is removed of all the values that were below threshold.
 * These remaining values are then converted to a bit set and added to 
 * this holder object "BaseFeedPartitioner".
 * These values are partitioned by the cardinality of the bit set.
 * 
 * The logic for converting values --> bits is in the Class "DimValIndex".
 * 
 * This class also provides a partition bit set stream reader object to facilitate easy reading.
 * 
 */

public class BaseFeedPartitioner {

    // Reader object instance for the current partition.
    private BaseFeedPartitionerReader bfpr = null;
    // Base Feed elements partitions.
    private BaseFeedElementList[] partitionMap = null;

    // BaseFeed Partitioner initialized to the no of fields.
    // So some of the partitions may be empty.
    public BaseFeedPartitioner(int length) {
        partitionMap = new BaseFeedElementList[length];
        for (int i = 0; i < partitionMap.length; i++) {
            partitionMap[i] = new BaseFeedElementList();
        }
    }

    // Add a new BaseFeed Element to the partition.
    public void addElement(int index, BaseFeedElement e) {
        partitionMap[index].add(e);
    }

    // Return the reader object.
    public BaseFeedPartitionerReader getReader() throws Exception {
        if (this.bfpr == null)
            throw new Exception("BaseFeedPartitionerReader not initialized");
        return this.bfpr;
    }

    // Initialize the reader object.
    public void initReader(int stage) {
        this.bfpr = new BaseFeedPartitionerReader(stage);
    }

    // unInitialize the reader.
    public void closeReader() {
        bfpr = null;
    }

    // Method to dump the contents of the partition.
    public void dump() {
        try {
            PrintWriter pw = new PrintWriter(new File(PrecisConfigProperties.BITSET_FEED_FILENAME));
            pw.append(this.toString());
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Returns the contents of the partition formatted.
    public String toString() {
        String str = "";
        for (int i = 0; i < partitionMap.length; i++) {
            ArrayList<BaseFeedElement> al = partitionMap[i];
            // if(al.size() > 0 )
            str = str + "Partition Number :: " + i + ", No Of Elements :: " + al.size() + ", Values :: " + al.toString()
                    + "\n";
        }
        return str;
    }

    // Created a dummy list inner class to create array of ArrayLists.
    public class BaseFeedElementList extends ArrayList<BaseFeedElement> {
        private static final long serialVersionUID = 1L;
    }

    // Base Feed Partition Reader inner class, a simple stream / byte array
    // reader to provide
    // sequential access of the records, irrespective of where it is stored in
    // the
    // base feed partition object.
    public class BaseFeedPartitionerReader {
        private int currentStage = 2;
        private int currentIndex = 0;

        // Need to initialize the reader with the current stage.
        public BaseFeedPartitionerReader(int stage) {
            this.currentStage = stage - 1; // index starts from "0".
            this.currentIndex = 0;
            while (this.currentStage < partitionMap.length && partitionMap[this.currentStage].size() == 0)
                this.currentStage++;
        }

        // Checks if Base Feed Elements are present for the stage
        // in the partition object.
        public boolean hasNext() {
            return ((this.currentStage < partitionMap.length)
                    && (this.currentIndex < partitionMap[this.currentStage].size()));
        }

        // Returns the next available base feed element from the
        // partition object.
        public BaseFeedElement getNext() {
            // logger.info("Current Index ::" + this.currentIndex +
            // " Current Stage ::" + this.currentStage);
            BaseFeedElement e = partitionMap[this.currentStage].get(currentIndex);
            this.currentIndex++;
            if (currentIndex >= partitionMap[this.currentStage].size()) {
                this.currentIndex = 0;
                this.currentStage++;
                while ((this.currentStage < partitionMap.length) && (partitionMap[this.currentStage].size() <= 0))
                    this.currentStage++;
            }
            return e;
        }

    }
}
