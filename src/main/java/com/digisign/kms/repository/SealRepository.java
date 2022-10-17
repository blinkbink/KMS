package com.digisign.kms.repository;

import com.digisign.kms.model.Seal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SealRepository extends JpaRepository<Seal, Long>, JpaSpecificationExecutor<Seal> {
    @Query( value = "select sl from Seal sl, UserSeal u_sl, SealDocAccess sl_dc_ac where sl_dc_ac.docAccess=:iddocaccess AND sl_dc_ac.userSealBean.id=u_sl.id AND u_sl.sealBean.id=sl.id and u_sl.eeuser=:iduser")
    Seal getUserSeal(@Param("iddocaccess") Long iddocaccess, @Param("iduser") Long iduser);

    @Query( value = "select sl from Seal sl, UserSeal u_sl where u_sl.sealBean.id=sl.id and sl.id=:sealid and u_sl.eeuser=:iduser and sl.mitra=:idmitra")
    Seal getUserSealTest(@Param("idmitra") Long idMitra, @Param("iduser") Long iduser, @Param("sealid") Long idseal);
}