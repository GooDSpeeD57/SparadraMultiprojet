-- =========================================
--      BASE DE DONNÉES : PHARMACIE
-- =========================================
DROP DATABASE IF EXISTS sparadra;
CREATE DATABASE IF NOT EXISTS sparadra;
USE sparadra;

CREATE TABLE Medicament(
   id_Medicament INT AUTO_INCREMENT PRIMARY KEY,
   nomMedicament VARCHAR(100) NOT NULL,
   categorie VARCHAR(50),
   prix DECIMAL(7,2) NOT NULL,
   dateCirculation DATE,
   stock INT DEFAULT 0,
   forme VARCHAR(50) NOT NULL,
   sansOrdonnance BOOLEAN NOT NULL
);

CREATE TABLE Medecin(
   id_Medecin INT AUTO_INCREMENT PRIMARY KEY,
   nomMedecin VARCHAR(50) NOT NULL,
   prenomMedecin VARCHAR(50) NOT NULL,
   adresseMedecin VARCHAR(100),
   codePostalMedecin VARCHAR(10),
   villeMedecin VARCHAR(50),
   telephoneMedecin VARCHAR(17),
   mailMedecin VARCHAR(100) NOT NULL,
   rppsMedecin VARCHAR(12) NOT NULL UNIQUE
);

CREATE TABLE Mutuelle(
   id_Mutuelle INT AUTO_INCREMENT PRIMARY KEY,
   nomMutuelle VARCHAR(100) NOT NULL,
   adresseMutuelle VARCHAR(100),
   codePostalMutuelle VARCHAR(10),
   villeMutuelle VARCHAR(50),
   telephoneMutuelle VARCHAR(17),
   mailMutuelle VARCHAR(100) NOT NULL,
   departementMutuelle VARCHAR(50),
   tRemboursement DECIMAL(5,2) NOT NULL
);

CREATE TABLE Pharmacien(
   id_Pharmacien INT AUTO_INCREMENT PRIMARY KEY,
   nomPharmacien VARCHAR(50) NOT NULL,
   prenomPharmacien VARCHAR(50) NOT NULL,
   rppsPharmacien VARCHAR(12) NOT NULL UNIQUE
);

CREATE TABLE Fournisseur(
   id_Fournisseur INT AUTO_INCREMENT PRIMARY KEY,
   nomFournisseur VARCHAR(100) NOT NULL,
   adresseFournisseur VARCHAR(150),
   mailFournisseur VARCHAR(100),
   telephoneFournisseur VARCHAR(16)
);

CREATE TABLE Regime(
   id_Regime INT AUTO_INCREMENT PRIMARY KEY,
   nomRegime VARCHAR(100) NOT NULL,
   tauxRemboursement DECIMAL(5,2) NOT NULL
);

CREATE TABLE Client(
   id_Client INT AUTO_INCREMENT PRIMARY KEY,
   nomClient VARCHAR(50) NOT NULL,
   prenomClient VARCHAR(50) NOT NULL,
   adresseClient VARCHAR(100),
   codePostalClient VARCHAR(10),
   villeClient VARCHAR(50),
   telephoneClient VARCHAR(17),
   mailClient VARCHAR(100) NOT NULL,
   nssClient VARCHAR(15) NOT NULL UNIQUE,
   dateNaissance DATE NOT NULL,
   id_Regime INT NOT NULL,
   id_Medecin INT,
   id_Mutuelle INT,
   idTitulaireMutuelle VARCHAR(50),
   FOREIGN KEY(id_Regime) REFERENCES Regime(id_Regime),
   FOREIGN KEY(id_Medecin) REFERENCES Medecin(id_Medecin),
   FOREIGN KEY(id_Mutuelle) REFERENCES Mutuelle(id_Mutuelle)
);

CREATE TABLE Ordonnance(
   id_Ordonnance INT AUTO_INCREMENT PRIMARY KEY,
   dateCreation DATE NOT NULL,
   id_Client INT NOT NULL,
   id_Medecin INT NOT NULL,
   FOREIGN KEY(id_Client) REFERENCES Client(id_Client),
   FOREIGN KEY(id_Medecin) REFERENCES Medecin(id_Medecin)
);

CREATE TABLE Achat(
   id_Achat INT AUTO_INCREMENT PRIMARY KEY,
   dateAchat DATE NOT NULL,
   typeAchat VARCHAR(50),
   id_Pharmacien INT NOT NULL,
   id_Client INT NOT NULL,
   FOREIGN KEY(id_Pharmacien) REFERENCES Pharmacien(id_Pharmacien),
   FOREIGN KEY(id_Client) REFERENCES Client(id_Client)
);

CREATE TABLE Facturation(
   id_Facture INT AUTO_INCREMENT PRIMARY KEY,
   dateFacture DATE NOT NULL,
   montantTotal DECIMAL(9,2) NOT NULL,
   modePaiement VARCHAR(50),
   id_Achat INT NOT NULL,
   FOREIGN KEY(id_Achat) REFERENCES Achat(id_Achat)
);

CREATE TABLE Compose(
   id_Medicament INT,
   id_Ordonnance INT,
   quantite INT DEFAULT 1,
   PRIMARY KEY(id_Medicament, id_Ordonnance),
   FOREIGN KEY(id_Medicament) REFERENCES Medicament(id_Medicament),
   FOREIGN KEY(id_Ordonnance) REFERENCES Ordonnance(id_Ordonnance)
);

CREATE TABLE Contient(
   id_Medicament INT,
   id_Achat INT,
   quantite INT DEFAULT 1,
   PRIMARY KEY(id_Medicament, id_Achat),
   FOREIGN KEY(id_Medicament) REFERENCES Medicament(id_Medicament),
   FOREIGN KEY(id_Achat) REFERENCES Achat(id_Achat)
);

CREATE TABLE AServi(
   id_Pharmacien INT,
   id_Ordonnance INT,
   dateService DATE NOT NULL,
   PRIMARY KEY(id_Pharmacien, id_Ordonnance),
   FOREIGN KEY(id_Pharmacien) REFERENCES Pharmacien(id_Pharmacien),
   FOREIGN KEY(id_Ordonnance) REFERENCES Ordonnance(id_Ordonnance)
);

CREATE TABLE Ordo_Achat(
   id_Ordonnance INT,
   id_Achat INT,
   PRIMARY KEY(id_Ordonnance, id_Achat),
   FOREIGN KEY(id_Ordonnance) REFERENCES Ordonnance(id_Ordonnance),
   FOREIGN KEY(id_Achat) REFERENCES Achat(id_Achat)
);

CREATE TABLE SeFournit(
   id_Medicament INT,
   id_Fournisseur INT,
   PRIMARY KEY(id_Medicament, id_Fournisseur),
   FOREIGN KEY(id_Medicament) REFERENCES Medicament(id_Medicament),
   FOREIGN KEY(id_Fournisseur) REFERENCES Fournisseur(id_Fournisseur)
);

CREATE TABLE Prescription(
    id_Prescription INT AUTO_INCREMENT PRIMARY KEY,
    id_Ordonnance INT NOT NULL,
    nomMedicament VARCHAR(255) NOT NULL,
    prixUnitaire DOUBLE NOT NULL,
    quantitePrescrite INT NOT NULL,
    FOREIGN KEY (id_Ordonnance) REFERENCES Ordonnance(id_Ordonnance)
);

-- Insertion des mutuelles
INSERT INTO Mutuelle (nomMutuelle, adresseMutuelle, codePostalMutuelle, villeMutuelle, telephoneMutuelle, mailMutuelle,departementMutuelle, tRemboursement)
VALUES
('Caisse Régional Crédit Agricole Mutuelle Lorraine', '56 Avenue André Malraux', '57000', 'Metz', '09 64 40 37 11', 'contact@camutuellelorraine.fr','Moselle', 70),
('Mutuelle Nationale Territoriale Section Metz', '1 rue du Pont Moreau', '57000', 'Metz', '03 87 37 58 32', 'service@mnt.fr','Moselle', 75),
('Mutuelle Nationale Territoriale Agence Mitterrand', '16 avenue François Mitterrand', '57000', 'Metz', '09 72 72 02 02', 'metz@mnt.fr','Moselle', 75),
('Mutlor Les Mutuelles de Lorraine', '11 Rue du Colonel Merlin', '54400', 'Longwy', '03 82 25 79 00', 'contact@mutlor.fr','Moselle', 65),
('Mutlor Les Mutuelles de Lorraine Nancy', '6 Rue de la Visitation', '54000', 'Nancy', '+33 3 83 36 77 07', 'nancy@mutlor.fr','Meurthe-et-Moselle', 65);

-- Insertion des médecins
INSERT INTO Medecin (nomMedecin, prenomMedecin, adresseMedecin, codePostalMedecin, villeMedecin, telephoneMedecin, mailMedecin, rppsMedecin)
VALUES
('Bertrand', 'Anne', '1 Bis avenue Coteaux', '57155', 'Marly', '03 87 69 00 22', 'bertrand.anne@example.com', '10002402179'),
('Breton', 'Jean Christophe', '8 Grand Rue', '57525', 'Talange', '03 87 71 48 42', 'breton.jc@example.com', '10002383924'),
('Albrecht', 'Corinne', '1 Bis avenue Coteaux', '57155', 'Marly', '03 87 62 38 81', 'albrecht.corinne@example.com', '10002378361'),
('Piroué', 'Elsa', '8 rue Messageries', '57000', 'Metz', '03 87 63 26 60', 'piroue.elsa@example.com', '10003769360'),
('Grosdidier', 'Laurène', '1 Bis avenue Coteaux', '57155', 'Marly', '03 87 63 39 77', 'grosdidier.laurene@example.com', '10101379872'),
('Masserann', 'Jean Luc', '6 rue Coislin', '57000', 'Metz', '03 87 75 21 63', 'masserann.jl@example.com', '10002550118'),
('Courtalon', 'Didier', '12 rue Devilly', '57070', 'Metz', '03 87 65 23 23', 'courtalon.didier@example.com', '10002357019'),
('Dugny', 'Christophe', '148 bis rue de Marly', '57950', 'Montigny-lès-Metz', '03 87 65 79 93', 'dugny.christophe@example.com', '10002394921'),
('Pellerini', 'André', '9 rue de Chatelaillon', '57515', 'Alsting', '03 87 00 33 57', 'pellerini.andre@example.com', '10002366945'),
('Blaise', 'Guy', '1 rue du Poitou', '57110', 'Yutz', '03 82 56 21 63', 'blaise.guy@example.com', '10002364601');

-- Insertion des régimes 
INSERT INTO Regime (nomRegime, tauxRemboursement) VALUES
('ALD', 100.00),
('Régime général', 70.00),
('Régime étudiant', 60.00),
('Régime agricole', 70.00),
('Régime des indépendants', 65.00),
('Régime complémentaire mutuelle', 80.00),
('Régime chômage', 70.00),
('Régime spécifique fonction publique', 75.00),
('Régime retraite', 70.00),
('Régime accident du travail', 100.00);

-- Insertion des clients
INSERT INTO Client (nomClient, prenomClient, adresseClient, codePostalClient, villeClient, telephoneClient, mailClient, nssClient, dateNaissance,id_Regime, id_Mutuelle, id_Medecin, idTitulaireMutuelle)
VALUES
('Martin', 'Luc', '12 Rue des Lilas', '57000', 'Metz', '06 12 34 56 78', 'luc.martin@example.com', '185107512341', '1980-06-15', 1, 1, 1, 'MUTA7F4K2L'),
('Durand', 'Sophie', '5 Avenue Foch', '57100', 'Thionville', '06 23 45 67 89', 'sophie.durand@example.com', '185107512342', '1975-11-22', 2, 2, 2, 'MUTB9X8V3Q'),
('Leroy', 'Pierre', '8 Rue de la Gare', '54110', 'Dombasle-sur-Meurthe', '06 34 56 78 90', 'pierre.leroy@example.com', '185107512343', '1990-03-03', 3, 3, 3, 'MUTC1R5N7Z'),
('Moreau', 'Claire', '20 Boulevard des Vosges', '54520', 'Laxou', '06 45 67 89 01', 'claire.moreau@example.com', '185107512344', '1985-09-19', 4, 4, 4, 'MUTD6Q2H9S'),
('Dubois', 'Julien', '14 Rue Anatole France', '54210', 'Saint-Nicolas-de-Port', '06 56 78 90 12', 'julien.dubois@example.com', '185107512345', '1978-12-10', 5, 5, 5, 'MUTE4J7P8K'),
('Petit', 'Amélie', '3 Place SaintLouis', '57600', 'Forbach', '06 67 89 01 23', 'amelie.petit@example.com', '185107512346', '1982-05-30', 1, 1, 6, 'MUTF2L9X0M'),
('Rousseau', 'Antoine', '2 Rue Michel Debré', '57600', 'Forbach', '06 78 90 12 34', 'antoine.rousseau@example.com', '185107512347', '1972-08-25', 2, 2, 7, 'MUTG8C3V1Y'),
('Faure', 'Laura', '7 Rue des Grands Champs', '57070', 'Vantoux', '06 89 01 23 45', 'laura.faure@example.com', '185107512348', '1992-02-14', 3, 3, 8, 'MUTH5K2N7R'),
('Gauthier', 'Nicolas', '1A Rue de Verdun', '57120', 'Pierrevillers', '06 90 12 34 56', 'nicolas.gauthier@example.com', '185107512349', '1988-07-05', 4, 4, 9, 'MUTI7X4B6L'),
('Laurent', 'Élodie', '20a Rue de la Gare', '57300', 'Hagondange', '06 01 23 45 67', 'elodie.laurent@example.com', '185107512350', '1995-10-12', 5, 5, 10, 'MUTJ3R9Q8P');

-- Insertion des pharmaciens
INSERT INTO Pharmacien (nomPharmacien, prenomPharmacien, rppsPharmacien)
VALUES
('Brunner', 'Jennifer', '10101069606'),
('Peltier', 'Jean-Julien', '10001204113'),
('Marconato', 'Isabelle', '10001195113'),
('Schillinger', 'Sarah', '10101010101'),
('Rowdo', 'Lorene', '10010101010');

-- Insertion des médicaments
INSERT INTO Medicament (nomMedicament, categorie, prix, dateCirculation, stock, forme, sansOrdonnance)
VALUES
('Doliprane', 'Antalgique', 2.50, '2021-01-15', 150, 'Comprimé', TRUE),
('Amoxicilline', 'Antibiotique', 8.90, '2020-06-30', 75, 'Comprimé', FALSE),
('Spasfon', 'Antispasmodique', 3.20, '2019-11-10', 120, 'Comprimé', TRUE),
('Ibuprofène', 'Anti-inflammatoire', 4.10, '2022-03-01', 200, 'Comprimé', TRUE),
('Ventoline', 'Bronchodilatateur', 15.00, '2018-05-25', 50, 'Inhalateur', FALSE),
('Xanax', 'Anxiolytique', 12.75, '2017-09-12', 30, 'Comprimé', FALSE),
('Efferalgan', 'Antalgique', 2.90, '2020-12-20', 180, 'Comprimé', TRUE),
('Aspirine', 'Anticoagulant', 3.00, '2016-07-14', 90, 'Comprimé', TRUE),
('Zyrtec', 'Antihistaminique', 6.50, '2021-04-08', 110, 'Comprimé', TRUE),
('Levothyrox', 'Hormonothérapie', 9.60, '2015-02-01', 60, 'Comprimé', FALSE);