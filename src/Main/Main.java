package Main;
import AllData.CompleteData;
import Countries.Countries;
import Departaments.Departaments;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Departaments departaments = new Departaments();
        Countries countries = new Countries();
        CompleteData allData = new CompleteData();
        boolean exit = false;

        while (!exit) {
            // Display the menu
            System.out.println("Menu Principal");
            System.out.println("1. Mostrar informacion (Solo paises)");
            System.out.println("2. Mostrar toda la informacion");
            System.out.println("3. Salir");

            // Prompt user for choice
            System.out.println("Ingrese su opcion: ");
            System.out.println("------------------------------------------------------------------------------");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    countries.displayContent(scanner);
                    break;
                case 2:
                    allData.displayContent(scanner);
                    break;
                case 3:
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
