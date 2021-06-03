/*
 * 文件名：CfpaExceptionHandlerAdvice.java
 * 版权：Copyright by www.fsmeeting.com
 * 描述： cfpa异常处理类
 * 修改人：zhangxt
 * 修改时间：2019年1月22日
 * 修改内容：
 */

package org.noahsark.http.advice;


import org.noahsark.http.common.Constants;
import org.noahsark.http.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * cfpa异常处理
 * 
 * @author zhangxt
 * @version 2019年1月22日
 * @see ExceptionHandlerAdvice
 * @since
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    /**
     * Description: <br>
     * DuplicateKeyException 异常处理<br>
     *
     * @param ex DuplicateKeyException
     * @return 结果
     * @see
     */
    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response exceptionResponse(Exception ex) {
        LOG.error("catch an exception:", ex);

        Response.Builder builder = new Response.Builder();

        return builder.code(Constants.ResponseCode.EXCEPTION).message(Constants.ResponseMsg.EXCEPTION).build();
    }
}
