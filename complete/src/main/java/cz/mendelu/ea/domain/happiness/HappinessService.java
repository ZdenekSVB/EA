package cz.mendelu.ea.domain.happiness;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HappinessService {

    private final HappinessRepository repository;

    public HappinessService(HappinessRepository repository) {
        this.repository = repository;
    }

    public List<Happiness> getAll() {
        List<Happiness> data = new ArrayList<>();
        repository.findAll().forEach(data::add);
        return data;
    }

    public Optional<Happiness> getById(Long id) {
        return repository.findById(id);
    }

    public Happiness create(Happiness happiness) {
        return repository.save(happiness);
    }

    public Happiness update(Long id, Happiness happiness) {
        happiness.setId(id);
        return repository.save(happiness);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public List<Happiness> saveAll(List<Happiness> happinessRecords) {
        return (List<Happiness>) repository.saveAll(happinessRecords);
    }
}
