
package ConfigInsta;

import java.nio.file.Paths;

/**
 *
 * @author vasqu
 */
public class Rutas {
    public static final String RUTA_RAIZ=Paths.get(System.getProperty("user.dir"),"INSTA_RAIZ").toString();
    public static final String RUTA_STICKERS_GLOBALES=Paths.get(RUTA_RAIZ,"stickers_globales").toString();
    public static final String ARCHIVO_USERS=Paths.get(RUTA_RAIZ,"users.ins").toString();
    private Rutas(){
    }
    
    public static String rutaCarpetaUsuario(String username){
        return Paths.get(RUTA_RAIZ,username).toString();
        
    }
    
    public static String rutaInbox(String username){
        return Paths.get(rutaCarpetaUsuario(username),"inbox.ins").toString();
    }
    
    public static String rutaFollowing(String username){
        return Paths.get(rutaCarpetaUsuario(username),"following.ins").toString();
    }
    
    public static String rutaFollowers(String username){
       return Paths.get(rutaCarpetaUsuario(username),"followers.ins").toString();
    }
    
    public static String rutaInsta(String username){
        return Paths.get(rutaCarpetaUsuario(username),"insta.ins").toString();
    }
    
    public static String rutaStickersIns(String username){
        return Paths.get(rutaCarpetaUsuario(username),"stickers.ins").toString();
    }
    
    public static String rutaImagenes(String username){
        return Paths.get(rutaCarpetaUsuario(username),"imagenes").toString();
    }
    
    public static String rutaFolderPersonales(String username){
        return Paths.get(rutaCarpetaUsuario(username),"folders_personales").toString();
    }
    
    public static String rutaStickersPersonales(String username){
        return Paths.get(rutaCarpetaUsuario(username),"stickers_personales").toString();
    }
}
