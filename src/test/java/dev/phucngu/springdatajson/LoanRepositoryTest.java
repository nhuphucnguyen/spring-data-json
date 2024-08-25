package dev.phucngu.springdatajson;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LoanRepositoryTest {

    @Autowired
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    LoanRepository loanRepository;

    @Test
    void findByAgeBetween() {
        var result = loanRepository.findByAgeBetween(20, 28);
        assertFalse(result.isEmpty());
    }

    @Test
    void findByAgeGreaterThan() {
        var minAge = 10;
        var maxAge = 50;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Loan> criteriaQuery = criteriaBuilder.createQuery(Loan.class);
        Root<Loan> root = criteriaQuery.from(Loan.class);

        // Construct JSON function expressions
        Expression<Integer> ageExpression = criteriaBuilder.function(
                "jsonb_extract_path_text",
                Integer.class,
                root.get("metadata"),
                criteriaBuilder.literal("age")
        );

        // Create predicates for age range
        Predicate agePredicate = criteriaBuilder.between(ageExpression, minAge, maxAge);
        criteriaQuery.where(agePredicate);

        TypedQuery<Loan> query = entityManager.createQuery(criteriaQuery);
        var result = query.getResultList();
        assertFalse(result.isEmpty());
    }

    @Test
    void findUsingSpec() {
        var result = loanRepository.findAll(LoanSpec.hasAgeBetween(20, 28));
        assertEquals(27, result.getFirst().getMetadata().getAge());
    }
}