/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.windows;

import everlastingconflict.ControlGroup;
import everlastingconflict.RTS;
import everlastingconflict.campaign.tutorial.GuidedGame;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elementosvisuales.BotonSimple;
import everlastingconflict.elements.*;
import everlastingconflict.elements.impl.*;
import everlastingconflict.elements.util.ElementosComunes;
import everlastingconflict.gestion.Evento;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.Clark;
import everlastingconflict.races.Fusion;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.watches.Reloj;
import everlastingconflict.watches.RelojAlianza;
import everlastingconflict.watches.RelojEternium;
import everlastingconflict.watches.RelojMaestros;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static everlastingconflict.elements.util.ElementosComunes.FULL_VISIBLE_COLOR;
import static everlastingconflict.elements.util.ElementosComunes.HALF_VISIBLE_COLOR;
import static everlastingconflict.watches.Reloj.TIME_REGULAR_SPEED;
import static everlastingconflict.windows.UI.UI_HEIGHT;
import static everlastingconflict.windows.UI.anchura_miniatura;


public class WindowCombat extends Window {

    private static final int HOLDING_THRESHOLD = 10;
    public static GameContainer container;
    public static Game game;
    public static UI ui;
    public static boolean ctrl, deseleccionar;
    public static boolean mayus;
    public static boolean pauseMenuEnabled;
    public static List<BotonSimple> pauseMenuButtons = new ArrayList<>();
    //Atacar
    public static boolean attackMoveModeEnabled;
    public Image atacar;
    //Construccion
    public static Edificio edificio;
    public static ElementoAtacante constructor;
    public Image construccion;
    //Scroll Mapa
    public int scrollspeed = 1;
    public static float playerX = 0;
    public static float playerY = 0;
    public static float WORLD_SIZE_WIDTH;
    public static float WORLD_SIZE_HEIGHT;
    public static float VIEWPORT_SIZE_WIDTH;
    public static float VIEWPORT_SIZE_HEIGHT;
    public static float offsetMinX = 0;
    public static float offsetMinY = 0;
    public static float offsetMaxX;
    public static float offsetMaxY;
    //Click
    public static boolean click;
    public static int x_click, y_click;
    public static int xStartHolding, yStartHolding;
    public static boolean holdingLeftClick = false;
    //Grupos de control
    public static List<ControlGroup> controlGroups = new ArrayList<>();
    //Mensajes
    private List<Mensaje> mensajes;
    //RTS.Relojes
    public static List<Reloj> relojes = new ArrayList<>();
    //Habilidades
    public static ElementoComplejo elemento_habilidad;
    public static Habilidad habilidad;
    //Tutorial y Fin de Partida
    public static BotonSimple continuar = new BotonSimple("Continuar");
    public static BotonComplejo selectMainElementButton;
    //Movimiento pantalla
    public float x_movimiento = -1, y_movimiento = -1;
    //Animaciones
    public ElementoVulnerable elementCircle = null;
    private float timeCircle = 1f, timeCircleCounter = 0;
    public static Animation victoria, derrota;
    //Zoom
    public float zoomLevel = 1f;
    public static final float zoomStep = 0.0625f;
    public static ElementoCoordenadas elementHighlighted = null;
    public static int highlightRadius = 50;
    //Music
    public static List<Sound> LOCAL_BSOS;
    public static Sound CURRENT_BSO;
    public static Sound CURRENT_NARRATION;

    public static void initWatches() {
        relojes = new ArrayList<>();
    }

    public static void createWatch(Reloj r) {
        relojes.add(r);
    }

    public static RelojEternium eterniumWatch() {
        Reloj relojEncontrado = relojes.stream().filter(r -> r instanceof RelojEternium).findFirst().orElse(null);
        if (relojEncontrado != null) {
            return (RelojEternium) relojEncontrado;
        }
        return null;
    }

    public static RelojAlianza alianceWatch(Edificio ship) {
        Reloj watchFound =
                relojes.stream().filter(r -> r instanceof RelojAlianza && ((RelojAlianza) r).shipAssociated.equals(ship)).findFirst().orElse(null);
        if (watchFound != null) {
            return (RelojAlianza) watchFound;
        }
        return null;
    }

    public static RelojMaestros masterWatch() {
        Reloj relojEncontrado = relojes.stream().filter(r -> r instanceof RelojMaestros).findFirst().orElse(null);
        if (relojEncontrado != null) {
            return (RelojMaestros) relojEncontrado;
        }
        return null;
    }

    public Vector2f getMousePos(Input input) {
        return new Vector2f(playerX + input.getMouseX(), playerY + input.getMouseY());
    }

    public void addZoomLevel(float a) {
        zoomLevel += a;
        if (zoomLevel > 1.2375f) {
            zoomLevel = 1.2375f;
        }
        if (zoomLevel < .1f) {
            zoomLevel = .1f;
        }
    }

    public void mouseWheelZoom(Input input) {
        int mouseWheel = Mouse.getDWheel();
        if (mouseWheel == 0) {
            return;
        }
        if (mouseWheel != 0) {
            Vector2f mousePosAbs = getMousePos(input);
            Vector2f mousePosRel = mousePosAbs.copy().scale(1 / zoomLevel).add(new Vector2f(playerX, playerY));
            if (mouseWheel > 0) {
                addZoomLevel(zoomStep);
            } else {
                addZoomLevel(-zoomStep);
            }
            Vector2f result = mousePosRel.sub(mousePosAbs.scale(1 / zoomLevel));
            playerX = result.x;
            playerY = result.y;
        }
    }

    public void movimiento_pantalla(float x, float y) {
        x_movimiento = x;
        y_movimiento = y;
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        WindowCombat.container = container;
        playerX = playerY = 0;
        pauseMenuEnabled = false;
        container.setShowFPS(false);
        container.setVSync(true);
        if (!(game instanceof GuidedGame)) {
            continuar.canBeUsed = false;
        }
        ctrl = attackMoveModeEnabled = mayus = click = false;
        deseleccionar = true;
        atacar = new Image("media/Cursores/atacar.png");
        construccion = new Image("media/Edificios/construccion.png");
        mensajes = new ArrayList<>();
        ui = new UI(game);
        pauseMenuButtons = new ArrayList<>();
        pauseMenuButtons.add(new BotonSimple("Reanudar") {
            @Override
            public void effect() throws SlickException {
                pauseMenuEnabled = false;
            }
        });
        pauseMenuButtons.add(new BotonSimple("Volver al Menú") {
            @Override
            public void effect() throws SlickException {
                endGameAndReturnToPreviousWindow();
            }
        });
        pauseMenuButtons.add(new BotonSimple("Salir del Juego") {
            @Override
            public void effect() throws SlickException {
                WindowMain.exit(container);
            }
        });
        if (!(game instanceof GuidedGame)) {
            LOCAL_BSOS = new ArrayList<>();
            if (CURRENT_BSO != null) {
                CURRENT_BSO.stop();
            }
            CURRENT_BSO = null;
        }

        selectMainElementButton = new BotonComplejo(ElementosComunes.getMainElementImage(game.getMainPlayer()));
        selectMainElementButton.tecla = Input.KEY_F1;
        selectMainElementButton.tecla_string = "F1";
        container.getInput().addMouseListener(new LeftClickMouseListener());
    }

    public void coordenadas_errores() {
        for (int i = (mensajes.size() - 1); i >= 0; i--) {
            if (mensajes.get(i).error) {
                mensajes.get(i).x =
                        WindowCombat.playerX + WindowCombat.VIEWPORT_SIZE_WIDTH / 2 - (mensajes.get(i).mensaje.length() * 10) / 2;
                mensajes.get(i).y =
                        WindowCombat.playerY + WindowCombat.VIEWPORT_SIZE_HEIGHT - 20 * (mensajes.size() - i);
            }
        }
    }

    public void anadir_mensaje(Mensaje m) {
        mensajes.add(m);
    }

    public void dibujar_edificio(Graphics g, Edificio edificio) {
        int anchura = edificio.animation.getWidth();
        int altura = edificio.animation.getHeight();
        float posx, posy;
        posx = edificio.x - anchura / 2;
        posy = edificio.y - altura / 2;
        if (edificio.vida == 0) {
            g.setColor(new Color(0f, 1f, 0f, 0.8f));
            g.fillRect(posx, posy, anchura, altura);
            edificio.animation.draw(posx, posy);
        }
    }

    public void dibujar_preview_edificio(Graphics g, Edificio edificio, Input input) {
        int anchura = edificio.animation.getWidth();
        int altura = edificio.animation.getHeight();
        //Dibujar edificio construcción                
        float posx, posy;
        posx = playerX + input.getMouseX() - anchura / 2;
        posy = playerY + input.getMouseY() - altura / 2;

        if (constructor instanceof Edificio) {
            Ellipse2D circulo = new Ellipse2D.Float(constructor.x - ((Edificio) constructor).radio_construccion / 2,
                    constructor.y - ((Edificio) constructor).radio_construccion / 2,
                    ((Edificio) constructor).radio_construccion, ((Edificio) constructor).radio_construccion);
            Rectangle re = new Rectangle((int) posx, (int) posy, anchura, altura);
            if (!circulo.contains(re)) {
                g.setColor(Color.red);
            } else {
                g.setColor(new Color(0f, 1f, 0f, 0.8f));
            }
        } else {
            if (edificio.nombre.equals("Refinería") || edificio.nombre.equals("Centro de restauración")) {
                g.setColor(Color.red);
            } else {
                g.setColor(new Color(0f, 1f, 0f, 0.8f));
            }
        }
        g.fillRect(posx, posy, anchura, altura);
        g.setColor(Color.red);
        List<Rectangle2D> intersecciones = edificio.obtener_intersecciones(game, playerX + input.getMouseX(),
                playerY + input.getMouseY());
        if (edificio.nombre.equals("Refinería") || edificio.nombre.equals("Centro de restauración")) {
            if (!intersecciones.isEmpty()) {
                g.setColor(new Color(0f, 1f, 0f, 0.8f));
                g.fillRect(posx, posy, anchura, altura);
            }
        } else {
            for (Rectangle2D r : intersecciones) {
                g.fillRect((float) r.getX(), (float) r.getY(), (float) r.getWidth(), (float) r.getHeight());
            }
        }
        edificio.animation.draw(posx, posy);
    }

    public void renderVisibility(Graphics g, Color c, int desplazamiento) {
        Jugador mainPlayer = game.getMainPlayer();
        mainPlayer.renderVisibility(g, c, desplazamiento);
        game.getAlliesFromPlayer(mainPlayer).forEach(p -> p.renderVisibility(g, c, desplazamiento));
    }

    public void moveUnitsSelected(boolean movimiento, List<Unidad> unidades, float x, float y) {
        int i = 0;
        List<Unidad> contador = new ArrayList<>();
        contador.addAll(unidades);
        for (int j = 8; contador.size() > 0; j += 8) {
            int horizontal = 2 * (j / 8) + 1;
            float contador_x = x, contador_y = y;
            while (!contador.isEmpty()) {
                Unidad u = contador.remove(0);
                if (movimiento) {
                    u.mover(game, contador_x, contador_y);
                } else {
                    u.x = contador_x;
                    u.y = contador_y;
                }
                i++;
                if (i == horizontal) {
                    contador_x = x;
                    contador_y += u.altura + 10;
                    i = 0;
                } else {
                    contador_x += u.anchura + 10;
                }
            }
        }
    }

    public static void getAttackMove(List<Unidad> unidades, float x, float y) {
        float distancia_x = x - unidades.get(0).x;
        float distancia_y = y - unidades.get(0).y;
        unidades.get(0).atacarmover(game, x, y);
        unidades.remove(unidades.get(0));
        for (Unidad u : unidades) {
            u.atacarmover(game, u.x + distancia_x, u.y + distancia_y);
        }
    }

    public void obtener_movimiento(List<Unidad> unidades, int x, int y) {
        float distancia_x = x - unidades.get(0).x;
        float distancia_y = y - unidades.get(0).y;
        unidades.get(0).mover(game, x, y);
        unidades.remove(unidades.get(0));
        for (Unidad u : unidades) {
            u.mover(game, u.x + distancia_x, u.y + distancia_y);
        }
    }

    public void handleRightClick(float x, float y) {
        if (ui.allElementsAreControlledByMainPlayer(game)) {
            for (ElementoSimple e : ui.elements) {
                if (e instanceof Edificio) {
                    Edificio contador = (Edificio) e;
                    contador.reunion_x = x;
                    contador.reunion_y = y;
                } else {
                    if (e instanceof Unidad) {
                        Unidad u = (Unidad) e;
                        List<Recurso> resourcesToCheck = "Recolector".equals(u.nombre) ?
                                game.getCivilTownsAndVisionTowers()
                                : game.getVisionTowers();
                        for (Recurso r : resourcesToCheck) {
                            if (r.hitbox(x, y)) {
                                u.recolectar(game, r);
                                return;
                            }
                        }
                        Jugador aliado = game.getPlayerFromElement(u);
                        if (u.piloto) {
                            //Embarca vehículo
                            for (Unidad u2 : aliado.unidades) {
                                if (u2.vehiculo) {
                                    if (u2.hitbox(x, y)) {
                                        u.embarcar(u2);
                                        return;
                                    }
                                }
                            }
                            //Embarca torreta
                            for (Edificio ed : aliado.edificios) {
                                if (ed.hitbox(x, y)) {
                                    if (ed.nombre.equals("Torreta defensiva")) {
                                        u.embarcar(ed);
                                        return;
                                    }
                                }
                            }
                        }
                        if (aliado.raza.equals(RaceEnum.GUARDIANES)) {
                            if (u.constructor) {
                                //Constructor activa edificio inactivo
                                for (Edificio ed : aliado.edificios) {
                                    if (!ed.activo) {
                                        if (ed.hitbox(x, y)) {
                                            u.embarcar(ed);
                                            return;
                                        }
                                    }
                                }
                            } else {
                                //Unidad solucion evento negativo
                                if (aliado.edificios.get(0).hitbox(x, y)) {
                                    //Comprobación de si la unidad es necesaria o no
                                    for (Evento ev : aliado.eventos.contenido) {
                                        if (!ev.positivo) {
                                            if (ev.nombre_elemento.equals(u.nombre)) {
                                                u.embarcar(aliado.edificios.get(0));
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            List<Unidad> unitsSelected = ui.elements.stream()
                    .filter(e -> (e instanceof Unidad) && !(e instanceof Bestia))
                    .map(e -> (Unidad) e).collect(Collectors.toList());
            elementCircle = game.getElementAttackedAtPosition(x, y, unitsSelected);
            if (elementCircle == null) {
                if (!unitsSelected.isEmpty()) {
                    playMainUnitTypeSound();
                    moveUnitsSelected(true, unitsSelected, x, y);
                }
            } else {
                timeCircleCounter = timeCircle;
            }
        }
    }

    public void playMainUnitTypeSound() {
        Unidad mainUnit = (Unidad) ui.currentSelectionPage.get(0);
        if (ElementosComunes.UNIT_MOVEMENT_SOUNDS.containsKey(mainUnit.nombre)) {
            if (!ElementosComunes.UNIT_MOVEMENT_SOUNDS.get(mainUnit.nombre).get(0).playing()) {
                Sound currentSound = ElementosComunes.UNIT_MOVEMENT_SOUNDS.get(mainUnit.nombre).remove(0);
                currentSound.play();
                ElementosComunes.UNIT_MOVEMENT_SOUNDS.get(mainUnit.nombre).add(currentSound);
            }
        }
    }

    public void handleForcedCameraMovement(int delta) {
        if (x_movimiento != -1) {
            if (playerX < x_movimiento) {
                if (playerX + 0.5f * delta >= x_movimiento) {
                    playerX = x_movimiento;
                } else {
                    playerX += 0.5f * delta;
                }
            } else if (playerX > x_movimiento) {
                if (playerX - 0.5f * delta <= x_movimiento) {
                    playerX = x_movimiento;
                } else {
                    playerX -= 0.5f * delta;
                }
            } else {
                x_movimiento = -1;
            }
        }
        if (y_movimiento != -1) {
            if (playerY < y_movimiento) {
                if (playerY + 0.5f * delta >= y_movimiento) {
                    playerY = y_movimiento;
                } else {
                    playerY += 0.5f * delta;
                }
            } else if (playerY > y_movimiento) {
                if (playerY - 0.5f * delta <= y_movimiento) {
                    playerY = y_movimiento;
                } else {
                    playerY -= 0.5f * delta;
                }
            } else {
                y_movimiento = -1;
            }
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        try {
            if (!(game instanceof GuidedGame)) {
                if (!LOCAL_BSOS.isEmpty() && CURRENT_BSO == null) {
                    CURRENT_BSO = LOCAL_BSOS.remove(0);
                } else if (LOCAL_BSOS.isEmpty()) {
                    LOCAL_BSOS.addAll(ElementosComunes.BSOS);
                }
            }
            Input input = container.getInput();
            // On F10 or ESC we enable / disable the pause menu
            if (input.isKeyPressed(Input.KEY_F10) || (input.isKeyPressed(Input.KEY_ESCAPE) && !ui.isSingleBuildingSelected())) {
                pauseMenuEnabled = !pauseMenuEnabled;
            }
            if (pauseMenuEnabled) {
                // Check options buttons pressed
                if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                    for (BotonSimple optionMenuButton : pauseMenuButtons) {
                        optionMenuButton.checkIfItsClickedInCombat(input);
                    }
                }
            } else {
                // On ESC we remove the last item from the current construction queue
                if (input.isKeyPressed(Input.KEY_ESCAPE)) {
                    if (ui.isSingleBuildingSelected()) {
                        Edificio buildingSelected = (Edificio) ui.elements.get(0);
                        buildingSelected.cancelLastElementBeingProduced(game);
                    }
                }
                Jugador mainPlayer = game.getMainPlayer();
                ctrl = input.isKeyDown(Input.KEY_LCONTROL);
                mayus = input.isKeyDown(Input.KEY_LSHIFT);
                // mensajes
                List<Mensaje> messagesToBeRemoved = new ArrayList<>();
                for (Mensaje m : mensajes) {
                    if (m.comprobar_mensaje(delta)) {
                        messagesToBeRemoved.add(m);
                    }
                }
                mensajes.removeAll(messagesToBeRemoved);
                handleForcedCameraMovement(delta);
                // Check victory and defeat
                if (game.checkMainTeamVictory() && victoria == null) {
                    displayVictory();
                }
                if (game.checkMainTeamDefeat() && derrota == null) {
                    displayDefeat();
                }
                //Relojes
                relojes.stream().forEach(r -> {
                    try {
                        r.avanzar_reloj(game, delta);
                    } catch (SlickException e) {
                    }
                });
                //Comprobar fusiones
                for (int j = 0; j < Clark.fusiones.size(); j++) {
                    Fusion f = Clark.fusiones.get(j);
                    if (f.comprobacion()) {
                        if (f.resultado == null) {
                            f.resultado = Clark.resolverFusion(game, f);
                        } else {
                            if (f.comportamiento(delta)) {
                                Clark.fusiones.remove(f);
                                j--;
                            }
                        }
                    }
                }
                //Controlar que presionen botones mediante teclas
                for (ElementoComplejo e : ui.currentSelectionPage) {
                    List<BotonComplejo> botones;
                    if (!(e instanceof Manipulador) || ((Manipulador) e).enhancementButtons.isEmpty()) {
                        botones = e.botones;
                    } else {
                        botones = ((Manipulador) e).enhancementButtons;
                    }
                    if (botones != null) {
                        botones.stream()
                                .filter(b -> input.isKeyPressed(b.tecla))
                                .forEach(b -> b.resolucion(ui.elements, e, game));
                    }
                }
                //Comportamientos
                game.comportamiento_elementos(container.getGraphics(), delta);
                //Mover la pantalla
                if (x_movimiento == -1 && y_movimiento == -1) {
                    if ((input.getMouseX() >= WindowCombat.VIEWPORT_SIZE_WIDTH - 20) || (input.isKeyDown(Input.KEY_RIGHT))) {
                        if (playerX < offsetMaxX) {
                            playerX += scrollspeed * delta;
                        }
                    }
                    if ((input.getMouseY() >= WindowCombat.VIEWPORT_SIZE_HEIGHT) || (input.isKeyDown(Input.KEY_DOWN))) {
                        if (playerY < offsetMaxY) {
                            playerY += scrollspeed * delta;
                        }
                    }
                    if ((input.getMouseX() <= 10) || (input.isKeyDown(Input.KEY_LEFT))) {
                        if (playerX > offsetMinX) {
                            playerX -= scrollspeed * delta;
                        }
                    }
                    if ((input.getMouseY() <= 10) || (input.isKeyDown(Input.KEY_UP))) {
                        if (playerY > offsetMinY) {
                            playerY -= scrollspeed * delta;
                        }
                    }
                }
                //Grupos de Control
                for (int numberKey = Input.KEY_1; numberKey <= Input.KEY_0; numberKey++) {
                    if (input.isKeyPressed(numberKey)) {
                        if (input.isKeyDown(Input.KEY_LCONTROL)) {
                            //Asignar grupo de control
                            final int numberKeyUsed = numberKey;
                            controlGroups.removeIf(cg -> cg.tecla == numberKeyUsed);
                            controlGroups.forEach(cg -> cg.contenido.removeAll(ui.elements));
                            controlGroups.removeIf(cg -> cg.contenido.isEmpty());
                            controlGroups.add(new ControlGroup(ui.elements, numberKey));
                        } else {
                            handleControlGroupSelection(numberKey);
                        }
                    }
                }
                //Rueda del ratón: Zoom
                mouseWheelZoom(input);
                // In case F1 pressed we select the main element
                if (input.isKeyPressed(Input.KEY_F1)) {
                    mainPlayer.selectMainElement();
                }
                //Tabulación
                if (input.isKeyPressed(Input.KEY_TAB)) {
                    ui.siguiente_seleccion();
                }
                //Cambiar a Atacar-Mover
                if (input.isKeyPressed(Input.KEY_A)) {
                    if (ui.unitSelected(game) && ui.allElementsAreControlledByMainPlayer(game)) {
                        attackMoveModeEnabled = true;
                        container.setMouseCursor(atacar, 10, 10);
                    }
                }
                if (game instanceof GuidedGame) {
                    if (!((GuidedGame) game).steps.isEmpty() && !((GuidedGame) game).steps.get(0).requiresClick) {
                        checkTutorialStepResolution();
                    }
                }
                // Mantener Click izquierdo
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    if (((playerY + input.getMouseY()) >= ((int) playerY + VIEWPORT_SIZE_HEIGHT - UI_HEIGHT))
                            && (((playerX + input.getMouseX()) >= (WindowCombat.playerX + anchura_miniatura + ui.anchura_seleccion + ui.anchura_botones))
                            && ((playerX + input.getMouseX()) <= (WindowCombat.playerX + WindowCombat.VIEWPORT_SIZE_WIDTH))) && !holdingLeftClick) {
                        // On minimap holding we move the perspective
                        ui.movePlayerPerspective(playerX + input.getMouseX(), playerY + input.getMouseY());
                    } else if (!holdingLeftClick) {
                        xStartHolding = (int) playerX + input.getMouseX();
                        yStartHolding = (int) playerY + input.getMouseY();
                        holdingLeftClick = true;
                    }
                }
                //Control de cuadrado de selección
                if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    handleSquareSelection(input);
                }
                //Click derecho
                if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
                    if (game instanceof GuidedGame) {
                        if (!((GuidedGame) game).steps.isEmpty() && ((GuidedGame) game).steps.get(0).requiresClick) {
                            if (checkTutorialStepResolution()) {
                                click = false;
                            }
                        }
                    }
                    x_click = (int) playerX + input.getMouseX();
                    y_click = (int) playerY + input.getMouseY();
                    if (y_click >= ((int) playerY + VIEWPORT_SIZE_HEIGHT - UI_HEIGHT)) {
                        if ((x_click >= (WindowCombat.playerX + anchura_miniatura + ui.anchura_seleccion + ui.anchura_botones)) && (x_click <= (WindowCombat.playerX + WindowCombat.VIEWPORT_SIZE_WIDTH))) {
                            Point2D resultado = ui.obtener_coordenadas_minimapa(x_click, y_click);
                            handleRightClick((float) resultado.getX(), (float) resultado.getY());
                        }
                        click = false;
                    } else if (attackMoveModeEnabled) {
                        container.setDefaultMouseCursor();
                        attackMoveModeEnabled = false;
                    } else if (edificio != null) {
                        //Cancelar Edificio
                        edificio = null;
                    } else if (elemento_habilidad != null) {
                        elemento_habilidad = null;
                        habilidad = null;
                    } else {
                        handleRightClick(x_click, y_click);
                    }
                }
                if (elementCircle != null) {
                    timeCircleCounter -= TIME_REGULAR_SPEED * delta;
                    if (timeCircleCounter <= 0) {
                        timeCircleCounter = 0;
                        elementCircle = null;
                    }
                }
            }
            // Update coordinates of watches and main element button
            WindowCombat.relojes.forEach(r -> {
                r.x = WindowCombat.playerX + WindowCombat.VIEWPORT_SIZE_WIDTH / 2 - 200 + 100 * WindowCombat.relojes.indexOf(r);
                r.y = WindowCombat.playerY + 5;
            });
            selectMainElementButton.x = playerX;
            selectMainElementButton.y =
                    playerY + WindowCombat.VIEWPORT_SIZE_HEIGHT - UI_HEIGHT - selectMainElementButton.altura;
        } catch (Exception e) {
            System.out.println("Exception during update loop");
            e.printStackTrace();
        }
    }

    private void handleControlGroupSelection(Integer numberKey) {
        //Seleccionar grupo de control
        for (ControlGroup c : controlGroups) {
            if (c.tecla == numberKey) {
                boolean cambiar_perspectiva = true;
                if (ui.elements.isEmpty()) {
                    cambiar_perspectiva = false;
                }
                for (int j = 0; j < ui.elements.size(); j++) {
                    if (ui.elements.get(j) != c.contenido.get(j)) {
                        cambiar_perspectiva = false;
                        break;
                    }
                }
                if (cambiar_perspectiva) {
                    changePlayerPerspectiveToElements(c.contenido);
                } else {
                    //El grupo de control no está seleccionado: Se selecciona.
                    game.deselectAllElements();
                    for (ElementoComplejo e : c.contenido) {
                        e.seleccionar();
                    }
                }
                break;
            }
        }
    }

    public static void changePlayerPerspectiveToElements(List<ElementoComplejo> contenido) {
        //El grupo de control ya está seleccionado: Se cambia a la perspectiva del g.c.
        //Obtener coordenadas mínimas y máximas
        float x_min = -1, y_min = -1;
        float x_max = -1, y_max = -1;
        for (ElementoComplejo e : contenido) {
            if (x_min == -1 || e.x < x_min) {
                x_min = e.x;
            }
            if (y_min == -1 || e.y < y_min) {
                y_min = e.y;
            }
            if (x_max == -1 || e.x > x_max) {
                x_max = e.x;
            }
            if (y_max == -1 || e.y > y_max) {
                y_max = e.y;
            }
        }
        //Obtener coordenadas medias
        float x_medio = (x_max + x_min) / 2;
        float y_medio = (y_max + y_min) / 2;
        if (x_medio - WindowCombat.VIEWPORT_SIZE_WIDTH / 2 <= 0) {
            WindowCombat.playerX = 0;
        } else {
            WindowCombat.playerX = x_medio - WindowCombat.VIEWPORT_SIZE_WIDTH / 2;
        }
        if (y_medio - WindowCombat.VIEWPORT_SIZE_HEIGHT / 2 <= 0) {
            WindowCombat.playerY = 0;
        } else {
            WindowCombat.playerY = y_medio - WindowCombat.VIEWPORT_SIZE_HEIGHT / 2;
        }
    }

    public static void handleAttackMove(float mouseX, float mouseY, GameContainer container) {
        List<Unidad> unitsSelected = ui.elements.stream()
                .filter(e -> (e instanceof Unidad) && !(e instanceof Bestia))
                .map(e -> (Unidad) e).collect(Collectors.toList());
        // First try to attack the element on the cursor
        ElementoVulnerable elementAttacked = game.getElementAttackedAtPosition((int) playerX + mouseX,
                (int) playerY + mouseY, unitsSelected);
        if (elementAttacked == null) {
            // If there is no element on the cursor we attack move to the point
            getAttackMove(unitsSelected, (int) playerX + mouseX, (int) playerY + mouseY);
        }
        attackMoveModeEnabled = false;
        container.setDefaultMouseCursor();
        deseleccionar = false;
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        try {
            if (CURRENT_BSO != null && !CURRENT_BSO.playing()) {
                CURRENT_BSO.play(1f, 0.1f);
            }
            Jugador mainPlayer = game.getMainPlayer();
            Input input = container.getInput();
            if (game.isShadowOfWarEnabled) {
                //Negro
                g.setBackground(new Color(0.1f, 0.1f, 0.05f, 1f));
            }
            g.scale(zoomLevel, zoomLevel);
            g.translate(-playerX, -playerY);
            if (victoria != null || derrota != null) {
                if (victoria != null) {
                    victoria.draw(VIEWPORT_SIZE_WIDTH / 2 - 400, VIEWPORT_SIZE_HEIGHT / 2 - 50);
                } else {
                    derrota.draw(VIEWPORT_SIZE_WIDTH / 2 - 400, VIEWPORT_SIZE_HEIGHT / 2 - 50);
                }
            } else {
                selectMainElementButton.render(g);
                if (game.isShadowOfWarEnabled) {
                    //Gris
                    renderVisibility(g, HALF_VISIBLE_COLOR, 100);
                    //Verde
                    renderVisibility(g, FULL_VISIBLE_COLOR, 0);
                }
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    if (holdingLeftClick) {
                        g.setColor(new Color(153, 255, 153));
                        g.fillRect(xStartHolding, yStartHolding, playerX + input.getMouseX() - xStartHolding,
                                playerY + input.getMouseY() - yStartHolding);
                        g.setColor(Color.white);
                    }
                }
                game.renderElements(g, input);
                if (elementCircle != null) {
                    Jugador elementCirclePlayer = game.getPlayerFromElement(elementCircle);
                    if (elementCirclePlayer != null) {
                        elementCircle.circulo_extendido(g, elementCirclePlayer.color);
                    } else {
                        elementCircle.circulo_extendido(g, Color.blue);
                    }
                }
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    if (holdingLeftClick) {
                        g.setColor(Color.green);
                        g.drawRect(xStartHolding, yStartHolding, playerX + input.getMouseX() - xStartHolding,
                                playerY + input.getMouseY() - yStartHolding);
                        g.setColor(Color.white);
                    }
                }
                //Construcción
                if ((edificio != null)) {
                    if (constructor instanceof Edificio) {
                        g.setColor(Color.green);
                        g.drawOval(constructor.x + -((Edificio) constructor).radio_construccion / 2,
                                constructor.y - ((Edificio) constructor).radio_construccion / 2,
                                ((Edificio) constructor).radio_construccion,
                                ((Edificio) constructor).radio_construccion);
                        g.setColor(Color.white);
                    }
                    dibujar_preview_edificio(g, edificio, input);
                }
                for (Unidad u : mainPlayer.unidades) {
                    Edificio contador = u.edificio_construccion;
                    if ((contador != null) && (contador.vida < contador.vida_max)) {
                        dibujar_edificio(g, u.edificio_construccion);
                    }
                }
                for (Edificio e : mainPlayer.edificios) {
                    Edificio contador = e.edificio_construccion;
                    if ((contador != null) && (contador.vida < contador.vida_max)) {
                        dibujar_edificio(g, e.edificio_construccion);
                    }
                }
                //Habilidad
                if (elemento_habilidad != null) {
                    g.setColor(Color.cyan);
                    float alcance_anchura = 2 * (habilidad.alcance + elemento_habilidad.anchura / 2), alcance_altura =
                            2 * (habilidad.alcance + elemento_habilidad.altura / 2);
                    g.drawOval(elemento_habilidad.x - alcance_anchura / 2, elemento_habilidad.y - alcance_altura / 2,
                            alcance_anchura, alcance_altura);
                    if (habilidad.area != 0) {
                        g.drawOval(playerX + input.getMouseX() - habilidad.area / 2,
                                playerY + input.getMouseY() - habilidad.area / 2, habilidad.area, habilidad.area);
                    }
                    g.setColor(Color.white);
                }
                ui.render(game, g, controlGroups);
                if (attackMoveModeEnabled) {
                    g.setColor(Color.red);
                    if (ui.currentSelectionPage.get(0) instanceof Unidad) {
                        if (((Unidad) ui.currentSelectionPage.get(0)).area > 0) {
                            Unidad unidad = (Unidad) ui.currentSelectionPage.get(0);
                            g.drawOval(playerX + input.getMouseX() - unidad.area,
                                    playerY + input.getMouseY() - unidad.area, unidad.area * 2, unidad.area * 2);
                        }
                    }
                    g.setColor(Color.white);
                }
                //Mensajes
                coordenadas_errores();
                for (Mensaje m : mensajes) {
                    m.dibujar(g);
                }
                //RTS.Relojes
                relojes.stream().forEach(r -> r.dibujar(input, g));
                //Boton seleccionado
                BotonComplejo hoveredButton = ui.obtainHoveredButton(playerX + input.getMouseX(),
                        playerY + input.getMouseY());
                if (hoveredButton != null) {
                    hoveredButton.renderExtendedInfo(game.getMainPlayer(), g, ui.currentSelectionPage.get(0));
                }
                //Event hovered Guardián
                if (RaceEnum.GUARDIANES.equals(mainPlayer.raza)) {
                    Evento eventHovered = mainPlayer.getHoveredEvent(playerX + input.getMouseX(),
                            playerY + input.getMouseY());
                    if (eventHovered != null) {
                        new BotonComplejo(eventHovered).renderExtendedInfo(mainPlayer, g, null);
                    }
                }
                //Guided Game steps
                if (game instanceof GuidedGame) {
                    float initialTutorialY = playerY + WindowCombat.VIEWPORT_SIZE_HEIGHT - UI_HEIGHT - 100;
                    GuidedGame guidedGame = (GuidedGame) game;
                    if (!guidedGame.steps.isEmpty() && guidedGame.steps.get(0).text != null) {
                        g.setColor(new Color(0.2f, 0.2f, 0.2f, 0.8f));
                        g.fillRect(playerX, initialTutorialY, WindowCombat.VIEWPORT_SIZE_WIDTH, 100);
                        g.setColor(Color.white);
                        g.drawString(guidedGame.steps.get(0).text, playerX, initialTutorialY);
                        continuar.x = playerX + WindowCombat.VIEWPORT_SIZE_WIDTH / 2 - continuar.anchura;
                        continuar.y = initialTutorialY + 100 - 21;
                    }
                }
                List<Jugador> mainTeam = game.getMainTeam();
                for (int i = 0; i < mainTeam.size(); i++) {
                    mainTeam.get(i).renderResources(g, playerX + VIEWPORT_SIZE_WIDTH - 100, playerY + 20 * i);
                }
            }
            continuar.render(g);
            if (elementHighlighted != null) {
                g.setColor(Color.red);
                g.drawOval(elementHighlighted.x - (elementHighlighted.anchura + highlightRadius) / 2,
                        elementHighlighted.y - (elementHighlighted.altura + highlightRadius) / 2,
                        elementHighlighted.anchura + highlightRadius, elementHighlighted.altura + highlightRadius);
            }
            if (pauseMenuEnabled) {
                renderPauseMenu(g);
            }
        } catch (Exception e) {
            System.out.println("Exception during render loop");
            e.printStackTrace();
        }
    }

    private void renderPauseMenu(Graphics g) {
        g.drawString("JUEGO PAUSADO", playerX + WindowCombat.middleScreenX("JUEGO PAUSADO"),
                playerY + WindowCombat.responsiveY(30));
        for (BotonSimple optionMenuButton : pauseMenuButtons) {
            optionMenuButton.x = playerX + WindowCombat.middleScreenX(optionMenuButton.texto);
            optionMenuButton.y =
                    playerY + WindowCombat.responsiveY(40 + 10 * pauseMenuButtons.indexOf(optionMenuButton));
        }
        pauseMenuButtons.forEach(b -> b.render(g));
    }

    private void displayVictory() throws SlickException {
        this.playerX = 0;
        this.playerY = 0;
        ElementosComunes.VICTORY_SOUND.playAt(0f, 0f, 0f);
        victoria = new Animation(new Image[]{new Image("media/Victoria1.png"), new Image("media/Victoria2.png")},
                new int[]{300, 300}, true);
        continuar.canBeUsed = true;
        continuar.x = VIEWPORT_SIZE_WIDTH / 2;
        continuar.y = VIEWPORT_SIZE_HEIGHT / 2 + 100;
    }

    private void displayDefeat() throws SlickException {
        this.playerX = 0;
        this.playerY = 0;
        ElementosComunes.DEFEAT_SOUND.playAt(0f, 0f, 0f);
        derrota = new Animation(new Image[]{new Image("media/Derrota1.png"), new Image("media/Derrota2.png")},
                new int[]{300, 300}, true);
        continuar.canBeUsed = true;
        continuar.x = VIEWPORT_SIZE_WIDTH / 2;
        continuar.y = VIEWPORT_SIZE_HEIGHT / 2 + 100;
    }

    public static boolean checkTutorialStepResolution() throws SlickException {
        GuidedGame t = (GuidedGame) game;
        if (t.steps.get(0).check(game)) {
            t.steps.get(0).effect(game);
            t.steps.remove(0);
            if (!t.steps.isEmpty()) {
                continuar.canBeUsed = t.steps.get(0).requiresContinueClick;
            }
            t.sounds.get(0).stop();
            t.sounds.remove(0);
            if (!t.sounds.isEmpty()) {
                t.sounds.get(0).playAt(1.0f, 10.0f, 0f, 0f, 0f);
            }
            return true;
        }
        return false;
    }

    public static void endGameAndReturnToPreviousWindow() throws SlickException {
        victoria = null;
        derrota = null;
        ElementosComunes.VICTORY_SOUND.stop();
        ElementosComunes.DEFEAT_SOUND.stop();
        if (CURRENT_BSO != null) {
            CURRENT_BSO.stop();
        }
        if (WindowMain.previousWindow != null) {
            RTS.mainController.windowSwitch(container, WindowMain.previousWindow);
        } else {
            RTS.mainController.windowSwitch(container, "Menu");
        }
    }

    public static float middleScreenX(String text) {
        return VIEWPORT_SIZE_WIDTH / 2 - (text.length() * 10) / 2;
    }

    public static float responsiveX(float percentage) {
        return VIEWPORT_SIZE_WIDTH * percentage / 100;
    }

    public static float responsiveY(float percentage) {
        return VIEWPORT_SIZE_HEIGHT * percentage / 100;
    }

    public static void selectAllSameElements(float xClick, float yClick) {
        ElementoComplejo selectedElement = game.obtainMainPlayerSelectedElement(xClick, yClick);
        if (selectedElement != null) {
            game.deselectAllElements();
            ui.selectAll(game.obtainAllSameElements(selectedElement));
        }
    }

    public static void handleSquareSelection(Input input) {
        if (holdingLeftClick) {
            int xEndHolding = (int) playerX + input.getMouseX();
            int yEndHolding = (int) playerY + input.getMouseY();
            if (Math.abs(xStartHolding - xEndHolding) > HOLDING_THRESHOLD || Math.abs(yStartHolding - yEndHolding) > HOLDING_THRESHOLD) {
                if (xStartHolding > xEndHolding) {
                    int contador = xStartHolding;
                    xStartHolding = xEndHolding;
                    xEndHolding = contador;
                }
                if (yStartHolding > yEndHolding) {
                    int contador = yStartHolding;
                    yStartHolding = yEndHolding;
                    yEndHolding = contador;
                }
                Jugador mainPlayer = game.getMainPlayer();
                List<ElementoComplejo> unitsToSelect = new ArrayList<>();
                for (Unidad u : mainPlayer.unidades) {
                    if (u.en_rango(xStartHolding, yStartHolding, xEndHolding, yEndHolding)) {
                        if (ctrl) {
                            for (Unidad u2 : mainPlayer.unidades) {
                                if (u2 != u) {
                                    if (u2.nombre.equals(u.nombre)) {
                                        if (unitsToSelect.indexOf(u2) == -1) {
                                            unitsToSelect.add(u2);
                                        }
                                    }
                                }
                            }
                        }
                        unitsToSelect.add(u);
                    }
                }
                if (!unitsToSelect.isEmpty()) {
                    if ((!mayus) && (deseleccionar)) {
                        game.deselectAllElements();
                    } else {
                        deseleccionar = true;
                    }
                    ui.selectAll(unitsToSelect);
                }
                // Units selected have priority over buildings selected
                boolean squareSelectBuildings = ui.elements.stream().noneMatch(e -> e instanceof Unidad);
                if (squareSelectBuildings) {
                    for (Edificio e : mainPlayer.edificios) {
                        if (e.en_rango(xStartHolding, yStartHolding, xEndHolding, yEndHolding)) {
                            if (ctrl) {
                                for (Edificio e2 : mainPlayer.edificios) {
                                    if (e2 != e) {
                                        if (e2.nombre.equals(e.nombre)) {
                                            e2.seleccionar();
                                        }
                                    }
                                }
                            }
                            e.seleccionar();
                        }
                    }
                }
            }
            holdingLeftClick = false;
        }
    }
}
