package com.guduo.wp.common.repo;

import com.guduo.wp.common.bean.WpTest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by csonezp on 2017/2/13.
 */
public interface WpTestRepo extends JpaRepository<WpTest,Integer> {
}
