package com.antzuhl.kibana.schedule;

import com.antzuhl.kibana.dao.QueryLogRepository;
import com.antzuhl.kibana.dao.SearchJobRepository;
import com.antzuhl.kibana.domain.SearchJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 定期校验是否有新的Job加入
 * @author AntzUhl
 * @Date 11:09
 */
@Slf4j
@Component
public class SearchSechedule {

    @Autowired
    private QueryLogRepository queryLogRepository;

    @Scheduled(cron = "0 0 0 ? * *" )
    public void refreshJob() {
        // 0点重新初始化任务
        DelayQueue.refresh(false);
        init();
    }

    public void init() {
        log.info("扫描任务，添加延迟队列");
        List<SearchJob> searchJobs = searchJobRepository.findAll();
        if (CollectionUtils.isEmpty(searchJobs)) {
            log.info("暂无任务处理..");
            return;
        }
        // build task
        log.info("task queue size: {}", searchJobs.size());
        searchJobs.stream().forEach(item -> {
            // 间隔时间
            if (!StringUtils.equals("python", item.getType())) {
                Calendar calendar = Calendar.getInstance();
                int currenthour = calendar.getTime().getHours();
                int currentminute = calendar.getTime().getMinutes();
                int currentsecond = calendar.getTime().getSeconds();
                // 当前时间
                long currentTime = currenthour * 60 * 60 + currentminute * 60 + currentsecond;

                calendar.setTime(item.getExecuteTime());
                int ehour = calendar.getTime().getHours();
                int eminute = calendar.getTime().getMinutes();
                int esecond = calendar.getTime().getSeconds();
                // 当天执行时间
                long executeTime = ehour * 60 * 60 + eminute * 60 + esecond;

                if (executeTime < currentTime) {
                    log.info("该任务已经过了今天的执行时间...{}", item);
                }
                // 延迟时间
                long time =  executeTime - currentTime;
                QueryTask queryTask = new QueryTask();
                queryTask.setSearchJob(item);
                queryTask.setQueryLogRepository(queryLogRepository);
                queryTask.setKey((int) (time));
                log.info("真实的延迟时间: {}", time);
                // 暂时使用30秒代替
//                queryTask.setKey(13);
                DelayQueue.addTask(queryTask);
            }
        });
    }


    @Autowired
    private SearchJobRepository searchJobRepository;

}

