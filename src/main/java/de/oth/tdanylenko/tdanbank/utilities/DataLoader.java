package de.oth.tdanylenko.tdanbank.utilities;

import de.oth.tdanylenko.tdanbank.entity.*;
import de.oth.tdanylenko.tdanbank.enums.AccountStatus;
import de.oth.tdanylenko.tdanbank.enums.ProductType;
import de.oth.tdanylenko.tdanbank.exceptions.RolesException;
import de.oth.tdanylenko.tdanbank.repository.*;
import de.oth.tdanylenko.tdanbank.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class DataLoader implements ApplicationRunner {
    private static final String CURRENCY = "EUR";
    private static final String IBAN = "DE89370400440532013000";
    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BusinessProductRepository productsRepo;
    @Autowired
    private RolesRepository rolesRepo;
    @Autowired
    private AccountService account;
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private BankRepository bankRepo;
    private Roles createManagersRole(){
        if (rolesRepo.findByName(RoleTypes.ROLE_MANAGER) == null) {
            return new Roles(RoleTypes.ROLE_MANAGER);
        } else {
     throw new RolesException("Role is already in data base");
     }
    }
    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        //dummy daten
        Address add1 = new Address( "Weiherweg", "Zimmer 2", 12, "Regensburg", "93051");
        Address add2 = new Address( "Wolframstrasse", "Zimmer 5", 2, "Regensburg", "93052");
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = dateformat.parse("17/07/1989");
        Date date2 = dateformat.parse("12/01/1983");
        Roles managerRole = new Roles(RoleTypes.ROLE_MANAGER);
        Roles userRole = new Roles(RoleTypes.ROLE_USER);
        Bank tdanBank = new Bank("DEUTDE2H253", "TDANBank");

        User user1 = new User("Gerald", "of Rivia", "admin", "admin", add1, Arrays.asList(managerRole), "gerald@gmail.com", "+49065267474", date2, tdanBank );
        if (userRepo.getUserByUsername(user1.getUsername()) == null) {
            account.createCustomer(user1);
        }
        log.info("test");
        User user2 = new User("Yennefer", "of Vengerberg", "dummy", "dummy", add2, Arrays.asList(userRole), "gerald@gmail.com", "+49065267474", date1, tdanBank);
        if (userRepo.getUserByUsername(user2.getUsername()) == null) {
            account.createCustomer(user2);
        }
        if(accountRepo.getAccountByUserUsername("dummy") == null) {
            List<String> tanList = account.generateTanListForAccount();
            List<Transaction> transactionList = new ArrayList<>();
            accountRepo.save(new Account(user2, 1000, CURRENCY, tanList, IBAN, transactionList, AccountStatus.ACTIVE));
        }
        Collection <BusinessProducts> productsList = new ArrayList<BusinessProducts>();
        BusinessProducts amazonCard = new BusinessProducts("Amazon Prime Visa Card", ProductType.CREDITCARD, "Kreditkarte für berechtigte Prime-Mitglieder", 99, tdanBank);
        if (productsRepo.getBusinessProductsByNameIgnoreCase(amazonCard.getName()) == null) {
            productsRepo.save(amazonCard);
            productsList.add(amazonCard);
        }
        BusinessProducts visaCard = new BusinessProducts("Visa Premium Card", ProductType.CREDITCARD, "Die Visa Platinum Karte ist Kunden mit überdurchschnittlich hohen Ausgaben vorbehalten", 199, tdanBank);
        if (productsRepo.getBusinessProductsByNameIgnoreCase(visaCard.getName()) == null) {
            productsRepo.save(visaCard);
            productsList.add(visaCard);
        }
        BusinessProducts travelSafeInsurance = new BusinessProducts("Travel Safe Insurance", ProductType.INSURANCE, "Die Auslandskrankenversicherung schützt Sie im Schadensfall vor hohen Arzt- und Krankenhauskosten", 30, tdanBank);
        if (productsRepo.getBusinessProductsByNameIgnoreCase(travelSafeInsurance.getName()) == null) {
            productsRepo.save(travelSafeInsurance);
            productsList.add(travelSafeInsurance);
        }
        BusinessProducts heroesOfSafety = new BusinessProducts("Heroes Of Safety", ProductType.INSURANCE, "Diese Versicherung übernimmt Krankheitskosten bei einer Akutbehandlung im Ausland (inkl. eines Rettungsfluges oder Rücktransportes)", 50, tdanBank);
        if (productsRepo.getBusinessProductsByNameIgnoreCase(heroesOfSafety.getName()) == null) {
            productsRepo.save(heroesOfSafety);
            productsList.add(heroesOfSafety);
        }
        if (bankRepo.getBankByBICIgnoreCase("DEUTDE2H253") == null) {
            bankRepo.getBankByBIC("DEUTDE2H253").setBusinessProducts(productsList);
        }
    }
}
