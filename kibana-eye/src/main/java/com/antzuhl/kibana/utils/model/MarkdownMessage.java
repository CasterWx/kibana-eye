package com.antzuhl.kibana.utils.model;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class MarkdownMessage {

    /** 标题 */
    private String title;

    /** markdown格式内容 */
    private String text;

    /** 是否艾特所有人 */
    private Boolean atAll = true;

    public String buildMessage() {
        JSONObject json = new JSONObject();
        json.put("msgtype", "markdown");
        JSONObject linkJson = new JSONObject();
        linkJson.put("title", getTitle());
        linkJson.put("text", getText());
        JSONObject atAllJson = new JSONObject();
        atAllJson.put("isAtAll", getAtAll());
        json.put("markdown", linkJson);
        json.put("at", atAllJson);
        return json.toJSONString();
    }

    public Boolean valid() {
        return title!=null && text!=null;
    }

    public Boolean notValid() {
        return  !valid();
    }
}
