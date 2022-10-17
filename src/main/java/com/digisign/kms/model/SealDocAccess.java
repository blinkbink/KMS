package com.digisign.kms.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "seal_doc_access")
public class SealDocAccess implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id ;
//
//    @Column(name = "user_seal")
//    private Long userSeal;

    @Column(name = "doc_access")
    private Long docAccess;

    @Column(name = "qr_text")
    private String qrText;

    @ManyToOne
    @JoinColumn(name="user_seal")//
    private UserSeal userSealBean;

}
