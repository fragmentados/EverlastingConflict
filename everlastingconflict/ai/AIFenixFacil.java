/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.ai;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Recurso;
import everlastingconflict.elements.impl.Tecnologia;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Game;
import everlastingconflict.races.Fenix;
import everlastingconflict.races.enums.SubRaceEnum;
import everlastingconflict.watches.Reloj;

import static everlastingconflict.races.enums.RaceEnum.FENIX;


public class AIFenixFacil extends AI {

    public float xcuartel, ycuartel;
    public float xacademia, yacademia;
    public float xcentro, ycentro;

    public AIFenixFacil(SubRaceEnum subRaceEnum, Integer t, boolean isLeader, boolean isJuggernaut) {
        super("AIFenix", FENIX, subRaceEnum, t, isLeader, isJuggernaut);

    }

    @Override
    public float getDelay() {
        return 10f;
    }

    @Override
    public void initElements(Game p) {
        super.initElements(p);
        xcuartel = this.horizontalOffset(x_inicial, 210);
        ycuartel = y_inicial;

        xacademia = x_inicial;
        yacademia = this.verticalOffset(y_inicial, 210);

        xcentro = this.horizontalOffset(x_inicial, 210);
        ycentro = this.verticalOffset(y_inicial, 210);

        npushear = 10;
    }

    @Override
    public void decisiones_unidades(Game p, int delta) {
        for (Unidad u : unidades) {
            if (u.delay > 0) {
                u.delay -= delta * Reloj.TIME_REGULAR_SPEED;
            } else {
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
    }

    @Override
    public void decisiones_edificios(Game p, int delta) {
        for (Edificio e : edificios) {
            if (e.delay > 0) {
                e.delay -= delta * Reloj.TIME_REGULAR_SPEED;
            } else {
                if (!BehaviourEnum.CONSTRUYENDOSE.equals(e.behaviour)) {
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
        }
    }

    public void comportamiento_constructor(Game p, Unidad u) {
        if (u.behaviour.equals(BehaviourEnum.PARADO)) {
            if (this.cantidad_edificio("Cuartel Fénix") < this.limite_cuarteles) {
                Edificio contador = new Edificio(this, "Cuartel Fénix");
                contador.vida = 0;
                u.construir(p, contador, xcuartel, ycuartel);
                xcuartel = this.horizontalOffset(xcuartel, contador.anchura + 10);
            } else {
                if (this.cantidad_edificio("Academia") < 1) {
                    Edificio contador = new Edificio(this, "Academia");
                    contador.vida = 0;
                    u.construir(p, contador, xacademia, yacademia);
                } else {
                    if (this.cantidad_edificio("Centro tecnológico") < 1) {
                        Edificio contador = new Edificio(this, "Centro tecnológico");
                        contador.vida = 0;
                        u.construir(p, contador, xcentro, ycentro);
                    }
                }
            }
        }
    }

    public void comportamiento_recolector(Game p, Unidad u) {
        if (u.behaviour.equals(BehaviourEnum.PARADO)) {
            Recurso r = p.closestResource(null, this.nombre, "Civiles", u.x, u.y);
            if (r != null) {
                u.recolectar(p, r);
            } else {
                if (u.x != this.x_inicial - 300 || u.y != this.y_inicial) {
                    u.mover(p, this.x_inicial - 300, this.y_inicial);
                }
            }
        }
    }

    public void comportamiento_cuartel(Game p, Edificio e) {
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

    public void comportamiento_academia(Game p, Edificio e) {
        if (e.cola_construccion.isEmpty()) {
            for (BotonComplejo b : e.botones) {
                Tecnologia t = new Tecnologia(b.elemento_nombre);
                e.researchTechnology(p, this, t);
                break;
            }
        }
    }

    public void comportamiento_sede(Game p, Edificio e) {
        if (e.cola_construccion.isEmpty()) {
            for (BotonComplejo b : e.botones) {
                if (b.elemento_nombre.equals("Recolector")) {
                    if (cantidad_unidad("Recolector") < 3) {
                        Unidad u = new Unidad(this, b.elemento_nombre);
                        e.createUnit(p, this, u);
                        break;
                    }
                }
                if (b.elemento_nombre.equals("Constructor")) {
                    if (cantidad_unidad("Constructor") < 2) {
                        Unidad u = new Unidad(this, b.elemento_nombre);
                        e.createUnit(p, this, u);
                        break;
                    }
                }
            }
        }
    }

}
