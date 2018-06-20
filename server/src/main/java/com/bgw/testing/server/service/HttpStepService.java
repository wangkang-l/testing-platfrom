package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.HttpRequestDto;
import com.bgw.testing.common.dto.StepResultDto;
import com.bgw.testing.server.util.BaseJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class HttpStepService {

    @Autowired
    private RestTemplateService restTemplate;

    public StepResultDto executeHttpRequest(HttpRequestDto requestDto) {
        StepResultDto stepResultDto = new StepResultDto();

        String url = requestDto.getUrl();
        Map<String, String> query = requestDto.getQuery();
        Map<String, String> headers = requestDto.getHeaders();
        String body = requestDto.getBody();

        try {
            ResponseEntity responseEntity = restTemplate.exchange(url, requestDto.getMethod(), query, headers, body);
            stepResultDto.setStatusCode(responseEntity.getStatusCodeValue());
            stepResultDto.setHeaders(responseEntity.getHeaders());
            stepResultDto.setData(BaseJsonUtils.writeValue(responseEntity.getBody()));
        } catch (Throwable e) {
            stepResultDto.setSuccess(false);
            stepResultDto.setException(e);
            return stepResultDto;
        }

        return stepResultDto;
    }

}
