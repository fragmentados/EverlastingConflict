/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.relojes;

import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.mapas.MapaCampo;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Elías
 */
public class RelojEternium extends Reloj {

    //Primer cuarto:    Capacidades ofensivas y defensivas al 75%
    //Segundo cuarto    Capacidades ofensivas y defensivas al 100%
    //Tercer cuarto     Capacidades ofensivas y defensivas al 150%
    //Cuarto cuarto     Capacidades ofensivas al 0% Capacidades defensivas al 500%
    private int minsPerQuarter = 1;
    public float inicio_primer_cuarto = 4 * minsPerQuarter * 60;
    public float fin_primer_cuarto = 3 * minsPerQuarter * 60;
    public float fin_segundo_cuarto = 2 * minsPerQuarter * 60;
    public float fin_tercer_cuarto = 1 * minsPerQuarter * 60;
    
    public RelojEternium(Jugador jugadorAsociado) {
        this.jugadorAsociado = jugadorAsociado;
        contador_reloj = inicio_primer_cuarto;
        ndivision = 1;
        detener = detener_contador = 0;
        try {
            sprite = new Image("media/Iconos/Reloj.png");
        } catch (SlickException ex) {
            Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public float tiempo_restante() {
        if (ndivision == 1) {
            return contador_reloj - fin_primer_cuarto;
        }
        if (ndivision == 2) {
            return contador_reloj - fin_segundo_cuarto;
        }
        if (ndivision == 3) {
            return contador_reloj - fin_tercer_cuarto;
        }
        if (ndivision == 4) {
            return contador_reloj;
        }
        return -1;
    }

    @Override
    public void avanzar_reloj(int delta) {
        if (detener > 0) {
            if ((detener_contador - Reloj.velocidad_reloj * delta) <= 0) {
                detener = 0;
                detener_contador = 0;
            } else {
                detener_contador -= Reloj.velocidad_reloj * delta;
            }
        } else {
            contador_reloj -= Reloj.velocidad_reloj * delta;
            if ((ndivision == 4) && (contador_reloj <= 0)) {
                contador_reloj = inicio_primer_cuarto;
                ndivision = 1;
                liberacion_temporal();
            } else if ((ndivision == 3) && (contador_reloj <= fin_tercer_cuarto)) {
                contador_reloj = fin_tercer_cuarto;
                ndivision = 4;
                detencion_temporal();
            } else if ((ndivision == 2) && (contador_reloj <= fin_segundo_cuarto)) {
                contador_reloj = fin_segundo_cuarto;
                ndivision = 3;
            } else if ((ndivision == 1) && (contador_reloj <= fin_primer_cuarto)) {
                contador_reloj = fin_primer_cuarto;
                ndivision = 2;
            }
        }
    }

    @Override
    public void dibujar(Graphics g) {
        float anchurag = 80, alturag = 80;
        float xg = MapaCampo.playerX + MapaCampo.VIEWPORT_SIZE_X / 2 - 100, yg = MapaCampo.playerY + 5;
        g.setColor(new Color(1f, 1f, 1f, 0.7f));
        g.fillOval(xg, yg, anchurag, alturag);
        if (detener > 0) {
            g.setColor(Color.gray);
        } else {
            g.setColor(Color.cyan);
        }
        g.drawOval(xg, yg, anchurag, alturag);
        sprite.draw(xg + anchurag / 2 - sprite.getWidth() / 2, yg + alturag / 2 - sprite.getHeight() / 2);
        float anchurap = 10, alturap = 10;
        if (ndivision >= 1) {
            g.fillOval(xg - 5, yg + 30, anchurap, alturap);
        } else {
            g.setColor(new Color(1f, 1f, 1f, 0.7f));
            g.fillOval(xg - 5, yg + 30, anchurap, alturap);
            if (detener > 0) {
                g.setColor(Color.gray);
            } else {
                g.setColor(Color.cyan);
            }
            g.drawOval(xg - 5, yg + 30, anchurap, alturap);
        }
        if (ndivision >= 2) {
            g.fillOval(xg + 35, yg - 5, anchurap, alturap);
        } else {
            g.setColor(new Color(1f, 1f, 1f, 0.7f));
            g.fillOval(xg + 35, yg - 5, anchurap, alturap);
            if (detener > 0) {
                g.setColor(Color.gray);
            } else {
                g.setColor(Color.cyan);
            }
            g.drawOval(xg + 35, yg - 5, anchurap, alturap);
        }
        if (ndivision >= 3) {
            g.fillOval(xg + 75, yg + 30, anchurap, alturap);
        } else {
            g.setColor(new Color(1f, 1f, 1f, 0.7f));
            g.fillOval(xg + 75, yg + 30, anchurap, alturap);
            if (detener > 0) {
                g.setColor(Color.gray);
            } else {
                g.setColor(Color.cyan);
            }
            g.drawOval(xg + 75, yg + 30, anchurap, alturap);
        }
        if (ndivision == 4) {
            g.fillOval(xg + 35, yg + 75, anchurap, alturap);
        } else {
            g.setColor(new Color(1f, 1f, 1f, 0.7f));
            g.fillOval(xg + 35, yg + 75, anchurap, alturap);
            if (detener > 0) {
                g.setColor(Color.gray);
            } else {
                g.setColor(Color.cyan);
            }
            g.drawOval(xg + 35, yg + 75, anchurap, alturap);
        }
        String tiempo;
        if (detener > 0) {
            tiempo = Reloj.tiempo_a_string(detener_contador);
        } else {
            tiempo = Reloj.tiempo_a_string(tiempo_restante());
        }
        g.setColor(Color.black);
        g.drawString(tiempo, xg + 65 - tiempo.length() * 10, yg + 30);
        g.setColor(Color.white);
    }

    public void liberacion_temporal() {
        for (Unidad u : this.jugadorAsociado.unidades) {
            if (u.nombre.equals("Protector")) {
                u.ataque = 0;
            } else {
                u.movil = true;
                u.hostil = true;
            }
        }
    }

    public void detencion_temporal() {
        for (Unidad u : this.jugadorAsociado.unidades) {
            if (u.nombre.equals("Protector")) {
                u.ataque = Unidad.ataque_estandar + 30;
            } else {
                u.movil = false;
                u.hostil = false;
            }
        }
    }

}
