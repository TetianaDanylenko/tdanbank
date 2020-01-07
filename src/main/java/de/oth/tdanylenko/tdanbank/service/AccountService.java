package de.oth.tdanylenko.tdanbank.service;

import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.entity.Tan;
import de.oth.tdanylenko.tdanbank.entity.Transaction;
import de.oth.tdanylenko.tdanbank.entity.User;
import de.oth.tdanylenko.tdanbank.enums.AccountStatus;
import de.oth.tdanylenko.tdanbank.repository.AccountRepository;
import de.oth.tdanylenko.tdanbank.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("labresources")
public class AccountService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AccountRepository accountRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }

        log.info("loadUserByUsername() : {}", username);
        return user;
    }

    public Account createBankAccount(String iban, String accountOwner, double balance, RedirectAttributes redirectAttributes) {
        User owner = userRepo.getUserByUsername(accountOwner);
        String defaultCurrency = "EUR";
        List<Transaction> transactionList = new ArrayList<Transaction>();
        List<Tan> tanList = new ArrayList<Tan>();
        if (owner == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found");
        }
        if (accountRepo.findByIbanIgnoreCase(iban) != null) {
            redirectAttributes.addAttribute("bankAccountCreateFail", true);
            redirectAttributes.addFlashAttribute("bankAccountCreateFailReasonIban", "IBAN was already used!");
            return new Account(owner, balance, defaultCurrency, tanList, null, transactionList, AccountStatus.INACTIVE );
        }
        return accountRepo.save(new Account(owner, balance, defaultCurrency, tanList, iban, transactionList, AccountStatus.ACTIVE));
    }

    public Account updateBalance(long bankAccountId, double balance) {
        Account bankAccount = accountRepo.findById(bankAccountId).orElse(null);
        if (bankAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank Account not found");
        }
        bankAccount.setBalance(balance);
        return accountRepo.save(bankAccount);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    public void kundeAnlegen(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
}
