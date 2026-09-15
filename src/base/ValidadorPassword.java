/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package base;

import Excepciones.PasswordInvalidaException;

/**
 *
 * @author gabri
 */
public class ValidadorPassword {
 
    private static final int LONGITUD_MINIMA = 6;
 
    private static final String SIMBOLOS_PERMITIDOS = "!@#$%^&*()\\-_=+.,;:";
 
    private static final java.util.regex.Pattern TIENE_LETRA =
            java.util.regex.Pattern.compile("[A-Za-zÁÉÍÓÚáéíóúÑñ]");
    private static final java.util.regex.Pattern TIENE_NUMERO =
            java.util.regex.Pattern.compile("[0-9]");
    private static final java.util.regex.Pattern TIENE_SIMBOLO =
            java.util.regex.Pattern.compile("[" + SIMBOLOS_PERMITIDOS + "]");
    private static final java.util.regex.Pattern SOLO_CARACTERES_PERMITIDOS =
            java.util.regex.Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9" + SIMBOLOS_PERMITIDOS + "]+$");
 
    private ValidadorPassword() {
    }
 
    public static void validar(String password) throws PasswordInvalidaException {
        if (password == null || password.trim().isEmpty()) {
            throw new PasswordInvalidaException("La contraseña no puede estar vacía.");
        }
 
        String p = password.trim();
 
        if (p.length() < LONGITUD_MINIMA) {
            throw new PasswordInvalidaException(
                    "La contraseña debe tener al menos " + LONGITUD_MINIMA + " caracteres.");
        }
        if (!SOLO_CARACTERES_PERMITIDOS.matcher(p).matches()) {
            throw new PasswordInvalidaException(
                    "La contraseña solo puede contener letras, números y los símbolos: "
                            + SIMBOLOS_PERMITIDOS.replace("\\-", "-"));
        }
        if (!TIENE_LETRA.matcher(p).find()) {
            throw new PasswordInvalidaException("La contraseña debe incluir al menos una letra.");
        }
        if (!TIENE_NUMERO.matcher(p).find()) {
            throw new PasswordInvalidaException("La contraseña debe incluir al menos un número.");
        }
        if (!TIENE_SIMBOLO.matcher(p).find()) {
            throw new PasswordInvalidaException(
                    "La contraseña debe incluir al menos un símbolo especial ("
                            + SIMBOLOS_PERMITIDOS.replace("\\-", "-") + ").");
        }
    }
}
