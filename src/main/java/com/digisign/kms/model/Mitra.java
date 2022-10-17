package com.digisign.kms.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "mitra")
public class Mitra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * nama_mitra
     */
    @Column(name = "name")
    @ApiModelProperty("nama_mitra")
    private String name;

    @Column(name = "type")
    private String type;

    /**
     * link_path siup file (pdf)
     */
    @Column(name = "i_siup")
    @ApiModelProperty("link_path siup file (pdf)")
    private String iSiup;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    /**
     * nama_gedung_mitra
     */
    @Column(name = "gedung")
    @ApiModelProperty("nama_gedung_mitra")
    private String gedung;

    @Column(name = "url")
    private String url;

    @Column(name = "fax")
    private String fax;

    @Column(name = "content")
    private String content;

    @Column(name = "custom")
    private Boolean custom = Boolean.FALSE;

    @Column(name = "logo")
    private String logo;

    @Column(name = "help_email")
    private String helpEmail;

    @Column(name = "verifikasi")
    private Boolean verifikasi = Boolean.FALSE;

    @Column(name = "notifikasi")
    private Boolean notifikasi = Boolean.FALSE;

    /**
     * link_path npwp file (pdf)
     */
    @Column(name = "i_npwp")
    @ApiModelProperty("link_path npwp file (pdf)")
    private String iNpwp;

    /**
     * link_path tdp(tanda daftar perusahaan) file (pdf)
     */
    @Column(name = "i_tdp")
    @ApiModelProperty("link_path tdp(tanda daftar perusahaan) file (pdf)")
    private String iTdp;

    /**
     * link_path akta_perusahaan file (pdf)
     */
    @Column(name = "i_akta_perusahaan")
    @ApiModelProperty("link_path akta_perusahaan file (pdf)")
    private String iAktaPerusahaan;

    /**
     * link_path surat domisili file (pdf)
     */
    @Column(name = "i_surat_domisili")
    @ApiModelProperty("link_path surat domisili file (pdf)")
    private String iSuratDomisili;

    /**
     * no_npwp perusahaan
     */
    @Column(name = "no_npwp")
    @ApiModelProperty("no_npwp perusahaan")
    private String noNpwp;

    /**
     * no_siup mitra
     */
    @Column(name = "no_siup")
    @ApiModelProperty("no_siup mitra")
    private String noSiup;

    /**
     * no_tdp(tanda daftar perusahaan) mitra
     */
    @Column(name = "no_tdp")
    @ApiModelProperty("no_tdp(tanda daftar perusahaan) mitra")
    private String noTdp;

    /**
     * no_akta_perusahaan
     */
    @Column(name = "no_akta_perusahaan")
    @ApiModelProperty("no_akta_perusahaan")
    private String noAktaPerusahaan;

    /**
     * no_surat_domisili perushaan
     */
    @Column(name = "no_surat_domisili")
    @ApiModelProperty("no_surat_domisili perushaan")
    private String noSuratDomisili;

    /**
     * email_company
     */
    @Column(name = "email_corporate")
    @ApiModelProperty("email_company")
    private String emailCorporate;

    /**
     * registration authority id
     */
    @Column(name = "parent_id")
    @ApiModelProperty("registration authority id")
    private Long parentId;

    /**
     * true = digisign, false = non digisign
     */
    @Column(name = "ekyc")
    @ApiModelProperty("true = digisign, false = non digisign")
    private Boolean ekyc = Boolean.FALSE;

    @Column(name = "level")
    private String level = "'C4'::character varying";

    @Column(name = "activation_redirect")
    private String activationRedirect;

    @Column(name = "signing_redirect")
    private String signingRedirect;

    @Column(name = "dukcapil")
    private Boolean dukcapil = Boolean.FALSE;

    @Column(name = "view_redirect")
    private String viewRedirect;

    /**
     * ekyc dari bank dengan bukti no rekening bank
     */
    @Column(name = "ekyc_tabungan")
    @ApiModelProperty("ekyc dari bank dengan bukti no rekening bank")
    private Boolean ekycTabungan = Boolean.FALSE;

    @Column(name = "user_sftp")
    private String userSftp;

    @Column(name = "transaction_p12")
    private Boolean transactionP12 = Boolean.FALSE;

    /**
     * P12 : digisign generate p12 and downloaded by client Sys, CSR: client app generate CSR for user
     */
    @Column(name = "gen_cert_user")
    @ApiModelProperty("P12 : digisign generate p12 and downloaded by client Sys, CSR: client app generate CSR for user")
    private String genCertUser;

    @Column(name = "kategori_usaha")
    private Long kategoriUsaha;

    @Column(name = "provinsi")
    private String provinsi;

    @OneToMany(mappedBy = "mitraBean")
    private List<Userdata> userdata;

    @OneToMany(mappedBy = "mitraBean")
    private List<Eeuser> eeuser;

}
