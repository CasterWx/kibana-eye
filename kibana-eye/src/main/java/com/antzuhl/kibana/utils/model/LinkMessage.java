package com.antzuhl.kibana.utils.model;


import com.alibaba.fastjson.JSONObject;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LinkMessage {

    /** 消息标题 */
    private String title;

    /** 消息内容 */
    private String text;

    /** 图片Url */
    private String picUrl = "";

    /** 消息点击跳转Url */
    private String messageUrl;

    /** 是否艾特所有人 */
    private Boolean atAll = true;

    public String buildMessage() {
        JSONObject json = new JSONObject();
        json.put("msgtype", "link");
        JSONObject linkJson = new JSONObject();
        linkJson.put("title", getTitle());
        linkJson.put("text", getText());
        linkJson.put("picUrl", getPicUrl());
        linkJson.put("messageUrl", getMessageUrl());
        JSONObject atAllJson = new JSONObject();
        atAllJson.put("isAtAll", getAtAll());
        json.put("link", linkJson);
        json.put("at", atAllJson);
        return json.toJSONString();
    }

    public Boolean valid() {
        return title!=null && text!=null
                && messageUrl!=null;
    }

    public Boolean notValid() {
        return  !valid();
    }
}
