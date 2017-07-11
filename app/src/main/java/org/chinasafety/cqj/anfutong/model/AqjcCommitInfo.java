package org.chinasafety.cqj.anfutong.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

/**
 * Entity mapped to table AQJC_COMMIT_INFO.
 */
public class AqjcCommitInfo {

    private Long id;
    private String imagePath;
    private String checkDate;
    private Integer recEmid;
    private String dLimit;
    private String hDetail;
    private String dScheme;
    private String hGrade;
    private Integer lEmid;
    private Float dCost;
    private Integer objOrganizationID;
    private Integer Taskid;
    private String TaskName;
    private String CsName;
    private Integer FliedID;
    private Integer objPartid;
    private String SetStr;
    private boolean isSuccess;

    public AqjcCommitInfo() {
    }

    public AqjcCommitInfo(Long id) {
        this.id = id;
    }

    public AqjcCommitInfo(Long id, String imagePath, String checkDate, Integer recEmid, String dLimit, String hDetail, String dScheme, String hGrade, Integer lEmid, Float dCost, Integer objOrganizationID, Integer Taskid, String TaskName, String CsName, Integer FliedID, Integer objPartid, String SetStr) {
        this.id = id;
        this.imagePath = imagePath;
        this.checkDate = checkDate;
        this.recEmid = recEmid;
        this.dLimit = dLimit;
        this.hDetail = hDetail;
        this.dScheme = dScheme;
        this.hGrade = hGrade;
        this.lEmid = lEmid;
        this.dCost = dCost;
        this.objOrganizationID = objOrganizationID;
        this.Taskid = Taskid;
        this.TaskName = TaskName;
        this.CsName = CsName;
        this.FliedID = FliedID;
        this.objPartid = objPartid;
        this.SetStr = SetStr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public Integer getRecEmid() {
        return recEmid;
    }

    public void setRecEmid(Integer recEmid) {
        this.recEmid = recEmid;
    }

    public String getDLimit() {
        return dLimit;
    }

    public void setDLimit(String dLimit) {
        this.dLimit = dLimit;
    }

    public String getHDetail() {
        return hDetail;
    }

    public void setHDetail(String hDetail) {
        this.hDetail = hDetail;
    }

    public String getDScheme() {
        return dScheme;
    }

    public void setDScheme(String dScheme) {
        this.dScheme = dScheme;
    }

    public String getHGrade() {
        return hGrade;
    }

    public void setHGrade(String hGrade) {
        this.hGrade = hGrade;
    }

    public Integer getLEmid() {
        return lEmid;
    }

    public void setLEmid(Integer lEmid) {
        this.lEmid = lEmid;
    }

    public Float getDCost() {
        return dCost;
    }

    public void setDCost(Float dCost) {
        this.dCost = dCost;
    }

    public Integer getObjOrganizationID() {
        return objOrganizationID;
    }

    public void setObjOrganizationID(Integer objOrganizationID) {
        this.objOrganizationID = objOrganizationID;
    }

    public Integer getTaskid() {
        return Taskid;
    }

    public void setTaskid(Integer Taskid) {
        this.Taskid = Taskid;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String TaskName) {
        this.TaskName = TaskName;
    }

    public String getCsName() {
        return CsName;
    }

    public void setCsName(String CsName) {
        this.CsName = CsName;
    }

    public Integer getFliedID() {
        return FliedID;
    }

    public void setFliedID(Integer FliedID) {
        this.FliedID = FliedID;
    }

    public Integer getObjPartid() {
        return objPartid;
    }

    public void setObjPartid(Integer objPartid) {
        this.objPartid = objPartid;
    }

    public String getSetStr() {
        return SetStr;
    }

    public void setSetStr(String SetStr) {
        this.SetStr = SetStr;
    }

}
