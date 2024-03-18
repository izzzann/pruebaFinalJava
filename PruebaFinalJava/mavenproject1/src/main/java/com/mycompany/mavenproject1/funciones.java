package com.mycompany.mavenproject1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class funciones {
    public static void añadirUsuarios(Connection connection, String url, String user, String password, String userName,
            String userPass) {
        try {

            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement(
                    "insert into usuarios (nombre, contraseña, correo) values (?,?,? || '@edu.uah.es')");
            ps.setString(1, userName);
            ps.setString(2, userPass);
            ps.setString(3, userName);

            ps.execute();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("El nombre de usuario ya existe en la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error en la conexión a la BBDD");
        }
    }

    public static void añadirLibros(Connection connection, String url, String user, String password) {

        try {
            System.out.println("Dime el nombre del libro");
            String bookName = io.leerString();
            System.out.println("Dime el autor del libro");
            String bookAuthor = io.leerString();
            System.out.println("Dime la editorial del libro");
            String bookEditorial = io.leerString();
            System.out.println("Dime el isbn del libro");
            int bookISBN = io.leerInt();
            System.out.println("Dime el numero de ejemplares del libro");
            int bookNumber = io.leerInt();

            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement(
                    "insert into biblioteca (nombre, autor, editorial, isbn, ejemplares) values (?,?,?,?,?)");
            ps.setString(1, bookName);
            ps.setString(2, bookAuthor);
            ps.setString(3, bookEditorial);
            ps.setInt(4, bookISBN);
            ps.setInt(5, bookNumber);

            ps.execute();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println(
                    "Has violado la restricción de clave primaria, el ISBN o el nombre del libro ya existe en la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error en la conexión a la BBDD");
        }
    }

    public static void verLibros(Connection connection, String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement("select * from biblioteca");
            String miquery = "select * from biblioteca";
            ResultSet rst = ps.executeQuery(miquery);

            System.out.println("");
            System.out.println("Libros: ");

            while (rst.next()) {
                System.out.println("Libro: " + rst.getString("NOMBRE") + " | " + "Autor: " + rst.getString("autor")
                        + " | " + "Editorial: "
                        + rst.getString("editorial") + " | " + "ISBN: " + rst.getInt("isbn") + " | " + "Ejemplares: "
                        + rst.getInt("ejemplares"));
            }
        } catch (SQLException e) {
            System.out.println("Error en la conexión a la BBDD");
        }
    }

    public static void eliminarLibros(Connection connection, String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Dime el ISBN del libro que quieras borrar");
            String borrarLibros = io.leerString();
            PreparedStatement borrarUser = connection.prepareStatement("delete from biblioteca where isbn = ?");
            borrarUser.setString(1, borrarLibros);

            borrarUser.execute();
        } catch (SQLException e) {
            System.out.println("Error en la conexión a la BBDD");
        }
    }

    public static void buscarLibro(Connection connection, String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            String buscarLibroPorAutor = null;
            String buscarLibroPorNombre = null;
            Integer buscarLibroPorISBN = 0;
            System.out.println("¿Cómo quieres buscar el libro?");
            System.out.println("1. Por autor");
            System.out.println("2. Por nombre");
            System.out.println("3. Por ISBN");
            int comoBuscar = io.leerInt();
            if (comoBuscar == 1) {
                System.out.println("Dime el autor del libro que quieras buscar");
                buscarLibroPorAutor = io.leerString();
            } else if (comoBuscar == 2) {
                System.out.println("Dime el nombre del libro que quieras buscar");
                buscarLibroPorNombre = io.leerString();
            } else if (comoBuscar == 3) {
                System.out.println("Dime el ISBN del libro que quieras buscar");
                buscarLibroPorISBN = buscarLibroPorISBN + io.leerInt();
            }

            PreparedStatement buscarUser = connection.prepareStatement(
                    "select * from biblioteca where lower(autor) like lower(?) or lower(nombre) like lower(?) or isbn = ?");
            buscarUser.setString(1, "%" + buscarLibroPorAutor + "%");
            buscarUser.setString(2, "%" + buscarLibroPorNombre + "%");
            buscarUser.setInt(3, buscarLibroPorISBN);

            ResultSet rst = buscarUser.executeQuery();

            while (rst.next()) {
                System.out.println("Libro: " + rst.getString("NOMBRE") + " | " + "Autor: " + rst.getString("autor")
                        + " | " + "Editorial: "
                        + rst.getString("editorial") + " | " + "ISBN: " + rst.getInt("isbn") + " | " + "Ejemplares: "
                        + rst.getInt("ejemplares"));
            }

        } catch (SQLException e) {
            System.out.println("Error en la conexión a la BBDD");
        }
    }

    public static void reservarLibro(Connection connection, String url, String user, String password, String userName) {
        try {
            connection = DriverManager.getConnection(url, user, password);

            System.out.println("¿Cómo quieres reservar el libro?");
            System.out.println("1. Por nombre y autor");
            System.out.println("2. Por ISBN");
            int comoReservar = io.leerInt();

            if (comoReservar == 1) {
                System.out.println("¿Que libro quieres reservar? (Introduce nombre y autor.)");
                System.out.println("Nombre del libro: ");
                String nombreLibro = io.leerString();
                System.out.println("Autor del libro: ");
                String nombreAutor = io.leerString();
                System.out.println("¿Cuantas unidades quieres reservar?");
                Integer unidadesReserva = io.leerInt();

                PreparedStatement buscarLibros = connection.prepareStatement(
                        "select * from biblioteca where ejemplares >= ? and lower(autor) = lower(?) and lower(nombre) like lower(?)");
                buscarLibros.setInt(1, unidadesReserva);
                buscarLibros.setString(2, nombreAutor);
                buscarLibros.setString(3, nombreLibro);

                ResultSet rst = buscarLibros.executeQuery();

                if (!rst.next()) {
                    System.out.println("No se ha encontrado el libro o no hay suficientes ejemplares.");
                } else {

                    PreparedStatement updateLibros = connection.prepareStatement(
                            "update biblioteca set ejemplares = ejemplares - ? where lower(autor) like lower(?) and lower(nombre) like lower(?)");
                    updateLibros.setInt(1, unidadesReserva);
                    updateLibros.setString(2, nombreAutor);
                    updateLibros.setString(3, nombreLibro);

                    updateLibros.executeQuery();

                    PreparedStatement meterLibrosReservados = connection
                            .prepareStatement("insert into usuarioslibros (nombre, libro, ejemplares) values (?,?,?)");
                    meterLibrosReservados.setString(1, userName);
                    meterLibrosReservados.setString(2, nombreLibro);
                    meterLibrosReservados.setInt(3, unidadesReserva);

                    meterLibrosReservados.execute();
                }
            } else if (comoReservar == 2) {
                System.out.println("¿Que libro quieres reservar? (Introduce ISBN y nombre del libro.)");
                System.out.println("ISBN del libro: ");
                Integer ISBNLibro = io.leerInt();
                System.out.println("Nombre del libro: ");
                String nombreLibro = io.leerString();
                System.out.println("¿Cuantas unidades quieres reservar?");
                Integer unidadesReserva = io.leerInt();

                PreparedStatement buscarLibros = connection.prepareStatement(
                        "select * from biblioteca where ejemplares >= ? and isbn = ? and lower(nombre) like lower(?)");
                buscarLibros.setInt(1, unidadesReserva);
                buscarLibros.setInt(2, ISBNLibro);
                buscarLibros.setString(3, nombreLibro);

                ResultSet rst = buscarLibros.executeQuery();

                if (!rst.next()) {
                    System.out.println("No se ha encontrado el libro o no hay suficientes ejemplares.");
                } else {
                    PreparedStatement updateLibros = connection.prepareStatement(
                            "update biblioteca set ejemplares = ejemplares - ? where isbn = ? and lower(nombre) like lower(?)");
                    updateLibros.setInt(1, unidadesReserva);
                    updateLibros.setInt(2, ISBNLibro);
                    updateLibros.setString(3, nombreLibro);

                    updateLibros.executeQuery();

                    PreparedStatement meterLibrosReservados = connection
                            .prepareStatement("insert into usuarioslibros (nombre, libro, ejemplares) values (?,?,?)");
                    meterLibrosReservados.setString(1, userName);
                    meterLibrosReservados.setString(2, nombreLibro);
                    meterLibrosReservados.setInt(3, unidadesReserva);

                    meterLibrosReservados.execute();
                }

            } else {
                System.out.println("Opcion no valida");
            }

        } catch (SQLException e) {
            System.out.println("Error en la conexión a la BBDD");
        }
    }

    public static void iniciarSesion(Connection connection, String url, String user, String password, String userName,
            String userPass) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement(
                    "select * from usuarios where lower(nombre) like lower(?) and lower(contraseña) like lower(?)");
            ps.setString(1, userName);
            ps.setString(2, userPass);
            ResultSet rst = ps.executeQuery();
            if (rst.next()) {
                System.out.println("Has iniciado sesion correctamente.");
            } else {
                System.out.println("Usuario o contraseña incorrectos.");
            }
        } catch (SQLException e) {
            System.out.println("Error en la conexión a la BBDD");
        }
    }

    public static void verReservas(Connection connection, String url, String user, String password, String userName) {
        try {
            connection = DriverManager.getConnection(url, user, password);

            System.out.println("¿Quieres ver las reservas de todos los usuarios o solo las tuyas?");
            System.out.println("1. Solo las mias");
            System.out.println("2. Todas");
            int opcionReservas = io.leerInt();

            if (opcionReservas == 1) {
                PreparedStatement ps = connection
                        .prepareStatement("select * from usuarioslibros where lower(nombre) like lower(?)");
                ps.setString(1, userName);

                ResultSet rst = ps.executeQuery();

                while (rst.next()) {
                    System.out.println("Usuario: " + rst.getString(1) + " | " + "Libro: " + rst.getString(2) + " | "
                            + "Ejemplares: " + rst.getInt(3));
                }
            } else if (opcionReservas == 2) {
                PreparedStatement ps = connection.prepareStatement("select * from usuarioslibros");
                ResultSet rst = ps.executeQuery();
                while (rst.next()) {
                    System.out.println("Usuario: " + rst.getString(1) + " | " + "Libro: " + rst.getString(2) + " | "
                            + "Ejemplares: " + rst.getInt(3));
                }
            } else {
                System.out.println("Opcion no valida");
            }

        } catch (SQLException e) {
            System.out.println("Error en la conexión a la BBDD");
        }
        ;
    }
}

// public static void registrarseOiniciarSesion(Connection connection, String
// url, String user, String password) {
// try {
// System.out.println("Quieres registrarte o iniciar sesion");
// System.out.println("1. Registrarte");
// System.out.println("2. Iniciar sesion");
// int opcionUsuarios = io.leerInt();

// if (opcionUsuarios == 1) {

// System.out.println("Introduce tu nombre de usuario: ");
// String userName = io.leerString();
// System.out.println("Introduce tu contraseña: ");
// String userPass = io.leerString();
// System.out.println("Correo genereado como: " + userName + "@edu.uah.es");
// connection = DriverManager.getConnection(url, user, password);
// PreparedStatement ps = connection.prepareStatement(
// "insert into usuarios (nombre, contraseña, correo) values (?,?,? ||
// '@edu.uah.es')");
// ps.setString(1, userName);
// ps.setString(2, userPass);
// ps.setString(3, userName);

// ps.execute();

// } else if (opcionUsuarios == 2) {
// System.out.println("Introduce tu nombre de usuario: ");
// String userName = io.leerString();
// System.out.println("Introduce tu contraseña: ");
// String userPass = io.leerString();
// connection = DriverManager.getConnection(url, user, password);
// PreparedStatement ps = connection.prepareStatement(
// "select * from usuarios where lower(nombre) like lower(?) and
// lower(contraseña) like lower(?)");
// ps.setString(1, userName);
// ps.setString(2, userPass);
// ResultSet rst = ps.executeQuery();
// if (rst.next()) {
// System.out.println("Has iniciado sesion correctamente.");
// } else {
// System.out.println("Usuario o contraseña incorrectos.");
// }
// } else {
// System.out.println("Opcion no valida");
// }
// } catch (SQLIntegrityConstraintViolationException e) {
// System.out.println("El nombre de usuario ya existe en la base de datos.");
// } catch (SQLException e) {
// System.out.println("Error en la conexión a la BBDD");

// }
// }
