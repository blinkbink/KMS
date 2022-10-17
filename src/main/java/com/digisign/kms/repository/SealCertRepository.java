package com.digisign.kms.repository;

import com.digisign.kms.model.KeyV3;
import com.digisign.kms.model.SealCert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SealCertRepository extends JpaRepository<SealCert, Long>, JpaSpecificationExecutor<SealCert> {

    @Query( value = "SELECT k FROM KeyV3 k WHERE k.mitra =:mitraid AND k.status = 'ACT' AND k.level = :level ORDER BY k.id DESC")
    List<KeyV3> checkACTSealCertificate(@Param("mitraid") Long mitraid, @Param("level") String level);

    @Query( value = "SELECT k FROM KeyV3 k WHERE k.mitra = :mitraid AND k.status = 'ACT' AND k.jenisKey = 'CR' AND k.level = :level ORDER BY k.id DESC")
    List<KeyV3> checkSealCertificate( @Param("mitraid") Long mitraid, @Param("level") String level);
}