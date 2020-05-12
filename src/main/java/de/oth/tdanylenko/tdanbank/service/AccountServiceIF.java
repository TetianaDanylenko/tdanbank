package de.oth.tdanylenko.tdanbank.service;

import de.oth.tdanylenko.tdanbank.entity.*;
import de.oth.tdanylenko.tdanbank.exceptions.AccountNotFoundException;
import de.oth.tdanylenko.tdanbank.exceptions.BankNotFoundException;
import de.oth.tdanylenko.tdanbank.exceptions.RolesException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

public interface AccountServiceIF {
    User loadUserByUsernameIgnoringCase(String username) throws UsernameNotFoundException;

    Account loadAccountByUser(User user) throws AccountNotFoundException;

    Account loadAccountByIban(String username) throws AccountNotFoundException;

    Account loadAccountByUsersUsername(String username) throws AccountNotFoundException;

    Account loadAccountByUsersUsernameCustomErrorHandling(String username);

    Roles loadRole (RoleTypes type) throws RolesException;

    Account createBankAccount(String iban, String accountOwner, double balance, RedirectAttributes redirectAttributes);

    Bank loadBank(String bankName) throws BankNotFoundException;

    void createNewUserByManager(String firstname, String lastname, String username,
                                String password, String mail, String phone,
                                Date dateOfBirth, String street,
                                String streetAddition, int houseNr, String city, String zip, RedirectAttributes redirectAttributes);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Account updateBalance(long bankAccountId, double balance);

    @Transactional
    Boolean updateUserInfoByAdmin(String username, String newfirstname, String newlastname, String newmail, String newphone, String newstreet,
                                  String newstreetAddition, int newhouseNr, String newcity,
                                  String newzip, RedirectAttributes redirectAttributes);

    @Transactional
    Boolean updateUserInfoByUser(String username, String newphone, String newstreet,
                                 String newstreetAddition, int newhouseNr, String newcity,
                                 String newzip, String password, RedirectAttributes redirectAttributes);

    @Transactional
    void deleteAccount(String username);

    @Transactional
    void createCustomer(User user);
}
