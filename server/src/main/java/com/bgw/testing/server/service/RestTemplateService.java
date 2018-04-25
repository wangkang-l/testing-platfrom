package com.bgw.testing.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;

@Service
public class RestTemplateService {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity exchange(String URL, HttpMethod httpMethod, Map<String, String> params, Map<String, String> headers, Object body, Class responseType) {
        return exchange(requestEntity(getURI(URL, params), httpMethod, headers, body), responseType != null ? responseType : String.class);
    }

    public ResponseEntity exchange(String URL, HttpMethod httpMethod, Map<String, String> params, Map<String, String> headers, Class responseType) {
        return exchange(requestEntity(getURI(URL, params), httpMethod, headers, null), responseType != null ? responseType : String.class);
    }

    public ResponseEntity exchange(String URL, HttpMethod httpMethod, Map<String, String> params, Map<String, String> headers, Object body) {
        return exchange(requestEntity(getURI(URL, params), httpMethod, headers, body), String.class);
    }

    public ResponseEntity exchange(String URL, HttpMethod httpMethod, Map<String, String> params, Map<String, String> headers) {
        return exchange(requestEntity(getURI(URL, params), httpMethod, headers, null), String.class);
    }

    private MultiValueMap getHeaders(Map<String, String> headers) {
        MultiValueMap multiValueMap = new LinkedMultiValueMap();

        multiValueMap.set(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        multiValueMap.set(HttpHeaders.ACCEPT, "application/json");

        if (headers != null && headers.size() > 0) {
            headers.keySet().forEach(key -> {
                multiValueMap.set(key, headers.get(key));
            });
        }

        return multiValueMap;
    }

    private ResponseEntity exchange(RequestEntity entity, Class responseType) {
        return restTemplate.exchange(entity, responseType);
    }

    private RequestEntity requestEntity(URI uri, HttpMethod httpMethod, Map<String, String> headers, Object body) {
        return new RequestEntity(body, getHeaders(headers), httpMethod, uri);
    }

    private URI getURI(String URL, Map params) {
        if (params != null && params.size() > 0) {
            StringBuffer finalUrl = new StringBuffer();
            finalUrl.append(URL).append("?");
            Iterator entries = params.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                finalUrl.append(key).append("=").append(value);
                if (entries.hasNext()) {
                    finalUrl.append("&");
                }
            }
            return URI.create(finalUrl.toString());
        }
        return URI.create(URL);
    }

}
