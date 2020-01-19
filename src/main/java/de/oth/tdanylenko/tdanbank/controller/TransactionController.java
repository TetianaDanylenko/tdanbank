package de.oth.tdanylenko.tdanbank.controller;

import de.oth.tdanylenko.tdanbank.dto.TransactionDTO;
import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.entity.Transaction;
import de.oth.tdanylenko.tdanbank.exceptions.TransactionNotFoundException;
import de.oth.tdanylenko.tdanbank.repository.AccountRepository;
import de.oth.tdanylenko.tdanbank.repository.TransactionRepository;
import de.oth.tdanylenko.tdanbank.service.AccountService;
import de.oth.tdanylenko.tdanbank.service.TransaсtionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class TransactionController {
    private static final Logger log = LogManager.getLogger(DashboardController.class);
   @Autowired
   private final TransactionRepository transactionRepository;
   @Autowired
   private TransaсtionService transactionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepo;

    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @RequestMapping(value = "/api/pptransaction/getfunds",  consumes = "application/json", produces =
            "application/json", method = RequestMethod.POST)
    public ResponseEntity<TransactionDTO> addTransactionFromPayPal(@RequestBody(required = false) TransactionDTO transactionDto) {
        TransactionDTO transaction = transactionService.directDebit(transactionDto);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @RequestMapping("/api/pptransaction/{transactionId}")
    public @ResponseBody TransactionDTO getTransaction(@PathVariable final Long transactionId) {
        Transaction transaction = transactionRepository.getById(transactionId);
        if (transaction != null) {
            return this.transactionService.toDto(transaction, true);
        } else throw new TransactionNotFoundException();
    }

    @RequestMapping(value = "/updatebankaccountbalance", method = RequestMethod.POST)
    public String updateBalance(@RequestParam long bankAccountId, @RequestParam double balance) {
        Account bankAccount = accountService.updateBalance(bankAccountId, balance);
        return "redirect:/account/" + bankAccount.getId();
    }

    @RequestMapping(value = "/transfermoney", method = RequestMethod.POST)
    public String transferMoney(@RequestParam String receiversIban, @RequestParam String sendersIban, @RequestParam double amount, @RequestParam String tan,
                                @RequestParam String message,
                                RedirectAttributes redirectAttributes) {
        Account bankAccount = accountRepo.findByAccIbanIgnoreCase(sendersIban);
        if (amount < 0) {
            redirectAttributes.addAttribute("transferFail", true);
            redirectAttributes.addFlashAttribute("transferFailAmount", "Amount must be more than 0!");
        } else {
            transactionService.transferMoney(receiversIban, sendersIban, amount, tan, message, redirectAttributes);
        }
        return "redirect:/account/" + bankAccount.getUser().getUsername();
    }
}
