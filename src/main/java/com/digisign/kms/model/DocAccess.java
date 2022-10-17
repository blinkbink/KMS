package com.digisign.kms.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name="doc_access")
public class DocAccess {

    @Id
    @SequenceGenerator(name = "docaccess_sq", sequenceName = "docaccess_sq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "docaccess_sq")
    private Long id;

    private String action;

    private Boolean cancel;

    @Column(name="date_sign")
    private Timestamp dateSign;

    private Timestamp datetime;

    private String email;

    private Boolean flag;

    private String invoice;

    private String lx;

    private String ly;

    private String name;

    private Integer page;

    private Boolean read;

    private String rx;

    private String ry;

    @Column(name="sequence_no")
    private Integer sequenceNo;

    private String type;

    private Long userdata;

    private Boolean visible;
//
//    //bi-directional many-to-one association to CanceledDocument
//    @OneToMany(mappedBy="docAccessBean")
//    private List<CanceledDocument> canceledDocuments;
//
    //bi-directional many-to-one association to Eeuser
    @ManyToOne
    @JoinColumn(name="eeuser")//
    private Eeuser eeuserBean;

    //bi-directional many-to-one association to Documents
    @ManyToOne
    @JoinColumn(name="document")//
    private Documents documentBean;

//    private Long documents;
//
//    //bi-directional many-to-one association to Invoice
//    @ManyToOne
//    @JoinColumn(name="id_invoice")
//    private Invoice invoiceBean;
//
//    //bi-directional many-to-one association to SealDocAccess
//    @OneToMany(mappedBy="docAccessBean")
//    private List<SealDocAccess> sealDocAccesses;
}
