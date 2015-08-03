package com.framgia.lupx.frss.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by FRAMGIA\pham.xuan.lu on 31/07/2015.
 */
public class HashUtils {

    public static String MD5(String input) {
        String hasedString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashed = md.digest(input.getBytes());
            BigInteger bi = new BigInteger(1, hashed);
            String result = bi.toString(16);
            if (result.length() % 2 != 0) {
                hasedString = "0" + result;
            }
            hasedString = result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hasedString;
    }

}