/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.gestion;

import everlastingconflict.razas.Eternium;
import everlastingconflict.razas.Fenix;
import everlastingconflict.razas.Guardianes;
import everlastingconflict.razas.Maestros;
import everlastingconflict.relojes.Reloj;
import everlastingconflict.elementos.ElementoAtacante;
import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.ElementoSimple;
import everlastingconflict.elementos.implementacion.ElementoEspecial;
import everlastingconflict.elementos.implementacion.Manipulador;
import everlastingconflict.elementos.implementacion.Recurso;
import everlastingconflict.elementos.implementacion.Taller;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.mapas.MapaCampo;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 *
 * @author Elías
 */
public class Jugador {

    //Datos basicos
    public float x_inicial, y_inicial;
    public String nombre, raza;
    public Color color;
    //RTS.Elementos
    public List<Unidad> unidades;
    public List<Edificio> edificios;
    public List<Recurso> lista_recursos;
    public List<Vision> visiones;
    public List<ElementoEspecial> elementos_especiales;           
    //Recursos
    public float recursos;
    public float recursos_alternativos;
    public float recursos_alternativos_dos;
    public int poblacion, poblacion_max;
    public boolean perforacion = false;
    public Eventos eventos; 

    public final int obtener_recursos() {
        switch (raza) {
            case "Fénix":
                return 10;
            case "Eternium":
                return 500;
            case "Clark":
                return 300;
            case "Maestros":
                return 0;
            case "Guardianes":
                eventos = new Eventos();
                recursos_alternativos = 50;
                recursos_alternativos_dos = 1;
                return 50;
        }
        //Nunca se debería llegar
        return -1;
    }

    public Jugador() {

    }

    public Jugador(String n, String r) {
        nombre = n;
        raza = r;
        unidades = new ArrayList<>();
        edificios = new ArrayList<>();
        lista_recursos = new ArrayList<>();
        visiones = new ArrayList<>();
        elementos_especiales = new ArrayList<>();
        recursos = obtener_recursos();
        poblacion = 0;
        poblacion_max = 200;
    }

    public void comprobacion_perforacion() {
        List<String> resultado = new ArrayList<>();
        for (Edificio e : edificios) {
            if ((e.nombre.equals("Cámara de asimilación") || (e.nombre.equals("Teletransportador")))) {
                if (resultado.indexOf(e) == -1) {
                    resultado.add(e.nombre);
                }
            }
        }
        perforacion = (resultado.size() == 2);
    }

    public boolean comprobacion_recursos_guardianes(ElementoSimple e) {
        if (this.recursos_alternativos_dos >= e.coste_alternativo) {
            if (recursos >= e.coste) {
                recursos -= e.coste;
                return true;
            }
        }
        return false;
    }

    public void resta_recursos(int coste) {
        if (!raza.equals(Fenix.nombre_raza)) {
            recursos -= coste;
        }
    }

    public boolean comprobacion_recursos(ElementoSimple e) {
        switch (raza) {
            case "Guardianes":
                return comprobacion_recursos_guardianes(e);
            default:
                if (recursos >= e.coste) {                    
                    return true;
                } else {
                    return false;
                }
        }
    }

    public void iniciar_elementos(Partida p) {
        switch (raza) {
            case "Fénix":
                edificios.add(new Edificio("Sede", x_inicial, y_inicial));
                unidades.add(new Unidad("Constructor", x_inicial, y_inicial + 150));
                unidades.add(new Unidad("Recolector", x_inicial + 50, y_inicial + 150));
                break;
            case "Eternium":
                edificios.add(new Edificio("Mando Central", x_inicial, y_inicial));
                unidades.add(new Unidad("Adepto", x_inicial + 50, y_inicial + 150));
                break;
            case "Clark":
                edificios.add(new Edificio("Primarca", x_inicial, y_inicial));
                unidades.add(new Unidad("Depredador", x_inicial - 50, y_inicial + 150));
                unidades.add(new Unidad("Devorador", x_inicial + 10, y_inicial + 150));
                unidades.add(new Unidad("Cazador", x_inicial + 70, y_inicial + 150));
                break;
            case "Maestros":
                unidades.add(new Manipulador(x_inicial, y_inicial));
                break;
            case "Guardianes":
                edificios.add(new Edificio("Ayuntamiento", x_inicial, y_inicial));
                edificios.add(new Edificio("Academia de pilotos", x_inicial + 250, y_inicial));
                edificios.add(new Edificio("Templo", x_inicial + 400, y_inicial));
                edificios.add(new Edificio("Vaticano", x_inicial + 550, y_inicial));
                edificios.add(new Edificio("Edificio gubernamental", x_inicial + 700, y_inicial));
                edificios.add(new Edificio("Laboratorio de I+D", x_inicial + 450, y_inicial + 250));
                edificios.add(new Taller("Taller bélico", x_inicial + 300, y_inicial + 250));
                edificios.add(new Taller("Taller bélico", x_inicial + 300, y_inicial + 400));
                break;
        }
        for (Unidad u : unidades) {
            this.poblacion += u.coste_poblacion;
            u.iniciarbotones(p);
        }
        for (Edificio e : edificios) {
            if (!this.raza.equals(Guardianes.nombre_raza) || e.nombre.equals("Ayuntamiento")) {
                e.iniciarbotones(p);
            }
        }
    }

    public void comportamiento_elementos(Partida p, Graphics g, int delta) {
        if (this.raza.equals(Guardianes.nombre_raza)) {
            this.recursos += (this.recursos_alternativos / 100f) * delta * Reloj.velocidad_reloj * Guardianes.recursos_por_segundo;
            eventos.comportamiento(this, delta);
        }
        comportamiento_unidades(p, g, delta);
        comportamiento_edificios(p, g, delta);
        for (int i = 0; i < visiones.size(); i++) {
            Vision v = visiones.get(i);
            if (v.comportamiento(delta)) {
                visiones.remove(v);
                i--;
            }
        }
    }

    public void comportamiento_unidades(Partida p, Graphics g, int delta) {
        for (Unidad u : unidades) {
            u.comportamiento(p, g, delta);
        }
    }

    public void comportamiento_edificios(Partida p, Graphics g, int delta) {
        for (Edificio e : edificios) {
            e.barra.aumentar_progreso(delta);
            e.comprobar_barra(p, this);
            e.comportamiento(p, g, delta);
        }
    }

    public void dibujar_elementos(Partida p, Graphics g, Input input) {
        for (Recurso r : lista_recursos) {
            r.dibujar(p, color, input, g);
            r.dibujar_barra_de_vida(g, color);
        }
        for (Edificio e : edificios) {
            e.dibujar(p, color, input, g);
        }
        if (this.raza.equals(Maestros.nombre_raza)) {
            if (this.unidades.get(0) instanceof Manipulador) {
                Manipulador m = (Manipulador) this.unidades.get(0);
                m.dibujar_nivel(g);
                m.dibujar_vida(g);
                m.dibujar_mana(g);
            }
        }
        if (this.raza.equals(Eternium.nombre_raza) && (MapaCampo.reloj_eternium.ndivision == 4)) {
            for (Unidad u : unidades) {
                if (u.nombre.equals("Protector")) {
                    u.dibujar(p, color, input, g);
                } else {
                    Eternium.dibujar_detencion(u, color, g);
                }
            }
        } else {
            for (Unidad u : unidades) {
                u.dibujar(p, color, input, g);
            }
        }
        for (ElementoEspecial e : elementos_especiales) {
            e.dibujar(p, color, input, g);
        }
        if (raza.equals(Guardianes.nombre_raza)) {
            eventos.dibujar(g);
        }
    }

    public void dibujar_recursos(Graphics g, float x, float y) {
        switch (raza) {
            case "Fénix":
                g.drawString(Integer.toString((int) recursos) + "%", x - 100, y);
                g.drawString(Integer.toString(cantidad_no_militar()) + "/" + Integer.toString(Fenix.limite_unidades_no_militares), x - 50, y);
                g.drawString(Integer.toString(cantidad_edificio("Cuartel Fénix")) + "/" + Integer.toString(Fenix.limite_cuarteles), x, y);
                g.drawString(Integer.toString(poblacion) + "/" + Integer.toString(poblacion_max), x + 50, y);
                break;
            case "Eternium":
                g.drawString(Integer.toString((int) recursos), x, y);
                g.drawString(Integer.toString(poblacion) + "/" + Integer.toString(poblacion_max), x + 50, y);
                break;
            case "Clark":
                g.drawString(Integer.toString((int) recursos), x, y);
                g.drawString(Integer.toString(poblacion) + "/" + Integer.toString(poblacion_max), x + 50, y);
                break;
            case "Guardianes":
                g.drawString(Integer.toString((int) recursos), x - 100, y);
                g.drawString(Integer.toString((int) recursos_alternativos) + "%", x - 50, y);
                g.drawString(Integer.toString((int) recursos_alternativos_dos), x, y);
                g.drawString(Integer.toString(poblacion) + "/" + Integer.toString(poblacion_max), x + 50, y);
                break;
        }
    }

    public int cantidad_edificio(String n) {
        int resultado = 0;
        for (Edificio ed : edificios) {
            if (n.equals(ed.nombre)) {
                resultado++;
            }
        }
        for (Unidad u : unidades) {
            if (u.constructor) {
                if (u.edificio_construccion != null) {
                    if (u.edificio_construccion.nombre.equals(n)) {
                        resultado++;
                    }
                }
            }
        }
        return resultado;
    }

    public int cantidad_unidad(String n) {
        int resultado = 0;
        for (Unidad ed : unidades) {
            if (n.equals(ed.nombre)) {
                resultado++;
            }
        }
        return resultado;
    }

    public int cantidad_elemento(ElementoSimple e) {
        if (e instanceof Edificio) {
            return cantidad_edificio(e.nombre);
        } else {
            return cantidad_unidad(e.nombre);
        }
    }

    public void avisar_ataque(Partida p, ElementoAtacante atacante) {

    }

    public List<Unidad> cantidad_militar() {
        List<Unidad> resultado = new ArrayList<>();
        for (Unidad u : unidades) {
            if (u.hostil) {
                resultado.add(u);
            }
        }
        return resultado;
    }

    public int cantidad_no_militar() {
        int resultado = 0;
        for (Unidad u : unidades) {
            if (!u.hostil) {
                resultado++;
            }
        }
        return resultado;
    }

    public void aumentar_porcentaje(int c) {
        if (this.recursos_alternativos + c >= Guardianes.maximo_felicidad) {
            this.recursos_alternativos = Guardianes.maximo_felicidad;
        } else {
            this.recursos_alternativos += c;
        }
    }

    public void disminuir_porcentaje(int c) {
        if (this.recursos_alternativos - c <= 0) {
            this.recursos_alternativos = 0;
        } else {
            this.recursos_alternativos -= c;
        }
    }

    public Evento obtener_evento_seleccionado(float x, float y) {
        for (Evento e : eventos.contenido) {
            if (new Rectangle2D.Float(e.x, e.y, e.anchura, e.altura).contains(new Point2D.Float(x, y))) {
                return e;
            }
        }
        return null;
    }
}