package model;

public class MyWarcRecord {
    protected String url;
    protected Long length;
    protected String mimeType;
    protected String recordIdentifier;
    protected String headerFields;//header 全内容
    protected String headerWarcType;//header
    protected String content;
    protected String coding;
    protected String httpContent;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getRecordIdentifier() {
        return recordIdentifier;
    }

    public void setRecordIdentifier(String recordIdentifier) {
        this.recordIdentifier = recordIdentifier;
    }

    public String getHeaderFields() {
        return headerFields;
    }

    public void setHeaderFields(String headerFields) {
        this.headerFields = headerFields;
    }

    public String getHeaderWarcType() {
        return headerWarcType;
    }

    public void setHeaderWarcType(String headerWarcType) {
        this.headerWarcType = headerWarcType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public String getHttpContent() {
        return httpContent;
    }

    public void setHttpContent(String httpContent) {
        this.httpContent = httpContent;
    }
}
