package everlastingconflict.windows;

import everlastingconflict.ai.AI;
import everlastingconflict.elementosvisuales.BotonSimple;
import everlastingconflict.elementosvisuales.ComboBox;
import everlastingconflict.elementosvisuales.ComboBoxOption;
import everlastingconflict.elements.util.ElementosComunes;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.races.enums.SubRaceEnum;
import everlastingconflict.victory.GameModeEnum;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WindowMenuPlayerSelection extends WindowMenuBasic {

    public BotonSimple volver, aceptar, salir;
    public static ComboBox gameModeCombo;
    public List<PlayerSelection> playerSelections = new ArrayList<>();
    private ComboBox mapCombo;
    public TextField userTextField;
    private final float mapPreviewSize = 200f;
    private final float playerStartPreviewSize = 200f;
    private final float resourcePreviewSize = 150f;

    public static boolean isLeaderGame() {
        return GameModeEnum.LEADER.label.equals(WindowMenuPlayerSelection.gameModeCombo.optionSelected.text);
    }

    public static boolean isJuggernautGame() {
        return GameModeEnum.JUGGERNAUT.label.equals(WindowMenuPlayerSelection.gameModeCombo.optionSelected.text);
    }

    public PlayerSelection getMainPlayerSelection() {
        return this.playerSelections.stream().filter(ps -> ps.isMainPlayer).findFirst().orElse(null);
    }

    @Override
    public void startGame(GameContainer container) {
        try {
            Jugador mainPlayer = new Jugador("Prueba",
                    RaceEnum.findByName(getMainPlayerSelection().raceCombo.optionSelected.text),
                    SubRaceEnum.findByName(getMainPlayerSelection().subRaceCombo.optionSelected.text),
                    Integer.valueOf(getMainPlayerSelection().teamCombo.optionSelected.text),
                    isLeaderGame() && "Sí".equals(this.getMainPlayerSelection().leaderCombo.optionSelected),
                    isJuggernautGame() && this.getMainPlayerSelection().isJuggernaut());
            List<Jugador> players = playerSelections
                    .stream().filter(p -> p.isPlayerActive() && !p.isMainPlayer)
                    .map(p -> AI.crearAI(RaceEnum.findByName(p.raceCombo.optionSelected.text),
                            SubRaceEnum.findByName(p.subRaceCombo.optionSelected.text),
                            Integer.valueOf(p.teamCombo.optionSelected.text), p.difficultyCombo
                                    .optionSelected.text, isLeaderGame() && p.isLeader(),
                            isJuggernautGame() && p.isJuggernaut()))
                    .collect(Collectors.toList());
            players.add(0, mainPlayer);
            Game game = new Game(players, MapEnum.findByName(mapCombo.optionSelected.text),
                    GameModeEnum.findByLabel(WindowMenuPlayerSelection.gameModeCombo.optionSelected.text));
            WindowCombat.game = game;
            WindowMain.windowSwitch(container, "Combat", "PlayerSelection");
        } catch (SlickException ex) {
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        super.init(container);
        userTextField = new TextField(container, container.getDefaultFont(), (int) WindowCombat.responsiveX(20),
                (int) WindowCombat.responsiveY(50), 100, 20);
        playerSelections = new ArrayList<>();
        playerSelections.add(new PlayerSelection(WindowCombat.responsiveX(20), WindowCombat.responsiveY(50), true));
        playerSelections.add(new PlayerSelection(WindowCombat.responsiveX(20), WindowCombat.responsiveY(55),
                false));
        playerSelections.get(1).teamCombo.optionSelected = playerSelections.get(1).teamCombo.options.get(1);
        playerSelections.get(1).raceCombo.optionSelected = playerSelections.get(1).raceCombo.options.get(1);
        playerSelections.get(1).subRaceCombo.options =
                SubRaceEnum.findByRace(RaceEnum.ETERNIUM.getName()).stream().map(sr -> new ComboBoxOption(sr.getName(),
                        sr.getDescription(),
                        sr.getImagePath())).collect(Collectors.toList());
        playerSelections.get(1).subRaceCombo.initProportionsBasedOnOptions();
        playerSelections.get(1).subRaceCombo.optionSelected = playerSelections.get(1).subRaceCombo.options.get(0);
        salir = new BotonSimple("Salir", WindowCombat.VIEWPORT_SIZE_WIDTH - "Salir".length() * 10, 0);
        volver = new BotonSimple("Volver", WindowCombat.responsiveX(40), WindowCombat.responsiveY(85));
        aceptar = new BotonSimple("Aceptar", WindowCombat.responsiveX(45), WindowCombat.responsiveY(85));
        mapCombo = new ComboBox("Mapa:", Arrays.stream(MapEnum.values()).map(m -> new ComboBoxOption(m.getName(),
                "Jugadores máximos : " + m.getMaxPlayers())).collect(Collectors.toList()),
                WindowCombat.responsiveX(25), WindowCombat.responsiveY(40));
        gameModeCombo = new ComboBox("Condición de victoria:",
                Arrays.stream(GameModeEnum.values()).map(v -> new ComboBoxOption(v.label, v.description)).collect(Collectors.toList()), WindowCombat.responsiveX(55), WindowCombat.responsiveY(40));
        WindowMenuBasic.subTitle = "COMBATE";
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        super.update(container, delta);
        Input input = container.getInput();
        //Boton izquierdo
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (aceptar.isHovered(input.getMouseX(), input.getMouseY())) {
                startGame(container);
            } else if (salir.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.exit(container);
            } else if (volver.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.windowSwitch(container, "Menu");
            }
            mapCombo.checkIfItsClicked(input);
            gameModeCombo.checkIfItsClicked(input);
            for (PlayerSelection ps : playerSelections) {
                ps.update(input, playerSelections);
            }
            gameModeCombo.checkOptionSelected(input.getMouseX(), input.getMouseY());
            if (mapCombo.checkOptionSelected(input.getMouseX(), input.getMouseY()) != null) {
                updatePlayerCountBasedOnMapSize();
            }
        }
    }

    private void updatePlayerCountBasedOnMapSize() {
        int newMaxPlayers = MapEnum.findByName(mapCombo.optionSelected.text).maxPlayers;
        if (newMaxPlayers > playerSelections.size()) {
            int playersToAdd = newMaxPlayers - playerSelections.size();
            for (int i = 0; i < playersToAdd; i++) {
                playerSelections.add(new PlayerSelection(WindowCombat.responsiveX(20),
                        WindowCombat.responsiveY(50 + 5 * playerSelections.size()), false));
            }
        } else if (newMaxPlayers < playerSelections.size()) {
            for (int i = (playerSelections.size() - 1); i >= newMaxPlayers; i--) {
                playerSelections.remove(i);
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        super.render(container, g);
        Input input = container.getInput();
        mapCombo.render(g);
        gameModeCombo.render(g);
        userTextField.render(container, g);
        for (int i = (playerSelections.size() - 1); i >= 0; i--) {
            playerSelections.get(i).render(input, g);
        }
        salir.render(g);
        volver.render(g);
        aceptar.render(g);
        for (int i = 0; i < playerSelections.size(); i++) {
            g.drawString("Jugador nº " + (i + 1), WindowCombat.responsiveX(10),
                    WindowCombat.responsiveY(50 + 5 * i));
        }
        mapCombo.renderHoveredDescription(input, g);
        renderMapPreview(g);
        gameModeCombo.renderHoveredDescription(input, g);
        playerSelections.stream().forEach(ps -> ps.renderHoveredDescription(input, g));
    }

    public MapEnum getCurrentMap() {
        return MapEnum.findByName(this.mapCombo.optionSelected.text);
    }

    public boolean existsPlayerWithRace(RaceEnum race) {
        return this.playerSelections.stream().anyMatch(ps -> RaceEnum.findByName(ps.raceCombo.optionSelected.text).equals(race));
    }

    public void renderMapPreview(Graphics g) {
        g.setColor(ElementosComunes.UI_COLOR);
        g.fillRect(WindowCombat.responsiveX(25), WindowCombat.responsiveY(15), mapPreviewSize, mapPreviewSize);
        g.setColor(Color.white);
        g.drawRect(WindowCombat.responsiveX(25), WindowCombat.responsiveY(15), mapPreviewSize, mapPreviewSize);
        MapEnum map = getCurrentMap();
        float playerPreviewSizeScaled = mapPreviewSize * (playerStartPreviewSize / map.width);
        renderElementPreview(g, Color.green, 200, 200, playerPreviewSizeScaled);
        renderElementPreview(g, Color.green, (map.width - 200), (map.height - 200), playerPreviewSizeScaled);
        if (map.maxPlayers >= 3) {
            renderElementPreview(g, Color.green, (map.width - 200), 200, playerPreviewSizeScaled);
        }
        if (map.maxPlayers >= 4) {
            renderElementPreview(g, Color.green, 200, (map.height - 200), playerPreviewSizeScaled);
        }
        if (map.maxPlayers >= 5) {
            renderElementPreview(g, Color.green, 200, map.height / 2, playerPreviewSizeScaled);
        }
        if (map.maxPlayers >= 6) {
            renderElementPreview(g, Color.green, (map.width - 200), map.height / 2, playerPreviewSizeScaled);
        }
        float resourcePreviewSizeScaled = mapPreviewSize * (resourcePreviewSize / map.width);
        renderResourcePreviews(g, resourcePreviewSizeScaled);
    }

    private void renderResourcePreviews(Graphics g, float playerPreviewSizeScaled) {
        float sextox = getCurrentMap().width / 6;
        float sextoy = getCurrentMap().height / 6;
        float visionTowerOffset = 120;
        // Torres de vigilancia
        renderElementPreview(g, Color.gray, sextox, sextoy * 2,
                playerPreviewSizeScaled);
        renderElementPreview(g, Color.gray, sextox, sextoy * 4,
                playerPreviewSizeScaled);
        renderElementPreview(g, Color.gray, sextox * 5, sextoy * 2,
                playerPreviewSizeScaled);
        renderElementPreview(g, Color.gray, sextox * 5, sextoy * 4,
                playerPreviewSizeScaled);
        renderElementPreview(g, Color.gray, sextox * 3, sextoy * 3,
                playerPreviewSizeScaled);
        if (existsPlayerWithRace(RaceEnum.CLARK) || existsPlayerWithRace(RaceEnum.MAESTROS)) {
            renderElementPreview(g, RaceEnum.CLARK.getColor(), sextox * 2, sextoy, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.CLARK.getColor(), sextox * 4, sextoy, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.CLARK.getColor(), sextox * 2, sextoy * 5, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.CLARK.getColor(), sextox * 4, sextoy * 5, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.CLARK.getColor(), sextox * 2, sextoy * 2, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.CLARK.getColor(), sextox * 4, sextoy * 2, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.CLARK.getColor(), sextox * 2, sextoy * 4, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.CLARK.getColor(), sextox * 4, sextoy * 4, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.CLARK.getColor(), sextox * 3, sextoy * 2, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.CLARK.getColor(), sextox * 3, sextoy * 4, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.CLARK.getColor(), sextox * 2, sextoy * 3, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.CLARK.getColor(), sextox * 4, sextoy * 3, playerPreviewSizeScaled);
        }
        if (existsPlayerWithRace(RaceEnum.ETERNIUM)) {
            renderElementPreview(g, RaceEnum.ETERNIUM.getColor(), sextox - visionTowerOffset, sextoy * 2,
                    playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.ETERNIUM.getColor(), sextox * 5 + visionTowerOffset, sextoy * 2,
                    playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.ETERNIUM.getColor(), sextox - visionTowerOffset, sextoy * 4,
                    playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.ETERNIUM.getColor(), sextox * 5 + visionTowerOffset, sextoy * 4,
                    playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.ETERNIUM.getColor(), sextox * 2, sextoy * 2, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.ETERNIUM.getColor(), sextox * 4, sextoy * 2, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.ETERNIUM.getColor(), sextox * 2, sextoy * 4, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.ETERNIUM.getColor(), sextox * 4, sextoy * 4, playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.ETERNIUM.getColor(), sextox * 3, sextoy * 3 - visionTowerOffset,
                    playerPreviewSizeScaled);
        }
        if (existsPlayerWithRace(RaceEnum.FENIX)) {
            renderElementPreview(g, RaceEnum.FENIX.getColor(), sextox, sextoy,
                    playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.FENIX.getColor(), sextox * 3, sextoy,
                    playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.FENIX.getColor(), sextox * 5, sextoy,
                    playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.FENIX.getColor(), sextox, sextoy * 3,
                    playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.FENIX.getColor(), sextox * 3, sextoy * 3 + visionTowerOffset,
                    playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.FENIX.getColor(), sextox * 5, sextoy * 3,
                    playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.FENIX.getColor(), sextox, sextoy * 5,
                    playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.FENIX.getColor(), sextox * 3, sextoy * 5,
                    playerPreviewSizeScaled);
            renderElementPreview(g, RaceEnum.FENIX.getColor(), sextox * 5, sextoy * 5,
                    playerPreviewSizeScaled);
        }
    }

    public void renderElementPreview(Graphics g, Color color, float playerStartX, float playerStartY,
                                     float playerPreviewSizeScaled) {
        MapEnum map = getCurrentMap();
        g.setColor(color);
        float x =
                WindowCombat.responsiveX(25) + ((playerStartX - playerPreviewSizeScaled / 2) / map.width) * (mapPreviewSize - playerPreviewSizeScaled);
        float y =
                WindowCombat.responsiveY(15) + ((playerStartY - playerPreviewSizeScaled / 2) / map.height) * (mapPreviewSize - playerPreviewSizeScaled);
        g.fillRect(x, y, playerPreviewSizeScaled, playerPreviewSizeScaled);
    }
}
