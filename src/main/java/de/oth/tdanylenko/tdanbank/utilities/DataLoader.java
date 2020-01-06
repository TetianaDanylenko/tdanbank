package de.oth.tdanylenko.tdanbank.utilities;

import de.oth.tdanylenko.tdanbank.entity.*;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Address add1 = new Address( "Weiherweg", "Zimmer 2", 12, "Regensburg", "93051");
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = dateformat.parse("17/07/1989");
        Roles role1 = new Roles(RoleTypes.ROLE_MANAGER);
        rolesRepo.save(role1);
        User user1 = new User("Gerald", "of Rivia", "user1234", "1234", add1, Arrays.asList(role1), "gerald@gmail.com", "+49065267474", date1);
        account.kundeAnlegen(user1);
        Address add2 = new Address( "Wolframstrasse", "Zimmer 5", 2, "Regensburg", "93051");
        Date date2 = dateformat.parse("12/01/1983");
        Roles role2 = new Roles(RoleTypes.ROLE_USER);
        rolesRepo.save(role2);
        User user2 = new User("Yennefer", "of Vengerberg", "user4321", "1111", add2, Arrays.asList(role2), "gerald@gmail.com", "+49065267474", date2);
        account.kundeAnlegen(user2);
    }

}
