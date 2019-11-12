package com.zfzj.netty.http.protocol;

import com.alibaba.fastjson.JSON;

/**
 * @author jeremy.wang
 * @date 2019-11-06
 */
public class JSONSerializer implements Serializer {

    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    public <T> T deserialize(byte[] bytes, Class<T> clz) {
        return JSON.parseObject(bytes, clz);
    }
}
