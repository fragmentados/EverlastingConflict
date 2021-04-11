/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.gestion;

import everlastingconflict.elements.ElementoComplejo;
import everlastingconflict.elements.ElementoCoordenadas;
import everlastingconflict.elements.ElementoVulnerable;
import everlastingconflict.elements.impl.*;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.victory.GameModeEnum;
import everlastingconflict.watches.RelojAlianza;
import everlastingconflict.watches.RelojEternium;
import everlastingconflict.watches.RelojMaestros;
import everlastingconflict.windows.MapEnum;
import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.util.*;
import java.util.stream.Collectors;

import static everlastingconflict.windows.WindowCombat.*;


public class Game {

    public List<Jugador> players = new ArrayList<>();
    public List<Recurso> recursos;
    public List<Bestias> bestias;
    public List<Proyectil> proyectiles;
    public MapEnum map;
    public GameModeEnum gameMode = GameModeEnum.ANHILATION;
    public boolean isShadowOfWarEnabled = true;

    public static String formatTextToFitWidth(String texto, float anchura) {
        String resultText = "";
        String initialText = texto;
        String currentLine, remainingText;
        int fin = texto.length() / ((int) anchura / 9);
        if (fin == 0) {
            resultText = initialText;
        }
        for (int i = 0; i < fin; i++) {
            currentLine = initialText.substring(0, ((int) anchura / 9)).trim() + "\n";
            remainingText = initialText.substring(((int) anchura / 9));
            resultText += currentLine;
            if (i == (fin - 1)) {
                resultText += remainingText.trim();
            } else {
                initialText = remainingText;
            }
        }
        return resultText;
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
        return this.players.stream().filter(p -> p.raza.equals(race)).findFirst().orElse(null);
    }

    public Jugador getPlayerByName(String name) {
        return this.players.stream().filter(p -> p.nombre.equals(name)).findFirst().orElse(null);
    }

    public boolean existsPlayerWithRace(RaceEnum race) {
        return this.players.stream().anyMatch(p -> race.equals(p.raza));
    }

    public void initElements() {
        //Representan la altura y anchura del mapa
        float width = map.getWidth();
        float height = map.getHeight();
        WindowCombat.WORLD_SIZE_X = width;
        WindowCombat.WORLD_SIZE_Y = height;
        WindowCombat.offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_WIDTH;
        WindowCombat.offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_HEIGHT;
        initPlayerCoordinates(width, height);
        initResources(width, height);
        // Inicializar relojes
        WindowCombat.initWatches();
        if (existsPlayerWithRace(RaceEnum.MAESTROS)) {
            WindowCombat.createWatch(new RelojMaestros(this.getPlayerByRace(RaceEnum.MAESTROS)));
        }
        if (existsPlayerWithRace(RaceEnum.ETERNIUM)) {
            WindowCombat.createWatch(new RelojEternium(this.getPlayerByRace(RaceEnum.ETERNIUM)));
        }
        if (existsPlayerWithRace(RaceEnum.ALIANZA)) {
            WindowCombat.createWatch(new RelojAlianza(this.getPlayerByRace(RaceEnum.ALIANZA)));
        }
        initPlayerColors();
        /*getMainPlayer().unidades.add(new Unidad(getMainPlayer(), "Despedazador", 400, 400));
        getMainPlayer().unidades.add(new Unidad(getMainPlayer(), "Despedazador", 400, 400));
        getMainPlayer().unidades.add(new Unidad(getMainPlayer(), "Despedazador", 400, 400));
        getMainPlayer().unidades.add(new Unidad(getMainPlayer(), "Despedazador", 400, 400));
        getMainPlayer().unidades.add(new Unidad(getMainPlayer(), "Despedazador", 400, 400));
        getMainPlayer().unidades.add(new Unidad(getMainPlayer(), "Despedazador", 400, 400));*/
        players.forEach(p -> p.initElements(this));
    }

    private void initResources(float width, float height) {
        float sextox = width / 6;
        float sextoy = height / 6;
        float visionTowerOffset = 120;
        if (existsPlayerWithRace(RaceEnum.CLARK) || existsPlayerWithRace(RaceEnum.MAESTROS)) {
            // Alphas
            bestias.add(new Bestias("Alphas", sextox * 2, sextoy));
            bestias.add(new Bestias("Alphas", sextox * 4, sextoy));
            bestias.add(new Bestias("Alphas", sextox * 2, sextoy * 5));
            bestias.add(new Bestias("Alphas", sextox * 4, sextoy * 5));
            // Betas
            bestias.add(new Bestias("Betas", sextox * 2, sextoy * 2));
            bestias.add(new Bestias("Betas", sextox * 4, sextoy * 2));
            bestias.add(new Bestias("Betas", sextox * 2, sextoy * 4));
            bestias.add(new Bestias("Betas", sextox * 4, sextoy * 4));
            // Gamma
            bestias.add(new Bestias("Gamma", sextox * 3, sextoy * 2));
            bestias.add(new Bestias("Gamma", sextox * 3, sextoy * 4));
            // Ommegas
            bestias.add(new Bestias("Ommega", sextox * 2, sextoy * 3));
            bestias.add(new Bestias("Ommega", sextox * 4, sextoy * 3));
        }
        if (existsPlayerWithRace(RaceEnum.ETERNIUM)) {
            recursos.add(new Recurso("Hierro", sextox - visionTowerOffset, sextoy * 2));
            recursos.add(new Recurso("Hierro", sextox * 5 + visionTowerOffset, sextoy * 2));
            recursos.add(new Recurso("Hierro", sextox - visionTowerOffset, sextoy * 4));
            recursos.add(new Recurso("Hierro", sextox * 5 + visionTowerOffset, sextoy * 4));
            recursos.add(new Recurso("Hierro", sextox * 2, sextoy * 2));
            recursos.add(new Recurso("Hierro", sextox * 4, sextoy * 2));
            recursos.add(new Recurso("Hierro", sextox * 2, sextoy * 4));
            recursos.add(new Recurso("Hierro", sextox * 4, sextoy * 4));
            recursos.add(new Recurso("Hierro", sextox * 3, sextoy * 3 - visionTowerOffset));
        }
        if (existsPlayerWithRace(RaceEnum.FENIX)) {
            recursos.add(new Recurso("Civiles", sextox, sextoy));
            recursos.add(new Recurso("Civiles", sextox * 3, sextoy));
            recursos.add(new Recurso("Civiles", sextox * 5, sextoy));
            recursos.add(new Recurso("Civiles", sextox, sextoy * 3));
            recursos.add(new Recurso("Civiles", sextox * 3, sextoy * 3 + visionTowerOffset));
            recursos.add(new Recurso("Civiles", sextox * 5, sextoy * 3));
            recursos.add(new Recurso("Civiles", sextox, sextoy * 5));
            recursos.add(new Recurso("Civiles", sextox * 3, sextoy * 5));
            recursos.add(new Recurso("Civiles", sextox * 5, sextoy * 5));
        }
        // Torres de vigilancia
        recursos.add(new TorreVision("Vision", sextox, sextoy * 2));
        recursos.add(new TorreVision("Vision", sextox, sextoy * 4));
        recursos.add(new TorreVision("Vision", sextox * 5, sextoy * 2));
        recursos.add(new TorreVision("Vision", sextox * 5, sextoy * 4));
        recursos.add(new TorreVision("Vision", sextox * 3, sextoy * 3));
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
        players.stream().filter(p -> !mainPlayer.equals(p) && p.team.equals(mainPlayer.team)).forEach(p -> p.color =
                Color.yellow);
        // Enemies
        players.stream().filter(p -> !p.team.equals(mainPlayer.team)).forEach(p -> p.color = Color.red);
    }

    public boolean checkMainTeamVictory() {
        return this.getNonMainTeam().stream().allMatch(p -> this.gameMode.victoryCondition.isDefeated(this, p));
    }

    public boolean checkMainTeamDefeat() {
        return this.gameMode.victoryCondition.isDefeated(this, getMainPlayer());
    }

    public void comportamiento_elementos(Graphics g, int delta) {
        try {
            for (Bestias b : bestias) {
                b.comportamiento(this, g, delta);
            }
            for (Proyectil p : proyectiles) {
                p.comportamiento(this, g, delta);
            }
        } catch (ConcurrentModificationException e) {
            //e.printStackTrace();
        }
        players.forEach(p -> p.comportamiento_elementos(this, g, delta));
    }

    public void renderElements(Graphics g, Input input) {
        for (Recurso r : recursos) {
            if (r.isVisibleByMainTeam(this)) {
                if (r.capturador == null) {
                    r.render(this, Color.blue, input, g);
                }
            }
        }
        for (Bestias b : bestias) {
            b.dibujar(this, Color.blue, input, g);
        }
        for (Proyectil p : proyectiles) {
            if (p.isVisibleByMainTeam(this)) {
                p.render(this, Color.blue, input, g);
            }
        }
        getMainTeam().forEach(p -> p.renderMainTeamElements(this, g, input));
        getNonMainTeam().forEach(p -> p.renderNonMainTeamElements(this, g, input));
    }

    public Game() {
        recursos = new ArrayList<>();
        bestias = new ArrayList<>();
        proyectiles = new ArrayList<>();
    }

    public Game(List<Jugador> players, MapEnum map) {
        this();
        this.players = players;
        this.players.get(0).isMainPlayer = true;
        this.map = map;
        this.initElements();
    }

    public Game(List<Jugador> players, MapEnum map, GameModeEnum gameMode) {
        this(players, map);
        this.gameMode = gameMode;
        if (GameModeEnum.JUGGERNAUT.equals(this.gameMode)) {
            initJuggernautData();
        }
    }

    public void initJuggernautData() {
        // Every player allies against the juggernaut
        Jugador juggernautPlayer = this.players.stream().filter(p -> p.isJuggernaut).findFirst().orElse(null);
        juggernautPlayer.team = 1;
        List<Jugador> nonJuggernautPlayers =
                this.players.stream().filter(p -> !p.isJuggernaut).collect(Collectors.toList());
        nonJuggernautPlayers.forEach(p -> p.team = 2);
        juggernautPlayer.applyJuggernautEnhancements(this, nonJuggernautPlayers.size());
    }

    public ElementoVulnerable getElementAttackedAtPosition(float x, float y, List<Unidad> seleccionadas) {
        Optional<Unidad> unitAttackedOp =
                getNonMainPlayers().stream().flatMap(p -> p.unidades.stream()).filter(u -> u.isVisibleByEnemy(this) && u.hitbox(x, y)).findFirst();
        if (!unitAttackedOp.isPresent()) {
            Optional<Edificio> buildingAttackedOp =
                    getNonMainPlayers().stream().flatMap(p -> p.edificios.stream()).filter(e -> e.isVisibleByEnemy(this) && e.hitbox(x, y)).findFirst();
            if (!buildingAttackedOp.isPresent()) {
                Optional<Recurso> resourceAttackedOp =
                        getNonMainPlayers().stream().flatMap(p -> p.lista_recursos.stream()).filter(r -> r.isVisibleByEnemy(this) && r.hitbox(x, y)).findFirst();
                if (!resourceAttackedOp.isPresent()) {
                    Optional<Bestia> beastAttackedOp =
                            this.bestias.stream().flatMap(be -> be.getContenido().stream()).filter(b -> b.isVisibleByEnemy(this) && b.hitbox(x, y)).findFirst();
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

    public List<Recurso> getCivilTownsAndVisionTowers() {
        return this.recursos.stream().filter(r -> (r.nombre.equals("Civiles") || r.nombre.equals("Vision"))).collect(Collectors.toList());
    }

    public List<Recurso> getVisionTowers() {
        return this.recursos.stream().filter(r -> r.nombre.equals("Vision")).collect(Collectors.toList());
    }

    public List<ElementoComplejo> getElementsAffectedByArea(ElementoComplejo origen, float x, float y,
                                                            HabilitySelectionType selectionType
            , float area) {
        List<ElementoComplejo> selectableElements = getSelectableElementsBasedOnSelectionType(origen, selectionType,
                x, y);
        selectableElements.removeIf(e -> !e.alcance(x, y, area));
        return selectableElements;
    }

    public List<ElementoComplejo> getSelectableElementsBasedOnSelectionType(ElementoComplejo origen,
                                                                            HabilitySelectionType selectionType,
                                                                            float x, float y) {
        Jugador aliado = getPlayerFromElement(origen);
        List<Jugador> enemies = getEnemyPlayersFromElement(origen);
        List<ElementoComplejo> selectableElements = new ArrayList<>();
        switch (selectionType) {
            case ALLY_BUILDING:
                selectableElements.addAll(aliado.edificios);
                break;
            case ENEMY_BUILDING:
                selectableElements.addAll(enemies.stream().flatMap(e -> e.edificios.stream()).collect(Collectors.toList()));
                break;
            case ENEMY_UNIT:
                selectableElements.addAll(enemies.stream().flatMap(e -> e.unidades.stream()).collect(Collectors.toList()));
                selectableElements.addAll(bestias.stream().flatMap(be -> be.contenido.stream()).collect(Collectors.toList()));
                break;
            case ALLY_UNIT:
                selectableElements.addAll(aliado.unidades);
                break;
            case ANY_UNIT:
                selectableElements.addAll(aliado.unidades);
                selectableElements.addAll(enemies.stream().flatMap(e -> e.unidades.stream()).collect(Collectors.toList()));
                selectableElements.addAll(bestias.stream().flatMap(be -> be.contenido.stream()).collect(Collectors.toList()));
                break;
            case CAPTURED_CITY:
                selectableElements.addAll(aliado.lista_recursos.stream().filter(r -> "Civiles".equals(r.nombre)).collect(Collectors.toList()));
                break;
            case SUMMON:
                selectableElements.addAll(aliado.unidades);
                selectableElements.removeIf(e -> e instanceof Manipulador);
                break;
            case BEAST:
                for (Bestias be : bestias) {
                    selectableElements.addAll(be.contenido);
                }
                break;
            case COORDINATES:
                selectableElements.add(new Unidad(null, "No hay", x, y));
                break;
        }
        return selectableElements;
    }

}
