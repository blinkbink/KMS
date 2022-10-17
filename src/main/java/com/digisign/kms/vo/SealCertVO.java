package com.digisign.kms.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
@ApiModel("Save ")
public class SealCertVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id can not null")
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
