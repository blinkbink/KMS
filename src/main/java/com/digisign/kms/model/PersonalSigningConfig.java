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
@Table(name = "personal_signing_config")
public class PersonalSigningConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "eeuser")
    private Long eeuser;

    @Column(name = "signature_with_qr")
    private Boolean signatureWithQr = Boolean.FALSE;

    @Column(name = "qr_logo_img")
    private String qrLogoImg;
}
