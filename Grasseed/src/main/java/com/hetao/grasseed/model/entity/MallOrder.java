package com.hetao.grasseed.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

/**
 * 下单信息
 * @author hetao
 *
 */
@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MallOrder {
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.hetao.grasseed.common.util.IdGenerator")
    private Long orderId;
	
	private Integer price;//订单价格
	
	@Column(length = 50)
	private String userId;//用户信息
	
    @Column(length = 10)
    private String fromChannel;//下单渠道
    
    @Column(length = 2)
    private String status;//订单状态  0创建  1支付中 2支付完成 3退款中 4退款完成
    
    @Column(length = 300)
    private String returnUrl;//返回前端字符串
    
    private Date payTime;//支付时间
    
    @Column(length = 300)
    private String failReason;//失败原因
    
    @Column(length = 50)
    private String outPaymentId;//支付渠道单号
    
    @Column(length = 16)
    private String outStatus;//外部系统支付状态
    
    @Column(length = 300)
    private String outStatusDescription;//外部支付订单状态的描述和下一步操作的指引
    
    
	@Version
	private int version;
	
	@CreatedDate
	private Date createTime;

    @LastModifiedDate
    private Date lastmodifiedTime;
}
