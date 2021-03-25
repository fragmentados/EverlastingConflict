/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos.implementacion;

import everlastingconflict.elementos.ElementoAtacante;
import everlastingconflict.elementos.ElementoComplejo;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.gestion.Vision;
import everlastingconflict.relojes.Reloj;
import everlastingconflict.ventanas.WindowCombat;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;

import static everlastingconflict.RTS.DEBUG_MODE;

/**
 * @author Elías
 */
public class Recurso extends ElementoComplejo {

    public boolean ocupado;
    public String capturador;
    public static int vida_civiles = 100;
    public static int tiempo_captura = 10;

    public final void initImages() {
        Integer contador = 1;
        List<Image> imageList = new ArrayList<>();
        try {
            icono = new Image("media/Iconos/" + nombre + ".png");
            miniatura = new Image("media/Miniaturas/Prueba.png");
            do {
                imageList.add(new Image("media/Recursos/" + nombre + contador + ".png"));
                contador++;
            } while (contador < 5);
        } catch (Exception e) {
        }
        Image[] images = new Image[imageList.size()];
        imageList.toArray(images);
        sprite = new Animation(images, 450, false);
        anchura = sprite.getWidth();
        altura = sprite.getHeight();
        anchura_barra_vida = anchura;
    }

    public Recurso(String n, float x, float y) {
        this.nombre = n;
        if (nombre.equals("Civiles")) {
            this.vida_max = Recurso.vida_civiles;
        }
        this.x = x;
        this.y = y;
        this.ocupado = false;
        initImages();
    }

    @Override
    public void destruir(Partida p, ElementoAtacante atacante) {
        Jugador aliado = p.getPlayerFromElement(this);
        if (aliado != null) {
            if (aliado.lista_recursos.indexOf(this) != -1) {
                aliado.lista_recursos.remove(this);
                p.recursos.add(this);
                if (this.capturador != null) {
                    aliado.diminishFenixPercentage();
                    capturador = null;
                }
                vida = 0;
                this.sprite.setAutoUpdate(false);
            }
        }
    }

    public void recolect(Partida partida, Jugador playerGatherer, Unidad gatherer, float delta) {
        if (this.vida < Recurso.vida_civiles) {
            if ((this.vida + (Recurso.vida_civiles / Recurso.tiempo_captura) * Reloj.TIME_REGULAR_SPEED * delta) >= Recurso.vida_civiles) {
                gatherer.movil = true;
                this.vida = Recurso.vida_civiles;
                this.capturar(partida, playerGatherer, gatherer);
            } else {
                if (this.vida == 0) {
                    playerGatherer.lista_recursos.add(this);
                    partida.recursos.remove(this);
                    gatherer.movil = false;
                }
                this.vida += (Recurso.vida_civiles / Recurso.tiempo_captura) * Reloj.TIME_REGULAR_SPEED * delta;
            }
        }
    }

    public void capturar(Partida p, Jugador j, Unidad gatherer) {
        j.addResources(10);
        capturador = j.nombre;
        j.visiones.add(new Vision(this.x, this.y, 450, 0));
        this.sprite.setAutoUpdate(true);
    }

    @Override
    public void render(Partida p, Color c, Input input, Graphics g) {
        //super.dibujar(p, c, input, g);
        sprite.draw(x - anchura / 2, y - altura / 2);
        g.setColor(Color.black);
        if (DEBUG_MODE) {
            g.drawRect(x - anchura / 2, y - altura / 2, anchura, altura);
        }
        g.setColor(Color.white);
        if (WindowCombat.ui.elementos.indexOf(this) != -1 ||
                (this.nombre.equals("Vision") && this.capturador != null)
                || (this.hitbox(WindowCombat.playerX + input.getMouseX(),
                WindowCombat.playerY + input.getMouseY()))) {
            circulo(g, c);
        }
        if (p.resourceBelongsToPlayer(this) && this.vida > 0) {
            drawLifeBar(g, c, this.vida, this.vida_max, this.y + this.altura / 2);
        }
    }
}
