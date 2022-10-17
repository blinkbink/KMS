package com.digisign.kms.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class Log {
    private String activity;
    private String result;
    private String information;
    private String auth[];
    private Long user;
    private Long document_id;
    private String certificate_cn;
    private String signing_time;

    public String getSigning_time() {
        return signing_time;
    }
    public void setSigning_time(String signing_time) {
        this.signing_time = signing_time;
    }

    public void setSigning_time(Date date) {
        this.signing_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
    }
    public String getActivity() {
        return activity;
    }
    public void setActivity(String activity) {
        this.activity = activity;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getInformation() {
        return information;
    }
    public void setInformation(String information) {
        this.information = information;
    }
    public String[] getAuth() {
        return auth;
    }
    public void setAuth(String[] auth) {
        this.auth = auth;
    }
    public Long getUser() {
        return user;
    }
    public void setUser(Long user) {
        this.user = user;
    }
    public Long getDocument_id() {
        return document_id;
    }
    public void setDocument_id(Long document_id) {
        this.document_id = document_id;
    }
    public String getCertificate_cn() {
        return certificate_cn;
    }
    public void setCertificate_cn(String certificate_cn) {
        this.certificate_cn = certificate_cn;
    }

}
