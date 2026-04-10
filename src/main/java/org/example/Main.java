package org.example;

import java.sql.*;


public class Main {

    public static void main(String[] args) {
        //Creamos la conexion para usar en todos los metodos
        try (Connection conn = DriverManager.getConnection(
                DBConfig.getUrl(),
                DBConfig.getUser(),
                DBConfig.getPassword())) {
            //Llamamos a los tres metodos
            clasificacionPuntos(conn);
            clasificacionEquipos(conn);
            rankingEtapas(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
//Creamos el primer metodo clasificacion de puntos
    public static void clasificacionPuntos(Connection conn) {
        System.out.println("--------------------------------------------------------------");
        System.out.println("Primera consulta, puntos de los 10 ciclistas con mas puntos");
        String sql = "SELECT CICLISTA.NOMBRE AS ciclista_NOMBRE,EQUIPO.NOMBRE as equipo_nombre,SUM(PARTICIPACION.PUNTOS) AS SUMA \n" +
                "FROM CICLISTA JOIN EQUIPO USING (ID_EQUIPO)  JOIN PARTICIPACION USING (ID_CICLISTA) \n" +
                "GROUP BY CICLISTA.NOMBRE,EQUIPO.NOMBRE \n" +
                "ORDER BY SUMA DESC\n" +
                "FETCH FIRST 10 ROWS ONLY";


        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String ciclistaNombre = rs.getString("ciclista_NOMBRE");
                String equipoNombre = rs.getString("equipo_nombre");
                int puntuacion = rs.getInt("SUMA");
                System.out.println(ciclistaNombre + " - " + equipoNombre + " - " + puntuacion);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("--------------------------------------------------------------");


    }
//Creamos el segundo metodo que nos muestra los equipos y los puntos de sus ciclistas
    public static void clasificacionEquipos(Connection conn) {
        System.out.println("Segunda consulta, puntos de los equipos");
        String sql = "SELECT EQUIPO.NOMBRE as equipo_nombre,EQUIPO.PAIS as equipo_pais,SUM(PARTICIPACION.PUNTOS) AS SUMA \n" +
                "FROM EQUIPO JOIN CICLISTA USING (ID_EQUIPO) JOIN PARTICIPACION USING (ID_CICLISTA) \n" +
                "GROUP BY EQUIPO.NOMBRE,EQUIPO.PAIS \n" +
                "ORDER BY SUMA DESC";


        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String equipoNombre = rs.getString("equipo_nombre");
                String pais = rs.getString("equipo_pais");
                int puntuacion = rs.getInt("SUMA");
                System.out.println( equipoNombre + " - "+ pais+ " - " + puntuacion);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("--------------------------------------------------------------");
    }
    //El tercer metodo que nos muestra el ranking de etapas donde nos muestra las etapas con mayor distancia
    public static void rankingEtapas(Connection conn) {
        System.out.println("Tercera consulta, ranking de etapas con mayor distancia");
        String sql = " SELECT NUMERO,ORIGEN,DESTINO,DISTANCIA_KM,FECHA FROM ETAPA " +
                "WHERE DISTANCIA_KM > (SELECT AVG(DISTANCIA_KM) FROM ETAPA) " +
                "ORDER BY DISTANCIA_KM DESC "+
                " FETCH FIRST 3 ROWS ONLY";



        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int numero = rs.getInt("numero");
                String origen = rs.getString("origen");
                String destino = rs.getString("destino");
                double distancia = rs.getDouble("DISTANCIA_KM");
                Date fecha = rs.getDate("FECHA");
                System.out.println( numero + " - "+ origen+ " - " + destino+ " - " + distancia + "km - " + fecha);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}