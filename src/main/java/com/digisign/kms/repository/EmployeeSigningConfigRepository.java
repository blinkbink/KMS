package com.digisign.kms.repository;

import com.digisign.kms.model.EmployeeSigningConfig;
import com.digisign.kms.model.MitraSigningConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeSigningConfigRepository extends JpaRepository<EmployeeSigningConfig, Long>, JpaSpecificationExecutor<EmployeeSigningConfig> {


    @Query( value = "SELECT es FROM EmployeeSigningConfig es WHERE  es.mitra = :mitraid")
    EmployeeSigningConfig getByMitra(@Param("mitraid") Long mitraid);
}