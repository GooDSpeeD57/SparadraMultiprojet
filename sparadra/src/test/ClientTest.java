package test;

import exception.SaisieException;
import modele.Client;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class ClientTest {

    @Test
    void testClientValide() throws SaisieException {
        Client c = new Client("Martin", "Luc", "12 Rue des Lilas", "57000", "Metz",
                "0612345678", "luc.martin@example.com", "185107512341",
                "15/06/1980", "Mutuelle A", "Dr Dupont");
        assertEquals("Martin", c.getNom());
        assertEquals("Luc", c.getPrenom());
        assertEquals("185107512341", c.getNss());
    }

    @Test
    void testNSSInvalide() {
        assertThrows(SaisieException.class, () ->
                new Client("Martin", "Luc", "12 Rue des Lilas", "57000", "Metz",
                        "0612345678", "luc.martin@example.com", "123",
                        "15/06/1980", "Mutuelle A", "Dr Dupont")
        );
    }

    @Test
    void testRechercheClientParNom() throws SaisieException {
        new Client("Martin", "Luc", "12 Rue des Lilas", "57000", "Metz",
                "0612345678", "luc.martin@example.com", "185107512341",
                "15/06/1980", "Mutuelle A", "Dr Dupont");
        List<Client> resultats = Client.rechercherClientParNom("Martin");
        assertFalse(resultats.isEmpty());
        assertEquals("Martin", resultats.get(0).getNom());
    }
}
