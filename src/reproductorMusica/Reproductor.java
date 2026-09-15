package reproductorMusica;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javax.swing.SwingUtilities;

public class Reproductor {

    private Cancion cancionActual;
    private AdvancedPlayer advancedPlayer;
    private Thread hiloReproduccion;

    private int framePausa = 0;
    private boolean estaPausado = false;
    private boolean estaReproduciendo = false;

    private Runnable alTerminarCancion;

    public Reproductor() {}

    public void setAlTerminarCancion(Runnable callback) {
        this.alTerminarCancion = callback;
    }

    public void cargarCancion(Cancion cancion) {
        detener();
        this.cancionActual = cancion;
        this.framePausa = 0;

        if (cancionActual != null) {
            reproducir();
        }
    }

    public void reproducir() {
        if (cancionActual == null) return;

        try {
            FileInputStream fis = new FileInputStream(cancionActual.getRuta().getAbsolutePath());
            BufferedInputStream bis = new BufferedInputStream(fis);

            advancedPlayer = new AdvancedPlayer(bis);
            advancedPlayer.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    if (estaPausado) {
                        framePausa += evt.getFrame();
                    }
                }
            });

            estaPausado = false;
            estaReproduciendo = true;

            hiloReproduccion = new Thread(() -> {
                try {
                    advancedPlayer.play(framePausa, Integer.MAX_VALUE);
                    
                    if (estaReproduciendo && !estaPausado) {
                        estaReproduciendo = false;
                        framePausa = 0;
                        if (alTerminarCancion != null) {
                            SwingUtilities.invokeLater(alTerminarCancion);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error durante la reproducción: " + e.getMessage());
                }
            });

            hiloReproduccion.start();

        } catch (Exception e) {
            e.printStackTrace();
            estaReproduciendo = false;
        }
    }

    public void saltarA(int segundo) {
        if (cancionActual == null) return;
        
        int frameObjetivo = (int) (segundo * 38.28);
        detener();
        this.framePausa = frameObjetivo;
        reproducir();
    }

    public void pausar() {
        if (advancedPlayer != null && estaReproduciendo) {
            estaPausado = true;
            estaReproduciendo = false;
            advancedPlayer.close();
        }
    }

    public void alternarPlayPausa() {
        if (estaReproduciendo) {
            pausar();
        } else {
            reproducir();
        }
    }

    public void detener() {
        estaReproduciendo = false;
        estaPausado = false;
        if (advancedPlayer != null) {
            advancedPlayer.close();
        }

        if (hiloReproduccion != null && hiloReproduccion.isAlive()) {
            hiloReproduccion.interrupt();
        }
    }

    public Cancion getCancionActual() { 
        return cancionActual; 
    }
    public boolean isReproduciendo() { 
        return estaReproduciendo; 
    }
    public boolean isPausado() { 
        return estaPausado; 
    }
}