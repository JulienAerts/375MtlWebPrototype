/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.uqam.projet.tasks;

import ca.uqam.projet.repositories.StationBixiRepository;
import ca.uqam.projet.resources.StationBixi;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class FetchStationsBixiTask {
    
    @Autowired private StationBixiRepository repository;
    private static final String URL = "https://secure.bixi.com/data/stations.json";

    @Scheduled(cron = "0 */10 * * * ?")
    @PostConstruct
    public void deserialisationStationsBixi() {
        try {
                RandomExtraStation stations = new RestTemplate().getForObject(URL, RandomExtraStation.class);
                stations.randomStationBixi.stream()
                        .map(this::asStationBixi)
                        .forEach((station) -> {
                            try {
                                repository.ajouter(station);
                            } catch (Exception ex) {
                                System.out.println(ex);
                            }
                        });
        } catch (ResourceAccessException e) {
            System.out.println("Erreur d'acces URL : " + e);
        }catch (RestClientException e) {
             System.out.println("Erreur dans deserialisationStationsBixi() : " + e);
        }

    }
    

    private StationBixi asStationBixi(RandomStationBixi b) {

        return new StationBixi(
                b.id,
                b.nom,
                b.id_station,
                b.etat,
                b.est_bloquer,
                b.est_maintenance,
                b.est_hors_usages,
                b.temps_derniere_mise,
                b.temps_derniere_com,
                b.bk,
                b.bl,
                b.lat,
                b.lng,
                b.nb_termino_dispo,
                b.nb_termino_non_dispo,
                b.nb_bixi_dispo,
                b.nb_bixi_non_dispo
        );
    }
}
    class RandomExtraStation {

    @JsonProperty("stations")
    List<RandomStationBixi> randomStationBixi;
    @JsonProperty("schemeSuspended")
    boolean schemeSuspended;
    @JsonProperty("timestamp")
    long timestamp;
    }
    class RandomStationBixi {

    @JsonProperty("id")
    int id;
    @JsonProperty("s")
    String nom;
    @JsonProperty("n")
    String id_station;
    @JsonProperty("st")
    int etat;
    @JsonProperty("b")
    boolean est_bloquer;
    @JsonProperty("su")
    boolean est_maintenance;
    @JsonProperty("m")
    boolean est_hors_usages;
    @JsonProperty("lu")
    long temps_derniere_mise;
    @JsonProperty("lc")
    long temps_derniere_com;
    @JsonProperty("bk")
    boolean bk;
    @JsonProperty("bl")
    boolean bl;
    @JsonProperty("la")
    double lat;
    @JsonProperty("lo")
    double lng;
    @JsonProperty("da")
    int nb_termino_dispo;
    @JsonProperty("dx")
    int nb_termino_non_dispo;
    @JsonProperty("ba")
    int nb_bixi_dispo;
    @JsonProperty("bx")
    int nb_bixi_non_dispo;
}

