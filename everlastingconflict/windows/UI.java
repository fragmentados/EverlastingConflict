/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.windows;

import everlastingconflict.ControlGroup;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elementosvisuales.BotonSimple;
import everlastingconflict.elements.ElementoComplejo;
import everlastingconflict.elements.ElementoEstado;
import everlastingconflict.elements.ElementoSimple;
import everlastingconflict.elements.impl.*;
import everlastingconflict.elements.util.ElementosComunes;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.races.enums.SubRaceEnum;
import everlastingconflict.status.Status;
import everlastingconflict.status.StatusNameEnum;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static everlastingconflict.elements.util.ElementosComunes.UI_COLOR;
import static everlastingconflict.windows.WindowCombat.*;

public class UI {

    public List<ElementoComplejo> elements;
    public List<ElementoComplejo> currentSelectionPage;
    public BotonSimple sig, ant;
    public int inicio, fin, npagina;
    public static float x_mini, y_mini, anchura_mini = 250, altura_mini = 200;
    public static float x_minim, y_minim, anchura_minim, altura_minim;
    public static float anchura_miniatura;
    public static float anchura_seleccion;
    public float anchura_botones;
    public static final float UI_HEIGHT = 200;
    public static final int ELEMENTS_PER_PAGE = 32;
    private Comparator<ElementoComplejo> elementSorter = Comparator.comparing(e -> e.nombre);

    public UI() {
        this.elements = new ArrayList<>();
        this.currentSelectionPage = new ArrayList<>();
        this.sig = new BotonSimple("sig");
        this.ant = new BotonSimple("ant");
        inicio = fin = 0;
        npagina = 1;
        anchura_miniatura = 200;
        anchura_seleccion = 730;
        anchura_botones = WindowCombat.VIEWPORT_SIZE_WIDTH - 1180;
    }

    public void previousPage() {
        if (inicio >= ELEMENTS_PER_PAGE) {
            fin -= (fin - inicio);
            inicio -= ELEMENTS_PER_PAGE;
            npagina--;
        }
    }

    public void nextPage() {
        if ((fin + ELEMENTS_PER_PAGE) <= elements.size()) {
            inicio += ELEMENTS_PER_PAGE;
            fin += ELEMENTS_PER_PAGE;
            npagina++;
        } else {
            if (fin < elements.size()) {
                inicio += ELEMENTS_PER_PAGE;
                fin = elements.size();
                npagina++;
            }
        }
    }

    public void resetAndSelect(ElementoComplejo e) {
        this.elements = new ArrayList<>();
        seleccionar(e);
    }

    public void selectAll(List<ElementoComplejo> elements) {
        this.elements.addAll(elements);
        Collections.sort(this.elements, elementSorter);
        initCurrentSelectionPage();
        if (fin + elements.size() <= ELEMENTS_PER_PAGE) {
            fin += elements.size();
        } else {
            fin = ELEMENTS_PER_PAGE;
        }
    }

    public void seleccionar(ElementoComplejo e) {
        if (elements.indexOf(e) == -1) {
            this.elements.add(e);
            Collections.sort(elements, elementSorter);
            initCurrentSelectionPage();
            if (fin < ELEMENTS_PER_PAGE) {
                fin++;
            }
        }
    }

    public void seleccionar(ElementoComplejo e, boolean mayus) {
        if (elements.indexOf(e) == -1) {
            this.elements.add(e);
            if (!mayus) {
                Collections.sort(elements, elementSorter);
                initCurrentSelectionPage();
            }
            if (fin < ELEMENTS_PER_PAGE) {
                fin++;
            }
        }
    }

    public void deseleccionar(ElementoComplejo e) {
        if (elements.indexOf(e) != -1) {
            this.elements.remove(e);
            if (currentSelectionPage.indexOf(e) != -1) {
                this.currentSelectionPage.remove(e);
            }
            Collections.sort(elements, elementSorter);
            if (!elements.isEmpty()) {
                initCurrentSelectionPage();
            }
            fin--;
        }
    }

    public void initCurrentSelectionPage() {
        //Inicializa la selección actual a la primera unidad.
        currentSelectionPage = new ArrayList<>();
        String nombre = elements.get(0).nombre;
        for (ElementoComplejo e : elements) {
            if (e.nombre.equals(nombre)) {
                currentSelectionPage.add(e);
            } else {
                break;
            }
        }
    }

    public void siguiente_seleccion() {
        //La selección actual cambia a la siguiente unidad. Si la seleccion actual
        //se encuentra al final de los elementos, la seleccion pasa al principio.
        if (!currentSelectionPage.isEmpty()) {
            ElementoComplejo firstElementOfSelection = currentSelectionPage.get(0);
            String initialName = firstElementOfSelection.nombre;
            String nombre = firstElementOfSelection.nombre;
            currentSelectionPage = new ArrayList<>();
            for (int i = elements.indexOf(firstElementOfSelection); i < elements.size(); i++) {
                ElementoComplejo e = elements.get(i);
                //Busco por la lista si hay una unidad con nombre diferente al del primero de la seleccion
                if (!e.nombre.equals(nombre)) {
                    nombre = e.nombre;
                    break;
                }
            }
            if (initialName.equals(nombre)) {
                //No se ha encontrado un nombre diferente. 
                //Solo hay un tipo de unidad en la seleccion
                initCurrentSelectionPage();
            } else {
                for (ElementoComplejo e : elements) {
                    if (e.nombre.equals(nombre)) {
                        currentSelectionPage.add(e);
                    }
                }
            }
        }
    }

    public void renderElementOnMinimap(Graphics g, ElementoComplejo e) {
        float anchura = anchura_mini * (e.anchura / WindowCombat.WORLD_SIZE_X);
        float altura = altura_mini * (e.altura / WindowCombat.WORLD_SIZE_Y);
        float x = x_mini + (anchura_mini - anchura) * ((e.x - e.anchura / 2) / WindowCombat.WORLD_SIZE_X);
        float y = y_mini + (altura_mini - altura) * ((e.y - e.altura / 2) / WindowCombat.WORLD_SIZE_Y);
        g.fillRect(x, y, anchura, altura);
    }

    public void renderElementVisibilityOnMinimap(Graphics g, ElementoComplejo e) {
        float anchura = anchura_mini * (e.anchura / WindowCombat.WORLD_SIZE_X);
        float altura = altura_mini * (e.altura / WindowCombat.WORLD_SIZE_Y);
        float x = x_mini + (anchura_mini - anchura) * ((e.x - e.anchura / 2) / WindowCombat.WORLD_SIZE_X);
        float y = y_mini + (altura_mini - altura) * ((e.y - e.altura / 2) / WindowCombat.WORLD_SIZE_Y);
        float visibilityRadius = anchura_mini * (e.vision / WindowCombat.WORLD_SIZE_X);
        g.fillRoundRect(x - visibilityRadius / 2, y - visibilityRadius / 2, visibilityRadius, visibilityRadius,
                (int) visibilityRadius);
        //g.fillArc(x - visibilityRadius / 2, y - visibilityRadius / 2, visibilityRadius, visibilityRadius, x_minim,
        // y_minim);
    }

    public void renderPlayerElementsVisibilityOnMinimap(Game p, Graphics g, Jugador player) {
        player.unidades.forEach(u -> renderElementVisibilityOnMinimap(g, u));
        player.edificios.forEach(e -> renderElementVisibilityOnMinimap(g, e));
        player.lista_recursos.forEach(r -> renderElementVisibilityOnMinimap(g, r));
    }

    public void renderPlayerElementsOnMinimap(Game p, Graphics g, Jugador player, boolean isMainTeam) {
        g.setColor(player.color);
        for (Unidad u : player.unidades) {
            if (isMainTeam || u.isVisibleByEnemy(p)) {
                renderElementOnMinimap(g, u);
            }
        }
        for (Edificio u : player.edificios) {
            if (isMainTeam || u.isVisibleByEnemy(p)) {
                renderElementOnMinimap(g, u);
            }
        }
        for (Recurso r : player.lista_recursos) {
            if (isMainTeam || r.isVisibleByEnemy(p)) {
                renderElementOnMinimap(g, r);
            }
        }
    }

    public void renderResourceOnMinimap(Game p, Graphics g) {
        for (Recurso r : p.recursos) {
            if (r.isVisibleByMainTeam(p)) {
                Color color;
                if (r.capturador != null) {
                    color = p.getPlayerByName(r.capturador).color;
                } else {
                    color = Color.gray;
                }
                g.setColor(color);
                renderElementOnMinimap(g, r);
                g.setColor(Color.gray);
            }
        }
    }

    public void renderElementsVisibilityOnMinimap(Game p, Graphics g) {
        g.setColor(UI_COLOR);
        p.getMainTeam().forEach(player -> renderPlayerElementsVisibilityOnMinimap(p, g, player));
    }

    public void renderElementsOnMinimap(Game p, Graphics g) {
        p.getMainTeam().forEach(player -> renderPlayerElementsOnMinimap(p, g, player, true));
        for (Jugador player : p.getNonMainTeam()) {
            renderPlayerElementsOnMinimap(p, g, player, false);
        }
        g.setColor(Color.gray);
        float anchura, altura, x, y;
        for (Bestias be : p.bestias) {
            if (be.muerte) {
                anchura = anchura_mini * (ElementosComunes.BEAST_DEATH_IMAGE.getWidth() / WindowCombat.WORLD_SIZE_X);
                altura = altura_mini * (ElementosComunes.BEAST_DEATH_IMAGE.getHeight() / WindowCombat.WORLD_SIZE_Y);
                x = x_mini + (200 - anchura) * (be.x / WindowCombat.WORLD_SIZE_X);
                y = y_mini + (200 - altura) * (be.y / WindowCombat.WORLD_SIZE_Y);
                ElementosComunes.BEAST_DEATH_IMAGE.draw(x, y, anchura, altura);
            } else {
                for (Bestia b : be.contenido) {
                    if (b.isVisibleByEnemy(p)) {
                        renderElementOnMinimap(g, b);
                    }
                }
            }
        }
        renderResourceOnMinimap(p, g);
        g.setColor(Color.white);
    }

    public void renderMinimap(Game p, Graphics g, float initialY) {
        //Rectángulo grande     
        x_mini = WindowCombat.playerX + WindowCombat.VIEWPORT_SIZE_WIDTH - anchura_mini;
        y_mini = initialY;
        //TODO FIX THE VISIBILITY ON THE MINIMAP
        /*g.setColor(NO_VISIBLE_COLOR);
        g.fillRect(x_mini, y_mini, anchura_mini - 1, altura_mini);*/
        g.setColor(Color.white);
        g.drawRect(x_mini, y_mini, anchura_mini - 1, altura_mini);
        //renderElementsVisibilityOnMinimap(p, g);
        renderElementsOnMinimap(p, g);
        //Rectángulo pequeño                
        anchura_minim = anchura_mini * (WindowCombat.VIEWPORT_SIZE_WIDTH / WindowCombat.WORLD_SIZE_X);
        altura_minim = altura_mini * (WindowCombat.VIEWPORT_SIZE_HEIGHT / WindowCombat.WORLD_SIZE_Y);
        x_minim = x_mini + (anchura_mini - anchura_minim) * (WindowCombat.playerX / WindowCombat.offsetMaxX);
        y_minim = y_mini + (altura_mini - altura_minim) * (WindowCombat.playerY / WindowCombat.offsetMaxY);
        g.drawRect(x_minim, y_minim, anchura_minim, altura_minim);
    }

    public void renderComplexElement(Game p, Graphics g, float x, float y, float anchura) {
        //x,y,anchura,altura representan la información del rectángulo donde
        //se escribira el elemento
        ElementoComplejo e = elements.get(0);
        if (e instanceof Edificio && ((Edificio) e).mostrarAyudaFusion) {
            renderFusionHelp(p.getPlayerFromElement(e), g, x, y);
        } else if (e instanceof Unidad && ((Unidad) e).mostrarAyudaPiloto) {
            renderPilotHelp(g, x, y);
        } else {
            if (!(e instanceof Edificio) || ((Edificio) e).cola_construccion.isEmpty()) {
                //Icono
                e.icono.draw(x + 20, y + 50);
                //Barra de vida
                if (e.vida > 0) {
                    g.setColor(Color.green);
                    g.drawString(Integer.toString((int) e.vida), x + 10, y + 50 + e.icono.getHeight() + 10);
                    g.drawString("/", x + 10 + Integer.toString((int) e.vida).length() * 10,
                            y + 50 + e.icono.getHeight() + 10);
                    g.drawString(Integer.toString((int) e.vida_max),
                            x + 10 + Integer.toString((int) e.vida).length() * 10 + 10,
                            y + 50 + e.icono.getHeight() + 10);
                }
                if (e instanceof ElementoEstado && ((ElementoEstado) e).statusCollection.containsStatus(StatusNameEnum.REGENERACION)) {
                    g.drawString("(+1)",
                            x + 10 + Integer.toString((int) e.vida).length() * 10 + 10 + Integer.toString((int) e.vida_max).length() * 10, y + 50 + e.icono.getHeight() + 10);
                }
                g.setColor(Color.white);
            }
            //Nombre
            g.drawString(e.nombre, x + anchura / 2 - e.nombre.length() * 5, y + 10);
            if (e instanceof Edificio) {
                //Cola de construcción
                Edificio ed = (Edificio) e;
                if (!ed.cola_construccion.isEmpty()) {
                    renderConstructionQueue(ed, y, g);
                } else {
                    dibujarStatsEdificio(ed, g, x, y);
                }
            } else {
                if (e instanceof Unidad) {
                    Unidad unidad = (Unidad) e;
                    renderUnitDetailedInfo(g, p, unidad, x, y);
                }
            }
        }
    }

    private void renderUnitDetailedInfo(Graphics g, Game game, Unidad unidad, float x, float y) {
        DecimalFormat d = new DecimalFormat();
        //Estadísticas
        int ataque, defensa;
        if (unidad.escudo > 0) {
            g.setColor(Color.cyan);
            g.drawString(Integer.toString((int) unidad.escudo),
                    x + 10 + Integer.toString((int) unidad.vida).length() * 10 + 10 + Integer.toString((int) unidad.vida_max).length() * 10 + 10, y + 50 + unidad.icono.getHeight() + 10);
            g.setColor(Color.white);
        }
        if (!(unidad instanceof Bestia) && (game.getPlayerFromElement(unidad).raza.equals(RaceEnum.ETERNIUM))) {
            ataque = unidad.getAttackBasedOnEterniumWatch(game.getPlayerFromElement(unidad));
            defensa = unidad.getDefenseBasedOnEterniumWatch(game.getPlayerFromElement(unidad));
        } else {
            ataque = unidad.ataque;
            defensa = unidad.defensa;
        }
        if (unidad.statusCollection.containsStatus(StatusNameEnum.ATAQUE_POTENCIADO)) {
            ataque += unidad.statusCollection.getStatusByBasicInfo(StatusNameEnum.ATAQUE_POTENCIADO).value;
        }
        if (unidad.statusCollection.containsStatus(StatusNameEnum.ATAQUE_DISMINUIDO)) {
            ataque -= unidad.statusCollection.getStatusByBasicInfo(StatusNameEnum.ATAQUE_DISMINUIDO).value;
            if (ataque < 0) {
                ataque = 0;
            }
        }
        if (game.getPlayerFromElement(unidad) != null) {
            if (game.getPlayerFromElement(unidad).raza.equals(RaceEnum.MAESTROS)) {
                if (Manipulador.alentar) {
                    Manipulador m = null;
                    for (Unidad u : game.getPlayerFromElement(unidad).unidades) {
                        if (u.nombre.equals("Manipulador")) {
                            m = (Manipulador) u;
                        }
                    }
                    if (m != null) {
                        if (unidad.alcance(200, m)) {
                            ataque += 5;
                        }
                    }
                }
            }
        }
        float ataquetx = x + 20 + unidad.icono.getWidth() + 150;
        float ataquex = ataquetx + 70;
        float defensatx = ataquex + Integer.toString(ataque).length() * 10 + 60;
        float defensax = defensatx + 80;
        float velocidadtx = ataquetx;
        float velocidadx = velocidadtx + 100;
        float alcancetx = velocidadx + Float.toString(unidad.velocidad).length() * 10 + 60;
        float alcancex = alcancetx + 80;
        float cadenciatx = ataquetx;
        float cadenciax = cadenciatx + 90;
        float linea1y = y + 50;
        float linea2y = linea1y + 30;
        float linea3y = linea2y + 30;
        float linea4y = linea3y + 30;
        if (ataquex < velocidadx) {
            ataquex = velocidadx;
            defensatx = ataquex + Integer.toString(ataque).length() * 10 + 10;
            defensax = defensatx + 80;
        } else {
            if (ataquex > velocidadx) {
                velocidadx = ataquex;
                alcancetx = velocidadx + Float.toString(unidad.velocidad).length() * 10 + 10;
                alcancex = alcancetx + 80;
            }
        }
        if (ataquex < cadenciax) {
            ataquex = cadenciax;
        } else {
            if (ataquex > cadenciax) {
                cadenciax = ataquex;
            }
        }
        if (defensatx < alcancetx) {
            defensatx = alcancetx;
            defensax = defensatx + 80;
        } else {
            if (defensatx > alcancetx) {
                alcancetx = defensatx;
                alcancex = alcancetx + 80;
            }
        }
        g.drawString("Ataque:", ataquetx, linea1y);
        g.drawString(Integer.toString(ataque), ataquex, linea1y);
        g.drawString("Defensa:", defensatx, linea1y);
        g.drawString(Integer.toString(defensa), defensax, linea1y);
        float velocidad;
        if (unidad.statusCollection.containsStatus(StatusNameEnum.RALENTIZACION)) {
            velocidad =
                    unidad.velocidad * (100 - unidad.statusCollection.getStatusByBasicInfo(StatusNameEnum.RALENTIZACION).value) / 100;
        } else {
            velocidad = unidad.velocidad;
        }
        g.drawString("Velocidad:", velocidadtx, linea2y);
        g.drawString(d.format(velocidad), velocidadx, linea2y);
        g.drawString("Alcance:", alcancetx, linea2y);
        g.drawString(Integer.toString(unidad.alcance), alcancex, linea2y);
        g.drawString("Cadencia:", cadenciatx, linea3y);
        g.drawString(d.format(unidad.cadencia), cadenciax, linea3y);
        if (unidad instanceof Manipulador) {
            Manipulador m = (Manipulador) unidad;
            float cdrtx = ataquetx;
            float cdrx = cdrtx + "Enfriamiento:".length() * 10;
            float podertx = cdrx + (Float.toString(m.reduccion_enfriamiento) + "%").length() * 10;
            float poderx = podertx + "Poder mágico".length() * 10;
            if (podertx < defensatx) {
                podertx = defensatx;
                poderx = podertx + "Poder mágico".length() * 10;
            }
            g.drawString("Enfriamiento:", cdrtx, linea4y);
            g.drawString(Float.toString(m.reduccion_enfriamiento) + "%", cdrx, linea4y);
            g.drawString("Poder mágico:", podertx, linea3y);
            g.drawString(Float.toString(m.poder_magico), poderx, linea3y);
            g.setColor(Color.cyan);
            g.drawString(Integer.toString((int) m.mana), x + 10, y + 50 + unidad.icono.getHeight() + 25);
            g.drawString("/", x + 10 + Integer.toString((int) m.mana).length() * 10,
                    y + 50 + unidad.icono.getHeight() + 25);
            g.drawString(Integer.toString((int) m.mana_max),
                    x + 10 + Integer.toString((int) m.mana).length() * 10 + 10,
                    y + 50 + unidad.icono.getHeight() + 25);
            g.drawString("(+" + d.format(m.regeneracion_mana) + ")",
                    x + 10 + Integer.toString((int) m.mana).length() * 10 + 10 + Integer.toString((int) m.mana_max).length() * 10, y + 50 + unidad.icono.getHeight() + 25);
            g.setColor(Color.orange);
            g.drawString(Integer.toString((int) m.experiencia), x + 10, y + 50 + unidad.icono.getHeight() + 40);
            g.drawString("/", x + 10 + Integer.toString((int) m.experiencia).length() * 10,
                    y + 50 + unidad.icono.getHeight() + 40);
            g.drawString(Integer.toString((int) m.experiencia_max),
                    x + 10 + Integer.toString((int) m.experiencia).length() * 10 + 10,
                    y + 50 + unidad.icono.getHeight() + 40);
            g.setColor(Color.white);
        }
        if (unidad instanceof Bestia) {
            Bestia b = (Bestia) unidad;
            float recompensatx = defensatx;
            float recompensax = recompensatx + 110;
            g.drawString("Recompensa:", recompensatx, linea3y);
            g.drawString(Integer.toString(b.recompensa), recompensax, linea3y);
        }
        dibujarEstadosUnidad(unidad, g, x, y);
    }

    private void renderPilotHelp(Graphics g, float x, float y) {
        x = x + 50;
        y = y + 5;
        // First Column
        renderUnitCombinations(g, x, y, ElementosComunes.ARTILLERO_IMAGE, ElementosComunes.BASIC_TURRET_IMAGE,
                ElementosComunes.DEMOLISHER_IMAGE);
        renderUnitCombinations(g, x, y + 40, ElementosComunes.AMPARADOR_IMAGE, ElementosComunes.BASIC_TURRET_IMAGE,
                ElementosComunes.WALL_IMAGE);
        renderUnitCombinations(g, x, y + 80, ElementosComunes.INGENIERO_IMAGE, ElementosComunes.BASIC_TURRET_IMAGE,
                ElementosComunes.REPAIR_IMAGE);
        renderSeparators(g, x, y);
        // Second Column
        renderUnitCombinations(g, x + 200, y, ElementosComunes.ARMERO_IMAGE, ElementosComunes.BASIC_TURRET_IMAGE,
                ElementosComunes.GUTLING_IMAGE);
        renderUnitCombinations(g, x + 200, y + 40, ElementosComunes.OTEADOR_IMAGE, ElementosComunes.BASIC_TURRET_IMAGE,
                ElementosComunes.ARTILLERY_IMAGE);
        renderUnitCombinations(g, x + 200, y + 80, ElementosComunes.EXPLORADOR_IMAGE,
                ElementosComunes.BASIC_TURRET_IMAGE,
                ElementosComunes.VIGILANCE_IMAGE);
        renderSeparators(g, x + 200, y);
        // Third Column
        renderUnitCombinations(g, x + 400, y, ElementosComunes.CORREDOR_IMAGE, ElementosComunes.BASIC_TURRET_IMAGE,
                ElementosComunes.TEMPORAL_DISTORTING_IMAGE);
    }

    private void renderFusionHelp(Jugador clarkPlayer, Graphics g, float x, float y) {
        x = x + 50;
        y = y + 5;
        // First Column : Depredador
        renderUnitCombinations(g, x, y, ElementosComunes.DEPREDADOR_IMAGE, ElementosComunes.DEPREDADOR_IMAGE,
                ElementosComunes.DESMEMBRADOR_IMAGE);
        renderUnitCombinations(g, x, y + 40, ElementosComunes.DEPREDADOR_IMAGE, ElementosComunes.DEVORADOR_IMAGE,
                ElementosComunes.MOLDEADOR_IMAGE);
        renderUnitCombinations(g, x, y + 80, ElementosComunes.DEPREDADOR_IMAGE, ElementosComunes.CAZADOR_IMAGE,
                ElementosComunes.DEFENSOR_IMAGE);
        renderSeparators(g, x, y);
        // Second Column : Devorador
        renderUnitCombinations(g, x + 200, y, ElementosComunes.DEVORADOR_IMAGE, ElementosComunes.DEPREDADOR_IMAGE,
                ElementosComunes.MOLDEADOR_IMAGE);
        renderUnitCombinations(g, x + 200, y + 40, ElementosComunes.DEVORADOR_IMAGE, ElementosComunes.DEVORADOR_IMAGE
                , ElementosComunes.ESCUPIDOR_IMAGE);
        renderUnitCombinations(g, x + 200, y + 80, ElementosComunes.DEVORADOR_IMAGE, ElementosComunes.CAZADOR_IMAGE,
                ElementosComunes.INSPIRADOR_IMAGE);
        renderSeparators(g, x + 200, y);
        // Third Column : Cazador
        renderUnitCombinations(g, x + 400, y, ElementosComunes.CAZADOR_IMAGE, ElementosComunes.DEPREDADOR_IMAGE,
                ElementosComunes.DEFENSOR_IMAGE);
        renderUnitCombinations(g, x + 400, y + 40, ElementosComunes.CAZADOR_IMAGE, ElementosComunes.DEVORADOR_IMAGE,
                ElementosComunes.INSPIRADOR_IMAGE);
        renderUnitCombinations(g, x + 400, y + 80, ElementosComunes.CAZADOR_IMAGE, ElementosComunes.CAZADOR_IMAGE,
                ElementosComunes.AMAESTRADOR_IMAGE);
        // Last row : Matriarca y Unidad de subraza
        if (SubRaceEnum.REGURGITADORES.equals(clarkPlayer.subRace)) {
            renderUnitCombinations(g, x, y + 145, ElementosComunes.DEVORADOR_IMAGE, ElementosComunes.DEVORADOR_IMAGE,
                    ElementosComunes.DEVORADOR_IMAGE, ElementosComunes.REGURGITADOR_IMAGE);
        } else if (SubRaceEnum.DESPEDAZADORES.equals(clarkPlayer.subRace)) {
            renderUnitCombinations(g, x, y + 145, ElementosComunes.DEPREDADOR_IMAGE,
                    ElementosComunes.DEPREDADOR_IMAGE, ElementosComunes.DEPREDADOR_IMAGE,
                    ElementosComunes.DESPEDAZADOR_IMAGE);
        } else {
            renderUnitCombinations(g, x, y + 145, ElementosComunes.CAZADOR_IMAGE, ElementosComunes.CAZADOR_IMAGE,
                    ElementosComunes.CAZADOR_IMAGE, ElementosComunes.RUMIANTE_IMAGE);
        }
        renderUnitCombinations(g, x + 300, y + 145, ElementosComunes.DEPREDADOR_IMAGE, ElementosComunes.CAZADOR_IMAGE
                , ElementosComunes.DEVORADOR_IMAGE, ElementosComunes.MATRIARCA_IMAGE);
    }

    private void renderSeparators(Graphics g, float x, float y) {
        g.drawString("|", x + 190, y);
        g.drawString("|", x + 190, y + 20);
        g.drawString("|", x + 190, y + 40);
        g.drawString("|", x + 190, y + 60);
        g.drawString("|", x + 190, y + 80);
        g.drawString("|", x + 190, y + 100);
        g.drawString("|", x + 190, y + 120);
    }

    private void renderUnitCombinations(Graphics g, float x, float y, Image... images) {
        images[0].draw(x + 5, y + 5);
        g.drawString("+", x + 55, y + 20);
        images[1].draw(x + 75, y + 5);
        if (images.length == 3) {
            g.drawString("=", x + 125, y + 20);
            images[2].draw(x + 145, y + 5);
        } else {
            g.drawString("+", x + 125, y + 20);
            images[2].draw(x + 145, y + 5);
            g.drawString("=", x + 195, y + 20);
            images[3].draw(x + 215, y + 5);
        }
    }

    private void renderConstructionQueue(Edificio ed, float initialY, Graphics g) {
        ed.cola_construccion.get(0).icono.draw(playerX + 230, initialY + 50);
        g.setColor(Color.black);
        g.drawRect(playerX + 230, initialY + 50, ed.cola_construccion.get(0).icono.getWidth(),
                ed.cola_construccion.get(0).icono.getHeight());
        g.setColor(Color.white);
        if (ed.cantidad_produccion != null) {
            g.setColor(Color.green);
            g.drawString(ed.cantidad_produccion.get(0).toString(), playerX + 230, initialY + 50);
            g.setColor(Color.white);
        }
        g.drawRect(playerX + 280, initialY + 70, ed.barra.anchura, ed.barra.altura);
        g.setColor(Color.green);
        g.fillRect(playerX + 280, initialY + 70, ed.barra.anchura * (ed.barra.progreso / ed.barra.getMaximo()),
                ed.barra.altura);
        g.setColor(Color.white);
        g.fillRect(playerX + 280 + ed.barra.anchura * (ed.barra.progreso / ed.barra.getMaximo()), initialY + 70,
                ed.barra.anchura - (ed.barra.anchura * (ed.barra.progreso / ed.barra.getMaximo())), ed.barra.altura);
        for (int i = 1; i < ed.cola_construccion.size(); i++) {
            ed.cola_construccion.get(i).icono.draw(playerX + 230 + (i - 1) * 50, initialY + 100);
            g.setColor(Color.black);
            g.drawRect(playerX + 230 + (i - 1) * 50, initialY + 100, ed.cola_construccion.get(i).icono.getWidth(),
                    ed.cola_construccion.get(i).icono.getHeight());
            g.setColor(Color.white);
            if (ed.cantidad_produccion != null) {
                g.setColor(Color.green);
                g.drawString(ed.cantidad_produccion.get(i).toString(), playerX + 230 + (i - 1) * 50, initialY + 100);
                g.setColor(Color.white);
            }
        }
    }

    private void dibujarStatsEdificio(Edificio ed, Graphics g, float x, float y) {
        float ataquetx;
        float ataquex;
        float defensatx;
        float defensax;
        float linea1y = y + 50;
        if (ed.ataque > 0) {
            ataquetx = x + 20 + ed.icono.getWidth() + 150;
            ataquex = ataquetx + 70;
            defensatx = ataquex + Integer.toString(ed.ataque).length() * 10 + 60;
            defensax = defensatx + 80;
            g.drawString("Ataque:", ataquetx, linea1y);
            g.drawString(Integer.toString(ed.ataque), ataquex, linea1y);
        } else {
            defensatx = x + 20 + ed.icono.getWidth() + 20;
            defensax = defensatx + 80;
        }
        g.drawString("Defensa:", defensatx, linea1y);
        g.drawString(Integer.toString(ed.defensa), defensax, linea1y);
    }

    private void dibujarEstadosUnidad(Unidad unidad, Graphics g, float x, float y) {
        float x_estados = x + 10;
        for (Status es : unidad.statusCollection.contenido) {
            g.drawString(es.name.getName(), x_estados, y + 50 + unidad.icono.getHeight() + 55);
            x_estados += es.name.getName().length() * 10;
            if (es.time > 0) {
                g.drawString(Integer.toString((int) es.timeCounter), x_estados, y + 50 + unidad.icono.getHeight() + 55);
                x_estados += Integer.toString((int) es.timeCounter).length() * 10;
            }
        }
    }

    public void checkActionOnElementHovered(float x, float y) {
        ElementoSimple resultado = getElementHovered(x, y);
        if (mayus) {
            //Eliminar la unidad clickada de la selección actual
            if (resultado != null) {
                for (int i = 0; i < ui.elements.size(); i++) {
                    if (ui.elements.get(i) == resultado) {
                        ui.elements.get(i).deseleccionar();
                        break;
                    }
                }
            }
        } else {
            if (currentSelectionPage.contains(resultado)) {
                //Susbstituir la selección actual por la unidad clickada
                if (resultado != null) {
                    for (int i = 0; i < ui.elements.size(); i++) {
                        if (ui.elements.get(i) != resultado) {
                            ui.elements.get(i).deseleccionar();
                            i = -1;
                        }
                    }
                }
            } else {
                // Otherwise we highlight the group of the element
                currentSelectionPage = new ArrayList<>();
                currentSelectionPage.addAll(elements.stream().filter(e -> e.nombre.equals(resultado.nombre)).collect(Collectors.toList()));
            }
        }
    }

    public ElementoComplejo getElementHovered(float x, float y) {
        for (int i = inicio; i < fin; i++) {
            ElementoComplejo e = elements.get(i);
            float initialX = WindowCombat.playerX + anchura_miniatura + 40 + (i % 8) * (e.icono.getWidth() + 5);
            float initialY =
                    WindowCombat.playerY + WindowCombat.VIEWPORT_SIZE_HEIGHT - UI_HEIGHT - 1 + 5 + ((i % 32) / 8) * (e.icono.getHeight() + 5);
            if ((x >= initialX && x <= initialX + e.icono.getWidth()) && (y >= initialY && y <= initialY + e.icono.getHeight())) {
                return e;
            }
        }
        return null;
    }

    public void render(Game p, Graphics g, List<ControlGroup> controlGroupGroups) {
        float initialY = WindowCombat.playerY + WindowCombat.VIEWPORT_SIZE_HEIGHT - UI_HEIGHT - 1;
        g.setColor(UI_COLOR);
        g.fillRect(WindowCombat.playerX, initialY, WindowCombat.VIEWPORT_SIZE_WIDTH - 1, UI_HEIGHT);
        //Primer Rectángulo: Miniatura.        
        if (!elements.isEmpty()) {
            Image miniatura = currentSelectionPage.get(0).miniatura;
            //g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.7f));
            //g.fillRect(MapaCampo.playerX, MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y, anchura_miniatura,
            // UI_HEIGHT);
            miniatura.draw(WindowCombat.playerX, initialY);
        }
        g.setColor(Color.white);
        g.drawRect(WindowCombat.playerX, initialY, anchura_miniatura, UI_HEIGHT);
        //Segundo Rectángulo: Seleccion.        
        g.drawRect(WindowCombat.playerX + anchura_miniatura, initialY, anchura_seleccion, UI_HEIGHT);
        if (!elements.isEmpty()) {
            if (elements.size() == 1) {
                renderComplexElement(p, g, WindowCombat.playerX + anchura_miniatura, initialY, anchura_seleccion);
            } else {
                ant.x = sig.x = WindowCombat.playerX + anchura_miniatura + 5;
                ant.y = initialY + 5;
                sig.y = initialY + 50;
                ant.render(g);
                g.drawString(Integer.toString(npagina), WindowCombat.playerX + anchura_miniatura + 15, initialY + 30);
                sig.render(g);
                for (int i = inicio; i < fin; i++) {
                    ElementoComplejo e = elements.get(i);
                    if (currentSelectionPage.indexOf(e) != -1) {
                        g.setColor(Color.green);
                        g.drawRect(WindowCombat.playerX + anchura_miniatura + 40 + (i % 8) * (e.icono.getWidth() + 5) - 1, initialY + 5 + ((i % 32) / 8) * (e.icono.getHeight() + 5) - 1, 41, 41);
                    }
                    e.icono.draw(WindowCombat.playerX + anchura_miniatura + 40 + (i % 8) * (e.icono.getWidth() + 5)
                            , initialY + 5 + ((i % 32) / 8) * (e.icono.getHeight() + 5));
                    e.dibujar_mini_barra(g,
                            WindowCombat.playerX + anchura_miniatura + 40 + (i % 8) * (e.icono.getWidth() + 5),
                            initialY + 5 + ((i % 32) / 8) * (e.icono.getHeight() + 5) + e.icono.getHeight() + 5,
                            Color.green);
                }
                g.setColor(Color.white);
            }
        }
        //Tercer Rectángulo: Botones
        g.drawRect(WindowCombat.playerX + anchura_miniatura + anchura_seleccion, initialY, anchura_botones,
                UI_HEIGHT);
        if (!currentSelectionPage.isEmpty() && this.allElementsAreControlledByMainPlayer(p)) {
            ElementoComplejo e = currentSelectionPage.get(0);
            List<BotonComplejo> botones;
            if (!(e instanceof Manipulador) || ((Manipulador) e).enhancementButtons.isEmpty()) {
                botones = e.botones;
            } else {
                Manipulador m = ((Manipulador) e);
                botones = m.enhancementButtons;
                g.drawString("Tienes " + m.enhancementsRemaining + " mejora(s) pendiente(s)",
                        WindowCombat.playerX + anchura_miniatura + anchura_seleccion + 5, initialY + 100);
            }
            if (botones != null && !botones.isEmpty()) {
                for (int i = 0; i < botones.size(); i++) {
                    BotonComplejo boton = botones.get(i);
                    //Las coordenadas de los botones tienen que cambiarse en este bucle para se tenga en cuenta
                    // playerX y playerY
                    boton.x =
                            WindowCombat.playerX + anchura_miniatura + anchura_seleccion + 5 + (i % 4) * (boton.sprite.getWidth() + 5);
                    boton.y = initialY + 5 + ((i % 16) / 4) * (boton.sprite.getHeight() + 5);
                    boton.render(g);
                    if (e instanceof Edificio) {
                        Edificio edificio = (Edificio) e;
                        if ((edificio.unidad_actual != null) && (edificio.unidad_actual.equals(boton.texto))) {
                            g.setColor(Color.green);
                            g.fillRect(boton.x, boton.y + boton.altura, boton.anchura, 5);
                            g.setColor(Color.white);
                        }
                    }
                }
            }
        }
        renderMinimap(p, g, initialY);
        renderControlGroups(g, controlGroupGroups, initialY);
    }

    public void renderControlGroups(Graphics g, List<ControlGroup> controlGroups, float initialY) {
        float initialX = WindowCombat.playerX + WindowCombat.VIEWPORT_SIZE_WIDTH - anchura_mini - 12 * 22;
        //Grupos de control
        for (int i = Input.KEY_1; i <= Input.KEY_0; i++) {
            int numero;
            if (i == 11) {
                numero = 0;
            } else {
                numero = i - 1;
            }
            g.drawRect(initialX + i * 22, initialY - 40, 20, 20);
            g.drawString(Integer.toString(numero), initialX + i * 22, initialY - 40);
            g.drawRect(initialX + i * 22, initialY - 20, 20, 20);
            for (ControlGroup c : controlGroups) {
                if (c.tecla == i) {
                    g.drawString(Integer.toString(c.contenido.size()), initialX + i * 22, initialY - 20);
                    break;
                }
            }
        }
    }

    public Point2D obtener_coordenadas_minimapa(float x_click, float y_click) {
        float x = x_click - (WindowCombat.playerX + anchura_miniatura + anchura_seleccion + anchura_botones);
        float y = y_click - ((int) playerY + VIEWPORT_SIZE_HEIGHT);
        float resultado_x = (x / anchura_mini) * WindowCombat.WORLD_SIZE_X;
        float resultado_y = (y / altura_mini) * WindowCombat.WORLD_SIZE_Y;
        return new Point2D.Float(resultado_x, resultado_y);
    }

    public void movePlayerPerspective(float x_click, float y_click) {
        //Obtengo el x relativo dentro del minimapa
        float x = x_click - (WindowCombat.playerX + anchura_miniatura + anchura_seleccion + anchura_botones);
        float y = y_click - (WindowCombat.playerY + WindowCombat.VIEWPORT_SIZE_HEIGHT - UI.UI_HEIGHT);
        //Transformación a las coordenadas del mapa
        x *= (WindowCombat.WORLD_SIZE_X / anchura_mini);
        y *= (WindowCombat.WORLD_SIZE_Y / altura_mini);
        //Centramos las coordenadas en el minimapa
        x -= (anchura_minim * (WindowCombat.WORLD_SIZE_X / anchura_mini)) / 2;
        y -= (altura_minim * (WindowCombat.WORLD_SIZE_Y / altura_mini)) / 2;
        if (x < WindowCombat.offsetMinX) {
            x = WindowCombat.offsetMinX;
        }
        if (y < WindowCombat.offsetMinY) {
            y = WindowCombat.offsetMinY;
        }
        if (x > WindowCombat.offsetMaxX) {
            x = WindowCombat.offsetMaxX;
        }
        if (y > WindowCombat.offsetMaxY) {
            y = WindowCombat.offsetMaxY;
        }
        WindowCombat.playerX = x;
        WindowCombat.playerY = y;
    }

    public void handleLeftClick(Input input, Game p, int x_click, int y_click) {
        if ((x_click >= (WindowCombat.playerX + anchura_miniatura)) && (x_click <= (WindowCombat.playerX + anchura_seleccion + anchura_miniatura))) {
            float initialY = WindowCombat.playerY + WindowCombat.VIEWPORT_SIZE_HEIGHT - UI.UI_HEIGHT;
            //Segundo cuadrado: Selección actual de unidades
            if (elements.size() == 1 && currentSelectionPage.get(0) instanceof Edificio) {
                //Cancelar producción
                Edificio ed = (Edificio) currentSelectionPage.get(0);
                if (ed.cola_construccion.size() > 0) {
                    float x_e = WindowCombat.playerX + 230;
                    float y_e = initialY + 50;
                    if ((x_click >= x_e) && (x_click <= x_e + 40)) {
                        if ((y_click >= y_e) && (y_click <= y_e + 40)) {
                            ed.cancelProduction(p, ed.cola_construccion.get(0));
                        }
                    }
                    for (int i = 1; i < ed.cola_construccion.size(); i++) {
                        x_e = WindowCombat.playerX + 230 + (i - 1) * 50;
                        y_e = initialY + 100;
                        if ((x_click >= x_e) && (x_click <= x_e + 40)) {
                            if ((y_click >= y_e) && (y_click <= y_e + 40)) {
                                ed.cancelProduction(p, ed.cola_construccion.get(i));
                                break;
                            }
                        }
                    }
                }
            } else {
                checkActionOnElementHovered(x_click, y_click);
                if (ant.isHovered(x_click, y_click)) {
                    previousPage();
                }
                if (sig.isHovered(x_click, y_click)) {
                    nextPage();
                }
            }
        } else {
            //Tercero cuadrado: Botones
            if ((x_click >= (WindowCombat.playerX + anchura_miniatura + anchura_seleccion)) && (x_click <= (WindowCombat.playerX + anchura_miniatura + anchura_seleccion + anchura_botones))) {
                for (ElementoComplejo e : ui.currentSelectionPage) {
                    List<BotonComplejo> botones;
                    if (!(e instanceof Manipulador) || ((Manipulador) e).enhancementButtons.isEmpty()) {
                        botones = e.botones;
                    } else {
                        botones = ((Manipulador) e).enhancementButtons;
                    }
                    for (BotonComplejo b : botones) {
                        if (b.isHovered((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY())) {
                            b.resolucion(elements, e, p);
                        }
                    }
                }
            }
            //Minimapa
            if ((x_click >= (WindowCombat.playerX + anchura_miniatura + anchura_seleccion + anchura_botones)) && (x_click <= (WindowCombat.playerX + WindowCombat.VIEWPORT_SIZE_WIDTH))) {
                //Mover la perspectiva
                ui.movePlayerPerspective(x_click, y_click);
            }
        }
    }

    public BotonComplejo obtainHoveredButton(float x, float y) {
        if (!ui.currentSelectionPage.isEmpty()) {
            List<BotonComplejo> botones;
            ElementoComplejo e = ui.currentSelectionPage.get(0);
            if (!(e instanceof Manipulador) || ((Manipulador) e).enhancementButtons.isEmpty()) {
                botones = e.botones;
            } else {
                botones = ((Manipulador) e).enhancementButtons;
            }
            for (BotonComplejo b : botones) {
                if (new Rectangle2D.Float(b.x, b.y, b.anchura, b.altura).contains(new Point2D.Float(x, y))) {
                    return b;
                }
            }
        }
        return null;
    }

    public boolean allElementsAreControlledByMainPlayer(Game game) {
        Jugador mainPlayer = game.getMainPlayer();
        return this.elements.stream().allMatch(e -> mainPlayer.equals(game.getPlayerFromElement(e)));
    }

    public boolean unitSelected(Game game) {
        return ui.elements.stream().anyMatch(e -> e instanceof Unidad);
    }
}
