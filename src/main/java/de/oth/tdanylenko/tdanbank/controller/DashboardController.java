package de.oth.tdanylenko.tdanbank.controller;

import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.entity.User;
import de.oth.tdanylenko.tdanbank.exceptions.AccountNotFoundException;
import de.oth.tdanylenko.tdanbank.exceptions.UserNotFoundException;
import de.oth.tdanylenko.tdanbank.repository.AccountRepository;
import de.oth.tdanylenko.tdanbank.repository.UserRepository;
import de.oth.tdanylenko.tdanbank.service.AccountService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class DashboardController {
    private static final Logger log = LogManager.getLogger(DashboardController.class);
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AccountRepository accountRepo;
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String getAccountPage(Model model) {
        log.info("dashboard");
        model.addAttribute("allusers", userRepo.findAll());
        return "dashboard";
    }
    @RequestMapping(value = "/dashboard/create/account", method = RequestMethod.GET)
    public String createAccount(Model model) {
        log.info("create");
        return "manageuseraccount";
    }

    @RequestMapping(value = "/dashboard/create/account", method = RequestMethod.POST)
    public String createBankAccount(@RequestParam String iban, @RequestParam String ownerUsername, @RequestParam double balance, RedirectAttributes redirectAttributes) {
        if (iban.trim().isEmpty() || ownerUsername.isEmpty()) {
            throw new IllegalArgumentException("IBAN or owner cannot be empty!");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative!");
        }
        Account bankAccount = accountService.createBankAccount(iban, ownerUsername, balance, redirectAttributes);
        log.info("created");
        log.info(iban);
        return "redirect:/dashboard/";
    }

   @RequestMapping(value ="/dashboard/create/user", method = RequestMethod.POST)
    public String createNewUser(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String username,
                                @RequestParam String password, @RequestParam String mail, @RequestParam String phone,
                                @RequestParam String dateOfBirth, @RequestParam String street,
                                @RequestParam String streetAddition, @RequestParam int houseNr, @RequestParam String city,
                                @RequestParam String zip, RedirectAttributes redirectAttributes) throws ParseException {
       log.info(dateOfBirth);
       SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
       Date date = dateformat.parse(dateOfBirth);
       accountService.createNewUserByManager(firstname, lastname, username, password, mail, phone, date, street, streetAddition, houseNr,
               city, zip, redirectAttributes);

       log.info("created");
       return "redirect:/dashboard/";
    }

    @GetMapping("/dashboard/manageuser/{username}")
    public String  tst (@PathVariable String username, Model model){
        log.info("to manage");
        User user = userRepo.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        Account account = accountRepo.getAccountByUserUsername(user.getUsername());
        if(account == null) {
            log.info("account is null");
        }
        log.info(user.getPassword());
        log.info(user.getRoles());
        model.addAttribute("user", user);
        model.addAttribute("bankaccount", account);
        return "manageuseraccount";

    }
    @GetMapping("/dashboard/updateuserinformation")
    public String tst2 (Model model) {
        log.info(model);
        return "manageuseraccount";
    }

    @RequestMapping(value ="/dashboard/userroletoadmin", method = RequestMethod.POST)
    public String changeUsersRoleToManager ( @RequestParam String usertoadmin,
                                          RedirectAttributes redirectAttributes)
    { log.info("test" + usertoadmin);
        accountService.changeRoleToManagers (usertoadmin, redirectAttributes);
        log.info("test" + usertoadmin);
        return "redirect:/dashboard/manageuser/" + usertoadmin;
    }
    @RequestMapping(value ="/dashboard/updateuserinformation", method = RequestMethod.POST)
    public String manageUserInformation ( @RequestParam String userToUpdate,
                                        @RequestParam String fname, @RequestParam String lname,
                                          @RequestParam String phone, @RequestParam int houseNr, @RequestParam String street,
                                          @RequestParam String streetaddition, @RequestParam String zip, @RequestParam String city,
                                          @RequestParam String mail, RedirectAttributes redirectAttributes)
    {
     accountService.updateUserInfoByAdmin (userToUpdate, fname, lname, mail, phone, street, streetaddition, houseNr, city, zip, redirectAttributes);
     return "redirect:/dashboard/manageuser/" + userToUpdate;
    }
/*    @GetMapping(value = "/dashboard/closeaccount")
    public String tst3(Model model) {
        log.info("enter update zone");
        return "manageuseraccount";
    }*/
    @RequestMapping(value ="/dashboard/closeaccount", method = RequestMethod.POST)
    public String closeAccount(Model model, @RequestParam String usertodelete) {
        log.info("to manage post2" + usertodelete);
        accountService.deleteAccount(usertodelete);
        return "redirect:/dashboard/manageuser/" + usertodelete;
    }
}


