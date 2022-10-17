package com.digisign.kms.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "seal")
public class Seal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id ;

    @Column(name = "name")
    private String name;

    @Column(name = "mitra")
    private Long mitra;

    @Column(name = "password")
    private String password;

    @Column(name = "image")
    private String image;

    @Column(name = "seal_with_qr")
    private Boolean sealWithQr = Boolean.FALSE;

    @Column(name = "image_qr")
    private String imageQr;

    @Column(name = "example_path")
    private String examplePath;

    @OneToMany(mappedBy = "sealBean")
    private List<UserSeal> userSeals;

}
