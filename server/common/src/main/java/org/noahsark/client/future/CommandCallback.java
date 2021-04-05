package org.noahsark.client.future;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/27
 */
public interface CommandCallback {

    void callback(Object result);

    void failure(Throwable cause);
}
