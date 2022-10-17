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
@Table(name = "employee_signing_config")
public class EmployeeSigningConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "mitra")
    private Long mitra;

    @Column(name = "signature_with_qr")
    private Boolean signatureWithQr = Boolean.FALSE;

    @Column(name = "qr_logo_img")
    private String qrLogoImg;

    @Column(name = "signature")
    private Boolean signature = Boolean.FALSE;

    @Column(name = "signature_qr_only")
    private Boolean qrOnly = Boolean.FALSE;

    @Column(name = "position")
    private int position = 1;
}
