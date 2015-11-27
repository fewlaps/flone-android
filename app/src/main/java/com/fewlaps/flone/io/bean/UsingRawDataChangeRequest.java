package com.fewlaps.flone.io.bean;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 07/05/2015
 */
public class UsingRawDataChangeRequest {
    boolean useRawData;

    public UsingRawDataChangeRequest(boolean useRawData) {
        this.useRawData = useRawData;
    }

    public boolean isUsingRawData() {
        return useRawData;
    }
}
