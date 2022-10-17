package com.digisign.kms.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("Retrieve by query ")
public class DocumentsQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String branch;

    private Long currentSeq;

    private String dealerCode;

    private Boolean delete;

    private Long eeuser;

    private String file;

    private String fileName;

    private Long formatPdf;

    private String idDocMitra;

    private String kodeAuthorized;

    private Date lastProses;

    private String path;

    private String payment;

    private Boolean proses;

    private Boolean redirect;

    private String rename;

    private Boolean sequence;

    private Boolean sign;

    private String signdoc;

    private String status;

    private String typeDocument;

    private Long userdata;

    private Date waktuBuat;

    private Date expDate;

    private Boolean exp;

}
