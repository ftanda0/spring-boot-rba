package rba.it.CardApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rba.it.CardApp.model.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByOib(String oib);
    void deleteByOib(String oib);
    boolean existsByOib(String oib);

}
