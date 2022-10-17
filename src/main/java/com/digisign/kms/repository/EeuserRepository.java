package com.digisign.kms.repository;

import com.digisign.kms.model.DocAccess;
import com.digisign.kms.model.Eeuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EeuserRepository extends JpaRepository<Eeuser, Long>, JpaSpecificationExecutor<Eeuser> {

    @Query( value = "SELECT ee FROM Eeuser ee WHERE  ee.nick = :user")
    Optional<Eeuser> getUser(@Param("user") String user);

    @Query( value = "SELECT ee FROM Eeuser ee WHERE  ee.id = :user")
    Eeuser getUserSeal(@Param("user") Long user);
}