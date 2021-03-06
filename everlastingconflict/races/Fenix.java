/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.races;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Habilidad;
import everlastingconflict.elements.impl.Tecnologia;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.SubRaceEnum;


public class Fenix {

    public static int ataque_tigre = Unidad.ataque_estandar;
    public static int alcance_halcon = Unidad.alcance_estandar + 100;
    public static int limite_unidades_no_militares = 5;
    public static boolean boton_cuartel_cuervo = false;
    public static boolean boton_cuartel_tortuga = false;
    public static boolean boton_cuartel_oso = false;
    public static boolean boton_cuartel_fenix = false;
    public static boolean boton_cuartel_tigre = false;
    public static boolean boton_cuartel_halcon = false;

    public static final void Recolector(Unidad u) {
        u.ataque = 0;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar + 0.1f;
        u.coste = 0;
        u.tiempo = Unidad.tiempo_estandar - 4;
        u.vision = Unidad.vision_estandar - 100;
        u.constructor = true;
        u.hostil = false;
        u.descripcion = "Unidad encargada de influir en las ciudades civiles con el fin de que se unan a la causa " +
                "Fénix.";
    }

    public static final void Constructor(Unidad u) {
        u.ataque = 0;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar;
        u.coste = 0;
        u.tiempo = Unidad.tiempo_estandar - 4;
        u.vision = Unidad.vision_estandar - 100;
        u.constructor = true;
        u.hostil = false;
        u.descripcion = "Unidad encargada de levantar los edificios Fénix.";
    }

    //Unidades Reales
    public static final void Tigre(Unidad u) {
        u.ataque = Unidad.ataque_estandar - 2;
        u.defensa = Unidad.defensa_estandar + 1;
        u.vida_max = Unidad.vida_estandar;
        u.velocidad = Unidad.velocidad_estandar + 0.1f;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar;
        u.vision = Unidad.vision_estandar + 50;
        u.coste = 10;
        u.tiempo = Unidad.tiempo_estandar + 5;
        u.descripcion = "Unidad ofensiva básica de medio alcance.";
    }

    public static final void Halcon(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 20;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar - 25;
        u.cadencia = Unidad.cadencia_estandar + 1;
        u.velocidad = Unidad.velocidad_estandar;
        u.alcance = Unidad.alcance_estandar * 2;
        u.vision = Unidad.vision_estandar;
        u.coste = 20;
        u.tiempo = Unidad.tiempo_estandar + 10;
        u.descripcion = "Unidad frágil de gran alcance y potencia de ataque.";
    }

    public static final void Fenix(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 10;
        u.defensa = Unidad.defensa_estandar + 4;
        u.vida_max = Unidad.vida_estandar + 150;
        u.alcance = Unidad.alcance_estandar - 50;
        u.cadencia = Unidad.cadencia_estandar + 2;
        u.velocidad = Unidad.velocidad_estandar - 0.2f;
        u.vision = Unidad.vision_estandar - 100;
        u.coste = 30;
        u.tiempo = Unidad.tiempo_estandar + 15;
        u.descripcion = "Unidad final con gran capacidad defensiva. Es capaz de volver a la vida tras recibir grandes" +
                " daños.";
    }

    public static final void Oso(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 15;
        u.defensa = Unidad.defensa_estandar + 2;
        u.vida_max = Unidad.vida_estandar + 150;
        u.alcance = Unidad.alcance_estandar - 70;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar + 0.5f;
        u.vision = Unidad.vision_estandar - 100;
        u.coste = 30;
        u.tiempo = Unidad.tiempo_estandar + 10;
        u.descripcion = "Unidad con gran capacidad ofensiva de ataque cuerpo a cuerpo y alta velocidad.";
    }

    public static final void Tortuga(Unidad u) {
        u.ataque = 0;
        u.defensa = Unidad.defensa_estandar + 6;
        u.vida_max = Unidad.vida_estandar + 200;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar;
        u.vision = Unidad.vision_estandar - 100;
        u.coste = 30;
        u.tiempo = Unidad.tiempo_estandar + 10;
        u.descripcion = "Unidad con gran capacidad defensiva que absorbe los ataques de los enemigos cercanos " +
                "automáticamente";
        u.hostil = false;
        u.isProyectileAttraction = true;
    }

    public static final void Cuervo(Unidad u) {
        u.ataque = 0;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar;
        u.vision = Unidad.vision_estandar + 200;
        u.coste = 30;
        u.tiempo = Unidad.tiempo_estandar + 15;
        u.descripcion = "Unidad sin capacidad ofensiva con poderosas habilidades para balancear la batalla";
        u.hostil = false;
    }

    //Edificios
    public static final void Centro(Edificio e) {
        e.coste = 0;
        e.vida_max = 500;
        e.vision = Unidad.vision_estandar;
        e.tiempo = 1000;
    }

    public static final void Sede(Edificio e) {
        e.vida_max = 3000;
        e.vision = Unidad.vision_estandar + 200;
        e.ataque = 25;
        e.alcance = 300;
        e.cadencia = Unidad.cadencia_estandar;
        e.hostil = true;
        e.descripcion = "Edificio central de la raza Fénix.";
        e.main = true;
        e.unitCreator = true;
    }

    public static final void CuartelFenix(Edificio e) {
        e.coste = 10;
        e.vida_max = Edificio.vida_estandar;
        e.vision = Edificio.vision_estandar;
        e.tiempo = Edificio.tiempo_estandar;
        e.behaviour = BehaviourEnum.CONSTRUYENDO;
        e.descripcion = "Edificio encargado de la producción de las unidades Fénix.";
        e.unitCreator = true;
    }

    public static final void CentroTecnologico(Edificio e) {
        e.coste = 20;
        e.vida_max = Edificio.vida_estandar / 2;
        e.vision = Edificio.vision_estandar;
        e.tiempo = Edificio.tiempo_estandar + 3;
        e.descripcion = "Edificio encargado de investigar las tecnologías.";
    }

    public static final void Academia(Edificio e) {
        e.coste = 20;
        e.vida_max = Edificio.vida_estandar;
        e.vision = Unidad.vision_estandar;
        e.tiempo = Edificio.tiempo_estandar + 3;
        e.descripcion = "Edificio encargado de desbloquear las unidades Fénix.";
    }

    public static void iniciar_botones_edificio(Jugador aliado, Edificio e) {
        switch (e.nombre) {
            case "Cuartel Fénix":
                e.botones.add(new BotonComplejo("Detener"));
                e.botones.add(new BotonComplejo("Tigre"));
                e.botones.add(new BotonComplejo("Halcón"));
                e.botones.add(new BotonComplejo("Fénix"));
                e.botones.get(1).canBeUsed = Fenix.boton_cuartel_tigre;
                e.botones.get(2).canBeUsed = Fenix.boton_cuartel_halcon;
                e.botones.get(3).canBeUsed = Fenix.boton_cuartel_fenix;
                if (SubRaceEnum.OSO.equals(aliado.subRace)) {
                    BotonComplejo oso = new BotonComplejo("Oso");
                    oso.canBeUsed = Fenix.boton_cuartel_oso;
                    e.botones.add(oso);
                } else if (SubRaceEnum.TORTUGA.equals(aliado.subRace)) {
                    BotonComplejo tortuga = new BotonComplejo("Tortuga");
                    tortuga.canBeUsed = Fenix.boton_cuartel_tortuga;
                    e.botones.add(tortuga);
                } else if (SubRaceEnum.CUERVO.equals(aliado.subRace)) {
                    BotonComplejo cuervo = new BotonComplejo("Cuervo");
                    cuervo.canBeUsed = Fenix.boton_cuartel_cuervo;
                    e.botones.add(cuervo);
                }
                break;
            case "Centro tecnológico":
                e.botones.add(new BotonComplejo(new Tecnologia("Rifles de asalto")));
                e.botones.add(new BotonComplejo(new Tecnologia("Clases de tiro")));
                e.botones.add(new BotonComplejo(new Tecnologia("Resurrección")));
                e.botones.add(new BotonComplejo(new Tecnologia("Destruir al invasor")));
                if (aliado.limite_cuarteles == 2) {
                    //Aumentar limite 1
                    e.botones.add(new BotonComplejo(new Tecnologia("Aumentar límite 1")));
                } else {
                    if (aliado.limite_cuarteles == 3) {
                        //Aumentar limite 2    
                        e.botones.add(new BotonComplejo(new Tecnologia("Aumentar límite 2")));
                    } else {
                        //Aumentar limite 3
                        e.botones.add(new BotonComplejo(new Tecnologia("Aumentar límite 3")));
                    }
                }
                break;
            case "Academia":
                e.botones.add(new BotonComplejo(new Tecnologia("Desbloquear Tigre")));
                e.botones.add(new BotonComplejo(new Tecnologia("Desbloquear Halcón")));
                e.botones.add(new BotonComplejo(new Tecnologia("Desbloquear Fénix")));
                if (SubRaceEnum.OSO.equals(aliado.subRace)) {
                    e.botones.add(new BotonComplejo(new Tecnologia("Desbloquear Oso")));
                } else if (SubRaceEnum.TORTUGA.equals(aliado.subRace)) {
                    e.botones.add(new BotonComplejo(new Tecnologia("Desbloquear Tortuga")));
                } else if (SubRaceEnum.CUERVO.equals(aliado.subRace)) {
                    e.botones.add(new BotonComplejo(new Tecnologia("Desbloquear Cuervo")));
                }
                break;
            case "Sede":
                e.botones.add(new BotonComplejo(new Unidad(aliado, "Constructor")));
                e.botones.add(new BotonComplejo(new Unidad(aliado, "Recolector")));
                break;
        }
    }

    public static void iniciar_botones_unidad(Jugador aliado, Unidad u) {
        switch (u.nombre) {
            case "Constructor":
                u.botones.add(new BotonComplejo(new Edificio(aliado, "Cuartel Fénix")));
                u.botones.add(new BotonComplejo(new Edificio(aliado, "Centro tecnológico")));
                u.botones.add(new BotonComplejo(new Edificio(aliado, "Academia")));
                break;
            case "Fénix":
                u.botones.add(new BotonComplejo("Resurrección"));
                u.botones.get(u.botones.size() - 1).canBeUsed = Unidad.activacion_resurreccion;
                break;
            case "Cuervo":
                u.botones.add(new BotonComplejo(new Habilidad("Motivar a las tropas")));
                u.botones.add(new BotonComplejo(new Habilidad("Desmoralizar al enemigo")));
                u.botones.add(new BotonComplejo(new Habilidad("Maniobra desesperada")));
                break;
        }
    }

    public static void unidad(Jugador aliado, Unidad u) {
        switch (u.nombre) {
            //Fenix
            case "Recolector":
                Fenix.Recolector(u);
                break;
            case "Constructor":
                Fenix.Constructor(u);
                break;
            case "Tigre":
                Fenix.Tigre(u);
                break;
            case "Halcón":
                Fenix.Halcon(u);
                break;
            case "Fénix":
                Fenix.Fenix(u);
                break;
            case "Oso":
                Fenix.Oso(u);
                break;
            case "Tortuga":
                Fenix.Tortuga(u);
                break;
            case "Cuervo":
                Fenix.Cuervo(u);
                break;
        }
    }

    public static void edificio(Jugador aliado, Edificio e) {
        switch (e.nombre) {
            //Fénix
            case "Centro de restauración":
                Fenix.Centro(e);
                break;
            case "Cuartel Fénix":
                Fenix.CuartelFenix(e);
                break;
            case "Centro tecnológico":
                Fenix.CentroTecnologico(e);
                break;
            case "Academia":
                Fenix.Academia(e);
                break;
            case "Sede":
                Fenix.Sede(e);
                break;
        }
    }

}
