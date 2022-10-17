package com.digisign.kms.controller;

import com.digisign.kms.dto.MitraSigningConfigDTO;
import com.digisign.kms.service.MitraSigningConfigService;
import com.digisign.kms.vo.MitraSigningConfigQueryVO;
import com.digisign.kms.vo.MitraSigningConfigUpdateVO;
import com.digisign.kms.vo.MitraSigningConfigVO;
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
@RequestMapping("/mitraSigningConfig")
public class MitraSigningConfigController {

    @Autowired
    private MitraSigningConfigService mitraSigningConfigService;

    @PostMapping
    @ApiOperation("Save ")
    public String save(@Valid @RequestBody MitraSigningConfigVO vO) {
        return mitraSigningConfigService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        mitraSigningConfigService.delete(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update ")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody MitraSigningConfigUpdateVO vO) {
        mitraSigningConfigService.update(id, vO);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve by ID ")
    public MitraSigningConfigDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return mitraSigningConfigService.getById(id);
    }

    @GetMapping
    @ApiOperation("Retrieve by query ")
    public Page<MitraSigningConfigDTO> query(@Valid MitraSigningConfigQueryVO vO) {
        return mitraSigningConfigService.query(vO);
    }
}
