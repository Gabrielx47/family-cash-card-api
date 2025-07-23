package example.cashcard;

import org.springframework.data.repository.CrudRepository;

public interface CashCardRepository extends CrudRepository<CashCard, Long> {
    // This interface will automatically provide CRUD operations for CashCard entities
    // No additional methods are needed unless custom queries are required
}
