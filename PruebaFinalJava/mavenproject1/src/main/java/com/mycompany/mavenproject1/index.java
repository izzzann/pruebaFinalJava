/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mavenproject1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class index {

    public static void main(String[] args) {
        Connection connection = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            String url = "jdbc:oracle:thin:@localhost:1521:";
            String user = "IZAN";
            String password = "1234";

            connection = DriverManager.getConnection(url, user, password);

            
            // Guardar usuario y contraseña, y generar correo
            System.out.println("Quieres iniciar sesion o registrarte: ");
            System.out.println("1. Iniciar sesion");
            System.out.println("2. Registrarte");
            int opcionUsuarios = io.leerInt();

            System.out.println("Nombre de usuario: ");
            String userName = io.leerString();
            System.out.println("Contraseña: ");
            String userPass = io.leerString();

            if (opcionUsuarios == 1) {
                funciones.iniciarSesion(connection, url, user, password, userName, userPass);
            } else if (opcionUsuarios == 2) {
                funciones.añadirUsuarios(connection, url, user, password, userName, userPass);
            } else {
                System.out.println("Opcion no valida");
            }

            while (true) {
                if (connection != null) {
                    System.out.println("Que quieres hacer: ");
                    System.out.println("1. Añadir libros");
                    System.out.println("2. Borrar libros");
                    System.out.println("3. Ver tabla");
                    System.out.println("4. Buscar libros");
                    System.out.println("5. Reservar un libro");
                    System.out.println("6. Ver reservas");
                    System.out.println("7. Salir");
                    int opcion = io.leerInt();

                    if (opcion == 1) {
                        funciones.añadirLibros(connection, url, user, password);
                    } else if (opcion == 2) {
                        funciones.eliminarLibros(connection, url, user, password);
                    } else if (opcion == 3) {
                        funciones.verLibros(connection, url, user, password);
                    } else if (opcion == 4) {
                        funciones.buscarLibro(connection, url, user, password);
                    } else if (opcion == 5) {
                        funciones.reservarLibro(connection, url, user, password, userName);
                    } else if (opcion == 6) {
                        funciones.verReservas(connection, url, user, password, userName);
                    } else if (opcion == 7) {
                        System.exit(0);
                    } else {
                        System.out.println("Opcion no valida");

                    }
                    connection.close();
                } else {
                    System.out.println("Conexión fallida");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el controlador JDBC");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error en la conexión a la BBDD");
            e.printStackTrace();
        }
    }
}
