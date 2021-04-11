package everlastingconflict.races;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.watches.RelojAlianza;
import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Alianza {

    public static String unitToDesembarc = "Granadero";
    public static final String RESOURCES = "Recursos";
    public static Integer numberOfUnitsToDesembarc = 5;
    public static Animation LANDING_ANIMATION;
    public static float landingX;
    public static float landingY;

    public static void initElements() {
        try {
            LANDING_ANIMATION = new Animation(new Image[]{new Image("media/Edificios/Aterrizaje1.png"), new Image(
                    "media/Edificios/Aterrizaje2.png")}, 450,
                    true);
        } catch (SlickException e) {
        }
    }

    public static void unidad(Jugador aliado, Unidad u) {
        switch (u.nombre) {
            case "Granadero":
                Alianza.Granadero(u);
                break;
            case "Luchadora":
                Alianza.Luchadora(u);
        }
    }

    public static final void Luchadora(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 15;
        u.defensa = Unidad.defensa_estandar + 1;
        u.vida_max = Unidad.vida_estandar + 50;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar;
        u.alcance = Unidad.MELEE_RANGE;
        u.vision = Unidad.vision_estandar;
        u.coste = 10;
        u.tiempo = 0;
        u.descripcion = "Unidad de corto alcance con ataque de área";
    }

    public static final void Granadero(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 10;
        u.defensa = Unidad.defensa_estandar + 1;
        u.vida_max = Unidad.vida_estandar;
        u.cadencia = Unidad.cadencia_estandar + 1;
        u.velocidad = Unidad.velocidad_estandar;
        u.alcance = Unidad.alcance_estandar + 50;
        u.vision = Unidad.vision_estandar;
        u.coste = 10;
        u.tiempo = 0;
        u.area = 50;
        u.descripcion = "Unidad de corto alcance con ataque de área";
        u.hostil = true;
        u.healer = true;
    }

    public static void edificio(Jugador aliado, Edificio e) {
        switch (e.nombre) {
            case "Nave":
                Alianza.Nave(e);
                break;
        }
    }

    public static final void Nave(Edificio e) {
        e.vida_max = 3000;
        e.vision = Unidad.vision_estandar + 200;
        e.descripcion = "Edificio central de la raza Alianza.";
        e.main = true;
        e.unitCreator = true;
    }

    public static void iniciar_botones_edificio(Jugador aliado, Edificio e) {
        switch (e.nombre) {
            case "Nave":
                e.botones.add(new BotonComplejo("Granadero"));
                e.botones.add(new BotonComplejo("Luchadora"));
                e.botones.add(new BotonComplejo(RESOURCES));
                break;
        }
    }

    public static void shipPrepareToTakeOffOrToLand(Jugador aliado) {
        Edificio nave = aliado.edificios.stream().filter(e -> e.main).findFirst().orElse(null);
        if (nave != null) {
            nave.isVisible = true;
            nave.behaviour = BehaviourEnum.DESPEGANDO;
            nave.animation.setAutoUpdate(true);
            nave.behaviourTimer = 2f;
            if (!RESOURCES.equals(unitToDesembarc)) {
                aliado.removeResources(new Unidad(aliado, Alianza.unitToDesembarc).coste * numberOfUnitsToDesembarc);
            }
        }
    }

    public static void updateLandingCoordinates(float x, float y) {
        landingX = x;
        landingY = y;
    }

    public static void selectLandingCoordinates(Jugador aliado, float x, float y) {
        updateLandingCoordinates(x, y);
        RelojAlianza alianceWatch = WindowCombat.alianceWatch();
        alianceWatch.ndivision = 2;
        try {
            alianceWatch.updateSprite();
        } catch (SlickException e) {
        }
        Alianza.shipPrepareToTakeOffOrToLand(aliado);
    }

    public static void shipTakeOff(Jugador aliado, Edificio nave) {
        nave.behaviour = BehaviourEnum.PARADO;
        nave.animation.restart();
        nave.animation.setAutoUpdate(false);
        nave.isVisible = false;
        nave.x = landingX;
        nave.y = landingY;
        nave.reunion_x = nave.x + nave.anchura / 2 + 10;
        nave.reunion_y = nave.y;
    }

    public static void shipLanding(Jugador aliado, Edificio nave) {
        nave.behaviour = BehaviourEnum.PARADO;
        nave.animation.restart();
        nave.animation.setAutoUpdate(false);
        if (RESOURCES.equals(unitToDesembarc)) {
            aliado.addResources(100, nave.x + nave.anchura / 2 - 20, nave.y - nave.altura / 2 - 20);
        } else {
            Unidad unit = new Unidad(aliado, unitToDesembarc);
            nave.setInitialCoordinatesForCreatedUnit(unit);
            nave.cola_construccion.add(unit);
            nave.cantidad_produccion.add(numberOfUnitsToDesembarc);
        }
    }

}
