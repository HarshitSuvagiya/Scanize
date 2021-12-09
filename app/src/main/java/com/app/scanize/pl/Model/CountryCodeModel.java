package com.app.scanize.pl.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryCodeModel {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("flow")
    @Expose
    private String flow;
    @SerializedName("operator")
    @Expose
    private String operator;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
