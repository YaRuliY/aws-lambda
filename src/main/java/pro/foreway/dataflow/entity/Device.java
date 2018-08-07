package pro.foreway.dataflow.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@DynamoDBTable(tableName = "reportStateDeviceTest")
public class Device {
    private String devID;
    private String owner;
    private String tuyaJson;
    private boolean online;
    private Map<String, Set<String>> linkMap;
    private String customName;

    public Device() {
    }

    public Device(String devID, String owner, String tuyaJson, boolean online, Map<String, Set<String>> linkMap,
                  String customName) {
        this.devID = devID;
        this.owner = owner;
        this.tuyaJson = tuyaJson;
        this.online = online;
        this.linkMap = linkMap;
        this.customName = customName;
    }

    @DynamoDBHashKey(attributeName = "devID")
    public String getDevID() {
        return devID;
    }

    public void setDevID(String devID) {
        this.devID = devID;
    }

    @DynamoDBAttribute(attributeName = "owner")
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @DynamoDBAttribute(attributeName = "tuyaJson")
    public String getTuyaJson() {
        return tuyaJson;
    }

    public void setTuyaJson(String tuyaJson) {
        this.tuyaJson = tuyaJson;
    }

    @DynamoDBAttribute(attributeName = "online")
    public boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @DynamoDBAttribute(attributeName = "linkingMap")
    public Map<String, Set<String>> getLinkMap() {
        return linkMap;
    }

    public void setLinkMap(Map<String, Set<String>> linkMap) {
        this.linkMap = linkMap;
    }

    @DynamoDBAttribute(attributeName = "customName")
    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public String toString() {
        return "Device{" +
                "devID='" + devID + '\'' +
                ", owner='" + owner + '\'' +
                ", tuyaJson='" + tuyaJson + '\'' +
                ", online=" + online +
                ", linkMap=" + linkMap +
                ", customName='" + customName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;
        Device device = (Device) o;
        return online == device.online &&
                Objects.equals(devID, device.devID) &&
                Objects.equals(owner, device.owner) &&
                Objects.equals(linkMap, device.linkMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(devID, owner, online, linkMap);
    }
}