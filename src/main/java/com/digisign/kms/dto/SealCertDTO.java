package com.digisign.kms.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("")
public class SealCertDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String jenisKey;

    private String keyId;

    private String userId;

    private String key;

    private Date waktuBuat;

    private String status;

    private Date waktuExp;

    private Boolean enc;

    private Long mitra;

    private String level;

    private String serial;

    private Long pairKeyId;

}
