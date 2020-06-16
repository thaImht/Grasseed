package com.hetao.grasseed.common.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;
import java.util.Map;

public class CryptUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
        fixKeyLength();
    }

    /**
     *
     * @param s
     * @param key 长度为32的字节数组（256位）密钥
     * @return
     * @throws java.security.GeneralSecurityException
     */
    public static byte[] pkcs7Encode(String s, byte[] key) throws java.security.GeneralSecurityException {
        //"BC"来指定使用BouncyCastle库
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(s.getBytes(StandardCharsets.UTF_8));
    }

    /**
     *
     * @param bytes
     * @param key 长度为32的字节数组（256位）密钥
     * @return
     * @throws java.security.GeneralSecurityException
     */
    public static String pkcs7Decode(byte[] bytes, byte[] key) throws java.security.GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decoded = cipher.doFinal(bytes);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    public static String md5(String clearText) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(clearText.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is unsupported", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MessageDigest不支持MD5Util", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] base64Decode(String s) {
        return Base64.getDecoder().decode(s);
    }

    //should use Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy
    private static void fixKeyLength() {
        String errorString = "Failed manually overriding key-length permissions.";
        int newMaxKeyLength;
        try {
            if ((newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES")) < 256) {
                Class c = Class.forName("javax.crypto.CryptoAllPermissionCollection");
                Constructor con = c.getDeclaredConstructor();
                con.setAccessible(true);
                Object allPermissionCollection = con.newInstance();
                Field f = c.getDeclaredField("all_allowed");
                f.setAccessible(true);
                f.setBoolean(allPermissionCollection, true);

                c = Class.forName("javax.crypto.CryptoPermissions");
                con = c.getDeclaredConstructor();
                con.setAccessible(true);
                Object allPermissions = con.newInstance();
                f = c.getDeclaredField("perms");
                f.setAccessible(true);
                ((Map) f.get(allPermissions)).put("*", allPermissionCollection);

                c = Class.forName("javax.crypto.JceSecurityManager");
                f = c.getDeclaredField("defaultPolicy");
                f.setAccessible(true);
                Field mf = Field.class.getDeclaredField("modifiers");
                mf.setAccessible(true);
                mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                f.set(null, allPermissions);

                newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES");
            }
        } catch (Exception e) {
            throw new RuntimeException(errorString, e);
        }
        if (newMaxKeyLength < 256)
            throw new RuntimeException(errorString); // hack failed
    }

    public static void main(String[] args) throws GeneralSecurityException {
//        System.out.println(
//                CryptUtil.base64Encode(
//                        CryptUtil.pkcs7Encode("1315714701", "".getBytes())
//                )
//        );

//        System.out.println(
//                CryptUtil.pkcs7Decode(
//                        CryptUtil.base64Decode("zxBSSRvFWqTVPoEt/Xov2u6doeU6XxjT9ixLXyVR0cjgZLnfTqqkVXiyx7ItcTOm"),
//                        "".getBytes()
//
//                )
//        );
    }

}
