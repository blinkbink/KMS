package com.digisign.kms.controller;

import com.digisign.kms.dto.UserdataDTO;
import com.digisign.kms.service.UserdataService;
import com.digisign.kms.vo.UserdataQueryVO;
import com.digisign.kms.vo.UserdataUpdateVO;
import com.digisign.kms.vo.UserdataVO;
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
@RequestMapping("/userdata")
public class UserdataController {

    @Autowired
    private UserdataService userdataService;

    @PostMapping
    @ApiOperation("Save ")
    public String save(@Valid @RequestBody UserdataVO vO) {
        return userdataService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        userdataService.delete(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update ")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody UserdataUpdateVO vO) {
        userdataService.update(id, vO);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve by ID ")
    public UserdataDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return userdataService.getById(id);
    }

    @GetMapping
    @ApiOperation("Retrieve by query ")
    public Page<UserdataDTO> query(@Valid UserdataQueryVO vO) {
        return userdataService.query(vO);
    }
}
