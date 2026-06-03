package fiap.hydrata.repository;

import fiap.hydrata.entity.Alerta;
import fiap.hydrata.enums.StatusAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByPropriedadeIdAndStatus(Long propriedadeId, StatusAlerta status);
}
