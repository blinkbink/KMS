package com.digisign.kms.controller;

import com.digisign.kms.core.pdf.SignDoc;
import com.digisign.kms.externalapi.ActivityLog;
import com.digisign.kms.model.*;
import com.digisign.kms.model.signer.Initials;
import com.digisign.kms.model.signer.SignatureData;
import com.digisign.kms.model.signer.UserCertificate;
import com.digisign.kms.model.signer.UserSignature;
import com.digisign.kms.repository.*;
import com.digisign.kms.service.KeyService;
import com.digisign.kms.service.SealCertService;
import com.digisign.kms.util.Description;
import com.digisign.kms.util.FileUtil;
import com.digisign.kms.util.LogSystem;
import com.digisign.kms.util.encryption.AESEncryption;
import com.digisign.kms.util.encryption.RSAEncryption;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONArray;
import org.bouncycastle.util.encoders.Base64;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/DSKeyCore")
public class DocAccessController extends Description {

    @Autowired
    DocAccessRepository docAccessRepository;

    @Autowired
    KeyRepository keyRepository;

    @Autowired
    KeyService keyService;

    @Autowired
    MitraRepository mitraRepo;

    @Autowired
    MitraSigningConfigRepository mscRepo;

    @Autowired
    EmployeeSigningConfigRepository escRepo;

    @Autowired
    SealCertRepository sealCertRepository;

    @Autowired
    InitialsRepository initialsRepository;

    @Autowired
    PersonalSigningConfigRepository PSRepository;

    @Autowired
    SealCertService sealCertService;

    @Autowired
    SealRepository sealRepository;

    @Autowired
    SealDocAccessRepository sealDAR;

    @Autowired
    EeuserRepository eeuserRepo;

    @PostMapping(value = "/sealTest.html", produces = {"multipart/form-data"})
    public ResponseEntity<?> sealTest(@RequestPart String jsonfield) throws Exception {
        // TODO Auto-generated method stub
        String id_doc=UUID.randomUUID().toString();
        byte[] bytesArray;
        String SYNC="SYN";
        long idxStatic=0;
        JSONObject jsonKey=new JSONObject();
        JSONObject jsonFile = new JSONObject();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        String modeEncrypt ="ECB";
        String plainData=null;
        Date timestamp;
        String tempDirPrefix="/tmp/fileTmp/";
        long idx;
        boolean canSign=true;

        synchronized (SYNC) {
            idxStatic++;
            timestamp=new Date();
            idxStatic=1;
            idx=idxStatic;
        }

        JSONObject response=new JSONObject();
        JSONObject jsonData=new JSONObject();

        try {

            jsonKey = new JSONObject(jsonfield);

            if (!jsonKey.has("JSONFile")) {
                jsonFile.put("error", "JSONFile is required");
            } else {
                if (jsonKey.has("encrypt-mode")) {
                    modeEncrypt = jsonKey.getString("encrypt-mode");
                }

                if (modeEncrypt.equals("CBC")) {
                    plainData = AESEncryption.decryptDoc(jsonKey.getString("JSONFile"));
                } else {
                    plainData = RSAEncryption.decryptWithPriv(jsonKey.getString("JSONFile"));
                }
            }

            jsonData = new JSONObject(plainData);

            Long seal_id = jsonData.getLong("seal_id");
            UserSignature userSignData;
            SignatureData signatureData=new SignatureData();
            UserCertificate uc;
            String org = null;

            String CheckSign;
            Date expiredDate = null;

            Eeuser user = eeuserRepo.getUserSeal(jsonData.getLong("user"));

            //query userdata untuk data sertifikat dan seal dari id seal
            Long userMitra = user.getMitraBean().getId();

            String userNama = user.getUserdataBean().getNama();
            String userEmail = user.getNick();
            Long iduser = user.getId();
            String userProvinsi = user.getMitraBean().getProvinsi();
            String userKota = user.getUserdataBean().getKota();
            String userLevel = user.getUserdataBean().getLevel();

            Optional<Mitra> mitraname = mitraRepo.findById(userMitra);
            LogSystem.info("MITRA NAME : " + mitraname.get().getName());

            uc=new UserCertificate(userNama, userEmail, iduser, userProvinsi,userKota, mitraname.get().getName(), "",userLevel);
            uc.setMitraID(userMitra);

            userSignData = new UserSignature(uc);

            userSignData.setInFile("/opt/sealTestFile/Test.pdf"); // file source
            userSignData.setSigpage(1); // halaman untuk tanda tangan
            userSignData.setSigPosLLX("212"); //posisi titik bawah kiri X
            userSignData.setSigPosLLY("651"); //posisi titik bawah kiri Y
            userSignData.setSigPosURX("336");//posisi titik atas kanan X
            userSignData.setSigPosURY("714");//posisi titik atas kanan Y
            userSignData.setType("seal");
            userSignData.setVisible(true);
            userSignData.setLevel("C5");
            userSignData.setDoc_id(id_doc);

            LogSystem.info("SEAL");
            userSignData.getCertificate().setLevelCert("C5");
            userSignData.getCertificate().setEmail("MT"+userMitra);

            //check for get user seal qr text
            LogSystem.info(userSignData.getCertificate().getIdUser().toString());
            Seal seal = sealRepository.getUserSealTest(userMitra, user.getId(), seal_id);

            if(seal != null)
            {
                userSignData.setSignID(seal_id.toString());
                LogSystem.info("ISI SEAL " + seal.getName());
                userSignData.setWithQR(seal.getSealWithQr());
                LogSystem.info("With QR : "+userSignData.isWithQR());
                if(userSignData.isWithQR())
                {
                    if(seal.getImageQr() != null)
                    {
                        org = seal.getImageQr();
                        userSignData.setPathLogo(org);
                    }
                    else
                    {
                        org = user.getUserdataBean().getITtd();
                    }
                }
                else
                {
                    org = seal.getImage();
                }

                userSignData.setQrText("Test seal");

                if(seal.getUserSeals().get(0).getSealDocAccesses().size() > 0)
                {
                    if(seal.getUserSeals().get(0).getSealDocAccesses().get(seal.getUserSeals().get(0).getSealDocAccesses().size()-1).getQrText() != null)
                    {
                        userSignData.setQrText(seal.getUserSeals().get(0).getSealDocAccesses().get(seal.getUserSeals().get(0).getSealDocAccesses().size()-1).getQrText());
                    }
                }

                userSignData.setImgFile(org);
            }
            else
            {
                LogSystem.info("Seal null");
                jsonFile.put("result", "14");
                jsonFile.put("info", "Seal can't be used");
                canSign=false;
                LogSystem.info("Seal can't be used");
            }

            LogSystem.info("Query for seal");
            LogSystem.info("LEVEL " + user.getUserdataBean().getLevel());
            LogSystem.info("LEVEL SEAL " + userSignData.getLevel());
            LogSystem.info("EEUSER " + user.getId());
            LogSystem.info("MITRA " + userMitra);

            List<KeyV3> checkCertSeal;

            checkCertSeal = sealCertRepository.checkSealCertificate(userMitra, userSignData.getLevel());
            if (checkCertSeal.size() < 1)
            {
                LogSystem.info("Seal certificate null");
                jsonFile.put("result", "14");
                jsonFile.put("info", "Seal can't be used");
                canSign=false;
                LogSystem.info("Seal can't be used");
            }
            CheckSign = sealCertService.checkSign(checkCertSeal.get(0));
            expiredDate = checkCertSeal.get(0).getWaktuExp();

            //Check user sertificate
            LogSystem.info("CheckCert result " + CheckSign);

            if(CheckSign.equals("05"))
            {
                jsonFile.put("result", "05");
                jsonFile.put("info", "user don't have certificate");
                LogSystem.info("User don't have certificate");
                canSign = false;
            }
            else if(CheckSign.equals("06"))
            {
                jsonFile.put("result", "06");
                jsonFile.put("info", "certificate is expired");
                jsonFile.put("expired-time",sdf.format(expiredDate));
                LogSystem.info("User certificate is expired");
                canSign = false;
            }
            else if(CheckSign.equals("07"))
            {
                jsonFile.put("result", "07");
                jsonFile.put("info", "certificate is revoked");
                LogSystem.info("User certificate is revoke");
                canSign = false;
            }

            if(userSignData.getType().equals("seal")) {
                LogSystem.info("Seal: " + userSignData.getType());
                signatureData.addSignature(userSignData);
            }

            if (canSign) {
                LogSystem.info("In File " + userSignData.getInFile());
                FileUtil fta =  new FileUtil();
                byte[] dataPdfSrc=fta.FiletoByte(new File(userSignData.getInFile()));

                LogSystem.info("Samba Process");
                Samba samba;
                LogSystem.info("Connect to Samba");
                samba=new Samba();

                LogSystem.info("File TTD " + userSignData.getImgFile());
                byte[] dataImgSrc=samba.openfile(userSignData.getImgFile());
                samba.close();
                LogSystem.info("Closed connection to Samba");

                LogSystem.info("Initialize temp folder");
                String UUID=java.util.UUID.randomUUID().toString();
                String tempDir=tempDirPrefix+sdf.format(timestamp)+"/"+idx+"-"+UUID+"-"+timestamp.getTime();

                String fileNameTmp=tempDir+"/"+ UUID +"-"+timestamp.getTime()+".pdf";
                String fileTtdTmp=tempDir+"/"+ UUID +"-"+timestamp.getTime()+".png";
                String fileDestNameTmp=tempDir+"/"+ UUID +"-"+timestamp.getTime()+"-SIGNED.pdf";

                LogSystem.info("Finish initialize temp folder");

                File directory = new File(tempDir);
                if (!directory.exists()){
                    directory.mkdirs();
                }

                LogSystem.info("Writing byte file to temp folder");
                FileUtils.writeByteArrayToFile(new File(fileNameTmp), dataPdfSrc);
                FileUtils.writeByteArrayToFile(new File(fileTtdTmp), dataImgSrc);
                LogSystem.info("Finish write temp file");

                SignDoc dSigner=new SignDoc();
                Date date=null;

                LogSystem.info("Check usersign is with QR");
                UserSignature userSign = signatureData.getSignatures().get(0);
                if(userSign.isWithQR())
                {
                    LogSystem.info("True");
                    String fileLogoTmp=tempDir+"/"+timestamp.getTime()+"-LOGOQR.png";
                    String fileQRTmp=tempDir+"/"+timestamp.getTime()+"-QRNEW.png";

                    userSign.setqRPathTemp(fileQRTmp);

                    if(userSign.getPathLogo()!=null) {
                        byte[] dataLogoSrc=samba.openfile(userSign.getPathLogo());
                        samba.close();
                        FileUtils.writeByteArrayToFile(new File(fileLogoTmp), dataLogoSrc);
                        userSign.setPathLogo(fileLogoTmp);
                    }
                    LogSystem.info("Signature with QR Generation " + timestamp);
                }

                userSign.setImgFile(fileTtdTmp);
                userSign.setOutFile(fileDestNameTmp);
                userSign.setInFile(fileNameTmp);

                LogSystem.info("Send data for Decrypt private key, OCSP, CRL and Seal process");

                date = dSigner.signingDoc(timestamp, userSign, signatureData.getSignatures(), tempDir + "/", null, checkCertSeal, org, jsonFile);

                LogSystem.info("Finish send data for Decrypt, OCSP, CRL and seal process");

                LogSystem.info("With response " + date);

                if(date!=null) {
                    File file = new File(fileDestNameTmp);
                    bytesArray = new byte[(int) file.length()];

                    FileInputStream fileInputStream = null;

                    //read file into bytes[]
                    fileInputStream = new FileInputStream(file);
                    fileInputStream.read(bytesArray);
                    jsonFile.put("file", Base64.toBase64String(bytesArray));
                    String id="sealdoctest"+timestamp.getTime();

                    String fileName = id+".pdf";
                    String pathOut= "/file2/data-DS/MitraFile/"+userMitra+"/seal/"+fileName;

                    boolean saveToFS=false;
                    try {
                        LogSystem.info("Pathout : " + pathOut);
                        saveToFS=samba.write(bytesArray, pathOut);
                        samba.close();
                        if(saveToFS) {

                            JSONArray tsArray = dSigner.getArrayTimeStamp();
                            if (tsArray != null && tsArray.length() > 1) {
                                jsonFile.put("listTimestamp", tsArray);
                            }
                            else
                            {
                                jsonFile.put("date", date.getTime());
                            }
                        }
                        //Update example_path table seal
//                        try {
//                            LogSystem.info("Update example_path table seal to " + pathOut);
//                            seal.setExamplePath(pathOut);
//                            sealRepository.save(seal);
//                            LogSystem.info("Success update");
//
//                            jsonFile.put("result", "00");
//                        }catch(Exception e)
//                        {
//                            e.printStackTrace();
//                            LogSystem.error(e.toString());
                            jsonFile.put("result", "00");
//                            jsonFile.put("error", e.toString());
//                        }

                        try
                        {
                            LogSystem.info("Send log activity");
                            Log log=new Log();
                            log.setActivity("signing");
                            log.setResult("success");
                            log.setInformation(fileName+" berhasil ditandatangani (TEST SEAL)");
                            log.setUser(jsonData.getLong("user"));
                            log.setSigning_time(date);

                            new ActivityLog(timestamp).sendPost(log);

                            LogSystem.info("Finish send log activity");

                        }catch (Exception c)
                        {
                            c.printStackTrace();
                            LogSystem.info("Failed send request to log activity");
                        }
                    }catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        jsonFile.put("result", "05");
                        jsonFile.put("error", e.toString());
                        LogSystem.error("Error : " + ExceptionUtils.getStackTrace(e) + " " + timestamp);
                    }finally {
                        fileInputStream.close();
                        samba.close();
                    }
                }else {
                    jsonFile.put("result", "05");
                    jsonFile.put("error", "Seal test failed");
                }

                //clear all temp data
                FileUtils.deleteDirectory(new File(tempDir));

                response.put("JSONFile", AESEncryption.encryptDoc(jsonFile.toString()));


            }

        }catch(Exception e)
        {
            e.printStackTrace();
            jsonFile.put("result", "05");
            jsonFile.put("error", e.toString());
            LogSystem.error(e.toString());
        }

//        LogSystem.response(jsonFile.toString());
//        response.put("JSONFile", AESEncryption.encryptDoc(jsonResp.toString()));
        if(jsonKey.has("plain"))
        {
            if(jsonKey.getBoolean("plain"))
            {
                response.put("JSONFile", jsonFile.toString());
            }
            else
            {
                response.put("JSONFile", AESEncryption.encryptDoc(jsonFile.toString()));
            }
        }
        else
        {
            response.put("JSONFile", AESEncryption.encryptDoc(jsonFile.toString()));
        }

        LogSystem.response("Result " + jsonFile.getString("result"));

        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }

    @PostMapping(value = "/docSign.html", produces = {"multipart/form-data"})
    public ResponseEntity<?> doSign(@RequestPart String jsonfield) throws Exception {
        long start = System.currentTimeMillis();
//        LogSystem.request("Encrypted " + jsonfield);

        Date timestamp=new Date();
        String uuid = UUID. randomUUID().toString().replace("-", "");

        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");

        JSONObject response = new JSONObject();
        JSONObject jsonFile = new JSONObject();
        JSONObject jsonKey = new JSONObject();
        JSONObject jsonData = new JSONObject();
        String modeEncrypt ="ECB";
        String plainData;
        boolean canSign = true;
        boolean descOnly=false;
        boolean signWQR=false;
        String pathLogo;
        String tempDirPrefix="/tmp/fileTmp/";
        String location="";
//        String tempDirPrefix="/mnt/c/SNI/Digisign/mitraapi/kms/src/test/resources/tmp/";
        byte[] bytesArray = null;

        try {
            //Set log id in response
            jsonFile.put("log", LogSystem.getLogId());

            jsonKey = new JSONObject(jsonfield);

            if(!jsonKey.has("JSONFile"))
            {
                jsonFile.put("error", "JSONFile is required");
                canSign = false;
            }
            else
            {
                if(jsonKey.has("encrypt-mode"))
                {
                    modeEncrypt = jsonKey.getString("encrypt-mode");
                }

                if(modeEncrypt.equals("CBC")){
                    plainData= AESEncryption.decryptDoc(jsonKey.getString("JSONFile"));
                }else {
                    plainData= RSAEncryption.decryptWithPriv(jsonKey.getString("JSONFile"));
                }

                LogSystem.request("Decrypted " + plainData);
                jsonData=new JSONObject(plainData);
//                LogSystem.info(jsonData.toString());
                String pathOut= jsonData.getString("outfile");

                if(jsonData.has("document_access"))
                {
                    jsonFile.put("document_access", jsonData.get("document_access"));
                }

                if(!jsonData.has("user") || !jsonData.has("document_id"))
                {
                    jsonFile.put("info", "incomplete data");
                    canSign = false;
                }

                DocAccess SignDA;
                DocAccess SealDA;
//                List<DocAccess> dataSign = docAccessRepository.getUserSign(jsonData.getLong("user"), jsonData.getLong("document_id"));
//                List<DocAccess> dataSignSeal = docAccessRepository.getUserSignSeal(jsonData.getLong("user"), jsonData.getLong("document_id"));
                List<DocAccess> dataSign = new ArrayList<DocAccess>();;
                List<DocAccess> dataSignSeal = new ArrayList<DocAccess>();;

                JSONArray docAccessArray = jsonData.getJSONArray("document_access");

                LogSystem.info("Doc Access Array " + docAccessArray);
                for (int j = 0 ; j < docAccessArray.length() ; j++)
                {
                    SignDA = docAccessRepository.getUserSignDA(jsonData.getLong("user"), Long.valueOf(docAccessArray.get(j).toString()));
                    SealDA = docAccessRepository.getUserSealDA(jsonData.getLong("user"), Long.valueOf(docAccessArray.get(j).toString()));

                    if(SignDA != null)
                    {
                        LogSystem.info("Doc access found sign/initials " + docAccessArray.get(j));
                        dataSign.add(SignDA);
                    }
                    else
                    {
                        LogSystem.info("Doc access not found sign/initials " + docAccessArray.get(j));
                    }

                    if(SealDA != null)
                    {
                        LogSystem.info("Doc access found seal " + docAccessArray.get(j));
                        dataSignSeal.add(SealDA);
                    }
                    else
                    {
                        LogSystem.info("Doc access not found seal " + docAccessArray.get(j));
                    }
                }

                if(dataSign.size() < 1 && dataSignSeal.size() < 1)
                {
                    jsonFile.put("result", "05");
                    jsonFile.put("info", "Null doc access");
                    LogSystem.info("Null document access");
                    response.put("JSONFile", AESEncryption.encryptDoc(jsonFile.toString()));

                    LogSystem.response("Result " + jsonFile.toString());

                    return ResponseEntity
                            .status(200)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response.toString());
                }

                List<KeyV3> checkCert;
                List<KeyV3> checkCertSeal;
                List<KeyV3> checkCertACT = null;
                List<KeyV3> checkCertSealACT=null;

                //Process Signing sign and initials
                if(dataSign.size() > 0)
                {
                    UserSignature userSignData = null;
                    SignatureData signatureData=new SignatureData();
                    UserCertificate uc;
                    String org = null;
                    String doc_name="";
                    String doc_id="";

                    for(int i = 0 ; i < dataSign.size() ; i++) {
                        doc_id=dataSign.get(i).getDocumentBean().getId().toString();
                        doc_name=dataSign.get(i).getDocumentBean().getFileName();
                        LogSystem.info("SIZEE : " + dataSign.size());

                        //Check document level with user signer level
                        String levelSender = dataSign.get(i).getDocumentBean().getEeuserBean().getUserdataBean().getLevel();
                        String levelSigner = dataSign.get(i).getEeuserBean().getUserdataBean().getLevel();

                        LogSystem.info("Sender level " + levelSender);
                        LogSystem.info("Signer level " + levelSigner);

                        int levelmitra = Integer.parseInt(levelSender.substring(1));
                        int leveluser = Integer.parseInt(levelSigner.substring(1));

                        if (levelmitra > 2 && leveluser < 3) {
                            jsonFile.put("result", "13");
                            jsonFile.put("info", "This user is not permitted to sign this document");
                            LogSystem.info("User sign " + dataSign.get(i).getEmail());
                            LogSystem.info("Info " + "This user is not permitted to sign this document");
                            canSign = false;
                        }

                        String CheckSign;
                        Date expiredDate = null;
                        Long userMitra = null;
                        Long senderMitra = null;
//                        Boolean personal_signature_with_qr = false;

                        if (dataSign.get(i).getDocumentBean().getEeuserBean().getMitraBean() != null)
                        {
                            //cek mitra user yang kirim dokumen
                            senderMitra = dataSign.get(i).getDocumentBean().getEeuserBean().getMitraBean().getId();
                        }

                        if(dataSign.get(i).getEeuserBean().getUserdataBean().getMitraBean() != null) {
                            //cek user yang tandatangan
                            userMitra = dataSign.get(i).getEeuserBean().getUserdataBean().getMitraBean().getId();
                        }

//                        if(dataSign.get(i).getEeuserBean().getSignature_with_qr())
//                        {
//                            personal_signature_with_qr = true;
//                        }

                        Long eeuserMitra = null;
                        if(dataSign.get(i).getEeuserBean().getMitraBean() != null) {
                            //Cek user karyawan yang tandatangan
                            location = dataSign.get(i).getEeuserBean().getMitraBean().getName();
                            eeuserMitra = dataSign.get(i).getEeuserBean().getMitraBean().getId();
                        }
                        String userNama = dataSign.get(i).getEeuserBean().getUserdataBean().getNama();
                        String userEmail = dataSign.get(i).getEeuserBean().getNick();
                        Long iduser = dataSign.get(i).getEeuserBean().getId();
                        String userProvinsi = dataSign.get(i).getEeuserBean().getUserdataBean().getPropinsi();
                        String userKota = dataSign.get(i).getEeuserBean().getUserdataBean().getKota();
                        String userLevel = dataSign.get(i).getEeuserBean().getUserdataBean().getLevel();

//                        org = userMitra==null?"Personal":userMitra.toString();

                        if(userMitra==null) {
                            LogSystem.info("User mitra null");
                            uc=new UserCertificate(userNama, userEmail, iduser, userProvinsi,userKota, "Personal", "",userLevel);
                        }else {
                            LogSystem.info("User mitra not null");
                            Optional<Mitra> mitraname = mitraRepo.findById(userMitra);
                            LogSystem.info("MITRA NAME : " + mitraname.get().getName());
//                            org=null;
                            if(Optional.ofNullable(mitraname).isPresent()){
//                                org=mitraname.get().getName();
                            }
                            uc=new UserCertificate(userNama, userEmail, iduser, userProvinsi,userKota, mitraname.get().getName(), "",userLevel);
                            uc.setMitraID(userMitra);
                        }

                        userSignData = new UserSignature(uc);

                        userSignData.setImgFile(dataSign.get(i).getEeuserBean().getUserdataBean().getITtd()); // file tanda tangan
                        userSignData.setInFile(dataSign.get(i).getDocumentBean().getPath()+dataSign.get(i).getDocumentBean().getSigndoc()); // file source
                        userSignData.setSigpage(dataSign.get(i).getPage()); // halaman untuk tanda tangan
                        userSignData.setSigPosLLX(dataSign.get(i).getLx()); //posisi titik bawah kiri X
                        userSignData.setSigPosLLY(dataSign.get(i).getLy()); //posisi titik bawah kiri Y
                        userSignData.setSigPosURX(dataSign.get(i).getRx());//posisi titik atas kanan X
                        userSignData.setSigPosURY(dataSign.get(i).getRy());//posisi titik atas kanan Y
                        userSignData.setType(dataSign.get(i).getType());
                        userSignData.setVisible(dataSign.get(i).getVisible());
                        userSignData.setLevel(dataSign.get(i).getEeuserBean().getUserdataBean().getLevel());
                        userSignData.setDoc_id(dataSign.get(i).getDocumentBean().getId().toString());
                        userSignData.setSignID(dataSign.get(i).getId().toString());
                        userSignData.setLocation(location);

                        LogSystem.info("Query for signature");
                        LogSystem.info("LEVEL " + dataSign.get(i).getEeuserBean().getUserdataBean().getLevel());
                        LogSystem.info("EEUSER " + dataSign.get(i).getEeuserBean().getId());
                        checkCert = keyRepository.checkSignatureCertificate(dataSign.get(i).getEeuserBean().getId(), dataSign.get(i).getEeuserBean().getUserdataBean().getLevel());
                        checkCertACT = keyRepository.checkACTSignatureCertificate(dataSign.get(i).getEeuserBean().getId(), dataSign.get(i).getEeuserBean().getUserdataBean().getLevel());
                        if(checkCert.size()>0)
                        {
                            LogSystem.info("Data user di key ada");
                            CheckSign = keyService.checkSign(checkCert.get(0));
                            expiredDate = checkCert.get(0).getWaktuExp();
                        }
                        else {
                            LogSystem.info("Data user di key tidak ada");
                            CheckSign="05";
                        }

                        //Check user sertificate
                        LogSystem.info("CheckCert result " + CheckSign);

                        if(CheckSign.equals("05"))
                        {
                            jsonFile.put("result", "05");
                            jsonFile.put("info", "user don't have certificate");
                            LogSystem.info("User don't have certificate");
                            canSign = false;
                            break;
                        }
                        else if(CheckSign.equals("06"))
                        {
                            jsonFile.put("result", "06");
                            jsonFile.put("info", "certificate is expired");
                            jsonFile.put("expired-time",sdf.format(expiredDate));
                            LogSystem.info("User certificate is expired");
                            canSign = false;
                            break;
                        }
                        else if(CheckSign.equals("07"))
                        {
                            jsonFile.put("result", "07");
                            jsonFile.put("info", "certificate is revoked");
                            LogSystem.info("User certificate is revoke");
                            canSign = false;
                            break;
                        }

                        LogSystem.info("Check !initials");
                        if(!dataSign.get(i).getType().equals("initials"))
                        {
                            if(eeuserMitra!=null)
                            {
                                LogSystem.info("EEUSER MITRA : " + eeuserMitra);
                                MitraSigningConfig mitraSigningConfig = mscRepo.getByMitra(eeuserMitra);
                                if(mitraSigningConfig != null)
                                {
                                    descOnly=mitraSigningConfig.getSignatureDescOnly();
                                }

                                LogSystem.info("User signing mitra " + eeuserMitra);
                                LogSystem.info("Sender doc mitra " + senderMitra);

                                if (Objects.equals(eeuserMitra, senderMitra)) {
                                    userSignData.setDescOnly(descOnly);
                                }
                            }
//                            if(!userSignData.getDescOnly() && (Objects.equals(userMitra, eeuserMitra)))
                            if(!userSignData.getDescOnly())
                            {

                                PersonalSigningConfig ps = PSRepository.getByUser(dataSign.get(i).getEeuserBean().getId());
                                LogSystem.info("eeuser " + dataSign.get(i).getEeuserBean().getId());
                                LogSystem.info("Personal Signing Config " + ps);
                                if(eeuserMitra == null && ps != null)
                                {
                                    LogSystem.info("Personal Signature With QR");
//                                    signWQR=true;
//                                    pathLogo="/file2/data-DS/logoQR/digisign.png";
                                    userSignData.setPathLogo(ps.getQrLogoImg());
                                    userSignData.setWithQR(ps.getSignatureWithQr());

                                    //setting request allobank
                                    if(userSignData.isWithQR())
                                    {
                                        userSignData.setPosition(1);
                                    }

                                    userSignData.setWithSignature(false);
                                    userSignData.setQrOnly(false);
                                }
                                else {
                                    EmployeeSigningConfig escConfig = null;
                                    if (eeuserMitra!=null)
                                    {
                                        escConfig = escRepo.getByMitra(eeuserMitra);
                                    }

                                    if(escConfig != null)
                                    {
                                        LogSystem.info("Employee signing config not null");
                                        signWQR=escConfig.getSignatureWithQr();
                                        pathLogo=escConfig.getQrLogoImg();
                                        userSignData.setPathLogo(pathLogo);
                                        userSignData.setWithQR(signWQR);

                                        //setting request allobank
                                        if(userSignData.isWithQR())
                                        {
                                            userSignData.setPosition(escConfig.getPosition());
                                        }

                                        userSignData.setWithSignature(escConfig.getSignature());
                                        userSignData.setQrOnly(escConfig.getQrOnly());
                                    }
                                }

                            }
                        }

                        LogSystem.info("GET TYIPE"+dataSign.get(i).getType());
                        if(dataSign.get(i).getType().equals("initials")) {
                            LogSystem.info("Initials : "+dataSign.get(i).getType());
                            signatureData.addInitial(userSignData);
                        }else {
                            LogSystem.info("Sign : "+dataSign.get(i).getType());
                            signatureData.addSignature(userSignData);
                        }
                    }

                    if (canSign) {
                        LogSystem.info("Samba Process");
                        Samba samba;
                        LogSystem.info("Connect to Samba");
                        samba=new Samba();
                        byte[] dataPdfSrc=samba.openfile(userSignData.getInFile());
                        samba.close();
                        byte[] dataImgSrc=samba.openfile(userSignData.getImgFile());
                        samba.close();
                        LogSystem.info("Closed connection to Samba");

                        LogSystem.info("Initialize temp folder");
                        String tempDir=tempDirPrefix+ uuid +"-"+timestamp.getTime();

                        String fileNameTmp=tempDir+"/"+timestamp.getTime()+".pdf";
                        String fileTtdTmp=tempDir+"/"+timestamp.getTime()+".png";
                        String fileDestNameTmp=tempDir+"/"+timestamp.getTime()+"-SIGNED.pdf";

                        String fileDestNameTmpInit=tempDir+"/"+timestamp.getTime()+"init-SIGNED.pdf";

                        LogSystem.info("Finish initialize temp folder");

                        File directory = new File(tempDir);
                        if (!directory.exists()){
                            directory.mkdirs();
                        }

                        LogSystem.info("Writing byte file to temp folder");
                        FileUtils.writeByteArrayToFile(new File(fileNameTmp), dataPdfSrc);
                        FileUtils.writeByteArrayToFile(new File(fileTtdTmp), dataImgSrc);
                        LogSystem.info("Finish write temp file");

                        SignDoc dSigner=new SignDoc();
                        Date date=null;

                        boolean initialSign = false;
                        if(signatureData.getInitials().size()>0)
                        {

                            SignatureData initialsData=new SignatureData();

                            UserSignature userSignInit=signatureData.getInitials().get(0);
                            userSignInit.setImgFile(fileTtdTmp);
                            userSignInit.setInFile(fileNameTmp);
                            userSignInit.setOutFile(fileDestNameTmpInit);

                            initialSign=true;

                            LogSystem.info("init >> " + userSignInit.getType() + userSignInit.getSignID());

                            List<Initials> listInitials = initialsRepository.getInitials(Long.valueOf(userSignInit.getSignID()));
                            LogSystem.info("initials " + listInitials.size());

                            for (int i = 0 ; i < listInitials.size() ; i++)
                            {
                                UserSignature userInitialsData = new UserSignature();
                                LogSystem.info("ID INIT " + listInitials.get(i).getId().toString());
                                userInitialsData.setSignID(listInitials.get(i).getId().toString());
                                userInitialsData.setVisible(userSignInit.isVisible());
                                userInitialsData.setType(userSignInit.getType());
                                userInitialsData.setSigpage(listInitials.get(i).getPage());
                                userInitialsData.setSigPosLLX(listInitials.get(i).getLx());
                                userInitialsData.setSigPosLLY(listInitials.get(i).getLy());
                                userInitialsData.setSigPosURX(listInitials.get(i).getRx());
                                userInitialsData.setSigPosURY(listInitials.get(i).getRy());

                                initialsData.addInitial(userInitialsData);
                            }

                            for (int b = 0 ; b < initialsData.getInitials().size() ; b++)
                            {
                                LogSystem.info(initialsData.getInitials().get(b).getSignID());
                            }

                            LogSystem.info("Send data for Decrypt private key, OCSP, CRL and signing process to document with initials");
                            date=dSigner.signingDoc(timestamp,userSignInit,initialsData.getInitials(),tempDir+"/", checkCertACT, checkCertSealACT, org, jsonFile);
                            LogSystem.info("Finish send data for Decrypt, OCSP, CRL and signing process to document with initials");
                        }

                        if(signatureData.getSignatures().size()>0 && (!initialSign || (initialSign && date!=null)))
                        {
                            LogSystem.info("Check usersign is with QR");
                            UserSignature userSign = signatureData.getSignatures().get(0);
                            String fileSignPersonal=tempDir+"/"+timestamp.getTime()+"-SIGNNEW.png";
                            if(userSign.isWithQR())
                            {
                                LogSystem.info("True");
                                String fileLogoTmp=tempDir+"/"+timestamp.getTime()+"-LOGOQR.png";
                                String fileQRTmp=tempDir+"/"+timestamp.getTime()+"-QRNEW.png";

                                userSign.setqRPathTemp(fileQRTmp);

                                if(userSign.getPathLogo()!=null) {
                                    byte[] dataLogoSrc=samba.openfile(userSign.getPathLogo());
                                    samba.close();
                                    FileUtils.writeByteArrayToFile(new File(fileLogoTmp), dataLogoSrc);
                                    userSign.setPathLogo(fileLogoTmp);

                                }
                                LogSystem.info("Signature with QR Generation " + timestamp);
                            }
                            userSign.setqRPathTemp(fileSignPersonal);
                            userSign.setImgFile(fileTtdTmp);
                            userSign.setOutFile(fileDestNameTmp);
                            userSign.setInFile(fileNameTmp);

                            date = null;
                            LogSystem.info("Send data for Decrypt private key, OCSP, CRL and signing process to document with signature");

                            date = dSigner.signingDoc(timestamp, userSign, signatureData.getSignatures(), tempDir + "/", checkCertACT, checkCertSealACT, org, jsonFile);

                            LogSystem.info("Finish send data for Decrypt, OCSP, CRL and signing process to document with signature");
                        }else if(initialSign) {
                            fileDestNameTmp=fileDestNameTmpInit;
                        }

                        LogSystem.info("With response " + date);

                        if(date!=null) {
                            File file = new File(fileDestNameTmp);
                            bytesArray = new byte[(int) file.length()];

                            FileInputStream fileInputStream = null;

                            //read file into bytes[]
                            fileInputStream = new FileInputStream(file);
                            fileInputStream.read(bytesArray);

                            boolean saveToFS=false;
                            try {
                                LogSystem.info("Pathout : " + pathOut);
                                saveToFS=samba.write(bytesArray, pathOut);
                                samba.close();
                                if(saveToFS) {
                                    jsonFile.put("date", date.getTime());
                                    JSONArray tsArray=dSigner.getArrayTimeStamp();
                                    if(tsArray!=null && tsArray.length()>1) {
                                        jsonFile.put("listTimestamp", tsArray);
                                    }
                                    jsonFile.put("result", "00");
                                }

                                try
                                {
                                    LogSystem.info("Send log activity");
                                    Log log=new Log();
                                    log.setActivity("signing");
                                    log.setResult("success");
                                    log.setInformation(doc_name+" berhasil ditandatangani");
                                    log.setUser( jsonData.getLong("user"));
                                    log.setSigning_time(date);
                                    log.setDocument_id(new Long(doc_id));

                                    new ActivityLog(timestamp).sendPost(log);

                                    LogSystem.info("Finish send log activity");

                                }catch (Exception c)
                                {
                                    c.printStackTrace();
                                    LogSystem.info("Failed send request to log activity");
                                }
                            }catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                                jsonFile.put("result", "05");
                                jsonFile.put("error", e.toString());
                                LogSystem.error("Error : " + ExceptionUtils.getStackTrace(e) + " " + timestamp);
                                response.put("error", e.getCause());
                            }finally {
                                fileInputStream.close();
                                samba.close();
                            }
                        }else {
                            jsonFile.put("result", "05");
                            canSign=false;
                        }

                        //clear all temp data
//                        FileUtils.deleteDirectory(new File(tempDir));

                        response.put("JSONFile", AESEncryption.encryptDoc(jsonFile.toString()));

                        LogSystem.response(response.toString());
                    }
//                    else
//                    {
//                        jsonFile.put("result", "91");
//                        jsonFile.put("info", "This user is not permitted to sign this document");
//                    }
                }
                //Process Seal
                else
                {
                    String doc_id="";
                    String doc_name="";
                    LogSystem.info("SIZEE SEAL: " + dataSignSeal.size());
                    UserSignature userSignData = null;
                    SignatureData signatureData=new SignatureData();
                    UserCertificate uc;
                    String org = null;

                    for(int i = 0 ; i < dataSignSeal.size() ; i++)
                    {
                        doc_id=dataSignSeal.get(i).getDocumentBean().getId().toString();
                        doc_name=dataSignSeal.get(i).getDocumentBean().getFileName();
                        //Check document level with user signer level

                        String levelSender = dataSignSeal.get(i).getDocumentBean().getEeuserBean().getUserdataBean().getLevel();
                        String levelSigner = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel();

                        LogSystem.info("Sender level " + levelSender);
                        LogSystem.info("Signer level " + levelSigner);

                        int levelmitra=Integer.parseInt(levelSender.substring(1));
                        int leveluser=Integer.parseInt(levelSigner.substring(1));

                        if(levelmitra>2 && leveluser<3)
                        {
                            jsonFile.put("result", "13");
                            jsonFile.put("info", "This user is not permitted to sign this document");
                            LogSystem.info("User sign " + dataSignSeal.get(i).getEmail());
                            LogSystem.info("Info " + "This user is not permitted to sign this document");
                            canSign = false;
                            break;
                        }


                        String CheckSign;
                        Date expiredDate = null;

                        Long userMitra = dataSignSeal.get(i).getEeuserBean().getMitraBean().getId();

                        Long eeuserMitra = null;
                        if(dataSignSeal.get(i).getEeuserBean().getMitraBean() != null) {
                            eeuserMitra = dataSignSeal.get(i).getEeuserBean().getMitraBean().getId();
                        }
                        else
                        {
                            eeuserMitra = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getMitraBean().getId();
                        }

                        String userNama = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getNama();
                        String userEmail = dataSignSeal.get(i).getEeuserBean().getNick();
                        Long iduser = dataSignSeal.get(i).getEeuserBean().getId();
                        String userProvinsi = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getPropinsi();
                        String userKota = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getKota();
                        String userLevel = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel();

//                        org = userMitra==null?"Personal":userMitra.toString();

                        if(userMitra==null) {
                            LogSystem.info("User mitra null");
                            uc=new UserCertificate(userNama, userEmail, iduser, userProvinsi,userKota, "Personal", "",userLevel);
                        }else {
                            LogSystem.info("User mitra not null");
                            Optional<Mitra> mitraname = mitraRepo.findById(userMitra);
                            LogSystem.info("MITRA NAME : " + mitraname.get().getName());
//                            org=null;
                            if(Optional.ofNullable(mitraname).isPresent()){
//                                org=mitraname.get().getName();
                            }
                            uc=new UserCertificate(userNama, userEmail, iduser, userProvinsi,userKota, mitraname.get().getName(), "",userLevel);
                            uc.setMitraID(userMitra);
                        }

                        userSignData = new UserSignature(uc);

                        userSignData.setImgFile(dataSignSeal.get(i).getEeuserBean().getUserdataBean().getITtd()); // file tanda tangan
                        userSignData.setInFile(dataSignSeal.get(i).getDocumentBean().getPath()+dataSignSeal.get(i).getDocumentBean().getSigndoc()); // file source
                        userSignData.setSigpage(dataSignSeal.get(i).getPage()); // halaman untuk tanda tangan
                        userSignData.setSigPosLLX(dataSignSeal.get(i).getLx()); //posisi titik bawah kiri X
                        userSignData.setSigPosLLY(dataSignSeal.get(i).getLy()); //posisi titik bawah kiri Y
                        userSignData.setSigPosURX(dataSignSeal.get(i).getRx());//posisi titik atas kanan X
                        userSignData.setSigPosURY(dataSignSeal.get(i).getRy());//posisi titik atas kanan Y
                        userSignData.setType(dataSignSeal.get(i).getType());
                        userSignData.setVisible(dataSignSeal.get(i).getVisible());
                        userSignData.setLevel(dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel());
                        userSignData.setDoc_id(dataSignSeal.get(i).getDocumentBean().getId().toString());
                        userSignData.setSignID(dataSignSeal.get(i).getId().toString());

                        if(dataSignSeal.get(i).getType().equals("seal"))
                        {
                            LogSystem.info("SEAL");
                            userSignData.setLevel("C5");
                            userSignData.getCertificate().setLevelCert("C5");
                            userSignData.getCertificate().setEmail("MT"+userMitra);

                            //check for get user seal qr text
                            LogSystem.info(dataSignSeal.get(i).getId().toString());
                            LogSystem.info(userSignData.getCertificate().getIdUser().toString());
                            Seal seal = sealRepository.getUserSeal(dataSignSeal.get(i).getId(), userSignData.getCertificate().getIdUser());

                            if(seal != null)
                            {
                                LogSystem.info("ISI SEAL " + seal.getName());
                                userSignData.setWithQR(seal.getSealWithQr());
                                LogSystem.info("With QR : "+userSignData.isWithQR());
                                if(userSignData.isWithQR())
                                {
                                    if(seal.getImageQr() != null)
                                    {
                                        org = seal.getImageQr();
                                        userSignData.setPathLogo(org);
                                    }
                                    else
                                    {
                                        org = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getITtd();
                                    }
                                }
                                else
                                {
                                    org = seal.getImage();
                                }

                                SealDocAccess sda = sealDAR.getSealDocAccess(dataSignSeal.get(i).getId());

                                if(sda != null)
                                {
                                    LogSystem.info("QR Text " + sda.getQrText());
                                    userSignData.setQrText(sda.getQrText());
                                }

                                userSignData.setImgFile(org);
                            }
                            else
                            {
                                LogSystem.info("Seal null");
                                jsonFile.put("result", "14");
                                jsonFile.put("info", "Seal can't be used");
                                canSign=false;
                                LogSystem.info("Seal can't be used");
                                break;
                            }
                        }

                        if(Objects.equals("C5", userSignData.getLevel()))
                        {
                            LogSystem.info("Query for seal");
                            LogSystem.info("LEVEL " + dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel());
                            LogSystem.info("LEVEL SEAL " + userSignData.getLevel());
                            LogSystem.info("EEUSER " + dataSignSeal.get(i).getEeuserBean().getId());
                            LogSystem.info("MITRA " + eeuserMitra);
//                            checkCertSeal = sealCertRepository.checkSealCertificate(dataSignSeal.get(i).getEeuserBean().getMitraBean().getId(), userSignData.getLevel());
                            checkCertSeal = sealCertRepository.checkSealCertificate(eeuserMitra, userSignData.getLevel());
                            if (checkCertSeal.size() < 1)
                            {
                                LogSystem.info("Seal certificate null");
                                jsonFile.put("result", "14");
                                jsonFile.put("info", "Seal can't be used");
                                canSign=false;
                                LogSystem.info("Seal can't be used");
                                break;
                            }
                            CheckSign = sealCertService.checkSign(checkCertSeal.get(0));
                            expiredDate = checkCertSeal.get(0).getWaktuExp();
                            LogSystem.info("MITRAAAAAAAAA "+eeuserMitra);
                            LogSystem.info("LEVELLLLL "+dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel());
//                            checkCertSealACT = sealCertRepository.checkACTSealCertificate(dataSignSeal.get(i).getEeuserBean().getMitraBean().getId(), userSignData.getLevel());
                            checkCertSealACT = sealCertRepository.checkACTSealCertificate(eeuserMitra, userSignData.getLevel());
                            if (checkCertSealACT.size() < 1)
                            {
                                LogSystem.info("Seal certificate null");
                                jsonFile.put("result", "14");
                                jsonFile.put("info", "Seal can't be used");
                                canSign=false;
                                LogSystem.info("Seal can't be used");
                                break;
                            }
                        }
                        else
                        {
                            LogSystem.info("Query for signature");
                            LogSystem.info("LEVEL " + dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel());
                            LogSystem.info("EEUSER " + dataSignSeal.get(i).getEeuserBean().getId());
                            checkCert = keyRepository.checkSignatureCertificate(dataSignSeal.get(i).getEeuserBean().getId(), dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel());
                            checkCertACT = keyRepository.checkACTSignatureCertificate(dataSignSeal.get(i).getEeuserBean().getId(), dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel());
                            if(checkCert.size()>0)
                            {
                                LogSystem.info("Data user di key ada");
                                CheckSign = keyService.checkSign(checkCert.get(0));
                                expiredDate = checkCert.get(0).getWaktuExp();
                            }
                            else {
                                LogSystem.info("Data user di key tidak ada");
                                CheckSign="05";
                            }

                        }

                        //Check user sertificate
                        LogSystem.info("CheckCert result " + CheckSign);

                        if(CheckSign.equals("05"))
                        {
                            jsonFile.put("result", "05");
                            jsonFile.put("info", "user don't have certificate");
                            LogSystem.info("User don't have certificate");
                            canSign = false;
                            break;
                        }
                        else if(CheckSign.equals("06"))
                        {
                            jsonFile.put("result", "06");
                            jsonFile.put("info", "certificate is expired");
                            jsonFile.put("expired-time",sdf.format(expiredDate));
                            LogSystem.info("User certificate is expired");
                            canSign = false;
                            break;
                        }
                        else if(CheckSign.equals("07"))
                        {
                            jsonFile.put("result", "07");
                            jsonFile.put("info", "certificate is revoked");
                            LogSystem.info("User certificate is revoke");
                            canSign = false;
                            break;
                        }

                        LogSystem.info("GET TYIPE"+dataSignSeal.get(i).getType());
                        if(dataSignSeal.get(i).getType().equals("seal")) {
                            LogSystem.info("Seal: "+dataSignSeal.get(i).getType());
                            signatureData.addSignature(userSignData);
                        }
                    }

                    if (canSign) {
                        LogSystem.info("Samba Process");
                        Samba samba;
                        LogSystem.info("Connect to Samba");
                        samba=new Samba();
                        LogSystem.info("In File " + userSignData.getInFile());
                        byte[] dataPdfSrc=samba.openfile(userSignData.getInFile());
                        samba.close();
                        LogSystem.info("File TTD " + userSignData.getImgFile());
                        byte[] dataImgSrc=samba.openfile(userSignData.getImgFile());
                        samba.close();
                        LogSystem.info("Closed connection to Samba");

                        LogSystem.info("Initialize temp folder");
                        String tempDir=tempDirPrefix+ uuid +"-"+timestamp.getTime();
//                        String originalFile=tempDir+"/original.pdf";
                        String fileNameTmp=tempDir+"/"+timestamp.getTime()+".pdf";
                        String fileTtdTmp=tempDir+"/"+timestamp.getTime()+".png";
                        String fileDestNameTmp=tempDir+"/"+timestamp.getTime()+"-SIGNED.pdf";

                        String fileDestNameTmpInit=tempDir+"/"+timestamp.getTime()+"init-SIGNED.pdf";

                        LogSystem.info("Finish initialize temp folder");

                        File directory = new File(tempDir);
                        if (!directory.exists()){
                            directory.mkdirs();
                        }

                        LogSystem.info("Writing byte file to temp folder");
//                        FileUtils.writeByteArrayToFile(new File(originalFile), dataPdfSrc);
                        FileUtils.writeByteArrayToFile(new File(fileNameTmp), dataPdfSrc);
                        FileUtils.writeByteArrayToFile(new File(fileTtdTmp), dataImgSrc);
                        LogSystem.info("Finish write temp file");

                        SignDoc dSigner=new SignDoc();
                        Date date=null;

                        boolean initialSign = false;
                        if(signatureData.getInitials().size()>0)
                        {
                            UserSignature userSignInit=signatureData.getInitials().get(0);
                            userSignInit.setImgFile(fileTtdTmp);
                            userSignInit.setInFile(fileNameTmp);
                            userSignInit.setOutFile(fileDestNameTmpInit);

                            initialSign=true;

                            LogSystem.info("Send data for Decrypt private key, OCSP, CRL and signing process to document with initials");
                            date=dSigner.signingDoc(timestamp,userSignInit,signatureData.getInitials(),tempDir+"/", checkCertACT, checkCertSealACT, org, jsonFile);
                            LogSystem.info("Finish send data for Decrypt, OCSP, CRL and signing process to document with initials");
                        }

                        if(signatureData.getSignatures().size()>0 && (!initialSign || (initialSign && date!=null)))
                        {
                            LogSystem.info("Check usersign is with QR");
                            UserSignature userSign = signatureData.getSignatures().get(0);
                            if(userSign.isWithQR())
                            {
                                LogSystem.info("True");
                                String fileLogoTmp=tempDir+"/"+timestamp.getTime()+"-LOGOQR.png";
                                String fileQRTmp=tempDir+"/"+timestamp.getTime()+"-QRNEW.png";

                                userSign.setqRPathTemp(fileQRTmp);

                                if(userSign.getPathLogo()!=null) {
                                    byte[] dataLogoSrc=samba.openfile(userSign.getPathLogo());
                                    samba.close();
                                    FileUtils.writeByteArrayToFile(new File(fileLogoTmp), dataLogoSrc);
                                    userSign.setPathLogo(fileLogoTmp);
                                }
                                LogSystem.info("Signature with QR Generation " + timestamp);
                            }

                            userSign.setImgFile(fileTtdTmp);
                            userSign.setOutFile(fileDestNameTmp);
                            userSign.setInFile(fileNameTmp);

                            date = null;

                            LogSystem.info("Send data for Decrypt private key, OCSP, CRL and signing process to document with signature");

                            date = dSigner.signingDoc(timestamp, userSign, signatureData.getSignatures(), tempDir + "/", checkCertACT, checkCertSealACT, org, jsonFile);

                            LogSystem.info("Finish send data for Decrypt, OCSP, CRL and signing process to document with signature");
                        }else if(initialSign) {
                            fileDestNameTmp=fileDestNameTmpInit;
                        }

                        LogSystem.info("With response " + date);

                        if(date!=null) {
                            File file = new File(fileDestNameTmp);
                            bytesArray = new byte[(int) file.length()];

                            FileInputStream fileInputStream = null;

                            //read file into bytes[]
                            fileInputStream = new FileInputStream(file);
                            fileInputStream.read(bytesArray);

                            boolean saveToFS=false;
                            try {
                                LogSystem.info("Pathout : " + pathOut);
                                saveToFS=samba.write(bytesArray, pathOut);
                                samba.close();
                                if(saveToFS) {
                                    jsonFile.put("date", date.getTime());
                                    JSONArray tsArray=dSigner.getArrayTimeStamp();
                                    if(tsArray!=null && tsArray.length()>1) {
                                        jsonFile.put("listTimestamp", tsArray);
                                    }
                                    jsonFile.put("result", "00");
                                }

                                try
                                {
                                    LogSystem.info("Send log activity");
                                    Log log=new Log();
                                    log.setActivity("signing");
                                    log.setResult("success");
                                    log.setInformation(doc_name+" berhasil ditandatangani");
                                    log.setUser( jsonData.getLong("user"));
                                    log.setSigning_time(date);
                                    log.setDocument_id(new Long(doc_id));
                                    new ActivityLog(timestamp).sendPost(log);

                                    LogSystem.info("Finish send log activity");
                                    LogSystem.info(log.toString());
                                }catch (Exception c)
                                {
                                    c.printStackTrace();
                                    LogSystem.info("Failed send request to log activity");
                                }
                            }catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                                jsonFile.put("result", "05");
                                jsonFile.put("error", e.toString());
                                LogSystem.error("Error : " + ExceptionUtils.getStackTrace(e) + " " + timestamp);
                                response.put("error", e.getCause());
                            }finally {
                                fileInputStream.close();
                                samba.close();
                            }
                        }else {
                            jsonFile.put("result", "05");

                        }

//                      clear all temp data
                        FileUtils.deleteDirectory(new File(tempDir));

                        response.put("JSONFile", AESEncryption.encryptDoc(jsonFile.toString()));

                        LogSystem.response("Encrypted " + response.toString());
                    }
                }

                if (dataSign.size() < 1 && dataSignSeal.size() < 1)
                {
                    jsonFile.put("result", "05");
                    jsonFile.put("info", "Null doc access");
                    LogSystem.info("Null document access");
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            LogSystem.error("Error, Message : " + e);
            jsonFile.put("result", "05");
            jsonFile.put("error", e.toString());
        }

        float sec = (System.currentTimeMillis() - start) / 1000F;

//        DecimalFormat df = new DecimalFormat("#.##");
        jsonFile.put("process_time", sec);

        LogSystem.response("Decrypted " + jsonFile.toString());
        response.put("JSONFile", AESEncryption.encryptDoc(jsonFile.toString()));

        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }

    @PostMapping(value = "/fixSeal.html", produces = {"multipart/form-data"})
    public ResponseEntity<?> fixSeal(@RequestPart String jsonfield) throws Exception {
        LogSystem.request(jsonfield);

        String uuid = UUID. randomUUID().toString().replace("-", "");

        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");

        JSONObject response = new JSONObject();
        JSONObject jsonFile = new JSONObject();
        JSONObject jsonKey = new JSONObject();
        JSONObject jsonData = new JSONObject();
        String modeEncrypt ="ECB";
        String plainData;
        boolean canSign = true;
        boolean descOnly=false;
        boolean signWQR=false;
        String pathLogo;
        String tempDirPrefix="/tmp/fileTmp/";
        byte[] bytesArray = null;

        try {
            jsonKey = new JSONObject(jsonfield);

            if(!jsonKey.has("JSONFile"))
            {
                jsonFile.put("error", "JSONFile is required");
                canSign = false;
            }
            else
            {
                if(jsonKey.has("encrypt-mode"))
                {
                    modeEncrypt = jsonKey.getString("encrypt-mode");
                }

                if(modeEncrypt.equals("CBC")){
                    plainData= AESEncryption.decryptDoc(jsonKey.getString("JSONFile"));
                }else {
                    plainData= RSAEncryption.decryptWithPriv(jsonKey.getString("JSONFile"));
                }

                LogSystem.info(plainData);
                jsonData=new JSONObject(plainData);
                LogSystem.info(jsonData.toString());
                String pathOut= jsonData.getString("outfile");

                if(jsonData.has("document_access"))
                {
                    jsonFile.put("document_access", jsonData.get("document_access"));
                }

                Date timestamp=new Date(Long.parseLong(jsonData.getString("time")));
                LogSystem.info("Timestamp " + timestamp);
                LogSystem.info("Timestamp 2" + new Date());
//                Date timestamp=new Date();


                LogSystem.info(jsonData.getString("document_access") + " " + pathOut);

//                if(!jsonData.has("user") || !jsonData.has("document_id"))
//                {
//                    jsonFile.put("info", "incomplete data");
//                    canSign = false;
//                }

                DocAccess SealDA;

                List<DocAccess> dataSignSeal = new ArrayList<DocAccess>();;

                JSONArray docAccessArray = jsonData.getJSONArray("document_access");

                LogSystem.info("Doc Access Array " + docAccessArray);
                for (int j = 0 ; j < docAccessArray.length() ; j++)
                {
                    SealDA = docAccessRepository.getUserSealAllDA(jsonData.getLong("user"), Long.valueOf(docAccessArray.get(j).toString()));

                    if(SealDA != null)
                    {
                        LogSystem.info("Doc access found seal " + docAccessArray.get(j));
                        dataSignSeal.add(SealDA);
                    }
                    else
                    {
                        LogSystem.info("Doc access not found seal " + docAccessArray.get(j));
                    }
                }

                if(dataSignSeal.size() < 1)
                {
                    jsonFile.put("result", "05");
                    jsonFile.put("info", "Null doc access");
                    LogSystem.info("Null document access");
                    response.put("JSONFile", AESEncryption.encryptDoc(jsonFile.toString()));

                    LogSystem.response("Result " + jsonFile.toString());

                    return ResponseEntity
                            .status(200)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response.toString());
                }

                List<KeyV3> checkCert;
                List<KeyV3> checkCertSeal;
                List<KeyV3> checkCertACT = null;
                List<KeyV3> checkCertSealACT=null;

                //Process Seal
                String doc_id=dataSignSeal.get(0).getDocumentBean().getId().toString();
                String doc_name=dataSignSeal.get(0).getDocumentBean().getFileName();
                LogSystem.info("SIZEE SEAL: " + dataSignSeal.size());
                UserSignature userSignData = null;
                SignatureData signatureData=new SignatureData();
                UserCertificate uc;
                String org = null;

                for(int i = 0 ; i < dataSignSeal.size() ; i++)
                {
                    //Check document level with user signer level

                    String levelSender = dataSignSeal.get(i).getDocumentBean().getEeuserBean().getUserdataBean().getLevel();
                    String levelSigner = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel();

                    LogSystem.info("Sender level " + levelSender);
                    LogSystem.info("Signer level " + levelSigner);

                    int levelmitra=Integer.parseInt(levelSender.substring(1));
                    int leveluser=Integer.parseInt(levelSigner.substring(1));

                    if(levelmitra>2 && leveluser<3)
                    {
                        jsonFile.put("result", "13");
                        jsonFile.put("info", "This user is not permitted to sign this document");
                        LogSystem.info("User sign " + dataSignSeal.get(i).getEmail());
                        LogSystem.info("Info " + "This user is not permitted to sign this document");
                        canSign = false;
                        break;
                    }


                    String CheckSign;
                    Date expiredDate = null;

                    Long userMitra = dataSignSeal.get(i).getEeuserBean().getMitraBean().getId();

                    Long eeuserMitra = null;
                    if(dataSignSeal.get(i).getEeuserBean().getMitraBean() != null) {
                        eeuserMitra = dataSignSeal.get(i).getEeuserBean().getMitraBean().getId();
                    }
                    else
                    {
                        eeuserMitra = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getMitraBean().getId();
                    }

                    String userNama = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getNama();
                    String userEmail = dataSignSeal.get(i).getEeuserBean().getNick();
                    Long iduser = dataSignSeal.get(i).getEeuserBean().getId();
                    String userProvinsi = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getPropinsi();
                    String userKota = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getKota();
                    String userLevel = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel();

//                        org = userMitra==null?"Personal":userMitra.toString();

                    if(userMitra==null) {
                        LogSystem.info("User mitra null");
                        uc=new UserCertificate(userNama, userEmail, iduser, userProvinsi,userKota, "Personal", "",userLevel);
                    }else {
                        LogSystem.info("User mitra not null");
                        Optional<Mitra> mitraname = mitraRepo.findById(userMitra);
                        LogSystem.info("MITRA NAME : " + mitraname.get().getName());
//                            org=null;
                        if(Optional.ofNullable(mitraname).isPresent()){
//                                org=mitraname.get().getName();
                        }
                        uc=new UserCertificate(userNama, userEmail, iduser, userProvinsi,userKota, mitraname.get().getName(), "",userLevel);
                        uc.setMitraID(userMitra);
                    }

                    userSignData = new UserSignature(uc);

                    userSignData.setImgFile(dataSignSeal.get(i).getEeuserBean().getUserdataBean().getITtd()); // file tanda tangan
                    userSignData.setInFile(dataSignSeal.get(i).getDocumentBean().getPath()+dataSignSeal.get(i).getDocumentBean().getRename()); // file source
                    userSignData.setSigpage(dataSignSeal.get(i).getPage()); // halaman untuk tanda tangan
                    userSignData.setSigPosLLX(dataSignSeal.get(i).getLx()); //posisi titik bawah kiri X
                    userSignData.setSigPosLLY(dataSignSeal.get(i).getLy()); //posisi titik bawah kiri Y
                    userSignData.setSigPosURX(dataSignSeal.get(i).getRx());//posisi titik atas kanan X
                    userSignData.setSigPosURY(dataSignSeal.get(i).getRy());//posisi titik atas kanan Y
                    userSignData.setType(dataSignSeal.get(i).getType());
                    userSignData.setVisible(dataSignSeal.get(i).getVisible());
                    userSignData.setLevel(dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel());
                    userSignData.setDoc_id(dataSignSeal.get(i).getDocumentBean().getId().toString());
                    userSignData.setSignID(dataSignSeal.get(i).getId().toString());

                    if(dataSignSeal.get(i).getType().equals("seal"))
                    {
                        LogSystem.info("SEAL");
                        userSignData.setLevel("C5");
                        userSignData.getCertificate().setLevelCert("C5");
                        userSignData.getCertificate().setEmail("MT"+userMitra);

                        //check for get user seal qr text
                        LogSystem.info(dataSignSeal.get(i).getId().toString());
                        LogSystem.info(userSignData.getCertificate().getIdUser().toString());
                        Seal seal = sealRepository.getUserSeal(dataSignSeal.get(i).getId(), userSignData.getCertificate().getIdUser());

                        if(seal != null)
                        {
                            LogSystem.info("ISI SEAL " + seal.getName());
                            userSignData.setWithQR(seal.getSealWithQr());
                            LogSystem.info("With QR : "+userSignData.isWithQR());
                            if(userSignData.isWithQR())
                            {
                                if(seal.getImageQr() != null)
                                {
                                    org = seal.getImageQr();
                                    userSignData.setPathLogo(org);
                                }
                                else
                                {
                                    org = dataSignSeal.get(i).getEeuserBean().getUserdataBean().getITtd();
                                }
                            }
                            else
                            {
                                org = seal.getImage();
                            }

                            SealDocAccess sda = sealDAR.getSealDocAccess(dataSignSeal.get(i).getId());

                            if(sda != null)
                            {
                                LogSystem.info("QR Text " + sda.getQrText());
                                userSignData.setQrText(sda.getQrText());
                            }

                            userSignData.setImgFile(org);
                        }
                        else
                        {
                            LogSystem.info("Seal null");
                            jsonFile.put("result", "14");
                            jsonFile.put("info", "Seal can't be used");
                            canSign=false;
                            LogSystem.info("Seal can't be used");
                            break;
                        }
                    }

                    if(Objects.equals("C5", userSignData.getLevel()))
                    {
                        LogSystem.info("Query for seal");
                        LogSystem.info("LEVEL " + dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel());
                        LogSystem.info("LEVEL SEAL " + userSignData.getLevel());
                        LogSystem.info("EEUSER " + dataSignSeal.get(i).getEeuserBean().getId());
                        LogSystem.info("MITRA " + eeuserMitra);
//                            checkCertSeal = sealCertRepository.checkSealCertificate(dataSignSeal.get(i).getEeuserBean().getMitraBean().getId(), userSignData.getLevel());
                        checkCertSeal = sealCertRepository.checkSealCertificate(eeuserMitra, userSignData.getLevel());
                        if (checkCertSeal.size() < 1)
                        {
                            LogSystem.info("Seal certificate null");
                            jsonFile.put("result", "14");
                            jsonFile.put("info", "Seal can't be used");
                            canSign=false;
                            LogSystem.info("Seal can't be used");
                            break;
                        }
                        CheckSign = sealCertService.checkSign(checkCertSeal.get(0));
                        expiredDate = checkCertSeal.get(0).getWaktuExp();
                        LogSystem.info("MITRAAAAAAAAA "+eeuserMitra);
                        LogSystem.info("LEVELLLLL "+dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel());
//                            checkCertSealACT = sealCertRepository.checkACTSealCertificate(dataSignSeal.get(i).getEeuserBean().getMitraBean().getId(), userSignData.getLevel());
                        checkCertSealACT = sealCertRepository.checkACTSealCertificate(eeuserMitra, userSignData.getLevel());
                        if (checkCertSealACT.size() < 1)
                        {
                            LogSystem.info("Seal certificate null");
                            jsonFile.put("result", "14");
                            jsonFile.put("info", "Seal can't be used");
                            canSign=false;
                            LogSystem.info("Seal can't be used");
                            break;
                        }
                    }
                    else
                    {
                        LogSystem.info("Query for signature");
                        LogSystem.info("LEVEL " + dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel());
                        LogSystem.info("EEUSER " + dataSignSeal.get(i).getEeuserBean().getId());
                        checkCert = keyRepository.checkSignatureCertificate(dataSignSeal.get(i).getEeuserBean().getId(), dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel());
                        checkCertACT = keyRepository.checkACTSignatureCertificate(dataSignSeal.get(i).getEeuserBean().getId(), dataSignSeal.get(i).getEeuserBean().getUserdataBean().getLevel());
                        if(checkCert.size()>0)
                        {
                            LogSystem.info("Data user di key ada");
                            CheckSign = keyService.checkSign(checkCert.get(0));
                            expiredDate = checkCert.get(0).getWaktuExp();
                        }
                        else {
                            LogSystem.info("Data user di key tidak ada");
                            CheckSign="05";
                        }

                    }

                    //Check user sertificate
                    LogSystem.info("CheckCert result " + CheckSign);

                    if(CheckSign.equals("05"))
                    {
                        jsonFile.put("result", "05");
                        jsonFile.put("info", "user don't have certificate");
                        LogSystem.info("User don't have certificate");
                        canSign = false;
                        break;
                    }
                    else if(CheckSign.equals("06"))
                    {
                        jsonFile.put("result", "06");
                        jsonFile.put("info", "certificate is expired");
                        jsonFile.put("expired-time",sdf.format(expiredDate));
                        LogSystem.info("User certificate is expired");
                        canSign = false;
                        break;
                    }
                    else if(CheckSign.equals("07"))
                    {
                        jsonFile.put("result", "07");
                        jsonFile.put("info", "certificate is revoked");
                        LogSystem.info("User certificate is revoke");
                        canSign = false;
                        break;
                    }

                    LogSystem.info("GET TYIPE"+dataSignSeal.get(i).getType());
                    if(dataSignSeal.get(i).getType().equals("seal")) {
                        LogSystem.info("Seal: "+dataSignSeal.get(i).getType());
                        signatureData.addSignature(userSignData);
                    }
                }

                if (canSign) {
                    LogSystem.info("Samba Process");
                    Samba samba;
                    LogSystem.info("Connect to Samba");
                    samba=new Samba();
                    LogSystem.info("In File " + userSignData.getInFile());
                    byte[] dataPdfSrc=samba.openfile(userSignData.getInFile());
                    samba.close();
                    LogSystem.info("File TTD " + userSignData.getImgFile());
                    byte[] dataImgSrc=samba.openfile(userSignData.getImgFile());
                    samba.close();
                    LogSystem.info("Closed connection to Samba");

                    LogSystem.info("Initialize temp folder");
                    String tempDir=tempDirPrefix+ uuid +"-"+timestamp.getTime();
//                        String originalFile=tempDir+"/original.pdf";
                    String fileNameTmp=tempDir+"/"+timestamp.getTime()+".pdf";
                    String fileTtdTmp=tempDir+"/"+timestamp.getTime()+".png";
                    String fileDestNameTmp=tempDir+"/"+timestamp.getTime()+"-SIGNED.pdf";

                    String fileDestNameTmpInit=tempDir+"/"+timestamp.getTime()+"init-SIGNED.pdf";

                    LogSystem.info("Finish initialize temp folder");

                    File directory = new File(tempDir);
                    if (!directory.exists()){
                        directory.mkdirs();
                    }

                    LogSystem.info("Writing byte file to temp folder");
//                        FileUtils.writeByteArrayToFile(new File(originalFile), dataPdfSrc);
                    FileUtils.writeByteArrayToFile(new File(fileNameTmp), dataPdfSrc);
                    FileUtils.writeByteArrayToFile(new File(fileTtdTmp), dataImgSrc);
                    LogSystem.info("Finish write temp file");

                    SignDoc dSigner=new SignDoc();
                    Date date=null;

                    boolean initialSign = false;
                    if(signatureData.getInitials().size()>0)
                    {
                        UserSignature userSignInit=signatureData.getInitials().get(0);
                        userSignInit.setImgFile(fileTtdTmp);
                        userSignInit.setInFile(fileNameTmp);
                        userSignInit.setOutFile(fileDestNameTmpInit);

                        initialSign=true;

                        LogSystem.info("Send data for Decrypt private key, OCSP, CRL and signing process to document with initials");
                        date=dSigner.signingDoc(timestamp,userSignInit,signatureData.getInitials(),tempDir+"/", checkCertACT, checkCertSealACT, org, jsonFile);
                        LogSystem.info("Finish send data for Decrypt, OCSP, CRL and signing process to document with initials");
                    }

                    if(signatureData.getSignatures().size()>0 && (!initialSign || (initialSign && date!=null)))
                    {
                        LogSystem.info("Check usersign is with QR");
                        UserSignature userSign = signatureData.getSignatures().get(0);
                        if(userSign.isWithQR())
                        {
                            LogSystem.info("True");
                            String fileLogoTmp=tempDir+"/"+timestamp.getTime()+"-LOGOQR.png";
                            String fileQRTmp=tempDir+"/"+timestamp.getTime()+"-QRNEW.png";

                            userSign.setqRPathTemp(fileQRTmp);

                            if(userSign.getPathLogo()!=null) {
                                byte[] dataLogoSrc=samba.openfile(userSign.getPathLogo());
                                samba.close();
                                FileUtils.writeByteArrayToFile(new File(fileLogoTmp), dataLogoSrc);
                                userSign.setPathLogo(fileLogoTmp);
                            }
                            LogSystem.info("Signature with QR Generation " + timestamp);
                        }

                        userSign.setImgFile(fileTtdTmp);
                        userSign.setOutFile(fileDestNameTmp);
                        userSign.setInFile(fileNameTmp);

                        date = null;

                        LogSystem.info("Send data for Decrypt private key, OCSP, CRL and signing process to document with signature");

                        date = dSigner.signingDoc(timestamp, userSign, signatureData.getSignatures(), tempDir + "/", checkCertACT, checkCertSealACT, org, jsonFile);

                        LogSystem.info("Finish send data for Decrypt, OCSP, CRL and signing process to document with signature");
                    }else if(initialSign) {
                        fileDestNameTmp=fileDestNameTmpInit;
                    }

                    LogSystem.info("With response " + date);

                    if(date!=null) {
                        File file = new File(fileDestNameTmp);
                        bytesArray = new byte[(int) file.length()];

                        FileInputStream fileInputStream = null;

                        //read file into bytes[]
                        fileInputStream = new FileInputStream(file);
                        fileInputStream.read(bytesArray);

                        boolean saveToFS=false;
                        try {
                            LogSystem.info("Pathout : " + pathOut);
                            saveToFS=samba.write(bytesArray, pathOut);
                            samba.close();
                            if(saveToFS) {
                                jsonFile.put("date", date.getTime());
                                JSONArray tsArray=dSigner.getArrayTimeStamp();
                                if(tsArray!=null && tsArray.length()>1) {
                                    jsonFile.put("listTimestamp", tsArray);
                                }
                                jsonFile.put("result", "00");
                            }

                            try
                            {
                                LogSystem.info("Send log activity");
                                Log log=new Log();
                                log.setActivity("signing");
                                log.setResult("success");
                                log.setInformation(doc_name+" berhasil ditandatangani");
                                log.setUser( jsonData.getLong("user"));
                                log.setSigning_time(date);
                                log.setDocument_id(new Long(doc_id));
                                new ActivityLog(timestamp).sendPost(log);

                                LogSystem.info("Finish send log activity");
                                LogSystem.info(log.toString());
                            }catch (Exception c)
                            {
                                c.printStackTrace();
                                LogSystem.info("Failed send request to log activity");
                            }
                        }catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                            jsonFile.put("result", "05");
                            jsonFile.put("error", e.toString());
                            LogSystem.error("Error : " + ExceptionUtils.getStackTrace(e) + " " + timestamp);
                            response.put("error", e.getCause());
                        }finally {
                            fileInputStream.close();
                            samba.close();
                        }
                    }else {
                        jsonFile.put("result", "05");

                    }

//                  clear all temp data
                    FileUtils.deleteDirectory(new File(tempDir));

                    response.put("JSONFile", AESEncryption.encryptDoc(jsonFile.toString()));

                    LogSystem.response(response.toString());
                }

                if (dataSignSeal.size() < 1)
                {
                    jsonFile.put("result", "05");
                    jsonFile.put("info", "Null doc access");
                    LogSystem.info("Null document access");
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            LogSystem.error("Error, Message : " + e);
            jsonFile.put("result", "05");
            jsonFile.put("error", e.toString());
        }

        LogSystem.response(jsonFile.toString());
        response.put("JSONFile", AESEncryption.encryptDoc(jsonFile.toString()));

        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }
}