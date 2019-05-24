package ru.ifmo.wst.client;

import static com.sun.jersey.api.client.ClientResponse.Status.OK;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
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

  private static final GenericType<String> STRING = new GenericType<String>() {
  };

  @NonNull
  private final String baseUrl;

  private final WebResource rootResource;

  AntibioticResourceClient(String baseUrl) {
    this.baseUrl = baseUrl + "/antibiotics";
    this.rootResource = Client.create().resource(url());
  }

  List<Antibiotics> findAll() {
    ClientResponse response = rootResource
        .path("/all_rows")
        .accept(APPLICATION_JSON_TYPE)
        .get(ClientResponse.class);

    if (response.getStatus() != OK.getStatusCode()) {
      throw new IllegalStateException("Request failed");
    }

    return response.getEntity(ANTIBIOTICS_LIST);
  }

  List<Antibiotics> findAllAntibiotics() {
    ClientResponse response = rootResource
        .path("/all")
        .accept(APPLICATION_JSON_TYPE)
        .get(ClientResponse.class);

    if (response.getStatus() != OK.getStatusCode()) {
      throw new IllegalStateException("Request failed");
    }

    return response.getEntity(ANTIBIOTICS_LIST);
  }

  List<Antibiotics> filter(Long id, String name, String method, Integer from, Integer to,
      String dose, String additional) {
    WebResource resource = rootResource;
    resource = addParam(resource, "id", id);
    resource = addParam(resource, "name", name);
    resource = addParam(resource, "method", method);
    resource = addParam(resource, "from", from);
    resource = addParam(resource, "to", to);
    resource = addParam(resource, "dosage", dose);
    resource = addParam(resource, "additional", additional);
    ClientResponse response = resource
        .path("/filter")
        .accept(APPLICATION_JSON_TYPE)
        .get(ClientResponse.class);

    if (response.getStatus() != OK.getStatusCode()) {
      throw new IllegalStateException("Request failed");
    }

    return response.getEntity(ANTIBIOTICS_LIST);
  }

  List<Antibiotics> findDosage(String name, String method, Integer skf) {
    WebResource resource = rootResource;
    resource = addParam(resource, "name", name);
    resource = addParam(resource, "method", method);
    resource = addParam(resource, "SKF", skf);
    ClientResponse response = resource
        .path("/find")
        .accept(APPLICATION_JSON_TYPE)
        .get(ClientResponse.class);

    if (response.getStatus() != OK.getStatusCode()) {
      throw new IllegalStateException("Request failed");
    }

    return response.getEntity(ANTIBIOTICS_LIST);
  }

  public Long create(String name, String method, Integer from, Integer to,
      String dose, String additional) {

    WebResource resource = rootResource;
    resource = addParam(resource, "name", name);
    resource = addParam(resource, "method", method);
    resource = addParam(resource, "from", from);
    resource = addParam(resource, "to", to);
    resource = addParam(resource, "dosage", dose);
    resource = addParam(resource, "additional", additional);

    ClientResponse response = resource
        .accept(TEXT_PLAIN_TYPE, APPLICATION_JSON_TYPE)
        .put(ClientResponse.class);

    if (response.getStatus() != OK.getStatusCode()) {
      throw new IllegalStateException("Request failed");
    }

    return Long.parseLong(response.getEntity(STRING));

  }

  public Long update(long id, String name, String method, Integer from, Integer to,
      String dose, String additional) {

    WebResource resource = rootResource;
    resource = addParam(resource, "name", name);
    resource = addParam(resource, "method", method);
    resource = addParam(resource, "from", from);
    resource = addParam(resource, "to", to);
    resource = addParam(resource, "dosage", dose);
    resource = addParam(resource, "additional", additional);

    ClientResponse response = resource
        .path(String.valueOf(id))
        .accept(MediaType.TEXT_PLAIN)
        .post(ClientResponse.class);

    if (response.getStatus() != OK.getStatusCode()) {
      throw new IllegalStateException("Request failed");
    }

    return Long.parseLong(response.getEntity(STRING));
  }

  public long delete(long id) {
    ClientResponse response = rootResource
        .path(String.valueOf(id))
        .accept(TEXT_PLAIN_TYPE)
        .delete(ClientResponse.class);

    if (response.getStatus() != OK.getStatusCode()) {
      throw new IllegalStateException("Request failed");
    }

    return Long.parseLong(response.getEntity(STRING));
  }

  private String url(String endpointAddress) {
    return baseUrl + endpointAddress;
  }

  private String url() {
    return baseUrl;
  }

  private WebResource addParam(WebResource webResource, String name, Object param) {
    if (param == null) {
      return webResource;
    }
    return webResource.queryParam(name, param.toString());
  }
}