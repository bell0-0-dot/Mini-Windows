/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import java.io.Serializable;

/**
 *
 * @author vasqu
 */
public class PeticionRed implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String comando;
    private Object[] parametros;

    public PeticionRed(String comando, Object... parametros) {
        this.comando = comando;
        this.parametros = parametros;
    }

    public String getComando() { return comando; }
    public Object[] getParametros() { return parametros; }
}
