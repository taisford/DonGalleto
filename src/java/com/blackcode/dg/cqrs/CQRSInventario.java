package com.blackcode.dg.cqrs;

import com.blackcode.dg.dao.DAOInventario;
import com.blackcode.dg.model.Galleta;
import com.blackcode.dg.model.Paquete;

/**
 * @author marko
 */
public class CQRSInventario {

    private DAOInventario daoinventario;

    public CQRSInventario() {
        daoinventario = new DAOInventario();
    }

    private void validarSave(Galleta g) throws Exception {
        if (g.getSabor() == null || g.getSabor().isEmpty()) {
            throw new Exception("El campo sabor esta vacio");
        }
    }

    public void validarDelete(Galleta g) throws Exception {
        if (g.getIdGalleta() <= 0) {
            throw new Exception("Debes seleccionar una galleta válida para eliminar");
        }
    }

    public void validarSavePaquete(Paquete p) throws Exception {
        if (p.getGalleta().getIdGalleta() <= 0) {
            throw new Exception("Debes seleccionar una galleta para armar un paquete");
        }
    }

    public int saveGalleta(Galleta g) throws Exception {
        validarSave(g);
        return daoinventario.save(g);
    }

    public void delete(Galleta g, Integer cantidad) throws Exception {
        validarDelete(g);
        daoinventario.delete(g, cantidad);
    }

    public int savePaquete(Paquete p) throws Exception {
        validarSavePaquete(p);
        return daoinventario.savePaquete(p);
    }
    
    

}
