package com.girls.scanize.selfiecamera.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubStatusModel {
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
