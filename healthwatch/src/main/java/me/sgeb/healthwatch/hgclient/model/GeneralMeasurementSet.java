package me.sgeb.healthwatch.hgclient.model;

import java.util.Date;

import me.sgeb.healthwatch.hgclient.HgClient;

public class GeneralMeasurementSet {
    private String uri;
    private Date timestamp;
    private Double systolic;
    private Double diastolic;
    private Double restingHeartrate;

    public GeneralMeasurementSet() {
        this.timestamp = new Date();
    }

    public String getId() {
        String res = null;
        if (uri != null && !uri.isEmpty()) {
            res = uri.replace(HgClient.PATH_WEIGHT + "/", "");
        }
        return res;
    }

    public void setSystolic(Double systolic) {
        this.systolic = systolic;
    }

    public void setDiastolic(Double diastolic) {
        this.diastolic = diastolic;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Double getSystolic() {
        return systolic;
    }

    public Double getDiastolic() {
        return diastolic;
    }

    public Double getRestingHeartrate() {
        return restingHeartrate;
    }

    public void setRestingHeartrate(Double restingHeartrate) {
        this.restingHeartrate = restingHeartrate;
    }

    public String getUri() {
        return uri;
    }
}
