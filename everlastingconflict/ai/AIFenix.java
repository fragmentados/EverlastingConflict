/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.ai;

import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.Fenix;
import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Recurso;
import everlastingconflict.elementos.implementacion.Tecnologia;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.elementosvisuales.BotonComplejo;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Elías
 */
public class AIFenix extends AI {

    public float xcuartel, ycuartel;
    public float xacademia, yacademia;
    public float xcentro, ycentro;

    public AIFenix() {
        super("AIFenix", "Fénix");

    }

    @Override
    public final void iniciar_elementos(Partida p) {
        super.iniciar_elementos(p);
        xcuartel = x_inicial - 210;
        ycuartel = y_inicial;

        xacademia = x_inicial;
        yacademia = y_inicial + 210;

        xcentro = x_inicial + 210;
        ycentro = y_inicial;

        npushear = 10;
    }

    @Override
    public void comportamiento_unidades(Partida p, Graphics g, int delta) {
        super.comportamiento_unidades(p, g, delta);
        pushear(p);
        for (Unidad u : unidades) {
            switch (u.nombre) {
                case "Recolector":
                    comportamiento_recolector(p, u);
                    break;
                case "Constructor":
                    comportamiento_constructor(p, u);
                    break;
                default:
                //Unidad Militar
                //comportamiento_militar(p, u);
            }
        }
    }

    @Override
    public void comportamiento_edificios(Partida p, Graphics g, int delta) {
        super.comportamiento_edificios(p, g, delta);
        for (Edificio e : edificios) {
            switch (e.nombre) {
                case "Cuartel Fénix":
                    comportamiento_cuartel(p, e);
                    break;
                case "Academia":
                case "Centro tecnológico":
                    comportamiento_academia(p, e);
                    break;
                case "Sede":
                    comportamiento_sede(p, e);
                    break;

            }
        }
    }

    public void comportamiento_constructor(Partida p, Unidad u) {
        if (u.statusBehaviour.equals(StatusBehaviour.PARADO)) {
            if (this.cantidad_edificio("Cuartel Fénix") < Fenix.limite_cuarteles) {
                Edificio contador = new Edificio("Cuartel Fénix");
                contador.vida = 0;
                u.construir(p, contador, xcuartel, ycuartel);
                xcuartel -= contador.anchura + 10;
            } else {
                if (this.cantidad_edificio("Academia") < 1) {
                    Edificio contador = new Edificio("Academia");
                    contador.vida = 0;
                    u.construir(p, contador, xacademia, yacademia);
                } else {
                    if (this.cantidad_edificio("Centro tecnológico") < 1) {
                        Edificio contador = new Edificio("Centro tecnológico");
                        contador.vida = 0;
                        u.construir(p, contador, xcentro, ycentro);
                    }
                }
            }
        }
    }

    public void comportamiento_recolector(Partida p, Unidad u) {
        if (u.statusBehaviour.equals(StatusBehaviour.PARADO)) {
            Recurso r = p.recurso_mas_cercano(null, this.nombre, "Civiles", u.x, u.y);
            if (r != null) {
                u.recolectar(p, r);
            } else {
                if (u.x != this.x_inicial - 300 || u.y != this.y_inicial) {
                    u.mover(p, this.x_inicial - 300, this.y_inicial);
                }
            }
        }
    }

    public void comportamiento_cuartel(Partida p, Edificio e) {
        if (e.unidad_actual != null) {
            if (e.unidad_actual.equals("Tigre") && Fenix.boton_cuartel_halcon) {
                for (BotonComplejo b : e.botones) {
                    if (b.texto.equals("Halcón")) {
                        b.resolucion(null, e, p);
                        break;
                    }
                }
            } else {
                if (e.unidad_actual.equals("Halcón") && Fenix.boton_cuartel_fenix) {
                    for (BotonComplejo b : e.botones) {
                        if (b.texto.equals("Fénix")) {
                            b.resolucion(null, e, p);
                            break;
                        }
                    }
                }
            }
        } else {
            if (Fenix.boton_cuartel_tigre) {
                for (BotonComplejo b : e.botones) {
                    if (b.texto.equals("Tigre")) {
                        b.resolucion(null, e, p);
                        break;
                    }
                }
            }
        }
    }

    public void comportamiento_academia(Partida p, Edificio e) {
        if (e.cola_construccion.isEmpty()) {
            for (BotonComplejo b : e.botones) {
                Tecnologia t = new Tecnologia(b.elemento_nombre);
                e.investigar_tecnologia(p, this, t);
                break;
            }
        }
    }

    public void comportamiento_sede(Partida p, Edificio e) {
        if (e.cola_construccion.isEmpty()) {
            for (BotonComplejo b : e.botones) {
                if (b.elemento_nombre.equals("Recolector")) {
                    if (cantidad_unidad("Recolector") < 3) {
                        Unidad u = new Unidad(b.elemento_nombre);
                        e.createUnit(p, this, u);
                        break;
                    }
                }
                if (b.elemento_nombre.equals("Constructor")) {
                    if (cantidad_unidad("Constructor") < 2) {
                        Unidad u = new Unidad(b.elemento_nombre);
                        e.createUnit(p, this, u);
                        break;
                    }
                }
            }
        }
    }

}
