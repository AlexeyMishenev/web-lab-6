package ru.ifmo.wst;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.HttpServer;
import ru.ifmo.wst.dao.AntibioticsDAO;
import ru.ifmo.wst.rs.AntibioticResource;
import ru.ifmo.wst.utils.Configuration;

@Slf4j
public class App {
  public static void main(String[] args) throws IOException {
    Configuration conf = new Configuration("config.properties");
    String scheme = conf.get("scheme", "http:");
    String host = conf.get("host", "localhost");
    String port = conf.get("port", "8081");
    String baseUrl = scheme + "//" + host + ":" + port;

    String appName = conf.get("app.name", "standalone-lab4");
    String appUrl = baseUrl + "/" + appName;

    DataSource dataSource = initDataSource();
    AntibioticResource.STATIC_DAO = new AntibioticsDAO(dataSource);

    ClassNamesResourceConfig resourceConfig = new ClassNamesResourceConfig(
        AntibioticResource.class);

    log.info("Start application");

    HttpServer httpServer = GrizzlyServerFactory.createHttpServer(appUrl, resourceConfig);
    httpServer.start();
    Runtime.getRuntime().addShutdownHook(new Thread(httpServer::stop));

    log.info("Application was successfully started");

    System.in.read();
  }

  @SneakyThrows
  private static DataSource initDataSource() {
    InputStream dsPropsStream = App.class.getClassLoader()
        .getResourceAsStream("datasource.properties");
    Properties dsProps = new Properties();
    dsProps.load(dsPropsStream);
    HikariConfig hikariConfig = new HikariConfig(dsProps);
    return new HikariDataSource(hikariConfig);
  }
}
