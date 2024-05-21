package Main;
import Departaments.Departaments;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Departaments departaments = new Departaments();
        boolean exit = false;

        while (!exit) {
            // Display the menu
            System.out.println("Menu Principal");
//            System.out.println("1. Cargar datos ISO");
            System.out.println("2. Mostrar toda la informacion");
            System.out.println("3. Realizar una busqueda por departamento");
            System.out.println("4. Realizar una busqueda por municipio");
            System.out.println("5. Realizar busqueda por codigo especifico");
            System.out.println("6. Salir");

            // Prompt user for choice
            System.out.println("Ingrese su opcion: ");
            System.out.println("------------------------------------------------------------------------------");
            int choice = scanner.nextInt();

            // Handle user's choice
            switch (choice) {
//                case 1:
//                    System.out.println("Cargar datos ISO a tabla de hash");
//                    break;
                case 2:
                    departaments.showDepData();
                    break;
                case 3:
                    System.out.println("Busqueda por departamento");
                    break;
                case 4:
                    System.out.println("Busqueda por municipio");
                    break;
                case 5:
                    System.out.println("Busqueda por codigo especifico");
                    break;
                case 6:
                    System.out.println("Cerrando sesion...");
                    exit = true; // Set exit flag to true
                    break;
                default:
                    System.out.println("Opcioin invalida. por favor ingrese un numero entre 1 y 6.");
            }
        }

        scanner.close();
    }
}
