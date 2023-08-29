package com.javayh.secure.transmit.encrypt;

import com.javayh.secure.transmit.constant.EncryptConstant;
import com.javayh.secure.transmit.encrypt.base.Base64Util;
import com.javayh.secure.transmit.encrypt.md5.MD5Crypto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * https://github.com/yanghaiji/javayh-platform/blob/master/javayh-dependencies/javayh-common-starter/src/main/java/com/javayh/common/encrypt/rsa/
 * 系统生成data key的生成方式
 * </p>
 *
 * @author Dylan-haiji
 * @version 1.0.0
 * @since 2020-03-24 15:40
 */
@Slf4j
public class DataKeyGenerator {

    public static class AES {
        /**
         * <p>
         * 安全随机种子
         * </p>
         *
         * @param keySize
         * @param seed
         * @return java.lang.String
         * @version 1.0.0
         * @author Dylan-haiji
         * @since 2020/3/24
         */
        public static String initKey(int keySize, long seed) {
            String key = "";
            try {
                KeyGenerator generator = KeyGenerator
                        .getInstance(EncryptConstant.ALGORITHM_AES);
                SecureRandom random = new SecureRandom();
                random.setSeed(seed);
                generator.init(keySize, random);
                SecretKey secretKey = generator.generateKey();
                key = MD5Crypto.hash32(new String(secretKey.getEncoded()));
            } catch (Exception e) {
                log.error("DataKeyGenerator NoSuchAlgorithmException{}", e.getMessage());
            }
            return key;
        }
    }


    public static class RSA {

        /**
         * 获得公钥
         */
        public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
            // 获得map中的公钥对象 转为key对象
            Key key = (Key) keyMap.get(EncryptConstant.PUBLIC_KEY);
            // 编码返回字符串
            return Base64Util.encode(key.getEncoded());
        }

        /**
         * 获得私钥
         */
        public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
            // 获得map中的私钥对象 转为key对象
            Key key = (Key) keyMap.get(EncryptConstant.PRIVATE_KEY);
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
                    .getInstance(EncryptConstant.KEY_ALGORITHM);
            keyPairGen.initialize(1024);
            /* 通过对象 KeyPairGenerator 获取对象KeyPair */
            KeyPair keyPair = keyPairGen.generateKeyPair();
            /* 通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey */
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            /* 公私钥对象存入map中 */
            Map<String, Object> keyMap = new HashMap<String, Object>(2);
            keyMap.put(EncryptConstant.PUBLIC_KEY, publicKey);
            keyMap.put(EncryptConstant.PRIVATE_KEY, privateKey);
            return keyMap;
        }


        public static PublicKey getPublicKey(String key) throws Exception {
            byte[] keyBytes = Base64.decodeBase64(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(EncryptConstant.KEY_ALGORITHM);
            return keyFactory.generatePublic(keySpec);
        }

        public static PrivateKey getPrivateKey(String key) throws Exception {
            byte[] keyBytes = Base64.decodeBase64(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(EncryptConstant.KEY_ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        }

        /**
         * 使用私钥进行签名
         *
         * @param data       待验签的数据
         * @param privateKey 私钥
         */
        public static String signData(String data, String privateKey) throws Exception {
            Signature signature = Signature.getInstance(EncryptConstant.RSA_SIGNATURE);
            signature.initSign(getPrivateKey(privateKey));
            signature.update(data.getBytes());
            byte[] signBytes = signature.sign();
            return Base64Util.encode(signBytes);
        }

        /**
         * 使用公钥进行验签
         *
         * @param data      待验签的数据
         * @param signature 前言数据
         * @param publicKey 公钥
         */
        public static boolean verifySignature(String data, String signature, String publicKey) throws Exception {
            Signature verifier = Signature.getInstance(EncryptConstant.RSA_SIGNATURE);
            verifier.initVerify(getPublicKey(publicKey));
            verifier.update(data.getBytes());
            byte[] signatureBytes = Base64Util.decode(signature);
            return verifier.verify(signatureBytes);
        }

    }


    public static class ECC {

        static {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        }

        /**
         * 生成秘钥对
         * <p>
         * {@link org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi.ecParameters (line #173)}
         * 192, 224, 239, 256, 384, 521
         */
        public static KeyPair generateKeyPair() throws Exception {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(EncryptConstant.EC, EncryptConstant.BC);
            keyPairGenerator.initialize(521);
            return keyPairGenerator.generateKeyPair();
        }

        /**
         * 获取公钥/私钥(Base64编码)
         */
        @SneakyThrows
        public static String keyToString(Key key) {
            byte[] keyBytes = key.getEncoded();
            return Base64Util.encode(keyBytes);
        }
    }


    public static class GCM {
        // 生成 AES-GCM 密钥
        public static SecretKey generateAESGCMKey() throws NoSuchAlgorithmException {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            // key size: must be equal to 128, 192 or 256 使用 256 位密钥
            keyGenerator.init(256);
            return keyGenerator.generateKey();
        }

        // 将密钥转换为字符串
        public static String keyToString(SecretKey secretKey) {
            byte[] encodedKey = secretKey.getEncoded();
            return Base64Util.encode(encodedKey);
        }

    }
}
