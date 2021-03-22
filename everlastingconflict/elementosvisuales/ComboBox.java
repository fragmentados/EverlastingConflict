/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementosvisuales;

import org.newdawn.slick.*;

import java.util.List;

/**
 *
 * @author Elías
 */
public class ComboBox {

    public float x, y;
    public float anchura, altura = 20;
    public List<ComboBoxOption> opciones;
    public String label;
    public ComboBoxOption opcion_seleccionada;
    public BotonSimple desplegar;
    public boolean desplegado;

    public ComboBox(String label, List<ComboBoxOption> o, float x, float y) {
        this.x = x;
        this.y = y;
        opciones = o;
        this.label = label;
        opcion_seleccionada = opciones.get(0);
        for (ComboBoxOption op : opciones) {
            if (op.text.length() * 10 > anchura) {
                anchura = op.text.length() * 10;
            }
        }
        for (ComboBoxOption op : opciones) {
            if (op.sprite != null) {
               anchura += 30;
               altura = 30;
               break;
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
            desplegar.altura = altura;
        } catch (SlickException e) {

        }
    }

    public ComboBoxOption checkOptionSelected(float x, float y) {
        if (desplegado) {
            int i = 1;
            for (ComboBoxOption op : opciones) {
                if (x >= this.x && x <= this.x + this.anchura + this.desplegar.anchura) {
                    if (y >= this.y + i * altura + 2 && y <= this.y + (i + 1) * altura) {
                        desplegado = false;
                        if (!opcion_seleccionada.equals(op)) {
                            ComboBoxOption previousSelectedOption = opcion_seleccionada;
                            opcion_seleccionada = op;
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

    public void renderOption(Graphics g, ComboBoxOption o, float x, float y) {
        float xOptionText = x;
        if (o.equals(opcion_seleccionada)) {
            g.setColor(new Color(0f, 0f, 0.8f, 0.8f));
        } else {
            g.setColor(Color.white);
        }
        g.fillRect(x, y, anchura + desplegar.anchura + 1, altura);
        g.setColor(Color.black);
        g.drawRect(x, y, anchura + desplegar.anchura + 1, altura);

        if (o.sprite != null) {
            o.sprite.draw(x, y, 30, 30);
            xOptionText += 30;
        }
        g.drawString(o.text, xOptionText, altura == 20 ? y : y + altura / 4);
    }

    public void render(Graphics g) {
        float xOptionText = this.x;
        g.setColor(Color.white);
        if (label != null) {
            g.drawString(label, x - label.length() * 10, y);
        }
        g.fillRect(x, y, anchura, altura);
        g.setColor(Color.black);
        g.drawRect(x, y, anchura, altura);
        if (opcion_seleccionada.sprite != null) {
            opcion_seleccionada.sprite.draw(xOptionText, y, 30, 30);
            xOptionText += 30;
        }
        g.drawString(opcion_seleccionada.text, xOptionText, altura == 20 ? y : y + altura / 4);
        if (desplegado) {
            g.setColor(Color.black);
            int i = 1;
            for (ComboBoxOption op : opciones) {
                renderOption(g, op, x, y + i * altura + 2);
                i++;
            }
        }
        desplegar.render(g);
        g.setColor(Color.white);
        if (this.opcion_seleccionada.description != null) {
            g.drawString(this.opcion_seleccionada.description, x + 300, y);
        }
    }

    public void checkIfItsClicked(Input input) {
        if(desplegar.isHovered(input.getMouseX(), input.getMouseY())) {
            desplegar.efecto();
        }
    }
}
