package com.digisign.kms.util.encryption;

import com.digisign.kms.util.LogSystem;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

//import java.util.Base64;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;

public class KeyEncryption {
    private static final String ALGO = "AES/ECB/PKCS5Padding";
    private static Key kst;

    public static String encrypt(String Data) throws Exception {
        Key key = loadKey();
        Cipher c = Cipher.getInstance(ALGO,"nCipherKM");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());

//        String encryptedValue = new BASE64Encoder().encode(encVal);
        String encryptedValue = Base64.encodeBase64String(encVal);
        return encryptedValue;
    }

    public static String decrypt(String encryptedData) throws Exception {
        Key key = loadKey();
        Cipher c = Cipher.getInstance(ALGO,"nCipherKM");
        c.init(Cipher.DECRYPT_MODE, key);
//        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decordedValue = Base64.decodeBase64(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key loadKey() throws Exception {
        if(kst==null) {
            System.setProperty("protect", "module");
            FileInputStream in=new FileInputStream("/opt/jks/aes.keystore");
            final KeyStore tmpKs = KeyStore.getInstance("nCipher.sworld", "nCipherKM");
            tmpKs.load(in, null);
            kst=tmpKs.getKey("aeskey", null);
            LogSystem.info("Load keystore AES");

        }
        return kst;
    }
}