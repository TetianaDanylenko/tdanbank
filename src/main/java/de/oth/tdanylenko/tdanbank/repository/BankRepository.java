package de.oth.tdanylenko.tdanbank.repository;

import de.oth.tdanylenko.tdanbank.entity.Bank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends CrudRepository<Bank, Long> {
    Bank getBankByName(String name);
    Bank getBankByBICIgnoreCase(String bic);
    Bank getBankByBIC(String bic);
}
