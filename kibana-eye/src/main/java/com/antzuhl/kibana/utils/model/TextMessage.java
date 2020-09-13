package com.antzuhl.kibana.utils.model;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TextMessage {

    /** 消息内容 */
    private String content;

    /** 是否艾特所有人 */
    private Boolean atAll = true;

    public String buildMessage() {
        JSONObject json = new JSONObject();
        json.put("msgtype", "text");
        JSONObject contentJson = new JSONObject();
        contentJson.put("content", getContent());
        JSONObject atAllJson = new JSONObject();
        atAllJson.put("isAtAll", getAtAll());
        json.put("text", contentJson);
        json.put("at", atAllJson);
        return json.toJSONString();
    }

    public Boolean valid() {
        return content != null;
    }

    public Boolean notValid() {
        return  !valid();
    }
}
