package com.example.book;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FutureTestRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(FutureTestRunner.class);

    private final SomeFuture someFuture;

    public FutureTestRunner(SomeFuture someFuture) {
        this.someFuture = someFuture;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("run");
        someFuture.get();
    }
}
