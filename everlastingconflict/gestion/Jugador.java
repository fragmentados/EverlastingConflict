/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.gestion;

import everlastingconflict.elements.ElementoAtacante;
import everlastingconflict.elements.ElementoSimple;
import everlastingconflict.elements.impl.*;
import everlastingconflict.elements.util.ElementosComunes;
import everlastingconflict.races.Eternium;
import everlastingconflict.races.Fenix;
import everlastingconflict.races.Guardianes;
import everlastingconflict.races.Raza;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.races.enums.SubRaceEnum;
import everlastingconflict.status.StatusName;
import everlastingconflict.watches.Reloj;
import everlastingconflict.windows.WindowCombat;
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
import java.util.function.Predicate;


public class Jugador {

    //Datos basicos
    public float x_inicial, y_inicial;
    public boolean verticalDown = true, horizontalRight = true;
    public String nombre;
    public RaceEnum raza;
    public SubRaceEnum subRace;
    public Color color;
    //RTS.Elementos
    public List<Unidad> unidades = new ArrayList<>();
    public List<Edificio> edificios = new ArrayList<>();
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
    public boolean isJuggernaut = false;
    public int advantageMultiplier;

    public Predicate<Jugador> isDefeated = jugador -> {
        if (RaceEnum.MAESTROS.equals(jugador.raza)) {
            return jugador.unidades.stream().noneMatch(u -> u instanceof Manipulador);
        } else {
            return jugador.edificios.stream().noneMatch(e -> e.main);
        }
    };



    public float getRecursos() {
        return recursos;
    }

    public void addResources(float amount) {
        recursos += amount;
        checkButtonResources();
    }

    public void diminishFenixPercentage() {
        recursos -= 10;
        checkButtonResources();
    }

    public void removeResources(float amount) {
        if (!raza.equals(RaceEnum.FENIX)) {
            recursos -= amount;
            checkButtonResources();
        }
    }

    public final int getInitialResources() {
        switch (raza) {
            case FENIX:
                return 10;
            case ETERNIUM:
                return 500;
            case CLARK:
                return 300;
            case MAESTROS:
                return 0;
            case GUARDIANES:
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

    public Jugador(String nombre, RaceEnum raza, Integer team, boolean isLeader) {
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

    public Jugador(String nombre, RaceEnum raza, SubRaceEnum subRace, Integer team, boolean isLeader,
                   boolean isJuggernaut) {
        this(nombre, raza, team, isLeader);
        this.subRace = subRace;
        this.isJuggernaut = isJuggernaut;
    }

    public void perforationCheck() {
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
            case GUARDIANES:
                return comprobacion_recursos_guardianes(e);
            default:
                return recursos >= e.coste;
        }
    }

    public void initElements(Game p) {
        switch (raza) {
            case FENIX:
                edificios.add(new Edificio("Sede", x_inicial, y_inicial));
                unidades.add(new Unidad(this, "Constructor", x_inicial, this.verticalOffset(y_inicial, 150)));
                unidades.add(new Unidad(this, "Recolector", this.horizontalOffset(x_inicial, 50),
                        this.verticalOffset(y_inicial, 150)));
                break;
            case ETERNIUM:
                edificios.add(new Edificio("Mando Central", x_inicial, y_inicial));
                unidades.add(new Unidad(this, "Adepto", this.horizontalOffset(x_inicial, 50),
                        this.verticalOffset(y_inicial, 150)));
                break;
            case CLARK:
                edificios.add(new Edificio("Primarca", x_inicial, y_inicial));
                unidades.add(new Unidad(this, "Depredador", this.horizontalOffset(x_inicial, -50),
                        this.verticalOffset(y_inicial, 150)));
                unidades.add(new Unidad(this, "Devorador", this.horizontalOffset(x_inicial, 10),
                        this.verticalOffset(y_inicial, 150)));
                unidades.add(new Unidad(this, "Cazador", this.horizontalOffset(x_inicial, 70),
                        this.verticalOffset(y_inicial, 150)));
                break;
            case MAESTROS:
                unidades.add(new Manipulador(this, x_inicial, y_inicial));
                break;
            case GUARDIANES:
                edificios.add(new Edificio("Ayuntamiento", x_inicial, y_inicial));
                edificios.add(new Taller("Taller bélico", this.horizontalOffset(x_inicial, 300),
                        this.verticalOffset(y_inicial, 250)));
                edificios.add(new Edificio("Academia de pilotos", this.horizontalOffset(x_inicial, 200), y_inicial));
                edificios.add(new Taller("Taller bélico", this.horizontalOffset(x_inicial, 300),
                        this.verticalOffset(y_inicial, 400)));
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
            if (!this.raza.equals(RaceEnum.GUARDIANES) || e.nombre.equals("Ayuntamiento")) {
                e.iniciarbotones(p);
            }
        }
    }

    public void comportamiento_elementos(Game p, Graphics g, int delta) {
        if (this.raza.equals(RaceEnum.GUARDIANES)) {
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

    public void comportamiento_unidades(Game p, Graphics g, int delta) {
        try {
            for (Unidad u : unidades) {
                u.comportamiento(p, g, delta);
            }
        } catch (Exception ex) {
        }
    }

    public void comportamiento_edificios(Game p, Graphics g, int delta) {
        try {
            for (Edificio e : edificios) {
                e.barra.aumentar_progreso(delta);
                e.checkProgressBar(p, this);
                e.comportamiento(p, g, delta);
            }
        } catch (Exception ex) {
        }
    }

    public void renderMainTeamElements(Game p, Graphics g, Input input) {
        for (Recurso r : lista_recursos) {
            r.render(p, color, input, g);
        }
        for (Edificio e : edificios) {
            e.render(p, color, input, g);
        }
        if (this.raza.equals(RaceEnum.MAESTROS)) {
            if (this.unidades.get(0) instanceof Manipulador) {
                Manipulador m = (Manipulador) this.unidades.get(0);
                m.drawXpCircle(g);
                m.drawLifeCircle(g);
                m.drawManaCircle(g);
            }
        }
        if (this.raza.equals(RaceEnum.ETERNIUM) && (WindowCombat.eterniumWatch().ndivision == 4)) {
            for (Unidad u : unidades) {
                if (u.nombre.equals("Protector") || u.nombre.equals("Erradicador")) {
                    u.render(p, color, input, g);
                } else {
                    Eternium.dibujar_detencion(u, color, g);
                }
            }
        } else {
            for (Unidad u : unidades) {
                u.render(p, color, input, g);
            }
        }
        for (ElementoEspecial e : elementos_especiales) {
            e.render(p, color, input, g);
        }
        if (raza.equals(RaceEnum.GUARDIANES)) {
            eventos.dibujar(g);
        }
    }

    public void renderNonMainTeamElements(Game p, Graphics g, Input input) {
        lista_recursos.stream().filter(r -> r.isVisibleByMainTeam(p)).forEach(r -> r.render(p, color, input, g));
        edificios.stream().filter(e -> e.isVisibleByMainTeam(p)).forEach(e -> e.render(p, color, input, g));
        if (this.raza.equals(RaceEnum.ETERNIUM) &&
                (WindowCombat.eterniumWatch() != null && WindowCombat.eterniumWatch().ndivision == 4)) {
            unidades.stream().filter(u -> u.isVisibleByMainTeam(p) && (u.nombre.equals("Protector") || u.nombre.equals(
                    "Erradicador"))).forEach(u -> Eternium.dibujar_detencion(u, color, g));
            unidades.stream().filter(u -> u.isVisibleByMainTeam(p) && !(u.nombre.equals("Protector") || u.nombre.equals("Erradicador")))
                    .forEach(u -> u.render(p, color, input, g));
        } else {
            unidades.stream().filter(u -> u.isVisibleByMainTeam(p)).forEach(u -> u.render(p, color, input, g));
        }
    }

    public void renderResources(Graphics g, float x, float y) {
        switch (raza) {
            case FENIX:
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
            case CLARK:
            case ETERNIUM:
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
            case GUARDIANES:
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

    public void avisar_ataque(Game p, ElementoAtacante atacante) {

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
        return this.isDefeated.test(this);
    }

    public void renderVisibility(Graphics g, Color c, int desplazamiento) {
        g.setColor(c);
        unidades.forEach(u -> {
            int alcance = (u.statusCollection.existe_estado(StatusName.CEGUERA)) ? 10 + desplazamiento :
                    u.vision + desplazamiento;
            g.fillOval(u.x - alcance / 2, u.y - alcance / 2, alcance, alcance);
        });
        edificios.forEach(u -> {
            int alcance = u.vision + desplazamiento;
            g.fillOval(u.x - alcance / 2, u.y - alcance / 2, alcance, alcance);
        });
        visiones.stream().forEach(v -> v.dibujar(g, c, desplazamiento));
    }

    public float horizontalOffset(float initialValue, float offset) {
        return this.horizontalRight ? initialValue + offset : initialValue - offset;
    }

    public float verticalOffset(float initialValue, float offset) {
        return this.verticalDown ? initialValue + offset : initialValue - offset;
    }

    public void applyJuggernautEnhancements(Game p, int numberOfEnemyPlayers) {
        // A 1v1 juggernaut would make no changes
        int advantageMultiplier = numberOfEnemyPlayers - 1;
        this.advantageMultiplier = advantageMultiplier;
        switch (raza) {
            case FENIX:
                // The fenix player starts with more resources
                this.addResources(10 * advantageMultiplier);
                break;
            case ETERNIUM:
                // The eternium player starts with more resources and with a certain number of buildings built
                if (advantageMultiplier > 0) {
                    this.edificios.add(new Edificio("Cámara de asimilación", this.horizontalOffset(this.x_inicial,
                            400), this.verticalOffset(this.y_inicial, 200)));

                }
                if (advantageMultiplier > 1) {
                    this.edificios.add(new Edificio("Teletransportador", this.horizontalOffset(this.x_inicial, 400),
                            this.verticalOffset(this.y_inicial, 350)));
                }
                if (advantageMultiplier > 2) {
                    this.edificios.add(new Edificio("Altar de los ancestros", this.horizontalOffset(this.x_inicial, 600),
                            this.verticalOffset(this.y_inicial, 200)));
                }
                this.addResources(Eternium.recursos_refineria * advantageMultiplier);
                break;
            case CLARK:
                // Add an extra set of basic units per disadvantage
                // We also reduce the cost on 10 * disadvantage
                for (int i = 0; i < advantageMultiplier; i++) {
                    unidades.add(new Unidad(this, "Depredador", this.horizontalOffset(x_inicial, -50),
                            this.verticalOffset(y_inicial, 150 + 75 * (i + 1))));
                    unidades.add(new Unidad(this, "Devorador", this.horizontalOffset(x_inicial, 10),
                            this.verticalOffset(y_inicial, 150 + 75 * (i + 1))));
                    unidades.add(new Unidad(this, "Cazador", this.horizontalOffset(x_inicial, 70),
                            this.verticalOffset(y_inicial, 150 + 75 * (i + 1))));
                }
                break;
            case GUARDIANES:
                // We automatically enable a number of buildings equal to the disadvantage
                for (int i = 0; i < advantageMultiplier; i++) {
                    this.edificios.get(i + 1).enable(null, p);
                }
                this.guardiansPeoplePercentage += 5 * advantageMultiplier;
                break;
            case MAESTROS:
                // The manipulator will have higher stats based on his disadvantage
                Manipulador m =
                        (Manipulador) this.unidades.stream().filter(u -> u instanceof Manipulador).findFirst().orElse(null);
                m.vida += 100 * advantageMultiplier;
                m.vida_max += 100 * advantageMultiplier;
                m.mana += 50 * advantageMultiplier;
                m.mana_max += 50 * advantageMultiplier;
                m.regeneracion_mana += 0.1 * advantageMultiplier;
                m.poder_magico += 5 * advantageMultiplier;
                m.ataque += 5 * advantageMultiplier;
                m.cadencia -= 0.1 * advantageMultiplier;
                m.defensa += 0.5 * advantageMultiplier;
                break;
        }
    }
}
