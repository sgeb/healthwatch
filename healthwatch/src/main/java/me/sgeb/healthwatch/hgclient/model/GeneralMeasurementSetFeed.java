package me.sgeb.healthwatch.hgclient.model;

import java.util.List;

public class GeneralMeasurementSetFeed {
    private int size;
    private List<GeneralMeasurementSet> items;
    private String previous;
    private String next;

    public List<GeneralMeasurementSet> getItems() {
        return items;
    }

    public String getPrevious() {
        return previous;
    }

    public String getNext() {
        return next;
    }

    @Override
    public String toString() {
        return "GeneralMeasurementSetFeed{" +
                "size=" + size +
                ", items=" + items +
                ", previous='" + previous + '\'' +
                ", next='" + next + '\'' +
                '}';
    }
}
