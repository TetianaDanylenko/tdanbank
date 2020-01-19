package de.oth.tdanylenko.tdanbank.service;

import de.oth.tdanylenko.tdanbank.dto.TransactionDTO;
import de.oth.tdanylenko.tdanbank.exceptions.TransactionServiceException;

public interface TransactionServiceIF {
    TransactionDTO transferMoney(TransactionDTO transaction) throws TransactionServiceException;
    TransactionDTO directDebit (TransactionDTO transaction) throws TransactionServiceException;
    String viewTransactionStatus(TransactionDTO transaction) throws TransactionServiceException;
}
