package com.sca.httpClient;

/** 
 * 自己定义的httpdelete的删除调用(原因是原始的delete不能传入参数)
 * @Title: UtryHttpDelete.java
 */
import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class UtryHttpDelete extends HttpEntityEnclosingRequestBase {
	public final static String METHOD_NAME = "DELETE";

	public UtryHttpDelete() {
		super();
	}

	public UtryHttpDelete(final URI uri) {
		super();
		setURI(uri);
	}

	/**
	 * @throws IllegalArgumentException
	 *             if the uri is invalid.
	 */
	public UtryHttpDelete(final String uri) {
		super();
		setURI(URI.create(uri));
	}

	@Override
	public String getMethod() {
		return METHOD_NAME;
	}

}
