package com.digisign.kms.controller;

import com.digisign.kms.service.KeyService;
import com.digisign.kms.vo.KeyQueryVO;
import com.digisign.kms.vo.KeyUpdateVO;
import com.digisign.kms.dto.KeyDTO;

import com.digisign.kms.vo.KeyVO;
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
@RequestMapping("/key")
public class KeyController {

    @Autowired
    private KeyService keyService;

    @PostMapping
    @ApiOperation("Save ")
    public String save(@Valid @RequestBody KeyVO vO) {
        return keyService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        keyService.delete(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update ")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody KeyUpdateVO vO) {
        keyService.update(id, vO);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve by ID ")
    public KeyDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return keyService.getById(id);
    }

    @GetMapping
    @ApiOperation("Retrieve by query ")
    public Page<KeyDTO> query(@Valid KeyQueryVO vO) {
        return keyService.query(vO);
    }
}
