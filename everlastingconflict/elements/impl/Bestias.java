/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elements.impl;

import everlastingconflict.gestion.Game;
import everlastingconflict.watches.Reloj;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.List;


public class Bestias {

    public List<Bestia> contenido, contenido_contador;
    public float tiempo, tiempo_contador;
    public float x, y;
    public boolean muerte;

    public List<Bestia> getContenido() {
        return contenido;
    }

    public void cambiar_coordenadas(float x, float y) {
        float distancia_x = x - this.x;
        float distancia_y = y - this.y;
        for (Bestia b : contenido) {
            b.cambiar_coordenadas(b.x + distancia_x, b.y + distancia_y);
        }
        for (Bestia b : contenido_contador) {
            b.cambiar_coordenadas(b.x + distancia_x, b.y + distancia_y);
        }
        this.x = x;
        this.y = y;
    }

    //x e y representan el centro del grupo
    public final void Grupo1(float x, float y) {
        contenido.add(new Bestia("Alpha", x, y));
        int distancia = contenido.get(0).anchura + 10;
        contenido.add(new Bestia("Alpha", x - distancia, y));
        contenido.add(new Bestia("Alpha", x + distancia, y));
        tiempo = 60;
    }

    public final void Grupo2(float x, float y) {
        Bestia contador = new Bestia("Beta");
        int distancia = contador.anchura + 10;
        contenido.add(new Bestia("Beta", x - (distancia / 2), y));
        contenido.add(new Bestia("Beta", x + (distancia / 2), y));
        tiempo = 120;
    }

    public final void Grupo3(float x, float y) {
        contenido.add(new Bestia("Gamma", x, y));
        tiempo = 180;
    }

    public final void Grupo4(float x, float y) {
        Bestia contador = new Bestia("Ommega");
        int distancia = contador.anchura + 10;
        contenido.add(new Bestia("Ommega", x - (distancia / 2), y));
        contenido.add(new Bestia("Ommega", x + (distancia / 2), y));
        tiempo = 120;
    }

    public Bestias(String n, float x, float y) {
        contenido = new ArrayList<>();
        contenido_contador = new ArrayList<>();
        muerte = false;
        this.x = x;
        this.y = y;
        switch (n) {
            case "Grupo1":
                Grupo1(x, y);
                break;
            case "Grupo2":
                Grupo2(x, y);
                break;
            case "Grupo3":
                Grupo3(x, y);
                break;
            case "Grupo4":
                Grupo4(x, y);
                break;
        }
        tiempo_contador = tiempo;
        for (Bestia b : contenido) {
            contenido_contador.add(new Bestia(b.nombre, b.x, b.y));
        }
    }

    public void comportamiento(Game p, Graphics g, int delta) {
        if (muerte) {
            if (tiempo_contador > 0) {
                tiempo_contador -= Reloj.TIME_REGULAR_SPEED * delta;
            } else {
                tiempo_contador = tiempo;
                for (Bestia b : contenido_contador) {
                    contenido.add(new Bestia(b.nombre, b.x, b.y));
                }
                muerte = false;
            }
        } else {
            for (Bestia b : contenido) {
                b.comportamiento(p, g, delta);
            }
        }
    }

    public void dibujar(Game p, Color c, Input input, Graphics g) {
        if (muerte) {
            g.setColor(Color.white);
            String tiempo_dibujar = Reloj.tiempo_a_string(tiempo_contador);
            g.drawString(tiempo_dibujar, x, y);
        } else {
            for (Bestia b : contenido) {
                if (b.isVisibleByEnemy(p)) {
                    b.render(p, c, input, g);
                }
            }
        }
    }

}
