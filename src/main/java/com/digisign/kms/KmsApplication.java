package com.digisign.kms;

import com.digisign.kms.core.pdf.LTV;
import com.digisign.kms.core.pdf.QRCode;
import com.digisign.kms.core.pdf.Signing;
import com.digisign.kms.model.signer.UserSignature;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.*;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.Attribute;

import java.awt.geom.Rectangle2D;
import java.security.cert.CertificateFactory;

import com.digisign.kms.model.signer.KeySigner;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.ExternalSigningSupport;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.digisign.kms.core.pdf.Signing.*;

@SpringBootApplication
public class KmsApplication {
//
//    static KeySigner signer = new KeySigner();
//		static String keyAlias =  "bzuiBE0a75K0A3GG7t7OEidbkKPo1dMWp/5nrz8XapemAbshfqFzfexYWfn4/2Ym";
//		static String base64 = "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";
//		static Certificate[] cert;
//
//    static {
//        try {
//            cert = signer.getCert(base64);
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        }
//    }


    public static void main(String[] args) throws Exception {

        //LTV Check
//        LTV check = new LTV();
//        String basePath = "C:\\SNI\\Digisign\\mitraapi\\kms\\tesdoc\\";
//        File resource = new File(basePath+"SuratPemjSept2018 (5).pdf");
//        check.checkLTV(resource);

//        String basePath = "C:\\SNI\\Digisign\\mitraapi\\kms\\tesdoc\\";

//        File resource = new File(basePath+"doc0.pdf");
//        OutputStream result = new FileOutputStream(new File(basePath, "testFieldWithLocking.pdf"));
//        PDDocument pdDocument = PDDocument.load(resource);
//
//        PDAcroForm acroForm = pdDocument.getDocumentCatalog().getAcroForm();
//        if (acroForm == null)
//        {
//            acroForm = new PDAcroForm(pdDocument);
//            pdDocument.getDocumentCatalog().setAcroForm(acroForm);
//        }
//        PDSignatureField signatureField = new PDSignatureField(acroForm);
//        signatureField.setPartialName("Seal");
//        acroForm.getFields().add(signatureField);
//        signatureField.getWidgets().get(0).setPage(pdDocument.getPage(0));
//        pdDocument.getPage(0).getAnnotations().add(signatureField.getWidgets().get(0));
//        signatureField.getWidgets().get(0).setRectangle(new PDRectangle(100, 600, 300, 200));
//        setLock(signatureField, acroForm);
//        pdDocument.save(result);
//
//
//        InputStream resource2 = new FileInputStream(new File(basePath, "testFieldWithLocking.pdf"));
//            OutputStream result2 = new FileOutputStream(new File(basePath, "testSignedWithLocking.pdf"));
//            PDDocument pdDocument2 = PDDocument.load(resource2);
//
//
//        signExistingFieldWithLock(pdDocument2, result2, data -> signWithSeparatedHashing(data));

		SpringApplication.run(KmsApplication.class, args);

//		String basePath = "C:\\SNI\\Digisign\\mitraapi\\kms\\tesdoc\\";
//		PDDocument doc = PDDocument.load(new File(basePath+"doc0_rotated (1).pdf"));
//		PDPage page = doc.getPage(0);
//
//		LogSystem.info("Dokumen rotasi " +page.getRotation());
//		LogSystem.info("Dokumen rotasi " +page.getMediaBox().getWidth());
//		LogSystem.info("Dokumen rotasi " +page.getMediaBox().getHeight());
//
//		KeySigner signer = new KeySigner();
//		String keyAlias =  "bzuiBE0a75K0A3GG7t7OEidbkKPo1dMWp/5nrz8XapemAbshfqFzfexYWfn4/2Ym";
//		String base64 = "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";
//		Certificate[] cert = signer.getCert(base64);
////
//		String basePath = "C:\\SNI\\Digisign\\mitraapi\\kms\\tesdoc\\";
////
//		Signing signing = new Signing(cert, keyAlias);
//		signing.setExternalSigning(true);
//		signing.setImageFile(new File(basePath+"ttd.png"));
//
//		//////		//90
//		float lx = 188;
//		float ly = 577;
//		float rx = 288;
//		float ry = 677;
////		float lx2 = 464;
////		float ly2 = 259;
////		float rx2 = 526;
////		float ry2 = 321;
//
////		//270
////		float lx = 114;
////		float ly = 138;
////		float rx = 238;
////		float ry = 201;
//// 		float lx2 = 138;
////		float ly2 = 244;
////		float rx2 = 200;
////		float ry2 = 307;
//
//		Rectangle2D humanRect = new Rectangle2D.Float(lx, ry, rx - lx, ry - ly);
////////
//		File doc = new File(basePath+"DS20220920092415.pdf"); //0
//////////		File doc = new File(basePath+"DS20220618061546.pdf"); //90
//////////		File doc = new File(basePath+"DS20220622093620.pdf"); //90
//////////		File doc = new File(basePath+"220703153833798879817.pdf"); //270
//////////		File doc = new File(basePath+"220704155955285602776.pdf"); //270
//		File out = new File(basePath+"out1.pdf");
//////////		File out2 = new File(basePath+"out2.pdf");
////////		String TSA="http://192.168.78.17:8080/signserver/process?workerName=TimeStampSigner";
////////
//		UserSignature user = new UserSignature();
//		user.setDescOnly(false);
//		user.setType("seal");
//		user.setVisible(true);
//		user.setLevel("C5");
//		user.setQrText("TESasdasdT");
//		user.setDescOnly(false);
//////
////		signing.signPDF(doc, out, humanRect, null, 0, "TESasdasdT", false, "DEA DEVINA RAKHMITYAS", user);
//		signing.signPDF(doc, out, humanRect, null, 0, "TEST", true, "MUHAMMAD IQBAL PRATAMA", user, new JSONObject());

//		//Test allobank
//		QRCode qr = new QRCode();
////
//		byte qronly = 0;
//		String basePath = "C:\\SNI\\Digisign\\mitraapi\\testtd\\";
////
//////		qr.generateImageSignNoQr("AALYA PALOMA CARPENTA LOVA", basePath+"much.png", basePath+"out.png", new Date());
//////
//		qr.generateQRCodeImage2("1","1", basePath+"logo3.png", "AALYA PALOMA CARPENTA LOVA", new Date(), basePath+"sign.png", basePath+"out.png", qronly, false, false);

//		BufferedImage image = ImageIO.read(new File(basePath+"out.png"));

//		qr.alloBank("Test", basePath+"logo1.png", "Muhammad Iqbal Pratama", new Date(), basePath+"sign.png", basePath+"out1.png", qronly, false, false);
//		qr.alloBank("Test", basePath+"logo2.png", "Muhammad iqbal pratama", new Date(), basePath+"sign.png", basePath+"out2.png", qronly, false, false);
//		qr.alloBank("Test", basePath+"logo3.png", "Muhammad iqbal pratama", new Date(), basePath+"sign.png", basePath+"out3.png", qronly, false, false);
	}

//    static void signExistingFieldWithLock(PDDocument document, OutputStream output, SignatureInterface signatureInterface) throws IOException {
//        PDSignatureField signatureField = document.getSignatureFields().get(0);
//        PDSignature signature = new PDSignature();
//        signatureField.setValue(signature);
//
//        COSBase lock = signatureField.getCOSObject().getDictionaryObject(COS_NAME_LOCK);
//        COSDictionary lockDict = null;
//        if (lock instanceof COSDictionary) {
//            lockDict = (COSDictionary) lock;
//            COSDictionary transformParams = new COSDictionary(lockDict);
//            transformParams.setItem(COSName.TYPE, COSName.getPDFName("TransformParams"));
//            transformParams.setItem(COSName.V, COSName.getPDFName("1.2"));
//            transformParams.setInt(COSName.P, 1);
//            transformParams.setDirect(true);
//            COSDictionary sigRef = new COSDictionary();
//            sigRef.setItem(COSName.TYPE, COSName.getPDFName("SigRef"));
//            sigRef.setItem(COSName.getPDFName("TransformParams"), transformParams);
//            sigRef.setItem(COSName.getPDFName("TransformMethod"), COSName.getPDFName("FieldMDP"));
//            sigRef.setItem(COSName.getPDFName("Data"), document.getDocumentCatalog());
//            sigRef.setDirect(true);
//            COSArray referenceArray = new COSArray();
//            referenceArray.add(sigRef);
//
//            COSDictionary referenceDict = new COSDictionary();
//            referenceDict.setItem(COSName.TYPE, COSName.SIG_REF);
//            referenceDict.setItem(COSName.TRANSFORM_METHOD, COSName.DOCMDP);
//            referenceDict.setItem(COSName.DIGEST_METHOD, COSName.getPDFName("SHA1"));
//            referenceDict.setItem(COSName.TRANSFORM_PARAMS, transformParams);
//            referenceDict.setNeedToBeUpdated(true);
//            referenceArray.add(referenceDict);
//
//            signature.getCOSObject().setItem(COSName.getPDFName("Reference"), referenceArray);
//
//            // Catalog
//            COSDictionary catalogDict = document.getDocumentCatalog().getCOSObject();
//            COSDictionary permsDict = new COSDictionary();
//            catalogDict.setItem(COSName.PERMS, permsDict);
//            permsDict.setItem(COSName.DOCMDP, signature);
//            catalogDict.setNeedToBeUpdated(true);
//            permsDict.setNeedToBeUpdated(true);
//        }
//
//        final Predicate<PDField> shallBeLocked;
//        final COSArray fieldsLock = lockDict.getCOSArray(COSName.FIELDS);
//        final List<String> fieldNames = fieldsLock == null ? Collections.emptyList() :
//                fieldsLock.toList().stream().filter(c -> (c instanceof COSString)).map(s -> ((COSString) s).getString()).collect(Collectors.toList());
//        final COSName action = lockDict.getCOSName(COSName.getPDFName("Action"));
//        if (action.equals(COSName.getPDFName("Include"))) {
//            shallBeLocked = f -> fieldNames.contains(f.getFullyQualifiedName());
//        } else if (action.equals(COSName.getPDFName("Exclude"))) {
//            shallBeLocked = f -> !fieldNames.contains(f.getFullyQualifiedName());
//        } else if (action.equals(COSName.getPDFName("All"))) {
//            shallBeLocked = f -> true;
//        } else { // unknown action, lock nothing
//            shallBeLocked = f -> false;
//        }
//        lockFields(document.getDocumentCatalog().getAcroForm().getFields(), shallBeLocked);
//
//        signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
//        signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
//        signature.setName("blablabla");
//        signature.setLocation("blablabla");
//        signature.setReason("blablabla");
//        signature.setSignDate(Calendar.getInstance());
//
//
//        document.addSignature(signature);
//
////        AccessPermission ap = new AccessPermission();
////        //add what ever perms you need blah blah...
////        ap.setCanModify(false);
////        ap.setCanExtractContent(false);
////        ap.setCanPrint(false);
////        ap.setCanPrintDegraded(false);
////        ap.setReadOnly();
////
////        StandardProtectionPolicy spp = new StandardProtectionPolicy(UUID.randomUUID().toString(), "", ap);
////
////        document.protect(spp);
//
//
//        ExternalSigningSupport externalSigning =
//                document.saveIncrementalForExternalSigning(output);
//        // invoke external signature service
//        byte[] cmsSignature = signatureInterface.sign(externalSigning.getContent());
//        // set signature bytes received from the service
//        externalSigning.setSignature(cmsSignature);
//    }
//
//    public static byte[] signWithSeparatedHashing(InputStream content) throws IOException
//    {
//        try
//        {
//            // Digest generation step
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] digest = md.digest(IOUtils.toByteArray(content));
//
//            // Separate signature container creation step
//            List<Certificate> certList = Arrays.asList(cert);
//            JcaCertStore certs = new JcaCertStore(certList);
//
//            CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
//
//            Attribute attr = new Attribute(CMSAttributes.messageDigest,
//                    new DERSet((ASN1Encodable) new DEROctetString(digest)));
//
//            ASN1EncodableVector v = new ASN1EncodableVector();
//
//            v.add(attr);
//
//            SignerInfoGeneratorBuilder builder = new SignerInfoGeneratorBuilder(new BcDigestCalculatorProvider())
//                    .setSignedAttributeGenerator(new DefaultSignedAttributeTableGenerator(new AttributeTable(v)));
//
//            AlgorithmIdentifier sha256withRSA = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA");
//
//            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
//            InputStream in = new ByteArrayInputStream(cert[0].getEncoded());
//            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
//
////            gen.addSignerInfoGenerator(builder.build(
////                    new BcRSAContentSignerBuilder(sha256withRSA,
////                            new DefaultDigestAlgorithmIdentifierFinder().find(sha256withRSA))
////                            .build(PrivateKeyFactory.createKey(null)),
////                    new JcaX509CertificateHolder(cert)));
//
//            gen.addCertificates(certs);
//
//            CMSSignedData s = gen.generate(new CMSAbsentContent(), false);
//            return s.getEncoded();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            throw new IOException(e);
//        }
//    }
//
//
//    static boolean lockFields(List<PDField> fields, Predicate<PDField> shallBeLocked) {
//
//        boolean isUpdated = false;
//        if (fields != null) {
//            for (PDField field : fields) {
//                boolean isUpdatedField = false;
//                if (shallBeLocked.test(field)) {
//                    field.setFieldFlags(field.getFieldFlags() | 1);
//                    if (field instanceof PDTerminalField) {
//                        for (PDAnnotationWidget widget : ((PDTerminalField) field).getWidgets()) {
//                            widget.setLocked(true);
//                            widget.setPrinted(true);
//                        }
//                    }
//                    isUpdatedField = true;
//                }
//                if (field instanceof PDNonTerminalField) {
//                    if (lockFields(((PDNonTerminalField) field).getChildren(), shallBeLocked))
//                        isUpdatedField = true;
//                }
//                if (isUpdatedField) {
//                    field.getCOSObject().setNeedToBeUpdated(true);
//                    isUpdated = true;
//                }
//            }
//        }
//        return isUpdated;
//    }

}