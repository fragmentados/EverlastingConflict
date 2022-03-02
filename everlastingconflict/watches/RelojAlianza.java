package everlastingconflict.watches;

import everlastingconflict.elements.impl.Nave;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.Alianza;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.windows.Mensaje;
import everlastingconflict.windows.UI;
import everlastingconflict.windows.WindowCombat;
import everlastingconflict.windows.WindowMain;
import org.newdawn.slick.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static everlastingconflict.races.Alianza.numberOfUnitsToDesembarc;
import static everlastingconflict.races.Alianza.unitToDesembarc;
import static everlastingconflict.windows.UI.anchura_miniatura;
import static everlastingconflict.windows.WindowCombat.VIEWPORT_SIZE_HEIGHT;

public class RelojAlianza extends Reloj {

    public float beginingFirstHalf = 20f;
    public float endFirstHalf = 10f;
    public Nave shipAssociated;

    public RelojAlianza(Jugador jugadorAsociado, Nave shipAssociated) {
        this.jugadorAsociado = jugadorAsociado;
        this.shipAssociated = shipAssociated;
        contador_reloj = beginingFirstHalf;
        ndivision = 1;
        detener = detener_contador = 0;
        try {
            sprite = new Image("media/Iconos/" + shipAssociated.nombre + "Despegue.png");
        } catch (SlickException ex) {
            Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
        }
        hintBoxText = "Este reloj representa el tiempo restante para el siguiente desembarco de refuerzos de la " +
                "Alianza."
                + " En el minimapa se resaltará el punto en el que se realizará el desembarco";
        hintBoxText = Game.formatTextToFitWidth(this.hintBoxText, hintBoxWidth);
        anchura = 80;
        altura = 80;
    }

    @Override
    public float tiempo_restante() {
        return (ndivision == 1) ? contador_reloj - endFirstHalf : contador_reloj;
    }

    public void updateSprite() throws SlickException {
        if (ndivision == 2) {
            sprite = new Image("media/Iconos/" + Alianza.unitToDesembarc + ".png");
        } else {
            sprite = new Image("media/Iconos/" + shipAssociated.nombre + "Despegue.png");
        }
    }

    @Override
    public void avanzar_reloj(Game game, int delta) throws SlickException {
        if (detener > 0) {
            if ((detener_contador - Reloj.TIME_REGULAR_SPEED * delta) <= 0) {
                detener = 0;
                detener_contador = 0;
            } else {
                detener_contador -= Reloj.TIME_REGULAR_SPEED * delta;
            }
        } else {
            if (ndivision == 2 || contador_reloj > endFirstHalf) {
                contador_reloj -= Reloj.TIME_REGULAR_SPEED * delta;
                if (ndivision == 1 && contador_reloj <= endFirstHalf) {
                    contador_reloj = endFirstHalf;
                } else if (ndivision == 2 && contador_reloj <= 0) {
                    contador_reloj = beginingFirstHalf;
                    ndivision = 1;
                    updateSprite();
                    shipAssociated.prepareToTakeOffOrToLand();
                }
            }
        }
    }

    @Override
    public void dibujar(Input input, Graphics g) {
        g.setColor(new Color(1f, 1f, 1f, 0.7f));
        g.fillOval(this.x, this.y, this.anchura, this.altura);
        if (detener > 0) {
            g.setColor(Color.gray);
        } else {
            g.setColor(RaceEnum.ALIANZA.getColor());
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
                g.setColor(RaceEnum.ALIANZA.getColor());
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
                g.setColor(RaceEnum.ALIANZA.getColor());
            }
            g.drawOval(this.x + 35, this.y + 75, anchurap, alturap);
        }
        String tiempo;
        if (detener > 0) {
            tiempo = Reloj.tiempo_a_string(detener_contador);
        } else {
            tiempo = Reloj.tiempo_a_string(tiempo_restante());
        }
        if (!NO_TIME_REMAINING.equals(tiempo)) {
            g.setColor(Color.white);
            g.fillRect(this.x + 65 - tiempo.length() * 10, this.y + 30, tiempo.length() * 10, 15);
            g.setColor(Color.black);
            g.drawString(tiempo, this.x + 65 - tiempo.length() * 10, this.y + 30);
            g.setColor(Color.white);
        }
        if (this.hitbox(WindowCombat.playerX + input.getMouseX(), WindowCombat.playerY + input.getMouseY())) {
            drawHint(g);
        }
    }

    @Override
    public void handleLeftClick() {
        if (ndivision == 1 && contador_reloj == endFirstHalf) {
            int costToDesembarc = new Unidad(jugadorAsociado, unitToDesembarc).coste * numberOfUnitsToDesembarc;
            if (jugadorAsociado.comprobacion_recursos(costToDesembarc)) {
                // Seleccionamos la nave como edificio a construir
                WindowCombat.edificio = shipAssociated;
            } else {
                WindowMain.combatWindow
                        .anadir_mensaje(new Mensaje("No hay suficientes recursos. Se necesitan " + costToDesembarc +
                                " para " +
                                "desembarcar",
                                Color.red, WindowCombat.playerX + anchura_miniatura,
                                WindowCombat.playerY + VIEWPORT_SIZE_HEIGHT - UI.UI_HEIGHT - 20, 5f));
            }
        }
    }
}
