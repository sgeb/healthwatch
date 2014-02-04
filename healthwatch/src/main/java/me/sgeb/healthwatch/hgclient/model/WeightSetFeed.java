package me.sgeb.healthwatch.hgclient.model;

import java.util.List;

public class WeightSetFeed {
    private int size;
    private List<WeightSet> items;
    private String previous;
    private String next;

    public List<WeightSet> getItems() {
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
        return "WeightSetFeed{" +
                "size=" + size +
                ", items=" + items +
                ", previous='" + previous + '\'' +
                ", next='" + next + '\'' +
                '}';
    }
}

