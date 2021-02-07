package everlastingconflict.elementos.util;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class ElementosComunes {

    public static Sound CONSTRUCTION_SOUND;
    public static Sound UNIT_DEATH_SOUND;
    public static Sound BUILDING_DEATH_SOUND;
    public static Sound VICTORY_SOUND;

    public static void init() throws SlickException {
        CONSTRUCTION_SOUND = new Sound("media/Sonidos/Construccion.ogg");
        UNIT_DEATH_SOUND = new Sound("media/Sonidos/MuerteUnidad.ogg");
        BUILDING_DEATH_SOUND = new Sound("media/Sonidos/MuerteEdificio.ogg");
        VICTORY_SOUND = new Sound("media/Sonidos/Victoria.ogg");
    }
}
