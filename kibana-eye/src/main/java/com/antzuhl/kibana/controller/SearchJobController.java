package com.antzuhl.kibana.controller;

import com.antzuhl.kibana.common.Response;
import com.antzuhl.kibana.controller.request.CreateJobRequest;
import com.antzuhl.kibana.controller.response.EditJobResponse;
import com.antzuhl.kibana.dao.QueryLogRepository;
import com.antzuhl.kibana.dao.SearchJobRepository;
import com.antzuhl.kibana.domain.SearchJob;
import com.antzuhl.kibana.schedule.DelayQueue;
import com.antzuhl.kibana.schedule.QueryTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author AntzUhl
 * @Date 11:05
 */
@Slf4j
@RestController
@RequestMapping("/job")
public class SearchJobController {

    @Autowired
    private SearchJobRepository searchJobRepository;

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public Response all() {
        List<SearchJob> searchJobs = searchJobRepository.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("total", searchJobs.size());
        map.put("item", searchJobs);
        return Response.success("success", map);
    }

    @GetMapping("/get")
    public Response get(@RequestParam("id") Long id){
        Optional<SearchJob> optional = searchJobRepository.findById(id);
        if (!optional.isPresent()) {
            Response.error("没有这个Job");
        }
        SearchJob searchJob = optional.get();
        EditJobResponse editJobResponse = new EditJobResponse();
        editJobResponse.setApplication(searchJob.getApplication());
        editJobResponse.setIndexName(searchJob.getIndexName());
        editJobResponse.setContent(searchJob.getQuery());
        editJobResponse.setContentShort(searchJob.getSummary());
        editJobResponse.setCycle(searchJob.getCycle());
        editJobResponse.setExecuteTime(searchJob.getExecuteTime());
        editJobResponse.setSendTo(searchJob.getSendTo());
        editJobResponse.setSendCc(searchJob.getSendCc());
        editJobResponse.setRegion(searchJob.getType());
        List<String> notic = new ArrayList<>();
        if (searchJob.getNotice() == 1) {
            notic.add("邮箱");
        } else if (searchJob.getNotice() == 2) {
            notic.add("钉钉");
        } else if (searchJob.getNotice() == 3) {
            notic.add("钉钉");
            notic.add("邮箱");
        }
        editJobResponse.setNotType(notic);
        return Response.success("success", editJobResponse);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Response addJob(@RequestBody CreateJobRequest createJobRequest) {
        log.info("create Job : {}", createJobRequest);
        if (createJobRequest == null) {
            return Response.error("参数错误");
        }
        SearchJob searchJob = new SearchJob();
        log.info("update : {}", createJobRequest);
        searchJob.setId(createJobRequest.getId());
        searchJob.setApplication(createJobRequest.getApplication());
        searchJob.setIndexName(createJobRequest.getIndexName());
        searchJob.setType(createJobRequest.getRegion());
        searchJob.setQuery(createJobRequest.getContent().replace("<p>", "")
                .replace("</p>", "").replace("&lt;", "<")
                .replace("&gt;", ">"));
        searchJob.setSendTo(createJobRequest.getSendTo());
        searchJob.setSendCc(createJobRequest.getSendCc());
        searchJob.setDeleted(0);
        searchJob.setExecuteTime(createJobRequest.getExecuteTime());
        searchJob.setSummary(createJobRequest.getContentShort());
        searchJob.setCycle(createJobRequest.getCycle());
        List<String> not = createJobRequest.getNotType();
        if (not != null) {
            if (not.size() == 2) {
                searchJob.setNotice(3);
            } else if (not.contains("邮箱")) {
                searchJob.setNotice(1);
            } else if (not.contains("钉钉")) {
                searchJob.setNotice(2);
            }
        }
        SearchJob save = searchJobRepository.save(searchJob);
        if (save == null)
            return Response.error("添加Job失败");
        if (!StringUtils.equals(save.getType(), "python")) {
            long time = save.getExecuteTime().getTime() - Calendar.getInstance().getTime().getTime();
            QueryTask queryTask = new QueryTask();
            queryTask.setSearchJob(save);
            queryTask.setQueryLogRepository(queryLogRepository);
            queryTask.setKey((int) (time/1000));
            log.info("真实的延迟时间: {}", time/1000);
            // 暂时使用30秒代替
//            queryTask.setKey(13);
            DelayQueue.addTask(queryTask);
        }
        return Response.success("success", save);
    }

    @GetMapping(value = "/run")
    public Response run(@RequestParam("id") Long id) {
        Optional<SearchJob> searchJob = searchJobRepository.findById(id);
        if (searchJob.isPresent()) {
            SearchJob item = searchJob.get();
            if (!StringUtils.equals("python", item.getType())) {
                QueryTask queryTask = new QueryTask();
                queryTask.setSearchJob(item);
                queryTask.setQueryLogRepository(queryLogRepository);
                // 暂时使用30秒代替
                queryTask.setKey(0);
                DelayQueue.addTask(queryTask);
                return Response.success("任务提交成功");
            } else if (StringUtils.equals("python", item.getType())){
                // 如果是python脚本任务
                Process proc = null;
                try {
                    proc = Runtime.getRuntime().exec("python " + item.getApplication());
                    proc.waitFor();
                } catch (Exception e) {
                    return Response.error("执行脚本错误");
                }
                System.out.println("ok");
                return Response.success("任务提交成功");
            }
        }
        return Response.error("没有这个Job");
    }

    @Autowired
    private QueryLogRepository queryLogRepository;
}
