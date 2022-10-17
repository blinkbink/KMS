package com.digisign.kms.controller;

import com.digisign.kms.dto.UserSealDTO;
import com.digisign.kms.service.UserSealService;
import com.digisign.kms.vo.UserSealQueryVO;
import com.digisign.kms.vo.UserSealUpdateVO;
import com.digisign.kms.vo.UserSealVO;
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
@RequestMapping("/userSeal")
public class UserSealController {

    @Autowired
    private UserSealService userSealService;

    @PostMapping
    @ApiOperation("Save ")
    public String save(@Valid @RequestBody UserSealVO vO) {
        return userSealService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        userSealService.delete(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update ")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody UserSealUpdateVO vO) {
        userSealService.update(id, vO);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve by ID ")
    public UserSealDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return userSealService.getById(id);
    }

    @GetMapping
    @ApiOperation("Retrieve by query ")
    public Page<UserSealDTO> query(@Valid UserSealQueryVO vO) {
        return userSealService.query(vO);
    }
}
