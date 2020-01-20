package de.oth.tdanylenko.tdanbank.service;

import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.entity.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

public interface AccountServiceIF {
    Account createBankAccount(String iban, String accountOwner, double balance, RedirectAttributes redirectAttributes);

    void createNewUserByManager(String firstname, String lastname, String username,
                                String password, String mail, String phone,
                                Date dateOfBirth, String street,
                                String streetAddition, int houseNr, String city, String zip, RedirectAttributes redirectAttributes);

    @Transactional
    Account updateBalance(long bankAccountId, double balance);

    @Transactional
    Boolean updateUserInfoByAdmin(String username, String newfirstname, String newlastname, String newmail, String newphone, String newstreet,
                                  String newstreetAddition, int newhouseNr, String newcity,
                                  String newzip, RedirectAttributes redirectAttributes);

    @Transactional
    void deleteAccount(String username);

    @Transactional
    void createCustomer(User user);
}
