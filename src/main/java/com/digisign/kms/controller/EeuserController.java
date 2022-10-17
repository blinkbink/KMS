package com.digisign.kms.controller;

import com.digisign.kms.dto.EeuserDTO;
import com.digisign.kms.service.EeuserService;
import com.digisign.kms.vo.EeuserQueryVO;
import com.digisign.kms.vo.EeuserUpdateVO;
import com.digisign.kms.vo.EeuserVO;
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
@RequestMapping("/eeuser")
public class EeuserController {

    @Autowired
    private EeuserService eeuserService;

    @PostMapping
    @ApiOperation("Save ")
    public String save(@Valid @RequestBody EeuserVO vO) {
        return eeuserService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        eeuserService.delete(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update ")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody EeuserUpdateVO vO) {
        eeuserService.update(id, vO);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve by ID ")
    public EeuserDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return eeuserService.getById(id);
    }

    @GetMapping
    @ApiOperation("Retrieve by query ")
    public Page<EeuserDTO> query(@Valid EeuserQueryVO vO) {
        return eeuserService.query(vO);
    }
}
