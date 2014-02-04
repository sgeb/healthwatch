package me.sgeb.healthwatch.hgclient.model;

import java.util.List;

public class GeneralMeasurementSetFeed {
    private int size;
    private List<GeneralMeasurementSet> items;

    public List<GeneralMeasurementSet> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "GeneralMeasurementSetFeed{" +
                "size=" + size +
                ", items=" + items +
                '}';
    }
}
