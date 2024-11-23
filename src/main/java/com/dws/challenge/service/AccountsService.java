package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AccountNotPresentException;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.repository.AccountsRepositoryInMemory;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Data
public class AccountsService {

  @Autowired
  private AccountsRepositoryInMemory accountsRepository;

  @Autowired
  private  EmailNotificationService notificationService;


  private final Lock lock = new ReentrantLock();

 /* @Autowired
  public AccountsService(AccountsRepository accountsRepository, NotificationService notificationService) {
    this.accountsRepository = accountsRepository;
    this.notificationService = notificationService;
  }*/

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

  public void updateAccount(Account account) {
    this.accountsRepository.updateAccount(account);
  }


  public boolean  transferMoney(String fromAccountId, String toAccountId, BigDecimal amount) {
    lock.lock();
    try {
      Account fromAccount = this.getAccount(fromAccountId);
      Account toAccount = this.getAccount(toAccountId);

      if (fromAccount == null) {
        System.out.println("Account with id " + fromAccountId + " does not exist ");
        return false;
      }
      if (toAccount == null) {
        System.out.println("Account with id " + toAccountId + " does not exist ");
        return false;
      }
      // Check if sufficient balance exists
      if (fromAccount.getBalance().compareTo(amount) < 0) {
        System.out.println("Insufficient balance");
        return false;
      }
      // Perform the transfer
      fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
      toAccount.setBalance(toAccount.getBalance().add(amount));

      // Save the updated accounts
      this.updateAccount(fromAccount);
      this.updateAccount(toAccount);

      //send the email notification
      this.notificationService.notifyAboutTransfer(toAccount, "Amount Credited");
      return  true;
    }
    finally {
      lock.unlock();
    }
  }

}
