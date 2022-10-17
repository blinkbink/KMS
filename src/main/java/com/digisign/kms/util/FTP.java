package com.digisign.kms.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.digisign.kms.model.itf.FileTransfer;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FTP extends Description implements FileTransfer {
    FTPSClient ftpClient ;
    Exception ex;
    Date date=new Date();
    private static final Logger log = LogManager.getLogger();

    @Override
    public void openConnection(boolean newServer) throws IOException {
        // TODO Auto-generated method stub
        String nas_user=null;
        String nas_pass=null;
        String nas_ip=null;
        if(!newServer) {
            nas_ip=FILESYS_SERVER_ADDRESS_NAS;
            nas_user=FILESYS_USERNAME_NAS;
            nas_pass=FILESYS_PASSWORD_NAS;
        }else {
            nas_ip=FILESYS_SERVER_ADDRESS_NAS2;
            nas_user=FILESYS_USERNAME_NAS;
            nas_pass=FILESYS_PASSWORD_NAS;

        }
        int trCount=0;
        log.info(LogSystem.getLog("FTP", date, "open connection FTP"));
        while (trCount<=8 && ftpClient==null) {

            if(trCount>1) {
                if(trCount==2 || trCount==6)
                    nas_ip=FILESYS_SERVER_ADDRESS_NAS2+"2";
                else if(trCount==3 || trCount==7)
                    nas_ip=FILESYS_SERVER_ADDRESS_NAS2+"3";
                else if(trCount==4 || trCount==8)
                    nas_ip=FILESYS_SERVER_ADDRESS_NAS2+"4";
                else
                    nas_ip=FILESYS_SERVER_ADDRESS_NAS2;
            }
            try {
                ftpClient= new FTPSClient("TLS", false);
//	    		ftpClient.addProtocolCommandListener(new PrintCommandListener(System.out));

                ftpClient.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
                log.info(LogSystem.getLog("FTP", date, "FTP connectiong to "+nas_ip));
                ftpClient.setConnectTimeout(3000);
                ftpClient.connect(nas_ip, 21);

                boolean login=ftpClient.login(nas_user, nas_pass);
                int reply = ftpClient.getReplyCode();
                showServerReply(ftpClient);
                if (!FTPReply.isPositiveCompletion(reply)|| !login)
                {
                    ftpClient.disconnect();
                    System.err.println("FTP server refused connection.");
                    log.error(LogSystem.getLog("FTP", date, "FTP server refused connection"));
                    ftpClient=null;
                    continue;
                }

                ftpClient.enterLocalPassiveMode();
                ftpClient.execPBSZ(0);
                ftpClient.execPROT("C");
                ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.COMPRESSED_TRANSFER_MODE);
                log.info(LogSystem.getLog("FTP", date, "FTP connection successful"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                log.error(LogSystem.getLog("FTP", date, e.getMessage()));
                log.error(LogSystem.getLog("FTP", date, ExceptionUtils.getStackTrace(e)));
                ftpClient=null;
                ex=e;

            }
            trCount++;
        }
        if(ftpClient!=null && !ftpClient.isConnected()) {
            ftpClient=null;
            throw new IOException("FTP connection problem");
        }
    }

    @Override
    public Exception getEx() {
        // TODO Auto-generated method stub
        return ex;
    }

    @Override
    public void close() {
        if(ftpClient!=null) {
            if(ftpClient.isConnected()) {
                try {
                    log.info(LogSystem.getLog("FTP", date, "disconnecting..."));
                    ftpClient.disconnect();
                    log.info(LogSystem.getLog("FTP", date, "disconnected"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ftpClient=null;
    }

    @Override
    public byte[] openfile(String pathFile) throws Exception {
        byte[] data=null;
        InputStream iStream=null;
        ByteArrayOutputStream outStream=null;
        // Connect to Share
        String [] path=pathFile.split("/");
        boolean statServer=false;
        if(path[1].equals("file2")) {
            statServer=true;
        }
        try{
            openConnection(statServer);


            log.info(LogSystem.getLog("FTP", date, "opening directory "+pathFile));

            outStream=new ByteArrayOutputStream();

            boolean downloaded = ftpClient.retrieveFile(pathFile, outStream);

            if (downloaded) {
                log.info(LogSystem.getLog("FTP", date, "download file "+pathFile+" completed"));
            }else {
                data=null;
                throw new Exception("document couldn't downloaded");
            }
            LogSystem.info("dwl :"+downloaded);
            data=outStream.toByteArray();


        } catch (IOException e) {
            e.printStackTrace();
            ex=e;
            if(iStream!=null)iStream.close();
            if(outStream!=null)outStream.close();
            throw ex;

        } catch (Exception e) {
            e.printStackTrace();
            ex=e;
            if(iStream!=null)iStream.close();
            if(outStream!=null)outStream.close();
            throw ex;
        }finally {
            if(iStream!=null)iStream.close();
            if(outStream!=null)outStream.close();
        }

        return data;
    }

    @Override
    public boolean write(byte[] data, String pathFile) throws Exception {
        boolean res=false;
        InputStream iStream=null;

        // Connect to Share
        String [] path=pathFile.split("/");
        String pathString="";
        String filename="";
        boolean statServer=false;
        if(path[1].equals("file2")) {
            statServer=true;
        }
        try{

            openConnection(statServer);

            int i=0;
            while(i<path.length) {
                if(i!=path.length-1) {
                    pathString+=path[i]+"/";
                    boolean chgDir=ftpClient.changeWorkingDirectory(path[i]+"/");
                    if(!chgDir) {
                        log.info(LogSystem.getLog("FTP", date, "creating directory "+path[i]));
                        ftpClient.makeDirectory(path[i]);
                        chgDir=ftpClient.changeWorkingDirectory(path[i]);
                        if(!chgDir) {
                            throw new Exception("Folder cannot be created");
                        }
                    }
                }
                else {
                    filename=path[i];
                }
                i++;
            }

            log.info(LogSystem.getLog("FTP", date, "opening directory "+pathFile));

            FTPFile f = ftpClient.mdtmFile(filename);
            if (f != null) {
                log.info(LogSystem.getLog("FTP", date, "file exist, skip "+filename));
                res=true;
//                throw new Exception("file cannot be created, duplicated file");
            }
            else {
                log.info(LogSystem.getLog("FTP", date, "transferring file " + pathFile));

                iStream = new ByteArrayInputStream(data);
                res = ftpClient.storeFile(filename, iStream);

                if (!res) {
                    throw new Exception("file cannot be created");
                }
                log.info(LogSystem.getLog("FTP", date, "file has been uploaded " + pathFile));
            }

        } catch (IOException e) {
            e.printStackTrace();
            ex=e;
            if(iStream!=null)iStream.close();
            throw ex;

        } catch (Exception e) {
            e.printStackTrace();
            ex=e;
            if(iStream!=null)iStream.close();
            throw ex;
        }finally {
            if(iStream!=null)iStream.close();
        }

        return res;
    }

    private void printList() throws IOException {
        FTPFile[] files = ftpClient.mlistDir();
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (FTPFile file : files) {
            String details = file.getName();
            if (file.isDirectory()) {
                details = "[" + details + "]";
            }
            details += "\t\t" + file.getSize();
            details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());

            LogSystem.info(details);
        }

    }



    private void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                LogSystem.info("SERVER: " + aReply);
            }
        }
    }


    @Override
    public void setTimetamp(Date date) {
        this.date=date;
    }

}