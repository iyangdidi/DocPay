package com.guduo.wp.domain.request;

import lombok.Data;

/**
 * Created by jack on 2017/2/24.
 */

@Data
public class RequestRegister {
    String phone;
    String password;
    String code;
}
