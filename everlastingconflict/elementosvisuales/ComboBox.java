/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementosvisuales;

import everlastingconflict.gestion.Partida;
import org.newdawn.slick.*;

import java.util.List;

import static everlastingconflict.elementos.util.ElementosComunes.UI_COLOR;

/**
 * @author Elías
 */
public class ComboBox {

    public float x, y;
    public float width, height = 20;
    public List<ComboBoxOption> options;
    public String label;
    public ComboBoxOption optionSelected;
    public BotonSimple deployButton;
    public boolean isDeployed = false;

    public ComboBox(String label, List<ComboBoxOption> o, float x, float y) {
        this.x = x;
        this.y = y;
        options = o;
        this.label = label;
        optionSelected = options.get(0);
        try {
            deployButton = new BotonSimple(new Image("media/Desplegar.png")) {
                @Override
                public void efecto() {
                    isDeployed = !isDeployed;
                }
            };
        } catch (SlickException e) {

        }
        initProportionsBasedOnOptions();
    }

    public void initProportionsBasedOnOptions() {
        width = 20;
        height = 20;
        for (ComboBoxOption op : options) {
            if (op.text.length() * 10 > width) {
                width = op.text.length() * 10;
            }
        }
        for (ComboBoxOption op : options) {
            if (op.sprite != null) {
                width += 30;
                height = 30;
                break;
            }
        }
        deployButton.x = x + width + 1;
        deployButton.y = y;
        deployButton.altura = height;
    }

    public ComboBoxOption checkOptionSelected(float x, float y) {
        if (isDeployed) {
            int i = 1;
            for (ComboBoxOption op : options) {
                if (x >= this.x && x <= this.x + this.width + this.deployButton.anchura) {
                    if (y >= this.y + i * height + 2 && y <= this.y + (i + 1) * height) {
                        isDeployed = false;
                        if (!optionSelected.equals(op)) {
                            ComboBoxOption previousSelectedOption = optionSelected;
                            optionSelected = op;
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

    public ComboBoxOption getOptionHovered(float x, float y) {
        if (isDeployed) {
            int i = 1;
            for (ComboBoxOption op : options) {
                if (x >= this.x && x <= this.x + this.width + this.deployButton.anchura) {
                    if (y >= this.y + i * height + 2 && y <= this.y + (i + 1) * height) {
                        return op;
                    }
                }
                i++;
            }
        }
        return null;
    }

    public void renderOption(Graphics g, ComboBoxOption o, float x, float y) {
        float xOptionText = x;
        if (o.equals(optionSelected)) {
            g.setColor(new Color(0f, 0f, 0.8f, 0.8f));
        } else {
            g.setColor(Color.white);
        }
        g.fillRect(x, y, width + deployButton.anchura + 1, height);
        g.setColor(Color.black);
        g.drawRect(x, y, width + deployButton.anchura + 1, height);

        if (o.sprite != null) {
            o.sprite.draw(x, y, 30, 30);
            xOptionText += 30;
        }
        g.drawString(o.text, xOptionText, height == 20 ? y : y + height / 4);
    }

    public void render(Input input, Graphics g) {
        float xOptionText = this.x;
        g.setColor(Color.white);
        if (label != null) {
            g.drawString(label, x - label.length() * 10, y);
        }
        g.fillRect(x, y, width, height);
        g.setColor(Color.black);
        g.drawRect(x, y, width, height);
        if (optionSelected.sprite != null) {
            optionSelected.sprite.draw(xOptionText, y, 30, 30);
            xOptionText += 30;
        }
        g.drawString(optionSelected.text, xOptionText, height == 20 ? y : y + height / 4);
        if (isDeployed) {
            g.setColor(Color.black);
            int i = 1;
            for (ComboBoxOption op : options) {
                renderOption(g, op, x, y + i * height + 2);
                i++;
            }
        }
        deployButton.render(g);
        g.setColor(Color.white);
    }

    public void checkIfItsClicked(Input input) {
        if (deployButton.isHovered(input.getMouseX(), input.getMouseY())) {
            deployButton.efecto();
        }
    }

    public void renderHoveredDescription(Input input, Graphics g) {
        ComboBoxOption optionHovered = getOptionHovered(input.getMouseX(), input.getMouseY());
        if (optionHovered != null && optionHovered.description != null) {
            //Origen solo es necesario para botones del Manipulador
            float xDescription = this.x + this.width + this.deployButton.anchura + 10;
            float widthDescription = optionHovered.description.length() * 10 < 400 ?
                    optionHovered.description.length() * 10 : 400;
            String descriptionFormatted = Partida.anadir_saltos_de_linea(optionHovered.description, widthDescription);
            float heightDescription = (descriptionFormatted.chars().filter(ch -> ch == '\n').count() + 1) * 20;
            g.setColor(UI_COLOR);
            g.fillRect(xDescription, this.y, widthDescription, heightDescription);
            g.setColor(Color.white);
            g.drawRect(xDescription, this.y, widthDescription, heightDescription);
            g.drawString(descriptionFormatted, xDescription + 1, this.y);
        }
    }

}
