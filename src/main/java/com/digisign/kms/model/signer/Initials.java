package com.digisign.kms.model.signer;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "initials")
public class Initials {

    private Long doc_access;

    @Id
    @SequenceGenerator(name = "initials_sq", sequenceName = "initials_sq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "initials_sq")
    private Long id;

    private int page;

    private String lx;

    private String ly;

    private String rx;

    private String ry;
}