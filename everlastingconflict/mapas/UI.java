/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.mapas;

import everlastingconflict.elementos.util.ElementosComunes;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elementosvisuales.BotonSimple;
import everlastingconflict.estados.Estado;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.Maestros;
import everlastingconflict.elementos.implementacion.Bestia;
import everlastingconflict.elementos.implementacion.Bestias;
import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.ElementoComplejo;
import everlastingconflict.elementos.ElementoCoordenadas;
import everlastingconflict.elementos.ElementoSimple;
import everlastingconflict.elementos.implementacion.Manipulador;
import everlastingconflict.elementos.implementacion.Recurso;
import everlastingconflict.elementos.implementacion.Unidad;

import static everlastingconflict.mapas.MapaCampo.VIEWPORT_SIZE_Y;
import static everlastingconflict.mapas.MapaCampo.iu;
import static everlastingconflict.mapas.MapaCampo.playerX;
import static everlastingconflict.mapas.MapaCampo.playerY;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

/**
 *
 * @author Elías
 */
class Comparador implements Comparator<ElementoSimple> {

    Comparador() {
    }

    // Overriding the compare method to sort the age 
    @Override
    public int compare(ElementoSimple e1, ElementoSimple e2) {
        if (e1.nombre.equals(e2.nombre)) {
            return 0;
        } else {
            if ((int) e1.nombre.charAt(0) < (int) e2.nombre.charAt(0)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}

public class UI {

    public List<ElementoComplejo> elementos;
    public List<ElementoComplejo> seleccion_actual;
    public BotonSimple sig, ant;
    public int inicio, fin, npagina;

    public float x_mini, y_mini, anchura_mini = 250, altura_mini = 200;
    public float x_minim, y_minim, anchura_minim, altura_minim;
    public float anchura_miniatura = 200, anchura_seleccion = 730, anchura_botones = MapaCampo.VIEWPORT_SIZE_X - 1180;

    public Color color;

    public UI() {
        this.elementos = new ArrayList<>();
        this.seleccion_actual = new ArrayList<>();
        this.sig = new BotonSimple("sig");
        this.ant = new BotonSimple("ant");
        inicio = fin = 0;
        npagina = 1;
        color = new Color(0f, 0f, 0.6f, 1f);
    }

    public void anterior() {
        if (inicio >= 32) {
            fin -= (fin - inicio);
            inicio -= 32;
            npagina--;
        }
    }

    public void siguiente() {
        if ((fin + 32) <= elementos.size()) {
            inicio += 32;
            fin += 32;
            npagina++;
        } else {
            if (fin < elementos.size()) {
                inicio += 32;
                fin = elementos.size();
                npagina++;
            }
        }
    }

    public void seleccionar(ElementoComplejo e) {
        if (elementos.indexOf(e) == -1) {
            this.elementos.add(e);
            Collections.sort(elementos, new Comparador());
            iniciar_seleccion();
            if (fin < 32) {
                fin++;
            }
        }
    }

    public void deseleccionar(ElementoComplejo e) {
        if (elementos.indexOf(e) != -1) {
            this.elementos.remove(e);
            if (seleccion_actual.indexOf(e) != -1) {
                this.seleccion_actual.remove(e);
            }
            Collections.sort(elementos, new Comparador());
            if (!elementos.isEmpty()) {
                iniciar_seleccion();
            }
            fin--;
        }
    }

    public void iniciar_seleccion() {
        //Inicializa la selección actual a la primera unidad.
        seleccion_actual = new ArrayList<>();
        String nombre = elementos.get(0).nombre;
        for (ElementoComplejo e : elementos) {
            if (e.nombre.equals(nombre)) {
                seleccion_actual.add(e);
            } else {
                break;
            }
        }
    }

    public void siguiente_seleccion() {
        //La selección actual cambia a la siguiente unidad. Si la seleccion actual
        //se encuentra al final de los elementos, la seleccion pasa al principio.
        if (!seleccion_actual.isEmpty()) {
            String contador = seleccion_actual.get(0).nombre;
            String nombre = seleccion_actual.get(0).nombre;
            seleccion_actual = new ArrayList<>();
            for (ElementoSimple e : elementos) {
                //Busco por la lista si hay una unidad con nombre diferente al del primero de la seleccion
                if ((int) e.nombre.charAt(0) > (int) nombre.charAt(0)) {
                    nombre = e.nombre;
                    break;
                }
            }
            if (contador.equals(nombre)) {
                //No se ha encontrado un nombre diferente. 
                //Solo hay un tipo de unidad en la seleccion
                iniciar_seleccion();
            } else {
                for (ElementoComplejo e : elementos) {
                    if (e.nombre.equals(nombre)) {
                        seleccion_actual.add(e);
                    }
                }
            }
        }
    }

    public void dibujar_punto(Graphics g, ElementoCoordenadas e) {
        float anchura = anchura_mini * (e.anchura / MapaCampo.WORLD_SIZE_X);
        float altura = altura_mini * (e.altura / MapaCampo.WORLD_SIZE_Y);
        float x = x_mini + (anchura_mini - anchura) * ((e.x - e.anchura / 2) / MapaCampo.WORLD_SIZE_X);
        float y = y_mini + (altura_mini - altura) * ((e.y - e.altura / 2) / MapaCampo.WORLD_SIZE_Y);
        g.fillRect(x, y, anchura, altura);
    }

    public void dibujar_puntos(Partida p, Graphics g) {

        g.setColor(p.j1.color);
        float anchura, altura, x, y;
        for (Unidad u : p.j1.unidades) {
            dibujar_punto(g, u);
            //SysteMapaCampo.out.println("x = " + x + ".y = " + y + ".anchura = " + anchura + ".altura = " + altura + ".");            
        }
        for (Edificio u : p.j1.edificios) {
            dibujar_punto(g, u);
        }
        g.setColor(p.j2.color);
        for (Unidad u : p.j2.unidades) {
            if (u.visible(p)) {
                dibujar_punto(g, u);
            }
        }
        for (Edificio u : p.j2.edificios) {
            if (u.visible(p)) {
                dibujar_punto(g, u);
            }
        }
        g.setColor(Color.gray);
        for (Bestias be : p.bestias) {
            if (be.muerte) {
                anchura = anchura_mini * (MapaCampo.muerte.getWidth() / MapaCampo.WORLD_SIZE_X);
                altura = altura_mini * (MapaCampo.muerte.getHeight() / MapaCampo.WORLD_SIZE_Y);
                x = x_mini + (200 - anchura) * (be.x / MapaCampo.WORLD_SIZE_X);
                y = y_mini + (200 - altura) * (be.y / MapaCampo.WORLD_SIZE_Y);
                MapaCampo.muerte.draw(x, y, anchura, altura);
            } else {
                for (Bestia b : be.contenido) {
                    if (b.visible(p)) {
                        dibujar_punto(g, b);
                    }
                }
            }
        }
        for (Recurso r : p.recursos) {
            if (r.visible(p)) {
                if (r.capturador.equals(p.j1.nombre)) {
                    g.setColor(Color.green);
                } else {
                    if (r.capturador.equals(p.j2.nombre)) {
                        if (r.visible(p)) {
                            g.setColor(Color.red);
                        }
                    }
                }
                dibujar_punto(g, r);
                g.setColor(Color.gray);
            }
        }
        g.setColor(Color.white);
    }

    public void dibujar_minimapa(Partida p, Graphics g) {
        //Rectángulo grande     
        x_mini = MapaCampo.playerX + MapaCampo.VIEWPORT_SIZE_X - anchura_mini;
        y_mini = MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y;
        dibujar_puntos(p, g);
        g.drawRect(x_mini, y_mini, anchura_mini, altura_mini);
        //Rectángulo pequeño                
        anchura_minim = anchura_mini * (MapaCampo.VIEWPORT_SIZE_X / MapaCampo.WORLD_SIZE_X);
        altura_minim = altura_mini * (MapaCampo.VIEWPORT_SIZE_Y / MapaCampo.WORLD_SIZE_Y);
        x_minim = x_mini + (anchura_mini - anchura_minim) * (MapaCampo.playerX / MapaCampo.offsetMaxX);
        y_minim = y_mini + (altura_mini - altura_minim) * (MapaCampo.playerY / MapaCampo.offsetMaxY);
        g.drawRect(x_minim, y_minim, anchura_minim, altura_minim);
    }

    public void dibujar_elemento_complejo(Partida p, Graphics g, float x, float y, float anchura, float altura) {
        //x,y,anchura,altura representan la información del rectángulo donde
        //se escribira el elemento
        ElementoComplejo e = elementos.get(0);
        if (e instanceof Edificio && ((Edificio) e).mostrarAyudaFusion) {
            dibujarAyudaFusion(g, x, y);
        } else {
            if (!(e instanceof Edificio) || ((Edificio) e).cola_construccion.isEmpty()) {
                //Icono
                e.icono.draw(x + 20, y + 50);
                //Barra de vida
                g.setColor(Color.green);
                g.drawString(Integer.toString((int) e.vida), x + 10, y + 50 + e.icono.getHeight() + 10);
                g.drawString("/", x + 10 + Integer.toString((int) e.vida).length() * 10, y + 50 + e.icono.getHeight() + 10);
                g.drawString(Integer.toString((int) e.vida_max), x + 10 + Integer.toString((int) e.vida).length() * 10 + 10, y + 50 + e.icono.getHeight() + 10);
                g.setColor(Color.white);
            }
            //Nombre
            g.drawString(e.nombre, x + anchura / 2 - e.nombre.length() * 5, y + 10);
            if (e instanceof Edificio) {
                //Cola de construcción
                Edificio ed = (Edificio) e;
                if (!ed.cola_construccion.isEmpty()) {
                    dibujarColaConstruccion(ed, g);
                } else {
                    dibujarStatsEdificio(ed, g, x, y);
                }
            } else {
                if (e instanceof Unidad) {
                    DecimalFormat d = new DecimalFormat();
                    //Estadísticas
                    int ataque, defensa;
                    Unidad unidad = (Unidad) e;
                    if (unidad.escudo > 0) {
                        g.setColor(Color.cyan);
                        g.drawString(Integer.toString((int) unidad.escudo), x + 10 + Integer.toString((int) e.vida).length() * 10 + 10 + Integer.toString((int) e.vida_max).length() * 10 + 10, y + 50 + e.icono.getHeight() + 10);
                        g.setColor(Color.white);
                    }
                    if (!(unidad instanceof Bestia) && (p.jugador_aliado(unidad).raza.equals("Eternium"))) {
                        ataque = unidad.ataque_eternium();
                        defensa = unidad.defensa_eternium();
                    } else {
                        ataque = unidad.ataque;
                        defensa = unidad.defensa;
                    }
                    if (unidad.estados.existe_estado(Estado.nombre_ataque_potenciado)) {
                        ataque += unidad.estados.obtener_estado(Estado.nombre_ataque_potenciado).contador;
                    }
                    if (p.jugador_aliado(unidad).raza.equals(Maestros.nombre_raza)) {
                        if (Manipulador.alentar) {
                            Manipulador m = null;
                            for (Unidad u : p.jugador_aliado(unidad).unidades) {
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
                    float ataquetx = x + 20 + e.icono.getWidth() + 150;
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
                    if (unidad.estados.existe_estado(Estado.nombre_ralentizacion)) {
                        velocidad = unidad.velocidad * (100 - unidad.estados.obtener_estado(Estado.nombre_ralentizacion).contador) / 100;
                    } else {
                        velocidad = unidad.velocidad;
                    }
                    g.drawString("Velocidad:", velocidadtx, linea2y);
                    g.drawString(d.format(velocidad), velocidadx, linea2y);
                    g.drawString("Alcance:", alcancetx, linea2y);
                    g.drawString(Integer.toString(unidad.alcance), alcancex, linea2y);
                    g.drawString("Cadencia:", cadenciatx, linea3y);
                    g.drawString(d.format(unidad.cadencia), cadenciax, linea3y);
                    if (e instanceof Manipulador) {
                        Manipulador m = (Manipulador) e;
                        float cdrtx = ataquetx;
                        float cdrx = cdrtx + "Enfriamiento:".length() * 10;
                        float podertx = cdrx + (Float.toString(m.reduccion_enfriamiento) + "%").length() * 10;
                        float poderx = podertx + "Poder mágico".length() * 10;
                        if (podertx < defensatx) {
                            podertx = defensatx;
                            poderx = podertx + "Poder mágico".length() * 10;
                        }
                        g.drawString("Enfriamiento:", cdrtx, linea3y);
                        g.drawString(Float.toString(m.reduccion_enfriamiento) + "%", cdrx, linea3y);
                        g.drawString("Poder mágico:", podertx, linea3y);
                        g.drawString(Float.toString(m.poder_magico), poderx, linea3y);
                        g.setColor(Color.cyan);
                        g.drawString(Integer.toString((int) m.mana), x + 10, y + 50 + e.icono.getHeight() + 25);
                        g.drawString("/", x + 10 + Integer.toString((int) m.mana).length() * 10, y + 50 + e.icono.getHeight() + 25);
                        g.drawString(Integer.toString((int) m.mana_max), x + 10 + Integer.toString((int) m.mana).length() * 10 + 10, y + 50 + e.icono.getHeight() + 25);
                        g.drawString("(+" + d.format(m.regeneracion_mana) + ")", x + 10 + Integer.toString((int) m.mana).length() * 10 + 10 + Integer.toString((int) m.mana_max).length() * 10, y + 50 + e.icono.getHeight() + 25);
                        g.setColor(Color.orange);
                        g.drawString(Integer.toString((int) m.experiencia), x + 10, y + 50 + e.icono.getHeight() + 40);
                        g.drawString("/", x + 10 + Integer.toString((int) m.experiencia).length() * 10, y + 50 + e.icono.getHeight() + 40);
                        g.drawString(Integer.toString((int) m.experiencia_max), x + 10 + Integer.toString((int) m.experiencia).length() * 10 + 10, y + 50 + e.icono.getHeight() + 40);
                        g.setColor(Color.white);
                    }
                    if (e instanceof Bestia) {
                        Bestia b = (Bestia) e;
                        float recompensatx = ataquetx + 40;
                        float recompensax = recompensatx + 110;
                        g.drawString("Recompensa:", recompensatx, linea3y);
                        g.drawString(Integer.toString(b.recompensa), recompensax, linea3y);
                    }
                    dibujarEstadosUnidad(unidad, g, x, y);
                }
            }
        }
    }

    private void dibujarAyudaFusion(Graphics g, float x, float y) {
        x = x + 50;
        y = y + 5;
        // First Column : Depredador
        dibujarCombinacionUnidades(g, x, y, ElementosComunes.DEPREDADOR_IMAGE, ElementosComunes.DEPREDADOR_IMAGE, ElementosComunes.DESMEMBRADOR_IMAGE);
        dibujarCombinacionUnidades(g, x, y + 40, ElementosComunes.DEPREDADOR_IMAGE, ElementosComunes.DEVORADOR_IMAGE, ElementosComunes.MOLDEADOR_IMAGE);
        dibujarCombinacionUnidades(g, x, y + 80, ElementosComunes.DEPREDADOR_IMAGE, ElementosComunes.CAZADOR_IMAGE, ElementosComunes.DEFENSOR_IMAGE);
        // Second Column : Devorador
        dibujarCombinacionUnidades(g, x + 200, y, ElementosComunes.DEVORADOR_IMAGE, ElementosComunes.DEPREDADOR_IMAGE, ElementosComunes.MOLDEADOR_IMAGE);
        dibujarCombinacionUnidades(g, x + 200, y + 40, ElementosComunes.DEVORADOR_IMAGE, ElementosComunes.DEVORADOR_IMAGE, ElementosComunes.REGURGITADOR_IMAGE);
        dibujarCombinacionUnidades(g, x + 200, y + 80, ElementosComunes.DEVORADOR_IMAGE, ElementosComunes.CAZADOR_IMAGE, ElementosComunes.INSPIRADOR_IMAGE);
        // Third Column : Cazador
        dibujarCombinacionUnidades(g, x + 400, y, ElementosComunes.CAZADOR_IMAGE, ElementosComunes.DEPREDADOR_IMAGE, ElementosComunes.DEFENSOR_IMAGE);
        dibujarCombinacionUnidades(g, x + 400, y + 40, ElementosComunes.CAZADOR_IMAGE, ElementosComunes.DEVORADOR_IMAGE, ElementosComunes.INSPIRADOR_IMAGE);
        dibujarCombinacionUnidades(g, x + 400, y + 80, ElementosComunes.CAZADOR_IMAGE, ElementosComunes.CAZADOR_IMAGE, ElementosComunes.AMAESTRADOR_IMAGE);
        // Last row : Matriarc
        dibujarCombinacionUnidades(g, x + 200, y + 145, ElementosComunes.DEPREDADOR_IMAGE, ElementosComunes.CAZADOR_IMAGE, ElementosComunes.DEVORADOR_IMAGE, ElementosComunes.MATRIARCA_IMAGE);
    }

    private void dibujarCombinacionUnidades(Graphics g, float x, float y, Image... images) {
        images[0].draw(x + 5, y + 5);
        g.drawString("+", x + 55, y + 20);
        images[1].draw(x + 75, y + 5);
        if (images.length == 3) {
            g.drawString("=", x + 125, y + 20);
            images[2].draw(x + 145, y + 5);
            g.drawString("|", x + 190, y + 20);
        } else {
            g.drawString("+", x + 125, y + 20);
            images[2].draw(x + 145, y + 5);
            g.drawString("=", x + 195, y + 20);
            images[3].draw(x + 215, y + 5);
        }
    }

    private void dibujarColaConstruccion(Edificio ed, Graphics g) {
        ed.cola_construccion.get(0).icono.draw(playerX + 230, playerY + VIEWPORT_SIZE_Y + 50);
        g.setColor(Color.black);
        g.drawRect(playerX + 230, playerY + VIEWPORT_SIZE_Y + 50, ed.cola_construccion.get(0).icono.getWidth(), ed.cola_construccion.get(0).icono.getHeight());
        g.setColor(Color.white);
        if (ed.cantidad_produccion != null) {
            g.setColor(Color.green);
            g.drawString(ed.cantidad_produccion.get(0).toString(), playerX + 230, playerY + VIEWPORT_SIZE_Y + 50);
            g.setColor(Color.white);
        }
        g.drawRect(playerX + 280, playerY + VIEWPORT_SIZE_Y + 70, ed.barra.anchura, ed.barra.altura);
        g.setColor(Color.green);
        g.fillRect(playerX + 280, playerY + VIEWPORT_SIZE_Y + 70, ed.barra.anchura * (ed.barra.progreso / ed.barra.getMaximo()), ed.barra.altura);
        g.setColor(Color.white);
        g.fillRect(playerX + 280 + ed.barra.anchura * (ed.barra.progreso / ed.barra.getMaximo()), playerY + VIEWPORT_SIZE_Y + 70, ed.barra.anchura - (ed.barra.anchura * (ed.barra.progreso / ed.barra.getMaximo())), ed.barra.altura);
        for (int i = 1; i < ed.cola_construccion.size(); i++) {
            ed.cola_construccion.get(i).icono.draw(playerX + 230 + (i - 1) * 50, playerY + VIEWPORT_SIZE_Y + 100);
            g.setColor(Color.black);
            g.drawRect(playerX + 230 + (i - 1) * 50, playerY + VIEWPORT_SIZE_Y + 100, ed.cola_construccion.get(i).icono.getWidth(), ed.cola_construccion.get(i).icono.getHeight());
            g.setColor(Color.white);
            if (ed.cantidad_produccion != null) {
                g.setColor(Color.green);
                g.drawString(ed.cantidad_produccion.get(i).toString(), playerX + 230 + (i - 1) * 50, playerY + VIEWPORT_SIZE_Y + 100);
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
        for (Estado es : unidad.estados.contenido) {
            g.drawString(es.tipo, x_estados, y + 50 + unidad.icono.getHeight() + 55);
            x_estados += es.tipo.length() * 10;
            if (es.tiempo > 0) {
                g.drawString(Integer.toString((int) es.tiempo_contador), x_estados, y + 50 + unidad.icono.getHeight() + 55);
                x_estados += Integer.toString((int) es.tiempo_contador).length() * 10;
            }
        }
    }

    public void dibujar_UI(Partida p, Graphics g) {
        float altura = 200;
        g.setColor(new Color(0f, 0f, 0.6f, 1f));
        g.fillRect(MapaCampo.playerX, MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y, MapaCampo.VIEWPORT_SIZE_X, altura);
        //Primer Rectángulo: Miniatura.        
        if (!elementos.isEmpty()) {
            Image miniatura = seleccion_actual.get(0).miniatura;
            //g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.7f));
            //g.fillRect(MapaCampo.playerX, MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y, anchura_miniatura, altura);
            miniatura.draw(MapaCampo.playerX, MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y);
        }
        g.setColor(Color.white);
        g.drawRect(MapaCampo.playerX, MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y, anchura_miniatura, altura);
        //Segundo Rectángulo: Seleccion.        
        g.drawRect(MapaCampo.playerX + anchura_miniatura, MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y, anchura_seleccion, altura);
        if (!elementos.isEmpty()) {
            if (elementos.size() == 1) {
                dibujar_elemento_complejo(p, g, MapaCampo.playerX + anchura_miniatura, MapaCampo.playerY + +MapaCampo.VIEWPORT_SIZE_Y, anchura_seleccion, altura);
            } else {
                ant.x = sig.x = MapaCampo.playerX + anchura_miniatura + 5;
                ant.y = MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y + 5;
                sig.y = MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y + 50;
                ant.dibujar(g);
                g.drawString(Integer.toString(npagina), MapaCampo.playerX + anchura_miniatura + 15, MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y + 30);
                sig.dibujar(g);
                g.setColor(Color.green);
                for (int i = inicio; i < fin; i++) {
                    ElementoComplejo e = elementos.get(i);
                    if (seleccion_actual.indexOf(e) != -1) {
                        g.drawRect(MapaCampo.playerX + anchura_miniatura + 40 + (i % 8) * (e.icono.getWidth() + 5) - 1, MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y + 5 + ((i % 32) / 8) * (e.icono.getHeight() + 5) - 1, 41, 41);
                    }
                    e.icono.draw(MapaCampo.playerX + anchura_miniatura + 40 + (i % 8) * (e.icono.getWidth() + 5), MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y + 5 + ((i % 32) / 8) * (e.icono.getHeight() + 5));
                    e.dibujar_mini_barra(g, MapaCampo.playerX + anchura_miniatura + 40 + (i % 8) * (e.icono.getWidth() + 5), MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y + 5 + ((i % 32) / 8) * (e.icono.getHeight() + 5) + e.icono.getHeight() + 5, Color.green);
                }
                g.setColor(Color.white);
            }
        }
        //Tercer Rectángulo: Botones
        g.drawRect(MapaCampo.playerX + anchura_miniatura + anchura_seleccion, MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y, anchura_botones, altura);
        if (!seleccion_actual.isEmpty()) {
            ElementoComplejo e = seleccion_actual.get(0);
            List<BotonComplejo> botones;
            if (!(e instanceof Manipulador) || ((Manipulador) e).botones_mejora.isEmpty()) {
                botones = e.botones;
            } else {
                botones = ((Manipulador) e).botones_mejora;
            }
            for (int i = 0; i < botones.size(); i++) {
                BotonComplejo boton = botones.get(i);
                //Las coordenadas de los botones tienen que cambiarse en este bucle para se tenga en cuenta playerX y playerY
                boton.x = MapaCampo.playerX + anchura_miniatura + anchura_seleccion + 5 + (i % 4) * (boton.sprite.getWidth() + 5);
                boton.y = MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y + 5 + ((i % 16) / 4) * (boton.sprite.getHeight() + 5);
                boton.dibujar(g);
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
        dibujar_minimapa(p, g);
    }

    public Point2D obtener_coordenadas_minimapa(float x_click, float y_click) {
        float x = x_click - (MapaCampo.playerX + anchura_miniatura + anchura_seleccion + anchura_botones);
        float y = y_click - ((int) playerY + VIEWPORT_SIZE_Y);
        float resultado_x = (x / anchura_mini) * MapaCampo.WORLD_SIZE_X;
        float resultado_y = (y / altura_mini) * MapaCampo.WORLD_SIZE_Y;
        return new Point2D.Float(resultado_x, resultado_y);
    }

    public void mover_perspectiva(float x_click, float y_click) {
        //Obtengo el x relativo dentro del minimapa
        float x = x_click - (MapaCampo.playerX + anchura_miniatura + anchura_seleccion + anchura_botones);
        float y = y_click - (MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y);
        //Transformación a las coordenadas del mapa
        x *= (MapaCampo.WORLD_SIZE_X / anchura_mini);
        y *= (MapaCampo.WORLD_SIZE_Y / altura_mini);
        //System.out.println("x = " + x + ".");
        //System.out.println("y = " + y + ".");
        //Centramos las coordenadas en el minimapa
        x -= (anchura_minim * (MapaCampo.WORLD_SIZE_X / anchura_mini)) / 2;
        y -= (altura_minim * (MapaCampo.WORLD_SIZE_Y / altura_mini)) / 2;
        if (x < MapaCampo.offsetMinX) {
            x = MapaCampo.offsetMinX;
        }
        if (y < MapaCampo.offsetMinY) {
            y = MapaCampo.offsetMinY;
        }
        if (x > MapaCampo.offsetMaxX) {
            x = MapaCampo.offsetMaxX;
        }
        if (y > MapaCampo.offsetMaxY) {
            y = MapaCampo.offsetMaxY;
        }
        MapaCampo.playerX = x;
        MapaCampo.playerY = y;
    }

    public void gestionar_click_izquierdo(Input input, Partida p, int x_click, int y_click, boolean mayus) {
        if ((x_click >= (MapaCampo.playerX + anchura_miniatura)) && (x_click <= (MapaCampo.playerX + anchura_seleccion + anchura_miniatura))) {
            //Segundo cuadrado: Selección actual de unidades
            if (elementos.size() == 1 && seleccion_actual.get(0) instanceof Edificio) {
                //Cancelar producción
                Edificio ed = (Edificio) seleccion_actual.get(0);
                if (ed.cola_construccion.size() > 0) {
                    float x_e = MapaCampo.playerX + 230;
                    float y_e = MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y + 50;
                    if ((x_click >= x_e) && (x_click <= x_e + 40)) {
                        if ((y_click >= y_e) && (y_click <= y_e + 40)) {
                            ed.cancelar_produccion(p, ed.cola_construccion.get(0));
                        }
                    }
                    for (int i = 1; i < ed.cola_construccion.size(); i++) {
                        x_e = MapaCampo.playerX + 230 + (i - 1) * 50;
                        y_e = MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y + 100;
                        if ((x_click >= x_e) && (x_click <= x_e + 40)) {
                            if ((y_click >= y_e) && (y_click <= y_e + 40)) {
                                ed.cancelar_produccion(p, ed.cola_construccion.get(i));
                                break;
                            }
                        }
                    }
                }
            } else {
                ElementoSimple resultado = null;
                for (int i = 0; i < iu.elementos.size(); i++) {
                    ElementoSimple e = iu.elementos.get(i);
                    float x_e = playerX + anchura_miniatura + 40 + (i % 8) * (e.icono.getWidth() + 5);
                    float y_e = playerY + VIEWPORT_SIZE_Y + 5 + ((i % 32) / 8) * (e.icono.getHeight() + 5);
                    if ((x_click >= x_e) && (x_click <= x_e + 40)) {
                        if ((y_click >= y_e) && (y_click <= y_e + 40)) {
                            resultado = e;
                            break;
                        }
                    }
                }
                if (mayus) {
                    //Eliminar la unidad clickada de la selección actual
                    if (resultado != null) {
                        for (int i = 0; i < iu.elementos.size(); i++) {
                            if (iu.elementos.get(i) == resultado) {
                                iu.elementos.get(i).deseleccionar();
                                break;
                            }
                        }
                    }
                } else {
                    //Susbstituir la selección actual por la unidad clickada
                    if (resultado != null) {
                        for (int i = 0; i < iu.elementos.size(); i++) {
                            if (iu.elementos.get(i) != resultado) {
                                iu.elementos.get(i).deseleccionar();
                                i = -1;
                            }
                        }
                    }
                }
                if (ant.presionado(x_click, y_click)) {
                    anterior();
                }
                if (sig.presionado(x_click, y_click)) {
                    siguiente();
                }
            }
        } else {
            //Tercero cuadrado: Botones
            if ((x_click >= (MapaCampo.playerX + anchura_miniatura + anchura_seleccion)) && (x_click <= (MapaCampo.playerX + anchura_miniatura + anchura_seleccion + anchura_botones))) {
                for (ElementoComplejo e : iu.seleccion_actual) {
                    List<BotonComplejo> botones;
                    if (!(e instanceof Manipulador) || ((Manipulador) e).botones_mejora.isEmpty()) {
                        botones = e.botones;
                    } else {
                        botones = ((Manipulador) e).botones_mejora;
                    }
                    for (BotonComplejo b : botones) {
                        if (b.presionado((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY())) {
                            b.resolucion(elementos, e, p);
                        }
                    }
                }
            }
            //Minimapa
            if ((x_click >= (MapaCampo.playerX + anchura_miniatura + anchura_seleccion + anchura_botones)) && (x_click <= (MapaCampo.playerX + MapaCampo.VIEWPORT_SIZE_X))) {
                //Mover la perspectiva
                iu.mover_perspectiva(x_click, y_click);
            }
        }
    }

    public BotonComplejo obtener_boton_seleccionado(float x, float y) {
        if (!iu.seleccion_actual.isEmpty()) {
            List<BotonComplejo> botones;
            ElementoComplejo e = iu.seleccion_actual.get(0);
            if (!(e instanceof Manipulador) || ((Manipulador) e).botones_mejora.isEmpty()) {
                botones = e.botones;
            } else {
                botones = ((Manipulador) e).botones_mejora;
            }
            for (BotonComplejo b : botones) {
                if (new Rectangle2D.Float(b.x, b.y, b.anchura, b.altura).contains(new Point2D.Float(x, y))) {
                    return b;
                }
            }
        }
        return null;
    }
}
