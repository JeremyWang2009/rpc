package com.zfzj.netty.http.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author jeremy.wang
 * @date 2019-11-06
 */
@Data
public class User {

    private String userName;

    private String method;

    private Date date;

}
