package com.digisign.kms.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@Data
@Entity
@Accessors(chain = true)
@Table(name = "userdata")
public class Userdata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "no_identitas")
    private String noIdentitas;

    @Column(name = "nama")
    private String nama;

    @Column(name = "jk")
    private String jk;

    @Column(name = "tempat_lahir")
    private String tempatLahir;

    @Column(name = "tgl_lahir")
    private LocalDate tglLahir;

    @Column(name = "kelurahan")
    private String kelurahan;

    @Column(name = "alamat")
    private String alamat;

    @Column(name = "kecamatan")
    private String kecamatan;

    @Column(name = "kota")
    private String kota;

    @Column(name = "propinsi")
    private String propinsi;

    @Column(name = "kodepos")
    private String kodepos;

    @Column(name = "no_handphone")
    private String noHandphone;

    @Column(name = "npwp")
    private String npwp;

    @Column(name = "i_ktp")
    private String iKtp;

    @Column(name = "i_npwp")
    private String iNpwp;

    @Column(name = "i_wajah")
    private String iWajah;

    @Column(name = "i_wajah2")
    private String iWajah2;

    @Column(name = "fingerprint1")
    private String fingerprint1;

    @Column(name = "fingerprint2")
    private String fingerprint2;

    @Column(name = "i_ttd")
    private String iTtd;

    @Column(name = "i_wajah3")
    private String iWajah3;

    /**
     * '1' : WNI, '2' :WNA
     */
    @Column(name = "wn")
    @ApiModelProperty("'1' : WNI, '2' :WNA")
    private String wn = "'1'::bpchar";

    /**
     * '1': KTP, '2':KITAS, '3':PASSPORT
     */
    @Column(name = "id_type")
    @ApiModelProperty("'1': KTP, '2':KITAS, '3':PASSPORT")
    private String idType = "'1'::bpchar";

    @Column(name = "kebangsaan")
    private String kebangsaan = "'Indonesia'::character varying";

    @Column(name = "i_kitas")
    private String iKitas;

    @Column(name = "kitas")
    private String kitas;

    @Column(name = "level")
    private String level = "'C4'::character varying";

    @Column(name = "verifier")
    private Long verifier;

    @Column(name = "score_face")
    private String scoreFace;

    @Column(name = "i_wajah_dukcapil")
    private String iWajahDukcapil;

    @Column(name = "i_wefie")
    private String iWefie;

    @ManyToOne
    @JoinColumn(name="mitra")//
    private Mitra mitraBean;

}
