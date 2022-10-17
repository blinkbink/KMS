package com.digisign.kms.util.encryption;

//import sun.misc.BASE64Decoder;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
//import java.util.Base64;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

//import org.bouncycastle.util.encoders.Base64;

public class AESEncryption {
    private static final String ALGO = "AES";
    private static final byte[] keyValue =
            new byte[] { 'c', '5', 'A', 'P', 'A', 'Y', 'M',	'e', 'n', 'T', 'a','e', 'S', 'K', '3', 'y' };


    public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
//        String encryptedValue = new BASE64Encoder().encode(encVal);
        String encryptedValue = Base64.encodeBase64String(encVal);
        return encryptedValue;
    }

    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
//        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decordedValue = Base64.decodeBase64(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    public static String decryptAlias(String encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Key key = loadKeyAlias();
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
        byte[] decordedValue = Base64.decodeBase64(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    private static Key loadKeyAlias() {
        Key key = new SecretKeySpec(keyAlias, ALGO);
        return key;
    }

    private static final byte[] keyDoc =
            new byte[] { 'Z', '1', 'a', 'x', 'd', '1', 'M',	'9', 'x', 'G', 'a','w', 'S', 'r', '1', 'Y' };

    private static final byte[] keyAlias =
            new byte[] { 'M', '1', '8', 'x', 'd', '1', '0',	'9', 'x', 'G', 'A','w', 'l', 'r', '1', 'e' };



    public static String encryptDoc(String Data) throws Exception {
        Key key = generateDocKey();
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        c.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(new byte[16]));
        byte[] encVal = c.doFinal(Data.getBytes());
//        String encryptedValue = new Base64().toBase64String(encVal);
        String encryptedValue = Base64.encodeBase64String(encVal);
        return encryptedValue;
    }

    public static String decryptDoc(String encryptedData) throws Exception {
        Key key = generateDocKey();
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
//        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decordedValue = Base64.decodeBase64(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateDocKey() throws Exception {
        Key key = new SecretKeySpec(keyDoc, ALGO);
        return key;
    }
}