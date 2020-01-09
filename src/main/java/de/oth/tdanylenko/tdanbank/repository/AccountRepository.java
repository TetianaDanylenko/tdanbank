package de.oth.tdanylenko.tdanbank.repository;

import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account getAccountByUser(Optional<User> user);
    Account findByIbanIgnoreCase(String iban);
    Account findAllByIdNot(long id);
    Account getAccountByUserUsername(String username);
}
