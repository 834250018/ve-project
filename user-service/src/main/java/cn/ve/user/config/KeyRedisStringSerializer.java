package cn.ve.user.config;

import com.alibaba.nacos.client.naming.utils.StringUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KeyRedisStringSerializer implements RedisSerializer<String> {

    private Charset charset;
    private String keyPrefix;

    public KeyRedisStringSerializer(String keyPrefix) {
        this(keyPrefix, StandardCharsets.UTF_8);
    }

    public KeyRedisStringSerializer(String keyPrefix, Charset charset) {
        this.charset = charset;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public byte[] serialize(String string) throws SerializationException {
        if (StringUtils.isNotEmpty(keyPrefix)) {
            string = keyPrefix + string;
        }
        return (string == null ? null : string.getBytes(charset));
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        String saveKey = new String(bytes, charset);
        int indexOf = saveKey.indexOf(keyPrefix);
        if (indexOf > 0) {
            saveKey = saveKey.substring(indexOf);
        }
        return saveKey;
    }
}
