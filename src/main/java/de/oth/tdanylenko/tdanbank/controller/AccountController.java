package de.oth.tdanylenko.tdanbank.controller;

import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.repository.AccountRepository;
import de.oth.tdanylenko.tdanbank.repository.UserRepository;
import de.oth.tdanylenko.tdanbank.service.AccountService;
import de.oth.tdanylenko.tdanbank.service.TransaсtionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;


@Controller
public class AccountController {
    private static final Logger log = LogManager.getLogger(AccountController.class);
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransaсtionService transaсtionService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AccountRepository accountRepo;
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String getAccountPage(Model model) {
        log.info("test");
        return "account";
    }

    @GetMapping("/account/{username}")
    public String viewUserAccount(@PathVariable String username, Model model) {
        log.info("entered accountcontroller");
        Account bankAccount = accountRepo.getAccountByUserUsername(username);
        if (bankAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        model.addAttribute("bankAccount", bankAccount);
        model.addAttribute("transactionHistory", bankAccount.getTransactionHistory());
        model.addAttribute("accountsiban", bankAccount.getiban());
        return "account";
    }
}
