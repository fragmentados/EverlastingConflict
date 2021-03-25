package everlastingconflict.elements.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class ElementosComunes {
    //Color
    public static final Color UI_COLOR = new Color(0f, 0f, 0.6f, 1f);
    // Common sounds
    public static Sound CONSTRUCTION_SOUND;
    public static Sound UNIT_DEATH_SOUND;
    public static Sound BUILDING_DEATH_SOUND;
    public static Sound VICTORY_SOUND;
    public static Sound DEFEAT_SOUND;
    public static Sound BACKGROUND_MUSIC_SOUND;
    // Image for resource drawing
    public static Image POPULATION_IMAGE;
    public static Image FENIX_CUARTEL;
    public static Image FENIX_UNIDADES_NO_CONSTRUCTORAS;
    public static Image MONEY_IMAGE;
    public static Image THREAT_IMAGE;
    // Colors for map drawing
    public static Color HALF_VISIBLE_COLOR = new Color(0.2f, 0.2f, 0.05f, 1f);
    public static Color FULL_VISIBLE_COLOR = new Color(0.3f, 0.3f, 0.1f, 1f);
    // Images used for fusion help
    public static Image CAZADOR_IMAGE;
    public static Image DEVORADOR_IMAGE;
    public static Image DEPREDADOR_IMAGE;
    public static Image AMAESTRADOR_IMAGE;
    public static Image INSPIRADOR_IMAGE;
    public static Image ESCUPIDOR_IMAGE;
    public static Image DEFENSOR_IMAGE;
    public static Image MOLDEADOR_IMAGE;
    public static Image DESMEMBRADOR_IMAGE;
    public static Image MATRIARCA_IMAGE;
    public static Image DESPEDAZADOR_IMAGE;
    public static Image REGURGITADOR_IMAGE;
    public static Image RUMIANTE_IMAGE;
    // Images used for pilot help
    public static Image ARTILLERO_IMAGE;
    public static Image EXPLORADOR_IMAGE;
    public static Image CORREDOR_IMAGE;
    public static Image INGENIERO_IMAGE;
    public static Image ARMERO_IMAGE;
    public static Image AMPARADOR_IMAGE;
    public static Image OTEADOR_IMAGE;
    public static Image BASIC_TURRET_IMAGE;
    public static Image TEMPORAL_DISTORTING_IMAGE;
    public static Image VIGILANCE_IMAGE;
    public static Image REPAIR_IMAGE;
    public static Image WALL_IMAGE;
    public static Image GUTLING_IMAGE;
    public static Image ARTILLERY_IMAGE;
    public static Image DEMOLISHER_IMAGE;
    // Day / Night Images
    public static Image DAY_IMAGE;
    public static Image NIGHT_IMAGE;
    // Victory condition icons
    public static Image LIDER_IMAGE;


    public static void init() throws SlickException {
        CONSTRUCTION_SOUND = new Sound("media/Sonidos/Construccion.wav");
        UNIT_DEATH_SOUND = new Sound("media/Sonidos/MuerteUnidad.wav");
        BUILDING_DEATH_SOUND = new Sound("media/Sonidos/MuerteEdificio.ogg");
        VICTORY_SOUND = new Sound("media/Sonidos/Victoria.ogg");
        DEFEAT_SOUND = new Sound("media/Sonidos/Derrota.wav");
        //BACKGROUND_MUSIC_SOUND = new Sound("media/Sonidos/MusicaFondo.ogg");
        POPULATION_IMAGE = new Image("media/Recursos/Poblacion.png");
        FENIX_CUARTEL = new Image("media/Recursos/CuartelFenix.png");
        FENIX_UNIDADES_NO_CONSTRUCTORAS = new Image("media/Recursos/FenixUnidadesNoConstructoras.png");
        MONEY_IMAGE = new Image("media/Recursos/IconoDinero.png");
        CAZADOR_IMAGE = new Image("media/Iconos/Cazador.png");
        DEVORADOR_IMAGE = new Image("media/Iconos/Devorador.png");
        AMAESTRADOR_IMAGE = new Image("media/Iconos/Amaestrador.png");
        INSPIRADOR_IMAGE = new Image("media/Iconos/Inspirador.png");
        ESCUPIDOR_IMAGE = new Image("media/Iconos/Escupidor.png");
        DEPREDADOR_IMAGE = new Image("media/Iconos/Depredador.png");
        DEFENSOR_IMAGE = new Image("media/Iconos/Defensor.png");
        MOLDEADOR_IMAGE = new Image("media/Iconos/Moldeador.png");
        DESMEMBRADOR_IMAGE = new Image("media/Iconos/Desmembrador.png");
        MATRIARCA_IMAGE = new Image("media/Iconos/Matriarca.png");
        DESPEDAZADOR_IMAGE = new Image("media/Iconos/Despedazador.png");
        REGURGITADOR_IMAGE = new Image("media/Iconos/Regurgitador.png");
        RUMIANTE_IMAGE = new Image("media/Iconos/Rumiante.png");
        DAY_IMAGE = new Image("media/Iconos/Sol.png");
        NIGHT_IMAGE = new Image("media/Iconos/Luna.png");
        THREAT_IMAGE = new Image("media/Recursos/GuardianesAmenaza.png");
        LIDER_IMAGE = new Image("media/Lider.png");

        ARTILLERO_IMAGE = new Image("media/Iconos/Artillero.png");
        EXPLORADOR_IMAGE = new Image("media/Iconos/Explorador.png");
        CORREDOR_IMAGE = new Image("media/Iconos/Corredor.png");
        INGENIERO_IMAGE = new Image("media/Iconos/Ingeniero.png");
        ARMERO_IMAGE = new Image("media/Iconos/Armero.png");
        AMPARADOR_IMAGE = new Image("media/Iconos/Amparador.png");
        OTEADOR_IMAGE = new Image("media/Iconos/Oteador.png");
        BASIC_TURRET_IMAGE = new Image("media/Iconos/Torreta defensiva.png");
        TEMPORAL_DISTORTING_IMAGE = new Image("media/Torretas/Distorsionador temporal_icono.png");
        VIGILANCE_IMAGE = new Image("media/Torretas/Estación de vigilancia_icono.png");
        REPAIR_IMAGE = new Image("media/Torretas/Estación reparadora_icono.png");
        WALL_IMAGE = new Image("media/Torretas/Muro_icono.png");
        GUTLING_IMAGE = new Image("media/Torretas/Ametralladora_icono.png");
        ARTILLERY_IMAGE = new Image("media/Torretas/Torreta artillería_icono.png");
        DEMOLISHER_IMAGE = new Image("media/Torretas/Torreta demoledora_icono.png");
    }
}
