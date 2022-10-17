package com.digisign.kms.service;

import com.digisign.kms.dto.MitraDTO;
import com.digisign.kms.model.Mitra;
import com.digisign.kms.repository.MitraRepository;
import com.digisign.kms.vo.MitraQueryVO;
import com.digisign.kms.vo.MitraUpdateVO;
import com.digisign.kms.vo.MitraVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MitraService {

    @Autowired
    private MitraRepository mitraRepository;

    public Long save(MitraVO vO) {
        Mitra bean = new Mitra();
        BeanUtils.copyProperties(vO, bean);
        bean = mitraRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        mitraRepository.deleteById(id);
    }

    public void update(Long id, MitraUpdateVO vO) {
        Mitra bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        mitraRepository.save(bean);
    }

    public MitraDTO getById(Long id) {
        Mitra original = requireOne(id);
        return toDTO(original);
    }

    public Page<MitraDTO> query(MitraQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private MitraDTO toDTO(Mitra original) {
        MitraDTO bean = new MitraDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Mitra requireOne(Long id) {
        return mitraRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
