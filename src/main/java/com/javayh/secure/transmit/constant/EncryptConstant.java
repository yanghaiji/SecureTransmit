package com.javayh.secure.transmit.constant;

/**
 * <p>
 * 加密常量
 * https://github.com/yanghaiji/javayh-platform/blob/master/javayh-dependencies/javayh-common-starter/src/main/java/com/javayh/common/encrypt/rsa/
 * </p>
 *
 * @author Dylan-haiji
 * @version 1.0.0
 * @since 2020-03-25 17:34
 */
public interface EncryptConstant {

	String ALGORITHM_AES = "AES";

	String AES_GCM = "AES/GCM/NoPadding";

	String INSTANCE = "AES/ECB/PKCS5Padding";

	char[] HEXADECIMAL = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
			'c', 'd', 'e', 'f' };

	char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
			'c', 'd', 'e', 'f', 'g' };

	char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
			'C', 'D', 'E', 'F', 'G' };

	String MD5 = "MD5";

	String KEY_ALGORITHM = "RSA";

	String PUBLIC_KEY = "RSAPublicKey";

	String PRIVATE_KEY = "RSAPrivateKey";

	String ALGORITHM = "RSA/ECB/PKCS1Padding";

	String RSA_SIGNATURE = "SHA256withRSA";

	String SHA = "SHA-256";

	String ECIES = "ECIES";

	String BC = "BC";

	String EC = "EC";

	String ECDSA = "ECDSA";
}
