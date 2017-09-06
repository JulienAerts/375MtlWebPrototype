package ca.uqam.projet.repositories;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import ca.uqam.projet.resources.*;
import java.sql.PreparedStatement;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;


@Component
public class ActiviteRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ActiviteRowMapper mapper;

    private static final String TROUVER_ACTIVITE_PAR_PARAM
            = " select"
            + "     activites.id AS id"
            + "   , nom"
            + "   , description"
            + "   , arrondissement"
            + "   , lieu"
            + "   , ST_X(coordonner) AS lat"
            + "   , ST_Y(coordonner) AS lng"
            + " from"
            + "   activites"
            + " inner join"
            + "   dates"
            + " on"
            + "   dates.id_activite = activites.id";

    public List<Activite> trouverAvecParam(String param) {
        return jdbcTemplate.query(TROUVER_ACTIVITE_PAR_PARAM + param, mapper);
    }

    private static final String TROUVER_TOUT
            = " select"
            + "     activites.id AS id"
            + "   , nom"
            + "   , description"
            + "   , arrondissement"
            + "   , lieu"
            + "   , ST_X(coordonner) AS lat"
            + "   , ST_Y(coordonner) AS lng"
            + " from"
            + "   activites";

    public List<Activite> trouverAuComplet() {
        return jdbcTemplate.query(TROUVER_TOUT, mapper);
    }

    private static final String TROUVER_PAR_ID
            = " SELECT"
            + "    id"
            + "   , nom"
            + "   , description"
            + "   , arrondissement"
            + "   , lieu"
            + "   , ST_X(coordonner) AS lat"
            + "   , ST_Y(coordonner) AS lng"
            + " from"
            + "  activites"
            + " where"
            + "   id = ?";

    public Activite trouverAvecID(int id) {
        return jdbcTemplate.queryForObject(TROUVER_PAR_ID, new Object[]{id}, mapper);
    }

    private static final String TROUVER_DATES_PAR_ID
            = " SELECT"
            + " date_activite"
            + " FROM"
            + " dates"
            + " WHERE"
            + " id_activite = ?";

    public List<Date> trouverDatesActivite(int id) {
        return jdbcTemplate.query(TROUVER_DATES_PAR_ID, new Object[]{id}, new DatesRowMapper());
    }

    private static final String AJOUTER
            = " INSERT INTO activites (id, nom, description, arrondissement, lieu, coordonner)"
            + " VALUES (?, ?, ?, ?, ?, ST_GeogFromText('POINT(' || ? || ' ' || ? || ')')::geometry)"
            + " on conflict do nothing";

    public int ajouterActivite(Activite activite) {
        return jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(AJOUTER);
            ps.setInt(1, activite.getId());
            ps.setString(2, activite.getNom());
            ps.setString(3, activite.getDescription());
            ps.setString(4, activite.getArrondissement());
            ps.setString(5, activite.getLieu().getNom());
            ps.setDouble(7, activite.getLieu().getLat());
            ps.setDouble(6, activite.getLieu().getLng());
            return ps;
        });
    }

    public void ajouterActiviteAvecDate(Activite activite) throws Exception {

        ajouterActivite(activite);
        for (int i = 0; i < activite.getDates().size(); i++) {
            ajouterDates(activite, i);
        }
    }
    private static final String AJOUTER_DATE
            = " INSERT INTO dates (date_activite, id_activite)"
            + " VALUES (?, ?)"
            + " on conflict (date_activite, id_activite) do nothing";

    public int ajouterDates(Activite activite, int index) throws Exception {
        Object timestamp = new java.sql.Timestamp(activite.getDates().get(index).getTime());
        return jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(AJOUTER_DATE);
            ps.setObject(1, timestamp);
            ps.setInt(2, activite.getId());
            return ps;
        });
    }
    private static final String MISE_A_JOUR_ACTIVITE
            = " UPDATE activites"
            + " SET"
            + "       nom = ?"
            + "     , description = ?"
            + "     , arrondissement = ?"
            + "     , lieu = ?"
            + "     , coordonner = ST_SetSRID(ST_MakePoint(?, ?),4326) "
            + " WHERE"
            + "     id = ?";

    private static final String ID_SUIVANT_ACTIVITE
            = " SELECT"
            + "     MAX(id) AS id_suivant"
            + " FROM"
            + "     activites";

    public Integer trouverIdSuivant() {
        return jdbcTemplate.queryForObject(ID_SUIVANT_ACTIVITE, new IntRowMapper());
    }

    public int actionPost(Activite activite) throws Exception {
        int id = trouverIdSuivant() + 1;
        activite.setId(id);
        ajouterActiviteAvecDate(activite);
        return id;
    }

    public void actionPut(int id, Activite activite) throws Exception {
        System.out.println(trouverAvecID(id));
        if (trouverAvecID(id) == null) {
            ajouterActiviteAvecDate(activite);
        } else {
            supprimerDate(id);
            mettreAJour(activite);
            for (int i = 0; i < activite.getDates().size(); i++) {
                ajouterDates(activite, i);
            }
        }
    }

    public int mettreAJour(Activite activite) {
        return jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(MISE_A_JOUR_ACTIVITE);
            ps.setString(1, activite.getNom());
            ps.setString(2, activite.getDescription());
            ps.setString(3, activite.getArrondissement());
            ps.setString(4, activite.getLieu().getNom());
            ps.setDouble(6, activite.getLieu().getLat());
            ps.setDouble(5, activite.getLieu().getLng());
            ps.setInt(7, activite.getId());
            return ps;
        });
    }

    public void supprimer(int id) {
        supprimerDate(id);
        supprimerActivite(id);

    }

    private static final String SUPPRIMER_ACTIVITE
            = " DELETE FROM activites"
            + " WHERE id = ?";

    public int supprimerActivite(int id) {
        return jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(SUPPRIMER_ACTIVITE);
            ps.setInt(1, id);
            return ps;
        });
    }

    private static final String SUPPRIMER_DATE_ACTIVITE
            = " DELETE FROM dates"
            + " WHERE id_activite = ?";

    public int supprimerDate(int id) {
        return jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(SUPPRIMER_DATE_ACTIVITE);
            ps.setInt(1, id);
            return ps;
        });
    }
}

class DatesRowMapper implements RowMapper<Date> {

    @Override
    public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
        Timestamp temp = rs.getTimestamp("date_activite");   
        return new Date(temp.getTime());
    }
}

class IntRowMapper implements RowMapper<Integer> {

    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("id_suivant");
    }
}
