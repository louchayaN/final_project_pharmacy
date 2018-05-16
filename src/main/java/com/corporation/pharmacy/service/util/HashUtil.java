package com.corporation.pharmacy.service.util;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtil {

    private HashUtil() {
        throw new AssertionError("Class contains static methods only. You should not instantiate it!");
    }

    public static String toHash(String value) {
        String hashedValue = DigestUtils.sha256Hex(value);
        return hashedValue;
    }

}
