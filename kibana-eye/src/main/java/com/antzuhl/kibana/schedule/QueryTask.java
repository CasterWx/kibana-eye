package com.antzuhl.kibana.schedule;

import com.antzuhl.kibana.core.RingBufferWheel;
import com.antzuhl.kibana.dao.QueryLogRepository;
import com.antzuhl.kibana.domain.QueryLog;
import com.antzuhl.kibana.domain.SearchJob;
import com.antzuhl.kibana.parser.RPCQueryParser;
import com.antzuhl.kibana.parser.SQLQueryParser;
import com.antzuhl.kibana.parser.WebappTimeParser;
import com.antzuhl.kibana.utils.DingPublishUtils;
import com.antzuhl.kibana.utils.HttpClientUtils;
import com.antzuhl.kibana.utils.RequestUtil;
import com.antzuhl.kibana.utils.model.LinkMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * @author AntzUhl
 * @Date 17:13
 */
@Slf4j
@Component
public class QueryTask extends RingBufferWheel.Task {

    @Value("${ke.host}")
    private String host;

    private SearchJob searchJob;

    private QueryLogRepository queryLogRepository;

    public void setSearchJob(SearchJob searchJob) {
        this.searchJob = searchJob;
    }

    public void setQueryLogRepository(QueryLogRepository queryLogRepository) {
        this.queryLogRepository = queryLogRepository;
    }

    @Value("${ke.es.url}")
    private String esBaseUrl;

    @Override
    public void run() {
        log.info("开始处理...{}, {}", searchJob.getIndexName(), searchJob.getApplication());
        String url = RequestUtil.buildRequestUrl(esBaseUrl, searchJob.getIndexName());
        String query = searchJob.getQuery();
        //  get local time,
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        // get stop time
        long lte = calendar.getTime().getTime();
        calendar.set(Calendar.HOUR_OF_DAY,-24);
        // get start time
        long gte = calendar.getTime().getTime();
        log.info("query start time {}, end time {}", gte, lte);
        if (StringUtils.equals(searchJob.getType(), "rpc")) {
            query = query.replace("${application_query_condition}", "json.type:0 AND app_name:" + searchJob.getApplication()+"");
        } else if (StringUtils.equals(searchJob.getType(), "sql")) {
            query = query.replace("${application_query_condition}", "app:" + searchJob.getApplication()+"");
        } else if (StringUtils.equals(searchJob.getType(), "webapp_time")) {
            query = query.replace("${application_query_condition}", "log_uuid:" + searchJob.getApplication()+"");
        } else if (StringUtils.equals(searchJob.getType(), "http_status")) {
            query = query.replace("${application_query_condition}", "log_uuid:" + searchJob.getApplication()+"");
        }
        query = query.replace("\"${gte}\"", String.valueOf(gte));
        query = query.replace("\"${lte}\"", String.valueOf(lte));
        System.out.println(query);
        // request 解析器
        try {
            QueryLog response = HttpClientUtils.doPostEs(searchJob.getIndexName(),
                    searchJob.getApplication(), searchJob.getType(), url, query);
//            System.out.println(response.getQuery().length());
            queryLogRepository.save(response);
            // TODO 处理执行结果
            int notice = searchJob.getNotice();
            if (notice == 1) {
                // 邮件
                log.info("发送邮件");
//                SendMailUtil.send(searchJob.getApplication() + " " + searchJob.getSummary(),
//                        parserType(response), searchJob.getSendTo());
            } else if (notice == 2) {
                // ding
                DingPublishUtils.publish(LinkMessage.builder()
                        .title("Job:" + searchJob.getApplication())
                        .text(searchJob.getSummary() + " ")
                        .atAll(false)
                        .messageUrl("http://" + host + "/#/report-log/log/?id=" + response.getId()).build());
            } else if (notice == 3) {
                // all
                DingPublishUtils.publish(LinkMessage.builder()
                        .title("Job:" + searchJob.getApplication())
                        .text(searchJob.getSummary() + " ")
                        .atAll(false)
                        .messageUrl("http://" + host + "/#/report-log/log/?id=" + response.getId()).build());
                // 邮件
//                SendMailUtil.send(searchJob.getApplication() + " " + searchJob.getSummary(),
//                        parserType(response), searchJob.getSendTo());
                log.info("发送邮件");
            }
        } catch (Exception e) {
            log.error("HttpClientUtils doPostError {}", e.getMessage());
        }
    }

    public static String parserType(QueryLog queryLog) {
        if (StringUtils.equals(queryLog.getType(), "sql")) {
            return SQLQueryParser.parser(queryLog.getApplication(), queryLog.getQuery());
        } else if (StringUtils.equals(queryLog.getType(), "rpc")) {
            return RPCQueryParser.parser(queryLog.getApplication(), queryLog.getQuery());
        } else if (StringUtils.equals(queryLog.getType(), "webapp_time")) {
            return WebappTimeParser.parser(queryLog.getApplication(), queryLog.getQuery());
        }
        return "";
    }
}