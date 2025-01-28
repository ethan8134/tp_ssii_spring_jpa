package ssii.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssii.dao.ParticipationRepository;
import ssii.dao.PersonneRepository;
import ssii.dao.ProjetRepository;
import ssii.entity.Participation;
import ssii.entity.Personne;
import ssii.entity.Projet;


@Service
@RequiredArgsConstructor
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final PersonneRepository personneRepository;
    private final ProjetRepository projetRepository;

    @Transactional
    public void addParticipation(Integer matricule, Integer projectCode, String role, Integer percentage) {
        Personne personne = personneRepository.findById(matricule)
                .orElseThrow(() -> new IllegalArgumentException("Personne introuvable"));

        Projet projet = projetRepository.findById(projectCode)
                .orElseThrow(() -> new IllegalArgumentException("Projet introuvable"));

        if (participationRepository.findAll().stream()
                .anyMatch(p -> p.getPersonne().equals(personne) && p.getProjet().equals(projet))) {
            throw new IllegalStateException("Personne participe déjà à ce projet");
        }

        // Vérifier que la personne n'est pas allouée à plus de 100% sur ses projets
        int totalPercentage = participationRepository.findAll().stream()
                .filter(p -> p.getPersonne().equals(personne))
                .mapToInt(Participation::getPercentage)
                .sum();
        if (totalPercentage + percentage > 100) {
            throw new IllegalStateException("Personne ne peut pas être à plus de 100% d'implication");
        }

        long projectCount = participationRepository.findAll().stream()
                .filter(p -> p.getPersonne().equals(personne))
                .count();
        if (projectCount >= 3) {
            throw new IllegalStateException("Personne ne peut pas participer à plus de 3 projets en même temps");
        }

        Participation participation = new Participation();
        participation.setPersonne(personne);
        participation.setProjet(projet);
        participation.setRole(role);
        participation.setPercentage(percentage);
        participationRepository.save(participation);
    }
}
