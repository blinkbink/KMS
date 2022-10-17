package com.digisign.kms.repository;

import com.digisign.kms.model.KeyV3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface KeyV3Repository extends JpaRepository<KeyV3, Long>, JpaSpecificationExecutor<KeyV3> {

}