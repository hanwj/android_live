package com.jazz.direct.libs;

/**
 * Created by TDJ on 2016/5/31.
 */
public class Device {
    private String model;//手机型号
    private String api;//sdk API

    public Device(String model, String api){
        this.model = model;
        this.api = api;
    }

    public Device setApi(String api) {
        this.api = api;
        return this;
    }

    public Device setModel(String model) {
        this.model = model;
        return this;
    }

    public String getApi() {
        return api;
    }

    public String getModel() {
        return model;
    }
}
