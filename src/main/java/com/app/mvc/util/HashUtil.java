package com.app.mvc.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jimin on 15/12/11.
 */
@Slf4j
public class HashUtil {

    public HashUtil() {
    }

    public static byte[] hash(String algorithm, byte[] bytes) throws NoSuchAlgorithmException {
        if (algorithm != null && bytes != null) {
            MessageDigest md = MessageDigest.getInstance(algorithm.toUpperCase());
            return md.digest(bytes);
        } else {
            return null;
        }
    }

    public static byte[] hash(String algorithm, String str) throws NoSuchAlgorithmException {
        return str == null ? null : hash(algorithm, str.getBytes());
    }

    public static byte[] hashMD5(byte[] bytes) {
        try {
            return hash("MD5", bytes);
        } catch (NoSuchAlgorithmException var2) {
            log.error("The algorithm is not available in the caller\'s environment.", var2);
            return null;
        }
    }

    public static byte[] hashMD5(String str) {
        return str == null ? null : hashMD5(str.getBytes());
    }

    public static byte[] hashSHA1(String str) {
        try {
            return hash("SHA1", str);
        } catch (NoSuchAlgorithmException var2) {
            log.error("The algorithm is not available in the caller\'s environment.", var2);
            return null;
        }
    }
}
