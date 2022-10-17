package com.digisign.kms.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@Data
@Entity
@Table(name = "eeuser")
public class Eeuser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nick", nullable = false)
    private String nick;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "mid")
    private Long mid;

    @Column(name = "time")
    private Date time;

/**
 * 0: belum verifikasi
 1: sudah verifikasi
 2: reject
 3: aktif
 4: suspend
 */
    @Column(name = "status")
    @ApiModelProperty("0: belum verifikasi 1:sudah verifikasi 2:reject 3:aktif 4:suspend")
            private String status="'0'::bpchar";

            @Column(name = "imei")
            private String imei;

/**
 * 1:  prepaid
2:  postpaid
3:  langganan
*/
            @Column(name = "pay_type", nullable = false)
            @ApiModelProperty("1:  prepaid 2:postpaid 3:langganan")
            private String payType="1";

            @Column(name = "admin")
            private Boolean admin=Boolean.FALSE;

            @Column(name = "auto_ttd", nullable = false)
            private Boolean autoTtd=Boolean.FALSE;

            @Column(name = "login")
            private Long login;

            @Column(name = "key_at_ttd")
            private String keyAtTtd;

            @Column(name = "i_qrcode")
            private String iQrcode;

            @Column(name = "position")
            private Long position;

            @Column(name = "block_send_doc")
            private Boolean blockSendDoc=Boolean.FALSE;

            @Column(name = "confirm_tnc")
            private Boolean confirmTnc=Boolean.FALSE;

            @Column(name = "kode_user")
            private String kodeUser;

        //bi-directional many-to-one association to Eeuser
        @ManyToOne
        @JoinColumn(name="userdata", unique = true)//
        private Userdata userdataBean;

    @ManyToOne
    @JoinColumn(name="mitra")//
    private Mitra mitraBean;
}
