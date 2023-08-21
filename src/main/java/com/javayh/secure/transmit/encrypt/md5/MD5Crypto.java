package com.javayh.secure.transmit.encrypt.md5;

import com.javayh.secure.transmit.constant.EncryptConstant;
import com.javayh.secure.transmit.exception.SecretException;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * <p>
 * md5加密算法
 * </p>
 *
 * @author Dylan-haiji
 * @version 1.0.0
 */
@Slf4j
public class MD5Crypto {

    /**
     * <p>
     * MD5 进行hash取模
     * </p>
     *
     * @param s
     * @return java.lang.String
     * @version 1.0.0
     * @author Dylan-haiji
     * @since 2020/3/24
     */
    public static String hash32(String s) {
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance(EncryptConstant.MD5);
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = EncryptConstant.HEXADECIMAL[byte0 >>> 4 & 0xf];
                str[k++] = EncryptConstant.HEXADECIMAL[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.error("MD5 hash32 Exception {}", e.getMessage());
            throw new SecretException(e.getMessage());
        }
    }

}
