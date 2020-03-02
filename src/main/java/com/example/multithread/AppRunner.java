package com.example.multithread;

import com.example.book.SomeFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

//@Component
public class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final GitHubLookupService gitHubLookupService;
    private final SomeFuture someFuture;

    public AppRunner(GitHubLookupService gitHubLookupService, SomeFuture someFuture) {
        this.gitHubLookupService = gitHubLookupService;
        this.someFuture = someFuture;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("run");

        // Start the clock
//        long start = System.currentTimeMillis();
//
//        // Kick of multiple, asynchronous lookups
//        CompletableFuture<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
//        CompletableFuture<User> page2 = gitHubLookupService.findUser("CloudFoundry");
//        CompletableFuture<User> page3 = gitHubLookupService.findUser("Spring-Projects");
//
//        // Wait until they are all done
//        CompletableFuture.allOf(page1,page2,page3).join();
//
//        // Print results, including elapsed time
//        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
//        logger.info("--> " + page1.get());
//        logger.info("--> " + page2.get());
//        logger.info("--> " + page3.get());

    }

}
