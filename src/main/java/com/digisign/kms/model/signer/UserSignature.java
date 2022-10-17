package com.digisign.kms.model.signer;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSignature {

    private UserCertificate certificate;
    private String inFile;
    private String outFile;
    private String imgFile;
    private int sigpage;
    private String sigPosLLX;
    private String sigPosLLY;
    private String sigPosURX;
    private String sigPosURY;
    private String signID;
    private String type;
    private String level;
    private String qrText;
    private boolean descOnly=false;
    private boolean visible=true;
    private boolean withQR=false;
    private boolean withSignature=false;
    private boolean qrOnly=false;
    private String pathLogo;
    private String doc_id;
    private String qRPathTemp;
    private int position=1;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isQrOnly() {
        return qrOnly;
    }

    public void setQrOnly(boolean qrOnly) {
        this.qrOnly = qrOnly;
    }

    public boolean isWithSignature() {
        return withSignature;
    }

    public void setWithSignature(boolean withSignature) {
        this.withSignature = withSignature;
    }

    public boolean getDescOnly() {
        return descOnly;
    }



    public String getPathLogo() {
        return pathLogo;
    }



    public void setPathLogo(String pathLogo) {
        this.pathLogo = pathLogo;
    }




    public String getDoc_id() {
        return doc_id;
    }



    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }



    public String getqRPathTemp() {
        return qRPathTemp;
    }



    public void setqRPathTemp(String qRPathTemp) {
        this.qRPathTemp = qRPathTemp;
    }




    public void setDescOnly(boolean descOnly) {
        this.descOnly = descOnly;
    }



    public boolean isVisible() {
        return visible;
    }



    public void setVisible(boolean visible) {
        this.visible = visible;
    }





    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSignID() {
        return signID;
    }

    public void setSignID(String signID) {
        this.signID = signID;
    }

    public String getQrText() {
        return qrText;
    }



    public void setQrText(String qrText) {
        this.qrText = qrText;
    }



    public UserSignature(UserCertificate cert) {
        certificate=cert;
    }

    public UserSignature() {

    }

    public int getSigpage() {
        return sigpage;
    }


    public void setSigpage(int sigpage) {
        this.sigpage = sigpage;
    }


    public UserCertificate getCertificate() {
        return certificate;
    }


    public void setCertificate(UserCertificate certificate) {
        this.certificate = certificate;
    }


    public String getInFile() {
        return inFile;
    }


    public void setInFile(String inFile) {
        this.inFile = inFile;
    }


    public String getOutFile() {
        return outFile;
    }


    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }


    public String getImgFile() {
        return imgFile;
    }


    public void setImgFile(String imgFile) {
        this.imgFile = imgFile;
    }


    public String getSigPosLLX() {
        return sigPosLLX;
    }


    public void setSigPosLLX(String sigPosLLX) {
        this.sigPosLLX = sigPosLLX;
    }

    public String getSigPosLLY() {
        return sigPosLLY;
    }

    public void setSigPosLLY(String sigPosLLY) {
        this.sigPosLLY = sigPosLLY;
    }

    public String getSigPosURX() {
        return sigPosURX;
    }

    public void setSigPosURX(String sigPosURX) {
        this.sigPosURX = sigPosURX;
    }

    public String getSigPosURY() {
        return sigPosURY;
    }

    public void setSigPosURY(String sigPosURY) {
        this.sigPosURY = sigPosURY;
    }
    public boolean isWithQR() {
        return withQR;
    }
    public void setWithQR(boolean withQR) {
        this.withQR = withQR;
    }


}
