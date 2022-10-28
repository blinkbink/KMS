package com.digisign.kms.repository;

import com.digisign.kms.model.EmployeeSigningConfig;
import com.digisign.kms.model.PersonalSigningConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonalSigningConfigRepository extends JpaRepository<PersonalSigningConfig, Long>, JpaSpecificationExecutor<PersonalSigningConfig> {

    @Query( value = "SELECT ps FROM PersonalSigningConfig ps WHERE  ps.eeuser = :eeuser")
    PersonalSigningConfig getByUser(@Param("eeuser") Long eeuser);
}