package com.digisign.kms.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel("Save ")
public class SealVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id can not null")
    private Long id;

    private String name;

    private Long mitra;

    private String password;

    private String image;

    private Boolean sealWithQr;

    private String imageQr;

    private String examplePath;

}
