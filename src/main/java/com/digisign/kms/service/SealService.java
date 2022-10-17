package com.digisign.kms.service;

import com.digisign.kms.dto.SealDTO;
import com.digisign.kms.model.Seal;
import com.digisign.kms.repository.SealRepository;
import com.digisign.kms.vo.SealQueryVO;
import com.digisign.kms.vo.SealUpdateVO;
import com.digisign.kms.vo.SealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SealService {

    @Autowired
    private SealRepository sealRepository;

    public Long save(SealVO vO) {
        Seal bean = new Seal();
        BeanUtils.copyProperties(vO, bean);
        bean = sealRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        sealRepository.deleteById(id);
    }

    public void update(Long id, SealUpdateVO vO) {
        Seal bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        sealRepository.save(bean);
    }

    public SealDTO getById(Long id) {
        Seal original = requireOne(id);
        return toDTO(original);
    }

    public Page<SealDTO> query(SealQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private SealDTO toDTO(Seal original) {
        SealDTO bean = new SealDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Seal requireOne(Long id) {
        return sealRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
