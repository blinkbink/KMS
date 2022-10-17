package com.digisign.kms.controller;

import com.digisign.kms.dto.EmployeeSigningConfigDTO;
import com.digisign.kms.service.EmployeeSigningConfigService;
import com.digisign.kms.vo.EmployeeSigningConfigQueryVO;
import com.digisign.kms.vo.EmployeeSigningConfigUpdateVO;
import com.digisign.kms.vo.EmployeeSigningConfigVO;
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
@RequestMapping("/employeeSigningConfig")
public class EmployeeSigningConfigController {

    @Autowired
    private EmployeeSigningConfigService employeeSigningConfigService;

    @PostMapping
    @ApiOperation("Save ")
    public String save(@Valid @RequestBody EmployeeSigningConfigVO vO) {
        return employeeSigningConfigService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        employeeSigningConfigService.delete(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update ")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody EmployeeSigningConfigUpdateVO vO) {
        employeeSigningConfigService.update(id, vO);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve by ID ")
    public EmployeeSigningConfigDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return employeeSigningConfigService.getById(id);
    }

    @GetMapping
    @ApiOperation("Retrieve by query ")
    public Page<EmployeeSigningConfigDTO> query(@Valid EmployeeSigningConfigQueryVO vO) {
        return employeeSigningConfigService.query(vO);
    }
}
