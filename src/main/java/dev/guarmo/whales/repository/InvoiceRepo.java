package dev.guarmo.whales.repository;

import dev.guarmo.whales.model.transaction.invoice.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepo extends JpaRepository<Invoice, Long> {
}
