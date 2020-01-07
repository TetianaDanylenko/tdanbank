package de.oth.tdanylenko.tdanbank.controller;

import de.oth.tdanylenko.tdanbank.dto.TransactionDTO;
import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.entity.Transaction;
import de.oth.tdanylenko.tdanbank.exceptions.TransactionNotFoundException;
import de.oth.tdanylenko.tdanbank.repository.TransactionRepository;
import de.oth.tdanylenko.tdanbank.service.AccountService;
import de.oth.tdanylenko.tdanbank.service.TransaсtionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class TransactionController {
   @Autowired
   private final TransactionRepository transactionRepository;
   @Autowired
   private TransaсtionService transactionService;
    @Autowired
    private AccountService accountService;


    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    TransactionDTO addTransaction(@RequestBody final TransactionDTO transactionDto) {
        return transactionService.save(transactionDto);
    }

    @GetMapping("/transaction/{transactionId}")
    public @ResponseBody TransactionDTO getTransaction(@PathVariable final Long transactionId) {
        final Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (transaction.isPresent()) {
            return this.transactionService.toDto(transaction.get());
        } else throw new TransactionNotFoundException();
    }

    @RequestMapping(value = "/updatebankaccountbalance", method = RequestMethod.POST)
    public String updateBalance(@RequestParam long bankAccountId, @RequestParam double balance) {
        Account bankAccount = accountService.updateBalance(bankAccountId, balance);
        return "redirect:/account/" + bankAccount.getId();
    }

    @RequestMapping(value = "/transfermoney", method = RequestMethod.POST)
    public String transferMoney(@RequestParam long to, @RequestParam long from, @RequestParam double amount, @RequestParam String message,
                                RedirectAttributes redirectAttributes) {
        if (amount <= 0) {
            redirectAttributes.addAttribute("transferFail", true);
            redirectAttributes.addFlashAttribute("transferFailAmount", "Amount must be more than 0!");
        } else {
            transactionService.transferMoney(to, from, amount, redirectAttributes);
        }
        return "redirect:/account/" + from;
    }
}
