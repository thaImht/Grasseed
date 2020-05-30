package com.hetao.grasseed.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

/**
 * 商品信息
 * @author hetao
 *
 */
@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Product {
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
	
	@Column(length = 20)
    private String productCode;//商品码
	
	@Column(length = 50)
    private String productName;//商品名称
	
	@Column(length = 500)
    private String productImageSmallUrl;//商品小图
	
	@Column(length = 500)
    private String productImageBigUrl;//商品大图
	
	private Integer price;//商品价格
	
	private Boolean isOnline;//是否上线
	
	private Long stockNumber;//库存
	
	private Boolean islimitStock;//是否限制库存
	
	@Column(length = 10)
    private String category;//商品分类
	
	@Column(length = 20)
    private String categoryName;//商品分类名称
	
	@Lob
	@Column(columnDefinition="TEXT")
    private String description;//商品介绍
	
	@Version
	private int version;
	
	@CreatedDate
	private Date createTime;

    @LastModifiedDate
    private Date lastmodifiedTime;
}
