package ru.ifmo.wst.client;


import static java.text.MessageFormat.format;
import static ru.ifmo.wst.client.Utils.readInt;
import static ru.ifmo.wst.client.Utils.readLong;
import static ru.ifmo.wst.client.Utils.readString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import ru.ifmo.wst.entity.Antibiotics;

public class Client {
  private static AntibioticResourceClient antibioticClient;

  public static void main(String[] args) throws IOException {
    URL url = new URL(args[0]);
    antibioticClient = new AntibioticResourceClient(url.toString());

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    int curState = 0;

    while (true) {
      switch (curState) {
        case 0:
          System.out.println("\nВыберите один из пунктов:");
          System.out.println("1. Вывести все антибиотики");
          System.out.println("2. Вывести все дозировки антибиотиков");
          System.out.println("3. Получить дозу антибиотика по уровню СКФ");
          System.out.println("4. Применить фильтры");
          System.out.println("5. Создать");
          System.out.println("6. Изменить");
          System.out.println("7. Удалить");
          System.out.println("8. Выйти");
          curState = readState(curState, reader);
          break;
        case 1:
          findAll();
          curState = 0;
          break;
        case 2:
          findAllDosages();
          curState = 0;
          break;
        case 3:
          getDosageBySKF(reader);
          curState = 0;
          break;
        case 4:
          filter(reader);
          curState = 0;
          break;
        case 5:
          create(reader);
          curState = 0;
          break;
        case 6:
          update(reader);
          curState = 0;
          break;
        case 7:
          delete(reader);
          curState = 0;
          break;
        case 8:
          return;
        default:
          curState = 0;
          break;
      }
    }
  }

  private static void findAll() {
    Result<List<String>> result = antibioticClient.findAllAntibiotics();

    if (result.isError()) {
      System.err.println(result.getErrorMessage());
    } else {
      List<String> drugs = result.getResult();

      if (drugs.size() == 0) {
        System.out.println("Ничего не найдено");
      } else {
        System.out.println("Найдено:");
        drugs.forEach(System.out::println);
      }
    }
  }

  private static void findAllDosages() {
    Result<List<Antibiotics>> result = antibioticClient.findAll();

    if (result.isError()) {
      System.err.println(result.getErrorMessage());
    } else {
      List<Antibiotics> drugs = result.getResult();

      if (drugs.size() == 0) {
        System.out.println("Ничего не найдено");
      } else {
        System.out.println("Найдено:");
        drugs.stream().map(Antibiotics::toString).forEach(System.out::println);
      }
    }
  }

  private static void getDosageBySKF(BufferedReader reader) {
    System.out.println("\nЗаполните все поля");
    String name;
    do {
      System.out.println("Название:");
      name = readString(reader);
    } while (name == null);

    System.out.println("Метод введения:");
    String method = readString(reader);

    System.out.println("СКФ (мл/мин):");
    Integer skf = readInt(reader);

    Result<String> result = antibioticClient.findDosage(name, method, skf);

    if (result.isError()) {
      System.err.println(result.getErrorMessage());
    } else {
      System.out.println("Найдено:");
      System.out.println(result.getResult());
    }
  }

  private static void filter(BufferedReader reader) {
    System.out.println("\nЧтобы не применять фильтр, оставьте значение пустым");

    System.out.println("id:");
    Long id = readLong(reader);

    System.out.println("Название:");
    String name = readString(reader);

    System.out.println("Метод введения:");
    String method = readString(reader);

    System.out.println("СКФ от:");
    Integer from = readInt(reader);

    System.out.println("СКФ до:");
    Integer to = readInt(reader);

    System.out.println("Дозировка:");
    String dosage = readString(reader);

    System.out.println("Дополнительное поле:");
    String additional = readString(reader);

    Result<List<Antibiotics>> result = antibioticClient
        .filter(id, name, method, from, to, dosage, additional);

    if (result.isError()) {
      System.err.println(result.getErrorMessage());
    } else {
      List<Antibiotics> drugs = result.getResult();

      if (drugs.size() == 0) {
        System.out.println("Ничего не найдено");
      } else {
        System.out.println("Найдено:");
        drugs.stream().map(Antibiotics::toString).forEach(System.out::println);
      }
    }

  }

  private static void create(BufferedReader reader) {
    System.out.println("\nЗаполните поля (* - обязательные)");
    String name;
    do {
      System.out.println("* Название:");
      name = readString(reader);
    } while (name == null);

    System.out.println("Метод введения:");
    String method = readString(reader);

    System.out.println("СКФ От (0 если пустое):");
    Integer from = readInt(reader, 0);
    System.out.println("СКФ До (1000 если пустое):");
    Integer to = readInt(reader, 1000);

    String dosage;
    do {
      System.out.println("* Дозировка:");
      dosage = readString(reader);
    } while (dosage == null);

    System.out.println("Дополнительно:");
    String additional = readString(reader);

    if (additional != null && !dosage.endsWith("*")) {
      dosage += "*";
    }

    Result<Long> result = antibioticClient.create(name, method, from, to, dosage, additional);
    if (result.isError()) {
      System.err.println(result.getErrorMessage());
    } else {
      System.out.println(format("ID новой записи: {0}", result.getResult()));
    }
  }

  private static void update(BufferedReader reader) {
    Long id;

    do {
      System.out.println("id изменяемой записи (0 для отмены операции):");
      id = readLong(reader);
    } while (id == null);

    if (id == 0L) {
      return;
    }

    System.out.println("* Название:");
    String name = readString(reader);

    System.out.println("Метод введения:");
    String method = readString(reader);

    System.out.println("СКФ От (0 если пустое):");
    Integer from = readInt(reader, 0);

    System.out.println("СКФ До (1000 если пустое):");
    Integer to = readInt(reader, 1000);

    System.out.println("* Дозировка:");
    String dosage = readString(reader);

    System.out.println("Дополнительно:");
    String additional = readString(reader);

    Result<Long> result = antibioticClient.update(id, name, method, from, to, dosage, additional);
    if (result.isError()) {
      System.err.println(result.getErrorMessage());
    } else {
      System.out.println(format("Изменено {0} строк(а)", result.getResult()));
    }
  }

  private static void delete(BufferedReader reader) {
    Long id;
    do {
      System.out.println("id удаляемой записи (0 для отмены операции):");
      id = readLong(reader);
    } while (id == null);

    if (id == 0L) {
      return;
    }

    Result<Long> result = antibioticClient.delete(id);
    if (result.isError()) {
      System.err.println(result.getErrorMessage());
    } else {
      System.out.println(format("Удалено {0} строк(а)", result.getResult()));
    }
  }

  private static int readState(int cur, BufferedReader reader) {
    try {
      return Integer.parseInt(reader.readLine());
    } catch (Exception e) {
      return cur;
    }
  }

}
