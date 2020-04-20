package org.jiong.filetree.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * to encode user info to a key and
 * decode user info from a key
 *
 * @author Mr.Jiong
 */
@Slf4j
public class Encrypt {

    int offset = 2;

    public String encode(String... args) {
        Assert.notNull(args, "Args can not be null");

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            String keyName = "fact" + i;

            map.put(keyName, arg);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String key = objectMapper.writeValueAsString(map);
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < key.length(); i++) {
                char c = key.charAt(i);
                int newChar = c + offset;
                sb.append(((char) newChar));
            }

            String newKey = sb.toString();
            log.debug("transform string : {}", newKey);
            String encodeToString = Base64Utils.encodeToString(newKey.getBytes());
            log.debug("Base64 encoded string: {}", encodeToString);
            return encodeToString;
        } catch (JsonProcessingException e) {
            log.warn("json processing failed", e);
            throw new RuntimeException(e);
        }

    }

    public Map<String, String> decode(String key) {
        byte[] bytes = Base64Utils.decodeFromString(key);
        String newKey = new String(bytes);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < newKey.length(); i++) {
            int originChar = newKey.charAt(i) - offset;
            sb.append((char) originChar);
        }

        String originKey = sb.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(originKey, HashMap.class);
        } catch (IOException e) {
            log.warn("json processing failed", e);
            return null;
        }
    }
}
