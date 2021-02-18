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
import everlastingconflict.mapas.VentanaCombate;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;

import static everlastingconflict.RTS.DEBUG_MODE;

/**
 *
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
            } while (contador < 3);
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
        this.capturador = "";
        initImages();

    }

    @Override
    public void destruir(Partida p, ElementoAtacante atacante) {
        if (p.jugador_aliado(this).lista_recursos.indexOf(this) != -1) {
            p.jugador_aliado(this).lista_recursos.remove(this);
            p.recursos.add(this);
            if (!this.capturador.equals("")) {
                p.jugador_aliado(this).removeResources(10);
                capturador = "";
            }
            vida = 0;
            this.sprite.setAutoUpdate(false);
        }
    }

    public void capturar(Jugador j) {
        j.addResources(10);
        capturador = j.nombre;
        j.visiones.add(new Vision(this.x, this.y, 450, 450, 0));
        this.sprite.setAutoUpdate(true);
    }

    @Override
    public void dibujar(Partida p, Color c, Input input, Graphics g) {
        //super.dibujar(p, c, input, g);
        sprite.draw(x - anchura / 2, y - altura / 2);
        g.setColor(Color.black);
        if (DEBUG_MODE) {
            g.drawRect(x - anchura / 2, y - altura / 2, anchura, altura);
        }
        g.setColor(Color.white);
        if (VentanaCombate.ui.elementos.indexOf(this) != -1)  {
            circulo(g, c);
        }
        if (this.hitbox(VentanaCombate.playerX + input.getMouseX(), VentanaCombate.playerY + input.getMouseY())) {
            circulo(g, c);
        }
    }
}
