package com.guduo.wp.controller;

import com.guduo.wp.common.bean.WpTest;
import com.guduo.wp.service.WpTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by csonezp on 2017/2/13.
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    WpTestService wpTestService;
    @RequestMapping("/list")
    public List<WpTest> getAllData(){
        return wpTestService.findAll();
    }
}
