package dev.phucngu.springdatajson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

//    @Query("SELECT loan from Loan loan where loan.metadata->'age' between :minAge and :maxAge")
    @Query(value = "SELECT * FROM loan WHERE CAST(JSON_EXTRACT(metadata, '$.age') AS UNSIGNED) BETWEEN :minAge AND :maxAge", nativeQuery = true)
    List<Loan> findByAgeBetween(Integer minAge, Integer maxAge);
}
