package com.gamesi.worker.giftcard.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="product")
@Table(name="product")
@Getter
@Setter
@EqualsAndHashCode
@Cacheable(value = false)
@NoArgsConstructor
public class ProductEntity extends BaseEntity{

    @Column(name="product_id")
    private Integer productId;
    @Column(name="product_name" , length = 100)
    private String productName;

    //global or with region
    @Column(name="global")
    private Boolean global;

    //پیش سفارش داره یا موجود هست
    @Column(name="supports_pre_order")
    private Boolean supportsPreOrder;

    //جمع این دو تا قیمت میشه
    @Column(name="sender_Fee")
    private Double senderFee;
    //unit price
    @Column(name="unit_price")
    private Double unitPrice;

    @Column(name="currency" , length = 10)
    private String currency;
    @Column(name="pic1" , length = 100)
    private String pic1;
    @Column(name="country_Code" , length = 5)
    private String CountryCode;
    @Column(name="country_Name" , length = 50)
    private String CountryName;
    @Column(name="description" , length = 5000)
    private String description;
    @Column(name="main_site" , length = 2000)
    private String mainSite;
}
