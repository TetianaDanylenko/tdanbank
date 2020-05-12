package de.oth.tdanylenko.tdanbank.service;

import de.oth.tdanylenko.tdanbank.dto.TransactionDTO;
import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.entity.Transaction;
import de.oth.tdanylenko.tdanbank.enums.TransactionStatus;
import de.oth.tdanylenko.tdanbank.exceptions.AccountNotFoundException;
import de.oth.tdanylenko.tdanbank.exceptions.BalanceInsufficientException;
import de.oth.tdanylenko.tdanbank.exceptions.TransactionServiceException;
import de.oth.tdanylenko.tdanbank.repository.AccountRepository;
import de.oth.tdanylenko.tdanbank.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Scope("singleton")
public class TransaсtionService implements TransactionServiceIF{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;

    public TransaсtionService(AccountRepository accountRepository, TransactionRepository transactionRepository,
                              AccountService accountService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public TransactionDTO toDto(Transaction transaction, Boolean isSuccessful){
        TransactionDTO dto = new TransactionDTO();
        dto.setAmount(transaction.getAmount());
        dto.setReasonForPayment(transaction.getReasonForPayment());
        dto.setSuccessful(isSuccessful);
        if (transaction.getFrom()!=null){
            dto.setSenderIBAN(transaction.getFrom().getiban());
        } else throw new AccountNotFoundException();
        return dto;
    }

    public Transaction toEntity (TransactionDTO dto){
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        Optional<Account>senderAccount = Optional.ofNullable(accountRepository.findByAccIbanIgnoreCase(dto.getSenderIBAN()));
        senderAccount.ifPresent(transaction::setFrom);
        transaction.setTimeStamp(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.STARTED);
        transaction.setReasonForPayment(dto.getReasonForPayment());
        return transaction;
    }

    public Transaction loadTransactionById(long id) {
        return transactionRepository.getById(id);
    }

    @Override
    public void transferMoney(String to, String from, double amount, String tan, String message, RedirectAttributes redirectAttributes) {
        Account recipient = accountRepository.findByAccIbanIgnoreCase(to);
        Account sender = accountRepository.findByAccIbanIgnoreCase(from);
        if (recipient == null || sender == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist");
        }
        if (sender.getBalance() - amount < 0) {
            redirectAttributes.addAttribute("transferFail", true);
            redirectAttributes.addFlashAttribute("transferFailAmount", "Not enough money on your account");
            return;
        }
       String tanToUse = this.requireTanforTransaction(tan, sender);
        LocalDateTime rightNow = LocalDateTime.now();
        String actionMessage = sender.getUser().getUsername() + " transferred " + amount + "€ from " + sender.getiban() + " ("
                + sender.getUser().getUsername() + ") to " + recipient.getiban() + " (" + recipient.getUser().getUsername() + ")";
        log.info(actionMessage);
        Transaction transaction = transactionRepository.save(new Transaction(sender, recipient, rightNow, tanToUse, amount, message, "EUR", TransactionStatus.SUCCEEDED ));
        accountService.updateBalance(sender.getId(), (sender.getBalance() - amount));
        accountService.updateBalance(recipient.getId(), (recipient.getBalance() + amount));
        this.addTransactionToHistory(transaction, sender, recipient);
    }

    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public String requireTanforTransaction (String tan, Account sender) {
        List <String> tanlist = sender.getTanList();
        int indexofTan = tanlist.indexOf(tan);
        log.info(tanlist.get(indexofTan));
        String tempTan = null;
        if(indexofTan!=-1){
            tempTan = tanlist.get(tanlist.indexOf(tan));
            tanlist.remove(tanlist.indexOf(tan));
            sender.setTanList(tanlist);
        }
        return tempTan;
    }
    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public void addTransactionToHistory (Transaction transaction, Account sender, Account recipient) {
        if(sender!=null){
            sender.getTransactionHistory().add(transaction);
        }
        accountRepository.save(sender);
    }

    @Override
    public TransactionDTO directDebit(TransactionDTO transactionDto) throws TransactionServiceException {
        try {
            Transaction transaction = this.toEntity(transactionDto);
            Account sender = accountRepository.findByAccIbanIgnoreCase(transaction.getFrom().getiban());
            if (sender == null) {
                throw new AccountNotFoundException();
            }
            if (sender.getBalance() - transaction.getAmount() < 0) {
                throw new BalanceInsufficientException();
            }
            String actionMessage = sender.getUser().getUsername() + " successfully transferred " + transaction.getAmount() + "€ ";
            log.info(actionMessage);
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            Transaction transactionToSave = new Transaction(sender, transaction.getTimeStamp(),transaction.getAmount(), transaction.getReasonForPayment(), "EUR", TransactionStatus.SUCCEEDED );
            transactionRepository.save(transactionToSave);
            this.addTransactionToHistory(transactionToSave, sender, null);
            return this.toDto(transactionToSave, true);
        } catch (AccountNotFoundException exception) {
            throw new AccountNotFoundException();
        } catch (BalanceInsufficientException exception) {
            throw new BalanceInsufficientException();
        }

    }

}
