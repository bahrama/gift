package com.gamesi.worker.giftcard.service;

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
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final String CACHE_NAME = "PRODUCT";
    private final RestTemplate restTemplate;

    private String cookie = "Bearer eyJraWQiOiJjNGE1ZWU1Zi0xYmE2LTQ1N2UtOTI3Yi1lYzdiODliNzcxZTIiLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMTE5MyIsImlzcyI6Imh0dHBzOi8vcmVsb2FkbHktc2FuZGJveC5hdXRoMC5jb20vIiwiaHR0cHM6Ly9yZWxvYWRseS5jb20vc2FuZGJveCI6dHJ1ZSwiaHR0cHM6Ly9yZWxvYWRseS5jb20vcHJlcGFpZFVzZXJJZCI6IjIxMTkzIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIiwiYXVkIjoiaHR0cHM6Ly9naWZ0Y2FyZHMtc2FuZGJveC5yZWxvYWRseS5jb20iLCJuYmYiOjE2OTg0MTg2MzMsImF6cCI6IjIxMTkzIiwic2NvcGUiOiJkZXZlbG9wZXIiLCJleHAiOjE2OTg1MDUwMzMsImh0dHBzOi8vcmVsb2FkbHkuY29tL2p0aSI6ImZkYjZjNzM2LWRmODUtNGY3ZS1iNTllLTUzNjVjYjU0NmM1OSIsImlhdCI6MTY5ODQxODYzMywianRpIjoiZGFiZmQxNTctYTk3Yy00ZWVmLWJlZTktN2UxMDM3YzE5ZTRjIn0.0KvaCeLSbvPaRMVY-7N-JMlcNeFPWc7K-DkEXO0avso";


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
    @Async
    @Scheduled(fixedRate = 5000)
    public void schaduleProducts(){
        System.out.println("saaaaaaaaaaaalam");
    }

}
