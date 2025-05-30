package cz.mendelu.ea.domain.prediction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    List<Prediction> findByCountryId(Long countryId);
    List<Prediction> findByYear(int year);
    void deleteByCountryId(Long countryId); // <--- přidáno!
}
