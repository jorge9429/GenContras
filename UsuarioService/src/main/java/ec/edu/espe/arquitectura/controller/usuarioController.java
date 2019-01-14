/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.arquitectura.controller;

import ec.edu.espe.arquitectura.msg.UsuarioRQ;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import redis.clients.jedis.Jedis;

/**
 *
 * @author jorge
 */
@Named(value = "usuarioController")
@ViewScoped
public class usuarioController implements Serializable {

    private List<UsuarioRQ> items = null;
    private UsuarioRQ usrq = new UsuarioRQ();
    private final String urlUsuario = "http://kyc.strangled.net:8080/KYC-mongo-rest-web/api/cliente/cedula/";
    private String usuario1 = "";
    private String usuario2 = "";
    private String usuario3 = "";
    private String contras1 = "";
    private String contras2 = "";
    private String cedula = "";

    public usuarioController() {
    }

    public void generarContraseña() {
        try {
            URL url = new URL(urlUsuario + cedula);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            BufferedReader inp = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            StringBuilder crunchifyBuilder = new StringBuilder();
            while ((line = inp.readLine()) != null) {
                crunchifyBuilder.append(line);
                if (conn.getResponseCode() == 200) {
                    int nomb = crunchifyBuilder.indexOf("nombres");
                    int apel = crunchifyBuilder.indexOf("apellidos");
                    int cedu = crunchifyBuilder.indexOf("identificacion");
                    int codi = crunchifyBuilder.indexOf("codigo");
                    usuario1 = crunchifyBuilder.substring(nomb + 10, nomb + 12);
                    usuario3 = crunchifyBuilder.substring(apel + 12, codi - 3);
                    usuario2 = eliminaContenido(usuario3, " ");
                    usuario1 = usuario1.concat(usuario2);
                    usuario1 = usuario1.replace(" ", "");
                    usuario1 = usuario1.toLowerCase();
                    contras1 = crunchifyBuilder.substring(apel + 12, codi - 3);
                    contras1 = eliminaContenido(contras1, " ");
                    contras2 = crunchifyBuilder.substring(cedu + 17, cedu + 21);
                    contras1 = contras1.concat(contras2);
                    contras1 = contras1.replace(" ", "");
                    contras1 = contras1.toLowerCase();
                    System.out.println("USUARIO=:>" + usuario1);
                    System.out.println("CONTRASEÑA=:>" + contras1);
                    //IngresarUsuario(usuario1, contras1, cedula);
                } else {
                    System.out.println("Fallamos perro :'(");
                }
            }
        } catch (Exception e) {
            System.out.println("Ex:" + e);
        }
    }

    public void IngresarUsuario(String usser, String contra, String ced) {
        System.out.println("DATOS LLEGA=:>" + usser + "/" + contra + "/" + ced);
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println("CONEXION ESTABLECIDA");
        usrq.setUsuario(usser);
        usrq.setContrasenia(contra);
        usrq.setCedula(ced);
        jedis.set(ced,usser+contra);
        System.out.println("Datos almacenados de forma correcta");
    }
    
    private static String eliminaContenido(String text, String sep){    
     if (text != null && text.contains(sep)) {     
         int position = text.indexOf(sep);
       text = text.substring(0, position +1);              
     }
     return text;
    }
    
    public void Conexion() {

    }

    public List<UsuarioRQ> getItems() {
        return items;
    }

    public void setItems(List<UsuarioRQ> items) {
        this.items = items;
    }

    public UsuarioRQ getUsrq() {
        return usrq;
    }

    public void setUsrq(UsuarioRQ usrq) {
        this.usrq = usrq;
    }

    public String getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(String usuario1) {
        this.usuario1 = usuario1;
    }

    public String getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(String usuario2) {
        this.usuario2 = usuario2;
    }

    public String getUsuario3() {
        return usuario3;
    }

    public void setUsuario3(String usuario3) {
        this.usuario3 = usuario3;
    }

    public String getContras1() {
        return contras1;
    }

    public void setContras1(String contras1) {
        this.contras1 = contras1;
    }

    public String getContras2() {
        return contras2;
    }

    public void setContras2(String contras2) {
        this.contras2 = contras2;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

}
