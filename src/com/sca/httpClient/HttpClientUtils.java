package com.sca.httpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * HttpClient已经实现了线程安全。所以希望用户在实例化HttpClient时，也要支持为多个请求使用。
 * httpClient的工具类，用来访问后台的服务操作
 *
 * @Title: HttpClientUtils.java
 *         <p/>
 *         POST /uri 创建
 *         <p/>
 *         DELETE /uri/xxx 删除
 *         <p/>
 *         PUT /uri/xxx 更新
 *         <p/>
 *         GET /uri/xxx 查看
 * @version 2.0 
 */
public class HttpClientUtils {
    /**
     * 初始化我们的httpClient对象
     */
    private static final HttpClient HTTP_CLIENT = HttpClients.createDefault();

    /**
     * delete方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @return serverinfostatus对象（这个对象主要装填了服务器的信息和返回状态）
     */
    public static ServerInfoStatus deleteServerInfoStatus(String servicePath,String apikey) {
        HttpDelete delete = new HttpDelete(servicePath);
        // 填入apikey到HTTP header
  		if(StringUtils.isNotBlank(apikey))
  			delete.setHeader("apikey", apikey);
        return analysisHttpResponse(delete);
    }

    /**
     * 根据传入的参数进行批量的删除操作
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @return serverinfostatus对象（这个对象主要装填了服务器的信息和返回状态）
     */
    public static ServerInfoStatus deleteServerInfoStatus(String servicePath, Object object) {
        StringEntity paramesEntity = new StringEntity(JSON.toJSONString(object), "UTF-8");
        paramesEntity.setContentEncoding("UTF-8");
        paramesEntity.setContentType("application/json");
        UtryHttpDelete delete = new UtryHttpDelete(servicePath);
        delete.setEntity(paramesEntity);
        return analysisHttpResponse(delete);
    }

    /**
     * delete方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param clazz       指定转换的类型
     * @return 指定的类型对象
     */
    public static <T> T deleteT(String servicePath, Class<T> clazz,String apiKey) {
        return deleteServerInfoStatus(servicePath,apiKey).getT(clazz);
    }

    /**
     * delete方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param clazz       指定转换的类型
     * @return 指定的类型对象的list集合
     */
    public static <T> List<T> deleteListT(String servicePath, Class<T> clazz,String apiKey) {
        return deleteServerInfoStatus(servicePath,apiKey).getListT(clazz);
    }

    /**
     * delete方式访问服务器服务
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @return 服务器返回的json信息字符串转换成的jsonobject对象
     */
    public static JSONObject delete(String servicePath,String apiKey) {
        return JSON.parseObject(JSON.toJSONString(deleteServerInfoStatus(servicePath,apiKey)));
    }

    /**
     * get方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @return 服务器返回的json信息字符串
     */
    public static ServerInfoStatus getServerInfoStatus(String servicePath,String apikey) {
        HttpGet get = new HttpGet(servicePath);
        // 填入apikey到HTTP header
		if(StringUtils.isNotBlank(apikey))
			get.setHeader("apikey", apikey);
        return analysisHttpResponse(get);
    }

    /**
     * 使用参数类型的get方式
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param paramObj    参数对象
     * @return 服务器信息
     */
    public static ServerInfoStatus utryGetServerInfoStatus(String servicePath, Object paramObj) {
        StringEntity paramesEntity = new StringEntity(JSON.toJSONString(paramObj), "UTF-8");
        paramesEntity.setContentEncoding("UTF-8");
        paramesEntity.setContentType("application/json");
        UtryHttpGet get = new UtryHttpGet(servicePath);
        get.setEntity(paramesEntity);
        return analysisHttpResponse(get);
    }

    /**
     * get方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @return 服务器返回的json信息字符串转换成的jsonobejct对象(单个对象，如果用户不想每次都要遍历)
     */
    public static JSONObject get(String servicePath,String apiKey) {
        return JSON.parseObject(JSON.toJSONString(getServerInfoStatus(servicePath,apiKey)));
    }

    /**
     * get方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param clazz       想要转成的类型class
     * @return 范型，指定类型的对象
     */
    public static <T> T getT(String servicePath, Class<T> clazz,String apiKey) {
        return getServerInfoStatus(servicePath,apiKey).getT(clazz);
    }

    /**
     * get方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param clazz       想要转成的类型class
     * @return 范型，指定类型的对象
     */
    public static <T> List<T> getListT(String servicePath, Class<T> clazz,String apiKey) {
        return getServerInfoStatus(servicePath,apiKey).getListT(clazz);
    }

    /**
     * post方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param params      json参数，是jsonObject形式
     * @return 服务器返回的json信息字符串
     */
    public static ServerInfoStatus postServerInfoStatus(String servicePath, Object params) {
    	String jsons = JSON.toJSONString(params);
    	System.out.println(jsons);
    	StringEntity paramesEntity = new StringEntity(jsons, "UTF-8");
        paramesEntity.setContentEncoding("UTF-8");
        paramesEntity.setContentType("application/json");
        HttpPost post = new HttpPost(servicePath);
        post.setEntity(paramesEntity);

        return analysisHttpResponse(post);
    }
    
    /**
	 * 添加数据，post方式访问服务器
     * @param servicePath 整个服务地址：例如：http://10.0.10.139:8060/uicsr/v1/restfulproword/add
     * @param json参数，是jsonObject形式
     * @return 服务器返回的json信息字符串
     */
    public static ServerInfoStatus postServerJSONStatus(String servicePath, String jsons,String apikey) {
    	System.out.println(jsons);
    	StringEntity paramesEntity = new StringEntity(jsons, "UTF-8");
        paramesEntity.setContentEncoding("UTF-8");
        paramesEntity.setContentType("application/json");
        HttpPost post = new HttpPost(servicePath);
        post.setEntity(paramesEntity);
        // 填入apikey到HTTP header
 		if(StringUtils.isNotBlank(apikey))
 			post.setHeader("apikey", apikey);
        return analysisHttpResponse(post);
    }

    /**
     * post方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param params      参数对象
     * @param clazz       指定的转换类型
     * @return 指定类型的list形式
     */
    public static <T> T postT(String servicePath, Object params, Class<T> clazz) {
        return postServerInfoStatus(servicePath, params).getT(clazz);
    }

    /**
     * post方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param params      参数对象
     * @param clazz       指定的转换类型
     * @return 指定类型的list形式
     */
    public static <T> List<T> postListT(String servicePath, Object params, Class<T> clazz) {
        return postServerInfoStatus(servicePath, params).getListT(clazz);
    }

    /**
     * post方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param params      json参数，是jsonObject形式
     * @return 服务器返回的json信息字符串转换成的jsonobject对象
     */
    public static JSONObject post(String servicePath, Object params) {
        return JSON.parseObject(JSON.toJSONString(postServerInfoStatus(servicePath, params)));
    }

    /**
     * put方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.10.139:8060/uicsr/v1/restfulproword/583
     * @param json参数，是jsonObject形式
     * @return ServerInfoStatus 返回数据和状态信息
     * @throws IllegalStateException
     */
    public static ServerInfoStatus putServerInfoStatus(String servicePath, Object params) {
    	String jsons = JSON.toJSONString(params);
    	System.out.println(jsons);
    	StringEntity paramesEntity = new StringEntity(jsons, "UTF-8");
        paramesEntity.setContentEncoding("UTF-8");
        paramesEntity.setContentType("application/json");

        HttpPut put = new HttpPut(servicePath);
        put.setEntity(paramesEntity);
        return analysisHttpResponse(put);
    }
    /**
     * 修改数据，put方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.10.139:8060/uicsr/v1/restfulproword/853
     * @param json参数，是jsonObject形式
     * @return ServerInfoStatus 返回数据和状态信息
     * @throws IllegalStateException
     */
    public static ServerInfoStatus putServerJSONStatus(String servicePath, String jsons,String apikey) {
    	System.out.println(jsons);
    	StringEntity paramesEntity = new StringEntity(jsons, "UTF-8");
    	paramesEntity.setContentEncoding("UTF-8");
    	paramesEntity.setContentType("application/json");
    	
    	HttpPut put = new HttpPut(servicePath);
    	put.setEntity(paramesEntity);
    	// 填入apikey到HTTP header
 		if(StringUtils.isNotBlank(apikey))
 			put.setHeader("apikey", apikey);
    	return analysisHttpResponse(put);
    }

    /**
     * put方式访问服务器的服务操作
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param params      参数对象
     * @param clazz       指定的转换类型
     * @return 指定类型对象
     */
    public static <T> T putT(String servicePath, Object params, Class<T> clazz) {
        return putServerInfoStatus(servicePath, params).getT(clazz);
    }

    /**
     * put方式访问服务器的服务操作
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param params      参数对象
     * @param clazz       指定的转换类型
     * @return 指定类型对象
     */
    public static <T> List<T> putListT(String servicePath, Object params, Class<T> clazz) {
        return putServerInfoStatus(servicePath, params).getListT(clazz);
    }

    /**
     * put方式访问服务器
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param params      参数对象
     * @return 服务器返回的json信息字符串转换成的jsonobject
     */
    public static JSONObject put(String servicePath, Object params) {
        return JSON.parseObject(JSON.toJSONString(putServerInfoStatus(servicePath, params)));
    }

    /**
     * 使用post的方式进行一个条件文件的下载(只支持单个文件的下载)
     *
     * @param servicePath 整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param params      需要传递的参数，进行下载文件的筛选
     * @return bufferedStream 下载的一个缓冲流
     */
    public static BufferedInputStream downLoadFile(String servicePath, Object params) {
        HttpPost post = new HttpPost(servicePath);
        StringEntity paramesEntity = new StringEntity(JSON.toJSONString(params), "UTF-8");
        paramesEntity.setContentEncoding("UTF-8");
        paramesEntity.setContentType("application/json");
        post.setEntity(paramesEntity);
        try {
            HttpResponse response = HTTP_CLIENT.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            return new BufferedInputStream(content);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传多个文件
     *
     * @param servicePath      整个服务地址：例如：http://10.0.2.52:9022/cas/login.do
     * @param requestParamFile springmvc的controller接受的文件参数的名字
     * @param filePath         文件地址的不确定参数[可以上传多个文件地址]
     * @return 服务器状态信息
     */
    public static ServerInfoStatus uploadFile(String servicePath, String requestParamFile, String... filePath) {
        ServerInfoStatus result = new ServerInfoStatus();
        HttpPost post = new HttpPost(servicePath);
        MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
        multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntity.setCharset(Charset.forName("UTF-8"));
        for (String tempFilePath : filePath) {
            multipartEntity.addPart(requestParamFile, new FileBody(new File(tempFilePath)));
        }
        post.setEntity(multipartEntity.build());
        try {
            HttpResponse postResponse = HTTP_CLIENT.execute(post);
            InputStream response = postResponse.getEntity().getContent();
            result.setServerInfo(convertStreamToString(response));
            StatusLine status = postResponse.getStatusLine();
            result.setServerStatus(new BeeBeeStatusLine(status.getProtocolVersion().getProtocol(),
                    status.getStatusCode(), status.getReasonPhrase()));
            post.abort();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 提取出来restful形式访问的时候的response的处理
     * 目的：提取访问结果封装成ServerInfoStatus中，便于我们的访问和操作
     *
     * @param request http的访问
     * @return 服务器信息
     */
    private static ServerInfoStatus analysisHttpResponse(HttpUriRequest request) {
        ServerInfoStatus result = new ServerInfoStatus();
        try {
            HttpResponse response = HTTP_CLIENT.execute(request);
            HttpEntity deleteEntity = response.getEntity();
            if (null != deleteEntity) {
                String serviceResult = null;
                InputStream inStream = deleteEntity.getContent();
                serviceResult = convertStreamToString(inStream);
                request.abort();
                result.setServerInfo(serviceResult);
            }
            StatusLine status = response.getStatusLine();
            result.setServerStatus(new BeeBeeStatusLine(status.getProtocolVersion().getProtocol(),
                    status.getStatusCode(), status.getReasonPhrase()));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 转换stream为string字符串
     *
     * @param is 输入流
     * @return 流形成的字符串
     */
    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return sb.toString();
    }
}
