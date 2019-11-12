package com.zfzj.netty.http.protocol;

/**
 * @author jeremy.wang
 * @date 2019-11-06
 */
public interface Serializer {

    byte[] serialize(Object object);

    <T> T deserialize(byte[] bytes, Class<T> clz);
}
