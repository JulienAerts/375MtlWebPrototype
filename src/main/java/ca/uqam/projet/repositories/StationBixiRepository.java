/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.uqam.projet.repositories;

import ca.uqam.projet.resources.Activite;
import ca.uqam.projet.resources.StationBixi;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class StationBixiRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final String TROUVER_PAR_ID
            = " select"
            + "     id"
            + "   , nom"
            + "   , id_station"
            + "   , etat"
            + "   , est_bloquer"
            + "   , est_maintenance"
            + "   , est_hors_usages"
            + "   , temps_derniere_mise"
            + "   , temps_derniere_com"
            + "   , bk"
            + "   , bl"
            + "   , ST_X(coordonner::geometry) AS lat"
            + "   , ST_Y(coordonner::geometry) AS lng"
            + "   , nb_termino_dispo"
            + "   , nb_termino_non_dispo"
            + "   , nb_bixi_dispo"
            + "   , nb_bixi_non_dispo"
            + " from"
            + "   stations_bixi"
            + " where"
            + "   id = ?";

    public List<StationBixi> trouverAvecID(int id) {
        return jdbcTemplate.query(TROUVER_PAR_ID, new Object[]{id}, new BixiRowMapper());
    }
    
    private static final String TROUVER_STATION_PAR_PARAM
            = " select"
            + "     id"
            + "   , nom"
            + "   , id_station"
            + "   , etat"
            + "   , est_bloquer"
            + "   , est_maintenance"
            + "   , est_hors_usages"
            + "   , temps_derniere_mise"
            + "   , temps_derniere_com"
            + "   , bk"
            + "   , bl"
            + "   , ST_X(coordonner::geometry) AS lat"
            + "   , ST_Y(coordonner::geometry) AS lng"
            + "   , nb_termino_dispo"
            + "   , nb_termino_non_dispo"
            + "   , nb_bixi_dispo"
            + "   , nb_bixi_non_dispo"
            + " from"
            + "   stations_bixi";
    
    public List<StationBixi> trouverAvecParam(String param){
        return jdbcTemplate.query(TROUVER_STATION_PAR_PARAM + param, new BixiRowMapper());
    }
    
    private static final String AJOUTER
            = " INSERT INTO stations_bixi"
            + " (id, "
            + " nom, "
            + " id_station, "
            + " etat, "
            + " est_bloquer, "
            + " est_maintenance, "
            + " est_hors_usages,"
            + " temps_derniere_mise, "
            + " temps_derniere_com, "
            + " bk, "
            + " bl, "
            + " coordonner, "
            + " nb_termino_dispo, "
            + " nb_termino_non_dispo, "
            + " nb_bixi_dispo, "
            + " nb_bixi_non_dispo)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ST_GeogFromText('SRID=4326;POINT(' || ? || ' ' || ? || ')')::geometry, ?, ?, ?, ?)"
            + " on conflict do nothing";

    public void ajouter(StationBixi station) throws Exception {
        ajouterStation(station);
    }

    public int ajouterStation(StationBixi station) throws Exception  {
        return jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(AJOUTER);
            ps.setInt(1, station.getId());
            ps.setString(2, station.getNom());
            ps.setString(3, station.getIdStation());
            ps.setInt(4, station.getEtat());
            ps.setBoolean(5, station.getEst_bloquer());
            ps.setBoolean(6, station.getEst_maintenance());
            ps.setBoolean(7, station.getEst_hors_usages());
            ps.setLong(8, station.getTemps_derniere_mise());
            ps.setLong(9, station.getTemps_derniere_com());
            ps.setBoolean(10, station.getBk());
            ps.setBoolean(11, station.getBl());
            ps.setDouble(13, station.getLat());
            ps.setDouble(12, station.getLng());
            ps.setInt(14, station.getNb_termino_dispo());
            ps.setInt(15, station.getNb_termino_non_dispo());
            ps.setInt(16, station.getNb_bixi_dispo());
            ps.setInt(17, station.getNb_bixi_non_dispo());
            return ps;
        });
    }

}

class BixiRowMapper implements RowMapper<StationBixi> {

    @Override
    public StationBixi mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new StationBixi(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("id_station"),
                rs.getInt("etat"),
                rs.getBoolean("est_bloquer"),
                rs.getBoolean("est_maintenance"),
                rs.getBoolean("est_hors_usages"),
                rs.getLong("temps_derniere_mise"),
                rs.getLong("temps_derniere_com"),
                rs.getBoolean("bk"),
                rs.getBoolean("bl"),
                rs.getDouble("lat"),
                rs.getDouble("lng"),
                rs.getInt("nb_termino_dispo"),
                rs.getInt("nb_termino_non_dispo"),
                rs.getInt("nb_bixi_dispo"),
                rs.getInt("nb_bixi_non_dispo")
        );
    }
}
