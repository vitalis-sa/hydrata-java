package fiap.hydrata.repository;

import fiap.hydrata.entity.Alerta;
import fiap.hydrata.enums.StatusAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByPropriedadeIdAndStatus(Long propriedadeId, StatusAlerta status);
    Optional<Alerta> findFirstByPropriedadeIdOrderByIdDesc(Long propriedadeId);
    List<Alerta> findByPropriedadeIdOrderByDataGeracaoDesc(Long propriedadeId);
    List<Alerta> findByPropriedadeIdAndTipoOrderByDataGeracaoDesc(Long propriedadeId, fiap.hydrata.enums.TipoAlerta tipo);
    void deleteByPropriedadeId(Long propriedadeId);
}
