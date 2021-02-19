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
import everlastingconflict.mapas.MapEnum;
import everlastingconflict.mapas.VentanaCombate;
import everlastingconflict.razas.RaceNameEnum;
import everlastingconflict.relojes.RelojEternium;
import everlastingconflict.relojes.RelojMaestros;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static everlastingconflict.mapas.VentanaCombate.*;

/**
 *
 * @author Elías
 */
public class Partida {

    public Jugador j1, j2;
    public List<Recurso> recursos;
    public List<Bestias> bestias;
    public List<Proyectil> proyectiles;
    public MapEnum map;

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

    public Recurso recurso_mas_cercano(Recurso recurso, String nombre_jugador, String nombre, float x, float y) {
        //Se pasa recurso para que no devuelva el mismo recurso.
        //Se pasa nombre para que el recurso tenga un nombre determinado
        //Se pasa nombre_jugador para que no devuelva un recurso capturado por el jugador
        //Todos los argumentos salvo x e y son opcionales
        Recurso resultado = null;
        float distancia = -1;
        for (Recurso r : recursos) {
            if ((recurso == null) || (r != recurso)) {
                if ((nombre == null) || ((r.nombre.equals(nombre) && (!r.nombre.equals("Civiles") || r.vida == 0)))) {
                    if ((nombre_jugador == null) || (!r.capturador.equals(nombre_jugador))) {
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

    public Jugador jugador_enemigo(Jugador j) {
        if (j == j1) {
            return j2;
        } else {
            return j1;
        }
    }

    public boolean belongsToMainPlayer(ElementoCoordenadas e) {
        if (e instanceof Unidad) {
            return j1.unidades.indexOf(e) != -1;
        } else if (e instanceof Edificio) {
            return j1.edificios.indexOf(e) != -1;
        } else {
            return j1.lista_recursos.indexOf(e) != -1;
        }
    }

    public Jugador jugador_aliado(ElementoCoordenadas e) {
        if (e instanceof Unidad) {
            if (j1.unidades.indexOf(e) != -1) {
                return j1;
            } else {
                return j2;
            }
        } else {
            if (e instanceof Edificio) {
                if (j1.edificios.indexOf(e) != -1) {
                    return j1;
                } else {
                    return j2;
                }
            } else {
                if (j1.lista_recursos.indexOf(e) != -1) {
                    return j1;
                } else {
                    return j2;
                }
            }
        }
    }

    public Jugador jugador_enemigo(ElementoCoordenadas e) {
        if (e instanceof Unidad) {
            if (j1.unidades.indexOf(e) != -1) {
                return j2;
            } else {
                return j1;
            }
        } else {
            if (e instanceof Edificio) {
                if (j1.edificios.indexOf(e) != -1) {
                    return j2;
                } else {
                    return j1;
                }
            } else {
                if (j1.lista_recursos.indexOf(e) != -1) {
                    return j2;
                } else {
                    return j1;
                }
            }
        }
    }

    public Jugador getPlayerByRace(String race) {
        Jugador playerByRace = null;
        if (race.equals(j1.raza)) {
            playerByRace = j1;
        } else if (race.equals(j2.raza)) {
            playerByRace = j2;
        }
        return playerByRace;
    }

    public void invertir() {
        Jugador contador = j1;
        j1 = j2;
        j2 = contador;

    }

    public void initElements(int njugador) {
        //Representan la altura y anchura del mapa
        float width = map.getWidth();
        float height = map.getHeight();
        VentanaCombate.WORLD_SIZE_X = width;
        VentanaCombate.WORLD_SIZE_Y = height;
        VentanaCombate.offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_X;
        VentanaCombate.offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_Y;
        if (njugador == 1) {
            j1.x_inicial = 200;
            j1.y_inicial = 200;
            j2.x_inicial = width - 400;
            j2.y_inicial = height - 400;
        } else {
            j2.x_inicial = 200;
            j2.y_inicial = 200;
            j1.x_inicial = width - 400;
            j1.y_inicial = height - 400;
        }
        float sextox = width / 6;
        float sextoy = height / 6;
        if ((j1.raza.equals(RaceNameEnum.CLARK.getName()) || j2.raza.equals(RaceNameEnum.CLARK.getName()))
            || (j1.raza.equals(RaceNameEnum.MAESTROS.getName()) || j2.raza.equals(RaceNameEnum.MAESTROS.getName()))) {
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
        if (j1.raza.equals("Eternium") || j2.raza.equals("Eternium")) {
            recursos.add(new Recurso("Hierro", sextox * 2, sextoy));
            recursos.add(new Recurso("Hierro", sextox * 4, sextoy));
            recursos.add(new Recurso("Hierro", sextox, sextoy * 2));
            recursos.add(new Recurso("Hierro", sextox * 5, sextoy * 2));
            recursos.add(new Recurso("Hierro", sextox, sextoy * 4));
            recursos.add(new Recurso("Hierro", sextox * 5, sextoy * 4));
            recursos.add(new Recurso("Hierro", sextox * 2, sextoy * 5));
            recursos.add(new Recurso("Hierro", sextox * 4, sextoy * 5));
        }
        if (j1.raza.equals("Fénix") || j2.raza.equals("Fénix")) {
            if (j1.raza.equals("Eternium") || j2.raza.equals("Eternium")) {
                recursos.add(new Recurso("Civiles", sextox, sextoy));
                recursos.add(new Recurso("Civiles", sextox * 3, sextoy));
                recursos.add(new Recurso("Civiles", sextox * 5, sextoy));
                recursos.add(new Recurso("Civiles", sextox, sextoy * 3));
                recursos.add(new Recurso("Civiles", sextox, sextoy * 5));
                recursos.add(new Recurso("Civiles", sextox * 5, sextoy * 3));
                recursos.add(new Recurso("Civiles", sextox * 5, sextoy * 5));
                recursos.add(new Recurso("Civiles", sextox * 3, sextoy * 5));
            } else {
                //if (j1.raza.equals("Clark") || j2.raza.equals("Clark")) {
                recursos.add(new Recurso("Civiles", sextox * 2, sextoy));
                recursos.add(new Recurso("Civiles", sextox * 4, sextoy));
                recursos.add(new Recurso("Civiles", sextox, sextoy * 2));
                recursos.add(new Recurso("Civiles", sextox * 5, sextoy * 2));
                recursos.add(new Recurso("Civiles", sextox, sextoy * 4));
                recursos.add(new Recurso("Civiles", sextox * 5, sextoy * 4));
                recursos.add(new Recurso("Civiles", sextox * 2, sextoy * 5));
                recursos.add(new Recurso("Civiles", sextox * 4, sextoy * 5));
                //}
            }
        }
        // Inicializar relojes
        if (j1.raza.equals("Maestros")) {
            VentanaCombate.crearReloj(new RelojMaestros(j1));
        }
        if (j2.raza.equals("Maestros")) {
            VentanaCombate.crearReloj(new RelojMaestros(j2));
        }

        if (j1.raza.equals("Eternium")) {
            VentanaCombate.crearReloj(new RelojEternium(j1));
        }
        if (j2.raza.equals("Eternium")) {
            VentanaCombate.crearReloj(new RelojEternium(j2));
        }
//        for (Recurso r : recursos) {
//            r.x -= 200;
//            r.y -= 200;
//        }
        j1.initElements(this);
        j2.initElements(this);
    }

    public boolean check_victory_player_1() {
        switch (j2.raza) {
            case "Maestros":
                return !j2.unidades.get(0).nombre.equals("Manipulador");
            default:
                return j2.edificios.isEmpty();
        }
    }

    public boolean check_victory_player_2() {
        switch (j1.raza) {
            case "Maestros":
                return !j1.unidades.get(0).nombre.equals("Manipulador");
            default:
                return j1.edificios.isEmpty();
        }
    }

    public void comportamiento_elementos(Graphics g, int delta) {
        try {
            for (Bestias b : bestias) {
                b.comportamiento(this, g, delta);
            }
            for (Proyectil p : proyectiles) {
                p.comportamiento(this, g, delta);
            }
        } catch (Exception e){}
    }

    public void dibujar_elementos(Graphics g, Input input) {
        for (Recurso r : recursos) {
            if (r.visible(this)) {
                if (r.capturador.equals("")) {
                    r.dibujar(this, Color.blue, input, g);
                }
            }
        }
        for (Bestias b : bestias) {
            b.dibujar(this, Color.blue, input, g);
        }
        for (Proyectil p : proyectiles) {
            if (p.visible(this)) {
                p.dibujar(this, Color.blue, input, g);
            }
        }
    }

    public Partida() {
        recursos = new ArrayList<>();
        bestias = new ArrayList<>();
        proyectiles = new ArrayList<>();
    }

    public Partida(Jugador j, Jugador j2, MapEnum map) {
        this();
        this.j1 = j;
        this.j2 = j2;
        j1.color = Color.green;
        j2.color = Color.red;
        this.map = map;
    }

    public ElementoVulnerable getElementAttackedAtPosition(float x, float y, List<Unidad> seleccionadas) {
        Optional<Unidad> unitAttackedOp = j2.unidades.stream().filter(u -> u.visible(this) && u.hitbox(x, y)).findFirst();
        if (!unitAttackedOp.isPresent()) {
            Optional<Edificio> buildingAttackedOp = j2.edificios.stream().filter(e -> e.visible(this) && e.hitbox(x, y)).findFirst();
            if (!buildingAttackedOp.isPresent()) {
                Optional<Recurso> resourceAttackedOp = j2.lista_recursos.stream().filter(r -> r.visible(this) && r.hitbox(x, y)).findFirst();
                if (!resourceAttackedOp.isPresent()) {
                    Optional<Bestia> beastAttackedOp = this.bestias.stream().flatMap(be -> be.getContenido().stream()).filter(b -> b.visible(this) && b.hitbox(x, y)).findFirst();
                    if (beastAttackedOp.isPresent()) {
                        final ElementoVulnerable beastAttacked = beastAttackedOp.get();
                        seleccionadas.stream().forEach(s -> s.atacar(beastAttacked));
                        return beastAttacked;
                    }
                } else {
                    final ElementoVulnerable resourceAttacked = resourceAttackedOp.get();
                    seleccionadas.stream().forEach(s -> s.atacar(resourceAttacked));
                    return resourceAttacked;
                }
            } else {
                final ElementoVulnerable buildingAttacked = buildingAttackedOp.get();
                seleccionadas.stream().forEach(s -> s.atacar(buildingAttacked));
                return buildingAttacked;
            }
        } else {
            final ElementoVulnerable unitAttacked = unitAttackedOp.get();
            seleccionadas.stream().forEach(s -> s.atacar(unitAttacked));
            return unitAttacked;
        }
        return null;
    }

    private List<ElementoComplejo> getSelectableElements() {
        //Seleccionar un Elemento
        List<ElementoComplejo> selectableElements = new ArrayList<>();
        selectableElements.addAll(j1.unidades);
        selectableElements.addAll(j1.edificios);
        selectableElements.addAll(j1.lista_recursos);
        selectableElements.addAll(j2.unidades);
        selectableElements.addAll(j2.edificios);
        selectableElements.addAll(j2.lista_recursos);
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
        return j1.lista_recursos.contains(r) || j2.lista_recursos.contains(r);
    }

}
