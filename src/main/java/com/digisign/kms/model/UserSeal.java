package com.digisign.kms.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Accessors(chain = true)
@Table(name ="user_seal")
public class UserSeal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

//    @Column(name = "seal")
//    private Long seal;

    @Column(name = "eeuser")
    private Long eeuser;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name="seal")//
    private Seal sealBean;

    @OneToMany(mappedBy = "userSealBean")
    private List<SealDocAccess> sealDocAccesses;

}
