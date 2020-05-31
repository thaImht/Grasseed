package com.hetao.grasseed.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信用户信息
 * @author hetao
 *
 */
@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "idx_openid",  columnList="openid")})
public class WxUserInfo {
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
	
	@Column(length = 50)
    private String openid;//用户的唯一标识
	
	@Column(length = 50)
    private String country;//国家，如中国为CN
	
	@Column(length = 50)
    private String province;//用户个人资料填写的省份
	
	@Column(length = 50)
    private String city;//普通用户个人资料填写的城市
	
    private Integer sex;//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	
	@Column(length = 100)
    private String nickname;//用户昵称
	
	@Column(length = 500)
    private String headimgurl;//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
	
	@Column(length = 20)
    private String language;//语言
	
	@Column(length = 500)
    private String privilege;//用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
	
	@Column(length = 500)
    private String unionid;//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	
	@Column(length = 50)
    private String appid;//公众号
	
    private Integer isSubscribe;//是否关注，0否 1是
    
    @Column(length = 50)
    private String subscribeScene;//返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_OTHERS 其他
    
    private Integer subscribeTime;//用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    
    @Column(length = 200)
    private String remark;//公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
    
    private Integer groupid;//用户所在的分组ID
    
    @Column(length = 50)
    private String tagidList;//用户被打上的标签ID列表
    
    private Integer qrScene;//二维码扫码场景
    
    @Column(length = 50)
    private String qrSceneStr;//二维码扫码场景描述
    
    @Version
	private int version;
	
	@CreatedDate
	private Date createTime;

    @LastModifiedDate
    private Date lastmodifiedTime;
}
