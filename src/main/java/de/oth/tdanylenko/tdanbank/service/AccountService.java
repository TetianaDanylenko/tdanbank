package de.oth.tdanylenko.tdanbank.service;

import de.oth.tdanylenko.tdanbank.entity.*;
import de.oth.tdanylenko.tdanbank.enums.AccountStatus;
import de.oth.tdanylenko.tdanbank.repository.AccountRepository;
import de.oth.tdanylenko.tdanbank.repository.RolesRepository;
import de.oth.tdanylenko.tdanbank.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Service
@Qualifier("labresources")
public class AccountService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AccountRepository accountRepo;
@Autowired
private RolesRepository rolesRepo;
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
        List<Transaction> transactionList = new ArrayList<>();
        if (owner == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found");
        }
        if(accountRepo.getAccountByUser(owner) != null) {
            redirectAttributes.addAttribute("bankAccountCreateFail", true);
            redirectAttributes.addFlashAttribute("bankAccountCreateFailReasonUserALreadyHasAnAccount", "user already has an account");
            return new Account(null, balance, defaultCurrency, null, null, transactionList, AccountStatus.INACTIVE );
        }
        List<String> tanList = this.generateTanListForAccount();
        //TODO check if customer already has an account
        if (accountRepo.findByAccIbanIgnoreCase(iban) != null) {
            redirectAttributes.addAttribute("bankAccountCreateFail", true);
            redirectAttributes.addFlashAttribute("bankAccountCreateFailReasonIban", "IBAN was already used!");
            return new Account(owner, balance, defaultCurrency, tanList, null, transactionList, AccountStatus.INACTIVE );
        }
        return accountRepo.save(new Account(owner, balance, defaultCurrency, tanList, iban, transactionList, AccountStatus.ACTIVE));
    }
    public void createNewUserByManager(String firstname, String lastname, String username,
                                          String password, String mail, String phone,
                                          Date dateOfBirth, String street,
                                          String streetAddition, int houseNr, String city, String zip, RedirectAttributes redirectAttributes) {

        Roles role = new Roles(RoleTypes.ROLE_USER);
        Address address= new Address( street, streetAddition, houseNr, city, zip);
        User user = new User (firstname, lastname, username, password, address, Arrays.asList(role), mail, phone, dateOfBirth);
        if (userRepo.findByUsernameIgnoreCase(username) != null) {
            redirectAttributes.addAttribute("newUsercreateFails", true);
            redirectAttributes.addFlashAttribute("newUsercreateFailsReasonUserAlreadyExists", "User with this username already exists!");
        } else {
            createCustomer(user);
        }
    }
//TODO
    public void changeRoleToManagers (String username,  RedirectAttributes redirectAttributes) {
        User user = userRepo.getUserByUsername(username);
        if (user == null) {
            redirectAttributes.addAttribute("changeRoleFails", true);
            redirectAttributes.addFlashAttribute("changeRoleFailsUserDoesntExist", "User with this username doesnt exist!");
        } else {
            log.info("loadUserByUsername() : {}", username);
            user.setRoles(Arrays.asList(rolesRepo.findByName(RoleTypes.ROLE_MANAGER)));
            log.info("loadUserByUsername() : {}", rolesRepo.findByName(RoleTypes.ROLE_MANAGER));
            userRepo.save(userRepo.getUserByUsername(username));

            log.info("loadUserByUsername() : {}", user.getUserID());
        }
}
    public  List<String> generateTanListForAccount() {
        List<String> tanList = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            tanList.add(RandomStringUtils.randomNumeric(10));
        }
        return tanList;
    }
   @Transactional
    public Account updateBalance(long bankAccountId, double balance) {
        Account bankAccount = accountRepo.findById(bankAccountId).orElse(null);
        if (bankAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank Account not found");
        }
        bankAccount.setBalance(balance);
        return accountRepo.save(bankAccount);
    }

    @Transactional
    public Boolean updateUserInfoByAdmin(String username, String newfirstname, String newlastname, String newmail, String newphone, String newstreet,
                                         String newstreetAddition, int newhouseNr, String newcity,
                                         String newzip, RedirectAttributes redirectAttributes) {
        User user = userRepo.getUserByUsername(username);
        log.info(user.getPassword());
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if(!user.getFirstName().equals(newfirstname)) {
            user.setFirstName(newfirstname);
        }
        if(!user.getLastName().equals(newlastname)) {
            user.setLastName(newlastname);
        }
        if(!user.getMail().equals(newmail)) {
            user.setMail(newmail);
        }
        if(!user.getPhone().equals(newphone)) {
            user.setPhone(newphone);
        }
        if(!user.getAddress().getStreet().equals(newstreet)) {
            user.getAddress().setStreet(newstreet);
        }
        if(!user.getAddress().getStreetAddition().equals(newstreetAddition)) {
            user.getAddress().setStreetAddition(newstreetAddition);
        }
        if(!user.getAddress().getCity().equals(newcity)) {
            user.getAddress().setCity(newcity);
        }
        if(!(user.getAddress().getHouseNr() == newhouseNr)) {
            user.getAddress().setHouseNr(newhouseNr);
        }
        if(!user.getAddress().getZip().equals(newzip)) {
            user.getAddress().setZip(newzip);
        }
        userRepo.save(user);
        return true;
    }

    @Transactional
    public void deleteAccount(String username) {
        accountRepo.deleteAccountByUserUsername(username);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Transactional
    public void createCustomer(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
}
