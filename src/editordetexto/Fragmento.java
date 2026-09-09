package editordetexto;

/**
 *
 * @author gabri
 */
public class Fragmento {
    private String texto;
    private FormatoTexto formato;

    public Fragmento() {
        this.texto = "";
        this.formato = new FormatoTexto();
    }

    public Fragmento(String texto, FormatoTexto formato) {
        this.texto = texto;
        this.formato = formato;
    }

    public String getTexto(){
        return texto;
    }
    
    public void setTexto(String texto){
        this.texto = texto;
    }

    public FormatoTexto getFormato(){
        return formato; 
    }
    
    public void setFormato(FormatoTexto formato) { 
        this.formato = formato;
    }
}
