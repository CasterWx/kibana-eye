package com.antzuhl.kibana.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.antzuhl.kibana.utils.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;

/**
 * 钉钉推送工具类
 *
 * @see TextMessage
 * @see LinkMessage
 * @see MarkdownMessage
 * @see ActionCardMessage
 * */
@Slf4j
public class DingPublishUtils {

    /** 钉钉robot开放API */
    private static final String BASE_DING_URL = "https://oapi.dingtalk.com/robot/send?access_token=";

    /** 默认token */
    private static final String DEFAULT_TOKEN = "761bdddcba1f3bc8abfad0500c0337a8a764d30c2b2dfb8b1137a277d19efaa5";

    /**
     * 推送文本类型消息
     * @param textMessage {@link TextMessage} 文本消息实体
     *               必填参数  content
     * @return {@link DingResultMessage} 钉钉调用返回结果 {"errcode":0, "errmsg":"ok"}
     * */
    public static DingResultMessage publish(TextMessage textMessage) {
        if (textMessage == null || textMessage.notValid()) {
            return DingResultMessage.createEmptyResult();
        }
        return publish(textMessage, DEFAULT_TOKEN);
    }

    /**
     * 推送链接类型消息
     * @param linkMessage {@link LinkMessage} 链接消息实体
     *               必填参数  title text messageUrl
     * @return {@link DingResultMessage} 钉钉调用返回结果 {"errcode":0, "errmsg":"ok"}
     * */
    public static DingResultMessage publish(LinkMessage linkMessage) {
        if (linkMessage == null || linkMessage.notValid()) {
            return DingResultMessage.createEmptyResult();
        }
        return publish(linkMessage, DEFAULT_TOKEN);
    }

    /**
     * 推送markdown类型消息
     * @param markdownMessage {@link MarkdownMessage} markdown消息实体
     *               必填参数  title text
     * @return {@link DingResultMessage} 钉钉调用返回结果 {"errcode":0, "errmsg":"ok"}
     * */
    public static DingResultMessage publish(MarkdownMessage markdownMessage) {
        if (markdownMessage == null || markdownMessage.notValid()) {
            return DingResultMessage.createEmptyResult();
        }
        return publish(markdownMessage, DEFAULT_TOKEN);
    }

    /**
     * 推送跳转卡片类型消息
     * @param actionCardMessage {@link ActionCardMessage} 跳转卡片消息实体
     *               必填参数  title text singleTitle singleURL
     * @return {@link DingResultMessage} 钉钉调用返回结果 {"errcode":0, "errmsg":"ok"}
     * */
    public static DingResultMessage publish(ActionCardMessage actionCardMessage) {
        if (actionCardMessage == null || actionCardMessage.notValid()) {
            return DingResultMessage.createEmptyResult();
        }
        return publish(actionCardMessage, DEFAULT_TOKEN);
    }

    /**
     * 推送文本类型消息
     * @param textMessage {@link TextMessage} 文本消息实体
     *               必填参数  content
     * @param token Token值，创建机器人时生成
     * @return {@link DingResultMessage} 钉钉调用返回结果 {"errcode":0, "errmsg":"ok"}
     * */
    public static DingResultMessage publish(TextMessage textMessage, String token) {
        if (textMessage == null || textMessage.notValid()) {
            return DingResultMessage.createEmptyResult();
        }
        return publish(textMessage.buildMessage(), token);
    }

    /**
     * 推送链接类型消息
     * @param linkMessage {@link LinkMessage} 链接消息实体
     *               必填参数  title text messageUrl
     * @param token Token值，创建机器人时生成
     * @return {@link DingResultMessage} 钉钉调用返回结果 {"errcode":0, "errmsg":"ok"}
     * */
    public static DingResultMessage publish(LinkMessage linkMessage, String token) {
        if (linkMessage == null || linkMessage.notValid()) {
            return DingResultMessage.createEmptyResult();
        }
        return publish(linkMessage.buildMessage(), token);
    }

    /**
     * 推送markdown类型消息
     * @param markdownMessage {@link MarkdownMessage} markdown消息实体
     *               必填参数  title text
     * @param token Token值，创建机器人时生成
     * @return {@link DingResultMessage} 钉钉调用返回结果 {"errcode":0, "errmsg":"ok"}
     * */
    public static DingResultMessage publish(MarkdownMessage markdownMessage, String token) {
        if (markdownMessage == null || markdownMessage.notValid()) {
            return DingResultMessage.createEmptyResult();
        }
        return publish(markdownMessage.buildMessage(), token);
    }

    /**
     * 推送跳转卡片类型消息
     * @param actionCardMessage {@link ActionCardMessage} 跳转卡片消息实体
     *               必填参数  title text singleTitle singleURL
     * @param token Token值，创建机器人时生成
     * @return {@link DingResultMessage} 钉钉调用返回结果 {"errcode":0, "errmsg":"ok"}
     * */
    public static DingResultMessage publish(ActionCardMessage actionCardMessage, String token) {
        if (actionCardMessage == null || actionCardMessage.notValid()) {
            return DingResultMessage.createEmptyResult();
        }
        return publish(actionCardMessage.buildMessage(), token);
    }

    private static final CloseableHttpClient httpClient;
    private static final HttpPost httpPost;
    private static final RequestConfig requestConfig;

    static {
        httpClient = HttpClients.createDefault();
        httpPost = new HttpPost();
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(2000)
                .setConnectionRequestTimeout(2000)
                .setSocketTimeout(2000).build();
        httpPost.setConfig(requestConfig);
    }

    /**
     * @param content {@link String} json化后的内容
     * @param token {@link String} 机器人token，有默认值
     * @return {@link DingResultMessage} 钉钉调用返回结果 {"errcode":0, "errmsg":"ok"}
     * */
    private static DingResultMessage publish(String content, String token) {
        httpPost.setURI(URI.create(BASE_DING_URL + token));
        StringEntity entity = new StringEntity(content,
                Charsets.UTF_8);
        entity.setContentEncoding("UTF-8");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = null;
        DingResultMessage message = new DingResultMessage();
        try {
            response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            String resultStr = EntityUtils.toString(resEntity, Charsets.UTF_8);
            JSONObject messageObj = JSON.parseObject(resultStr);

            message = DingResultMessage.builder()
                    .errCode(messageObj.getInteger("errcode"))
                    .errMessage(messageObj.getString("errmsg"))
                    .build();
            EntityUtils.consumeQuietly(response.getEntity());
        } catch (Exception e) {
            log.error("DingPublishUtils.publish content:{}, token:{}", content, token, e);
        }
        System.out.println(message.toString());
        return message.valid();
    }

}

