package org.noahsark.server.dispatcher;

import java.util.HashMap;
import java.util.Map;
import org.noahsark.server.processor.AbstractProcessor;


/**
 * @author: noahsark
 * @version:
 * @date: 2020/12/3
 */
public class Dispatcher {

    private Map<String, AbstractProcessor> processors = new HashMap<>();

    private static class DispatcherHolder {
        private static final Dispatcher instance = new Dispatcher();
    }

    private Dispatcher() {}

    public static Dispatcher getInstance() {
        return DispatcherHolder.instance;
    }

    public AbstractProcessor getProcessor(String name) {
        return processors.get(name);
    }

    public void register(String name, AbstractProcessor processor) {
        processors.put(name, processor);
    }







}
