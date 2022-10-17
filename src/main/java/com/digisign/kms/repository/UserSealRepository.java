package com.digisign.kms.repository;

import com.digisign.kms.model.UserSeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserSealRepository extends JpaRepository<UserSeal, Long>, JpaSpecificationExecutor<UserSeal> {

}