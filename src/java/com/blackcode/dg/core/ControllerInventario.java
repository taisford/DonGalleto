package com.blackcode.dg.core;

import com.blackcode.dg.cqrs.CQRSInventario;
import com.blackcode.dg.dao.DAOInventario;
import com.blackcode.dg.model.Galleta;
import com.blackcode.dg.model.Paquete;
import java.util.List;

/**
 * @author marko
 */
public class ControllerInventario {

    private CQRSInventario cqrsinventario;
    private DAOInventario daoinventario;

    public ControllerInventario() {
        cqrsinventario = new CQRSInventario();
        daoinventario = new DAOInventario();
    }

    public int insert(Galleta g) throws Exception {
        return cqrsinventario.saveGalleta(g);
    }

    public void delete(Galleta g, Integer cantidad) throws Exception {
        cqrsinventario.delete(g, cantidad);
    }

    public List<Galleta> getAll() throws Exception {
        return daoinventario.getAll();
    }

    public int insertPaquete(Paquete p) throws Exception {
        return cqrsinventario.savePaquete(p);
    }

    public List<Paquete> getAllPaquete() throws Exception {
        return daoinventario.getAllPaquetes();
    }

}
