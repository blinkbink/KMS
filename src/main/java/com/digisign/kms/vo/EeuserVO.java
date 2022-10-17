package com.digisign.kms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
@ApiModel("Save ")
public class EeuserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id can not null")
    private Long id;

    @NotNull(message = "nick can not null")
    private String nick;

    private String password;

    private String name;

    private Boolean deleted;

    private Long mid;

    private Long userdata;

    private Date time;


    /**
     * 0: belum verifikasi
     1: sudah verifikasi
     2: reject
     3: aktif
     4: suspend
     */
    @ApiModelProperty("0: belum verifikasi 1:sudah verifikasi 2:reject 3:aktif 4:suspend")
            private java.lang.String status;

            private java.lang.String imei;


            /**
             * 1:  prepaid
            2:  postpaid
            3:  langganan
            */
            @NotNull(message = "payType can not null")
            @ApiModelProperty("1:  prepaid 2:postpaid 3:langganan")
            private java.lang.String payType;

            private java.lang.Long mitra;

            private java.lang.String noHandphone;

            private java.lang.String npwp;

            private java.lang.String iKtp;

            private java.lang.String iNpwp;

            private java.lang.String iWajah;

            private java.lang.String iWajah2;

            private java.lang.String iTtd;

            private java.lang.Boolean admin;

            @NotNull(message = "autoTtd can not null")
            private java.lang.Boolean autoTtd;

            private java.lang.String iWajah3;

            private java.lang.Long login;

            private java.lang.String keyAtTtd;

            private java.lang.String iQrcode;

            private java.lang.Long position;

            private java.lang.Boolean blockSendDoc;

            private java.lang.Boolean confirmTnc;

            private java.lang.String kodeUser;

            private java.lang.Long branch;
}
