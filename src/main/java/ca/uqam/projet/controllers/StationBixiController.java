/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.uqam.projet.controllers;

import ca.uqam.projet.repositories.StationBixiRepository;
import ca.uqam.projet.resources.Erreur;
import ca.uqam.projet.resources.StationBixi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stations-bixi")
public class StationBixiController {
    
    private static final double LAT_PK = 45.50894093;
    private static final double LNG_PK = -73.56863737;
    private static final int RAD_PK = 5000;
    
    
    @Autowired
    private StationBixiRepository repository;
    
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity get(@RequestParam(defaultValue = "0") Integer min_bixi_dispo, @RequestParam(defaultValue = "5000") Integer rayon, 
            @RequestParam(defaultValue = "45.50894093") Double lat, @RequestParam(defaultValue = "-73.56863737") Double lng) {
        
        String param = " where";
        param += " nb_bixi_dispo >= " + min_bixi_dispo;
        param += " and ";
        param += "ST_Distance_Sphere(";
        param += "     coordonner::geometry, ";
        param += "     ST_MakePoint(" + lng + "," + lat +" )::geometry";
        param += ") ";
        param += "<= " + rayon;
        
        if (repository.trouverAvecParam(param).isEmpty()) {
                return new ResponseEntity<>(new Erreur(404, "Aucune station bixi trouve avec les parametres fournis"), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(repository.trouverAvecParam(param), HttpStatus.OK);
            }
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getId(@PathVariable("id") int id) {

         if (repository.trouverAvecID(id).isEmpty() || repository.trouverAvecID(id).size() > 1 ) {
                return new ResponseEntity<>(new Erreur(404, "Station demander non trouver"), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(repository.trouverAvecID(id).get(0), HttpStatus.OK);
            }
    }
}
