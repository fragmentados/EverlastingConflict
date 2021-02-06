/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElementosSlick2D;

import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Elías
 */
public class ComboBox {

    public float x, y;
    public float anchura, altura = 20;
    public List<String> opciones;
    public String opcion_seleccionada;
    public BotonSimple desplegar;
    public boolean desplegado;

    public ComboBox(List<String> o, float x, float y) {
        this.x = x;
        this.y = y;
        opciones = o;
        opcion_seleccionada = opciones.get(0);
        for (String t : opciones) {
            if (t.length() * 10 > anchura) {
                anchura = t.length() * 10;
            }
        }
        desplegado = false;
        try {
            desplegar = new BotonSimple(new Image("datar/Desplegar.png")) {
                @Override
                public void efecto() {
                    desplegado = !desplegado;
                }
            };
            desplegar.x = x + anchura + 1;
            desplegar.y = y;
        } catch (SlickException e) {

        }
    }

    public boolean elegir_opcion(float x, float y) {
        if (desplegado) {
            int i = 1;
            for (String t : opciones) {
                if (x >= this.x && x <= this.x + this.anchura) {
                    if (y >= this.y + i * 20 + 2 && y <= this.y + (i + 1) * 20) {
                        desplegado = false;
                        if (!opcion_seleccionada.equals(t)) {
                            opcion_seleccionada = t;
                            return true;
                        }                        
                        break;
                    }
                }
                i++;
            }
        }
        return false;
    }

    public void dibujar_opcion(Graphics g, String o, float x, float y) {
        if (o.equals(opcion_seleccionada)) {
            g.setColor(new Color(0f, 0f, 0.8f, 0.8f));
        } else {
            g.setColor(Color.white);
        }
        g.fillRect(x, y, anchura + desplegar.anchura + 1, altura);
        g.setColor(Color.black);
        g.drawRect(x, y, anchura + desplegar.anchura + 1, altura);
        g.drawString(o, x, y);
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(x, y, anchura, altura);
        g.setColor(Color.black);
        g.drawRect(x, y, anchura, altura);
        g.drawString(opcion_seleccionada, x, y);
        if (desplegado) {
            g.setColor(Color.black);
            int i = 1;
            for (String t : opciones) {
                dibujar_opcion(g, t, x, y + i * 20 + 2);
                i++;
            }
        }
        desplegar.dibujar(g);
        g.setColor(Color.white);
    }

}
