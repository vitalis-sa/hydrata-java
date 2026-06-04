package fiap.hydrata.repository;

import fiap.hydrata.entity.FonteExterna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FonteExternaRepository extends JpaRepository<FonteExterna, Long> {
    java.util.Optional<FonteExterna> findByNome(String nome);
}
