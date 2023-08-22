# SecureTransmit

`SecureTransmit`致力于应对传输层数据安全问题，为广泛的安全需求提供高效而综合的解决方案。其核心目标在于确保通过网络传输的数据在各个环节得到可靠的保护，从而防范潜在的安全威胁。
作为其关键特征，`SecureTransmit`不仅赋予用户将全字段加密的能力，更为难得的是，其还允许用户在需求情景下实现针对特定字段的精确加密，从而最大程度地维护数据隐私。
值得一提的是，`SecureTransmit`秉承高标准的加密实践，全面支持行业主流的`RSA`、`AES`等加解密算法(更加详细的加密算法请查看[加密算法的规划](#加密算法的规划))，为用户提供可信赖的安全通信通道。
无论是在数据的传输、存储，亦或是处理过程中，`SecureTransmit`均旨在确保数据的机密性与完整性，为您的敏感信息提供最优保障。

# 加密算法的规划

✅ 表示支持    ⬜ 表示暂未支持

| 算法名称                              | 简要介绍                                                     | 是否支持 |
| ------------------------------------- | ------------------------------------------------------------ | -------- |
| AES（Advanced Encryption Standard）   | 是一种对称加密算法，用于替代过时的DES（Data Encryption Standard）。AES支持128位、192位和256位密钥长度，被广泛用于保护敏感信息。 | ✅        |
| RSA（Rivest-Shamir-Adleman            | 是一种非对称加密算法，用于实现数据的加密和数字签名。RSA基于数学问题，涉及公钥和私钥，公钥用于加密，私钥用于解密。 | ✅        |
| Diffie-Hellman                        | 是一种用于安全地交换密钥的协议，通过不安全的通信渠道，双方可以生成一个共享的密钥，然后用于对称加密。 | ⬜        |
| ECC（Elliptic Curve Cryptography      | 是一种基于椭圆曲线数学问题的非对称加密算法，与RSA相比，可以实现相同的安全性，但使用更短的密钥长度。 | ⬜        |
| Blowfish                              | 是一种对称加密算法，适用于加密较大的数据块，由于其速度较快，被广泛用于加密通信和文件 | ⬜        |
| Twofish                               | 也是一种对称加密算法，是Blowfish的后续版本，被认为在安全性方面更为强大。 | ⬜        |
| DES（Data Encryption Standard）       | 是一种对称加密算法，但由于其56位密钥长度较短，已被认为不够安全，逐渐被AES所取代。 | ⬜        |
| 3DES（Triple Data Encryption Standard | 是对DES的改进，通过多次应用DES算法来增加安全性，但由于其计算开销较大，逐渐被更高效的加密算法取代。 | ⬜        |

# 数据加密

目前项目中主要采用的是RSA的加密算法，公钥加密私钥解密的方式

项目中提供了生成key的策略，参考如下：

```
    public static void main(String[] args) throws Exception {
        String aes = AES.initKey(256, 256);
        Map<String, Object> rsa = RSA.initKey();
        System.out.println(aes);
        System.out.println(RSA.getPrivateKey(rsa));
        System.out.println(RSA.getPublicKey(rsa));
    }
```



# 使用参考
## 制作依赖

```
mvn clean package install
```

## 引入依赖

```
<dependency>
    <groupId>com.javayh.secure.transmit</groupId>
    <artifactId>secure.transmit.boot.starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 参加配置

```
secure:
  transmit:
    enable: true
    is-show-log: true
    type: aes
    # aes 加解密
    aes:
        # 自定义的 iv 加言
      iv: 长度为10的iv
        # 生成的 key
      key: 生成的key
    # rsa 加解密
#   rsa:
#     private-key: 生成的私钥
#     public-key: 生成公钥
#
```

这里的`private-key` 和 `public-key` 在实际项目中需要换成自己的key，参考[数据加密](#_数据加密)

## 示例1

提供了两个注解用于全字段加解密 `@Encrypt` `@Decrypt` 在需要加解密的方法上使用注解即可

```
    @Encrypt
    @GetMapping(value = "/get/Encrypt")
    public ProbeLink getUsertEncrypt() {
        ProbeLink test = ProbeLink.builder().appName("test").contextPath("sys/api").status(1).build();
        return test;
    }
```

提供了两个注解用于全字段加解密 `@Encrypt` `@Decrypt` 在需要加解密的方法上使用注解即可

## 示例2

提供了两个注解用于全字段加解密 `@SecureCrypto` `@EncryptField` `@DecryptField` 在需要加解密的方法和指定的字段上即可

```
    @SecureCrypto
    @PostMapping(value = "/add/Encrypt/Decrypt")
    public ProbeLink userDecrypt(@RequestBody ProbeLink probeLink) {
        return probeLink;
    }


    public class ProbeLink {
    
    
        private Long id;
    
    
        /**
         * 服务名
         */
        @DecryptField
        @EncryptField
        private String appName;
    
        /**
         * context path
         */
        private String contextPath;
    
        /**
         * 方法
         */
        @EncryptField
        private String method;
    }
```

