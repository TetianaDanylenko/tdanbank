package de.oth.tdanylenko.tdanbank.repository;

import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AccountRepository extends CrudRepository<Account, Long> {
    Account getAccountByUser(User user);
    Account findByAccIbanIgnoreCase(String iban);
    Account getAccountByUserUsername(String username);
    Account deleteAccountByUserId(Long userid);
}
