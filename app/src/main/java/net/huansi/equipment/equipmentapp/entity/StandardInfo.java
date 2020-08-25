package net.huansi.equipment.equipmentapp.entity;

public class StandardInfo {
    private String standardName;
    private String standardFloat;
    private String standardBiao;//标准尺码M，S
    private String standardSize;//标准尺寸 客人标准3.4
    private long standardId;

    public String getStandardBiao() {
        return standardBiao;
    }

    public void setStandardBiao(String standardBiao) {
        this.standardBiao = standardBiao;
    }
    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getStandardFloat() {
        return standardFloat;
    }

    public void setStandardFloat(String standardFloat) {
        this.standardFloat = standardFloat;
    }

    public String getStandardSize() {
        return standardSize;
    }

    public void setStandardSize(String standardSize) {
        this.standardSize = standardSize;
    }

    public long getStandardId() {
        return standardId;
    }

    public void setStandardId(long standardId) {
        this.standardId = standardId;
    }
}
