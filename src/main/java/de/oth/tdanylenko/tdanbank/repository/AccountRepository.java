package de.oth.tdanylenko.tdanbank.repository;

import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account getAccountByUser(User user);
    Account findByAccIbanIgnoreCase(String iban);
    Account findAllByIdNot(long id);
    Account getAccountByUserUsername(String username);
    Account deleteAccountByUserUsername(String username);
    List<String> findByTanList(String val);
}
