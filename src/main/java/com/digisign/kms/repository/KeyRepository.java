package com.digisign.kms.repository;

import com.digisign.kms.model.Key;
import com.digisign.kms.model.KeyV3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KeyRepository extends JpaRepository<Key, Long>, JpaSpecificationExecutor<Key> {

    @Query( value = "SELECT k FROM KeyV3 k WHERE k.eeuser = :eeuser AND k.status = 'ACT' AND k.jenisKey = 'CR' AND k.level = :level ORDER BY k.id DESC")
    List<KeyV3> checkSignatureCertificate(@Param("eeuser") Long eeuser, @Param("level") String level);

    @Query( value = "SELECT k FROM KeyV3 k WHERE k.eeuser = :eeuser AND k.status = 'ACT' AND k.level = :level ORDER BY k.id DESC")
    List<KeyV3> checkACTSignatureCertificate(@Param("eeuser") Long eeuser, @Param("level") String level);
}