package com.antzuhl.kibana;

import com.antzuhl.kibana.schedule.SearchSechedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


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

	@Value("${ke.version}")
	private String version;

	@Override
	public void run(String... args) {
		// 启动时需要计算所有Job下次执行时间与当前时间距离， 加入到延时队列
		searchSechedule.init();
//		SendMailUtil.Send();
//		TimeUnit.SECONDS.sleep(1);
//		RingBufferWheel.Task task = new SearchSechedule();
//		task.setKey(10);
//		DelayQueue.addTask(task);
		log.info("启动环境: {}", version);
	}

}

