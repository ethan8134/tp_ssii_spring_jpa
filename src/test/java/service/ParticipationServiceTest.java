package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ssii.dao.ParticipationRepository;
import ssii.dao.PersonneRepository;
import ssii.dao.ProjetRepository;
import ssii.entity.Participation;
import ssii.service.ParticipationService;
import ssii.entity.Personne;
import ssii.entity.Projet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParticipationServiceTest {

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private PersonneRepository personneRepository;

    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private ParticipationService participationService;

    private Personne personne;
    private Projet projet1;
    private Projet projet2;
    private Projet projet3;
    private Projet projet4;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        personne = new Personne();
        personne.setMatricule(1);
        personne.setNom("Bruce Wayne");

        projet1 = new Projet();
        projet1.setCode(1);
        projet1.setName("Rénovation de la batcave");

        projet2 = new Projet();
        projet2.setCode(2);
        projet2.setName("Publication Daily Planet");

        projet3 = new Projet();
        projet3.setCode(3);
        projet3.setName("Protection Amazones");

        projet4 = new Projet();
        projet4.setCode(4);
        projet4.setName("Rues de Gotham plus sûres");
    }

    @Test
    void testAddParticipation_Success() {
        when(personneRepository.findById(1)).thenReturn(Optional.of(personne));
        when(projetRepository.findById(1)).thenReturn(Optional.of(projet1));
        when(participationRepository.findAll()).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> participationService.addParticipation(1, 1, "PDG", 50));

        verify(participationRepository, times(1)).save(any(Participation.class));
    }

    @Test
    void testAddParticipation_FailsWhenDuplicateParticipation() {
        Participation existingParticipation = new Participation();
        existingParticipation.setPersonne(personne);
        existingParticipation.setProjet(projet1);

        when(personneRepository.findById(1)).thenReturn(Optional.of(personne));
        when(projetRepository.findById(1)).thenReturn(Optional.of(projet1));
        when(participationRepository.findAll()).thenReturn(List.of(existingParticipation));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> participationService.addParticipation(1, 1, "PDG", 50));
        assertEquals("Personne participe déjà à ce projet", exception.getMessage());
    }

    @Test
    void testAddParticipation_FailsWhenExceeding100Percent() {
        Participation participation1 = new Participation();
        participation1.setPersonne(personne);
        participation1.setProjet(projet1);
        participation1.setPercentage(60);

        Participation participation2 = new Participation();
        participation2.setPersonne(personne);
        participation2.setProjet(projet2);
        participation2.setPercentage(40);

        when(personneRepository.findById(1)).thenReturn(Optional.of(personne));
        when(projetRepository.findById(3)).thenReturn(Optional.of(projet3));
        when(participationRepository.findAll()).thenReturn(List.of(participation1, participation2));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> participationService.addParticipation(1, 3, "Journaliste", 10));
        assertEquals("Personne ne peut pas être à plus de 100% d'implication", exception.getMessage());
    }

    @Test
    void testAddParticipation_FailsWhenMoreThanThreeProjects() {
        Participation participation1 = new Participation();
        participation1.setPersonne(personne);
        participation1.setProjet(projet1);
        participation1.setPercentage(30);

        Participation participation2 = new Participation();
        participation2.setPersonne(personne);
        participation2.setProjet(projet2);
        participation2.setPercentage(30);

        Participation participation3 = new Participation();
        participation3.setPersonne(personne);
        participation3.setProjet(projet3);
        participation3.setPercentage(30);

        when(personneRepository.findById(1)).thenReturn(Optional.of(personne));
        when(projetRepository.findById(4)).thenReturn(Optional.of(projet4));
        when(participationRepository.findAll()).thenReturn(List.of(participation1, participation2, participation3));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> participationService.addParticipation(1, 4, "Ingénieur", 10));
        assertEquals("Personne ne peut pas participer à plus de 3 projets en même temps", exception.getMessage());
    }
}

