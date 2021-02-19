/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos;

import everlastingconflict.RTS;
import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.gestion.Vision;
import everlastingconflict.mapas.VentanaCombate;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import java.awt.geom.Rectangle2D;

/**
 *
 * @author Elías
 */
public abstract class ElementoCoordenadas extends ElementoSimple {

    public float x, y;
    public int anchura, altura;    
    
    public boolean visible(Partida p) {
        Jugador enemigo = p.jugador_enemigo(this);
        for (Edificio e : enemigo.edificios) {
            if (e.alcance(e.vision / 2, this)) {
                return true;
            }
        }
        for (Unidad u : enemigo.unidades) {
            if (u.alcance(u.vision / 2, this)) {
                return true;
            }
        }
        for (Vision v : enemigo.visiones) {
            if (v.forma.contains(new Rectangle2D.Float(x - anchura / 2, y - altura / 2, anchura, altura))) {
                return true;
            }
        }
        return false;
    }

    public boolean valueInRange(float value, float min, float max) {
        return (value >= min) && (value <= max);
    }

    public boolean en_rango(float xi, float yi, float xf, float yf) {
        //Recibe el punto inicial y el punto final de un rectángulo que queremos
        //checkear si se choca con el rectángulo del elemento
        boolean xOverlap = valueInRange(xi, x - anchura / 2, x + anchura / 2) || valueInRange(x - anchura / 2, xi, xf);
        boolean yOverlap = valueInRange(yi, y - altura / 2, y + altura / 2) || valueInRange(y - altura / 2, yi, yf);
        return xOverlap && yOverlap;
    }

    public boolean hitbox(float x, float y) {
        //Devuelve true si la unidad se encuentra en x, y.
        if ((x >= (this.x - anchura / 2)) && (x <= (this.x + anchura / 2))) {
            if ((y >= (this.y - altura / 2)) && (y <= (this.y + altura / 2))) {
                return true;
            }
        }
        return false;
    }

    public void circulo_extendido(Graphics g, Color c) {
        //Se ejecuta cuando se hace click derecho sobre un elemento
        g.setColor(c);
        g.drawOval(x - (anchura + 10) / 2, y + altura / 2, anchura + 10, 20);
        g.setColor(Color.white);
    }

    public void circulo(Graphics g, Color c) {
        g.setColor(c);
        g.drawOval(x - anchura / 2, y + altura / 2, anchura, 20);
        g.setColor(Color.white);
    }

    public boolean alcance(float x, float y, float n) {
        //x e y representan las coordenadas iniciales desde las que se comprueba
        //this representa el objetivo
        float distancia_x, distancia_y;
        if (x < this.x) {
            distancia_x = (this.x - this.anchura / 2) - x;
        } else {
            if (x > this.x) {
                distancia_x = x - (this.x + this.anchura / 2);
            } else {
                distancia_x = 0;
            }
        }
        if (y < this.y) {
            distancia_y = (this.y - this.altura / 2) - y;
        } else {
            if (y > this.y) {
                distancia_y = y - (this.y + this.altura / 2);
            } else {
                distancia_y = 0;
            }
        }

        return (distancia_x + distancia_y) <= n / 2;
    }

    public boolean alcance(float n, ElementoCoordenadas atacada) {
        float distancia_x, distancia_y;
        if (this.x < atacada.x) {
            distancia_x = (atacada.x - atacada.anchura / 2) - (this.x + this.anchura / 2);
        } else {
            if (this.x > atacada.x) {
                distancia_x = (this.x - this.anchura / 2) - (atacada.x + atacada.anchura / 2);
            } else {
                distancia_x = 0;
            }
        }
        if (this.y < atacada.y) {
            distancia_y = (atacada.y - atacada.altura / 2) - (this.y + this.altura / 2);
        } else {
            if (this.y > atacada.y) {
                distancia_y = (this.y - this.altura / 2) - (atacada.y + atacada.altura / 2);
            } else {
                distancia_y = 0;
            }
        }
        return (distancia_x + distancia_y) <= n;
    }

    public ElementoComplejo colision(Partida p, float x, float y) {
        //Devuelve true si existe alguna otra unidad en las coordenadas especificadas
        ElementoComplejo resultado = new Unidad("No hay");
        for (Edificio e : p.j1.edificios) {
            if (e != this) {
                if (e.en_rango(x - anchura / 2, y - altura / 2, x + anchura / 2, y + altura / 2)) {
                    resultado = e;
                    return resultado;
                }
            }
        }
        for (Edificio e : p.j2.edificios) {
            if (e != this) {
                if (e.en_rango(x - anchura / 2, y - altura / 2, x + anchura / 2, y + altura / 2)) {
                    resultado = e;
                    return resultado;
                }
            }
        }
        for (Unidad u : p.j1.unidades) {
            if (u != this) {
                if (u.en_rango(x - anchura / 2, y - altura / 2, x + anchura / 2, y + altura / 2)) {
                    resultado = u;
                    return resultado;
                }
            }
        }
        for (Unidad u : p.j2.unidades) {
            if (u != this) {
                if (u.en_rango(x - anchura / 2, y - altura / 2, x + anchura / 2, y + altura / 2)) {
                    resultado = u;
                    return resultado;
                }
            }
        }
        return resultado;
    }

    public void dibujar(Partida p, Color c, Input input, Graphics g){
        sprite.draw(x - anchura / 2, y - altura / 2);
        g.setColor(Color.black);
        if (RTS.DEBUG_MODE) {
            g.drawRect(x - anchura / 2, y - altura / 2, anchura, altura);
        }
        g.setColor(Color.white);
        if (VentanaCombate.ui.elementos.indexOf(this) != -1)  {
            circulo(g, c);
        }
        if(this.hitbox(VentanaCombate.playerX + input.getMouseX(), VentanaCombate.playerY + input.getMouseY())){
            circulo_extendido(g,c);
        }
    }
}
