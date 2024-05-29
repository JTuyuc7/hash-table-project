package AllData;

import Countries.Country;
import DbConnection.DbConnection;
import Reports.Report;
import Utils.FormatMessage;
import Utils.Queries;
import Utils.UtilsMethod;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class CompleteData {
    DbConnection db;
    ResultSet allInfo;
    Map<CompositeKey, FullData> completeHashMap;
    UtilsMethod utils = new UtilsMethod();
    FormatMessage formatMessage = new FormatMessage();
    Map<String, String> data_report = new LinkedHashMap<>();

    public CompleteData(){
        db = new DbConnection();
        completeHashMap = new HashMap<>();
    }

    public void displayContent (Scanner scanner) throws SQLException {
        loadAllContentInfo();
        showAllContentInfo();

        int choice = 0;
        boolean validChoice = false;

        while (!validChoice){
            System.out.println("Ingrese una opcion: ");
            System.out.println("1. Mostrar tabla de datos");
            System.out.println("2. Realizar una busqueda por Pais (ISO)");
            System.out.println("3. Realizar una busqueda por Provincia/departamento (ISO)");
            System.out.println("4. Realizar una busqueda por municipio (ISO)");
            System.out.println("5. Realizar una busqueda por todos los ISO campos");
            System.out.println("6. Regresar al menu principal");

            choice = scanner.nextInt();

            switch (choice){
                case 1:
                    showAllContentInfo();
                    break;
                case 2:
                    System.out.println("Ingrese el criterio de búsqueda (ISO País): ");
                    String countryIsoCode = scanner.next().trim();
                    searchByIsoCode(countryIsoCode);
                    break;
                case 3:
                    System.out.println("Ingrese el criterio de búsqueda (ISO Provincia: ");
                    String province_iso_code = scanner.next().trim();
                    searchByProvinceIsoCode(province_iso_code);
                    break;
                case 4:
                    System.out.println("Ingrese el criterio de búsqueda (ISO municipio: ");
                    String mun_iso_code = scanner.next().trim();
                    searchByMunicipalityIsoCode(mun_iso_code);
                    break;
                case 5:
                    System.out.println("Ingrese los ISO codes (País, Provincia, Municipio): ");
                    System.out.print("ISO País: ");
                    String countryIso = scanner.next().trim();
                    System.out.print("ISO Provincia: ");
                    String provIso = scanner.next().trim();
                    System.out.print("ISO Municipio: ");
                    String munIso = scanner.next().trim();
                    searchByAllIsoCodes(countryIso, provIso, munIso);
                    break;
                case 6:
                    System.out.println("Regresando al menu principal...");
                    validChoice = true;
                    break;
                default:
                    System.out.println("Ingrese una opcion valida");
            }
        }
    }

    public void loadAllContentInfo() throws SQLException {
        Map<String, String> data_report = new LinkedHashMap<>();
        Report report = new Report(2, "hash_table" );
        long startTime = System.nanoTime();
        allInfo = getDataFromDb();
        while (allInfo.next()){
            String country_name = allInfo.getString("country_name");
            String country_iso_code = allInfo.getString("iso_code");
            String country_official_name = allInfo.getString("official_name");
            String prov_iso_code = allInfo.getString("prov_iso");
            String prov_name = allInfo.getString("province_name");
            String mun_iso_code = allInfo.getString("mun_iso");
            String mun_name = allInfo.getString("municipality_name");
            FullData all_iso_info = new FullData(country_iso_code, country_name, country_official_name, prov_iso_code, prov_name, mun_iso_code, mun_name);
            CompositeKey key = new CompositeKey(country_iso_code, prov_iso_code, mun_iso_code);
            completeHashMap.put(key, all_iso_info);
        }
        long endTime = System.nanoTime(); // Measure end time

        String timeFormatted = utils.formatTime(startTime, endTime);
        String timeCrated = utils.getFomattedDate();

        data_report.put("Tipo resumen", "Carga todos los datos");
        data_report.put("Resumen creado", timeCrated);
        data_report.put("Tiempo que tomo", timeFormatted);
        report.createReport(data_report);
    }

    public void showAllContentInfo() throws SQLException {
        formatMessage.headerAllDataBox();
        int total= 0;
        for(Map.Entry<CompositeKey, FullData> entry : completeHashMap.entrySet()){
            FullData fullData = entry.getValue();
            total += 1;
            formatMessage.bodyAllDataHashInfo(fullData.country_iso_code, fullData.country_name, fullData.prov_iso_code, fullData.prov_name, fullData.mun_iso_code, fullData.mun_name);
        }
        formatMessage.footerEndBox(210);
//        System.out.println("total data" + total);
    }

    public ResultSet getDataFromDb() throws SQLException {
        return db.readFromDb(Queries.GET_ALL_DATA);
    }

    public void searchByIsoCode(String isoCode) {
        Report report = new Report(2, "hash_table" );
        long startTime = System.nanoTime();
        boolean found = false;
        FullData firstMatch = null;
        formatMessage.headerAllDataBox();
        for (Map.Entry<CompositeKey, FullData> entry : completeHashMap.entrySet()) {
            FullData data = entry.getValue();
            if ((data.country_iso_code != null && data.country_iso_code.equalsIgnoreCase(isoCode)) ||
                    (data.prov_iso_code != null && data.prov_iso_code.equalsIgnoreCase(isoCode)) ||
                    (data.mun_iso_code != null && data.mun_iso_code.equalsIgnoreCase(isoCode))) {
                formatMessage.bodyAllDataHashInfo(data.country_iso_code, data.country_name, data.prov_iso_code, data.prov_name, data.mun_iso_code, data.mun_name);
                firstMatch = data;
                found = true;
            }
        }
        if (!found) {
            System.out.println("No se encontraron coincidencias para el ISO code proporcionado.");
        } else {
            formatMessage.footerEndBox(210);
        }
        long endTime = System.nanoTime();
        String timeFormatted = utils.formatTime(startTime, endTime);
        String timeCrated = utils.getFomattedDate();
        data_report.put("Resumen creado", timeCrated);
        data_report.put("Tiempo que tomo", timeFormatted);
        if(firstMatch != null){
            data_report.put("Codigo buscado", firstMatch.country_iso_code);
            data_report.put("Nombre Pais buscado", firstMatch.country_name);
            report.createReport(data_report);
        }
    }

    public void searchByProvinceIsoCode(String provIsoCode) {
        Report report = new Report(2, "hash_table" );
        long startTime = System.nanoTime();
        boolean found = false;
        FullData firstMatch = null;
        formatMessage.headerAllDataBox();
        for (Map.Entry<CompositeKey, FullData> entry : completeHashMap.entrySet()) {
            FullData data = entry.getValue();
            if (data.prov_iso_code != null && data.prov_iso_code.equalsIgnoreCase(provIsoCode)) {
                formatMessage.bodyAllDataHashInfo(data.country_iso_code, data.country_name, data.prov_iso_code, data.prov_name, data.mun_iso_code, data.mun_name);
                firstMatch = data;
                found = true;
            }
        }
        if (!found) {
            System.out.println("No se encontraron coincidencias para el ISO de la provincia proporcionado.");
        } else {
            formatMessage.footerEndBox(210);
        }

        long endTime = System.nanoTime();
        String timeFormatted = utils.formatTime(startTime, endTime);
        String timeCrated = utils.getFomattedDate();
        data_report.put("Resumen creado", timeCrated);
        data_report.put("Tiempo que tomo", timeFormatted);
        if(firstMatch != null){
            data_report.put("Codigo buscado", firstMatch.country_iso_code);
            data_report.put("Nombre Pais buscado", firstMatch.country_name);
            data_report.put("Nombre de la provincia", firstMatch.prov_name);
            report.createReport(data_report);
        }

    }

    public void searchByMunicipalityIsoCode(String munIsoCode) {
        Report report = new Report(2, "hash_table" );
        long startTime = System.nanoTime();
        FullData firstMatch = null;
        boolean found = false;
        formatMessage.headerAllDataBox();
        for (Map.Entry<CompositeKey, FullData> entry : completeHashMap.entrySet()) {
            FullData data = entry.getValue();
            if (data.mun_iso_code != null && data.mun_iso_code.equalsIgnoreCase(munIsoCode)) {
                formatMessage.bodyAllDataHashInfo(data.country_iso_code, data.country_name, data.prov_iso_code, data.prov_name, data.mun_iso_code, data.mun_name);
                firstMatch = data;
                found = true;
            }
        }
        if (!found) {
            System.out.println("No se encontraron coincidencias para el ISO del municipio proporcionado.");
        } else {
            formatMessage.footerEndBox(210);
        }

        long endTime = System.nanoTime();
        String timeFormatted = utils.formatTime(startTime, endTime);
        String timeCrated = utils.getFomattedDate();
        data_report.put("Resumen creado", timeCrated);
        data_report.put("Tiempo que tomo", timeFormatted);
        if(firstMatch != null){
            data_report.put("Codigo buscado", firstMatch.country_iso_code);
            data_report.put("Nombre Pais buscado", firstMatch.country_name);
            data_report.put("Nombre de la provincia/departamento", firstMatch.prov_name);
            data_report.put("Nombre del municipio", firstMatch.mun_name);
            report.createReport(data_report);
        }
    }

    public void searchByAllIsoCodes(String countryIsoCode, String provIsoCode, String munIsoCode) {
        Report report = new Report(2, "hash_table" );
        long startTime = System.nanoTime();
        FullData firstMatch = null;
        boolean found = false;
        formatMessage.headerAllDataBox();
        for (Map.Entry<CompositeKey, FullData> entry : completeHashMap.entrySet()) {
            FullData data = entry.getValue();
            if (data.country_iso_code != null && data.country_iso_code.equalsIgnoreCase(countryIsoCode) &&
                    data.prov_iso_code != null && data.prov_iso_code.equalsIgnoreCase(provIsoCode) &&
                    data.mun_iso_code != null && data.mun_iso_code.equalsIgnoreCase(munIsoCode)) {
                formatMessage.bodyAllDataHashInfo(data.country_iso_code, data.country_name, data.prov_iso_code, data.prov_name, data.mun_iso_code, data.mun_name);
                firstMatch = data;
                found = true;
            }
        }
        if (!found) {
            System.out.println("No se encontraron coincidencias para los ISO codes proporcionados.");
        } else {
            formatMessage.footerEndBox(210);
        }
        long endTime = System.nanoTime();
        String timeFormatted = utils.formatTime(startTime, endTime);
        String timeCrated = utils.getFomattedDate();
        data_report.put("Resumen creado", timeCrated);
        data_report.put("Tiempo que tomo", timeFormatted);
        if(firstMatch != null){
            data_report.put("Codigo buscado", firstMatch.country_iso_code);
            data_report.put("Nombre Pais buscado", firstMatch.country_name);
            data_report.put("ISO provincia/departamento", firstMatch.prov_iso_code);
            data_report.put("Nombre de la provincia/departamento", firstMatch.prov_name);
            data_report.put("ISO Municipio buscado", firstMatch.mun_iso_code);
            data_report.put("Nombre del municipio", firstMatch.mun_name);
            report.createReport(data_report);
        }
    }


}
