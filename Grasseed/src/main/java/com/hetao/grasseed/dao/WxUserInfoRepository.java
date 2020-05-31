package com.hetao.grasseed.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hetao.grasseed.model.entity.WxUserInfo;

public interface WxUserInfoRepository extends JpaRepository<WxUserInfo, Long>{

	WxUserInfo findByOpenid(String openid);

}
