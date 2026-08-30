
package base;

import java.io.Serializable;
import java.time.LocalDate;



public  class Usuario implements Serializable{
    protected String User;
    protected String password;
    protected boolean activo;
    protected LocalDate fechaRegistro;

    public Usuario(String User, String password) {
        this.User = User;
        this.password = password.trim();
        this.activo=true;
        this.fechaRegistro=LocalDate.now();
        
    }
    public boolean verificarPassword(String intento){
        if(intento==null){
            return false;
        }
        String ingresado=intento.trim();
        
        return ingresado.equals(password);
    }

    public void activarUser(){
        this.activo=true;
        
    }
    public void desactivarUser(){
        this.activo=false;
        
    }
    public String getUser() {
        return User;
    }

    public void setUser(String User) {
        this.User = User;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
