package de.oth.tdanylenko.tdanbank.controller;

import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.repository.UserRepository;
import de.oth.tdanylenko.tdanbank.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DashboardController {
    private static final Logger log = LogManager.getLogger(DashboardController.class);
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepo;
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String getAccountPage(Model model) {
        log.info("dashboard");
        return "dashboard";
    }
    @RequestMapping(value = "/dashboard/update/user", method = RequestMethod.GET)
    public String updateUser(Model model) {
        log.info("updateUser");
        return "updateuser";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/dashboard/create/account", method = RequestMethod.POST)
    public String createBankAccount(@RequestParam String iban, @RequestParam String ownerUsername, @RequestParam double balance, RedirectAttributes redirectAttributes) {
        if (iban.trim().isEmpty() || ownerUsername.isEmpty()) {
            throw new IllegalArgumentException("IBAN or owner cannot be empty!");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative!");
        }
        Account bankAccount = accountService.createBankAccount(iban, ownerUsername, balance, redirectAttributes);
        return "redirect:/account/" + bankAccount.getId();
    }
}
