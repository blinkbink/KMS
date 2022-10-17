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
@Table(name = "key")
public class Key implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "userdata")
    private Long userdata;

    @Column(name = "jenis_key")
    private String jenisKey;

    @Column(name = "key_id")
    private String keyId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "key")
    private String key;

    @Column(name = "waktu_buat")
    private Date waktuBuat;

    @Column(name = "status")
    private String status;

    @Column(name = "waktu_exp")
    private Date waktuExp;

    @Column(name = "enc")
    private Boolean enc;

    @Column(name = "eeuser")
    private Long eeuser;

    @Column(name = "level")
    private String level = "'C4'::character varying";

    @Column(name = "serial")
    private String serial;

    @Column(name = "pair_key_id")
    private Long pairKeyId;

}
