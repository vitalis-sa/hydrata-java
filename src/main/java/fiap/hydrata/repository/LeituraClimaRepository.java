package fiap.hydrata.repository;

import fiap.hydrata.entity.LeituraClima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeituraClimaRepository extends JpaRepository<LeituraClima, Long> {
    List<LeituraClima> findByDispositivoIotId(Long dispositivoIotId);
    Optional<LeituraClima> findFirstByDispositivoIotIdOrderByIdDesc(Long dispositivoIotId);
}
