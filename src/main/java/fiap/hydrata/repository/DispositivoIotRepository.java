package fiap.hydrata.repository;

import fiap.hydrata.entity.DispositivoIot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DispositivoIotRepository extends JpaRepository<DispositivoIot, Long> {
    List<DispositivoIot> findByStatus(fiap.hydrata.enums.StatusSensor status);
    Optional<DispositivoIot> findByMacAddress(String macAddress);
    Optional<DispositivoIot> findByPropriedadeId(Long propriedadeId);
}
