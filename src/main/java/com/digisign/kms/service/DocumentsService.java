package com.digisign.kms.service;

import com.digisign.kms.dto.DocumentsDTO;
import com.digisign.kms.model.Documents;
import com.digisign.kms.repository.DocumentsRepository;
import com.digisign.kms.vo.DocumentsQueryVO;
import com.digisign.kms.vo.DocumentsUpdateVO;
import com.digisign.kms.vo.DocumentsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Service
public class DocumentsService {

    @Autowired
    private DocumentsRepository documentsRepository;

    public Long save(@Valid DocumentsVO vO) {
        Documents bean = new Documents();
        BeanUtils.copyProperties(vO, bean);
        bean = documentsRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        documentsRepository.deleteById(id);
    }

    public void update(Long id, @Valid DocumentsUpdateVO vO) {
        Documents bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        documentsRepository.save(bean);
    }

    public DocumentsDTO getById(Long id) {
        Documents original = requireOne(id);
        return toDTO(original);
    }

    public Page<DocumentsDTO> query(@Valid DocumentsQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private DocumentsDTO toDTO(Documents original) {
        DocumentsDTO bean = new DocumentsDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Documents requireOne(Long id) {
        return documentsRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
