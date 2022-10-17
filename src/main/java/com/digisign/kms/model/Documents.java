package com.digisign.kms.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Data
@Entity
@Accessors(chain = true)
@Table(name = "documents")
public class Documents implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "branch")
    private String branch;

    @Column(name = "current_seq")
    private Long currentSeq = 0L;

    @Column(name = "dealer_code")
    private String dealerCode;

    @Column(name = "delete")
    private Boolean delete = Boolean.FALSE;

    @Column(name = "file")
    private String file;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "format_pdf")
    private Long formatPdf;

    @Column(name = "id_doc_mitra")
    private String idDocMitra;

    @Column(name = "kode_authorized")
    private String kodeAuthorized;

    @Column(name = "last_proses")
    private Date lastProses;

    @Column(name = "path")
    private String path;

    @Column(name = "payment")
    private String payment;

    @Column(name = "proses")
    private Boolean proses = Boolean.FALSE;

    @Column(name = "redirect")
    private Boolean redirect = Boolean.FALSE;

    @Column(name = "rename")
    private String rename;

    @Column(name = "sequence")
    private Boolean sequence = Boolean.FALSE;

    @Column(name = "sign")
    private Boolean sign = Boolean.FALSE;

    @Column(name = "signdoc")
    private String signdoc;

    @Column(name = "status")
    private String status;

    @Column(name = "type_document")
    private String typeDocument;

    @Column(name = "userdata")
    private Long userdata;

    @Column(name = "waktu_buat")
    private Date waktuBuat;

    @Column(name = "exp_date")
    private Date expDate;

    @Column(name = "exp")
    private Boolean exp = Boolean.FALSE;

    //bi-directional many-to-one association to Eeuser
    @ManyToOne
    @JoinColumn(name="eeuser")//
    private Eeuser eeuserBean;

    @OneToMany(mappedBy = "documentBean")
    private List<DocAccess> DocAccess;

}
