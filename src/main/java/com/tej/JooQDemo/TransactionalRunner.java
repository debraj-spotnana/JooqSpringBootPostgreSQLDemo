package com.tej.JooQDemo;

import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@AllArgsConstructor
public class TransactionalRunner {
  private final PlatformTransactionManager manager;

  public void readOnlyTransaction(Runnable fn) {
    var template = new TransactionTemplate(manager);
    template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
    template.execute(
        transactionStatus -> {
          fn.run();
          return null;
        });
  }
}
