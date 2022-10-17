package com.digisign.kms.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Retrieve by query ")
public class MitraQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;


    /**
     * nama_mitra
     */
    @ApiModelProperty("nama_mitra")
    private String name;

    private String type;


    /**
     * link_path siup file (pdf)
     */
    @ApiModelProperty("link_path siup file (pdf)")
    private String iSiup;

    private Long eeuser;

    private String address;

    private String phone;


    /**
     * nama_gedung_mitra
     */
    @ApiModelProperty("nama_gedung_mitra")
    private String gedung;

    private String url;

    private String fax;

    private String content;

    private Boolean custom;

    private String logo;

    private String helpEmail;

    private Boolean verifikasi;

    private Boolean notifikasi;


    /**
     * link_path npwp file (pdf)
     */
    @ApiModelProperty("link_path npwp file (pdf)")
    private String iNpwp;


    /**
     * link_path tdp(tanda daftar perusahaan) file (pdf)
     */
    @ApiModelProperty("link_path tdp(tanda daftar perusahaan) file (pdf)")
    private String iTdp;


    /**
     * link_path akta_perusahaan file (pdf)
     */
    @ApiModelProperty("link_path akta_perusahaan file (pdf)")
    private String iAktaPerusahaan;


    /**
     * link_path surat domisili file (pdf)
     */
    @ApiModelProperty("link_path surat domisili file (pdf)")
    private String iSuratDomisili;


    /**
     * no_npwp perusahaan
     */
    @ApiModelProperty("no_npwp perusahaan")
    private String noNpwp;


    /**
     * no_siup mitra
     */
    @ApiModelProperty("no_siup mitra")
    private String noSiup;


    /**
     * no_tdp(tanda daftar perusahaan) mitra
     */
    @ApiModelProperty("no_tdp(tanda daftar perusahaan) mitra")
    private String noTdp;


    /**
     * no_akta_perusahaan
     */
    @ApiModelProperty("no_akta_perusahaan")
    private String noAktaPerusahaan;


    /**
     * no_surat_domisili perushaan
     */
    @ApiModelProperty("no_surat_domisili perushaan")
    private String noSuratDomisili;


    /**
     * email_company
     */
    @ApiModelProperty("email_company")
    private String emailCorporate;


    /**
     * registration authority id
     */
    @ApiModelProperty("registration authority id")
    private Long parentId;


    /**
     * true = digisign, false = non digisign
     */
    @ApiModelProperty("true = digisign, false = non digisign")
    private Boolean ekyc;

    private String level;

    private String activationRedirect;

    private String signingRedirect;

    private Boolean dukcapil;

    private String viewRedirect;


    /**
     * ekyc dari bank dengan bukti no rekening bank
     */
    @ApiModelProperty("ekyc dari bank dengan bukti no rekening bank")
    private Boolean ekycTabungan;

    private String userSftp;

    private Boolean haveCabang;

    private Boolean transactionP12;


    /**
     * P12 : digisign generate p12 and downloaded by client Sys, CSR: client app generate CSR for user
     */
    @ApiModelProperty("P12 : digisign generate p12 and downloaded by client Sys, CSR: client app generate CSR for user")
    private String genCertUser;

    private Long kategoriUsaha;

    private String provinsi;

}
