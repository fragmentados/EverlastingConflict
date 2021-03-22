/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.gestion;

import everlastingconflict.elementos.ElementoComplejo;
import everlastingconflict.elementos.ElementoCoordenadas;
import everlastingconflict.elementos.ElementoVulnerable;
import everlastingconflict.elementos.implementacion.*;
import everlastingconflict.razas.RaceEnum;
import everlastingconflict.relojes.RelojEternium;
import everlastingconflict.relojes.RelojMaestros;
import everlastingconflict.ventanas.MapEnum;
import everlastingconflict.ventanas.VentanaCombate;
import everlastingconflict.victory.AnhilationVictoryCondition;
import everlastingconflict.victory.VictoryCondition;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static everlastingconflict.ventanas.VentanaCombate.*;

/**
 *
 * @author Elías
 */
public class Partida {

    public List<Jugador> players = new ArrayList<>();
    public List<Recurso> recursos;
    public List<Bestias> bestias;
    public List<Proyectil> proyectiles;
    public MapEnum map;
    public VictoryCondition victoryCondition = new AnhilationVictoryCondition();

    public static String anadir_saltos_de_linea(String texto, float anchura) {
        String resultado = "";
        String contador = texto;
        String a, b;
        int fin = texto.length() / ((int) anchura / 10);
        if (fin == 0) {
            resultado = contador;
        }
        for (int i = 0; i < fin; i++) {
            a = contador.substring(0, ((int) anchura / 10));
            b = contador.substring(((int) anchura / 10));
            a += "\n";
            if (i == (fin - 1)) {
                resultado += a;
                resultado += b;
            } else {
                resultado += a;
                contador = b;
            }
        }
        return resultado;
    }

    public Recurso closestResource(Recurso recurso, String nombre_jugador, String nombre, float x, float y) {
        //Se pasa recurso para que no devuelva el mismo recurso.
        //Se pasa nombre para que el recurso tenga un nombre determinado
        //Se pasa nombre_jugador para que no devuelva un recurso capturado por el jugador
        //Todos los argumentos salvo x e y son opcionales
        Recurso resultado = null;
        float distancia = -1;
        for (Recurso r : recursos) {
            if ((recurso == null) || (r != recurso)) {
                if ((nombre == null) || ((r.nombre.equals(nombre) && (!r.nombre.equals("Civiles") || r.vida == 0)))) {
                    if ((nombre_jugador == null) || (!nombre_jugador.equals(r.capturador))) {
                        float distancia_local = Math.abs(r.x - x) + Math.abs(r.y - y);
                        if ((distancia_local < distancia) || (distancia == -1)) {
                            if (!r.ocupado) {
                                distancia = distancia_local;
                                resultado = r;
                            }
                        }
                    }
                }
            }
        }
        return resultado;
    }

    public List<Jugador> enemyPlayers(Jugador player) {
        return players.stream().filter(p -> !player.team.equals(p.team)).collect(Collectors.toList());
    }

    public List<Jugador> getNonMainPlayers() {
        return players.stream().filter(p -> !p.isMainPlayer).collect(Collectors.toList());
    }

    public List<Jugador> getNonMainTeam() {
        List<Jugador> mainTeam = getMainTeam();
        return this.players.stream().filter(p -> !mainTeam.contains(p)).collect(Collectors.toList());
    }

    public Jugador getMainPlayer() {
        return players.stream().filter(p -> p.isMainPlayer).findFirst().get();
    }

    public List<Jugador> getMainTeam() {
        List<Jugador> mainTeam = new ArrayList<>();
        mainTeam.add(getMainPlayer());
        mainTeam.addAll(getAlliesFromPlayer(mainTeam.get(0)));
        return mainTeam;
    }

    public List<Jugador> getAlliesFromPlayer(Jugador j) {
        return players.stream().filter(p -> !j.equals(p) && j.team.equals(p.team)).collect(Collectors.toList());
    }

    public boolean belongsToMainPlayer(ElementoCoordenadas e) {
        Jugador mainPlayer = this.getMainPlayer();
        if (e instanceof Unidad) {
            return mainPlayer.unidades.indexOf(e) != -1;
        } else if (e instanceof Edificio) {
            return mainPlayer.edificios.indexOf(e) != -1;
        } else {
            return mainPlayer.lista_recursos.indexOf(e) != -1;
        }
    }

    public Jugador getPlayerFromElement(ElementoCoordenadas e) {
        return this.players.stream()
                .filter(p -> p.unidades.contains(e) || p.edificios.contains(e) || p.lista_recursos.contains(e))
                .findFirst().orElse(null);
    }

    public List<Jugador> getEnemyPlayersFromElement(ElementoCoordenadas e) {
        Jugador playerFromElement = getPlayerFromElement(e);
        if (playerFromElement != null) {
            return this.players.stream().filter(p -> !p.team.equals(playerFromElement.team)).collect(Collectors.toList());
        } else {
            return Arrays.asList(this.getMainPlayer());
        }
    }

    public Jugador getPlayerByRace(RaceEnum race) {
        return this.players.stream().filter(p -> p.raza.equals(race.getName())).findFirst().orElse(null);
    }

    public Jugador getPlayerByName(String name) {
        return this.players.stream().filter(p -> p.nombre.equals(name)).findFirst().orElse(null);
    }

    public boolean existsPlayerWithRace(RaceEnum race) {
        return this.players.stream().anyMatch(p -> race.getName().equals(p.raza));
    }

    public void initElements() {
        //Representan la altura y anchura del mapa
        float width = map.getWidth();
        float height = map.getHeight();
        VentanaCombate.WORLD_SIZE_X = width;
        VentanaCombate.WORLD_SIZE_Y = height;
        VentanaCombate.offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_WIDTH;
        VentanaCombate.offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_HEIGHT;
        initPlayerCoordinates(width, height);
        float sextox = width / 6;
        float sextoy = height / 6;
        if (existsPlayerWithRace(RaceEnum.CLARK) || existsPlayerWithRace(RaceEnum.MAESTROS)) {
            //Alphas
            bestias.add(new Bestias("Grupo1", sextox, sextoy));
            bestias.add(new Bestias("Grupo1", sextox * 3, sextoy));
            bestias.add(new Bestias("Grupo1", sextox * 5, sextoy));
            bestias.add(new Bestias("Grupo1", sextox, sextoy * 3));
            bestias.add(new Bestias("Grupo1", sextox, sextoy * 5));
            bestias.add(new Bestias("Grupo1", sextox * 5, sextoy * 3));
            bestias.add(new Bestias("Grupo1", sextox * 5, sextoy * 5));
            bestias.add(new Bestias("Grupo1", sextox * 3, sextoy * 5));
            //Betas
            bestias.add(new Bestias("Grupo2", sextox * 3, sextoy * 2));
            bestias.add(new Bestias("Grupo2", sextox * 3, sextoy * 4));
            bestias.add(new Bestias("Grupo2", sextox * 2, sextoy * 3));
            bestias.add(new Bestias("Grupo2", sextox * 4, sextoy * 3));
            //Gamma
            bestias.add(new Bestias("Grupo3", sextox * 3, sextoy * 3));
            //Ommegas
            bestias.add(new Bestias("Grupo4", sextox * 2, sextoy * 2));
            bestias.add(new Bestias("Grupo4", sextox * 4, sextoy * 2));
            bestias.add(new Bestias("Grupo4", sextox * 2, sextoy * 4));
            bestias.add(new Bestias("Grupo4", sextox * 4, sextoy * 4));
//            for (Bestias be : bestias) {
//                be.cambiar_coordenadas(be.x - 200, be.y - 200);
//            }
        }
        if (existsPlayerWithRace(RaceEnum.ETERNIUM)) {
            recursos.add(new Recurso("Hierro", sextox * 2, sextoy));
            recursos.add(new Recurso("Hierro", sextox * 4, sextoy));
            recursos.add(new Recurso("Hierro", sextox, sextoy * 2));
            recursos.add(new Recurso("Hierro", sextox * 5, sextoy * 2));
            recursos.add(new Recurso("Hierro", sextox, sextoy * 4));
            recursos.add(new Recurso("Hierro", sextox * 5, sextoy * 4));
            recursos.add(new Recurso("Hierro", sextox * 2, sextoy * 5));
            recursos.add(new Recurso("Hierro", sextox * 4, sextoy * 5));
        }
        if (existsPlayerWithRace(RaceEnum.FENIX)) {
            if (existsPlayerWithRace(RaceEnum.ETERNIUM)) {
                recursos.add(new Recurso("Civiles", sextox, sextoy));
                recursos.add(new Recurso("Civiles", sextox * 3, sextoy));
                recursos.add(new Recurso("Civiles", sextox * 5, sextoy));
                recursos.add(new Recurso("Civiles", sextox, sextoy * 3));
                recursos.add(new Recurso("Civiles", sextox, sextoy * 5));
                recursos.add(new Recurso("Civiles", sextox * 5, sextoy * 3));
                recursos.add(new Recurso("Civiles", sextox * 5, sextoy * 5));
                recursos.add(new Recurso("Civiles", sextox * 3, sextoy * 5));
            } else {
                recursos.add(new Recurso("Civiles", sextox * 2, sextoy));
                recursos.add(new Recurso("Civiles", sextox * 4, sextoy));
                recursos.add(new Recurso("Civiles", sextox, sextoy * 2));
                recursos.add(new Recurso("Civiles", sextox * 5, sextoy * 2));
                recursos.add(new Recurso("Civiles", sextox, sextoy * 4));
                recursos.add(new Recurso("Civiles", sextox * 5, sextoy * 4));
                recursos.add(new Recurso("Civiles", sextox * 2, sextoy * 5));
                recursos.add(new Recurso("Civiles", sextox * 4, sextoy * 5));
            }
        }
        // Torre de vigilancia en medio del mapa
        recursos.add(new TorreVision("Vision", map.getWidth() / 2, map.getHeight() / 2));
        // Inicializar relojes
        VentanaCombate.initWatches();
        if (existsPlayerWithRace(RaceEnum.MAESTROS)) {
            VentanaCombate.createWatch(new RelojMaestros(this.getPlayerByRace(RaceEnum.MAESTROS)));
        }
        if (existsPlayerWithRace(RaceEnum.ETERNIUM)) {
            VentanaCombate.createWatch(new RelojEternium(this.getPlayerByRace(RaceEnum.ETERNIUM)));
        }
        initPlayerColors();
        players.forEach(p -> p.initElements(this));
        players.get(0).unidades.add(new Unidad("Tortuga", 200, 200));
    }

    private void initPlayerCoordinates(float width, float height) {
        // Left Top Corner
        this.players.get(0).x_inicial = 200;
        this.players.get(0).y_inicial = 200;
        // Right Bottom Corner
        this.players.get(1).x_inicial = width - 400;
        this.players.get(1).y_inicial = height - 400;
        this.players.get(1).verticalDown = false;
        this.players.get(1).horizontalRight = false;
        if (this.players.size() >= 3) {
            // Right Top Corner
            this.players.get(2).x_inicial = width - 400;
            this.players.get(2).y_inicial = 400;
            this.players.get(2).horizontalRight = false;
        }
        if (this.players.size() >= 4) {
            // Left Bottom Corner
            this.players.get(3).x_inicial = 400;
            this.players.get(3).y_inicial = height - 400;
            this.players.get(3).verticalDown = false;
        }
        if (this.players.size() >= 5) {
            // Left Middle Corner
            this.players.get(4).x_inicial = 200;
            this.players.get(4).y_inicial = height / 2;
        }
        if (this.players.size() >= 6) {
            // Right Bottom Corner
            this.players.get(5).x_inicial = width - 400;
            this.players.get(5).y_inicial = height / 2;
            this.players.get(5).horizontalRight = false;
        }
    }

    private void initPlayerColors() {
        Jugador mainPlayer = getMainPlayer();
        mainPlayer.color = Color.green;
        // Allies
        players.stream().filter(p -> !mainPlayer.equals(p) && p.team.equals(mainPlayer.team)).forEach(p -> p.color = Color.yellow);
        // Enemies
        players.stream().filter(p -> !p.team.equals(mainPlayer.team)).forEach(p -> p.color = Color.red);
    }

    public boolean checkMainPlayerVictory() {
        return this.players.stream().filter(p -> !p.isMainPlayer).allMatch(p -> this.victoryCondition.isDefeated(this, p));
    }

    public boolean checkMainPlayerDefeat() {
        return this.victoryCondition.isDefeated(this, getMainPlayer());
    }

    public void comportamiento_elementos(Graphics g, int delta) {
        try {
            for (Bestias b : bestias) {
                b.comportamiento(this, g, delta);
            }
            for (Proyectil p : proyectiles) {
                p.comportamiento(this, g, delta);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        players.forEach(p -> p.comportamiento_elementos(this, g, delta));
    }

    public void renderElements(Graphics g, Input input) {
        for (Recurso r : recursos) {
            if (r.visibleByMainTeam(this)) {
                if (r.capturador == null) {
                    r.dibujar(this, Color.blue, input, g);
                }
            }
        }
        for (Bestias b : bestias) {
            b.dibujar(this, Color.blue, input, g);
        }
        for (Proyectil p : proyectiles) {
            if (p.visibleByMainTeam(this)) {
                p.dibujar(this, Color.blue, input, g);
            }
        }
        getMainTeam().forEach(p -> p.renderMainTeamElements(this, g, input));
        getNonMainTeam().forEach(p -> p.renderNonMainTeamElements(this, g, input));
    }

    public Partida() {
        recursos = new ArrayList<>();
        bestias = new ArrayList<>();
        proyectiles = new ArrayList<>();
    }

    public Partida(List<Jugador> players, MapEnum map) {
        this();
        this.players = players;
        this.players.get(0).isMainPlayer = true;
        this.map = map;
    }

    public Partida(List<Jugador> players, MapEnum map, VictoryCondition victoryCondition) {
        this(players, map);
        this.victoryCondition = victoryCondition;
    }

    public ElementoVulnerable getElementAttackedAtPosition(float x, float y, List<Unidad> seleccionadas) {
        Optional<Unidad> unitAttackedOp = getNonMainPlayers().stream().flatMap(p -> p.unidades.stream()).filter(u -> u.visible(this) && u.hitbox(x, y)).findFirst();
        if (!unitAttackedOp.isPresent()) {
            Optional<Edificio> buildingAttackedOp = getNonMainPlayers().stream().flatMap(p -> p.edificios.stream()).filter(e -> e.visible(this) && e.hitbox(x, y)).findFirst();
            if (!buildingAttackedOp.isPresent()) {
                Optional<Recurso> resourceAttackedOp = getNonMainPlayers().stream().flatMap(p -> p.lista_recursos.stream()).filter(r -> r.visible(this) && r.hitbox(x, y)).findFirst();
                if (!resourceAttackedOp.isPresent()) {
                    Optional<Bestia> beastAttackedOp = this.bestias.stream().flatMap(be -> be.getContenido().stream()).filter(b -> b.visible(this) && b.hitbox(x, y)).findFirst();
                    if (beastAttackedOp.isPresent()) {
                        final ElementoVulnerable beastAttacked = beastAttackedOp.get();
                        seleccionadas.stream().forEach(s -> s.atacar(this, beastAttacked));
                        return beastAttacked;
                    }
                } else {
                    final ElementoVulnerable resourceAttacked = resourceAttackedOp.get();
                    seleccionadas.stream().forEach(s -> s.atacar(this, resourceAttacked));
                    return resourceAttacked;
                }
            } else {
                final ElementoVulnerable buildingAttacked = buildingAttackedOp.get();
                seleccionadas.stream().forEach(s -> s.atacar(this, buildingAttacked));
                return buildingAttacked;
            }
        } else {
            final ElementoVulnerable unitAttacked = unitAttackedOp.get();
            seleccionadas.stream().forEach(s -> s.atacar(this, unitAttacked));
            return unitAttacked;
        }
        return null;
    }

    private List<ElementoComplejo> getSelectableElements() {
        //Seleccionar un Elemento
        List<ElementoComplejo> selectableElements = new ArrayList<>();
        players.forEach(p -> {
            selectableElements.addAll(p.unidades);
            selectableElements.addAll(p.edificios);
            selectableElements.addAll(p.lista_recursos);
        });
        bestias.stream().map(bs -> bs.contenido).forEach(b -> selectableElements.addAll(b));
        selectableElements.addAll(recursos);
        return selectableElements;
    }

    public void deselectAllElements() {
        //Seleccionar un Elemento
        List<ElementoComplejo> selectableElements = getSelectableElements();
        selectableElements.stream()
                .filter(e -> e.seleccionada())
                .forEach(e -> e.deseleccionar());
    }

    public void checkSelections(Input input) {
        //Seleccionar un Elemento
        List<ElementoComplejo> selectableElements = getSelectableElements();
        selectableElements.stream()
                .filter(e -> e.hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY()))
                .findFirst().ifPresent(e -> handleSelection(e));
    }

    private void handleSelection(ElementoComplejo e) {
        if (!mayus) {
            deselectAllElements();
        }
        e.seleccionar(mayus);
        click = false;
    }

    public boolean resourceBelongsToPlayer(Recurso r) {
        return players.stream().anyMatch(p -> p.lista_recursos.contains(r));
    }

    public List<Recurso> getGameCivilTowns() {
        return this.recursos.stream().filter(r -> r.nombre.equals("Civiles")).collect(Collectors.toList());
    }

    public List<Recurso> getVisionTowers() {
        return this.recursos.stream().filter(r -> r.nombre.equals("Vision")).collect(Collectors.toList());
    }

}
