package com.digisign.kms.repository;

import com.digisign.kms.model.Userdata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserdataRepository extends JpaRepository<Userdata, Long>, JpaSpecificationExecutor<Userdata> {

}