package com.ning.geekbang.github_oauth2.controller;

import com.alibaba.fastjson.JSONObject;
import com.ning.geekbang.github_oauth2.properties.GithubOauth2Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class LoginController {

    private final GithubOauth2Properties githubOauth2Properties;

    @Autowired
    public LoginController(GithubOauth2Properties githubOauth2Properties) {
        this.githubOauth2Properties = githubOauth2Properties;
    }


    @GetMapping("/authorize")
    public String authorize() {
        StringBuilder url = new StringBuilder();
        url.append(githubOauth2Properties.getAuthorizeUrl())
                .append("?client_id=")
                .append(githubOauth2Properties.getClientId())
                .append("&redirect_uri=")
                .append(githubOauth2Properties.getRedirectUrl());
        log.info("授权url:{}", url);
        return "redirect:" + url;
    }

    @GetMapping("/oauth2/callback")
    public String callback(@RequestParam("code") String code, ModelAndView modelAndView) {
        log.info("callback code={}", code);
        // code换token
        String accessToken = getAccessToken(code);
        // token换userInfo
        String userInfo = getUserInfo(accessToken);
        log.info("callback userInfo={}", userInfo);
        return "redirect:/home";
    }

    @GetMapping("/home")
    @ResponseBody
    public String home() {
        return "<h1>Hello World</h1>";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "index";
    }

    private String getAccessToken(String code) {
        StringBuilder url = new StringBuilder();
        url.append(githubOauth2Properties.getAccessTokenUrl())
                .append("?client_id=")
                .append(githubOauth2Properties.getClientId())
                .append("&client_secret=")
                .append(githubOauth2Properties.getClientSecret())
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");
        log.info("getAccessToken request:{}", url);
        // 构建请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        // 指定响应返回json格式
        requestHeaders.add("accept", "application/json");
        // 构建请求实体
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        // post 请求方式
        ResponseEntity<String> response = restTemplate.postForEntity(url.toString(), requestEntity, String.class);
        String responseStr = response.getBody();
        log.info("getAccessToken response={}", responseStr);
        // 解析响应json字符串
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        String accessToken = jsonObject.getString("access_token");
        log.info("getAccessToken accessToken={}", accessToken);
        return accessToken;
    }

    private String getUserInfo(String accessToken) {
        String url = githubOauth2Properties.getUserInfoUrl();
        log.info("getUserInfo request:{}", url);
        // 构建请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        // 指定响应返回json格式
        requestHeaders.add("accept", "application/json");
        // AccessToken放在请求头中
        requestHeaders.add("Authorization", "token " + accessToken);
        // 构建请求实体
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        // get请求方式
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        return response.getBody();
    }

}
