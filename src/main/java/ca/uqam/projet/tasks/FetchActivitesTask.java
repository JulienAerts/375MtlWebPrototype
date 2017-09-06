package ca.uqam.projet.tasks;

import ca.uqam.projet.repositories.ActiviteRepository;
import ca.uqam.projet.resources.Activite;
import ca.uqam.projet.resources.Lieu;
import java.util.*;
import java.io.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.annotation.PostConstruct;
import org.json.simple.parser.ParseException;

@Service
public class FetchActivitesTask {

    private static final double LAT_PK = 45.50894093;
    private static final double LNG_PK = -73.56863737;
    private static final String CHEMIN_FICHIER = "src/main/resources/programmation-375mtl/programmation-parcs.json";

    @Autowired
    private ActiviteRepository repository;

    @PostConstruct
    public void deserialisationActivites() {

        try (FileReader lecteur = new FileReader(CHEMIN_FICHIER)) {

            JSONParser deserialiseur = new JSONParser();
            Object uneActivite = deserialiseur.parse(lecteur);
            JSONArray tableauActivites = (JSONArray) uneActivite;
            JSONObject monActivite;
            JSONObject monLieu;
            ArrayList<String> liste;
            List<Date> listeDates;
            Activite activite;

            for (int i = 0; i < tableauActivites.size(); i++) {
                monActivite = (JSONObject) tableauActivites.get(i);
                monLieu = (JSONObject) monActivite.get("lieu");
                liste = (ArrayList<String>) monActivite.get("dates");
                listeDates = new ArrayList<>();

                for (String s : liste) {
                    Calendar calendar = javax.xml.bind.DatatypeConverter.parseDateTime(s);
                    listeDates.add(new Date(calendar.getTimeInMillis()));
                }
                if (monLieu.containsKey("lat") && monLieu.containsKey("lng")) {
                    activite = new Activite(Integer.parseInt((String) monActivite.get("id")),
                            (String) monActivite.get("nom"),
                            (String) monActivite.get("description"),
                            (String) monActivite.get("arrondissement"),
                            listeDates,
                            new Lieu((String) monLieu.get("nom"),
                                    (double) monLieu.get("lat"),
                                    (double) monLieu.get("lng")
                            )
                    );
                    repository.ajouterActiviteAvecDate(activite);
                }
            }
            lecteur.close();
        } catch (IOException e) {
            System.out.println("Fichier introuvable : " + e);
        } catch (ParseException e) {
            System.out.println("Erreur lors de manipulation JSON : " + e);
        } catch (java.lang.Exception e) {
            System.out.println("Erreur dans deserialisationActivites() : " + e);

        }

    }
}
