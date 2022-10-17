package com.digisign.kms.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("Update $table.comment")
@EqualsAndHashCode(callSuper = false)
public class SealUpdateVO extends SealVO implements Serializable {
    private static final long serialVersionUID = 1L;

}
