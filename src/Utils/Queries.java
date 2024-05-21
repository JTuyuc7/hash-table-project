package Utils;

public class Queries {

    public static final String ALL_DATA = "SELECT `municipalities`.`codigo`, `municipalities`.`municipio`, `departments`.`nombre`, `departments`.`codigo` AS `codigo_dep` , `departments`.`nombre` FROM `municipalities` JOIN `departments` ON `municipalities`.`department_id` = `departments`.`id`";

    public static final String temp = "SELECT * FROM `municipalities`";
}
