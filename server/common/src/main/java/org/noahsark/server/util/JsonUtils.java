package org.noahsark.server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/12/7
 */
public class JsonUtils {

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static <T> T fromJson(String json, Class<T> classz) {
        return GSON.fromJson(json, classz);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classz) {
        return GSON.fromJson(json, classz);
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
