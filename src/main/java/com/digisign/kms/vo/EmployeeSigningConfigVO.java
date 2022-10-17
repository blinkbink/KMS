package com.digisign.kms.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel("Save ")
public class EmployeeSigningConfigVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id can not null")
    private Long id;

    private Long mitra;

    private Boolean signatureWithQr;

    private String qrLogoImg;

}
