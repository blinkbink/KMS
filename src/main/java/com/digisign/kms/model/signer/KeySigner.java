package com.digisign.kms.model.signer;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

public class KeySigner {

    PrivateKey pv;
    PublicKey pb;
    Certificate[] cert;
    Long pv_ID=null;
    Date expiredCert;

    public Date getExpiredCert() {
        return expiredCert;
    }

    public PrivateKey getPv() {
        return pv;
    }

    public PublicKey getPb() {
        return pb;
    }

    public Certificate[] getCert() {
        return cert;
    }

    public PrivateKey getPrivateKey(String base64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PrivateKey hasil=null;
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec privateKeySpec = new PKCS8EncodedKeySpec(java.util.Base64.getDecoder().decode(base64));
//	    KeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.gdecode(base64));
        hasil = keyFactory.generatePrivate(privateKeySpec);
        return hasil;
    }

    public PublicKey getPublicKey(String base64) throws NoSuchAlgorithmException, InvalidKeySpecException, CertificateException {

        PublicKey publicKey =
                KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(java.util.Base64.getDecoder().decode(base64)));

        return publicKey;
    }

    public Certificate[] getCert(String cert) throws CertificateException {
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream bis=new ByteArrayInputStream(java.util.Base64.getDecoder().decode(cert));
        Certificate c = fact.generateCertificate(bis);
        Certificate[] lCert=new Certificate[1];
        lCert[0]=c;

        return lCert;
    }
}
