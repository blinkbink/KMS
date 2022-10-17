package com.digisign.kms.service;

import com.digisign.kms.dto.UserSealDTO;
import com.digisign.kms.model.UserSeal;
import com.digisign.kms.repository.UserSealRepository;
import com.digisign.kms.vo.UserSealQueryVO;
import com.digisign.kms.vo.UserSealUpdateVO;
import com.digisign.kms.vo.UserSealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserSealService {

    @Autowired
    private UserSealRepository userSealRepository;

    public Long save(UserSealVO vO) {
        UserSeal bean = new UserSeal();
        BeanUtils.copyProperties(vO, bean);
        bean = userSealRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        userSealRepository.deleteById(id);
    }

    public void update(Long id, UserSealUpdateVO vO) {
        UserSeal bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        userSealRepository.save(bean);
    }

    public UserSealDTO getById(Long id) {
        UserSeal original = requireOne(id);
        return toDTO(original);
    }

    public Page<UserSealDTO> query(UserSealQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private UserSealDTO toDTO(UserSeal original) {
        UserSealDTO bean = new UserSealDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private UserSeal requireOne(Long id) {
        return userSealRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
