package nl.winfinnity.housingapp.repository;

import nl.winfinnity.housingapp.models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByEmailIgnoreCase(String email);
    Page<Customer> findByFirstnameLikeIgnoreCaseAndLastnameLikeIgnoreCase(String firstname, String lastname, Pageable pageable);
    @Modifying
    @Query(
            value = "truncate table customer",
            nativeQuery = true
    )
    void truncateMyTable();

}
