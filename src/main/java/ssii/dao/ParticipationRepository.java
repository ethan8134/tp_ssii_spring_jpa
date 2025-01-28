package ssii.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ssii.entity.Participation;

public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
}