package com.digisign.kms.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Retrieve by query ")
public class EmployeeSigningConfigQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long mitra;

    private Boolean signatureWithQr;

    private String qrLogoImg;

}
