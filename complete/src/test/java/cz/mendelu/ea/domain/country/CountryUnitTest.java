package cz.mendelu.ea.domain.country;

import cz.mendelu.ea.domain.account.Account;
import cz.mendelu.ea.domain.transaction.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CountryUnitTest {

    @Test
    public void testGetSumOfAllTransactions() {
        // given
        Account account = new Account();

        Transaction transaction1 = new Transaction();
        transaction1.setAmount(1000);
        Transaction transaction2 = new Transaction();
        transaction2.setAmount(200);
        Transaction transaction3 = new Transaction();
        transaction3.setAmount(300);

        account.getOutgoingTransactions().add(transaction1);
        account.getIncomingTransactions().add(transaction2);
        account.getIncomingTransactions().add(transaction3);

        // when
        double sum = account.getSumOfAllTransactions();

        // then
        assertThat(sum, is(-500.0));
    }

    @Test
    public void testGetSumOfAllTransactions_InvalidTransactionAmount() {
        // given
        Account account = new Account();

        Transaction transaction1 = new Transaction();
        transaction1.setAmount(-1000);

        account.getOutgoingTransactions().add(transaction1);

        // when called then exception is thrown
        assertThrows(DataIntegrityViolationException.class, () -> account.getSumOfAllTransactions());
    }

}
