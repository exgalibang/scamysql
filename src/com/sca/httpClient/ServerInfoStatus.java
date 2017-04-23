package com.sca.httpClient;

/**
 * 服务器返回信息的dto
 *
 * @Title: ServerInfoStatus.java
 */
import java.util.List;

import com.alibaba.fastjson.JSON;

public class ServerInfoStatus {
    /**
     * 服务器返回的信息
     */
    private String serverInfo;
    /**
     * 服务器返回的状态信息
     */
    private BeeBeeStatusLine serverStatus;

    public String getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    public BeeBeeStatusLine getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(BeeBeeStatusLine serverStatus) {
        this.serverStatus = serverStatus;
    }

    /**
     * 提取服务器发送的信息转换成指定的类型（如果服务器发送的信息不是json形式，我们不提供json转换，会跑出来异常信息）
     *
     * @param clazz 要转换的类型
     * @return 转换的指定类型
     */
    public <T> T getT(Class<T> clazz) {
        return JSON.parseObject(serverInfo, clazz);
    }

    /**
     * 提取服务器的信息转换成指定类型的List形式（如果服务器发送的信息不是json形式，我们不提供json转换，会跑出来异常信息）
     *
     * @param clazz 要转换的类型
     * @return 指定类型的list集合
     */
    public <T> List<T> getListT(Class<T> clazz) {
        return JSON.parseArray(serverInfo, clazz);
    }

    /**
     *
     * @return object的toString的重写
     */
    @Override
	public String toString() {
        return serverInfo.toString();

    }

}
