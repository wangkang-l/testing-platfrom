package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.ApiRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class HttpService {

    @Autowired
    private RestTemplateService restTemplate;

    public void executeHttpRequest(ApiRequestDto requestDto) {

        String url = requestDto.getUrl();
        Map<String, String> query = requestDto.getQuery();
        Map<String, String> headers = requestDto.getHeaders();
        String body = requestDto.getBody();

        restTemplate.exchange(url, requestDto.getMethod(), query, headers, body);
    }

}
