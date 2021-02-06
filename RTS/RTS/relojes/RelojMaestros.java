/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.relojes;

import ElementosSlick2D.BotonComplejo;
import ElementosSlick2D.BotonManipulador;
import RTS.elementos.implementacion.Unidad;
import RTS.gestion.Jugador;
import RTS.mapas.MapaCampo;
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
public class RelojMaestros extends Reloj {

    public float inicio_primera_mitad = 600f;
    public float fin_primera_mitad = 300f;

    public static final String nombre_dia = "Día";
    public static final String nombre_noche = "Noche";
    public static String tiempo = nombre_dia;

    public RelojMaestros() {
        contador_reloj = inicio_primera_mitad;
        ndivision = 1;
        detener = detener_contador = 0;
        try {
            sprite = new Image("datar/Iconos/Sol.png");
        } catch (SlickException ex) {
            Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cambio_temporal(Jugador j, String t) {
        RelojMaestros.tiempo = t;
        for (Unidad u : j.unidades) {
            if (u.nombre.equals("Manipulador")) {
                for (BotonComplejo b : u.botones) {
                    if (b instanceof BotonManipulador) {
                        BotonManipulador bm = (BotonManipulador) b;
                        if (bm.requisito.equals(t)) {
                            bm.activado = true;
                        } else {
                            if (!bm.requisito.equals("Cualquiera")) {
                                bm.activado = false;
                            }
                        }
                    }
                }
                break;
            }
        }
    }

    @Override
    public void avanzar_reloj(Jugador j, int delta) {
        if (detener > 0) {
            if ((detener_contador - Reloj.velocidad_reloj * delta) <= 0) {
                detener = 0;
                detener_contador = 0;
            } else {
                detener_contador -= Reloj.velocidad_reloj * delta;
            }
        } else {
            contador_reloj -= Reloj.velocidad_reloj * delta;
            if ((ndivision == 2) && (contador_reloj <= 0)) {
                contador_reloj = inicio_primera_mitad;
                ndivision = 1;
                cambio_temporal(j, RelojMaestros.nombre_dia);
                try {
                    sprite = new Image("datar/Iconos/Sol.png");
                } catch (SlickException ex) {
                    Logger.getLogger(RelojMaestros.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if ((ndivision == 1) && (contador_reloj <= fin_primera_mitad)) {
                ndivision = 2;
                cambio_temporal(j, RelojMaestros.nombre_noche);
                try {
                    sprite = new Image("datar/Iconos/Luna.png");
                } catch (SlickException ex) {
                    Logger.getLogger(RelojMaestros.class.getName()).log(Level.SEVERE, null, ex);
                }
                contador_reloj = fin_primera_mitad;
            }
        }
    }

    @Override
    public float tiempo_restante() {
        if (ndivision == 1) {
            return contador_reloj - fin_primera_mitad;
        }
        if (ndivision == 2) {
            return contador_reloj;
        }
        return -1;
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
            g.setColor(new Color(0.5f, 0f, 0.5f, 0.5f));
        }
        g.drawOval(xg, yg, anchurag, alturag);
        sprite.draw(xg + anchurag / 2 - sprite.getWidth() / 2, yg + alturag / 2 - sprite.getHeight() / 2);
        float anchurap = 10, alturap = 10;
        if (ndivision == 1) {
            g.fillOval(xg + 35, yg - 5, anchurap, alturap);
        } else {
            g.setColor(new Color(1f, 1f, 1f, 0.7f));
            g.fillOval(xg + 35, yg - 5, anchurap, alturap);
            if (detener > 0) {
                g.setColor(Color.gray);
            } else {
                g.setColor(new Color(0.5f, 0f, 0.5f, 0.5f));
            }
            g.drawOval(xg + 35, yg - 5, anchurap, alturap);
        }
        if (ndivision == 2) {
            g.fillOval(xg + 35, yg + 75, anchurap, alturap);
        } else {
            g.setColor(new Color(1f, 1f, 1f, 0.7f));
            g.fillOval(xg + 35, yg + 75, anchurap, alturap);
            if (detener > 0) {
                g.setColor(Color.gray);
            } else {
                g.setColor(new Color(0.5f, 0f, 0.5f, 0.5f));
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
}
