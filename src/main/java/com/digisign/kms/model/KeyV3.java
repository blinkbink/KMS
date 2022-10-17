package com.digisign.kms.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Data
@Entity
@Accessors(chain = true)
@Table(name = "key_v3")
public class KeyV3 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "jenis_key")
    private String jenisKey;

    @Column(name = "key_alias")
    private String keyAlias;

    @Column(name = "key_version")
    private String keyVersion;

    @Column(name = "key")
    private String key;

    @Column(name = "waktu_buat")
    private Date waktuBuat;

    @Column(name = "status")
    private String status;

    @Column(name = "waktu_exp")
    private Date waktuExp;

    @Column(name = "eeuser")
    private Long eeuser;

    @Column(name = "level")
    private String level;

    @Column(name = "serial")
    private String serial;

    @Column(name = "waktu_download")
    private Date waktuDownload;

    @Column(name = "waktu_upd")
    private Date waktuUpd;

    @Column(name = "mitra")
    private Long mitra;

}
