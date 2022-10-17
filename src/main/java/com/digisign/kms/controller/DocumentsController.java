package com.digisign.kms.controller;

import com.digisign.kms.dto.DocumentsDTO;
import com.digisign.kms.service.DocumentsService;
import com.digisign.kms.vo.DocumentsQueryVO;
import com.digisign.kms.vo.DocumentsUpdateVO;
import com.digisign.kms.vo.DocumentsVO;
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
@RequestMapping("/documents")
public class DocumentsController {

    @Autowired
    private DocumentsService documentsService;

    @PostMapping
    @ApiOperation("Save ")
    public String save(@Valid @RequestBody DocumentsVO vO) {
        return documentsService.save(vO).toString();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public void delete(@Valid @NotNull @PathVariable("id") Long id) {
        documentsService.delete(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update ")
    public void update(@Valid @NotNull @PathVariable("id") Long id,
                       @Valid @RequestBody DocumentsUpdateVO vO) {
        documentsService.update(id, vO);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve by ID ")
    public DocumentsDTO getById(@Valid @NotNull @PathVariable("id") Long id) {
        return documentsService.getById(id);
    }

    @GetMapping
    @ApiOperation("Retrieve by query ")
    public Page<DocumentsDTO> query(@Valid DocumentsQueryVO vO) {
        return documentsService.query(vO);
    }
}
