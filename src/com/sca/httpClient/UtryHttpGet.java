package com.sca.httpClient;

/** 
 * get方式的访问操作
 * @Title: UtryHttpGet.java 
 */
import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class UtryHttpGet extends HttpEntityEnclosingRequestBase {
	public final static String METHOD_NAME = "GET";

	public UtryHttpGet() {
		super();
	}

	public UtryHttpGet(final URI uri) {
		super();
		setURI(uri);
	}

	/**
	 * @throws IllegalArgumentException
	 *             if the uri is invalid.
	 */
	public UtryHttpGet(final String uri) {
		super();
		setURI(URI.create(uri));
	}

	@Override
	public String getMethod() {
		return METHOD_NAME;
	}

}
