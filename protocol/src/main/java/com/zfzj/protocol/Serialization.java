package com.zfzj.protocol;

/**
 * @author jeremy.wang
 * @date 2019-11-04
 */
public interface Serialization {

    <T> byte[] serialize(T t);

    <T> T deSerialize(byte[] data, Class<T> clz);
}
