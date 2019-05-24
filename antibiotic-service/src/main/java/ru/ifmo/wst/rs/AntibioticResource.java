package ru.ifmo.wst.rs;

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
import lombok.SneakyThrows;
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
  @SneakyThrows
  public List<Antibiotics> findAll() {
    return antibioticsDAO.findAll();
  }

  @GET
  @Path("/all")
  @SneakyThrows
  public List<String> findAllAntibiotics() {
    return antibioticsDAO.findAllAntibiotics();
  }

  @GET
  @Path("/find")
  @SneakyThrows
  public String findDosage(
      @QueryParam("name") String name,
      @QueryParam("method") String method,
      @QueryParam("SKF") Integer skf) {
    return antibioticsDAO.findDosage(name, method, skf);
  }


  @GET
  @Path("/filter")
  @SneakyThrows
  public List<Antibiotics> filter(
      @QueryParam("id") Long id,
      @QueryParam("name") String name,
      @QueryParam("method") String method,
      @QueryParam("from") Integer from,
      @QueryParam("to") Integer to,
      @QueryParam("dosage") String dose,
      @QueryParam("additional") String additional) {
    return antibioticsDAO.filter(id, name, method, from, to, dose, additional);
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  @SneakyThrows
  public String create(
      @QueryParam("name") String name,
      @QueryParam("method") String method,
      @QueryParam("from") Integer from,
      @QueryParam("to") Integer to,
      @QueryParam("dosage") String dose,
      @QueryParam("additional") String additional) {
    return String.valueOf(antibioticsDAO.create(name, method, from, to, dose, additional));
  }

  @DELETE
  @Produces(MediaType.TEXT_PLAIN)
  @Path("/{id}")
  @SneakyThrows
  public String delete(@PathParam("id") long id) {
    return String.valueOf(antibioticsDAO.delete(id));
  }

  @POST
  @Produces(MediaType.TEXT_PLAIN)
  @Path("/{id}")
  @SneakyThrows
  public String update(
      @QueryParam("id") Long id,
      @QueryParam("name") String name,
      @QueryParam("method") String method,
      @QueryParam("from") Integer from,
      @QueryParam("to") Integer to,
      @QueryParam("dosage") String dosage,
      @QueryParam("additional") String additional) {
    return String.valueOf(antibioticsDAO.update(id, name, method, from, to, dosage, additional));
  }

}