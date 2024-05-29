package Utils;

public class Queries {

    public static final String ALL_DATA = "SELECT `municipalities`.`codigo`, `municipalities`.`municipio`, `departments`.`nombre`, `departments`.`codigo` AS `codigo_dep` , `departments`.`nombre` FROM `municipalities` JOIN `departments` ON `municipalities`.`department_id` = `departments`.`id`";

    public static final String temp = "SELECT * FROM `municipalities`";

    public static final String GET_DATA_BY_MUN = "SELECT m.*, p.prov AS province_name, c.name AS country_name, c.iso_code AS country_code\n" +
            "FROM municipalities m\n" +
            "JOIN provinces p ON m.province_id = p.id\n" +
            "JOIN countries c ON p.country_id = c.id\n" +
            "WHERE p.prov = 'Chimaltenango'";

    public static final String GET_ALL_COUNTRIES_ONLY = "SELECT c.id, c.name, c.iso_code, c.official_name, COUNT(DISTINCT p.id) AS province_count, COUNT(DISTINCT m.id) AS municipality_count FROM countries c LEFT JOIN provinces p ON c.id = p.country_id LEFT JOIN municipalities m ON p.id = m.province_id GROUP BY c.id, c.name, c.iso_code, c.official_name ORDER BY c.iso_code;";

    public static final String GET_ALL_DATA = "SELECT c.id AS country_id, c.name AS country_name, c.iso_code, c.official_name, p.id AS province_id, p.prov_code AS prov_iso, p.prov AS province_name, m.id AS municipality_id, m.mun AS municipality_name, m.mun_code AS mun_iso FROM countries c LEFT JOIN provinces p ON c.id = p.country_id LEFT JOIN municipalities m ON p.id = m.province_id ORDER BY c.iso_code, p.id, m.id;";

    public static final String QUERY_WITH_TOTAL = "SELECT c.id AS country_id, c.name AS country_name, c.iso_code, c.official_name, p.id AS province_id, p.prov_code AS prov_iso, p.prov AS province_name, m.id AS municipality_id, m.mun AS municipality_name, m.mun_code AS mun_iso, COUNT(*) OVER() AS total_count FROM countries c LEFT JOIN provinces p ON c.id = p.country_id LEFT JOIN municipalities m ON p.id = m.province_id ORDER BY c.iso_code, p.id, m.id;";
}
