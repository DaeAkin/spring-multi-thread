package com.example.multithread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

@SpringBootApplication
@EnableAsync // 스프링에게 @Async 메소드를 백그라운드 스레드 풀에서 실행하라고 알림.
public class MultiThreadApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiThreadApplication.class, args);
	}

	//튜닝 방법 https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/integration.html#scheduling-task-executor
	@Bean
	//Executor는 작업 등록과 작업 실행을 분리하는 표준적인 방법.
	// 프로듀서-컨슈머 패턴을 기반으로 하고 있다.
	public Executor taskExecutor() {
		//이 빈을 지정하지 않으면 SimpleAsyncTaskExecutor가 사용됨.
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("GithubLookup-");
		executor.initialize();
		return executor;
	}
}
