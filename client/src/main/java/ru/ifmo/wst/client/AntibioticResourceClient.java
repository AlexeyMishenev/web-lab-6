package ru.ifmo.wst.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.util.List;
import javax.ws.rs.core.MediaType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.wst.entity.Antibiotics;

@Slf4j
@RequiredArgsConstructor
class AntibioticResourceClient {

  private static final GenericType<List<Antibiotics>> ANTIBIOTICS_LIST = new GenericType<List<Antibiotics>>() {
  };

  @NonNull
  private final String baseUrl;

  private final WebResource findAllResource;
  private final WebResource findAllAntibiotics;
  private final WebResource filterResource;
  private final WebResource findDosage;

  AntibioticResourceClient(String baseUrl) {
    this.baseUrl = baseUrl + "/antibiotics";
    this.findAllResource = Client.create().resource(url("/all_rows"));
    this.findAllAntibiotics = Client.create().resource(url("/all"));
    this.filterResource = Client.create().resource(url("/filter"));
    this.findDosage = Client.create().resource(url("/find"));
  }

  List<Antibiotics> findAll() {
    return findAllResource.accept(MediaType.APPLICATION_JSON_TYPE).get(ANTIBIOTICS_LIST);
  }

  List<Antibiotics> findAllAntibiotics() {
    return findAllAntibiotics.accept(MediaType.APPLICATION_JSON_TYPE).get(ANTIBIOTICS_LIST);
  }

  List<Antibiotics> filter(Long id, String name, String method, Integer from, Integer to,
      String dose, String additional) {
    WebResource resource = filterResource;
    resource = addParam(resource, "id", id);
    resource = addParam(resource, "name", name);
    resource = addParam(resource, "method", method);
    resource = addParam(resource, "from", from);
    resource = addParam(resource, "to", to);
    resource = addParam(resource, "dosage", dose);
    resource = addParam(resource, "additional", additional);
    return resource.accept(MediaType.APPLICATION_JSON_TYPE).get(ANTIBIOTICS_LIST);
  }

  List<Antibiotics> findDosage(String name, String method, Integer skf) {
    WebResource resource = findDosage;
    resource = addParam(resource, "name", name);
    resource = addParam(resource, "method", method);
    resource = addParam(resource, "SKF", skf);
    return resource.accept(MediaType.APPLICATION_JSON_TYPE).get(ANTIBIOTICS_LIST);
  }

  private String url(String endpointAddress) {
    return baseUrl + endpointAddress;
  }

  private WebResource addParam(WebResource webResource, String name, Object param) {
    if (param == null) {
      return webResource;
    }
    return webResource.queryParam(name, param.toString());
  }
}