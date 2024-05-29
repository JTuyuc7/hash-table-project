package Countries;

import DbConnection.DbConnection;
import Reports.Report;
import Utils.FormatMessage;
import Utils.Queries;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import java.sql.ResultSet;
import java.sql.SQLException;

import Utils.UtilsMethod;


public class Countries {
    DbConnection db;
    ResultSet countries;
    Map<String, Country> countryMap;
    FormatMessage formatMessage = new FormatMessage();
    Map<String, String> data = new LinkedHashMap<>();

    UtilsMethod utils = new UtilsMethod();
    public Countries () {
        db = new DbConnection();
        countryMap = new HashMap<>();
    }

    public void displayContent (Scanner scanner) throws SQLException {
        loadCountriesInfo();
        showCountriesInfo();

        int choice = 0;
        boolean validChoice = false;

        while (!validChoice){
            System.out.println("Ingrese una opcion: ");
            System.out.println("1. Mostrar tabla de datos");
            System.out.println("2. Realizar una busqueda por pais");
            System.out.println("3. Regresar al menu principal");

            choice = scanner.nextInt();

            switch (choice){
                case 1:
                    showCountriesInfo();
                    break;
                case 2:
                    System.out.println("Ingrese el Codigo ISO del Pais a buscar: ");
                    String country_iso_code = scanner.next();
                    searchCountryByCode(country_iso_code.toUpperCase());
                    break;
                case 3:
                    System.out.println("Regresando al menu principal...");
                    validChoice = true;
                    break;
                default:
                    System.out.println("Ingrese una opcion valida");
            }
        }
    }


    public void searchCountryByCode(String isoCode){
        Report report = new Report(2, "ciudades" );
        long startTime = System.nanoTime();
        Country country = countryMap.get(isoCode);
        if(country == null){
            System.out.println("Pais con el codigo ISO " + isoCode.toUpperCase() + " no encontrado");
        }else {
            formatMessage.headerCountryBox();
            formatMessage.bodyCountryBox(country.isoCode, country.name, country.officialName, country.province_count, country.municipality_count);
            formatMessage.footerEndBox(160);
        }
        long endTime = System.nanoTime();
        String timeFormatted = utils.formatTime(startTime, endTime);
        String timeCrated = utils.getFomattedDate();
        assert country != null;
        data.put("Resumen creado", timeCrated);
        data.put("Tiempo que tomo", timeFormatted);
        data.put("Codigo buscado", country.isoCode);
        data.put("Nombre Pais buscado", country.name);
        data.put("Provincias/departamentos", String.valueOf(country.province_count));
        data.put("Municpios", String.valueOf(country.municipality_count));
        report.createReport(data);
    }

    public void loadCountriesInfo() throws SQLException {
        Report report = new Report(1, "ciudades" );
        long startTime = System.nanoTime();
        countries = getDataFromDb();
        while (countries.next()) {
            String name = countries.getString("name");
            String isoCode = countries.getString("iso_code");
            String officialName = countries.getString("official_name");
            int prov_count = countries.getInt("province_count");
            int mun_count = countries.getInt("municipality_count");
            Country country = new Country (name, isoCode, officialName, prov_count, mun_count);
            countryMap.put(isoCode, country);
        }
        long endTime = System.nanoTime(); // Measure end time

        String timeFormatted = utils.formatTime(startTime, endTime);
        String timeCrated = utils.getFomattedDate();
        assert countryMap != null;
        data.put("Tipo resumen", "Carga datos ciudades");
        data.put("Resumen creado", timeCrated);
        data.put("Tiempo que tomo", timeFormatted);
        report.createReport(data);
    }

    public void showCountriesInfo()  {
        formatMessage.headerCountryBox();
        for (Map.Entry<String, Country> entry : countryMap.entrySet()) {
            Country country = entry.getValue();
            formatMessage.bodyCountryBox(country.isoCode, country.name, country.officialName, country.province_count, country.municipality_count);
        }
        formatMessage.footerEndBox(160);
    }

    public ResultSet getDataFromDb() throws SQLException {
        return db.readFromDb(Queries.GET_ALL_COUNTRIES_ONLY);
    }
}
