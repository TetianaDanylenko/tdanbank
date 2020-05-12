package de.oth.tdanylenko.tdanbank.service;

import de.oth.tdanylenko.tdanbank.entity.*;
import de.oth.tdanylenko.tdanbank.enums.AccountStatus;
import de.oth.tdanylenko.tdanbank.exceptions.AccountNotFoundException;
import de.oth.tdanylenko.tdanbank.exceptions.BankNotFoundException;
import de.oth.tdanylenko.tdanbank.exceptions.RolesException;
import de.oth.tdanylenko.tdanbank.exceptions.UserNotFoundException;
import de.oth.tdanylenko.tdanbank.repository.AccountRepository;
import de.oth.tdanylenko.tdanbank.repository.BankRepository;
import de.oth.tdanylenko.tdanbank.repository.RolesRepository;
import de.oth.tdanylenko.tdanbank.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Service
@Scope("singleton")
@Qualifier("userdetails")
public class AccountService implements UserDetailsService, AccountServiceIF {
    private static final String CURRENCY = "EUR";
    private static final String BANKNAME = "TDANBank";
    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private RolesRepository rolesRepo;
    @Autowired
    private BankRepository bankRepo;
    @Autowired
    public AccountService(UserRepository userRepo, AccountRepository accountRepo, RolesRepository rolesRepo, BankRepository bankRepo) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
        this.rolesRepo = rolesRepo;
        this.bankRepo = bankRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        return user;
    }
    @Override
    public User loadUserByUsernameIgnoringCase(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        return user;
    }
    @Override
    public Account loadAccountByUser(User user) throws AccountNotFoundException {
        Account account = accountRepo.getAccountByUser(user);
        if(account == null){
            throw new AccountNotFoundException();
        }
        return account;
    }
    @Override
    public Account loadAccountByIban(String username) throws AccountNotFoundException {
        Account account = accountRepo.findByAccIbanIgnoreCase(username);
        if(account == null){
            throw new AccountNotFoundException();
        }
        return account;
    }
    @Override
    public Account loadAccountByUsersUsername(String username) throws AccountNotFoundException {
        Account account = accountRepo.getAccountByUserUsername(username);
        if(account == null){
            throw new AccountNotFoundException();
        }
        return account;
    }
    @Override
    public Account loadAccountByUsersUsernameCustomErrorHandling(String username) {
        Account account = accountRepo.getAccountByUserUsername(username);
        return account;
    }

    @Override
    public Roles loadRole(RoleTypes type) throws RolesException {
        Roles role = rolesRepo.findByName(type);
        if(role == null){
            throw new RolesException("Role not found");
        }
        return role;
    }
    @Override
    public Account createBankAccount(String iban, String accountOwner, double balance, RedirectAttributes redirectAttributes) throws UserNotFoundException {
        List<Transaction> transactionList = new ArrayList<>();
        List<String> tanList = this.generateTanListForAccount();
        if(accountRepo.getAccountByUserUsername(accountOwner) != null) {
            redirectAttributes.addAttribute("bankAccountCreateFail", true);
            redirectAttributes.addFlashAttribute("bankAccountCreateFailReasonUserALreadyHasAnAccount", "user already has an account");
            return new Account();
        }
        if (accountRepo.findByAccIbanIgnoreCase(iban) != null) {
            redirectAttributes.addAttribute("bankAccountCreateFail", true);
            redirectAttributes.addFlashAttribute("bankAccountCreateFailReasonIban", "IBAN was already used!");
            return new Account();
        }
        if (userRepo.getUserByUsername(accountOwner) == null){
            throw new UserNotFoundException();
        }
        User owner = userRepo.getUserByUsername(accountOwner);
        return accountRepo.save(new Account(owner, balance, AccountService.CURRENCY, tanList, iban, transactionList, AccountStatus.ACTIVE));
    }
    @Override
    public Bank loadBank(String bankName) throws BankNotFoundException {
       return bankRepo.getBankByName(bankName);
    }
    @Override
    public void createNewUserByManager(String firstname, String lastname, String username,
                                       String password, String mail, String phone,
                                       Date dateOfBirth, String street,
                                       String streetAddition, int houseNr, String city, String zip, RedirectAttributes redirectAttributes) {
        Roles role =  (this.loadRole(RoleTypes.ROLE_USER)!=null) ? this.loadRole(RoleTypes.ROLE_USER) : new Roles(RoleTypes.ROLE_USER);
        Bank tdanBank = this.loadBank(BANKNAME);
        Address address= new Address( street, streetAddition, houseNr, city, zip);
        User user = new User (firstname, lastname, username, password, address, Arrays.asList(role), mail, phone, dateOfBirth, tdanBank);
        if (userRepo.getUserByUsername(username) != null) {
            redirectAttributes.addAttribute("newUsercreateFails", true);
            redirectAttributes.addFlashAttribute("newUsercreateFailsReasonUserAlreadyExists", "User with this username already exists!");
        } else {
            createCustomer(user);
        }
    }

    public  List<String> generateTanListForAccount() {
        List<String> tanList = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            tanList.add(RandomStringUtils.randomNumeric(10));
        }
        return tanList;
    }

   @Override
   @Transactional (propagation = Propagation.REQUIRES_NEW)
    public Account updateBalance(long bankAccountId, double balance) {
        Account bankAccount = accountRepo.findById(bankAccountId).orElse(null);
        if (bankAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank Account not found");
        }
        bankAccount.setBalance(balance);
        return accountRepo.save(bankAccount);
    }

    @Override
    @Transactional
    public Boolean updateUserInfoByAdmin(String username, String newfirstname, String newlastname, String newmail, String newphone, String newstreet,
                                         String newstreetAddition, int newhouseNr, String newcity,
                                         String newzip, RedirectAttributes redirectAttributes) {
        User user = userRepo.getUserByUsername(username);
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
        redirectAttributes.addAttribute("infoUpdated", true);
        redirectAttributes.addFlashAttribute("userWasUpdated", "User was successfully updated!");
        return true;
    }
    @Override
    @Transactional
    public Boolean updateUserInfoByUser(String username, String newphone, String newstreet,
                                        String newstreetAddition, int newhouseNr, String newcity,
                                        String newzip, String password, RedirectAttributes redirectAttributes) {
        User user = userRepo.getUserByUsername(username);
        log.info(user.getPassword());
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
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
        if(password!=null) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepo.save(user);
        redirectAttributes.addAttribute("ownInfoUpdated", true);
        redirectAttributes.addFlashAttribute("userInfoWasUpdated", "User was successfully updated!");
        return true;
    }
    @Override
    @Transactional
    public void deleteAccount(String username) {
        Account test = this.accountRepo.getAccountByUserUsername(username);
        accountRepo.delete(test);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public void createCustomer(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
}
