package com.cpmc.ls.models;

import java.util.List;

/**
 * Created by Rakib on 5/18/2017.
 */
public class Operation {
    private String deviceId;
    private String uuid;
    private List<ComponentOperation> componentOperations;

    public Operation() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<ComponentOperation> getComponentOperations() {
        return componentOperations;
    }

    public void setComponentOperations(List<ComponentOperation> componentOperations) {
        this.componentOperations = componentOperations;
    }
}
