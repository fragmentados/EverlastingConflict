package everlastingconflict.elements.impl;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.watches.Reloj;
import everlastingconflict.watches.RelojAlianza;
import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import static everlastingconflict.behaviour.BehaviourEnum.DESPEGANDO;
import static everlastingconflict.races.Alianza.*;

public class Nave extends Edificio {

    public float landingX;
    public float landingY;

    public Nave(Jugador aliado, String n) {
        super(aliado, n);
    }

    public Nave(Jugador aliado, String n, float x, float y) {
        super(aliado, n, x, y);
    }

    public Nave(Jugador aliado, Edificio e) {
        super(aliado, e);
    }

    @Override
    public void comportamiento(Game p, Graphics g, int delta) {
        super.comportamiento(p, g, delta);
        Jugador aliado = p.getPlayerFromElement(this);
        if (DESPEGANDO.equals(behaviour)) {
            behaviourTimer -= Reloj.TIME_REGULAR_SPEED * delta;
            if (behaviourTimer <= 0) {
                behaviourTimer = 0;
                if (WindowCombat.alianceWatch(this).ndivision == 2) {
                    takeOff(aliado);
                } else {
                    landing(aliado);
                }
            }
        }
    }

    public void updateLandingCoordinates(float x, float y) {
        this.landingX = x;
        this.landingY = y;
    }

    public void prepareToTakeOffOrToLand() {
        isVisible = true;
        behaviour = BehaviourEnum.DESPEGANDO;
        animation.setAutoUpdate(true);
        behaviourTimer = 2f;
    }

    public void takeOff(Jugador aliado) {
        behaviour = BehaviourEnum.PARADO;
        animation.restart();
        animation.setAutoUpdate(false);
        isVisible = false;
        x = landingX;
        y = landingY;
        reunion_x = x + anchura / 2 + 10;
        reunion_y = y;
        if (!RESOURCES.equals(unitToDesembarc)) {
            aliado.removeResources(new Unidad(aliado, unitToDesembarc).coste * numberOfUnitsToDesembarc);
        }
    }

    public void landing(Jugador aliado) {
        behaviour = BehaviourEnum.PARADO;
        animation.restart();
        animation.setAutoUpdate(false);
        if (RESOURCES.equals(unitToDesembarc)) {
            aliado.addResources(100, x + anchura / 2 - 20, y - altura / 2 - 20);
        } else {
            Unidad unit = new Unidad(aliado, unitToDesembarc);
            setInitialCoordinatesForCreatedUnit(unit);
            cola_construccion.add(unit);
            cantidad_produccion.add(numberOfUnitsToDesembarc);
        }
    }

    public void selectLandingCoordinates(float x, float y) {
        updateLandingCoordinates(x, y);
        RelojAlianza alianceWatch = WindowCombat.alianceWatch(this);
        alianceWatch.ndivision = 2;
        try {
            alianceWatch.updateSprite();
        } catch (SlickException e) {
        }
        prepareToTakeOffOrToLand();
    }

}
