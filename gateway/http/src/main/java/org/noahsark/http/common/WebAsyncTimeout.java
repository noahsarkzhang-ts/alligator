package org.noahsark.http.common;

import org.noahsark.http.dto.Response;
import org.springframework.web.context.request.async.WebAsyncTask;

/**
 * @Description: 异步超时处理
 * @Author: yicai.liu
 * @Date: 14:31 2017/7/21
 */
public class WebAsyncTimeout {

    /**
     * @Description: 超时处理
     * @Author: yicai.liu
     * @Date 14:51 2017/7/21
     */
    public static void onTimeout(WebAsyncTask<Response> result) {
        result.onTimeout(() -> new Response.Builder()
                .code(Constants.ResponseCode.TIMEOUT)
                .message(Constants.ResponseMsg.TIMEOUT)
                .build());
    }
}
