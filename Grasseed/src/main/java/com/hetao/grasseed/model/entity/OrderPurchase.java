package com.hetao.grasseed.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

/**
 * 购买信息
 * @author hetao
 *
 */
@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class OrderPurchase {
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
	
    private Long orderId;

    @Column(length = 20)
    private String productCode;//商品码
    
    private Integer purchaseNumber;//购买数量

    private Integer unitPrice;//购买单价

    private Integer totalPrice;//购买总价
    
    @Version
	private int version;
	
	@CreatedDate
	private Date createTime;

    @LastModifiedDate
    private Date lastmodifiedTime;
}
