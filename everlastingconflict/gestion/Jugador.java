/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.gestion;

import everlastingconflict.elementos.ElementoAtacante;
import everlastingconflict.elementos.ElementoSimple;
import everlastingconflict.elementos.implementacion.*;
import everlastingconflict.elementos.util.ElementosComunes;
import everlastingconflict.razas.*;
import everlastingconflict.relojes.Reloj;
import everlastingconflict.ventanas.VentanaCombate;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Elías
 */
public class Jugador {

    //Datos basicos
    public float x_inicial, y_inicial;
    public boolean verticalDown = true, horizontalRight = true;
    public String nombre, raza;
    public SubRaceEnum subRace;
    public Color color;
    //RTS.Elementos
    public List<Unidad> unidades;
    public List<Edificio> edificios;
    public List<Recurso> lista_recursos = new ArrayList<>();
    public List<Vision> visiones;
    public List<ElementoEspecial> elementos_especiales;
    public List<Tecnologia> tecnologias = new ArrayList<>();
    //Recursos
    private float recursos;
    public Image resourceImage;
    public float guardiansPeoplePercentage;
    public float guardiansThreatLevel;
    public int poblacion, poblacion_max;
    public boolean perforacion = false;
    public Eventos eventos;
    public int limite_cuarteles = 2;
    // Team data
    public Integer team;
    public boolean isMainPlayer;
    public boolean isLeader = false;

    public float getRecursos() {
        return recursos;
    }

    public void addResources(float amount) {
        recursos += amount;
        checkButtonResources();
    }

    public void removeResources(float amount) {
        if (!raza.equals(RaceEnum.FENIX.getName())) {
            recursos -= amount;
            checkButtonResources();
        }
    }

    public final int getInitialResources() {
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
                guardiansPeoplePercentage = 50;
                guardiansThreatLevel = 1;
                return 50;
        }
        //Nunca se debería llegar
        return -1;
    }

    public Jugador() {

    }

    public Jugador(String nombre, String raza, Integer team, boolean isLeader) {
        this.nombre = nombre;
        this.raza = raza;
        unidades = new ArrayList<>();
        edificios = new ArrayList<>();
        lista_recursos = new ArrayList<>();
        visiones = new ArrayList<>();
        elementos_especiales = new ArrayList<>();
        recursos = getInitialResources();
        poblacion = 0;
        poblacion_max = 200;
        this.resourceImage = Raza.getResourceImage(raza);
        this.team = team;
        this.isLeader = isLeader;
    }

    public Jugador(String nombre, String raza, Integer team, boolean isLeader, SubRaceEnum subRace) {
        this(nombre, raza, team, isLeader);
        this.subRace = subRace;
    }

    public void comprobacion_perforacion() {
        List<String> resultado = new ArrayList<>();
        for (Edificio e : edificios) {
            if ((e.nombre.equals("Cámara de asimilación") || (e.nombre.equals("Teletransportador")))) {
                if (resultado.indexOf(e.nombre) == -1) {
                    resultado.add(e.nombre);
                }
            }
        }
        perforacion = (resultado.size() == 2);
    }

    public boolean comprobacion_recursos_guardianes(ElementoSimple e) {
        return recursos >= e.coste && this.guardiansThreatLevel >= e.guardiansThreatLevelNeeded;
    }

    public boolean comprobacion_recursos(ElementoSimple e) {
        switch (raza) {
            case "Guardianes":
                return comprobacion_recursos_guardianes(e);
            default:
                return recursos >= e.coste;
        }
    }

    public void initElements(Partida p) {
        switch (raza) {
            case "Fénix":
                edificios.add(new Edificio("Sede", x_inicial, y_inicial));
                unidades.add(new Unidad("Constructor", x_inicial, this.verticalOffset(y_inicial, 150)));
                unidades.add(new Unidad("Recolector", this.horizontalOffset(x_inicial, 50), this.verticalOffset(y_inicial, 150)));
                break;
            case "Eternium":
                edificios.add(new Edificio("Mando Central", x_inicial, y_inicial));
                unidades.add(new Unidad("Adepto", this.horizontalOffset(x_inicial, 50), this.verticalOffset(y_inicial, 150)));
                break;
            case "Clark":
                edificios.add(new Edificio("Primarca", x_inicial, y_inicial));
                unidades.add(new Unidad("Depredador", this.horizontalOffset(x_inicial, -50), this.verticalOffset(y_inicial, 150)));
                unidades.add(new Unidad("Devorador", this.horizontalOffset(x_inicial, 10), this.verticalOffset(y_inicial, 150)));
                unidades.add(new Unidad("Cazador", this.horizontalOffset(x_inicial, 70), this.verticalOffset(y_inicial, 150)));
                break;
            case "Maestros":
                unidades.add(new Manipulador(x_inicial, y_inicial));
                break;
            case "Guardianes":
                edificios.add(new Edificio("Ayuntamiento", x_inicial, y_inicial));
                edificios.add(new Taller("Taller bélico", this.horizontalOffset(x_inicial, 300), this.verticalOffset(y_inicial, 250)));
                edificios.add(new Edificio("Academia de pilotos", this.horizontalOffset(x_inicial, 200), y_inicial));
                edificios.add(new Taller("Taller bélico", this.horizontalOffset(x_inicial, 300), this.verticalOffset(y_inicial, 400)));
                edificios.add(new Edificio("Templo", this.horizontalOffset(x_inicial, 400), y_inicial));
                edificios.add(new Edificio("Laboratorio de I+D", this.horizontalOffset(x_inicial, 500), y_inicial));
                edificios.add(new Edificio("Vaticano", this.horizontalOffset(x_inicial, 600), y_inicial));
                edificios.add(new Edificio("Edificio gubernamental", this.horizontalOffset(x_inicial, 700), y_inicial));
                break;
        }
        for (Unidad u : unidades) {
            this.poblacion += u.coste_poblacion;
            u.iniciarbotones(p);
        }
        for (Edificio e : edificios) {
            if (!this.raza.equals(RaceEnum.GUARDIANES.getName()) || e.nombre.equals("Ayuntamiento")) {
                e.iniciarbotones(p);
            }
        }
    }

    public void comportamiento_elementos(Partida p, Graphics g, int delta) {
        if (this.raza.equals(RaceEnum.GUARDIANES.getName())) {
            this.addResources((this.guardiansPeoplePercentage / 100f) * delta * Reloj.TIME_REGULAR_SPEED * Guardianes.recursos_por_segundo);
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
        try {
            for (Unidad u : unidades) {
                u.comportamiento(p, g, delta);
            }
        } catch (Exception ex) {
        }
    }

    public void comportamiento_edificios(Partida p, Graphics g, int delta) {
        try {
            for (Edificio e : edificios) {
                e.barra.aumentar_progreso(delta);
                e.checkProgressBar(p, this);
                e.comportamiento(p, g, delta);
            }
        } catch (Exception ex) {
        }
    }

    public void renderMainTeamElements(Partida p, Graphics g, Input input) {
        for (Recurso r : lista_recursos) {
            r.dibujar(p, color, input, g);
        }
        for (Edificio e : edificios) {
            e.dibujar(p, color, input, g);
        }
        if (this.raza.equals(RaceEnum.MAESTROS.getName())) {
            if (this.unidades.get(0) instanceof Manipulador) {
                Manipulador m = (Manipulador) this.unidades.get(0);
                m.drawXpCircle(g);
                m.drawLifeCircle(g);
                m.drawManaCircle(g);
            }
        }
        if (this.raza.equals(RaceEnum.ETERNIUM.getName()) && (VentanaCombate.eterniumWatch().ndivision == 4)) {
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
        if (raza.equals(RaceEnum.GUARDIANES.getName())) {
            eventos.dibujar(g);
        }
    }

    public void renderNonMainTeamElements(Partida p, Graphics g, Input input) {
        lista_recursos.stream().filter(r -> r.visibleByMainTeam(p)).forEach(r -> r.dibujar(p, color, input, g));
        edificios.stream().filter(e -> e.visibleByMainTeam(p)).forEach(e -> e.dibujar(p, color, input, g));
        if (this.raza.equals(RaceEnum.ETERNIUM.getName()) &&
                (VentanaCombate.eterniumWatch() != null && VentanaCombate.eterniumWatch().ndivision == 4)) {
            unidades.stream().filter(u -> u.visibleByMainTeam(p)).forEach(u -> Eternium.dibujar_detencion(u, color, g));
        } else {
            unidades.stream().filter(u -> u.visibleByMainTeam(p)).forEach(u -> u.dibujar(p, color, input, g));
        }
    }

    public void renderResources(Graphics g, float x, float y) {
        switch (raza) {
            case "Fénix":
                if (isLeader) {
                    ElementosComunes.LIDER_IMAGE.draw(x - 160, y, 20, 20);
                }
                g.setColor(this.color);
                g.fillRect(x - 140, y, 20, 20);
                g.setColor(Color.white);
                this.resourceImage.draw(x - 120, y, 20, 20);
                g.drawString(Integer.toString((int) recursos) + "%", x - 100, y);
                ElementosComunes.FENIX_UNIDADES_NO_CONSTRUCTORAS.draw(x - 70, y, 20, 20);
                g.drawString(Integer.toString(cantidad_no_militar()) + "/" + Integer.toString(Fenix.limite_unidades_no_militares), x - 50, y);
                ElementosComunes.FENIX_CUARTEL.draw(x - 20, y, 20, 20);
                g.drawString(Integer.toString(cantidad_edificio("Cuartel Fénix")) + "/" + Integer.toString(this.limite_cuarteles), x, y);
                ElementosComunes.POPULATION_IMAGE.draw(x + 30, y, 20, 20);
                g.drawString(Integer.toString(poblacion) + "/" + Integer.toString(poblacion_max), x + 50, y);
                break;
            case "Clark":
            case "Eternium":
                if (isLeader) {
                    ElementosComunes.LIDER_IMAGE.draw(x - 40, y, 20, 20);
                }
                g.setColor(this.color);
                g.fillRect(x - 20, y, 20, 20);
                g.setColor(Color.white);
                this.resourceImage.draw(x, y, 20, 20);
                g.drawString(Integer.toString((int) recursos), x + 20, y);
                ElementosComunes.POPULATION_IMAGE.draw(x + 70, y, 20, 20);
                g.drawString(Integer.toString(poblacion) + "/" + Integer.toString(poblacion_max), x + 90, y);

                break;
            case "Guardianes":
                if (isLeader) {
                    ElementosComunes.LIDER_IMAGE.draw(x - 160, y, 20, 20);
                }
                g.setColor(this.color);
                g.fillRect(x - 140, y, 20, 20);
                g.setColor(Color.white);
                ElementosComunes.MONEY_IMAGE.draw(x - 120, y, 20, 20);
                g.drawString(Integer.toString((int) recursos), x - 100, y);
                this.resourceImage.draw(x - 70, y, 20, 20);
                g.drawString(Integer.toString((int) guardiansPeoplePercentage) + "%", x - 50, y);
                ElementosComunes.THREAT_IMAGE.draw(x - 10, y, 20, 20);
                g.drawString(Integer.toString((int) guardiansThreatLevel), x + 10, y);
                ElementosComunes.POPULATION_IMAGE.draw(x + 30, y, 20, 20);
                g.drawString(Integer.toString(poblacion) + "/" + Integer.toString(poblacion_max), x + 50, y);
                break;
        }
    }

    public int cantidad_edificio(String n) {
        Set<Edificio> buildingsToCount = new HashSet<>();
        for (Edificio ed : edificios) {
            if (n.equals(ed.nombre)) {
                buildingsToCount.add(ed);
            }
        }
        for (Unidad u : unidades) {
            if (u.constructor) {
                if (u.edificio_construccion != null) {
                    if (u.edificio_construccion.nombre.equals(n)) {
                        buildingsToCount.add(u.edificio_construccion);
                    }
                }
            }
        }
        return buildingsToCount.size();
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
        if (this.guardiansPeoplePercentage + c >= Guardianes.maximo_felicidad) {
            this.guardiansPeoplePercentage = Guardianes.maximo_felicidad;
        } else {
            this.guardiansPeoplePercentage += c;
        }
    }

    public void disminuir_porcentaje(int c) {
        if (this.guardiansPeoplePercentage - c <= 0) {
            this.guardiansPeoplePercentage = 0;
        } else {
            this.guardiansPeoplePercentage -= c;
        }
    }

    public Evento getSelectedEvent(float x, float y) {
        for (Evento e : eventos.contenido) {
            if (new Rectangle2D.Float(e.x, e.y, e.anchura, e.altura).contains(new Point2D.Float(x, y))) {
                return e;
            }
        }
        return null;
    }

    public void checkButtonResources() {
        edificios.stream().forEach(e -> e.checkButtonResources(this));
        unidades.stream().forEach(u -> u.checkButtonResources(this));
    }

    public boolean hasTecnologyResearched(String tecnologyName) {
        return this.tecnologias.stream().anyMatch(t -> t.nombre.equals(tecnologyName));
    }

    public Manipulador getManipulator() {
        return (Manipulador) this.unidades.stream().filter(u -> u instanceof Manipulador).findFirst().get();
    }

    public boolean isDefeated() {
        if (RaceEnum.MAESTROS.getName().equals(this.raza)) {
            return !unidades.get(0).nombre.equals("Manipulador");
        } else {
            return edificios.stream().noneMatch(e -> e.main);
        }
    }

    public void renderVisibility(Graphics g, Color c, int desplazamiento) {
        g.setColor(c);
        unidades.forEach(u -> {
            int alcance = u.vision + desplazamiento;
            g.fillOval(u.x - alcance / 2, u.y - alcance / 2, alcance, alcance);
        });
        edificios.forEach(u -> {
            int alcance = u.vision + desplazamiento;
            g.fillOval(u.x - alcance / 2, u.y - alcance / 2, alcance, alcance);
        });
        visiones.stream().forEach(v -> v.dibujar(g, desplazamiento));
    }

    public float horizontalOffset(float initialValue, float offset) {
        return this.horizontalRight ? initialValue + offset : initialValue - offset;
    }

    public float verticalOffset(float initialValue, float offset) {
        return this.verticalDown ? initialValue + offset : initialValue - offset;
    }
}
