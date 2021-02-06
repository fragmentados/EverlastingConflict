/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.razas;

import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Tecnologia;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.elementosvisuales.BotonComplejo;

import java.util.logging.Level;
import java.util.logging.Logger;

import everlastingconflict.gestion.Jugador;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Elías
 */
public class Fenix {

    public static String nombre_raza = "Fénix";
    public static int ataque_tigre = Unidad.ataque_estandar;
    public static int alcance_halcon = Unidad.alcance_estandar + 100;
    public static int limite_cuarteles = 2;
    public static int limite_unidades_no_militares = 5;
    public static boolean boton_cuartel_fenix = false;
    public static boolean boton_cuartel_tigre = false;
    public static boolean boton_cuartel_halcon = false;

    public Fenix() {

    }

    //Unidades Prueba
    public static final void Soldado(Unidad u) {
        u.ataque = Unidad.ataque_estandar;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar;
        u.coste = 10;
        u.tiempo = Unidad.tiempo_estandar;
        u.vision = Unidad.vision_estandar;
        try {
            u.arriba = new Animation(new Image[]{new Image("media/Unidades/SoldadoArr1.png"), new Image("media/Unidades/SoldadoArr2.png")}, new int[]{300, 300}, false);
            u.abajo = new Animation(new Image[]{new Image("media/Unidades/SoldadoAba1.png"), new Image("media/Unidades/SoldadoAba2.png")}, new int[]{300, 300}, false);
            u.izquierda = new Animation(new Image[]{new Image("media/Unidades/SoldadoIzq1.png"), new Image("media/Unidades/SoldadoIzq2.png")}, new int[]{300, 300}, false);
            u.derecha = new Animation(new Image[]{new Image("media/Unidades/SoldadoDer1.png"), new Image("media/Unidades/SoldadoDer2.png")}, new int[]{300, 300}, false);
        } catch (SlickException ex) {
            Logger.getLogger(Fenix.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static final void Sniper(Unidad u) {
        u.ataque = 20;
        u.vida_max = 70;
        u.alcance = 200;
        u.cadencia = 230;
        u.velocidad = Unidad.velocidad_estandar;
        u.coste = 20;
        u.tiempo = 1500;
        u.vision = Unidad.vision_estandar - 100;
    }

    public static final void Minero(Unidad u) {
        u.ataque = 0;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar;
        u.coste = 10;
        u.tiempo = Unidad.tiempo_estandar;
        u.vision = Unidad.vision_estandar - 100;
        u.constructor = true;
    }

    public static final void Recolector(Unidad u) {
        u.ataque = 0;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar + 0.1f;
        u.coste = 0;
        u.tiempo = Unidad.tiempo_estandar + 2;
        u.vision = Unidad.vision_estandar - 100;
        u.constructor = true;
        u.hostil = false;
        u.descripcion = "Unidad encargada de influir en las ciudades civiles con el fin de que se unan a la causa Fénix.";
    }

    public static final void Constructor(Unidad u) {
        u.ataque = 0;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar;
        u.coste = 0;
        u.tiempo = Unidad.tiempo_estandar + 2;
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
        u.descripcion = "Unidad final con gran capacidad defensiva. Es capaz de volver a la vida tras recibir grandes daños.";
    }

    public static final void Oso(Unidad u) {

    }

    public static final void Avispa(Unidad u) {

    }

    public static final void Cuervo(Unidad u) {

    }

    //Edificios
    public static final void Cuartel(Edificio e) {
        e.coste = 100;
        e.vida_max = 1000;
        e.vision = Unidad.vision_estandar + 100;
        e.tiempo = 1500;
    }

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
    }

    public static final void CuartelFenix(Edificio e) {
        e.coste = 10;
        e.vida_max = Edificio.vida_estandar;
        e.vision = Edificio.vision_estandar;
        e.tiempo = Edificio.tiempo_estandar;
        e.estado = "Construyendo";
        e.descripcion = "Edificio encargado de la producción de las unidades Fénix.";
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

    public static void iniciar_botones_edificio(Edificio e) {
        switch (e.nombre) {
            case "Cuartel":
                e.botones.add(new BotonComplejo(new Unidad("Soldado")));
                e.botones.add(new BotonComplejo(new Unidad("Minero")));
                e.botones.add(new BotonComplejo(new Unidad("Sniper")));
                e.botones.add(new BotonComplejo(new Unidad("Fénix")));
                break;
            case "Cuartel Fénix":
                e.botones.add(new BotonComplejo("Detener"));
                e.botones.add(new BotonComplejo("Tigre"));
                e.botones.add(new BotonComplejo("Halcón"));
                e.botones.add(new BotonComplejo("Fénix"));
                e.botones.get(1).activado = Fenix.boton_cuartel_tigre;
                e.botones.get(2).activado = Fenix.boton_cuartel_halcon;
                e.botones.get(3).activado = Fenix.boton_cuartel_fenix;
                break;
            case "Centro tecnológico":
                e.botones.add(new BotonComplejo(new Tecnologia("Rifles de asalto")));
                e.botones.add(new BotonComplejo(new Tecnologia("Clases de tiro")));
                e.botones.add(new BotonComplejo(new Tecnologia("Resurrección")));
                e.botones.add(new BotonComplejo(new Tecnologia("Destruir al invasor")));
                if (Fenix.limite_cuarteles == 2) {
                    //Aumentar limite 1
                    e.botones.add(new BotonComplejo(new Tecnologia("Aumentar límite 1")));
                } else {
                    if (Fenix.limite_cuarteles == 3) {
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
                break;
            case "Sede":
                e.botones.add(new BotonComplejo(new Unidad("Constructor")));
                e.botones.add(new BotonComplejo(new Unidad("Recolector")));
                break;            
        }
    }

    public static void iniciar_botones_unidad(Unidad u) {
        switch (u.nombre) {
            case "Constructor":
                u.botones.add(new BotonComplejo(new Edificio("Cuartel Fénix")));
                u.botones.add(new BotonComplejo(new Edificio("Centro tecnológico")));
                u.botones.add(new BotonComplejo(new Edificio("Academia")));
                break;
            case "Fénix":
                u.botones.add(new BotonComplejo("Resurrección"));
                u.botones.get(u.botones.size() - 1).activado = Unidad.activacion_resurreccion;
                break;
        }
    }

    public static void checkButtonResources(Jugador jugadorFenix) {
        if (jugadorFenix != null && jugadorFenix.recursos < 100) {
            for (Edificio e : jugadorFenix.edificios) {
                for (BotonComplejo b : e.botones) {
                    if (b.elemento_coste > 0) {
                        b.activado = checkResources(jugadorFenix.recursos, b.elemento_coste);
                    }
                }
            }
            for (Unidad u : jugadorFenix.unidades) {
                for (BotonComplejo b : u.botones) {
                    if ("Cuartel Fénix".equals(b.elemento_nombre)) {
                        b.activado = jugadorFenix.cantidad_edificio(b.elemento_nombre) < Fenix.limite_cuarteles;
                    } else if (b.elemento_coste > 0) {
                        b.activado = checkResources(jugadorFenix.recursos, b.elemento_coste);
                    }
                }
            }
        }
    }

    public static boolean checkResources(Float resources, Float cost) {
        return resources >= cost;
    }
}
