package com.guduo.wp.controller;

import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 2017/3/16.
 */
@Controller
@RequestMapping(value = "/vcode", produces = "application/json; charset=utf-8")
public class VcodeController {

    @Value("${bi.user.api.url.host}")
    private String userApiHost;

    @RequestMapping("/send")
    @ResponseBody
    public String send(@RequestParam(value = "phone") String phoneNumber){
        List<NameValuePair> request = new ArrayList<>();
        request.add(new BasicNameValuePair("phoneNumber", phoneNumber));
        String result;
        try {
            result = Request.Post(
                    UriComponentsBuilder.fromUriString(userApiHost).path("/api/vcode/send")
                            .build().toUriString()
            ).bodyForm(request).execute().returnContent().asString();
        } catch (Exception e) {
            result = "{\"code\" = 111, \"message\" = " + e.toString() + "}";
        }
        return result;
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verify(@RequestParam("phone")String phoneNumber, @RequestParam("code")String code){
        List<NameValuePair> request = new ArrayList<>();
        request.add(new BasicNameValuePair("phoneNumber", phoneNumber));
        request.add(new BasicNameValuePair("code", code));
        String result;
        try {
            result = Request.Post(
                    UriComponentsBuilder.fromUriString(userApiHost).path("/api/vcode/verify")
                            .build().toUriString()
            ).bodyForm(request).execute().returnContent().asString();
        } catch (Exception e) {
            result = "{\"code\" = 111, \"message\" = " + e.toString() + "}";
        }
        return result;
    }
}
