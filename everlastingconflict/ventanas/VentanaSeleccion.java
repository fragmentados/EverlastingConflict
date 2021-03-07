package everlastingconflict.ventanas;

import everlastingconflict.ai.AI;
import everlastingconflict.elementosvisuales.BotonSimple;
import everlastingconflict.elementosvisuales.ComboBox;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceEnum;
import everlastingconflict.victory.VictoryCondition;
import everlastingconflict.victory.VictoryConditionEnum;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VentanaSeleccion extends Ventana {

    private final String titulo = "THE EVERLASTING CONFLICT";
    public BotonSimple volver, aceptar, salir;
    public ComboBox mainPlayerRace;
    public ComboBox mainPlayerTeam;
    public ComboBox mainPlayerLeader;
    public static ComboBox victoryCondition;
    public List<PlayerSelection> playerSelections;
    private ComboBox mapCombo;
    public boolean start;

    public static boolean isLeaderGame() {
        return "Jugador Lider".equals(VentanaSeleccion.victoryCondition.opcion_seleccionada);
    }

    public void start(GameContainer container) {
        try {
            Jugador mainPlayer = new Jugador("Prueba", mainPlayerRace.opcion_seleccionada,
                    Integer.valueOf(mainPlayerTeam.opcion_seleccionada),
                    isLeaderGame() && "Sí".equals(this.mainPlayerLeader.opcion_seleccionada));
            List<Jugador> players = playerSelections
                    .stream().filter(p -> p.isPlayerActive())
                    .map(p -> AI.crearAI(p.raceCombo.opcion_seleccionada,
                            Integer.valueOf(p.teamCombo.opcion_seleccionada), p.difficultyCombo
                            .opcion_seleccionada, isLeaderGame() && p.isLeader()))
                    .collect(Collectors.toList());
            players.add(0, mainPlayer);
            Partida partida = new Partida(players, MapEnum.findByName(mapCombo.opcion_seleccionada),
                    VictoryCondition.getFromName(VentanaSeleccion.victoryCondition.opcion_seleccionada));
            VentanaPrincipal.windowSwitch(container, partida, "Campo");
        } catch (SlickException ex) {
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        mainPlayerRace = new ComboBox("Raza:", RaceEnum.getAllNames(), 600, 500);
        mainPlayerTeam = new ComboBox("Equipo:", IntStream.rangeClosed(1, 4)
                .boxed().map(n -> n.toString()).collect(Collectors.toList()), 800, 500);
        mainPlayerLeader = new ComboBox("Lider:", Arrays.asList("Sí", "No"), 900, 500);
        playerSelections = new ArrayList<>();
        playerSelections.add(new PlayerSelection(400, 550));
        salir = new BotonSimple("Salir", VentanaCombate.VIEWPORT_SIZE_WIDTH - 60, 0);
        volver = new BotonSimple("Volver", 600, 800);
        aceptar = new BotonSimple("Aceptar", 700, 800);
        mapCombo = new ComboBox("Mapa:", MapEnum.getAllNames(), MapEnum.getAllPlayerLimits(), 500, 400);
        victoryCondition = new ComboBox("Condición de victoria:", VictoryConditionEnum.getAllNames(),
                VictoryConditionEnum.getAllDescriptions(), 500, 300);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
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
                VentanaPrincipal.exit(container);
            } else if (volver.isHovered(input.getMouseX(), input.getMouseY())) {
                VentanaPrincipal.windowSwitch(container, null, "Menu");
            }
            mainPlayerRace.checkIfItsClicked(input);
            mapCombo.checkIfItsClicked(input);
            mainPlayerTeam.checkIfItsClicked(input);
            victoryCondition.checkIfItsClicked(input);
            playerSelections.stream().forEach(p -> p.update(input));
            mainPlayerRace.checkOptionSelected(input.getMouseX(), input.getMouseY());
            mainPlayerTeam.checkOptionSelected(input.getMouseX(), input.getMouseY());
            victoryCondition.checkOptionSelected(input.getMouseX(), input.getMouseY());
            if (mapCombo.checkOptionSelected(input.getMouseX(), input.getMouseY()) != null) {
                updatePlayerCountBasedOnMapSize();
            }
        }
    }

    private void updatePlayerCountBasedOnMapSize() {
        int newMaxPlayers = MapEnum.findByName(mapCombo.opcion_seleccionada).maxPlayers;
        if (newMaxPlayers > playerSelections.size()) {
            int playersToAdd = newMaxPlayers - (playerSelections.size() + 1);
            for (int i = 0; i < playersToAdd; i++) {
                playerSelections.add(new PlayerSelection(400, 500 + 50 * (playerSelections.size() + 1)));
            }
        } else if (newMaxPlayers < playerSelections.size()) {
            for (int i = (playerSelections.size() - 1); i >= (newMaxPlayers - 1); i--) {
                playerSelections.remove(i);
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.drawString(titulo, (VentanaCombate.VIEWPORT_SIZE_WIDTH - (titulo.length() * 10)) / 2, 0);
        mapCombo.render(g);
        victoryCondition.render(g);
        for (int i = (playerSelections.size() - 1); i >= 0; i--) {
            playerSelections.get(i).render(g);
        }
        mainPlayerRace.render(g);
        mainPlayerTeam.render(g);
        if (isLeaderGame()) {
            mainPlayerLeader.render(g);
        }
        salir.render(g);
        volver.render(g);
        aceptar.render(g);
        g.drawString(VentanaMenu.usuario.getText(), 400, 500);
        g.drawString("Usuario: ", 20, 50);
        VentanaMenu.usuario.render(container, g);
        g.drawString("Jugador nº 1", 200, 500);
        for (int i = 0; i < playerSelections.size(); i++) {
            g.drawString("Jugador nº " + (i + 2), 200, 500 + 50 * (i + 1));
        }
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
