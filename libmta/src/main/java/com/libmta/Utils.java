package com.libmta;

import android.content.Context;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Gson
 * @date 2021/8/3 10:55
 */
public class Utils {

    public static String md5(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append(0);
            }
            hex.append(Integer.toHexString(b & 0xff));
        }
        return hex.toString();

    }

    public static float getDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager systemService = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        systemService.getDefaultDisplay().getMetrics(dm);
        return dm.density;

    }

    public static String getAndroidId(Context contex) {
        return Settings.System.getString(contex.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getSign(String appid, String ac, String ts) {
        return md5(appid + "" + ac + "" + ts).toUpperCase();
    }

    public static String getSid() {
        char[] s = new char[36];
        String hexDigits = "0123456789abcdef";
        for (int i = 0; i < 36; i++) {
            s[i] = hexDigits.charAt((int) Math.floor(Math.random() * 0x10));
        }
        s[14] = '4';
        s[19] = hexDigits.charAt((s[19] & 0x3) | 0x8);
        s[8] = s[13] = s[18] = s[23] = '-';

        return s.toString();
    }
}
