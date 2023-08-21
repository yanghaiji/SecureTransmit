package com.javayh.secure.transmit.encrypt.rsa;


import com.javayh.secure.transmit.constant.ConstantUtils;
import com.javayh.secure.transmit.constant.EncryptConstant;

/**
 * <p>
 * https://github.com/yanghaiji/javayh-platform/blob/master/javayh-dependencies/javayh-common-starter/src/main/java/com/javayh/common/encrypt/rsa/
 * </p>
 *
 * @author Dylan-haiji
 * @version 1.0.0
 * @since 2020/3/5
 */
public class HexUtil {

    /**
     * 16进制转byte数组
     *
     * @param data 16进制字符串
     * @return byte数组
     * @throws Exception 转化失败的异常
     */
    public static byte[] hex2Bytes(final String data) throws Exception {
        final int len = data.length();
        if ((len & 0x01) != 0) {
            throw new Exception("Odd number of characters.");
        }
        final byte[] out = new byte[len >> 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data.charAt(j), j) << 4;
            j++;
            f = f | toDigit(data.charAt(j), j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    /**
     * bytes数组转16进制String
     *
     * @param data bytes数组
     * @return 转化结果
     */
    public static String bytes2Hex(final byte[] data) {
        return bytes2Hex(data, true);
    }

    /**
     * bytes数组转16进制String
     *
     * @param data        bytes数组
     * @param toLowerCase 是否小写
     * @return 转化结果
     */
    public static String bytes2Hex(final byte[] data, final boolean toLowerCase) {
        return bytes2Hex(data, toLowerCase ? EncryptConstant.DIGITS_LOWER
                : EncryptConstant.DIGITS_UPPER);
    }

    /**
     * bytes数组转16进制String
     *
     * @param data     bytes数组
     * @param toDigits DIGITS_LOWER或DIGITS_UPPER
     * @return 转化结果
     */
    private static String bytes2Hex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return new String(out);
    }

    /**
     * 16转化为数字
     *
     * @param ch    16进制
     * @param index 索引
     * @return 转化结果
     * @throws Exception 转化失败异常
     */
    private static int toDigit(final char ch, final int index) throws Exception {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new Exception(
                    "Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    /**
     * <p>
     * 16进制字符串转字符串
     * </p>
     *
     * @param hex
     * @return java.lang.String
     * @version 1.0.0
     * @author Dylan-haiji
     * @since 2020/3/5
     */
    public static String hex2String(String hex) throws Exception {
        String r = bytes2String(hexString2Bytes(hex));
        return r;
    }

    /**
     * <p>
     * 字节数组转字符串
     * </p>
     *
     * @param b
     * @return java.lang.String
     * @version 1.0.0
     * @author Dylan-haiji
     * @since 2020/3/5
     */
    public static String bytes2String(byte[] b) throws Exception {
        String r = new String(b, ConstantUtils.UTF);
        return r;
    }

    /**
     * <p>
     * 16进制字符串转字节数组
     * </p>
     *
     * @param hex
     * @return byte[]
     * @version 1.0.0
     * @author Dylan-haiji
     * @since 2020/3/5
     */
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }
    }

    /**
     * <p>
     * 字符转换为字节
     * </p>
     *
     * @param c
     * @return byte
     * @version 1.0.0
     * @author Dylan-haiji
     * @since 2020/3/5
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * <p>
     * 字符串转16进制字符串
     * </p>
     *
     * @param s
     * @return java.lang.String
     * @version 1.0.0
     * @author Dylan-haiji
     * @since 2020/3/5
     */
    public static String string2HexString(String s) throws Exception {
        String r = bytes2HexString(string2Bytes(s));
        return r;
    }

    /**
     * <p>
     * 字节数组转16进制字符串
     * </p>
     *
     * @param b
     * @return java.lang.String
     * @version 1.0.0
     * @author Dylan-haiji
     * @since 2020/3/5
     */
    public static String bytes2HexString(byte[] b) {
        String r = "";

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }

        return r;
    }

    /**
     * <p>
     * 字符串转字节数组
     * </p>
     *
     * @param s
     * @return byte[]
     * @version 1.0.0
     * @author Dylan-haiji
     * @since 2020/3/5
     */
    public static byte[] string2Bytes(String s) {
        byte[] r = s.getBytes();
        return r;
    }

}
