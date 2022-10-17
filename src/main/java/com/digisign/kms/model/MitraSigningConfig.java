package com.digisign.kms.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "mitra_signing_config")
public class MitraSigningConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "mitra")
    private Long mitra;

    @Column(name = "signature_desc_only")
    private Boolean signatureDescOnly = Boolean.FALSE;

}
