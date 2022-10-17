package com.digisign.kms.repository;

import com.digisign.kms.model.Mitra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MitraRepository extends JpaRepository<Mitra, Long>, JpaSpecificationExecutor<Mitra> {

}