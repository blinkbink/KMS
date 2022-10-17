package com.digisign.kms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;


@Data
@ApiModel("Save ")
public class UserdataVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id can not null")
    private Long id;

    private String noIdentitas;

    private String nama;

    private String jk;

    private String tempatLahir;

    private LocalDate tglLahir;

    private String kelurahan;

    private String alamat;

    private String kecamatan;

    private String kota;

    private String propinsi;

    private String kodepos;

    private String noHandphone;

    private String npwp;

    private String iKtp;

    private String iNpwp;

    private String iWajah;

    private Long mitra;

    private String iWajah2;

    private String fingerprint1;

    private String fingerprint2;

    private String iTtd;

    private String iWajah3;


    /**
     * '1' : WNI, '2' :WNA
     */
    @ApiModelProperty("'1' : WNI, '2' :WNA")
    private String wn;


    /**
     * '1': KTP, '2':KITAS, '3':PASSPORT
     */
    @ApiModelProperty("'1': KTP, '2':KITAS, '3':PASSPORT")
    private String idType;

    private String kebangsaan;

    private String iKitas;

    private String kitas;

    private String level;

    private Long verifier;

    private String scoreFace;

    private String iWajahDukcapil;

    private String iWefie;

}
