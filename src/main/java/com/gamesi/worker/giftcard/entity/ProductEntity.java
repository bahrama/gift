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
    @Column(name="supports_PreOrder")
    private Boolean supportsPreOrder;

    //جمع این دو تا قیمت میشه
    @Column(name="sender_Fee")
    private Double senderFee;
    //unit price
    @Column(name="min_Sender_Denomination")
    private Double minSenderDenomination;

    @Column(name="currency" , length = 10)
    private String currency;
    @Column(name="pic1" , length = 100)
    private String pic1;
    @Column(name="Country_Code" , length = 5)
    private String CountryCode;
    @Column(name="Country_Name" , length = 50)
    private String CountryName;
}
