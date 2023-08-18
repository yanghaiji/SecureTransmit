# SecureTransmit

主要用于在前后端分离项目中为保护在传输层的数据安全，实现的加解密解决方案

# 数据加密

目前项目中主要采用的是RSA的加密算法，公钥加密私钥解密的方式

项目中提供了生成key的策略，参考如下：

```
    public static void main(String[] args) {
        Map<String, Object> keyMap;
        try {
            keyMap = initKey();
            String publicKey = getPublicKey(keyMap);
            System.out.println(publicKey);
            String privateKey = getPrivateKey(keyMap);
            System.out.println(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```



# 使用参考
## 引入依赖

```
<dependency>
    <groupId>com.javayh.secure.transmit</groupId>
    <artifactId>securetransmit.boot.start</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 参加配置

```
secure:
  transmit:
    enable: true
    is-show-log: true
    type: rsa
    private-key: MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIEIV6jsOKyT396ykkPC+8aT0GlQwLTv+PoEwzU0dmdexnZ7NVUR8YeP6HUpAb40u6WYdMMQlAefL+K2+0wKMg9R6Ykdxw7mrB7kKJFttCVS02MMQzSQysTp6zCHJFHhCSVM8QF9Mu/CzC6A2+zEEyoCZwKhI2jdGSDixde9CCjtAgMBAAECgYA/D3Gv98p25Uoqz0DWZwufcAwR/EpB42nd3sf8T6hyOoppyys0aTGOXBFyeNkGOUVf19NwpcCCvRStC1pjPjRaQJ2dJwwkgpAjZF4dZgCh1bF7vTC3H8HQV09xfeGYOArAuOClD5dKxKPGyDjDtsNWstSkIvsneGNX8Xh0DhiNbQJBALxixDFoXnawZRci3Z0TkSuKEg3aO17jrwwfstqyrBLf50JXadmE5kDslX3C4F/BNoVPF2TQ9COXGYf23hta+B8CQQCvWBfoJbhqo5LzuBpD4e/R3YGyPlJRgawrT3PxntylgppuwRfRPFhleIcZsGI+8AUq0j8M+oo+qzJfi4ctyu1zAkEAsoGUDo0rFaRH5ghvniuwX2VRfjbQEzYD5KUUwQ6U5r2rUL2r2yWHWPXVIXnLSnC2zNMJA8rLy/2df6x5AcrNWQJAUjZzvx0wgo9/b1Z6uilNUJETJoeiASVTnFr8eeWKfu/liNhwWmJ479q7PYg+CFRxl8pMVcGC4C9UxwecshPbkQJAUwiOTwdNgvf67B1tw3rlJ5B270mpyc5ANINMXlGA0fjaP3rdKDTEPUTq4/Zwy0uq/T9zJDjSorbKenkc/Pxzdw==
    public-key: MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBCFeo7Disk9/espJDwvvGk9BpUMC07/j6BMM1NHZnXsZ2ezVVEfGHj+h1KQG+NLulmHTDEJQHny/itvtMCjIPUemJHccO5qwe5CiRbbQlUtNjDEM0kMrE6eswhyRR4QklTPEBfTLvwswugNvsxBMqAmcCoSNo3Rkg4sXXvQgo7QIDAQAB
    
```

这里的`private-key` 和 `public-key` 在实际项目中需要换成自己的key，参考[数据加密](#_数据加密)

## 示例

本文中提供了两个注解用于加解密 `@Encrypt` `@Decrypt` 在需要加解密的方法上使用注解即可

```
    @Encrypt
    @GetMapping(value = "/get/Encrypt")
    public ProbeLink getUsertEncrypt() {
        ProbeLink test = ProbeLink.builder().appName("test").contextPath("sys/api").status(1).build();
        return test;
    }
```

