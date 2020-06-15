package com.hetao.grasseed.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hetao.grasseed.common.https.HttpsUtils;
import com.hetao.grasseed.common.util.WxInfoUtil;
import com.hetao.grasseed.common.util.WxUtil;
import com.hetao.grasseed.dao.WxUserInfoRepository;
import com.hetao.grasseed.model.entity.WxUserInfo;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("")
@Slf4j
public class WxController {

	@Autowired
	private WxUtil wxUtil;
	
	@Autowired
	private WxUserInfoRepository userInfoRepository;
	
	@GetMapping("/wx/o/notify")
	@ResponseBody
	public String notifyGet(@RequestParam(name = "signature", required = false) String signature,
            @RequestParam(name = "timestamp", required = false) String timestamp,
            @RequestParam(name = "nonce", required = false) String nonce,
            @RequestParam(name = "echostr", required = false) String echostr) {
		log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,timestamp, nonce, echostr);
	    return echostr;
	}

	@PostMapping("/wx/o/notify")
	@ResponseBody
	public String notifyPost(@RequestBody String notifyXml) {
		log.info("notifyXml==" + notifyXml);
		Map<String, String> recieve = WxInfoUtil.xmlToMap(notifyXml);
		if("SCAN".equals(recieve.get("Event"))) {//扫描带参数二维码事件
			//return handleScan(recieve);
		}
		if("subscribe".equals(recieve.get("Event"))) {//关注公众号
			return handleSubscribe(recieve);
		}
		if("unsubscribe".equals(recieve.get("Event"))) {//取消关注公众号
			return handleUnsubcribe(recieve);
		}
		return "success";
	}
	
	private String handleSubscribe(Map<String, String> recieve) {
		saveUserInfo(recieve.get("FromUserName"));
		
		WxUserInfo userInfo = userInfoRepository.findByOpenid(recieve.get("FromUserName"));
		if(userInfo==null) {
			userInfo = new WxUserInfo();
		}
		userInfo.setOpenid(recieve.get("FromUserName"));
		userInfo.setIsSubscribe(1);
		userInfo.setAppid(recieve.get("ToUserName"));
		userInfoRepository.save(userInfo);
		if(true) {
			Map<String, String> result = new HashMap<>();
			result.put("ToUserName", recieve.get("FromUserName"));
			result.put("FromUserName", recieve.get("ToUserName"));
			result.put("CreateTime", String.valueOf(System.currentTimeMillis()));
			result.put("MsgType", "text");
			result.put("Content", "欢迎来到智禾公众号");
		    return WxInfoUtil.mapToXml(result);
		}
		return "success";
	}
	
	private String handleUnsubcribe(Map<String, String> recieve) {
		WxUserInfo userInfo = userInfoRepository.findByOpenid(recieve.get("FromUserName"));
		if(userInfo==null) {
			userInfo = new WxUserInfo();
		}
		userInfo.setOpenid(recieve.get("FromUserName"));
		userInfo.setIsSubscribe(0);
		userInfoRepository.save(userInfo);
		return "success";
	}
	
	private void saveUserInfo(String openId) {
		//获取access_token
		String accessToken = wxUtil.getAccessToken();
		String result = HttpsUtils.get("https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN");
		JSONObject obj = JSON.parseObject(result);
		saveUserInfo(obj);
	}
	
	private void saveUserInfo(JSONObject obj) {
		String openid = obj.getString("openid");
		if(openid==null) {
			return;
		}
		WxUserInfo userInfo = userInfoRepository.findByOpenid(openid);
		if(userInfo==null) {
			userInfo = new WxUserInfo();
		}
		userInfo.setCity(obj.getString("city"));
		userInfo.setCountry(obj.getString("country"));
		userInfo.setHeadimgurl(obj.getString("headimgurl"));
		userInfo.setLanguage(obj.getString("language"));
		userInfo.setNickname(obj.getString("nickname"));
		userInfo.setOpenid(openid);
		userInfo.setPrivilege(obj.getString("privilege"));
		userInfo.setProvince(obj.getString("province"));
		userInfo.setSex(obj.getInteger("sex"));
		userInfo.setUnionid(obj.getString("unionid"));
		userInfo.setAppid("grasseed");
		if(obj.getInteger("subscribe")!=null) {
			userInfo.setIsSubscribe(obj.getInteger("subscribe"));
		}
		if(obj.getInteger("subscribe_time")!=null) {
			userInfo.setSubscribeTime(obj.getInteger("subscribe_time"));
		}
		if(obj.getString("remark")!=null) {
			userInfo.setRemark(obj.getString("remark"));
		}
		if(obj.getInteger("groupid")!=null) {
			userInfo.setGroupid(obj.getInteger("groupid"));
		}
		if(obj.getJSONArray("tagid_list")!=null) {
			userInfo.setTagidList(obj.getJSONArray("tagid_list").toJSONString());
		}
		if(obj.getString("subscribe_scene")!=null) {
			userInfo.setSubscribeScene(obj.getString("subscribe_scene"));
		}
		if(obj.getInteger("qr_scene")!=null) {
			userInfo.setQrScene(obj.getInteger("qr_scene"));
		}
		if(obj.getString("qr_scene_str")!=null) {
			userInfo.setQrSceneStr(obj.getString("qr_scene_str"));
		}
		userInfoRepository.save(userInfo);
	}
	
	/**
	 * 返回MP_verify_e7pVkVyrfI0GcSDA.txt文件
	 * @param req
	 * @return
	 */
	@RequestMapping("/MP_verify_e7pVkVyrfI0GcSDA.txt")
	public void file(HttpServletResponse response){
		Resource resource = new ClassPathResource("MP_verify_e7pVkVyrfI0GcSDA.txt");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			InputStream  fis = resource.getInputStream();
			byte[] bb = new byte[4096];
			int len;
			while((len=fis.read(bb)) != -1){
				baos.write(bb, 0, len);
			}
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=MP_verify_e7pVkVyrfI0GcSDA.txt");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        try {
            OutputStream os = response.getOutputStream();
            os.write(baos.toByteArray());
            baos.close();
            os.flush();
            os.close();
        } catch (Exception e) {
            log.warn("微信校验文件导出:{}",e);
        }
	}
	
	@RequestMapping("/wx/menu")
	@ResponseBody
	public String menu() {
		log.info("menu");
		//获取access_token
		String accessToken = wxUtil.getAccessToken();
		
		JSONObject button = new JSONObject();
		JSONArray buttons  = new JSONArray();
		
		JSONObject button1 = new JSONObject();
		JSONArray subButton1s  = new JSONArray();
		JSONObject button11 = new JSONObject();
		button11.put("type", "view");
		button11.put("name", "精品课程");
		button11.put("url", "http://www.grasseed.com/productList");
		subButton1s.add(button11);
		button1.put("name", "课程介绍");
		button1.put("sub_button", subButton1s);
		
		JSONObject button2 = new JSONObject();
		button2.put("type", "click");
		button2.put("name", "待开发");
		button2.put("key", "20");
		
		JSONObject button3 = new JSONObject();
		button3.put("type", "click");
		button3.put("name", "待开发");
		button3.put("key", "30");
		
		buttons.add(0, button1);
		buttons.add(1, button2);
		buttons.add(2, button3);
		button.put("button", buttons);
		String result = HttpsUtils.postJSON("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken, button.toJSONString());
		return result;
	}
	
}
