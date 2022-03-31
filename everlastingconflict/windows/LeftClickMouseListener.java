package everlastingconflict.windows;

import everlastingconflict.campaign.tutorial.GuidedGame;
import everlastingconflict.elements.ElementoComplejo;
import everlastingconflict.elements.impl.Nave;
import everlastingconflict.elements.impl.Recurso;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.watches.Reloj;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;

import static everlastingconflict.windows.UI.UI_HEIGHT;
import static everlastingconflict.windows.UI.anchura_miniatura;
import static everlastingconflict.windows.WindowCombat.*;

public class LeftClickMouseListener implements MouseListener {
    @Override
    public void mouseWheelMoved(int i) {

    }

    @Override
    public void mouseClicked(int button, int mouseX, int mouseY, int clickCount) {
        try {
            if (Input.MOUSE_LEFT_BUTTON == button) {
                if (clickCount == 2) {
                    WindowCombat.selectAllSameElements(mouseX, mouseY);
                } else {
                    try {
                        x_click = (int) playerX + mouseX;
                        y_click = (int) playerY + mouseY;
                        if (continuar.isHovered(x_click, y_click)) {
                            if ((game instanceof GuidedGame && ((GuidedGame) game).steps.isEmpty()) || (victoria != null || derrota != null)) {
                                endGameAndReturnToPreviousWindow();
                            }
                        }
                        click = true;
                        if (game instanceof GuidedGame) {
                            if (!((GuidedGame) game).steps.isEmpty() && ((GuidedGame) game).steps.get(0).requiresClick) {
                                if (checkTutorialStepResolution()) {
                                    click = false;
                                }
                            }
                        }
                        Reloj relojClickado = relojes.stream().filter(r -> r.hitbox(playerX + mouseX,
                                playerY + mouseX)).findFirst().orElse(null);
                        if (selectMainElementButton.isHovered(playerX + mouseX,
                                playerY + mouseY)) {
                            game.getMainPlayer().selectMainElement();
                            click = false;
                        } else if (relojClickado != null) {
                            relojClickado.handleLeftClick();
                            click = false;
                        } else if (y_click >= ((int) playerY + VIEWPORT_SIZE_HEIGHT - UI_HEIGHT)) {
                            ui.handleLeftClick(game, x_click, y_click);
                            click = false;
                        } else if ((edificio != null)) {
                            if (edificio instanceof Nave) {
                                // Decidimos el punto de aterrizaje de la nave
                                ((Nave) edificio).selectLandingCoordinates(x_click, y_click);
                                edificio = null;
                                click = false;
                            } else if (!constructor.nombre.equals("No hay")) {
                                if (edificio.construible(constructor, game, x_click, y_click)) {
                                    if (edificio.nombre.equals("Refinería") || edificio.nombre.equals("Centro de " +
                                            "restauración")) {
                                        Recurso r = game.closestResource(null, null, "Hierro",
                                                x_click, y_click);
                                        if (r != null) {
                                            constructor.construir(game, edificio, r.x, r.y);
                                        }
                                    } else {
                                        constructor.construir(game, edificio, x_click, y_click);
                                    }
                                    edificio = null;
                                    constructor = null;
                                }
                                click = false;
                            }
                        } else if (elemento_habilidad != null) {
                            //Selección de objetivo para habilidad
                            ElementoComplejo elemento;
                            if ((elemento = habilidad.targetSelection(game, elemento_habilidad, x_click, y_click)) != null) {
                                Unidad unidad = (Unidad) elemento_habilidad;
                                unidad.habilidad(habilidad, elemento);
                            } else {
                                String wrongSkillText = "Elemento seleccionado incorrecto. La habilidad tiene estos " +
                                        "objetivos" +
                                        " válidos: ";
                                WindowMain.combatWindow
                                        .anadir_mensaje(new Mensaje(wrongSkillText + habilidad.selectionType,
                                                Color.red, playerX + anchura_miniatura,
                                                playerY + VIEWPORT_SIZE_HEIGHT - UI_HEIGHT - 20, 5f));
                            }
                            elemento_habilidad = null;
                            habilidad = null;
                            click = false;
                        } else if (attackMoveModeEnabled) {
                            handleAttackMove(mouseX, mouseY, container);
                        } else {
                            if (WindowCombat.ctrl) {
                                WindowCombat.selectAllSameElements(mouseX, mouseY);
                            } else {
                                game.checkSingleElementSelection(mouseX, mouseY);
                            }
                        }
                    } catch (SlickException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception during left click mouse listener");
            e.printStackTrace();
        }
    }

    @Override
    public void mousePressed(int i, int i1, int i2) {

    }

    @Override
    public void mouseReleased(int i, int i1, int i2) {

    }

    @Override
    public void mouseMoved(int i, int i1, int i2, int i3) {

    }

    @Override
    public void mouseDragged(int i, int i1, int i2, int i3) {

    }

    @Override
    public void setInput(Input input) {

    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {

    }

    @Override
    public void inputStarted() {

    }
}
