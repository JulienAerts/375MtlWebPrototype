package ca.uqam.projet.controllers;

import ca.uqam.projet.repositories.ActiviteRepository;
import ca.uqam.projet.resources.Activite;
import ca.uqam.projet.resources.Erreur;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activites-375e")
public class ActiviteController {

    private static final double LAT_PK = 45.50894093;
    private static final double LNG_PK = -73.56863737;
    private static final int RAD_PK = 5000;

    @Autowired
    private ActiviteRepository repository;
    
    @RequestMapping(value = "/activites", method = RequestMethod.GET)
    public List<Activite> findAll() {
        return repository.trouverAuComplet();
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity get(@RequestParam(required = false) Integer rayon, @RequestParam(required = false) Double lat, @RequestParam(required = false) Double lng,
            @RequestParam(required = false) String du, @RequestParam(required = false) String au) {

        /* 
        Si aucun rayon et aucune coordonnées GPS n'ont été données, le système ne doit pas appliquer les valeurs par défaut associées à ces valeurs.
        Si aucune date n'a été donnée, le système ne doit pas appliquer les valeurs par défaut associées à ces valeurs.
         */
        if ((rayon == null && lat == null && lng == null) || (du == null && au == null)|| (du == "" || au == "")) {
            return new ResponseEntity<>(new Erreur(400, "Erreur de syntaxe dans la requete"), HttpStatus.BAD_REQUEST);
        } else {
            if (du == null) {
                du = genererDateHier();
            }
            if (au == null) {
                au = genererDateDemain();
            }
            if (rayon == null) {
                rayon = RAD_PK;
            }
            if (lat == null) {
                lat = LAT_PK;
            }
            if (lng == null) {
                lng = LNG_PK;
            }
            String param = " where ";
            param += "ST_Distance_Sphere(";
            param += "     coordonner::geometry, ";
            param += "     ST_MakePoint(" + lng + "," + lat + " )::geometry";
            param += ") ";
            param += "<= " + rayon;
            param += " and";
            param += " date_activite >= " + "'" + du + "'";
            param += " and";
            param += " date_activite <= " + "'" + au + "'";
            if (repository.trouverAvecParam(param).isEmpty()) {
                return new ResponseEntity<>(new Erreur(404, "Aucune activite trouve avec les parametres fournis"), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(repository.trouverAvecParam(param), HttpStatus.OK);
            }
        }
    }

    private String genererDateHier() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    private String genererDateDemain() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +1);
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity post(@RequestBody Activite activite) {
        int id = -1;
        try {
            id = repository.actionPost(activite);
        } catch (Exception ex) {
            return new ResponseEntity<>(new Erreur(400, "Erreur de syntaxe dans la requete"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Ajout d'une nouvelle activitee complete", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity put(@PathVariable("id") int id, @RequestBody Activite activite) {
        try {
            repository.actionPut(id, activite);
        } catch (Exception ex) {
            return new ResponseEntity<>(new Erreur(400, "Erreur de syntaxe dans la requete"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Mise a jours complete", HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") int id) {
        repository.supprimer(id);
        return new ResponseEntity<>("Supression complete", HttpStatus.OK);
    }

}
