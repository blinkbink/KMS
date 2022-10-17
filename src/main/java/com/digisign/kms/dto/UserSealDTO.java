package com.digisign.kms.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("")
public class UserSealDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private Long seal;

    private Long eeuser;

    private Boolean status;

}
