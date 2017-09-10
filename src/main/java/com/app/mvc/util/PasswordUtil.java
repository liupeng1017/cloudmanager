package com.app.mvc.util;

import java.util.Date;
import java.util.Random;

/**
 * Created by jimin on 16/4/4.
 */
public class PasswordUtil {

    public final static String[] _NUM = { "2", "3", "4", "5", "6", "7", "8", "9" };

    public final static String[] _WORD = { "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "A",
            "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "~", "!", "@", "#", "$", "%", "^",
            "&", "+", "?" };

    public static String randomPassword() {
        StringBuffer password = new StringBuffer();
        Random random = new Random(new Date().getTime());
        Boolean flag = false;
        int length = random.nextInt(3) + 8;
        for (int i = 0; i < length; i++) {
            if (flag) {
                password.append(_NUM[random.nextInt(_NUM.length)]);
            } else {
                password.append(_WORD[random.nextInt(_WORD.length)]);
            }
            flag = !flag;
        }
        return password.toString();
    }
}
