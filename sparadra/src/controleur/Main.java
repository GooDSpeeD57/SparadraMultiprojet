package controleur;

import exception.SaisieException;
import modele.*;
import modele.Client;

public class Main {

    public Main() {
    }
    public static void main(String[] args) {

    }
        public static void chargement ()throws SaisieException {
            Client c1 = new Client("Martin", "Luc", "12 Rue des Lilas", "57000", "Metz", "06 12 34 56 78", "luc.martin@example.com", "185107512341", "15/06/1980", "Mutuelle A", "Shiva Marie Khorsand Taefehnorooz");
            Client c2 = new Client("Durand", "Sophie", "5 Avenue Foch", "57100", "Thionville", "06 23 45 67 89", "sophie.durand@example.com", "185107512342", "22/11/1975", "Mutuelle B", "Eric Partouche");
            Client c3 = new Client("Leroy", "Pierre", "8 Rue de la Gare", "54110", "Dombasle-sur-Meurthe", "06 34 56 78 90", "pierre.leroy@example.com", "185107512343", "03/03/1990", "Mutuelle C", "JACQUIER MERCIER Marie");
            Client c4 = new Client("Moreau", "Claire", "20 Boulevard des Vosges", "54520", "Laxou", "06 45 67 89 01", "claire.moreau@example.com", "185107512344", "19/09/1985", "Mutuelle D", "TOURPE Dominique");
            Client c5 = new Client("Dubois", "Julien", "14 Rue Anatole France", "54210", "Saint-Nicolas-de-Port", "06 56 78 90 12", "julien.dubois@example.com", "185107512345", "10/12/1978", "Mutuelle E", "LIGIER Romain");
            Client c6 = new Client("Petit", "Amélie", "3 Place SaintLouis", "57600", "Forbach", "06 67 89 01 23", "amelie.petit@example.com", "185107512346", "30/05/1982", "Mutuelle F", "Cécile Flye Sainte Marie");
            Client c7 = new Client("Rousseau", "Antoine", "2 Rue Michel Debré", "57600", "Forbach", "06 78 90 12 34", "antoine.rousseau@example.com", "185107512347", "25/08/1972", "Mutuelle G", "Ingrid Chiaro");
            Client c8 = new Client("Faure", "Laura", "7 Rue des Grands Champs", "57070", "Vantoux", "06 89 01 23 45", "laura.faure@example.com", "185107512348", "14/02/1992", "Mutuelle H", "Jean Louis Mougenel");
            Client c9 = new Client("Gauthier", "Nicolas", "1A Rue de Verdun", "57120", "Pierrevillers", "06 90 12 34 56", "nicolas.gauthier@example.com", "185107512349", "05/07/1988", "Mutuelle I", "Alisson ANCEL");
            Client c10 = new Client("Laurent", "Élodie", "20a Rue de la Gare", "57300", "Hagondange", "06 01 23 45 67", "elodie.laurent@example.com", "185107512350", "12/10/1995", "Mutuelle J", "Claire PIERRON");
            Medecin m1 = new Medecin("Bertrand", "Anne", "1 Bis avenue Coteaux", "57155", "Marly", "03 87 69 00 22", "bertrand.anne@example.com", "10002402179");
            Medecin m2 = new Medecin("Breton", "Jean Christophe", "8 Grand Rue", "57525", "Talange", "03 87 71 48 42", "breton.jc@example.com", "10002383924");
            Medecin m3 = new Medecin("Albrecht", "Corinne", "1 Bis avenue Coteaux", "57155", "Marly", "03 87 62 38 81", "albrecht.corinne@example.com", "10002378361");
            Medecin m4 = new Medecin("Piroué", "Elsa", "8 rue Messageries", "57000", "Metz", "03 87 63 26 60", "piroue.elsa@example.com", "10003769360");
            Medecin m5 = new Medecin("Grosdidier", "Laurène", "1 Bis avenue Coteaux", "57155", "Marly", "03 87 63 39 77", "grosdidier.laurene@example.com", "10101379872");
            Medecin m6 = new Medecin("Masserann", "Jean Luc", "6 rue Coislin", "57000", "Metz", "03 87 75 21 63", "masserann.jl@example.com", "10002550118");
            Medecin m7 = new Medecin("Courtalon", "Didier", "12 rue Devilly", "57070", "Metz", "03 87 65 23 23", "courtalon.didier@example.com", "10002357019");
            Medecin m8 = new Medecin("Dugny", "Christophe", "148 bis rue de Marly", "57950", "Montigny-lès-Metz", "03 87 65 79 93", "dugny.christophe@example.com", "10002394921");
            Medecin m9 = new Medecin("Pellerini", "André", "9 rue de Chatelaillon", "57515", "Alsting", "03 87 00 33 57", "pellerini.andre@example.com", "10002366945");
            Medecin m10 = new Medecin("Blaise", "Guy", "1 rue du Poitou", "57110", "Yutz", "03 82 56 21 63", "blaise.guy@example.com", "10002364601");
            Pharmacien p1 = new Pharmacien("Brunner", "Jennifer", "10101069606");
            Pharmacien p2 = new Pharmacien("Peltier", "Jean-Julien", "10001204113");
            Pharmacien p3 = new Pharmacien("Marconato", "Isabelle", "10001195113");
            Pharmacien p4 = new Pharmacien("Schillinger", "Sarah", "10101010101");
            Pharmacien p5 = new Pharmacien("Rowdo", "Lorene", "10010101010");
            Mutuelle mu1 = new Mutuelle("Caisse Régional Crédit Agricole Mutuelle Lorraine", "56 Avenue André Malraux", "57000", "Metz", "09 64 40 37 11", "contact@camutuellelorraine.fr", "Moselle", 70);
            Mutuelle mu2 = new Mutuelle("Mutuelle Nationale Territoriale Section Metz", "1 rue du Pont Moreau", "57000", "Metz", "03 87 37 58 32", "service@mnt.fr", "Moselle", 75);
            Mutuelle mu3 = new Mutuelle("Mutuelle Nationale Territoriale Agence Mitterrand", "16 avenue François Mitterrand", "57000", "Metz", "09 72 72 02 02", "metz@mnt.fr", "Moselle", 75);
            Mutuelle mu4 = new Mutuelle("Mutlor Les Mutuelles de Lorraine", "11 Rue du Colonel Merlin", "54400", "Longwy", "03 82 25 79 00", "contact@mutlor.fr", "Moselle", 65);
            Mutuelle mu5 = new Mutuelle("Mutlor Les Mutuelles de Lorraine Nancy", "6 Rue de la Visitation", "54000", "Nancy", "+33 3 83 36 77 07", "nancy@mutlor.fr", "Meurthe-et-Moselle", 65);
            Medicament me1 = new Medicament("Doliprane", "Antalgique", 2.50, "2021-01-15", 150, "Oui");
            Medicament me2 = new Medicament("Amoxicilline", "Antibiotique", 8.90, "2020-06-30", 75, "Non");
            Medicament me3 = new Medicament("Spasfon", "Antispasmodique", 3.20, "2019-11-10", 120, "Oui");
            Medicament me4 = new Medicament("Ibuprofène", "Anti-inflammatoire", 4.10, "2022-03-01", 200, "Oui");
            Medicament me5 = new Medicament("Ventoline", "Bronchodilatateur", 15.00, "2018-05-25", 50, "Non");
            Medicament me6 = new Medicament("Xanax", "Anxiolytique", 12.75, "2017-09-12", 30, "Non");
            Medicament me7 = new Medicament("Efferalgan", "Antalgique", 2.90, "2020-12-20", 180, "Oui");
            Medicament me8 = new Medicament("Aspirine", "Anticoagulant", 3.00, "2016-07-14", 90, "Oui");
            Medicament me9 = new Medicament("Zyrtec", "Antihistaminique", 6.50, "2021-04-08", 110, "Oui");
            Medicament me10 = new Medicament("Levothyrox", "Hormonothérapie", 9.60, "2015-02-01", 60, "Non");
        }
}