package org.noahsark.server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import io.netty.util.CharsetUtil;
import org.noahsark.server.rpc.Result;

/**
 * Json 工具类
 * @author zhangxt
 * @date 2021/11/06 12:17
 **/
public class JsonUtils {

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static <T> T fromJson(String json, Class<T> classz) {
        return GSON.fromJson(json, classz);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classz) {
        return GSON.fromJson(json, classz);
    }

    public static <T> Result<T> fromJsonObject(String json, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(Result.class, new Class[]{clazz});
        return GSON.fromJson(json, type);
    }

    public static Result<Void> fromCommonObject(byte[] object) {
        String json = new String((byte[]) object, CharsetUtil.UTF_8);

        return JsonUtils.fromJsonObject(json, Void.class);
    }

    /**
     *
     * @author zhangxt
     * @date 2021/11/06 12:16
     * @param json json字串
     * @param clazz 反序列化的类
     * @return org.noahsark.server.rpc.Result<java.util.List<T>>
    */
    public static <T> Result<List<T>> fromJsonArray(String json, Class<T> clazz) {
        // 生成List<T> 中的 List<T>
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        // 根据List<T>生成完整的Result<List<T>>
        Type type = new ParameterizedTypeImpl(Result.class, new Type[]{listType});
        return GSON.fromJson(json, type);
    }

    public static <T> String toJson(T obj) {
        return GSON.toJson(obj);
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class raw;
        private final Type[] args;

        public ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }

        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }

        @Override
        public Type getRawType() {
            return raw;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    /**
     * @author: noahsark
     * @version:
     * @date: 2020/12/7
     */
    public static void main(String[] args) {
/*
        MeetingLog meetingLog = new MeetingLog();
        meetingLog.setLastUpdateTime(10000L);
        meetingLog.setMeetingLogId(100);
        meetingLog.setRoomId(1000);
        List<MeetingLog> data = new ArrayList<>();
        data.add(meetingLog);

        meetingLog = new MeetingLog();
        meetingLog.setLastUpdateTime(10001L);
        meetingLog.setMeetingLogId(101);
        meetingLog.setRoomId(1001);
        data.add(meetingLog);

        Result<List<MeetingLog>> result = new Result<>();
        result.setData(data);

        String json = toJson(result);
        System.out.println("json = " + json);

        Result<List<MeetingLog>> result1 = fromJsonArray(json, MeetingLog.class);
        System.out.println("result1 = " + result1);*/

    }


}
