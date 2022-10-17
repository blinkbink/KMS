package com.digisign.kms.service;

import com.digisign.kms.dto.EmployeeSigningConfigDTO;
import com.digisign.kms.model.EmployeeSigningConfig;
import com.digisign.kms.repository.EmployeeSigningConfigRepository;
import com.digisign.kms.vo.EmployeeSigningConfigQueryVO;
import com.digisign.kms.vo.EmployeeSigningConfigUpdateVO;
import com.digisign.kms.vo.EmployeeSigningConfigVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class EmployeeSigningConfigService {

    @Autowired
    private EmployeeSigningConfigRepository employeeSigningConfigRepository;

    public Long save(EmployeeSigningConfigVO vO) {
        EmployeeSigningConfig bean = new EmployeeSigningConfig();
        BeanUtils.copyProperties(vO, bean);
        bean = employeeSigningConfigRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        employeeSigningConfigRepository.deleteById(id);
    }

    public void update(Long id, EmployeeSigningConfigUpdateVO vO) {
        EmployeeSigningConfig bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        employeeSigningConfigRepository.save(bean);
    }

    public EmployeeSigningConfigDTO getById(Long id) {
        EmployeeSigningConfig original = requireOne(id);
        return toDTO(original);
    }

    public Page<EmployeeSigningConfigDTO> query(EmployeeSigningConfigQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private EmployeeSigningConfigDTO toDTO(EmployeeSigningConfig original) {
        EmployeeSigningConfigDTO bean = new EmployeeSigningConfigDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private EmployeeSigningConfig requireOne(Long id) {
        return employeeSigningConfigRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
