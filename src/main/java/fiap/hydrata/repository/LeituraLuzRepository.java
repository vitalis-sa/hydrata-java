package fiap.hydrata.repository;

import fiap.hydrata.entity.LeituraLuz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeituraLuzRepository extends JpaRepository<LeituraLuz, Long> {
    List<LeituraLuz> findByDispositivoIotId(Long dispositivoIotId);
    Optional<LeituraLuz> findFirstByDispositivoIotIdOrderByIdDesc(Long dispositivoIotId);
}
