package fiap.hydrata.repository;

import fiap.hydrata.entity.DadoExterno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DadoExternoRepository extends JpaRepository<DadoExterno, Long> {
    java.util.Optional<DadoExterno> findFirstByTipoOrderByIdDesc(String tipo);
}
