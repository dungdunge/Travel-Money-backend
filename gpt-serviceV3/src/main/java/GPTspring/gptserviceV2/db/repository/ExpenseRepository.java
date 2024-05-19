package GPTspring.gptserviceV2.db.repository;

import GPTspring.gptserviceV2.db.domain.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
