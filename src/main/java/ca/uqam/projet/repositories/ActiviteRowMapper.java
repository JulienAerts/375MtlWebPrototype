/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.uqam.projet.repositories;
import ca.uqam.projet.resources.Activite;
import ca.uqam.projet.resources.Lieu;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


@Component
public class ActiviteRowMapper implements RowMapper<Activite> {

    @Autowired
    ActiviteRepository activityRepository;

    @Override
    public Activite mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Date> dates = activityRepository.trouverDatesActivite(rs.getInt("id"));
        Lieu lieu = new Lieu( rs.getString("lieu"),rs.getDouble("lat"),rs.getDouble("lng"));
        Activite activite = new Activite( rs.getInt("id"),rs.getString("nom"),rs.getString("description"),rs.getString("arrondissement"),dates,lieu);
        return activite;
   
    }
}
