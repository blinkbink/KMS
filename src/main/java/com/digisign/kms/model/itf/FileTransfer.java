package com.digisign.kms.model.itf;

import java.io.IOException;
import java.util.Date;

public interface FileTransfer {
    public void openConnection(boolean newServer)  throws IOException;
    public Exception getEx();
    public void close();
    public void setTimetamp(Date date);
    public byte[] openfile(String pathFile) throws Exception ;
    public boolean write(byte [] data, String pathFile) throws Exception;
}