package de.oth.tdanylenko.tdanbank.controller;

import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.entity.BusinessProducts;
import de.oth.tdanylenko.tdanbank.entity.User;
import de.oth.tdanylenko.tdanbank.repository.BusinessProductRepository;
import de.oth.tdanylenko.tdanbank.repository.UserRepository;
import de.oth.tdanylenko.tdanbank.service.AccountService;

import de.oth.tdanylenko.tdanbank.service.AccountServiceIF;
import de.oth.tdanylenko.tdanbank.service.BusinessProductServiceIF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class DashboardController {
    private static final Logger log = LogManager.getLogger(DashboardController.class);
    @Autowired
    private AccountService accountService;
    @Autowired
    private BusinessProductServiceIF businessProdService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BusinessProductRepository productsRepo;

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String getAccountPage(Model model) {
        model.addAttribute("allusers", userRepo.findAll());
        model.addAttribute("allproducts", productsRepo.findAll());
        return "dashboard";
    }
    @RequestMapping(value = "/dashboard/create/account", method = RequestMethod.GET)
    public String createAccount(Model model) {
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
       accountService.createBankAccount(iban, ownerUsername, balance, redirectAttributes);
        return "redirect:/dashboard/";
    }

   @RequestMapping(value ="/dashboard/create/user", method = RequestMethod.POST)
    public String createNewUser(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String username,
                                @RequestParam String password, @RequestParam String mail, @RequestParam String phone,
                                @RequestParam String dateOfBirth, @RequestParam String street,
                                @RequestParam String streetAddition, @RequestParam int houseNr, @RequestParam String city,
                                @RequestParam String zip, RedirectAttributes redirectAttributes) throws ParseException {
       SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
       Date date = dateformat.parse(dateOfBirth);
       accountService.createNewUserByManager(firstname, lastname, username, password, mail, phone, date, street, streetAddition, houseNr,
               city, zip, redirectAttributes);
       return "redirect:/dashboard/";
    }

    @GetMapping("/dashboard/manageuser/{username}")
    public String  getManageUser (@PathVariable String username, Model model){
        User user = accountService.loadUserByUsernameIgnoringCase(username);
        Account account = accountService.loadAccountByUsersUsernameCustomErrorHandling(user.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("bankaccount", account);
        return "manageuseraccount";
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
    @GetMapping(value = "/dashboard/closeaccount")
    public String getCloseAccount(Model model) {
        return "manageuseraccount";
    }
    @RequestMapping(value ="/dashboard/closeaccount", method = RequestMethod.POST)
    public String closeAccount(Model model, @RequestParam String usertodelete) {
        accountService.deleteAccount(usertodelete);
        return "redirect:/dashboard/manageuser/"+usertodelete;
    }

    @GetMapping("/dashboard/managebusinessproduct/{prodid}")
    public String postProducts(@PathVariable long prodid, Model model, RedirectAttributes redirectAttributes) throws IOException {
        BusinessProducts prod = productsRepo.getBusinessProductsById(prodid);
        boolean isSuccessful = businessProdService.postBusinessProductToTheMarketPlace(prodid, redirectAttributes);
        if(isSuccessful){
            System.out.println("Product " + prod.getName() + "was added to market place");
        } else {
            System.out.println("Partners service unavailable");
        }
        model.addAttribute("prodid", prodid);
        return "redirect:/dashboard/";
    }

    @RequestMapping(value ="/dashboard/back", method = RequestMethod.POST)
    public String redirectBackToDashboard ()
    {
        return "redirect:/dashboard/";
    }
}


