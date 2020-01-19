package de.oth.tdanylenko.tdanbank.utilities;

import de.oth.tdanylenko.tdanbank.entity.*;
import de.oth.tdanylenko.tdanbank.repository.AccountRepository;
import de.oth.tdanylenko.tdanbank.repository.RolesRepository;
import de.oth.tdanylenko.tdanbank.repository.UserRepository;
import de.oth.tdanylenko.tdanbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Component
public class DataLoader implements ApplicationRunner {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RolesRepository rolesRepo;
    @Autowired
    private AccountService account;
    @Autowired
    private AccountRepository accountRepo;


    @Override
    public void run(ApplicationArguments args) throws Exception {

       Address add1 = new Address( "Weiherweg", "Zimmer 2", 12, "Regensburg", "93051");
       Address add2 = new Address( "Wolframstrasse", "Zimmer 5", 2, "Regensburg", "93052");
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = dateformat.parse("17/07/1989");
        Date date2 = dateformat.parse("12/01/1983");
        Roles managerRole = new Roles(RoleTypes.ROLE_MANAGER);
/*        if (rolesRepo.findByName(RoleTypes.ROLE_MANAGER) == null) {
            rolesRepo.save(role1);
        }*/
        Roles userRole = new Roles(RoleTypes.ROLE_USER);
     /*   if (rolesRepo.findByName(RoleTypes.ROLE_USER) == null) {
            rolesRepo.save(role2);
        }*/
        User user1 = new User("Gerald", "of Rivia", "user1234", "1234", add1, Arrays.asList(managerRole), "gerald@gmail.com", "+49065267474", date1);
        if (userRepo.getUserByUsername("user1234") == null) {
            account.createCustomer(user1);
        }
        User user2 = new User("Yennefer", "of Vengerberg", "user4321", "4321", add2, Arrays.asList(userRole), "gerald@gmail.com", "+49065267474", date2);
        if (userRepo.getUserByUsername("user4321") == null) {
            account.createCustomer(user2);
        }
        userRepo.getUserByUsername("user").setPassword("1111");
    }

}
