package com.digisign.kms.repository;

import com.digisign.kms.model.signer.Initials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InitialsRepository extends JpaRepository<Initials, Long>, JpaSpecificationExecutor<Initials> {

    @Query( value = "SELECT i FROM Initials i WHERE i.doc_access = :docaccess")
    List<Initials> getInitials(@Param("docaccess") Long docaccess);
}
