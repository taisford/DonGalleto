package com.blackcode.dg.dao;

import com.blackcode.dg.db.ConexionMySQL;
import com.blackcode.dg.model.Galleta;
import com.blackcode.dg.model.Paquete;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author marko
 */
public class DAOInventario {

    public int save(Galleta g) throws Exception {
        System.out.println("Entre al metodo Save");
        String sql = """
                     call saveGalleta(?, ?)
                     """;
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cs = conn.prepareCall(sql);

        int idGalletaGenerado = 0;

        try {
            cs.setString(1, g.getSabor());
            cs.registerOutParameter(2, Types.INTEGER);
            cs.executeUpdate();

            idGalletaGenerado = cs.getInt(2);
            g.setIdGalleta(idGalletaGenerado);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            cs.close();
        }
        return idGalletaGenerado;
    }

    //  Este metodo es un metodo para mermar galletas, en el que 
    //  si se ingresa una cantidad, se merma solo la cantidad, pero
    //  si no se ingresa nada se merma todo, esto funciona de acuerdo
    //  al Checkbox del Front que deshabilitara el input para borrar 
    //  todo
    public void delete(Galleta g, Integer cantidad) throws Exception {
        System.out.println("Llegue al metodo Delete");
        String sql = """
                 call eliminarGalletas(?, ?)
                 """;
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cs = conn.prepareCall(sql);
        try {
            cs.setInt(1, g.getIdGalleta());
            // Primero compruebo si la cantidad es nula o 0
            if (cantidad == null || cantidad <= 0) {
                //  y en caso de que si va a pasar como valor un 0 
                //  para que el SP elimine todo
                cs.setInt(2, 0);
            } else {
                //  si no, se va a pasar la cantidad ingresada
                cs.setInt(2, cantidad);
            }
            cs.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cs.close();
            conn.close();
        }
    }

    public List<Galleta> getAll() throws Exception {
        String sql = "SELECT * FROM galleta WHERE estatus = 1";

        ConexionMySQL conMySQL = new ConexionMySQL();
        Connection conn = conMySQL.open();
        PreparedStatement ps = conn.prepareCall(sql);
        ResultSet rs = ps.executeQuery();
        ArrayList<Galleta> galletas = new ArrayList<>();

        while (rs.next()) {
            Galleta ga = fill(rs);
            galletas.add(ga);
        }
        rs.close();
        ps.close();
        conn.close();

        return galletas;
    }

    private Galleta fill(ResultSet rs) throws Exception {
        Galleta g = new Galleta();

        g.setIdGalleta(rs.getInt("idGalleta"));
        g.setTipo(rs.getString("tipo"));
        g.setSabor(rs.getString("sabor"));
        g.setCantidad(rs.getInt("cantidad"));
        g.setFechaCad(rs.getString("fechaCad"));
        g.setEstatus(rs.getInt("estatus"));

        return g;
    }

    ////////////////////////////////////////////////////////////////////////////
    //                              PAQUETES                                  //
    ////////////////////////////////////////////////////////////////////////////
    public int savePaquete(Paquete p, Integer cantidad) throws Exception {
        System.out.println("Entre al metodo SavePaquete");
        String sql = """
                     call crearPaquete(?, ?, ?)
                     """;
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cs = conn.prepareCall(sql);

        int idPaqueteGenerado = 0;

        try {
            cs.setInt(1, p.getGalleta().getIdGalleta());
            cs.setInt(2, cantidad);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.executeUpdate();

            idPaqueteGenerado = cs.getInt(3);
            p.setIdPaquete(idPaqueteGenerado);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            cs.close();
        }
        return idPaqueteGenerado;
    }

    public List<Paquete> getAllPaquetes() throws Exception {
        String sql = "SELECT * FROM ViewPaquetes Where estatus = 1";

        ConexionMySQL conMySQL = new ConexionMySQL();
        Connection conn = conMySQL.open();
        PreparedStatement ps = conn.prepareCall(sql);
        ResultSet rs = ps.executeQuery();
        ArrayList<Paquete> paquetes = new ArrayList<>();

        while (rs.next()) {
            Paquete pa = fillPaquete(rs);
            paquetes.add(pa);
        }
        rs.close();
        ps.close();
        conn.close();

        return paquetes;
    }

    private Paquete fillPaquete(ResultSet rs) throws Exception {
        Paquete p = new Paquete();
        Galleta g = new Galleta();

        // Llenar datos de la galleta
        g.setIdGalleta(rs.getInt("idGalleta"));
        g.setSabor(rs.getString("sabor"));
        g.setFechaCad(rs.getString("fechaCad"));
        g.setTipo(rs.getString("tipo"));
        g.setCantidad(rs.getInt("cantidad"));

        // Llenar datos del paquete
        p.setIdPaquete(rs.getInt("idPaquete"));
        p.setTipo(rs.getString("tipo"));  // Esto es el tipo de paquete (ahora lo llamamos tipoPaquete)
        p.setCantidad(rs.getInt("cantidad"));  // Es la cantidad de paquetes
        p.setEstatus(rs.getInt("estatus"));
        p.setGalleta(g);

        return p;
    }
}
