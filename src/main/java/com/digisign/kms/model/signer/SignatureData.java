package com.digisign.kms.model.signer;

import java.util.ArrayList;
import java.util.List;

public class SignatureData {

    List<UserSignature> initials=new ArrayList<UserSignature>();

    List<UserSignature> signatures=new ArrayList<UserSignature>();

    List<Long> error=new ArrayList<Long>();

    String RC="05";

    public void addInitial(UserSignature initial) {
        initials.add(initial);
    }

    public void addSignature(UserSignature signature) {
        signatures.add(signature);
    }

    public String getRC() {
        return RC;
    }

    public void setRC(String rC) {
        RC = rC;
    }

    public List<Long> getError() {
        return error;
    }

    public void setError(List<Long> error) {
        this.error = error;
    }

    public List<UserSignature> getInitials() {
        return initials;
    }

    public List<UserSignature> getSignatures() {
        return signatures;
    }
}