package com.wjjnzn.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        StringHttpMessageConverter messageConverter = new StringHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        messageConverter.setSupportedMediaTypes(supportedMediaTypes);
        restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplate2(){
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        messageConverter.setSupportedMediaTypes(supportedMediaTypes);
        restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
        return restTemplate;
    }
}
