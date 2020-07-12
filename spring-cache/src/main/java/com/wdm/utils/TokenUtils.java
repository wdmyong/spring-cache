package com.wdm.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.wdm.model.User;

/**
 * @author wdmyong
 */
public final class TokenUtils {

    private static final Logger logger  = LoggerFactory.getLogger(TokenUtils.class);

    private static final String SPLIT = "_";
    private static final Joiner TOKEN_JOINER = Joiner.on(SPLIT);
    private static final Splitter TOKEN_SPLITTER = Splitter.on(SPLIT);
    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    private TokenUtils() {
    }

    public static String generateToken(User user) {
        // userId + updateTime + salt(当前时间)
        String text = TOKEN_JOINER.join(user.getId(), user.getUpdateTime(), System.currentTimeMillis());
        final byte[] textByte;
        try {
            textByte = text.getBytes("UTF-8");
            return encoder.encodeToString(textByte);
        } catch (UnsupportedEncodingException e) {
            logger.error("encode error for user:{}", user.getId());
        }
        return StringUtils.EMPTY;
    }

    public static long getUserIdByToken(String token) {
        try {
            String decodeText = new String(decoder.decode(token), "UTF-8");
            List<String> infos = TOKEN_SPLITTER.splitToList(decodeText);
            if (!CollectionUtils.isEmpty(infos) && infos.size() == 3) {
                // todo，检查时间啥的
                return NumberUtils.toLong(infos.get(0));
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("encode error for token:{}", token);
        }
        return 0;
    }

}
