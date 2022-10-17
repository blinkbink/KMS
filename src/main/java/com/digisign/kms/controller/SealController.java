package com.digisign.kms.controller;

import com.digisign.kms.dto.SealDTO;
import com.digisign.kms.service.SealService;
import com.digisign.kms.vo.SealQueryVO;
import com.digisign.kms.vo.SealUpdateVO;
import com.digisign.kms.vo.SealVO;
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
@RequestMapping("/seal")
public class SealController {

    @Autowired
    private SealService sealService;

    @PostMapping
    @ApiOperation("Save ")
    public String save(@Valid @RequestBody SealVO vO) {
        return sealService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        sealService.delete(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update ")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody SealUpdateVO vO) {
        sealService.update(id, vO);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve by ID ")
    public SealDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return sealService.getById(id);
    }

    @GetMapping
    @ApiOperation("Retrieve by query ")
    public Page<SealDTO> query(@Valid SealQueryVO vO) {
        return sealService.query(vO);
    }
}
