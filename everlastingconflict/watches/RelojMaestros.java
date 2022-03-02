/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.watches;

import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elementosvisuales.BotonManipulador;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.elements.util.ElementosComunes;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;


public class RelojMaestros extends Reloj {

    public float inicio_primera_mitad = 600f;
    public float fin_primera_mitad = 300f;

    public static final String nombre_dia = "Día";
    public static final String nombre_noche = "Noche";
    public static String tiempo = nombre_dia;

    public RelojMaestros(Jugador jugadorAsociado) {
        this.jugadorAsociado = jugadorAsociado;
        contador_reloj = inicio_primera_mitad;
        ndivision = 1;
        detener = detener_contador = 0;
        sprite = ElementosComunes.DAY_IMAGE;
        hintBoxText = "Este reloj representa el tiempo que tarda en pasar del día a la noche."
                + " El Manipulador tendrá habilidades diferentes en cuanto cambie el reloj."
                + " También hay unidades que son más fuertes en el día o en la noche.";
        hintBoxText = Game.formatTextToFitWidth(this.hintBoxText, hintBoxWidth);
        anchura = 80;
        altura = 80;
    }

    public void cambio_temporal(String t) {
        RelojMaestros.tiempo = t;
        if (tiempo.equals(nombre_dia)) {
            sprite = ElementosComunes.DAY_IMAGE;
        } else {
            sprite = ElementosComunes.NIGHT_IMAGE;
        }
        for (Unidad u : this.jugadorAsociado.unidades) {
            if (u.nombre.equals("Manipulador")) {
                for (BotonComplejo b : u.botones) {
                    if (b instanceof BotonManipulador) {
                        BotonManipulador bm = (BotonManipulador) b;
                        if (bm.requisito != null) {
                            bm.canBeUsed = bm.requisito.equals(t);
                        }
                    }
                }
                break;
            }
        }
    }

    @Override
    public void avanzar_reloj(Game game, int delta) {
        if (detener > 0) {
            if ((detener_contador - Reloj.TIME_REGULAR_SPEED * delta) <= 0) {
                detener = 0;
                detener_contador = 0;
            } else {
                detener_contador -= Reloj.TIME_REGULAR_SPEED * delta;
            }
        } else {
            contador_reloj -= Reloj.TIME_REGULAR_SPEED * delta;
            if ((ndivision == 2) && (contador_reloj <= 0)) {
                contador_reloj = inicio_primera_mitad;
                ndivision = 1;
                cambio_temporal(RelojMaestros.nombre_dia);
            }
            if ((ndivision == 1) && (contador_reloj <= fin_primera_mitad)) {
                ndivision = 2;
                cambio_temporal(RelojMaestros.nombre_noche);
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
    public void dibujar(Input input, Graphics g) {
        g.setColor(new Color(1f, 1f, 1f, 0.7f));
        g.fillOval(this.x, this.y, this.anchura, this.altura);
        if (detener > 0) {
            g.setColor(Color.gray);
        } else {
            g.setColor(new Color(0.5f, 0f, 0.5f, 0.5f));
        }
        g.drawOval(this.x, this.y, this.anchura, this.altura);
        sprite.draw(this.x + this.anchura / 2 - sprite.getWidth() / 2,
                this.y + this.altura / 2 - sprite.getHeight() / 2);
        float anchurap = 10, alturap = 10;
        if (ndivision == 1) {
            g.fillOval(this.x + 35, this.y - 5, anchurap, alturap);
        } else {
            g.setColor(new Color(1f, 1f, 1f, 0.7f));
            g.fillOval(this.x + 35, this.y - 5, anchurap, alturap);
            if (detener > 0) {
                g.setColor(Color.gray);
            } else {
                g.setColor(new Color(0.5f, 0f, 0.5f, 0.5f));
            }
            g.drawOval(this.x + 35, this.y - 5, anchurap, alturap);
        }
        if (ndivision == 2) {
            g.fillOval(this.x + 35, this.y + 75, anchurap, alturap);
        } else {
            g.setColor(new Color(1f, 1f, 1f, 0.7f));
            g.fillOval(this.x + 35, this.y + 75, anchurap, alturap);
            if (detener > 0) {
                g.setColor(Color.gray);
            } else {
                g.setColor(new Color(0.5f, 0f, 0.5f, 0.5f));
            }
            g.drawOval(this.x + 35, this.y + 75, anchurap, alturap);
        }
        String tiempo;
        if (detener > 0) {
            tiempo = Reloj.tiempo_a_string(detener_contador);
        } else {
            tiempo = Reloj.tiempo_a_string(tiempo_restante());
        }
        g.setColor(Color.black);
        g.drawString(tiempo, this.x + 65 - tiempo.length() * 10, this.y + 30);
        g.setColor(Color.white);
        if (this.hitbox(WindowCombat.playerX + input.getMouseX(), WindowCombat.playerY + input.getMouseY())) {
            drawHint(g);
        }
    }

    @Override
    public void handleLeftClick() {

    }

}
