package com.gamesi.worker.giftcard.service;

import com.gamesi.worker.giftcard.dao.ProductDao;
import com.gamesi.worker.giftcard.entity.ProductEntity;
import com.gamesi.worker.giftcard.model.product.Content;
import com.gamesi.worker.giftcard.model.product.ProductRes;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final String CACHE_NAME = "PRODUCT";
    private final RestTemplate restTemplate;
    private final ProductDao productDao;

    private String cookie = "Bearer eyJraWQiOiJjNGE1ZWU1Zi0xYmE2LTQ1N2UtOTI3Yi1lYzdiODliNzcxZTIiLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMTE5MyIsImlzcyI6Imh0dHBzOi8vcmVsb2FkbHktc2FuZGJveC5hdXRoMC5jb20vIiwiaHR0cHM6Ly9yZWxvYWRseS5jb20vc2FuZGJveCI6dHJ1ZSwiaHR0cHM6Ly9yZWxvYWRseS5jb20vcHJlcGFpZFVzZXJJZCI6IjIxMTkzIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIiwiYXVkIjoiaHR0cHM6Ly9naWZ0Y2FyZHMtc2FuZGJveC5yZWxvYWRseS5jb20iLCJuYmYiOjE2OTg3NTA3NTksImF6cCI6IjIxMTkzIiwic2NvcGUiOiJkZXZlbG9wZXIiLCJleHAiOjE2OTg4MzcxNTksImh0dHBzOi8vcmVsb2FkbHkuY29tL2p0aSI6IjRlNDU0MGI4LWY0NjktNGI1ZS04YTI3LTQ0NDA1YWQzY2QzZSIsImlhdCI6MTY5ODc1MDc1OSwianRpIjoiZmU5NzVjOTMtYzliOC00NWNmLTllM2EtODUxNDIwOTAxMzkwIn0.IP64Dkyovp4XZAUZp7DlOfT0H9ZxfEz3TKvPLTczKAA";


    private final RedisTemplate<String,Object> redisTemplate;
    private HashOperations<String,String, ProductRes> hashOperations;

    @PostConstruct
    private void initHashOper(){
        hashOperations = redisTemplate.opsForHash();
    }

    public ProductRes allProducts(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept","application/com.reloadly.giftcards-v1+json");
        headers.set("Authorization",cookie);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ProductRes productRes = restTemplate
                .exchange("https://giftcards-sandbox.reloadly.com/products", HttpMethod.GET, entity, ProductRes.class).getBody();
        int totalElements = productRes.getTotalElements();
        int totalPage = productRes.getTotalPages();

        for (int i =2; i<=totalPage ; i++){
            ProductRes productRes2 = restTemplate
                    .exchange("https://giftcards-sandbox.reloadly.com/products?size=200&page=" + i, HttpMethod.GET, entity, ProductRes.class).getBody();
        productRes.getContent().addAll(productRes2.getContent());
        }

        hashOperations.put(CACHE_NAME , "main",productRes);
        return productRes;
    }



    public ProductRes readProducts(){
        return (ProductRes) hashOperations.entries(CACHE_NAME).get("main");
    }

    //@Scheduled(cron ="${Scheduled.time}")
    //@Scheduled(cron ="@hourly")
/*    @Async
    @Scheduled(fixedRate = 21600000)
    public void schaduleProducts(){
        hashOperations.delete(CACHE_NAME , "main");
        System.out.println("----------------------------");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept","application/com.reloadly.giftcards-v1+json");
        headers.set("Authorization",cookie);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ProductRes productRes = restTemplate
                .exchange("https://giftcards-sandbox.reloadly.com/products", HttpMethod.GET, entity, ProductRes.class).getBody();
        int totalElements = productRes.getTotalElements();
        int totalPage = productRes.getTotalPages();

        for (int i =2; i<=totalPage ; i++){
            ProductRes productRes2 = restTemplate
                    .exchange("https://giftcards-sandbox.reloadly.com/products?size=200&page=" + i, HttpMethod.GET, entity, ProductRes.class).getBody();
            productRes.getContent().addAll(productRes2.getContent());
            System.out.println(i);
        }
        hashOperations.put(CACHE_NAME , "main",productRes);

    }*/

    @Async
    @Scheduled(fixedRate = 21600000)
    @Transactional
    public void schaduleProducts(){
        hashOperations.delete(CACHE_NAME , "main");
        System.out.println("----------------------------");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept","application/com.reloadly.giftcards-v1+json");
        headers.set("Authorization",cookie);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ProductRes productRes = restTemplate
                .exchange("https://giftcards-sandbox.reloadly.com/products", HttpMethod.GET, entity, ProductRes.class).getBody();
        int totalElements = productRes.getTotalElements();
        int totalPage = productRes.getTotalPages();

        for (int i =2; i<=totalPage ; i++){
            ProductRes productRes2 = restTemplate
                    .exchange("https://giftcards-sandbox.reloadly.com/products?size=200&page=" + i, HttpMethod.GET, entity, ProductRes.class).getBody();
            productRes.getContent().addAll(productRes2.getContent());
            System.out.println(i);
        }
        //hashOperations.put(CACHE_NAME , "main",productRes);
        productRes.getContent().forEach(p->{
           if(p.getFixedSenderDenominations()!=null){
               p.getFixedRecipientDenominations().forEach(o->{
                   ProductEntity productEntity = new ProductEntity();
                   productEntity.setProductName(p.getProductName());
                   productEntity.setProductId(p.getProductId());
                   productEntity.setGlobal(p.getGlobal());
                   productEntity.setSenderFee(p.getSenderFee());
                   productEntity.setUnitPrice(o);
                   productEntity.setCurrency(p.getSenderCurrencyCode());
                   productEntity.setSupportsPreOrder(p.getSupportsPreOrder());
                   productEntity.setCountryName(p.getCountry().getName());
                   productEntity.setCountryCode(p.getCountry().getIsoName());
                   if(p.getRedeemInstruction()!=null){
                       if(p.getRedeemInstruction().getVerbose()!=null)
                       productEntity.setDescription(p.getRedeemInstruction().getVerbose());
                       if(p.getRedeemInstruction().getConcise()!=null)
                       productEntity.setMainSite(p.getRedeemInstruction().getConcise());
                   }
                   save(productEntity);
                   System.out.println("-----added------");
               });
           }else {
               ProductEntity productEntity = new ProductEntity();
               productEntity.setProductName(p.getProductName());
               productEntity.setProductId(p.getProductId());
               productEntity.setGlobal(p.getGlobal());
               productEntity.setSenderFee(p.getSenderFee());
               productEntity.setUnitPrice(p.getMinSenderDenomination());
               productEntity.setCurrency(p.getSenderCurrencyCode());
               productEntity.setSupportsPreOrder(p.getSupportsPreOrder());
               productEntity.setCountryName(p.getCountry().getName());
               productEntity.setCountryCode(p.getCountry().getIsoName());
               if(p.getRedeemInstruction()!=null){
                   if(p.getRedeemInstruction().getVerbose()!=null)
                       productEntity.setDescription(p.getRedeemInstruction().getVerbose());
                   if(p.getRedeemInstruction().getConcise()!=null)
                       productEntity.setMainSite(p.getRedeemInstruction().getConcise());
               }
               save(productEntity);
               System.out.println("-----added------");
           }


        });
    }

    public List<Content> findByParam(String param){
       return hashOperations.entries(CACHE_NAME).get("main").getContent()
               .stream().filter(p->p.getProductName().toLowerCase().contains(param.toLowerCase()))
               .skip(9).limit(5).collect(Collectors.toList());
    }
    @Transactional
    public void save(ProductEntity productEntity){
        ProductEntity productEntity1 = new ProductEntity();
        productEntity1.setProductId(productEntity.getProductId());
        productEntity1.setProductName(productEntity.getProductName());
        productEntity1.setGlobal(productEntity.getGlobal());
        productEntity1.setCurrency(productEntity.getCurrency());
        productEntity1.setCountryName(productEntity.getCountryName());
        productEntity1.setCountryCode(productEntity.getCountryCode());
        productEntity1.setPic1(productEntity.getPic1());
        productEntity1.setSenderFee(productEntity.getSenderFee());
        productEntity1.setUnitPrice(productEntity.getUnitPrice());
        productEntity1.setDescription(productEntity.getDescription());
        productEntity1.setMainSite(productEntity.getMainSite());
        productEntity1.setSupportsPreOrder(productEntity.getSupportsPreOrder());
        productDao.save(productEntity1);
    }

}
