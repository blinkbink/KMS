package com.digisign.kms.service;

import com.digisign.kms.dto.UserdataDTO;
import com.digisign.kms.model.Userdata;
import com.digisign.kms.repository.UserdataRepository;
import com.digisign.kms.vo.UserdataQueryVO;
import com.digisign.kms.vo.UserdataUpdateVO;
import com.digisign.kms.vo.UserdataVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserdataService {

    @Autowired
    private UserdataRepository userdataRepository;

    public Long save(UserdataVO vO) {
        Userdata bean = new Userdata();
        BeanUtils.copyProperties(vO, bean);
        bean = userdataRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        userdataRepository.deleteById(id);
    }

    public void update(Long id, UserdataUpdateVO vO) {
        Userdata bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        userdataRepository.save(bean);
    }

    public UserdataDTO getById(Long id) {
        Userdata original = requireOne(id);
        return toDTO(original);
    }

    public Page<UserdataDTO> query(UserdataQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private UserdataDTO toDTO(Userdata original) {
        UserdataDTO bean = new UserdataDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Userdata requireOne(Long id) {
        return userdataRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
