package de.oth.tdanylenko.tdanbank.controller;

import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.entity.User;
import de.oth.tdanylenko.tdanbank.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AccountController {
    private static final Logger log = LogManager.getLogger(AccountController.class);
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String getAccountPage(Model model) {
        return "account";
    }

    @GetMapping("/account/{username}")
    public String viewUserAccount(@PathVariable String username, Model model) {
        Account bankAccount = accountService.loadAccountByUsersUsernameCustomErrorHandling(username);
        if (bankAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        model.addAttribute("bankAccount", bankAccount);
        model.addAttribute("transactionHistory", bankAccount.getTransactionHistory());
        model.addAttribute("accountsiban", bankAccount.getiban());
        model.addAttribute("tanlist", bankAccount.getTanList());
        return "account";
    }
    @RequestMapping(value ="/account/updateuserinformation", method = RequestMethod.POST)
    public String manageUserInformation ( @RequestParam String userToUpdate,
                                          @RequestParam String phone, @RequestParam int houseNr, @RequestParam String street,
                                          @RequestParam String streetaddition, @RequestParam String zip, @RequestParam String city,
                                          @RequestParam String passwordtoupdate, RedirectAttributes redirectAttributes)
    {
        accountService.updateUserInfoByUser (userToUpdate, phone, street, streetaddition, houseNr, city, zip, passwordtoupdate, redirectAttributes);
        return "redirect:/account/manage/userinformation/" + userToUpdate;
    }

    @GetMapping("/account/manage/userinformation/{username}")
    public String  manageOwnInfo (@PathVariable String username, Model model){
        User user = accountService.loadUserByUsernameIgnoringCase(username);
        //user can be loaded even if he/her doesnt have an account
        Account account = accountService.loadAccountByUsersUsernameCustomErrorHandling(user.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("bankaccount", account);
        return "manageownusersaccount";
    }
    @RequestMapping(value ="/account/back", method = RequestMethod.POST)
    public String redirectBackToUser (@RequestParam String usertoreturn)
    {
        return "redirect:/account/" + usertoreturn;
    }
}
