package com.hetao.grasseed.common.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScheduleCache {
    private static Map<String, String> messageMap = new HashMap<String, String>();  
    
    /**
     * 设置定时缓存
     */
    public void put(String key, String value,int millisecond ){
        messageMap.put(key, value);
        Timer timer = new Timer();
        //60秒后执行任务TimerDeleteTask
        timer.schedule(new TimerDeleteTask(key, timer), millisecond );
    }
    
    /**
     * 获取缓存
     */
    public String get(String key) {
    	return messageMap.get(key);
    }
    		
    /**
     * 定义一个任务，millisecond后删除map中对应的key-value值
     */
    class TimerDeleteTask extends TimerTask {
        private String key;
        private Timer timer;
        
        public TimerDeleteTask(String key, Timer timer){
            this.key = key;
            this.timer = timer;
        }

        @Override
        public void run(){
            messageMap.remove(key);
            timer.cancel();
            if (null != timer) {
                timer = null;
            }
        }
    }
}
