package com.digisign.kms.controller;

import com.digisign.kms.dto.SealCertDTO;
import com.digisign.kms.service.SealCertService;
import com.digisign.kms.vo.SealCertQueryVO;
import com.digisign.kms.vo.SealCertUpdateVO;
import com.digisign.kms.vo.SealCertVO;
import com.sun.istack.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "")
@Validated
@RestController
@RequestMapping("/sealCert")
public class SealCertController {

    @Autowired
    private SealCertService sealCertService;

    @PostMapping
    @ApiOperation("Save ")
    public String save(@Valid @RequestBody SealCertVO vO) {
        return sealCertService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        sealCertService.delete(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update ")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody SealCertUpdateVO vO) {
        sealCertService.update(id, vO);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve by ID ")
    public SealCertDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return sealCertService.getById(id);
    }

    @GetMapping
    @ApiOperation("Retrieve by query ")
    public Page<SealCertDTO> query(@Valid SealCertQueryVO vO) {
        return sealCertService.query(vO);
    }
}
