package com.guduo.wp.domain.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by jack on 2017/2/27.
 */
@Data
public class RequestPerfectInfo {

    private String userName;

    private String token;

    @NotBlank
    private String nickName;

    @NotBlank
    private String email;

    @NotBlank
    private String company;

    @NotBlank
    private String job;
}
