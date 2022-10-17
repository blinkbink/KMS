package com.digisign.kms.service;

import com.digisign.kms.dto.MitraSigningConfigDTO;
import com.digisign.kms.model.MitraSigningConfig;
import com.digisign.kms.repository.MitraSigningConfigRepository;
import com.digisign.kms.vo.MitraSigningConfigQueryVO;
import com.digisign.kms.vo.MitraSigningConfigUpdateVO;
import com.digisign.kms.vo.MitraSigningConfigVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MitraSigningConfigService {

    @Autowired
    private MitraSigningConfigRepository mitraSigningConfigRepository;

    public Long save(MitraSigningConfigVO vO) {
        MitraSigningConfig bean = new MitraSigningConfig();
        BeanUtils.copyProperties(vO, bean);
        bean = mitraSigningConfigRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        mitraSigningConfigRepository.deleteById(id);
    }

    public void update(Long id, MitraSigningConfigUpdateVO vO) {
        MitraSigningConfig bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        mitraSigningConfigRepository.save(bean);
    }

    public MitraSigningConfigDTO getById(Long id) {
        MitraSigningConfig original = requireOne(id);
        return toDTO(original);
    }

    public Page<MitraSigningConfigDTO> query(MitraSigningConfigQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private MitraSigningConfigDTO toDTO(MitraSigningConfig original) {
        MitraSigningConfigDTO bean = new MitraSigningConfigDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private MitraSigningConfig requireOne(Long id) {
        return mitraSigningConfigRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
