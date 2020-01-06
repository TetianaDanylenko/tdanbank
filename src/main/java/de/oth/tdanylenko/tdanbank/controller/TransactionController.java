package de.oth.tdanylenko.tdanbank.controller;

import de.oth.tdanylenko.tdanbank.dto.TransactionDTO;
import de.oth.tdanylenko.tdanbank.entity.Transaction;
import de.oth.tdanylenko.tdanbank.exceptions.TransactionNotFoundException;
import de.oth.tdanylenko.tdanbank.repository.TransactionRepository;
import de.oth.tdanylenko.tdanbank.service.TransaсtionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class TransactionController {
   @Autowired
   private final TransactionRepository transactionRepository;
   @Autowired
   private TransaсtionService transactionService;

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
}
