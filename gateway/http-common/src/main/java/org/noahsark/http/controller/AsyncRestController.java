package org.noahsark.http.controller;

import org.noahsark.http.common.Constants;
import org.noahsark.http.common.WebAsyncTimeout;
import org.noahsark.http.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/26
 */
@RestController
@RequestMapping("/async")
public class AsyncRestController {

    private static Logger logger = LoggerFactory.getLogger(AsyncRestController.class);

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @GetMapping("/hello")
    public WebAsyncTask sayHello(@RequestParam(defaultValue = "Demo inviter") String name) {
        logger.info("receive a request,thread: {},param :{}", Thread.currentThread().getId(), name);

        WebAsyncTask<Response> task = new WebAsyncTask<>(Constants.INTERFACE_TIMEOUT_MS, asyncTaskExecutor, () -> {

            logger.info("execute task,thread: {}", Thread.currentThread().getId());

            Response response = new Response.Builder().ok().build();

            return response;
        });

        WebAsyncTimeout.onTimeout(task);

        return task;
    }
}
