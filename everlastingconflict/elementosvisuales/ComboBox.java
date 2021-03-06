/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementosvisuales;

import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Elías
 */
public class ComboBox {

    public float x, y;
    public float anchura, altura = 20;
    public List<String> opciones;
    public String label;
    public String opcion_seleccionada;
    public BotonSimple desplegar;
    public boolean desplegado;
    public List<String> descriptions = new ArrayList<>();

    public ComboBox(String label, List<String> o, float x, float y) {
        this.x = x;
        this.y = y;
        opciones = o;
        this.label = label;
        opcion_seleccionada = opciones.get(0);
        for (String t : opciones) {
            if (t.length() * 10 > anchura) {
                anchura = t.length() * 10;
            }
        }
        desplegado = false;
        try {
            desplegar = new BotonSimple(new Image("media/Desplegar.png")) {
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

    public ComboBox(String label, List<String> o, List<String> descriptions, float x, float y) {
        this(label, o, x, y);
        this.descriptions = descriptions;
    }

    public String checkOptionSelected(float x, float y) {
        if (desplegado) {
            int i = 1;
            for (String t : opciones) {
                if (x >= this.x && x <= this.x + this.anchura) {
                    if (y >= this.y + i * 20 + 2 && y <= this.y + (i + 1) * 20) {
                        desplegado = false;
                        if (!opcion_seleccionada.equals(t)) {
                            String previousSelectedOption = opcion_seleccionada;
                            opcion_seleccionada = t;
                            return previousSelectedOption;
                        }                        
                        break;
                    }
                }
                i++;
            }
        }
        return null;
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

    public void render(Graphics g) {
        g.setColor(Color.white);
        if (label != null) {
            g.drawString(label, x - label.length() * 10, y);
        }
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
        desplegar.render(g);
        g.setColor(Color.white);
        if ((descriptions.size() - 1) >= this.opciones.indexOf(opcion_seleccionada)) {
            g.drawString(descriptions.get(this.opciones.indexOf(opcion_seleccionada)), x + 300, y);
        }
    }

    public void checkIfItsClicked(Input input) {
        if(desplegar.isHovered(input.getMouseX(), input.getMouseY())) {
            desplegar.efecto();
        }
    }
}
