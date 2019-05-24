package ru.ifmo.wst.rs;

import static java.text.MessageFormat.format;

import java.sql.SQLException;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import ru.ifmo.wst.dao.AntibioticsDAO;
import ru.ifmo.wst.entity.Antibiotics;


@RequestScoped
@Path("/antibiotics")
@Produces({MediaType.APPLICATION_JSON})
public class AntibioticResource {
  public static AntibioticsDAO STATIC_DAO;

  @Inject
  private AntibioticsDAO antibioticsDAO;

  public AntibioticResource() {
    antibioticsDAO = STATIC_DAO;
  }

  @GET
  @Path("/all_rows")
  public List<Antibiotics> findAll() throws ResourceException {
    try {
      return antibioticsDAO.findAll();
    } catch (SQLException e) {
      throw new ResourceException(
          format("SQL error: {0}; State:{1}", e.getMessage(), e.getSQLState()));
    }
  }

  @GET
  @Path("/all")
  public List<String> findAllAntibiotics() throws ResourceException {
    try {
      return antibioticsDAO.findAllAntibiotics();
    } catch (SQLException e) {
      throw new ResourceException(
          format("SQL error: {0}; State:{1}", e.getMessage(), e.getSQLState()));
    }
  }

  @GET
  @Path("/find")
  public String findDosage(
      @QueryParam("name") String name,
      @QueryParam("method") String method,
      @QueryParam("SKF") Integer skf) throws ResourceException {
    try {
      return antibioticsDAO.findDosage(name, method, skf);
    } catch (SQLException e) {
      throw new ResourceException(
          format("SQL error: {0}; State:{1}", e.getMessage(), e.getSQLState()));
    }
  }


  @GET
  @Path("/filter")
  public List<Antibiotics> filter(
      @QueryParam("id") Long id,
      @QueryParam("name") String name,
      @QueryParam("method") String method,
      @QueryParam("from") Integer from,
      @QueryParam("to") Integer to,
      @QueryParam("dosage") String dose,
      @QueryParam("additional") String additional) throws ResourceException {
    try {
      return antibioticsDAO.filter(id, name, method, from, to, dose, additional);
    } catch (SQLException e) {
      throw new ResourceException(
          format("SQL error: {0}; State:{1}", e.getMessage(), e.getSQLState()));
    }
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public String create(
      @QueryParam("name") String name,
      @QueryParam("method") String method,
      @QueryParam("from") Integer from,
      @QueryParam("to") Integer to,
      @QueryParam("dosage") String dose,
      @QueryParam("additional") String additional) throws ResourceException {
    try {
      return String.valueOf(antibioticsDAO.create(name, method, from, to, dose, additional));
    } catch (SQLException e) {
      throw new ResourceException(
          format("SQL error: {0}; State:{1}", e.getMessage(), e.getSQLState()));
    }
  }

  @DELETE
  @Produces(MediaType.TEXT_PLAIN)
  @Path("/{id}")
  public String delete(@PathParam("id") long id) throws ResourceException {
    try {
      return String.valueOf(antibioticsDAO.delete(id));
    } catch (SQLException e) {
      throw new ResourceException(
          format("SQL error: {0}; State:{1}", e.getMessage(), e.getSQLState()));
    }
  }

  @POST
  @Produces(MediaType.TEXT_PLAIN)
  @Path("/{id}")
  public String update(
      @QueryParam("id") Long id,
      @QueryParam("name") String name,
      @QueryParam("method") String method,
      @QueryParam("from") Integer from,
      @QueryParam("to") Integer to,
      @QueryParam("dosage") String dosage,
      @QueryParam("additional") String additional) throws ResourceException {
    try {
      return String.valueOf(antibioticsDAO.update(id, name, method, from, to, dosage, additional));
    } catch (SQLException e) {
      throw new ResourceException(
          format("SQL error: {0}; State:{1}", e.getMessage(), e.getSQLState()));
    }
  }

}