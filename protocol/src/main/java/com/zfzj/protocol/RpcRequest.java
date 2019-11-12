package com.zfzj.protocol;

import lombok.Data;

/**
 * @author jeremy.wang
 * @date 2019-11-04
 */
@Data
public class RpcRequest {

    private String requestId;

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;
}
