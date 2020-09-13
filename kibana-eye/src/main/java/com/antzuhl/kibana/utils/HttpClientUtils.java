package com.antzuhl.kibana.utils;

/**
 * @author AntzUhl
 * @Date 11:42
 */
//

// Source code recreated from a .class file by IntelliJ IDEA

// (powered by Fernflower decompiler)

//

import com.antzuhl.kibana.dao.QueryLogRepository;
import com.antzuhl.kibana.domain.QueryLog;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;

public class HttpClientUtils {

    private static Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);

    private static PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();

    private static RequestConfig requestConfig;

    private static final int MAX_TOTAL = 100;

    private static final int MAX_TIMEOUT = 7000;

    private static final int CONNECT_TIMEOUT = 10000;

    private static final int SOCKET_TIMEOUT = 40000;

    private static final String CHARSET = "UTF-8";

    public HttpClientUtils() {

    }

    public static String doGet(String url) throws Exception {

        return doGet(url, new HashMap());

    }

    public static String doGet(String url, Map params) throws Exception {

        String result = null;

        if (StringUtils.isEmpty(url)) {

//            LOG.info("warn:doGet url is null or '' ");

            return result;

        } else {

            List pairList = new ArrayList(params.size());

            Iterator var4 = params.entrySet().iterator();

            while(var4.hasNext()) {

                Entry entry = (Entry)var4.next();

                NameValuePair pair = new BasicNameValuePair((String)entry.getKey(), entry.getValue().toString());

                pairList.add(pair);

            }

            CloseableHttpResponse response = null;

            InputStream instream = null;

            CloseableHttpClient httpclient = HttpClients.createDefault();

            try {
                URIBuilder URIBuilder = new URIBuilder(url);
                URIBuilder.addParameters(pairList);
                URI uri = URIBuilder.build();
                HttpGet httpGet = new HttpGet(uri);
                response = httpclient.execute(httpGet);
                int statusCode = response.getStatusLine().getStatusCode();
//                LOG.info("doGet statusCode:{}", statusCode);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, "UTF-8");
                }
            } catch (IOException var16) {

//                LOG.error("doGetIO ERROR :{}", var16.getMessage());

            } catch (URISyntaxException var17) {

//                LOG.error("doGet URISyntaxException :{}", var17.getMessage());

            } finally {

                if (null != instream) {

                    instream.close();

                }

                if (null != response) {

                    response.close();

                }

                if (null != httpclient) {

                    httpclient.close();

                }

//                LOG.info("closeinstream response httpClientconnection succ");

            }

            return result;

        }

    }

    public static String doGet(String url, Map params, String charset) throws Exception {

        String result = null;

        if (StringUtils.isEmpty(url)) {

//            LOG.info("warn:doGet url is null or '' ");

            return result;

        } else {

            List pairList = new ArrayList(params.size());

            Iterator var5 = params.entrySet().iterator();

            while(var5.hasNext()) {

                Entry entry = (Entry)var5.next();

                NameValuePair pair = new BasicNameValuePair((String)entry.getKey(), entry.getValue().toString());

                pairList.add(pair);

            }

            CloseableHttpResponse response = null;

            InputStream instream = null;

            CloseableHttpClient httpclient = HttpClients.createDefault();

            try {

                URIBuilder URIBuilder = new URIBuilder(url);

                URIBuilder.addParameters(pairList);

                URI uri = URIBuilder.build();

                HttpGet httpGet = new HttpGet(uri);

                response = httpclient.execute(httpGet);

                int statusCode = response.getStatusLine().getStatusCode();

//                LOG.info("doGet statusCode:{}", statusCode);

                HttpEntity entity = response.getEntity();

                if (entity != null) {

                    instream = entity.getContent();

                    result = IOUtils.toString(instream, charset);

                }

            } catch (IOException var17) {

//                LOG.error("doGetIO ERROR :{}", var17.getMessage());

            } catch (URISyntaxException var18) {

//                LOG.error("doGet URISyntaxException :{}", var18.getMessage());

            } finally {

                if (null != instream) {

                    instream.close();

                }

                if (null != response) {

                    response.close();

                }

                if (null != httpclient) {

                    httpclient.close();

                }

//                LOG.info("closeinstream response httpClientconnection succ");

            }

            return result;

        }

    }

    public static String doPost(String apiUrl) throws Exception {

        return doPost(apiUrl, (Map)(new HashMap()));

    }

    public static String doPost(String url, Map params) throws Exception {

        String result = null;

        String param = "";

        if (StringUtils.isEmpty(url)) {

////            LOG.info("warn:doPost url is null or '' ");

            return result;

        } else {

            List pairList = new ArrayList(params.size());

            Iterator var5 = params.entrySet().iterator();

            while(var5.hasNext()) {

                Entry entry = (Entry)var5.next();

                NameValuePair pair = new BasicNameValuePair((String)entry.getKey(), entry.getValue().toString());

                pairList.add(pair);

                if (param.equals("")) {

                    param = (String)entry.getKey() + "=" + entry.getValue();

                } else {

                    param = param + "&" + (String)entry.getKey() + "=" + entry.getValue();

                }

            }

////            LOG.info("http请求地址:" + url + "?" + param);

            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(url);

            CloseableHttpResponse response = null;

            InputStream instream = null;

            try {

                httpPost.setConfig(requestConfig);

                httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));

                response = httpClient.execute(httpPost);

                int statusCode = response.getStatusLine().getStatusCode();

////                LOG.info("doPost statusCode:{}", statusCode);

                HttpEntity entity = response.getEntity();

                if (entity != null) {

                    instream = entity.getContent();

                    result = IOUtils.toString(instream, "UTF-8");

////                    LOG.info("doPost Result:{}", result);

                }

            } catch (IOException var14) {

//                LOG.error("doPostERROR :{}", var14.getMessage());

            } finally {

                if (null != instream) {

                    instream.close();

                }

                if (null != response) {

                    response.close();

                }

                if (null != httpClient) {

                    httpClient.close();

                }

////                LOG.info("closeinstream response httpClientconnection succ");

            }

            return result;

        }

    }

    public static String doPost(String url, String xml) throws Exception {

        String result = null;

        if (StringUtils.isEmpty(url)) {

//            LOG.info("warn:doPost url is null or '' ");

            return result;

        } else {

            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(url);

            CloseableHttpResponse response = null;

            InputStream instream = null;

            try {

////                LOG.info("短信请求服务器地址:" + url + "?" + xml);

                httpPost.setConfig(requestConfig);

                httpPost.setEntity(new StringEntity(xml, "GBK"));

                response = httpClient.execute(httpPost);

                int statusCode = response.getStatusLine().getStatusCode();

////                LOG.info("doPost statusCode:{}", statusCode);

                HttpEntity entity = response.getEntity();

                if (entity != null) {

                    instream = entity.getContent();

                    result = IOUtils.toString(instream, "UTF-8");

                }

            } catch (IOException var12) {

////                LOG.error("doPostERROR :{}", var12.getMessage());

            } finally {

                if (null != instream) {

                    instream.close();

                }

                if (null != response) {

                    response.close();

                }

                if (null != httpClient) {

                    httpClient.close();

                }

////                LOG.info("closeinstream response httpClientconnection succ");

            }

            return result;

        }

    }

    public static String doPost(String url, List<NameValuePair> nvps) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(url)) {
////            LOG.info("warn:doPostByJson url is null or '' ");
            return result;
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            CloseableHttpResponse response = null;
            InputStream instream = null;
            try {
                httpPost.setConfig(requestConfig);
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                httpPost.setHeader("Connection", "keep-alive");
                httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
                httpPost.setHeader("Accept", "*/*");

                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
////                LOG.info("doPost statusCode:{}", statusCode);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, "UTF-8");
                }
            } catch (IOException var13) {
////                LOG.error("doPost BY JSON ERROR :{}", var13.getMessage());
            } finally {
                if (null != instream) {
                    instream.close();
                }
                if (null != response) {
                    response.close();
                }
                if (null != httpClient) {
                    httpClient.close();
                }
            }
            return result;
        }

    }

    public static String doPostPay(String url, Object json) throws Exception {

        String result = null;

        if (StringUtils.isEmpty(url)) {
////            LOG.info("warn:doPostByJson url is null or '' ");
            return result;
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            CloseableHttpResponse response = null;
            InputStream instream = null;
            try {
                httpPost.setConfig(requestConfig);
                StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
                httpPost.setHeader("kbn-version", "7.4.2");
                httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
                httpPost.setHeader("Accept", "application/json");
                stringEntity.setContentEncoding("UTF-8");
                stringEntity.setContentType("application/json");

                httpPost.setEntity(stringEntity);

                response = httpClient.execute(httpPost);

                int statusCode = response.getStatusLine().getStatusCode();

////                LOG.info("doPost statusCode:{}", statusCode);

                HttpEntity entity = response.getEntity();

                if (entity != null) {

                    instream = entity.getContent();

                    result = IOUtils.toString(instream, "UTF-8");

                }

            } catch (IOException var13) {

////                LOG.error("doPost BY JSON ERROR :{}", var13.getMessage());

            } finally {

                if (null != instream) {

                    instream.close();

                }

                if (null != response) {

                    response.close();

                }

                if (null != httpClient) {

                    httpClient.close();

                }

            }

            return result;

        }

    }

    public static QueryLog doPostEs(String indexName, String application, String type, String url, Object json) throws Exception {
        String result = null;
        QueryLog queryLog = new QueryLog();
        if (StringUtils.isEmpty(url)) {
            return null;
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            CloseableHttpResponse response = null;
            InputStream instream = null;
            try {
                httpPost.setConfig(requestConfig);
                StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
//                httpPost.setHeader("kbn-version", "7.4.2");
                httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
                httpPost.setHeader("Accept", "application/json");
                stringEntity.setContentEncoding("UTF-8");
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
                response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                LOG.info("doPost statusCode:{}", statusCode);
                queryLog.setApplication(application);
                queryLog.setStatus(statusCode);
                queryLog.setType(type);
                queryLog.setName(indexName);
                queryLog.setCreateTime(Calendar.getInstance().getTime());
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, "UTF-8");
                }
                queryLog.setQuery(result);
            } catch (IOException var13) {
                LOG.error("doPost BY JSON ERROR :{}", var13.getMessage());
            } finally {
                if (null != instream) {
                    instream.close();
                }
                if (null != response) {
                    response.close();
                }
                if (null != httpClient) {
                    httpClient.close();
                }
            }
            return queryLog;
        }

    }

    public static String doPostSSL(String apiUrl, Map params) throws Exception {

        String result = null;

        if (StringUtils.isEmpty(apiUrl)) {

////            LOG.info("warn:doPostSSL url is null or '' ");

            return result;

        } else {

            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();

            HttpPost httpPost = new HttpPost(apiUrl);

            CloseableHttpResponse response = null;

            InputStream instream = null;

            try {

                httpPost.setConfig(requestConfig);

                List pairList = new ArrayList(params.size());

                Iterator var8 = params.entrySet().iterator();

                Entry entry;

                while(var8.hasNext()) {

                    entry = (Entry)var8.next();

                    NameValuePair pair = new BasicNameValuePair((String)entry.getKey(), entry.getValue().toString());

                    pairList.add(pair);

                }

                httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));

                response = httpClient.execute(httpPost);

                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != 200) {

////                    LOG.info("doPostSSL statusCode:{}", statusCode);

                    entry = null;

                    return String.valueOf(entry);

                }

                HttpEntity entity = response.getEntity();

                if (entity != null) {

                    instream = entity.getContent();

                    result = IOUtils.toString(instream, "UTF-8");

                }

            } catch (Exception var14) {

////                LOG.error("doPostSSL ERROR :{}", var14.getMessage());

            } finally {

                if (null != instream) {

                    instream.close();

                }

                if (null != response) {

                    response.close();

                }

                if (null != httpClient) {

                    httpClient.close();

                }

////                LOG.info("closeinstream response httpClientconnection succ");

            }

            return result;

        }

    }

    public static String doPostSSL(String apiUrl, Object json) throws Exception {

        String result = null;

        if (StringUtils.isEmpty(apiUrl)) {

////            LOG.info("warn:doPostSSL By Json url is null or '' ");

            return result;

        } else {

            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();

            HttpPost httpPost = new HttpPost(apiUrl);

            CloseableHttpResponse response = null;

            InputStream instream = null;

            HttpEntity entity;

            try {

                httpPost.setConfig(requestConfig);

                StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");

                stringEntity.setContentEncoding("UTF-8");

                stringEntity.setContentType("application/json");

                httpPost.setEntity(stringEntity);

                response = httpClient.execute(httpPost);

                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {

                    entity = response.getEntity();

                    if (entity != null) {

                        instream = entity.getContent();

                        result = IOUtils.toString(instream, "UTF-8");

                    }

                    return result;

                }

//                LOG.info("doPostSSL by json statusCode:{}", statusCode);

                entity = null;

            } catch (Exception var13) {

//                LOG.error("doPostSSL BY JSON ERROR :{}", var13.getMessage());

                return result;

            } finally {

                if (null != instream) {

                    instream.close();

                }

                if (null != response) {

                    response.close();

                }

                if (null != httpClient) {

                    httpClient.close();

                }

//                LOG.info("closeinstream response httpClientconnection succ");

            }

            return String.valueOf(entity);

        }

    }

    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {

        SSLConnectionSocketFactory sslsf = null;

        try {

            SSLContext sslContext = (new SSLContextBuilder()).loadTrustMaterial((KeyStore)null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    return true;

                }

            }).build();

            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                public boolean verify(String arg0, SSLSession arg1) {

                    return true;

                }

                public void verify(String host, SSLSocket ssl) throws IOException {

                }

                public void verify(String host, X509Certificate cert) throws SSLException {

                }

                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {

                }

            });

        } catch (GeneralSecurityException var2) {

//            LOG.error("createSSLConnSocketFactory ERROR :{}", var2.getMessage());

        }

        return sslsf;

    }

    public static String doPostPay(String url, Object json, String authorization) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(url)) {
//            LOG.info("warn:doPostByJson url is null or '' ");
            return result;
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            CloseableHttpResponse response = null;
            InputStream instream = null;
            try {
                httpPost.setConfig(requestConfig);
                StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
                httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Authorization", authorization);
                stringEntity.setContentEncoding("UTF-8");
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
                response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
//                LOG.info("doPost statusCode:{}", statusCode);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, "UTF-8");
                }
            } catch (IOException var14) {
//                LOG.error("doPost BY JSON ERROR :{}", var14.getMessage());
            } finally {
                if (null != instream) {
                    instream.close();
                }
                if (null != response) {
                    response.close();
                }
                if (null != httpClient) {
                    httpClient.close();
                }
            }
            return result;
        }

    }

    public static String doPostPayUpgraded(String url, Object json, String authorization) throws Exception {

        String result = null;

        if (StringUtils.isEmpty(url)) {

//            LOG.info("新支付接口url不能为空！");

            return result;

        } else {

            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(url);

            CloseableHttpResponse response = null;

            InputStream instream = null;

            try {

                httpPost.setConfig(requestConfig);

                StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");

                httpPost.setHeader("Accept", "application/json");

                httpPost.setHeader("Content-Type", "application/json;charset=utf-8");

                httpPost.setHeader("Authorization", authorization);

                stringEntity.setContentEncoding("UTF-8");

                stringEntity.setContentType("application/json");

                httpPost.setEntity(stringEntity);

                response = httpClient.execute(httpPost);

                int statusCode = response.getStatusLine().getStatusCode();

//                LOG.info("新支付请求状态 statusCode:{}", statusCode);

                HttpEntity entity = response.getEntity();

                if (entity != null) {

                    instream = entity.getContent();

                    result = IOUtils.toString(instream, "UTF-8");

                }

            } catch (IOException var14) {

//                LOG.error("新支付接口发送异常:{}", var14.getMessage());

            } finally {

                if (null != instream) {

                    instream.close();

                }

                if (null != response) {

                    response.close();

                }

                if (null != httpClient) {

                    httpClient.close();

                }

            }

            return result;

        }

    }

    static {

        connMgr.setMaxTotal(100);

        connMgr.setDefaultMaxPerRoute(100);

        Builder configBuilder = RequestConfig.custom();

        configBuilder.setConnectTimeout(30000);

        configBuilder.setSocketTimeout(30000);

        configBuilder.setConnectionRequestTimeout(30000);

        configBuilder.setStaleConnectionCheckEnabled(true);

        requestConfig = configBuilder.build();

    }

}

