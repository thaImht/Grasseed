package com.hetao.grasseed.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hetao.grasseed.common.cache.ScheduleCache;
import com.hetao.grasseed.common.https.HttpsUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WxUtil {
	@Value("${wx.appid}")
   	private String appId;
	
	@Value("${wx.appsecret}")
   	private String appSecret;
	
	@Autowired
    private ScheduleCache scheduleCache;
	
	public String getAccessToken() {
		String accessToken = scheduleCache.get("WX_ACCSEE_TOEKN");
		if(accessToken==null) {
			log.info("====url:"+"https://api.weixin.qq.com/cgi-bin/token?appid="+appId+"&secret="+appSecret+"&grant_type=client_credential");
			String result = HttpsUtils.get("https://api.weixin.qq.com/cgi-bin/token?appid="+appId+"&secret="+appSecret+"&grant_type=client_credential");
			JSONObject obj = JSON.parseObject(result);
			accessToken= obj.getString("access_token");
			scheduleCache.put("WX_ACCSEE_TOEKN", accessToken, 7000*1000);
		}
		return accessToken;
	}
}
