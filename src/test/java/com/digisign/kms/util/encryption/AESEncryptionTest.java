package com.digisign.kms.util.encryption;

import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class AESEncryptionTest {

//    @Test
//    void decryptAlias() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
//        String keyAliasEnc="68JParzL7GS2jwtimm18+h9GEPqP4Fz0X3Q4ORA0o4qOk83uJEmI1NfEllYgTe39";
//        assertEquals("RG-dzrZa71DeEx6ebfuA82Wy39qJKpiuM52PgSmGMeoco3b", AESEncryption.decryptAlias(keyAliasEnc));
//    }
}