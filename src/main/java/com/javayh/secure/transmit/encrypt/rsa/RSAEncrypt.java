package com.javayh.secure.transmit.encrypt.rsa;

import com.javayh.secure.transmit.constant.EncryptConstantUtils;
import com.javayh.secure.transmit.encrypt.base.Base64Util;
import org.apache.commons.codec.binary.Base64;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 生成公钥私钥
 * https://github.com/yanghaiji/javayh-platform/blob/master/javayh-dependencies/javayh-common-starter/src/main/java/com/javayh/common/encrypt/rsa/
 * </p>
 *
 * @author zhao-baolin
 * @version 1.0.0
 * @since 2020/3/5
 */
public class RSAEncrypt {


    /**
     * 获得公钥
     */
    private static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        // 获得map中的公钥对象 转为key对象
        Key key = (Key) keyMap.get(EncryptConstantUtils.PUBLIC_KEY);
        // 编码返回字符串
        return Base64Util.encode(key.getEncoded());
    }

    /**
     * 获得私钥
     */
    private static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        // 获得map中的私钥对象 转为key对象
        Key key = (Key) keyMap.get(EncryptConstantUtils.PRIVATE_KEY);
        // 编码返回字符串
        return Base64Util.encode(key.getEncoded());
    }


    /**
     * <p>
     * map对象中存放公私钥
     * </p>
     *
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author Dylan-haiji
     */
    public static Map<String, Object> initKey() throws Exception {
        /* 获得对象 KeyPairGenerator 参数 RSA 256个字节 */
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(EncryptConstantUtils.KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        /* 通过对象 KeyPairGenerator 获取对象KeyPair */
        KeyPair keyPair = keyPairGen.generateKeyPair();
        /* 通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey */
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        /* 公私钥对象存入map中 */
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(EncryptConstantUtils.PUBLIC_KEY, publicKey);
        keyMap.put(EncryptConstantUtils.PRIVATE_KEY, privateKey);
        return keyMap;
    }


    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static void main(String[] args) {
        Map<String, Object> keyMap;
        try {
            keyMap = initKey();
            String publicKey = getPublicKey(keyMap);
            System.out.println(publicKey);
            String privateKey = getPrivateKey(keyMap);
            System.out.println(privateKey);
            String encryptString = RsaTools.encrypt(publicKey, "1234");
            System.out.println(encryptString);
            String decryptString = RsaTools.decrypt("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIEIV6jsOKyT396ykkPC+8aT0GlQwLTv+PoEwzU0dmdexnZ7NVUR8YeP6HUpAb40u6WYdMMQlAefL+K2+0wKMg9R6Ykdxw7mrB7kKJFttCVS02MMQzSQysTp6zCHJFHhCSVM8QF9Mu/CzC6A2+zEEyoCZwKhI2jdGSDixde9CCjtAgMBAAECgYA/D3Gv98p25Uoqz0DWZwufcAwR/EpB42nd3sf8T6hyOoppyys0aTGOXBFyeNkGOUVf19NwpcCCvRStC1pjPjRaQJ2dJwwkgpAjZF4dZgCh1bF7vTC3H8HQV09xfeGYOArAuOClD5dKxKPGyDjDtsNWstSkIvsneGNX8Xh0DhiNbQJBALxixDFoXnawZRci3Z0TkSuKEg3aO17jrwwfstqyrBLf50JXadmE5kDslX3C4F/BNoVPF2TQ9COXGYf23hta+B8CQQCvWBfoJbhqo5LzuBpD4e/R3YGyPlJRgawrT3PxntylgppuwRfRPFhleIcZsGI+8AUq0j8M+oo+qzJfi4ctyu1zAkEAsoGUDo0rFaRH5ghvniuwX2VRfjbQEzYD5KUUwQ6U5r2rUL2r2yWHWPXVIXnLSnC2zNMJA8rLy/2df6x5AcrNWQJAUjZzvx0wgo9/b1Z6uilNUJETJoeiASVTnFr8eeWKfu/liNhwWmJ479q7PYg+CFRxl8pMVcGC4C9UxwecshPbkQJAUwiOTwdNgvf67B1tw3rlJ5B270mpyc5ANINMXlGA0fjaP3rdKDTEPUTq4/Zwy0uq/T9zJDjSorbKenkc/Pxzdw==", "YkvR4+2Ft85pgX+FTUqVna/Kqad7u4F9KF7jy11fhbycuq/AfSIGFEl3LTFkhs+HzAHX7AwB0v2HDabtDAqAHcI0f0XQG2oDpNUdnVsMdmyJj9cE+/NL9A1lHz1lCsGuzM/ETZ5FiLZG4iQJvQodWoIfmuOBeDJphwDMKpozfio=");
            System.out.println(decryptString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
