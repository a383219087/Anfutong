package org.chinasafety.cqj.anfutong.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table JCB_DETAIL_INFO.
 */
public class JcbDetailInfo {

    private Long id;
    private String jcb_id;
    private String oblititle;
    private String odetail;
    private String sCheckListtype;
    private String induname;
    private String inseName;
    private String malName;
    private String rAdvise;
    private Boolean is_choose;

    public JcbDetailInfo() {
    }

    public JcbDetailInfo(Long id) {
        this.id = id;
    }

    public JcbDetailInfo(Long id, String jcb_id, String oblititle, String odetail, String sCheckListtype, String induname, String inseName, String malName, String rAdvise, Boolean is_choose) {
        this.id = id;
        this.jcb_id = jcb_id;
        this.oblititle = oblititle;
        this.odetail = odetail;
        this.sCheckListtype = sCheckListtype;
        this.induname = induname;
        this.inseName = inseName;
        this.malName = malName;
        this.rAdvise = rAdvise;
        this.is_choose = is_choose;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJcb_id() {
        return jcb_id;
    }

    public void setJcb_id(String jcb_id) {
        this.jcb_id = jcb_id;
    }

    public String getOblititle() {
        return oblititle;
    }

    public void setOblititle(String oblititle) {
        this.oblititle = oblititle;
    }

    public String getOdetail() {
        return odetail;
    }

    public void setOdetail(String odetail) {
        this.odetail = odetail;
    }

    public String getSCheckListtype() {
        return sCheckListtype;
    }

    public void setSCheckListtype(String sCheckListtype) {
        this.sCheckListtype = sCheckListtype;
    }

    public String getInduname() {
        return induname;
    }

    public void setInduname(String induname) {
        this.induname = induname;
    }

    public String getInseName() {
        return inseName;
    }

    public void setInseName(String inseName) {
        this.inseName = inseName;
    }

    public String getMalName() {
        return malName;
    }

    public void setMalName(String malName) {
        this.malName = malName;
    }

    public String getRAdvise() {
        return rAdvise;
    }

    public void setRAdvise(String rAdvise) {
        this.rAdvise = rAdvise;
    }

    public Boolean getIs_choose() {
        return is_choose;
    }

    public void setIs_choose(Boolean is_choose) {
        this.is_choose = is_choose;
    }

}
