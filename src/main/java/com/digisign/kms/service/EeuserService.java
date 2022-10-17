package com.digisign.kms.service;

import com.digisign.kms.dto.EeuserDTO;
import com.digisign.kms.model.Eeuser;
import com.digisign.kms.repository.EeuserRepository;
import com.digisign.kms.vo.EeuserQueryVO;
import com.digisign.kms.vo.EeuserUpdateVO;
import com.digisign.kms.vo.EeuserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class EeuserService {

    @Autowired
    private EeuserRepository eeuserRepository;

    public Long save(EeuserVO vO) {
        Eeuser bean = new Eeuser();
        BeanUtils.copyProperties(vO, bean);
        bean = eeuserRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        eeuserRepository.deleteById(id);
    }

    public void update(Long id, EeuserUpdateVO vO) {
        Eeuser bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        eeuserRepository.save(bean);
    }

    public EeuserDTO getById(Long id) {
        Eeuser original = requireOne(id);
        return toDTO(original);
    }

    public Page<EeuserDTO> query(EeuserQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private EeuserDTO toDTO(Eeuser original) {
        EeuserDTO bean = new EeuserDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Eeuser requireOne(Long id) {
        return eeuserRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
