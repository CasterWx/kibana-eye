package com.antzuhl.kibana.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.antzuhl.kibana.common.Constants;
import com.antzuhl.kibana.dao.IndexDetailRepository;
import com.antzuhl.kibana.dao.LoginInfoRepository;
import com.antzuhl.kibana.domain.IndexDetail;
import com.antzuhl.kibana.domain.LoginInfo;
import com.antzuhl.kibana.utils.DingPublishUtils;
import com.antzuhl.kibana.utils.HttpClientUtils;
import com.antzuhl.kibana.utils.model.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author AntzUhl
 * @Date 16:56
 */
@Slf4j
@Component
public class PullSechedule {

    @Autowired
    IndexDetailRepository indexDetailRepository;

    @Autowired
    LoginInfoRepository loginInfoRepository;

//    @Scheduled(fixedDelay = 3000)
    @Scheduled(cron = "0 0 9 ? * *" )
    public void reportCurrentTime() {
        //  get local time,
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        // get stop time
        long lte = calendar.getTime().getTime();
        calendar.set(Calendar.HOUR_OF_DAY, -24);
//        calendar.set(Calendar.DAY_OF_MONTH, -3);
        // get start time
        long gte = calendar.getTime().getTime();
        IndexDetail indexDetail = new IndexDetail();
        indexDetail.setName("*jiagou-dubbo-login-*");
        indexDetail.setTotal(0);
        //bizlog-jiagou-dubbo-login-2020.09.06
        String req = Constants.defaultTRequest.replace("\"${gte}\"", String.valueOf(gte));
        req = req.replace("\"${lte}\"", String.valueOf(lte));
        pullLog(indexDetail, req);
        System.out.println("OK~" );
    }

    public void pullLog(IndexDetail index, Object object) {  // bizlog-jiagou-dubbo-login-2020.09.06
        try {
            String response = HttpClientUtils.doPostPay(Constants.BASE_URL + "path=%2F"
                    + index.getName() + "%2F_search&method=POST", object);
            JSONObject res = JSON.parseObject(response);
            JSONObject hits = res.getJSONObject("hits");
            // 获取条数 + 更新条数
            Integer nums = hits.getJSONObject("total").getInteger("value");
            log.info("索引条目: {}, 存储条目: {}", nums, index.getTotal());
            if (nums > index.getTotal()) {
                // 有新数据，更新
                JSONArray array = hits.getJSONArray("hits");
                int length = array.size();
                for (int i=0; i<length; i++) {
                    LoginInfo loginInfo = new LoginInfo();
                    JSONObject source = array.getJSONObject(i).getJSONObject("_source");
                    // 处理数据
                    String traceId = source.getString("trace_id").split("\\.")[2];
                    Long trace = Long.valueOf(traceId);
                    Optional<LoginInfo> log = loginInfoRepository.findById(trace);
                    if (log.isPresent()) {
                        // 存储过的消息
                        System.out.print(".");
                        continue;
                    }
                    String log_time = source.getString("log_time");
                    String content = source.getString("content");
                    String ip = source.getString("ip");
                    String koo_env = source.getString("koo_env");
                    loginInfo.setTraceId(Long.valueOf(traceId));
                    loginInfo.setLogTime(log_time);
                    loginInfo.setIp(ip);
                    loginInfo.setEnv(koo_env);
                    //appId[160] mobile[13313395126] usage[5] user_mobile_verify_error_three"
                    if (content.contains("user_mobile_verify_error_five")) {
                        loginInfo.setErrorNum(5);
                    } else if (content.contains("user_mobile_verify_error_three")) {
                        loginInfo.setErrorNum(3);
                    }
                    if (content.contains("appId[160]")) {
                        loginInfo.setClientType("新东方中小学网校");
                    } else if (content.contains("appId[161]")){
                        loginInfo.setClientType("新东方中小学网校-老师端");
                    } else if (content.contains("appId[172]")){
                        loginInfo.setClientType("新东方中小学网校-PC端");
                    } else if (content.contains("appId[182]")){
                        loginInfo.setClientType("新东方在线中小学");
                    } else {
                        loginInfo.setClientType(getAppId(content));
                    }
                    loginInfo.setMobile(getMobile(content));
                    loginInfoRepository.save(loginInfo);
                    //短信验证码登录失败
                    DingPublishUtils.publish(TextMessage.builder()
                            .content("[~]" + loginInfo.toString()).atAll(false).build());
                }
            }
            index.setTotal(nums);
//            indexDetailRepository.save(index);
            log.info("更新完成: {}", index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Pattern mobile_pattern = Pattern.compile("mobile\\[1\\d{10}");

    public static String getMobile(String src) {
        Matcher m = mobile_pattern.matcher(src);
        if (m.find()) {
            return m.group().replace("mobile[", "");
        }
        return "null";
    }

    private  static final Pattern app_pattern = Pattern.compile("appId\\[\\d+]");
    public static String getAppId(String src) {
        Matcher m = app_pattern.matcher(src);
        if (m.find()) {
            return m.group();
        }
        return null;
    }
}
