package com.hetao.grasseed.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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
    
	@Version
	private int version;
	
	@CreatedDate
	private Date createTime;

    @LastModifiedDate
    private Date lastmodifiedTime;
}
