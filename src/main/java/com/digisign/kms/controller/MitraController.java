package com.digisign.kms.controller;

import com.digisign.kms.dto.MitraDTO;
import com.digisign.kms.service.MitraService;
import com.digisign.kms.vo.MitraQueryVO;
import com.digisign.kms.vo.MitraUpdateVO;
import com.digisign.kms.vo.MitraVO;
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
@RequestMapping("/mitra")
public class MitraController {

    @Autowired
    private MitraService mitraService;

    @PostMapping
    @ApiOperation("Save ")
    public String save(@Valid @RequestBody MitraVO vO) {
        return mitraService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        mitraService.delete(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update ")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody MitraUpdateVO vO) {
        mitraService.update(id, vO);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve by ID ")
    public MitraDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return mitraService.getById(id);
    }

    @GetMapping
    @ApiOperation("Retrieve by query ")
    public Page<MitraDTO> query(@Valid MitraQueryVO vO) {
        return mitraService.query(vO);
    }
}
