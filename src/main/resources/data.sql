-- Initialisation des tables
-- Ce fichier ne peut pas être vide
SELECT 0 as INUTILE;

-- Insert records into Personne
INSERT INTO personne (matricule, nom) VALUES (1, 'Bruce Wayne');
INSERT INTO personne (matricule, nom) VALUES (2, 'Clark Kent');
INSERT INTO personne (matricule, nom) VALUES (3, 'Diana Prince');

-- Insert records into Projet
INSERT INTO projet (code, name) VALUES (1, 'Rénovation de la Batcave');
INSERT INTO projet (code, name) VALUES (2, 'Publication pour le Daily Planet');
INSERT INTO projet (code, name) VALUES (3, 'Protection des Amazones');

-- Insert records into Participation
INSERT INTO participation (id, personne_id, projet_id, role, percentage) VALUES (1, 1, 1, 'PDG', 50);
INSERT INTO participation (id, personne_id, projet_id, role, percentage) VALUES (2, 2, 2, 'Journaliste', 30);
INSERT INTO participation (id, personne_id, projet_id, role, percentage) VALUES (3, 3, 3, 'Reine', 70);

