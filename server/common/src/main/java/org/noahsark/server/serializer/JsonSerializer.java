package org.noahsark.server.serializer;

import org.noahsark.server.util.JsonUtils;

import java.nio.charset.Charset;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/2
 */
public class JsonSerializer implements Serializer {

    private Charset charset = Charset.forName("UTF-8");

    @Override
    public byte[] encode(Object obj) {

        String json = JsonUtils.toJson(obj);

        return json.getBytes(charset);
    }

    @Override
    public <T> T decode(byte[] bytes, Class<T> classz) {

        String json = new String(bytes,charset);

        return JsonUtils.fromJson(json,classz);
    }

}
