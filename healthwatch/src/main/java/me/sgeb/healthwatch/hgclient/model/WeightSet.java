package me.sgeb.healthwatch.hgclient.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import me.sgeb.healthwatch.hgclient.HgClient;

public class WeightSet {
    private String uri;
    private Date timestamp;
    private Double weight;
    @SerializedName("fat_percent")
    private Double fatPercent;

    public WeightSet() {
        this.timestamp = new Date();
    }

    public String getId() {
        String res = null;
        if (uri != null && !uri.isEmpty()) {
            res = uri.replace(HgClient.PATH_WEIGHT + "/", "");
        }
        return res;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setFatPercent(Double fatPercent) {
        this.fatPercent = fatPercent;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getFatPercent() {
        return fatPercent;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "WeightSet{" +
                "uri='" + uri + '\'' +
                ", timestamp=" + timestamp +
                ", weight=" + weight +
                ", fatPercent=" + fatPercent +
                '}';
    }
}
