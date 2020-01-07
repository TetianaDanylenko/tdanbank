package de.oth.tdanylenko.tdanbank.service;

import de.oth.tdanylenko.tdanbank.dto.TransactionDTO;
import de.oth.tdanylenko.tdanbank.entity.Account;
import de.oth.tdanylenko.tdanbank.entity.Transaction;
import de.oth.tdanylenko.tdanbank.entity.User;
import de.oth.tdanylenko.tdanbank.enums.TransactionStatus;
import de.oth.tdanylenko.tdanbank.exceptions.UserNotFoundException;
import de.oth.tdanylenko.tdanbank.repository.AccountRepository;
import de.oth.tdanylenko.tdanbank.repository.TransactionRepository;
import de.oth.tdanylenko.tdanbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransaсtionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    public TransactionDTO toDto(Transaction transaction){
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        if (transaction.getFrom()!=null){
            dto.setSenderMail(transaction.getFrom().getUser().getMail());
        } else throw new UserNotFoundException();
        if (transaction.getTo()!=null){
            dto.setReceiverMail(transaction.getTo().getUser().getMail());
        } else throw new UserNotFoundException();
        dto.setTimeStamp(LocalDateTime.now());
        dto.setStatus(TransactionStatus.INPROGRESS);
        return dto;
    }

    public Transaction toEntity (TransactionDTO dto){
        Transaction transaction = new Transaction();
        transaction.setId(dto.getId());
        transaction.setAmount(dto.getAmount());
        Optional<User> sender = Optional.ofNullable(userRepository.getUserByMail(dto.getSenderMail()));
        Optional<Account>senderAccount = Optional.ofNullable(accountRepository.getAccountByUser(sender));
        senderAccount.ifPresent(transaction::setFrom);
        Optional<User>receiver = Optional.ofNullable(userRepository.getUserByMail(dto.getReceiverMail()));
        Optional<Account>receiverAccount = Optional.ofNullable(accountRepository.getAccountByUser(receiver));
        receiverAccount.ifPresent(transaction::setTo);
        transaction.setTimeStamp(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.STARTED);
        return transaction;
    }

    public TransactionDTO save(TransactionDTO transactionDto) {
        Transaction transaction = this.toEntity(transactionDto);
        transactionRepository.save(transaction);
        return this.toDto(transaction);
    }


    @Transactional
    public void transferMoney(long to, long from, double amount, RedirectAttributes redirectAttributes) {
        Account recepient = accountRepository.findById(to).orElse(null);
        Account sender = accountRepository.findById(from).orElse(null);
        if (recepient == null || sender == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist");
        }
        if (sender.getBalance() - amount < 0) {
            redirectAttributes.addAttribute("transferFail", true);
            redirectAttributes.addFlashAttribute("transferFailAmount", "Not enough money on your account");
            return;
        }
    }
    public List<Transaction> getBankAccountTransactionHistory(Account bankAccount) {
      return null;
    }


}
