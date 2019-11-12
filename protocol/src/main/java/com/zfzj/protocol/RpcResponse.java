package com.zfzj.protocol;

import lombok.Data;

/**
 * @author jeremy.wang
 * @date 2019-11-04
 */
@Data
public class RpcResponse {

    private String requestId;

    private Throwable throwable;

    private Object result;
}
