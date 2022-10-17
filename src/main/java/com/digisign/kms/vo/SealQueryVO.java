package com.digisign.kms.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Retrieve by query ")
public class SealQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Long mitra;

    private String password;

    private String image;

    private Boolean sealWithQr;

    private String imageQr;

    private String examplePath;

}
