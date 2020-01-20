package de.oth.tdanylenko.tdanbank.service;

import de.oth.tdanylenko.tdanbank.dto.TransactionDTO;
import de.oth.tdanylenko.tdanbank.exceptions.TransactionServiceException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface TransactionServiceIF {
    @Transactional
    void transferMoney(String to, String from, double amount, String tan, String message, RedirectAttributes redirectAttributes)
            throws TransactionServiceException;
    @Transactional
    TransactionDTO directDebit (TransactionDTO transaction) throws TransactionServiceException;
}
