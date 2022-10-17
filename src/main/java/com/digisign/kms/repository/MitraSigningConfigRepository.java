package com.digisign.kms.repository;

import com.digisign.kms.model.MitraSigningConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MitraSigningConfigRepository extends JpaRepository<MitraSigningConfig, Long>, JpaSpecificationExecutor<MitraSigningConfig> {

    @Query( value = "SELECT ms FROM MitraSigningConfig ms WHERE  ms.mitra = :mitraid")
    MitraSigningConfig getByMitra(@Param("mitraid") Long mitraid);

}