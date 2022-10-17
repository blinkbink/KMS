package com.digisign.kms.controller;

import com.digisign.kms.dto.SealDocAccessDTO;
import com.digisign.kms.service.SealDocAccessService;
import com.digisign.kms.vo.SealDocAccessQueryVO;
import com.digisign.kms.vo.SealDocAccessUpdateVO;
import com.digisign.kms.vo.SealDocAccessVO;
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
@RequestMapping("/sealDocAccess")
public class SealDocAccessController {

    @Autowired
    private SealDocAccessService sealDocAccessService;

    @PostMapping
    @ApiOperation("Save ")
    public String save(@Valid @RequestBody SealDocAccessVO vO) {
        return sealDocAccessService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        sealDocAccessService.delete(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update ")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody SealDocAccessUpdateVO vO) {
        sealDocAccessService.update(id, vO);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve by ID ")
    public SealDocAccessDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return sealDocAccessService.getById(id);
    }

    @GetMapping
    @ApiOperation("Retrieve by query ")
    public Page<SealDocAccessDTO> query(@Valid SealDocAccessQueryVO vO) {
        return sealDocAccessService.query(vO);
    }
}
