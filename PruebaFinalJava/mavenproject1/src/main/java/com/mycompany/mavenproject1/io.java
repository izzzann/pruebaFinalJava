package com.mycompany.mavenproject1;

import java.util.Scanner;

class io {
    private static Scanner input = new Scanner(System.in);

    public static String leerString() {
        return input.nextLine();
    }

    public static int leerInt() {
        while (true) {
            if (input.hasNextInt()) {
                int numero = input.nextInt();
                input.nextLine();
                return numero;
            } else {
                System.out.print("Tienes que introducir un numero valido: ");
                input.nextLine();
            }
        }
    }

    public static Double leerDouble() {
        while (true) {
            if (input.hasNextDouble()) {
                Double numero = input.nextDouble();
                input.nextLine();
                return numero;
            } else {
                System.out.print("Tienes que introducir un numero valido: ");
                input.nextLine();
            }
        }
    }
}