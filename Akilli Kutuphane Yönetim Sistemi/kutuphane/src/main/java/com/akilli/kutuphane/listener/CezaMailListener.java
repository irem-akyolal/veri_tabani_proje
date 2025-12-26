package com.akilli.kutuphane.listener;

import com.akilli.kutuphane.Repository.CezaRepository;
import com.akilli.kutuphane.Service.MailService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
public class CezaMailListener {

    private final CezaRepository cezaRepository;
    private final MailService mailService;

    public CezaMailListener(CezaRepository cezaRepository,
                            MailService mailService) {
        this.cezaRepository = cezaRepository;
        this.mailService = mailService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void cezaMail(Long oduncId) {
        cezaRepository.findByOduncAlma_OduncId(oduncId)
                .ifPresent(mailService::sendCezaMail);
    }
}

