package fiap.hydrata.repository;

import fiap.hydrata.entity.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PropriedadeRepository extends JpaRepository<Propriedade, Long> {
    java.util.List<Propriedade> findByProdutorId(Long produtorId);
}
