package me.sgeb.healthwatch.hgclient.model;

import java.util.List;

public class WeightSetFeed {
    private int size;
    private List<WeightSet> items;

    public List<WeightSet> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "WeightSetFeed{" +
                "size=" + size +
                ", items=" + items +
                '}';
    }
}

