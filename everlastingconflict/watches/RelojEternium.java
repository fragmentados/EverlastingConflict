/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.watches;

import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.*;

import java.util.logging.Level;
import java.util.logging.Logger;


public class RelojEternium extends Reloj {

    //Primer cuarto:    Capacidades ofensivas y defensivas al 75%
    //Segundo cuarto    Capacidades ofensivas y defensivas al 100%
    //Tercer cuarto     Capacidades ofensivas y defensivas al 150%
    //Cuarto cuarto     Capacidades ofensivas al 0% Capacidades defensivas al 500%
    public float minsPerQuarter = 0.5f;
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
        hintBoxText = "Este reloj representa diferentes fases de la partida para el jugador Eternium."
                + " En la primera fase las capacidades militares del ejercito estan al 75%"
                + " En la segunda fase las capacidades militares del ejercito estan al 100%"
                + " En la tercera fase las capacidades militares del ejercito estan al 150%"
                + " En la cuarta fase las unidades Eternium no pueden moverse ni atacar";
        hintBoxText = Game.anadir_saltos_de_linea(this.hintBoxText, hintBoxWidth);
        anchura = 80;
        altura = 80;
    }

    public RelojEternium(Jugador jugadorAsociado, float minsPerQuarter) {
        this(jugadorAsociado);
        this.minsPerQuarter = minsPerQuarter;
        inicio_primer_cuarto = 4 * minsPerQuarter * 60;
        fin_primer_cuarto = 3 * minsPerQuarter * 60;
        fin_segundo_cuarto = 2 * minsPerQuarter * 60;
        fin_tercer_cuarto = 1 * minsPerQuarter * 60;
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
            if ((detener_contador - Reloj.TIME_REGULAR_SPEED * delta) <= 0) {
                detener = 0;
                detener_contador = 0;
            } else {
                detener_contador -= Reloj.TIME_REGULAR_SPEED * delta;
            }
        } else {
            contador_reloj -= Reloj.TIME_REGULAR_SPEED * delta;
            if ((ndivision == 4) && (contador_reloj <= 0)) {
                contador_reloj = inicio_primer_cuarto;
                ndivision = 1;
                temporalRelease();
            } else if ((ndivision == 3) && (contador_reloj <= fin_tercer_cuarto)) {
                contador_reloj = fin_tercer_cuarto;
                ndivision = 4;
                temporalStop();
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
    public void dibujar(Input input, Graphics g) {
        this.x = WindowCombat.playerX + WindowCombat.VIEWPORT_SIZE_WIDTH / 2 - 200;
        this.y = WindowCombat.playerY + 5;
        g.setColor(new Color(1f, 1f, 1f, 0.7f));
        g.fillOval(this.x, this.y, this.anchura, this.altura);
        if (detener > 0) {
            g.setColor(Color.gray);
        } else {
            g.setColor(Color.cyan);
        }
        g.drawOval(this.x, this.y, this.anchura, this.altura);
        sprite.draw(this.x + this.anchura / 2 - sprite.getWidth() / 2,
                this.y + this.altura / 2 - sprite.getHeight() / 2);
        float anchuraPunto = 10, alturaPunto = 10;
        if (ndivision >= 1) {
            g.fillOval(this.x - 5, this.y + 30, anchuraPunto, alturaPunto);
        } else {
            g.setColor(new Color(1f, 1f, 1f, 0.7f));
            g.fillOval(this.x - 5, this.y + 30, anchuraPunto, alturaPunto);
            if (detener > 0) {
                g.setColor(Color.gray);
            } else {
                g.setColor(Color.cyan);
            }
            g.drawOval(this.x - 5, this.y + 30, anchuraPunto, alturaPunto);
        }
        if (ndivision >= 2) {
            g.fillOval(this.x + 35, this.y - 5, anchuraPunto, alturaPunto);
        } else {
            g.setColor(new Color(1f, 1f, 1f, 0.7f));
            g.fillOval(this.x + 35, this.y - 5, anchuraPunto, alturaPunto);
            if (detener > 0) {
                g.setColor(Color.gray);
            } else {
                g.setColor(Color.cyan);
            }
            g.drawOval(this.x + 35, this.y - 5, anchuraPunto, alturaPunto);
        }
        if (ndivision >= 3) {
            g.fillOval(this.x + 75, this.y + 30, anchuraPunto, alturaPunto);
        } else {
            g.setColor(new Color(1f, 1f, 1f, 0.7f));
            g.fillOval(this.x + 75, this.y + 30, anchuraPunto, alturaPunto);
            if (detener > 0) {
                g.setColor(Color.gray);
            } else {
                g.setColor(Color.cyan);
            }
            g.drawOval(this.x + 75, this.y + 30, anchuraPunto, alturaPunto);
        }
        if (ndivision == 4) {
            g.fillOval(this.x + 35, this.y + 75, anchuraPunto, alturaPunto);
        } else {
            g.setColor(new Color(1f, 1f, 1f, 0.7f));
            g.fillOval(this.x + 35, this.y + 75, anchuraPunto, alturaPunto);
            if (detener > 0) {
                g.setColor(Color.gray);
            } else {
                g.setColor(Color.cyan);
            }
            g.drawOval(this.x + 35, this.y + 75, anchuraPunto, alturaPunto);
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

    public void temporalRelease() {
        this.jugadorAsociado.unidades.stream().filter(u -> "Protector".equals(u.nombre)).forEach(p -> p.isProyectileAttraction = false);
    }

    public void temporalStop() {
        this.jugadorAsociado.unidades.stream().filter(u -> "Protector".equals(u.nombre)).forEach(p -> p.isProyectileAttraction = true);
        this.jugadorAsociado.unidades.stream().filter(u -> !"Protector".equals(u.nombre)).forEach(u -> u.parar());
    }

}
