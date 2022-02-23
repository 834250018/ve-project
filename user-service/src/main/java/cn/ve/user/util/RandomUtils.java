package cn.ve.user.util;

import java.security.SecureRandom;

/**
 * @author ve
 * @date 2021/8/3
 */
public class RandomUtils {

    public static String nextSmsCode() {
        return nextInteger(6, 10);
    }

    public static String nextInteger(int length, int bound) {
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(bound));
        }
        return stringBuilder.toString();
    }

}
