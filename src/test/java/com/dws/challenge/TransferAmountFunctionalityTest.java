package com.dws.challenge;

import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.repository.AccountsRepositoryInMemory;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.EmailNotificationService;
import com.dws.challenge.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class TransferAmountFunctionalityTest {

    @InjectMocks
    private AccountsRepositoryInMemory repository;

    @InjectMocks
    private EmailNotificationService notificationService;

    @InjectMocks
    private AccountsService accountService;


    private Account tomAccount;
    private Account davidAccount;

    @BeforeEach
    public void setUp() {
        // Setup test data
        tomAccount = new Account("Id-222", new BigDecimal(500.00));
        davidAccount = new Account("Id-333", new BigDecimal(300.00));

        this.accountService.setAccountsRepository(this.repository);
        this.accountService.setNotificationService(this.notificationService);

        this.accountService.createAccount(tomAccount);
        this.accountService.createAccount(davidAccount);

    }

    @Test
    public void testTransferMoneySuccessful() {
        BigDecimal amountToTransfer = new BigDecimal(100.00);

        accountService.transferMoney("Id-222", "Id-333", amountToTransfer);

        assertEquals(new BigDecimal(400.00), tomAccount.getBalance());
        assertEquals(new BigDecimal(400.00), davidAccount.getBalance());
    }

    @Test
    public void testTransferInsufficientBalance() {

        BigDecimal amountToTransfer = new BigDecimal(600);

        boolean result = accountService.transferMoney("Id-222", "Id-333", amountToTransfer);

        assertFalse(result);

        Account acc1 = accountService.getAccount("Id-222");
        Account acc2 = accountService.getAccount("Id-333");

        assertEquals(new BigDecimal(500), acc1.getBalance());
        assertEquals(new BigDecimal(300), acc2.getBalance());
    }

    @Test
    public void testTransferFromNonExistingAccount() {
        boolean result = accountService.transferMoney("Id-100", "Id-333", new BigDecimal(200));
        assertFalse(result);
        Account acc2 = accountService.getAccount("Id-333");
        assertEquals(new BigDecimal(300), acc2.getBalance());
    }

    @Test
    public void testTransferZeroAmount() {
        boolean result = accountService.transferMoney("Id-222", "Id-333", BigDecimal.ZERO);
        assertTrue(result);
        Account acc1 = accountService.getAccount("Id-222");
        Account acc2 = accountService.getAccount("Id-333");

        assertEquals(new BigDecimal(500), acc1.getBalance());
        assertEquals(new BigDecimal(300), acc2.getBalance());
    }


}
