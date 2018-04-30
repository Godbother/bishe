package model.esmodel;

import model.MyWarcRecord;

public class EsWarc extends MyWarcRecord{
    private String id;
    private String addTime;
    private String updateTime;
    protected String warcUrl;//用于生成的URL
    protected String responseUrl;//提及的URL
    protected Long create_at;
    protected Long update_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getWarcUrl() {
        return warcUrl;
    }

    public void setWarcUrl(String warcUrl) {
        this.warcUrl = warcUrl;
    }

    public String getResponseUrl() {
        return responseUrl;
    }

    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }

    public Long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Long create_at) {
        this.create_at = create_at;
    }

    public Long getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Long update_at) {
        this.update_at = update_at;
    }

    @Override
    public String toString() {
        return "EsWarc{" +
                "id='" + id + '\'' +
                ", addTime='" + addTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", warcUrl='" + warcUrl + '\'' +
                ", responseUrl='" + responseUrl + '\'' +
                ", create_at=" + create_at +
                ", update_at=" + update_at +
                '}';
    }
}
