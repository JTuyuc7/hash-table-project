package Utils;

public class Queries {

    public static final String ALL_DATA = "SELECT `municipalities`.`codigo`, `municipalities`.`municipio`, `departments`.`nombre`, `departments`.`codigo` AS `codigo_dep` , `departments`.`nombre` FROM `municipalities` JOIN `departments` ON `municipalities`.`department_id` = `departments`.`id`";

    public static final String temp = "SELECT * FROM `municipalities`";

    public static final String GET_DATA_BY_MUN = "SELECT m.*, p.prov AS province_name, c.name AS country_name, c.iso_code AS country_code\n" +
            "FROM municipalities m\n" +
            "JOIN provinces p ON m.province_id = p.id\n" +
            "JOIN countries c ON p.country_id = c.id\n" +
            "WHERE p.prov = 'Chimaltenango'";
}
