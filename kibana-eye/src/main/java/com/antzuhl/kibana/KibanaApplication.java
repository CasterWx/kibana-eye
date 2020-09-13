package com.antzuhl.kibana;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.antzuhl.kibana.core.RingBufferWheel;
import com.antzuhl.kibana.dao.QueryLogRepository;
import com.antzuhl.kibana.dao.SearchJobRepository;
import com.antzuhl.kibana.schedule.DelayQueue;
import com.antzuhl.kibana.schedule.SearchSechedule;
import com.antzuhl.kibana.utils.HttpClientUtils;
import com.antzuhl.kibana.utils.SendMailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Slf4j
@EnableScheduling
@EnableSwagger2
@SpringBootApplication
public class KibanaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(KibanaApplication.class, args);
	}

	@Autowired
	private SearchSechedule searchSechedule;

	@Override
	public void run(String... args) throws Exception {
		// 启动时需要计算所有Job下次执行时间与当前时间距离， 加入到延时队列
		searchSechedule.init();
//		SendMailUtil.Send();
//		TimeUnit.SECONDS.sleep(1);
//		RingBufferWheel.Task task = new SearchSechedule();
//		task.setKey(10);
//		DelayQueue.addTask(task);
	}

}

