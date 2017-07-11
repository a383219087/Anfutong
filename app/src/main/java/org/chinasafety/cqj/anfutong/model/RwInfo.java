package org.chinasafety.cqj.anfutong.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

/**
 * Entity mapped to table RW_INFO.
 */
public class RwInfo implements IChooseItem{

    private String partid;
    private String partname;

    public RwInfo() {
    }


    public RwInfo(String partid, String partname) {
        this.partid = partid;
        this.partname = partname;
    }


    public String getPartid() {
        return partid;
    }

    public void setPartid(String partid) {
        this.partid = partid;
    }

    public String getPartname() {
        return partname;
    }

    public void setPartname(String partname) {
        this.partname = partname;
    }


    @Override
    public String getItemName() {
        return partname;
    }

    @Override
    public String getItemId() {
        return partid;
    }

    @Override
    public String getCsId() {
        return null;
    }
}