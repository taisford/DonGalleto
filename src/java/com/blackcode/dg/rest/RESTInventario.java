package com.blackcode.dg.rest;

import com.blackcode.dg.core.ControllerInventario;
import com.blackcode.dg.model.Galleta;
import com.blackcode.dg.model.Paquete;
import com.google.gson.Gson;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import jakarta.ws.rs.PATCH;

/**
 * @author marko
 */
@Path("inv")
public class RESTInventario {

    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosGalleta") @DefaultValue("") String datosGalleta) throws Exception {
        Galleta ga = null;
        ControllerInventario ci = new ControllerInventario();
        String out = null;
        Gson gson = new Gson();
        System.out.println(datosGalleta);

        try {
            ga = gson.fromJson(datosGalleta, Galleta.class);
            if (ga.getIdGalleta() < 1) {
                ci.insert(ga);
            }
            out = """
                {"result":"Lote guardado correctamente."}
                """;
        } catch (Exception e) {
            e.printStackTrace();
            out = """
                    {"exception":"Ocurrio un error en el servidor. %S"}
                  """;
            out = String.format(out, e.toString().replaceAll("\"", ""));
            System.out.println(out);
        }
        return Response.ok(out).build();
    }

    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        ControllerInventario ci = new ControllerInventario();

        List<Galleta> galletas = null;
        String out = null;
        Gson gson = new Gson();

        try {
            galletas = ci.getAll();
            out = gson.toJson(galletas);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":" + e.toString().replaceAll("\"", "") + "\"}";
        }
        return Response.ok(out).build();
    }

    @Path("del")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("eliminacion") @DefaultValue("") String eliminacion) throws Exception {

        Galleta g = null;
        ControllerInventario ci = new ControllerInventario();
        String out = null;
        Gson gson = new Gson();

        try {
            g = gson.fromJson(eliminacion, Galleta.class);
            if (g.getIdGalleta() > 0) {
                // Obtener la cantidad desde el objeto Galleta (puede ser null)
                Integer cantidad = g.getCantidad();
                ci.delete(g, cantidad);
            }
            out = """
                {"response":"Datos del Producto eliminados correctamente"}
              """;
        } catch (Exception e) {
            out = """
                {"response":"Ocurrió un error en el servidor. %s"}
              """;
            out = String.format(out, e.toString().replaceAll("\"", ""));
        }
        return Response.ok(out).build();
    }

    @Path("savePkg")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response savePaquete(@FormParam("datosPaquete") @DefaultValue("") String datosPaquete) throws Exception {
        Paquete pa = null;
        ControllerInventario ci = new ControllerInventario();
        String out = null;
        Gson gson = new Gson();
        System.out.println(datosPaquete);

        try {
            pa = gson.fromJson(datosPaquete, Paquete.class);
            if (pa.getGalleta().getIdGalleta() > 0) {
                Integer cantidad = pa.getCantidad();
                ci.insertPaquete(pa, cantidad);
            }
            out = """
                {"result":"Paquete guardado correctamente."}
                """;
        } catch (Exception e) {
            e.printStackTrace();
            out = """
                    {"exception":"Ocurrio un error en el servidor. %S"}
                  """;
            out = String.format(out, e.toString().replaceAll("\"", ""));
            System.out.println(out);
        }
        return Response.ok(out).build();
    }

    @Path("gaPkg")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPaquete() {
        ControllerInventario ci = new ControllerInventario();

        List<Paquete> paquetes = null;
        String out = null;
        Gson gson = new Gson();

        try {
            paquetes = ci.getAllPaquete();
            out = gson.toJson(paquetes);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":" + e.toString().replaceAll("\"", "") + "\"}";
        }
        return Response.ok(out).build();
    }
}
