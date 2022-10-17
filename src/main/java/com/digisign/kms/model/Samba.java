package com.digisign.kms.model;

import com.digisign.kms.model.itf.FileTransfer;
import com.digisign.kms.util.FTP;

import java.util.Date;

public class Samba {

    FileTransfer fTransfer=null;
    public Samba() {
        fTransfer=new FTP();
    }

    public Exception getEx() {
        return fTransfer.getEx();
    }
    public void close() {
        fTransfer.close();
    }
    public void setTimetamp(Date date) {
        fTransfer.setTimetamp(date);
    }
    public byte[] openfile(String pathFile) throws Exception {
        return fTransfer.openfile(pathFile);
    }
    public boolean write(byte[] data, String pathFile) throws Exception {
        return fTransfer.write(data, pathFile);
    }

}
