package com.digisign.kms.repository;

import com.digisign.kms.model.Seal;
import com.digisign.kms.model.SealDocAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SealDocAccessRepository extends JpaRepository<SealDocAccess, Long>, JpaSpecificationExecutor<SealDocAccess> {

    @Query( value = "select sl_dc_ac from SealDocAccess sl_dc_ac where sl_dc_ac.docAccess=:iddocaccess")
    SealDocAccess getSealDocAccess(@Param("iddocaccess") Long iddocaccess);

}