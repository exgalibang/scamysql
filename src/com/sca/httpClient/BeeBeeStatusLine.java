package com.sca.httpClient;
/**
 * 
 * @ClassName：BeeBeeStatusLine     
 * @Description: 自己定义的statusline状态信息    
 *
 */
public class BeeBeeStatusLine {
    /**
     * 协议字符串
     */
    private String protocolVersion;

    /**
     * 返回的状态码
     */
    private int statusCode;

    /**
     *  原因字符串（例如：找不到资源的页面信息等）
     *
     * */
    private String reasonPhrase;

    public BeeBeeStatusLine() {

    }

    public BeeBeeStatusLine(String protocolVersion, int statusCode, String reasonPhrase) {
        super();
        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

}
