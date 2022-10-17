package com.digisign.kms.model.signer;

import java.io.Serializable;

public class UserCertificate implements Serializable {

    private String name;
    private Long mitraID;
    private String email;
    private Long idUser;
    private String province;
    private String city;
    private String organization;
    private String unitOrganization;
    private String levelCert;

    public UserCertificate(String name, String email, Long idUser, String province, String city, String organization,
                           String unitOrganization, String levelCert) {
        super();
        this.name = name;
        this.email = email;
        this.idUser = idUser;
        this.province = province;
        this.city = city;
        this.organization = organization;
        this.unitOrganization = unitOrganization;
        this.levelCert=levelCert;
    }

    public Long getMitraID() {
        return mitraID;
    }

    public void setMitraID(Long mitraID) {
        this.mitraID = mitraID;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Long getIdUser() {
        return idUser;
    }
    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getOrganization() {
        return organization;
    }
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    public String getUnitOrganization() {
        return unitOrganization;
    }
    public void setUnitOrganization(String unitOrganization) {
        this.unitOrganization = unitOrganization;
    }
    public String getLevelCert() {
        return levelCert;
    }
    public void setLevelCert(String levelCert) {
        this.levelCert = levelCert;
    }


}