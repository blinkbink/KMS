package com.digisign.kms.service;

import com.digisign.kms.dto.SealDocAccessDTO;
import com.digisign.kms.model.SealDocAccess;
import com.digisign.kms.repository.SealDocAccessRepository;
import com.digisign.kms.vo.SealDocAccessQueryVO;
import com.digisign.kms.vo.SealDocAccessUpdateVO;
import com.digisign.kms.vo.SealDocAccessVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SealDocAccessService {

    @Autowired
    private SealDocAccessRepository sealDocAccessRepository;

    public Long save(SealDocAccessVO vO) {
        SealDocAccess bean = new SealDocAccess();
        BeanUtils.copyProperties(vO, bean);
        bean = sealDocAccessRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        sealDocAccessRepository.deleteById(id);
    }

    public void update(Long id, SealDocAccessUpdateVO vO) {
        SealDocAccess bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        sealDocAccessRepository.save(bean);
    }

    public SealDocAccessDTO getById(Long id) {
        SealDocAccess original = requireOne(id);
        return toDTO(original);
    }

    public Page<SealDocAccessDTO> query(SealDocAccessQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private SealDocAccessDTO toDTO(SealDocAccess original) {
        SealDocAccessDTO bean = new SealDocAccessDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private SealDocAccess requireOne(Long id) {
        return sealDocAccessRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
