package com.digisign.kms.repository;

import com.digisign.kms.model.DocAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocAccessRepository extends JpaRepository<DocAccess, Long>, JpaSpecificationExecutor<DocAccess> {

    @Query( value = "SELECT da FROM DocAccess da, Documents d WHERE da.documentBean.id =d.id and da.eeuserBean.id = :usersign and da.id = :idDocAccess and (da.type='sign' or da.type='initials') and da.flag = false")
    DocAccess getUserSignDA(@Param("usersign") Long usersign, @Param("idDocAccess") Long idDocAccess);

    @Query( value = "SELECT da FROM DocAccess da, Documents d WHERE da.documentBean.id =d.id and da.eeuserBean.id = :usersign and da.id = :idDocAccess and da.type='seal' and da.flag = false")
    DocAccess getUserSealDA(@Param("usersign") Long usersign, @Param("idDocAccess") Long idDocAccess);

    @Query( value = "SELECT da FROM DocAccess da, Documents d WHERE da.documentBean.id =d.id and da.eeuserBean.id = :usersign and da.documentBean.id = :document and (da.type='sign' or da.type='initials') and da.flag = false")
    List<DocAccess> getUserSign(@Param("usersign") Long usersign, @Param("document") Long document);

    @Query( value = "SELECT da FROM DocAccess da, Documents d WHERE da.documentBean.id =d.id and da.eeuserBean.id = :usersign and da.documentBean.id = :document and da.type='seal' and da.flag = false")
    List<DocAccess> getUserSignSeal(@Param("usersign") Long usersign, @Param("document") Long document);

    @Query( value = "SELECT da FROM DocAccess da WHERE  da.documentBean.id = :document")
    Optional <List<DocAccess>> getByDocument(@Param("document") Long document);

    @Query( value = "SELECT da FROM DocAccess da, Documents d WHERE da.documentBean.id =d.id and da.eeuserBean.id = :usersign and da.id = :idDocAccess and da.type='seal'")
    DocAccess getUserSealAllDA(@Param("usersign") Long usersign, @Param("idDocAccess") Long idDocAccess);
}
