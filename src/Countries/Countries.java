package Countries;

import DbConnection.DbConnection;
import Utils.FormatMessage;
import Utils.Queries;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Countries {
    DbConnection db;
    ResultSet countries;
    Map<String, Country> countryMap;
    FormatMessage formatMessage = new FormatMessage();
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
        Country country = countryMap.get(isoCode);
        System.out.println(country + "country" + isoCode.toUpperCase());
        if(country == null){
            System.out.println("Pais con el codigo ISO " + isoCode.toUpperCase() + " no encontrado");
        }else {
            formatMessage.headerCountryBox();
            formatMessage.bodyCountryBox(country.isoCode, country.name, country.officialName, country.province_count, country.municipality_count);
            formatMessage.footerEndBox(160);
        }
    }

    public void loadCountriesInfo() throws SQLException {
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
