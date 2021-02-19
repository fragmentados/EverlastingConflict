/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos.implementacion;

import everlastingconflict.elementos.ElementoSimple;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.gestion.Evento;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.Clark;
import everlastingconflict.razas.Eternium;
import everlastingconflict.razas.Fenix;
import everlastingconflict.razas.Guardianes;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Elías
 */
public class Tecnologia extends ElementoSimple {

    public Tecnologia(String n) {
        nombre = n;
        descripcion = "";
        iniciar_coste();
        iniciarimagenes();
    }

    public final void iniciarimagenes() {
        try {
            sprite = new Animation(new Image[]{new Image("media/Tecnologías/" + nombre + ".png")}, new int[]{300}, false);
            icono = new Image("media/Tecnologías/" + nombre + ".png");
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public final void iniciar_coste() {
        switch (nombre) {
            //Fénix
            case "Desbloquear Tigre":
                coste = 10;
                tiempo = 10;
                descripcion = "Desbloquea a la unidad Tigre para construirla en el Cuartel.";
                break;
            case "Desbloquear Halcón":
                coste = 20;
                tiempo = 15;
                descripcion = "Desbloquea a la unidad Halcón para construirla en el Cuartel.";
                break;
            case "Desbloquear Fénix":
                coste = 30;
                tiempo = 20;
                descripcion = "Desbloquea a la unidad Fénix para construirla en el Cuartel.";
                break;
            case "Rifles de asalto":
                coste = 20;
                tiempo = 10;
                descripcion = "Aumenta el alcance de los Halcones ligeramente.";
                break;
            case "Clases de tiro":
                coste = 40;
                tiempo = 15;
                descripcion = "Aumenta el ataque de los Tigres ligeramente.";
                break;
            case "Resurrección":
                coste = 50;
                tiempo = 20;
                descripcion = "Permite a los Fénix utilizar la habilidad de Resurrección para volver a la vida.";
                break;
            case "Destruir al invasor":
                coste = 100;
                tiempo = 20;
                descripcion = "Aumenta el ataque y la defensa de todas las unidades militares Fénix.";
                break;
            //Eternium
            case "Catalización extrema":
                coste = 100;
                tiempo = 10;
                descripcion = "Mejora la recolección de hierro de las Refinerías.";
                break;
            case "Arco de plasma":
                coste = 75;
                tiempo = 10;
                descripcion = "Aumenta el ataque de los Guerreros ligeramente y su alcance considerablemente.";
                break;
            case "Espada desintegradora":
                coste = 100;
                tiempo = 10;
                descripcion = "Aumenta drásticamente el ataque de los Guerreros.";
                break;
            case "Lanza de Longinus":
                coste = 125;
                tiempo = 15;
                descripcion = "Aumenta drásticamente el ataque de los Ancestros.";
                break;
            case "Aumentar límite 1":
                coste = 20;
                tiempo = 15;
                descripcion = "Aumenta en uno el número de Cuarteles que puedes construir.";
                break;
            case "Aumentar límite 2":
                coste = 30;
                tiempo = 20;
                descripcion = "Aumenta en uno el número de Cuarteles que puedes construir.";
                break;
            case "Aumentar límite 3":
                coste = 50;
                tiempo = 35;
                descripcion = "Aumenta en dos el número de Cuarteles que puedes construir.";
                break;
            //Clark
            case "Asimilación mejorada":
                coste = 200;
                tiempo = 20;
                descripcion = "Aumenta la recompensa de todas las bestias.";
                break;
            case "Instintos primarios":
                coste = 200;
                tiempo = 20;
                descripcion = "Aumenta ligeramente el ataque del depredador y del destructor.";
                break;
            case "Bioacero reforzado":
                coste = 150;
                tiempo = 15;
                descripcion = "Aumenta drásticamente la defensa del defensor.";
                break;
            //Guardianes
            case "Reforma gubernamental":
                coste = 100;
                tiempo = 10;
                descripcion = "Aumenta la felicidad de la población por cada evento positivo adquirido.";
                guardiansThreatLevelNeeded = 2;
                break;
            case "Ruedas mejoradas":
                coste = 150;
                tiempo = 20;
                descripcion = "Aumenta la velocidad de los vehículos.";
                guardiansThreatLevelNeeded = 2;
                break;
            case "Propaganda positiva":
                coste = 200;
                tiempo = 20;
                descripcion = "Aumenta el máximo de felicidad de la población hasta 200%";
                guardiansThreatLevelNeeded = 2;
                break;
            case "Cobradores de impuestos":
                coste = 100;
                tiempo = 20;
                descripcion = "Aumenta los recursos obtenidos por segundo pero disminuye la felicidad de la población.";
                break;
            case "Aumentar nivel de amenaza1":
                coste = 150;
                tiempo = 30;
                descripcion = "Aumenta el nivel de amenaza en 1.";
                break;
            case "Aumentar nivel de amenaza2":
                coste = 300;
                tiempo = 35;
                descripcion = "Aumenta el nivel de amenaza en 1.";
                break;
            case "Intervención divina":
                coste = 100;
                tiempo = 20;
                descripcion = "Desbloquea la habilidad del Enviado celeste del mismo nombre.";
                guardiansThreatLevelNeeded = 3;
                break;
            case "Armas bíblicas":
                coste = 100;
                tiempo = 20;
                descripcion = "Aumenta el ataque de las unidades sagradas.";
                guardiansThreatLevelNeeded = 2;
                break;
            case "Ley marcial":
                coste = 100;
                tiempo = 30;
                descripcion = "Desactiva los eventos negativos permanentemente.";
                guardiansThreatLevelNeeded = 3;
                break;
        }
    }

    public void resolver_efecto(Partida partida, Jugador jugador) {
        switch (nombre) {
            //Eternium
            case "Catalización extrema":
                Edificio.recursos_refineria = 100;
                break;
            case "Arco de plasma":
                Eternium.ataque_guerrero += 10;
                for (Unidad u : jugador.unidades) {
                    if (u.nombre.equals("Guerrero")) {
                        u.ataque += 10;
                        u.alcance += 50;
                    }
                }
                //jugador.raza.unidades[1].habilidad.nombre = "Masoquismo";
                break;
            case "Espada desintegradoara":
                Eternium.ataque_guerrero += 30;
                for (Unidad u : jugador.unidades) {
                    if (u.nombre.equals("Guerrero")) {
                        u.ataque += 30;
                    }
                }
                break;
            case "Lanza de Longinus":
                Eternium.ataque_ancestro += 40;
                for (Unidad u : jugador.unidades) {
                    if (u.nombre.equals("Ancestro")) {
                        u.ataque += 40;
                    }
                }
                break;
//            case "Puños de hierro reforzado":
//                jugador.raza.unidades[4].ataque += 50;
//                break;
//            case "Agilidad de pensamiento":
//                jugador.raza.unidades[3].movimiento++;
//                break;
            //Clark
            case "Asimilación mejorada":
                for (Bestias be : partida.bestias) {
                    for (Bestia b : be.contenido) {
                        b.recompensa += 50;
                    }
                }
                break;
            case "Instintos primarios":
                Clark.ataque_depredador += 5;
                Clark.ataque_destructor += 10;
                for (Unidad u : jugador.unidades) {
                    if (u.nombre.equals("Depredador")) {
                        u.ataque += 5;
                    }
                    if (u.nombre.equals("Destructor")) {
                        u.ataque += 10;
                    }
                }
                break;
            case "Bioacero reforzado":
                Clark.defensa_defensor += 5;
                for (Unidad u : jugador.unidades) {
                    if (u.nombre.equals("Defensor")) {
                        u.defensa += 5;
                    }
                }
                break;
            case "Biofusión enriquecida":
                break;
            //Fénix   
            case "Desbloquear Tigre":
                Fenix.boton_cuartel_tigre = true;
                for (Edificio ed : jugador.edificios) {
                    if (ed.nombre.equals("Cuartel Fénix")) {
                        for (BotonComplejo b : ed.botones) {
                            if (b.texto.equals("Tigre")) {
                                b.canBeUsed = true;
                            }
                        }
                    }
                }
                break;
            case "Desbloquear Halcón":
                Fenix.boton_cuartel_halcon = true;
                for (Edificio ed : jugador.edificios) {
                    if (ed.nombre.equals("Cuartel Fénix")) {
                        for (BotonComplejo b : ed.botones) {
                            if (b.texto.equals("Halcón")) {
                                b.canBeUsed = true;
                            }
                        }
                    }
                }
                break;
            case "Desbloquear Fénix":
                Fenix.boton_cuartel_fenix = true;
                for (Edificio ed : jugador.edificios) {
                    if (ed.nombre.equals("Cuartel Fénix")) {
                        for (BotonComplejo b : ed.botones) {
                            if (b.texto.equals("Fénix")) {
                                b.canBeUsed = true;
                            }
                        }
                    }
                }
                break;
            case "Rifles de asalto":
                Fenix.alcance_halcon += 50;
                for (Unidad u : jugador.unidades) {
                    if (u.nombre.equals("Halcón")) {
                        u.alcance += 50;
                    }
                }
                break;
            case "Tecno-hierro endurecido":
                break;
            case "Clases de tiro":
                Fenix.ataque_tigre += 5;
                for (Unidad u : jugador.unidades) {
                    if (u.nombre.equals("Tigre")) {
                        u.ataque += 5;
                    }
                }
                break;
            case "Destruir al invasor":
                for (Unidad u : jugador.unidades) {
                    if (!u.nombre.equals("Constructor") && !u.nombre.equals("Recolector")) {
                        u.ataque += 5;
                        u.defensa++;
                    }
                }
                break;
            case "Resurrección":
                Unidad.activacion_resurreccion = true;
                for (Unidad u : jugador.unidades) {
                    if (u.nombre.equals("Fénix")) {
                        for (BotonComplejo b : u.botones) {
                            if (b.texto.equals("Resurrección")) {
                                b.canBeUsed = true;
                                break;
                            }
                        }
                    }
                }
                break;
            case "Aumentar límite 1":
                for (Edificio e : jugador.edificios) {
                    if (e.nombre.equals("Centro tecnológico")) {
                        e.botones.add(new BotonComplejo(new Tecnologia("Aumentar límite 2")));
                        e.initButtonKeys();
                    }
                }
                Fenix.limite_cuarteles++;
                break;
            case "Aumentar límite 2":
                for (Edificio e : jugador.edificios) {
                    if (e.nombre.equals("Centro tecnológico")) {
                        e.botones.add(new BotonComplejo(new Tecnologia("Aumentar límite 3")));
                        e.initButtonKeys();
                    }
                }
                Fenix.limite_cuarteles++;
                break;
            case "Aumentar límite 3":
                Fenix.limite_cuarteles += 2;
                break;
            //Guardianes
            case "Reforma gubernamental":
                int resultado = 0;
                for (Evento e : jugador.eventos.contenido) {
                    if (e.positivo) {
                        resultado += 15;
                    }
                }
                jugador.guardiansPeoplePercentage += resultado;
                break;
            case "Ruedas mejoradas":
                Guardianes.ruedas_mejoradas = true;
                for (Unidad u : jugador.unidades) {
                    if (u.vehiculo) {
                        u.velocidad += 0.3f;
                    }
                }
                break;
            case "Propaganda positiva":
                Guardianes.maximo_felicidad = 200;
                break;
            case "Cobradores de impuestos":
                Guardianes.recursos_por_segundo -= 5;
                jugador.guardiansPeoplePercentage -= 10;
                break;
            case "Aumentar nivel de amenaza1":
                for (Edificio e : jugador.edificios) {
                    if (e.nombre.equals("Edificio gubernamental")) {
                        e.botones.add(new BotonComplejo(new Tecnologia("Aumentar nivel de amenaza2")));
                        e.initButtonKeys();
                    }
                }
                jugador.guardiansThreatLevel++;
                break;
            case "Aumentar nivel de amenaza2":
                jugador.guardiansThreatLevel++;
                break;
            case "Intervención divina":
                Guardianes.habilidad_enviado = true;
                for (Unidad u : jugador.unidades) {
                    if (u.nombre.equals("Enviado celeste")) {
                        for (BotonComplejo b : u.botones) {
                            if (b.elemento_nombre != null && b.elemento_nombre.equals("Intervención divina")) {
                                b.canBeUsed = true;
                            }
                        }
                    }
                }
                break;
            case "Armas bíblicas":
                for (Unidad u : jugador.unidades) {
                    if (u.nombre.equals("Pacificador") || u.nombre.equals("Silenciador") || u.nombre.equals("Inquisidor") || u.nombre.equals("Enviado celeste")) {
                        u.ataque += 2;
                    }
                }
                Guardianes.armas_biblicas = true;
                break;
            case "Ley marcial":
                jugador.eventos.desactivacion_permanente();
                jugador.disminuir_porcentaje(20);
                break;
//                case "Ecología":
//                    if (jugador.restauracion_int == 3) {
//                        jugador.restauracion_int = 2;
//                    }
//                    break;
            //Guardianes de la Paz
//            case "Pacto de beneficio mutuo":
//                for (int i = 0; i < jugador.raza.unidades.length; i++) {
//                    jugador.raza.unidades[i].defensa++;
//                }
//                break;
//            case "Mantener el orden":
//                jugador.aumentar_porcentaje(20);
//                break;
//            case "Conversión":
//                for (int i = 0; i < jugador.raza.unidades.length; i++) {
//                    jugador.raza.unidades[i].vida += 10;
//                    jugador.raza.unidades[i].vidatotal += 10 * jugador.raza.unidades[i].vida;
//                }
//                break;
        }

    }
}
