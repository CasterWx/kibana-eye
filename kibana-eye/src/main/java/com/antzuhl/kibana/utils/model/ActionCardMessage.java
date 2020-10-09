package com.antzuhl.kibana.utils.model;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ActionCardMessage {

    /** 标题 */
    private String title;

    /** markdown格式内容 */
    private String text;

    /** 跳转按钮文本 */
    private String singleTitle;

    /** 跳转目标Url */
    private String singleUrl;

    /** 是否隐藏发送者头像 0:显示  1:隐藏 */
    private String hideAvatar = "0";

    /** 按钮排列方向  0:竖直 1:水平*/
    private String btnOrientation = "0";

    /** 是否艾特所有人 */
    private Boolean atAll = true;

    public String buildMessage() {
        JSONObject json = new JSONObject();
        json.put("msgtype", "actionCard");
        JSONObject linkJson = new JSONObject();
        linkJson.put("title", getTitle());
        linkJson.put("text", getText());
        linkJson.put("singleTitle", getSingleTitle());
        linkJson.put("singleUrl", getSingleUrl());
        linkJson.put("btnOrientation", getBtnOrientation());
        linkJson.put("hideAvatar", getHideAvatar());
        JSONObject atAllJson = new JSONObject();
        atAllJson.put("isAtAll", getAtAll());
        json.put("actionCard", linkJson);
        json.put("at", atAllJson);
        return json.toJSONString();
    }

    public Boolean valid() {
        return title!=null && text!=null
                && singleTitle!=null && singleUrl!=null;
    }

    public Boolean notValid() {
        return  !valid();
    }
}
