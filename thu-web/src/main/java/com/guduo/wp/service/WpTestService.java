package com.guduo.wp.service;

import com.guduo.wp.common.bean.WpTest;
import com.guduo.wp.common.repo.WpTestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by csonezp on 2017/2/13.
 */
@Service
public class WpTestService {
    @Autowired
    WpTestRepo repo;
    public List<WpTest> findAll(){
    	
    	System.out.print("test...degug");
    	
        return repo.findAll();
    }
}
