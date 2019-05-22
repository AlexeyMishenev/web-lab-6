package ru.ifmo.wst.rs;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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

}