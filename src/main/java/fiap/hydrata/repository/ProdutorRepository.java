package fiap.hydrata.repository;

import fiap.hydrata.entity.Produtor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutorRepository extends JpaRepository<Produtor, Long> {
    Optional<Produtor> findByEmail(String email);
}
