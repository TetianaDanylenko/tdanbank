package de.oth.tdanylenko.tdanbank.controller;

import de.oth.tdanylenko.tdanbank.repository.UserRepository;
import de.oth.tdanylenko.tdanbank.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.function.Supplier;

@Controller
public class AccountController {
    private static final Logger log = LogManager.getLogger(AccountController.class);
    @Autowired
    private AccountService userService;
    @Autowired
    private UserRepository userRepo;
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String getAccountPage(Model model) {
        log.info("test");
        return "account";
    }
}
