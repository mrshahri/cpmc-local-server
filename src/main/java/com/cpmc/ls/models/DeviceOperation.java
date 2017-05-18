package com.cpmc.ls.models;

import java.util.HashMap;

/**
 * Created by Rakib on 5/17/2017.
 */
public class DeviceOperation {
    private String deviceId;
    private String operationId;
    private HashMap<String, String> parameters;

    public DeviceOperation() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }
}
