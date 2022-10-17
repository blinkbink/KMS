package com.digisign.kms.service;

import com.digisign.kms.core.certificate.CertificateStatus;
import com.digisign.kms.dto.KeyDTO;
import com.digisign.kms.model.Key;
import com.digisign.kms.model.KeyV3;
import com.digisign.kms.repository.KeyRepository;
import com.digisign.kms.util.LogSystem;
import com.digisign.kms.vo.KeyQueryVO;
import com.digisign.kms.vo.KeyUpdateVO;
import com.digisign.kms.vo.KeyVO;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class KeyService {

    Certificate[] cert;
    X509Certificate cr;

    @Autowired
    private KeyRepository keyRepository;

    public Long save(KeyVO vO) {
        Key bean = new Key();
        BeanUtils.copyProperties(vO, bean);
        bean = keyRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        keyRepository.deleteById(id);
    }

    public void update(Long id, KeyUpdateVO vO) {
        Key bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        keyRepository.save(bean);
    }

    public KeyDTO getById(Long id) {
        Key original = requireOne(id);
        return toDTO(original);
    }

    public List<KeyV3> getKeyACT(Long iduser, String level)
    {
        return keyRepository.checkACTSignatureCertificate(iduser, level);
    }

    public Page<KeyDTO> query(KeyQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private KeyDTO toDTO(Key original) {
        KeyDTO bean = new KeyDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Key requireOne(Long id) {
        return keyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }

    public Certificate[] getCert(String cert) throws CertificateException {
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream bis=new ByteArrayInputStream(java.util.Base64.getDecoder().decode(cert));
        Certificate c = fact.generateCertificate(bis);
        Certificate[] lCert=new Certificate[1];
        lCert[0]=c;

        return lCert;
    }

    public String checkSign(KeyV3 checkCert) throws Exception {
        CertificateStatus cReq = new CertificateStatus();
        LogSystem.info("Process Check certificate signer");
        if(checkCert != null)
        {
            cert = getCert(checkCert.getKey());
//            LogSystem.info("CERT : " + cert.toString());
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
}
