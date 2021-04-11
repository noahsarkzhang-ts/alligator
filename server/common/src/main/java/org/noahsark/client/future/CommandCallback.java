package org.noahsark.client.future;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/27
 */
public interface CommandCallback {

    public static final CommandCallback DEFAUTL_CALLBACK = new CommandCallback() {
        @Override
        public void callback(Object result) {

        }

        @Override
        public void failure(Throwable cause) {

        }
    };

    void callback(Object result);

    void failure(Throwable cause);
}
