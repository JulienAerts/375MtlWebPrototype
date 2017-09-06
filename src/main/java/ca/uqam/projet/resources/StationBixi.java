package ca.uqam.projet.resources;

import org.springframework.stereotype.*;

@Component
public class StationBixi {

    private int id;
    private String nom;
    private String id_station;
    private int etat;
    private boolean est_bloquer;
    private boolean est_maintenance;
    private boolean est_hors_usages;
    private long temps_derniere_mise;
    private long temps_derniere_com;
    private boolean bk;
    private boolean bl;
    private double lat;
    private double lng;
    private int nb_termino_dispo;
    private int nb_termino_non_dispo;
    private int nb_bixi_dispo;
    private int nb_bixi_non_dispo;

    public StationBixi() {
    }

    public StationBixi(int id,
            String nom,
            String id_station,
            int etat,
            boolean est_bloquer,
            boolean est_maintenance,
            boolean est_hors_usages,
            long temps_derniere_mise,
            long temps_derniere_com,
            boolean bk,
            boolean bl,
            double lat,
            double lng,
            int nb_termino_dispo,
            int nb_termino_non_dispo,
            int nb_bixi_dispo,
            int nb_bixi_non_dispo) {
        this.id = id;
        this.nom = nom;
        this.id_station = id_station;
        this.etat = etat;
        this.est_bloquer = est_bloquer;
        this.est_maintenance = est_maintenance;
        this.est_hors_usages = est_hors_usages;
        this.temps_derniere_mise = temps_derniere_mise;
        this.temps_derniere_com = temps_derniere_com;
        this.bk = bk;
        this.bl = bl;
        this.lat = lat;
        this.lng = lng;
        this.nb_termino_dispo = nb_termino_dispo;
        this.nb_termino_non_dispo = nb_termino_non_dispo;
        this.nb_bixi_dispo = nb_bixi_dispo;
        this.nb_bixi_non_dispo = nb_bixi_non_dispo;
    }

    
    @Override
    public String toString() {
        return String.format("«%s» --%s", id, nom, id_station);
    }
    
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getIdStation() {
        return id_station;
    }

    public int getEtat() {
        return etat;
    }

    public boolean getEst_bloquer() {
        return est_bloquer;
    }

    public boolean getEst_maintenance() {
        return est_maintenance;
    }

    public boolean getEst_hors_usages() {
        return est_hors_usages;
    }

    public long getTemps_derniere_mise() {
        return temps_derniere_mise;
    }

    public long getTemps_derniere_com() {
        return temps_derniere_com;
    }
    
    public boolean getBk(){ 
        return bk;
    }
    
    public boolean getBl(){ 
        return bl;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getNb_termino_dispo() {
        return nb_termino_dispo;
    }

    public int getNb_termino_non_dispo() {
        return nb_termino_non_dispo;
    }

    public int getNb_bixi_dispo() {
        return nb_bixi_dispo;
    }

    public int getNb_bixi_non_dispo() {
        return nb_bixi_non_dispo;
    }

}
