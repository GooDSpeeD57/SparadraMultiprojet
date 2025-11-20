package training.afpa.cda24060.modele;

import training.afpa.cda24060.ClasseDAO.MedicamentDAO;
import training.afpa.cda24060.exception.SaisieException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PanelMedicament extends JPanel {

    private JTable tableMedicament;
    private DefaultTableModel modelMedicament;

    private JTextField txtNomMedicament, txtCategoriMedicament, txtPrixMedicament;
    private JTextField txtDateMiseCirculation, txtQuantiteMedicament;
    private JComboBox<String> cbSansOrdonnanceMedicament;
    private JTextField txtRechercheNomMedicament, txtRechercheCategorieMedicament;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final MedicamentDAO medicamentDAO = new MedicamentDAO();

    public PanelMedicament() {
        initComponents();
        chargerMedicaments();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelSaisieMedicament = creerPanelSaisie();
        JPanel panelRechercheMedicament = creerPanelRecherche();

        JPanel panelSuperior = new JPanel(new GridLayout(1, 2));
        panelSuperior.add(panelSaisieMedicament);
        panelSuperior.add(panelRechercheMedicament);

        creerTable();
        JScrollPane scrollMedicament = new JScrollPane(tableMedicament);
        scrollMedicament.setBorder(new TitledBorder("Liste des Médicaments"));

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollMedicament, BorderLayout.CENTER);
    }

    private JPanel creerPanelSaisie() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Nouveau Médicament"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        ajouterChamp(panel, "Nom :", txtNomMedicament = new JTextField(15), 0, 0, gbc);
        ajouterChamp(panel, "Catégorie :", txtCategoriMedicament = new JTextField(15), 0, 1, gbc);
        ajouterChamp(panel, "Prix (€) :", txtPrixMedicament = new JTextField(15), 0, 2, gbc);
        ajouterChamp(panel, "Date mise en circulation :", txtDateMiseCirculation = new JTextField(15), 2, 0, gbc);
        ajouterChamp(panel, "Quantité :", txtQuantiteMedicament = new JTextField(15), 2, 1, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Sans ordonnance :"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cbSansOrdonnanceMedicament = new JComboBox<>(new String[]{"Oui","Non"});
        panel.add(cbSansOrdonnanceMedicament, gbc);

        JPanel panelBoutons = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVider = new JButton("Vider");

        btnAjouter.addActionListener(e -> ajouterMedicament());
        btnModifier.addActionListener(e -> modifierMedicament());
        btnSupprimer.addActionListener(e -> supprimerMedicament());
        btnVider.addActionListener(e -> viderChamps());

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnVider);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        panel.add(panelBoutons, gbc);

        return panel;
    }

    private JPanel creerPanelRecherche() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Recherche"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx=0; gbc.gridy=0;
        panel.add(new JLabel("Par nom:"), gbc);
        gbc.gridx=1;
        txtRechercheNomMedicament = new JTextField(15);
        panel.add(txtRechercheNomMedicament, gbc);
        gbc.gridx=2;
        JButton btnRechercheNom = new JButton("🔍");
        btnRechercheNom.addActionListener(e -> rechercherParNom());
        panel.add(btnRechercheNom, gbc);

        gbc.gridx=0; gbc.gridy=1;
        panel.add(new JLabel("Par catégorie:"), gbc);
        gbc.gridx=1;
        txtRechercheCategorieMedicament = new JTextField(15);
        panel.add(txtRechercheCategorieMedicament, gbc);
        gbc.gridx=2;
        JButton btnRechercheCategorie = new JButton("🔍");
        btnRechercheCategorie.addActionListener(e -> rechercherParCategorie());
        panel.add(btnRechercheCategorie, gbc);

        JButton btnAfficherTous = new JButton("Afficher tous");
        btnAfficherTous.addActionListener(e -> chargerMedicaments());
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=3;
        panel.add(btnAfficherTous, gbc);

        return panel;
    }

    private void creerTable() {
        String[] colonnes = {"ID","Nom", "Catégorie", "Prix", "Date Circulation", "Quantité", "Sans Ordonnance"};
        modelMedicament = new DefaultTableModel(colonnes,0){
            @Override
            public boolean isCellEditable(int row,int column){ return false;}
        };
        tableMedicament = new JTable(modelMedicament);
        tableMedicament.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Masquer la colonne ID
        TableColumn colID = tableMedicament.getColumnModel().getColumn(0);
        colID.setMinWidth(0);
        colID.setMaxWidth(0);
        colID.setPreferredWidth(0);

        tableMedicament.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2) chargerMedicamentDansFormulaire();
            }
        });
    }

    private void ajouterChamp(JPanel panel, String label, JTextField field,int colLabel,int row,GridBagConstraints gbc){
        gbc.gridx=colLabel; gbc.gridy=row; gbc.anchor=GridBagConstraints.EAST; gbc.fill=GridBagConstraints.NONE;
        panel.add(new JLabel(label),gbc);
        gbc.gridx=colLabel+1; gbc.fill=GridBagConstraints.HORIZONTAL;
        panel.add(field,gbc);
    }

    private void ajouterMedicament() {
        try {
            String nom = txtNomMedicament.getText().trim();
            String categorie = txtCategoriMedicament.getText().trim();
            double prix = Double.parseDouble(txtPrixMedicament.getText().trim());
            int quantite = Integer.parseInt(txtQuantiteMedicament.getText().trim());
            if (quantite <= 0) throw new SaisieException("La quantité doit être supérieure à 0 !");
            LocalDate date = LocalDate.parse(txtDateMiseCirculation.getText().trim(), formatter);
            boolean sansOrdonnance = "Oui".equals(cbSansOrdonnanceMedicament.getSelectedItem());

            Medicament med = new Medicament(nom, categorie, prix, date, quantite, sansOrdonnance);

            medicamentDAO.insert(med);
            chargerMedicaments();
            viderChamps();
            JOptionPane.showMessageDialog(this, "Médicament ajouté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Prix et quantité doivent être des nombres valides !", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez dd-MM-yyyy.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur base : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierMedicament() {
        int row = tableMedicament.getSelectedRow();
        if(row==-1){ JOptionPane.showMessageDialog(this,"Sélectionnez un médicament."); return; }
        try{
            int id = (int) modelMedicament.getValueAt(row,0);
            Medicament med = medicamentDAO.findById(id);

            med.setNomMedicament(txtNomMedicament.getText());
            med.setCategorieMedicament(txtCategoriMedicament.getText());
            med.setPrixMedicament(Double.parseDouble(txtPrixMedicament.getText()));
            med.setQuantiteMedicament(Integer.parseInt(txtQuantiteMedicament.getText()));
            med.setDateMiseEnCirculation(LocalDate.parse(txtDateMiseCirculation.getText(),formatter));
            med.setSansOrdonnanceMedicament("Oui".equals(cbSansOrdonnanceMedicament.getSelectedItem()));

            medicamentDAO.update(med);
            chargerMedicaments();
            viderChamps();
            JOptionPane.showMessageDialog(this,"Médicament modifié !");
        } catch(SaisieException e){
            JOptionPane.showMessageDialog(this,"Erreur de saisie : "+e.getMessage(),"Erreur",JOptionPane.ERROR_MESSAGE);
        } catch(Exception e){
            JOptionPane.showMessageDialog(this,"Erreur : "+e.getMessage());
        }
    }

    private void supprimerMedicament() {
        int row = tableMedicament.getSelectedRow();
        if(row==-1){ JOptionPane.showMessageDialog(this,"Sélectionnez un médicament."); return; }
        try{
            int id = (int) modelMedicament.getValueAt(row,0);
            int confirm = JOptionPane.showConfirmDialog(this,"Confirmer la suppression ?","Supprimer",JOptionPane.YES_NO_OPTION);
            if(confirm==JOptionPane.YES_OPTION){
                medicamentDAO.delete(id);
                chargerMedicaments();
                viderChamps();
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(this,"Erreur : "+e.getMessage());
        }
    }

    public void chargerMedicaments() {
        modelMedicament.setRowCount(0);
        List<Medicament> medicaments = medicamentDAO.findAll();
        for(Medicament med: medicaments){
            modelMedicament.addRow(new Object[]{
                    med.getIdMedicament(),
                    med.getNomMedicament(),
                    med.getCategorieMedicament(),
                    med.getPrixMedicament()+" €",
                    med.getDateMiseEnCirculation().format(formatter),
                    med.getQuantiteMedicament(),
                    med.isSansOrdonnanceMedicament()?"Oui":"Non"
            });
        }
    }

    private void chargerMedicamentDansFormulaire(){
        int row = tableMedicament.getSelectedRow();
        if(row!=-1){
            int id = (int) modelMedicament.getValueAt(row,0);
            Medicament med = medicamentDAO.findById(id);
            if(med!=null){
                txtNomMedicament.setText(med.getNomMedicament());
                txtCategoriMedicament.setText(med.getCategorieMedicament());
                txtPrixMedicament.setText(String.valueOf(med.getPrixMedicament()));
                txtDateMiseCirculation.setText(med.getDateMiseEnCirculation().format(formatter));
                txtQuantiteMedicament.setText(String.valueOf(med.getQuantiteMedicament()));
                cbSansOrdonnanceMedicament.setSelectedItem(med.isSansOrdonnanceMedicament()?"Oui":"Non");
            }
        }
    }

    private void rechercherParNom(){
        String nom = txtRechercheNomMedicament.getText().trim().toLowerCase();
        modelMedicament.setRowCount(0);
        List<Medicament> medicaments = medicamentDAO.findAll();
        for(Medicament med: medicaments){
            if(med.getNomMedicament().toLowerCase().contains(nom)){
                modelMedicament.addRow(new Object[]{
                        med.getIdMedicament(),
                        med.getNomMedicament(),
                        med.getCategorieMedicament(),
                        med.getPrixMedicament()+" €",
                        med.getDateMiseEnCirculation().format(formatter),
                        med.getQuantiteMedicament(),
                        med.isSansOrdonnanceMedicament()?"Oui":"Non"
                });
            }
        }
    }

    private void rechercherParCategorie(){
        String cat = txtRechercheCategorieMedicament.getText().trim().toLowerCase();
        modelMedicament.setRowCount(0);
        List<Medicament> medicaments = medicamentDAO.findAll();
        for(Medicament med: medicaments){
            if(med.getCategorieMedicament().toLowerCase().contains(cat)){
                modelMedicament.addRow(new Object[]{
                        med.getIdMedicament(),
                        med.getNomMedicament(),
                        med.getCategorieMedicament(),
                        med.getPrixMedicament()+" €",
                        med.getDateMiseEnCirculation().format(formatter),
                        med.getQuantiteMedicament(),
                        med.isSansOrdonnanceMedicament()?"Oui":"Non"
                });
            }
        }
    }

    private void viderChamps(){
        txtNomMedicament.setText("");
        txtCategoriMedicament.setText("");
        txtPrixMedicament.setText("");
        txtDateMiseCirculation.setText("");
        txtQuantiteMedicament.setText("");
        cbSansOrdonnanceMedicament.setSelectedIndex(0);
    }
}
