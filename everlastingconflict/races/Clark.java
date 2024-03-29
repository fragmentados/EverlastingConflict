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
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.SubRaceEnum;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


public class Clark {

    public static final int tiempo_mineria = 15;
    public static final int recursos_primarca = 100;
    public static int ataque_depredador = Unidad.ataque_estandar + 5;
    public static int ataque_destructor = Unidad.ataque_estandar + 40;
    public static int defensa_defensor = Unidad.defensa_estandar + 4;
    public static List<Fusion> fusiones = new ArrayList<>();

    public static int ataque_moldeador_cac = Unidad.ataque_estandar + 20;
    public static int ataque_moldeador_distancia = Unidad.ataque_estandar + 5;
    public static int alcance_moldeador_cac = Unidad.alcance_estandar - 50;
    public static int alcance_moldeador_distancia = Unidad.alcance_estandar + 50;

    public static Point2D.Float calcular_punto_medio(List<Unidad> unidades) {
        float x_contador = 0, y_contador = 0;
        for (Unidad u : unidades) {
            x_contador += u.x;
            y_contador += u.y;
        }
        x_contador = x_contador / unidades.size();
        y_contador = y_contador / unidades.size();

        return new Point2D.Float(x_contador, y_contador);
    }

    public static Unidad resolverFusion(Game p, Fusion f) {
        Unidad resultado = null;
        if (!f.contenido.isEmpty()) {
            int ndepredador = (int) f.contenido.stream().filter(u -> "Depredador".equals(u.nombre)).count();
            int ndevorador = (int) f.contenido.stream().filter(u -> "Devorador".equals(u.nombre)).count();
            int ncazador = (int) f.contenido.stream().filter(u -> "Cazador".equals(u.nombre)).count();
            float x = f.contenido.get(0).x, y = f.contenido.get(0).y;
            Jugador aliado = p.getPlayerFromElement(f.contenido.get(0));
            int tamano = f.contenido.size();
            switch (ndepredador) {
                case 0:
                    switch (ndevorador) {
                        case 0:
                            switch (ncazador) {
                                case 2:
                                    //Amaestrador
                                    resultado = new Unidad(aliado, "Amaestrador", x, y);
                                    break;
                                case 3:
                                    if (SubRaceEnum.RUMIANTES.equals(aliado.subRace)) {
                                        //Rumiante
                                        resultado = new Unidad(aliado, "Rumiante", x, y);
                                    }
                                    break;
                            }
                            break;
                        case 1:
                            switch (ncazador) {
                                case 1:
                                    //Inspirador
                                    resultado = new Unidad(aliado, "Inspirador", x, y);
                                    break;
                            }
                            break;
                        case 2:
                            //Escupidor
                            resultado = new Unidad(aliado, "Escupidor", x, y);
                            break;
                        case 3:
                            if (SubRaceEnum.REGURGITADORES.equals(aliado.subRace)) {
                                //Regurgitador
                                resultado = new Unidad(aliado, "Regurgitador", x, y);
                            }
                            break;
                    }
                    break;
                case 1:
                    switch (ndevorador) {
                        case 0:
                            switch (ncazador) {
                                case 1:
                                    //Defensor
                                    resultado = new Unidad(aliado, "Defensor", x, y);
                                    break;
                            }
                            break;
                        case 1:
                            switch (ncazador) {
                                case 0:
                                    //Moldeador
                                    resultado = new Unidad(aliado, "Moldeador", x, y);
                                    break;
                                case 1:
                                    //Matriarca
                                    resultado = new Unidad(aliado, "Matriarca", x, y);
                                    break;
                            }
                            break;
                    }
                    break;
                case 2:
                    switch (ndevorador) {
                        case 0:
                            switch (ncazador) {
                                case 0:
                                    //Desmembrador
                                    resultado = new Unidad(aliado, "Desmembrador", x, y);
                                    break;
                            }
                            break;
                    }
                    break;
                case 3:
                    switch (ndevorador) {
                        case 0:
                            switch (ncazador) {
                                case 0:
                                    if (SubRaceEnum.DESPEDAZADORES.equals(aliado.subRace)) {
                                        //Despedazador
                                        resultado = new Unidad(aliado, "Despedazador", x, y);
                                    }
                                    break;
                            }
                            break;
                    }
                    break;
            }
            if (resultado != null) {
                for (int i = 0; i < tamano; i++) {
                    f.contenido.get(0).destruir(p, null, false);
                    f.contenido.remove(0);
                }
                resultado.behaviour = BehaviourEnum.EMERGIENDO;
                resultado.vida = 0;
                aliado.unidades.add(resultado);
                aliado.unidades.get(aliado.unidades.size() - 1).iniciarbotones(p);
                aliado.unidades.get(aliado.unidades.size() - 1).seleccionar();
            }
        }
        return resultado;
    }

    public static void iniciarFusion(Game p, Fusion f) {
        if (f.contenido.size() > 1) {
            Point2D.Float punto = calcular_punto_medio(f.contenido);
            for (Unidad u : f.contenido) {
                u.mover(p, punto.x, punto.y);
            }
            Clark.fusiones.add(new Fusion(f.contenido));
        } else if (f.contenido.size() == 1) {
            Clark.resolverFusion(p, f);
        }
    }

    //Constructo CaC
    public static void Depredador(Jugador aliado, Unidad u) {
        u.ataque = Clark.ataque_depredador;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar - 90;
        u.cadencia = Unidad.cadencia_estandar - 0.2f;
        u.velocidad = Unidad.velocidad_estandar + 0.6f;
        u.vision = Unidad.vision_estandar + 100;
        u.tiempo = Unidad.tiempo_estandar - 8;
        u.coste = 100;
        if (aliado.isJuggernaut) {
            u.coste -= 10 * aliado.advantageMultiplier;
        }
        u.descripcion = "Constructor cuerpo a cuerpo.";
    }

    //Constructor A Distancia    
    public static void Devorador(Jugador aliado, Unidad u) {
        u.ataque = Unidad.ataque_estandar + 10;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar - 30;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar + 0.1f;
        u.velocidad = Unidad.velocidad_estandar - 0.2f;
        u.vision = Unidad.vision_estandar;
        u.tiempo = Unidad.tiempo_estandar - 8;
        u.coste = 100;
        if (aliado.isJuggernaut) {
            u.coste -= 10 * aliado.advantageMultiplier;
        }
        u.descripcion = "Constructor a distancia.";
    }

    //Constructor Apoyo
    public static void Cazador(Jugador aliado, Unidad u) {
        u.ataque = Unidad.ataque_estandar;
        u.defensa = Unidad.defensa_estandar + 1;
        u.vida_max = Unidad.vida_estandar - 30;
        u.alcance = Unidad.alcance_estandar - 50;
        u.cadencia = Unidad.cadencia_estandar + 0.2f;
        u.velocidad = Unidad.velocidad_estandar + 0.2f;
        u.vision = Unidad.vision_estandar;
        u.tiempo = Unidad.tiempo_estandar - 8;
        u.coste = 100;
        if (aliado.isJuggernaut) {
            u.coste -= 10 * aliado.advantageMultiplier;
        }
        u.descripcion = "Constructor de apoyo.";
    }

    //CaC+Distancia
    public static void Moldeador(Unidad u) {
        u.ataque = Clark.ataque_moldeador_cac;
        u.defensa = Unidad.defensa_estandar + 2;
        u.vida_max = Unidad.vida_estandar + 50;
        u.alcance = Clark.alcance_moldeador_cac;
        u.cadencia = Unidad.cadencia_estandar + 0.5f;
        u.velocidad = Unidad.velocidad_estandar;
        u.vision = Unidad.vision_estandar;
        u.tiempo = Unidad.tiempo_estandar - 8;
        u.descripcion = "Unidad capaz de cambiar entre cuerpo a cuerpo y distancia.";
    }

    //CaC+Apoyo
    public static void Defensor(Unidad u) {
        u.ataque = 0;
        u.defensa = Clark.defensa_defensor;
        u.vida_max = Unidad.vida_estandar + 200;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar + 0.4f;
        u.velocidad = Unidad.velocidad_estandar + 0.1f;
        u.vision = Unidad.vision_estandar + 100;
        u.tiempo = Unidad.tiempo_estandar - 8;
        u.descripcion = "Unidad sin capacidad ofensiva utilizada para defender a otras unidades";
    }

    //Distancia+Apoyo
    public static void Inspirador(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 20;
        u.defensa = Unidad.defensa_estandar + 2;
        u.vida_max = Unidad.vida_estandar + 50;
        u.alcance = Unidad.alcance_estandar - 50;
        u.cadencia = Unidad.cadencia_estandar + 0.4f;
        u.velocidad = Unidad.velocidad_estandar;
        u.vision = Unidad.vision_estandar;
        u.tiempo = Unidad.tiempo_estandar - 8;
        u.descripcion = "Unidad con habilidades para potenciar otras uniadades aliadas.";
    }

    //Cac+CaC
    public static void Desmembrador(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 60;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.MELEE_RANGE;
        u.cadencia = Unidad.cadencia_estandar - 0.5f;
        u.velocidad = Unidad.velocidad_estandar + 0.1f;
        u.vision = Unidad.vision_estandar + 100;
        u.tiempo = Unidad.tiempo_estandar - 8;
        u.descripcion = "Unidad con gran capacidad ofensiva a poco alcance.";
    }

    //Cac+CaC+CaC
    public static void Despedazador(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 90;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.MELEE_RANGE;
        u.cadencia = Unidad.cadencia_estandar - 0.5f;
        u.velocidad = Unidad.velocidad_estandar + 2f;
        u.vision = Unidad.vision_estandar;
        u.tiempo = Unidad.tiempo_estandar - 8;
        u.descripcion = "Unidad con inmensa capacidad ofensiva a poco alcance. Puede cegar al enemigo";
    }

    //Distancia+Distancia
    public static void Escupidor(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 60;
        u.area = Unidad.area_estandar;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar + 150;
        u.cadencia = Unidad.cadencia_estandar - 0.3f;
        u.velocidad = Unidad.velocidad_estandar + 0.1f;
        u.vision = Unidad.vision_estandar + 100;
        u.tiempo = Unidad.tiempo_estandar - 8;
        u.descripcion = "Unidad capaz de bombardear desde grandes distancias.";
    }

    //Distancia+Distancia+Distancia
    public static void Regurgitador(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 70;
        u.area = Unidad.area_estandar;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar + 200;
        u.cadencia = Unidad.cadencia_estandar - 0.5f;
        u.velocidad = Unidad.velocidad_estandar;
        u.vision = Unidad.vision_estandar + 200;
        u.tiempo = Unidad.tiempo_estandar - 8;
        u.descripcion = "Unidad a distancia definitiva con habilidad para aumentar drásticamente su velocidad de " +
                "ataque durante un tiempo.";
    }

    //Apoyo+Apoyo
    public static void Amaestrador(Unidad u) {
        u.ataque = 0;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar + 100;
        u.alcance = Unidad.alcance_estandar + 200;
        u.cadencia = 0;
        u.velocidad = Unidad.velocidad_estandar;
        u.vision = Unidad.vision_estandar + 100;
        u.tiempo = Unidad.tiempo_estandar - 8;
        u.descripcion = "Unidad sin capacidad ofensiva con grandes habilidades";
        u.hostil = false;
    }

    //Apoyo+Apoyo+Apoyo
    public static void Rumiante(Unidad u) {
        u.ataque = 0;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar + 100;
        u.alcance = Unidad.alcance_estandar + 200;
        u.cadencia = 0;
        u.velocidad = Unidad.velocidad_estandar;
        u.vision = Unidad.vision_estandar + 100;
        u.tiempo = Unidad.tiempo_estandar - 8;
        u.descripcion = "Unidad sin capacidad ofensiva con supremas habilidades";
        u.hostil = false;
    }

    //Cac+Distancia+Apoyo
    public static void Matriarca(Unidad u) {
        u.ataque = Unidad.ataque_estandar * 10;
        u.defensa = Unidad.defensa_estandar + 5;
        u.vida_max = Unidad.vida_estandar * 3;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar - 0.4f;
        u.velocidad = Unidad.velocidad_estandar;
        u.vision = Unidad.vision_estandar;
        u.tiempo = Unidad.tiempo_estandar - 8;
        u.descripcion = "Unidad final con gran vida y ataque.";
    }

    //Edificios
    public static void Primarca(Edificio e) {
        e.vida_max = 2000;
        e.vision = Unidad.vision_estandar + 200;
        e.tiempo = 3000;
        e.coste = 0;
        e.descripcion = "Edificio central capaz de crear constructores e investigar tecnologías.";
        e.main = true;
        e.unitCreator = true;
    }

    public static void iniciar_botones_edificio(Jugador aliado, Edificio e) {
        switch (e.nombre) {
            case "Primarca":
                e.botones.add(new BotonComplejo(new Unidad(aliado, "Depredador")));
                e.botones.add(new BotonComplejo(new Unidad(aliado, "Devorador")));
                e.botones.add(new BotonComplejo(new Unidad(aliado, "Cazador")));
                e.botones.add(new BotonComplejo(new Tecnologia("Asimilación mejorada")));
                e.botones.add(new BotonComplejo(new Tecnologia("Instintos primarios")));
                e.botones.add(new BotonComplejo(new Tecnologia("Bioacero reforzado")));
                e.botones.add(new BotonComplejo("Ayuda Fusión"));
                break;
        }
    }

    static void iniciar_botones_unidad(Unidad u) {
        switch (u.nombre) {
            case "Depredador":
            case "Devorador":
                u.botones.add(new BotonComplejo("Fusion"));
                break;
            case "Cazador":
                u.botones.add(new BotonComplejo("Fusion"));
                u.botones.add(new BotonComplejo(new Habilidad("Eliminación")));
                break;
            case "Moldeador":
                u.botones.add(new BotonComplejo(new Habilidad("Cambiar modo")));
                break;
            case "Amaestrador":
                u.botones.add(new BotonComplejo(new Habilidad("Domesticar")));
                u.botones.add(new BotonComplejo(new Habilidad("Redención")));
                u.botones.add(new BotonComplejo(new Habilidad("Inmolación")));
                break;
            case "Defensor":
                u.botones.add(new BotonComplejo(new Habilidad("Provocar")));
                break;
            case "Inspirador":
                u.botones.add(new BotonComplejo(new Habilidad("Alentar")));
                break;
            case "Despedazador":
                u.botones.add(new BotonComplejo(new Habilidad("Cegar a la presa")));
                break;
            case "Regurgitador":
                u.botones.add(new BotonComplejo(new Habilidad("Modo supremo")));
                break;
            case "Rumiante":
                u.botones.add(new BotonComplejo(new Habilidad("Domesticacion experta")));
                break;
        }
    }

    public static void unidad(Jugador aliado, Unidad u) {
        switch (u.nombre) {
            case "Depredador":
                Clark.Depredador(aliado, u);
                break;
            case "Regurgitador":
                Clark.Regurgitador(u);
                break;
            case "Devorador":
                Clark.Devorador(aliado, u);
                break;
            case "Cazador":
                Clark.Cazador(aliado, u);
                break;
            case "Moldeador":
                Clark.Moldeador(u);
                break;
            case "Defensor":
                Clark.Defensor(u);
                break;
            case "Inspirador":
                Clark.Inspirador(u);
                break;
            case "Desmembrador":
                Clark.Desmembrador(u);
                break;
            case "Despedazador":
                Clark.Despedazador(u);
                break;
            case "Escupidor":
                Clark.Escupidor(u);
                break;
            case "Amaestrador":
                Clark.Amaestrador(u);
                break;
            case "Matriarca":
                Clark.Matriarca(u);
                break;
            case "Rumiante":
                Clark.Rumiante(u);
                break;
        }
    }

    public static void edificio(Jugador aliado, Edificio e) {
        switch (e.nombre) {
            //Clark
            case "Primarca":
                Clark.Primarca(e);
                break;
        }
    }
}
