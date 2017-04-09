package com.guduo.wp.controller;

import com.google.gson.Gson;
import com.guduo.wp.domain.request.RequestLogin;
import com.guduo.wp.domain.request.RequestPerfectInfo;
import com.guduo.wp.domain.request.RequestRegister;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/user", produces = "application/json; charset=utf-8")
public class UserController {

    @Value("${bi.user.api.url.host}")
    private String userApiHost;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(@ModelAttribute RequestRegister register) {
        String result;
        try {
            result = Request.Post(
                    UriComponentsBuilder.fromUriString(userApiHost).path("/api/user/register")
                            .build().toUriString()
            ).bodyString(new Gson().toJson(register), ContentType.APPLICATION_JSON).execute().returnContent().asString();
        } catch (Exception e) {
            result = "{\"code\" = 111, \"message\" = " + e.toString() + "}";
        }
        return result;
    }

    @RequestMapping(value = "/perfectInfo", method = RequestMethod.POST)
    @ResponseBody
    public String perfectInfo(@ModelAttribute RequestPerfectInfo perfectInfo) {
        String result;
        try {
            result = Request.Post(
                    UriComponentsBuilder.fromUriString(userApiHost).path("/api/user/perfectInfo")
                            .build().toUriString()
            ).bodyString(new Gson().toJson(perfectInfo), ContentType.APPLICATION_JSON).execute().returnContent().asString();
        } catch (Exception e) {
            result = "{\"code\" = 111, \"message\" = " + e.toString() + "}";
        }
        return result;
    }

    @RequestMapping(value = "/logIn", method = RequestMethod.POST)
    @ResponseBody
    public String logIn(@ModelAttribute RequestLogin logIn) {
        String result = new Gson().toJson(logIn);
        try {
            result = Request.Post(
                    UriComponentsBuilder.fromUriString(userApiHost).path("/api/user/logIn")
                            .build().toUriString()
            ).bodyString(result, ContentType.APPLICATION_JSON).execute().returnContent().asString();
        } catch (Exception e) {
            result = "{\"code\" = 111, \"message\" = " + e.toString() + "}";
        }
        return result;
    }

    @RequestMapping(value = "/checkToken", method = RequestMethod.POST)
    @ResponseBody
    public String checkToken(@RequestParam("userName") String userName,
                             @RequestParam("token") String token) {
        List<NameValuePair> request = new ArrayList<>();
        request.add(new BasicNameValuePair("userName", userName));
        request.add(new BasicNameValuePair("token", token));
        String result;
        try {
            result = Request.Post(
                    UriComponentsBuilder.fromUriString(userApiHost).path("/api/user/checkToken")
                            .build().toUriString()
            ).bodyForm(request).execute().returnContent().asString();
        } catch (Exception e) {
            result = "{\"code\" = 111, \"message\" = " + e.toString() + "}";
        }
        return result;
    }

    @RequestMapping(value = "/get")
    @ResponseBody
    public String get(@RequestParam("token") String token) {
        List<NameValuePair> request = new ArrayList<>();
        request.add(new BasicNameValuePair("token", token));
        String result;
        try {
            result = Request.Post(
                    UriComponentsBuilder.fromUriString(userApiHost).path("/api/user/get")
                            .build().toUriString()
            ).bodyForm(request).execute().returnContent().asString();
        } catch (Exception e) {
            result = "{\"code\" = 111, \"message\" = " + e.toString() + "}";
        }
        return result;
    }

    /**
     * 修改密码
     *
     * @param phone
     * @param code
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public String resetPassword(@RequestParam("phone") String userName, @RequestParam("code") String code, @RequestParam("newPassword") String newPassword) {
        List<NameValuePair> request = new ArrayList<>();
        request.add(new BasicNameValuePair("userName", userName));
        request.add(new BasicNameValuePair("code", code));
        request.add(new BasicNameValuePair("newPassword", newPassword));
        String result;
        try {
            result = Request.Post(
                    UriComponentsBuilder.fromUriString(userApiHost).path("/api/user/resetPassword")
                            .build().toUriString()
            ).bodyForm(request).execute().returnContent().asString();
        } catch (Exception e) {
            result = "{\"code\" = 111, \"message\" = " + e.toString() + "}";
        }
        return result;
    }

}
