package com.antzuhl.kibana.utils.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * 服务端行为日志数据。
 * <p/>
 * 将该数据发往kafka，大数据平台进行分析使用。
 * 
 * @author <a href="http://blog.warningrc.com">王宁</a>
 * @date 2020-02-19 16:00
 */
@Data
public class ActionLog implements Serializable {

    /**
     * 当前日志行为类别
     */
    private final String logtype;

    /**
     * 当前行为发生的时间戳
     */
    private final long serverts = System.currentTimeMillis();

    /**
     * 当前行为数据
     */
    private final Object data;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
