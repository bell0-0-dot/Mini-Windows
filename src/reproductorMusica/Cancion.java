package reproductorMusica;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.images.Artwork;

import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;

/**
 *
 * @author gabri
 */

public class Cancion {
    private String titulo;
    private String artista;
    private int duracionSegundos;
    private File ruta;
    private BufferedImage portada;

    public Cancion(File ruta) {
        this.ruta = ruta;

        try {
            AudioFile audioFile = AudioFileIO.read(ruta);
            
            if (audioFile.getAudioHeader() != null) {
                this.duracionSegundos = audioFile.getAudioHeader().getTrackLength();
            }

            Tag tag = audioFile.getTag();
            if (tag != null) {
                String t = tag.getFirst(FieldKey.TITLE);
                String a = tag.getFirst(FieldKey.ARTIST);

                this.titulo = (t != null && !t.isBlank()) ? t : ruta.getName();
                this.artista = (a != null && !a.isBlank()) ? a : "Artista desconocido";

                Artwork portadaArt = tag.getFirstArtwork();
                if (portadaArt != null) {
                    byte[] imageData = portadaArt.getBinaryData();
                    ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
                    this.portada = ImageIO.read(bais);
                }
            } else {
                this.titulo = ruta.getName();
                this.artista = "Artista desconocido";
            }

            if (this.portada == null) {
                var resource = getClass().getResourceAsStream("/reproductorMusica/res/DefaultSongCover.png");
                if (resource != null) {
                    this.portada = ImageIO.read(resource);
                }
            }
        } catch (Exception e) {
            this.titulo = ruta.getName();
            this.artista = "Error al cargar metadatos";
        }
    }

    public String getTitulo() {
        return titulo; 
    }
    public String getArtista() { 
        return artista; 
    }
    public int getDuracionSegundos() {
        return duracionSegundos; 
    }
    public File getRuta() {
        return ruta; 
    }
    public BufferedImage getPortada() {
        return portada; 
    }
}