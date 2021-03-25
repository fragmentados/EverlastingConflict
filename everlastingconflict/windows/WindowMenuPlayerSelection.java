package everlastingconflict.windows;

import everlastingconflict.ai.AI;
import everlastingconflict.elementosvisuales.BotonSimple;
import everlastingconflict.elementosvisuales.ComboBox;
import everlastingconflict.elementosvisuales.ComboBoxOption;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.races.enums.SubRaceEnum;
import everlastingconflict.victory.GameModeEnum;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WindowMenuPlayerSelection extends WindowMenuBasic {

    public BotonSimple volver, aceptar, salir;
    public static ComboBox gameModeCombo;
    public List<PlayerSelection> playerSelections = new ArrayList<>();
    private ComboBox mapCombo;
    public boolean start;

    public static boolean isLeaderGame() {
        return GameModeEnum.LEADER.label.equals(WindowMenuPlayerSelection.gameModeCombo.optionSelected.text);
    }

    public static boolean isJuggernautGame() {
        return GameModeEnum.JUGGERNAUT.label.equals(WindowMenuPlayerSelection.gameModeCombo.optionSelected.text);
    }

    public PlayerSelection getMainPlayerSelection() {
        return this.playerSelections.stream().filter(ps -> ps.isMainPlayer).findFirst().orElse(null);
    }

    public void start(GameContainer container) {
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
            WindowMain.windowSwitch(container, game, "Combat");
        } catch (SlickException ex) {
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        playerSelections = new ArrayList<>();
        playerSelections.add(new PlayerSelection(WindowCombat.responsiveX(20), WindowCombat.responsiveY(50), true));
        playerSelections.add(new PlayerSelection(WindowCombat.responsiveX(20), WindowCombat.responsiveY(55),
                false));
        playerSelections.get(1).teamCombo.optionSelected = playerSelections.get(1).teamCombo.options.get(1);
        salir = new BotonSimple("Salir", WindowCombat.VIEWPORT_SIZE_WIDTH - 60, 0);
        volver = new BotonSimple("Volver", WindowCombat.responsiveX(40), WindowCombat.responsiveY(85));
        aceptar = new BotonSimple("Aceptar", WindowCombat.responsiveX(45), WindowCombat.responsiveY(85));
        mapCombo = new ComboBox("Mapa:", Arrays.stream(MapEnum.values()).map(m -> new ComboBoxOption(m.getName(),
                "Jugadores máximos : " + m.getMaxPlayers())).collect(Collectors.toList()),
                WindowCombat.responsiveX(25), WindowCombat.responsiveY(40));
        gameModeCombo = new ComboBox("Condición de victoria:",
                Arrays.stream(GameModeEnum.values()).map(v -> new ComboBoxOption(v.label, v.description)).collect(Collectors.toList()), WindowCombat.responsiveX(55), WindowCombat.responsiveY(40));
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        super.update(container, delta);
        Input input = container.getInput();
        if (start) {
            start(container);
            start = false;
        }
        //Boton izquierdo
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (aceptar.isHovered(input.getMouseX(), input.getMouseY())) {
                start = true;
            } else if (salir.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.exit(container);
            } else if (volver.isHovered(input.getMouseX(), input.getMouseY())) {
                WindowMain.windowSwitch(container, null, "Menu");
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
        mapCombo.render(input, g);
        gameModeCombo.render(input, g);
        for (int i = (playerSelections.size() - 1); i >= 0; i--) {
            playerSelections.get(i).render(input, g);
        }
        salir.render(g);
        volver.render(g);
        aceptar.render(g);
        g.drawString(WindowMenuBasic.usuario.getText(), WindowCombat.responsiveX(20), WindowCombat.responsiveY(50));
        for (int i = 0; i < playerSelections.size(); i++) {
            g.drawString("Jugador nº " + (i + 1), WindowCombat.responsiveX(10),
                    WindowCombat.responsiveY(50 + 5 * i));
        }
        mapCombo.renderHoveredDescription(input, g);
        gameModeCombo.renderHoveredDescription(input, g);
        playerSelections.stream().forEach(ps -> ps.renderHoveredDescription(input, g));
    }

    private void test() {
        /*String previousRacePlayer1 = raceTutorial.checkOptionSelected(input.getMouseX(), input.getMouseY());
        if (previousRacePlayer1 != null) {
            racePlayer2.opciones.remove(raceTutorial.opcion_seleccionada);
            if (racePlayer2.opcion_seleccionada.equals(raceTutorial.opcion_seleccionada)) {
                racePlayer2.opcion_seleccionada = racePlayer2.opciones.get(0);
            }
            racePlayer2.opciones.add(previousRacePlayer1);
            racePlayer2.opciones = RaceNameEnum.sortRaceNames(racePlayer2.opciones);
        }
        String previousRacePlayer2 = racePlayer2.checkOptionSelected(input.getMouseX(), input.getMouseY());
        if (previousRacePlayer2 != null) {
            raceTutorial.opciones.remove(racePlayer2.opcion_seleccionada);
            if (raceTutorial.opcion_seleccionada.equals(racePlayer2.opcion_seleccionada)) {
                raceTutorial.opcion_seleccionada = raceTutorial.opciones.get(0);
            }
            raceTutorial.opciones.add(previousRacePlayer2);
            raceTutorial.opciones = RaceNameEnum.sortRaceNames(raceTutorial.opciones);
        }*/
    }
}
