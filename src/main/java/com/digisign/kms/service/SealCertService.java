package com.digisign.kms.service;

import com.digisign.kms.core.certificate.CertificateStatus;
import com.digisign.kms.dto.SealCertDTO;
import com.digisign.kms.model.Key;
import com.digisign.kms.model.KeyV3;
import com.digisign.kms.model.SealCert;
import com.digisign.kms.repository.SealCertRepository;
import com.digisign.kms.util.LogSystem;
import com.digisign.kms.vo.SealCertQueryVO;
import com.digisign.kms.vo.SealCertUpdateVO;
import com.digisign.kms.vo.SealCertVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
public class SealCertService {

    Certificate[] cert;
    X509Certificate cr;

    @Autowired
    private SealCertRepository sealCertRepository;

    public Long save(SealCertVO vO) {
        SealCert bean = new SealCert();
        BeanUtils.copyProperties(vO, bean);
        bean = sealCertRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        sealCertRepository.deleteById(id);
    }

    public void update(Long id, SealCertUpdateVO vO) {
        SealCert bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        sealCertRepository.save(bean);
    }

    public SealCertDTO getById(Long id) {
        SealCert original = requireOne(id);
        return toDTO(original);
    }

    public Page<SealCertDTO> query(SealCertQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private SealCertDTO toDTO(SealCert original) {
        SealCertDTO bean = new SealCertDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private SealCert requireOne(Long id) {
        return sealCertRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }

    public String checkSign(KeyV3 checkCert) throws Exception {
        CertificateStatus cReq = new CertificateStatus();
        LogSystem.info("Process Check certificate signer");
        if(checkCert != null)
        {
            cert = getCert(checkCert.getKey());
            cr = (X509Certificate) cert[0];
            LogSystem.info("CERT STATUS : " + checkCert.getStatus());

            if (checkCert.getStatus().equals("RVK")) {
                LogSystem.info("Status is RVK");
                return "05";
            } else {
                Date oneHourExpired = new Date(new Date().getTime() + (1000 * 60 * 60));
                if(checkCert.getStatus().equals("ACT"))
                {
                    LogSystem.info("Result is 00");
                    return "00";
                }
                else if (cr.getNotAfter().before(oneHourExpired))
                {
                    LogSystem.info("Status is Expired");
                    return "06";
                } else {
                    //Check to EJBCA
                    if (!cReq.checkStatus(cr.getIssuerDN().toString(), cr.getSerialNumber())) {
                        LogSystem.info("Status is Revoke");
                        return "07";
                    }
                }
                LogSystem.info("Result is 00");
                return "00";
            }
        }
        else
        {
            LogSystem.info("Result is 05");
            return "05";
        }

    }

    public Certificate[] getCert(String cert) throws CertificateException {
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream bis=new ByteArrayInputStream(java.util.Base64.getDecoder().decode(cert));
        Certificate c = fact.generateCertificate(bis);
        Certificate[] lCert=new Certificate[1];
        lCert[0]=c;

        return lCert;
    }
}
