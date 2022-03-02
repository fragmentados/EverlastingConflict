/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elements;

import everlastingconflict.elements.impl.Bestia;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;


public abstract class ElementoVulnerable extends ElementoCoordenadas {

    public int anchura_barra_vida;
    public int altura_barra_vida = 5;
    public float vida, vida_max;
    public int defensa;
    public float experiencia_al_morir;
    public float escudo, escudoInicial;
    public boolean isProyectileAttraction = false;

    public int getDefenseBasedOnEterniumWatch(Jugador aliado) {
        boolean allyHasTemporalControl = aliado.hasTecnologyResearched("Control temporal");
        switch (WindowCombat.eterniumWatch().ndivision) {
            case 1:
                return allyHasTemporalControl ? (int) (this.defensa * (90f / 100f)) :
                        (int) (this.defensa * (75f / 100f));
            case 2:
                return allyHasTemporalControl ? (int) (this.defensa * (150f / 100f)) : this.defensa;
            case 3:
                return allyHasTemporalControl ? (int) (this.defensa * (200f / 100f)) :
                        (int) (this.defensa * (150f / 100f));
            default:
                //N_cuarto = 4 -> Defensa mejorada
                if (this.defensa == 0) {
                    return 4;
                } else {
                    return (int) (this.defensa * (500f / 100f));
                }
        }
    }

    public int getDefense(Jugador aliado) {
        int defense;
        if (!(this instanceof Bestia) && (aliado != null && RaceEnum.ETERNIUM.equals(aliado.raza))) {
            defense = getDefenseBasedOnEterniumWatch(aliado);
        } else {
            defense = defensa;
        }
        return defense;
    }

    public void dibujar_mini_barra(Graphics g, float x_contador, float y_contador, Color c) {
        float anchura_contador;
        anchura_contador = 40;
        float contador = this.vida;
        float n_cuadrados;
        float anchura_mini_cuadrado = 5;
        n_cuadrados = anchura_contador / anchura_mini_cuadrado;
        float vida_por_cuadrado = this.vida_max / n_cuadrados;
        n_cuadrados = 0;
        while (contador > 0) {
            n_cuadrados++;
            contador -= vida_por_cuadrado;
        }
        g.setColor(c);
        for (int i = 0; i < n_cuadrados; i++) {
            if ((i + 1) * anchura_mini_cuadrado > anchura_contador) {
                //Penúltimo cuadrado
                float anchura_hasta_el_momento = i * anchura_mini_cuadrado;
                g.fillRect(x_contador + i * anchura_mini_cuadrado, y_contador,
                        anchura_contador - anchura_hasta_el_momento, altura_barra_vida);
            } else {
                g.fillRect(x_contador + i * anchura_mini_cuadrado, y_contador, anchura_mini_cuadrado,
                        altura_barra_vida);
            }
        }
        g.setColor(Color.black);
        //Dibujar cuadrados
        n_cuadrados = anchura_contador / anchura_mini_cuadrado;
        for (int i = 0; i < n_cuadrados; i++) {
            if ((i + 1) * anchura_mini_cuadrado > anchura_contador) {
                //Penúltimo cuadrado
                float anchura_hasta_el_momento = i * anchura_mini_cuadrado;
                g.drawRect(x_contador + i * anchura_mini_cuadrado, y_contador,
                        anchura_contador - anchura_hasta_el_momento, altura_barra_vida);
            } else {
                g.drawRect(x_contador + i * anchura_mini_cuadrado, y_contador, anchura_mini_cuadrado,
                        altura_barra_vida);
            }
        }
        g.setColor(Color.white);
    }

    public void drawLifeBar(Graphics g, Color c, float initialValue, float maxValue, float y) {
        //Rellenar cuadrados
        float anchura_contador;
        anchura_contador = anchura_barra_vida;
        float contador = initialValue;
        float n_cuadrados;
        float anchura_mini_cuadrado = 5;
        n_cuadrados = anchura_contador / anchura_mini_cuadrado;
        float vida_por_cuadrado = maxValue / n_cuadrados;
        n_cuadrados = 0;
        while (contador > 0) {
            n_cuadrados++;
            contador -= vida_por_cuadrado;
        }
        g.setColor(c);
        for (int i = 0; i < n_cuadrados; i++) {
            if ((i + 1) * anchura_mini_cuadrado > anchura_contador) {
                //Penúltimo cuadrado
                float anchura_hasta_el_momento = i * anchura_mini_cuadrado;
                g.fillRect(x - anchura / 2 + i * anchura_mini_cuadrado, y,
                        anchura_contador - anchura_hasta_el_momento, altura_barra_vida);
            } else {
                g.fillRect(x - anchura / 2 + i * anchura_mini_cuadrado, y, anchura_mini_cuadrado,
                        altura_barra_vida);
            }
        }
        g.setColor(Color.black);
        //Dibujar cuadrados
        n_cuadrados = anchura_contador / anchura_mini_cuadrado;
        for (int i = 0; i < n_cuadrados; i++) {
            if ((i + 1) * anchura_mini_cuadrado > anchura_contador) {
                //Penúltimo cuadrado
                float anchura_hasta_el_momento = i * anchura_mini_cuadrado;
                g.drawRect(x - anchura / 2 + i * anchura_mini_cuadrado, y,
                        anchura_contador - anchura_hasta_el_momento, altura_barra_vida);
            } else {
                g.drawRect(x - anchura / 2 + i * anchura_mini_cuadrado, y, anchura_mini_cuadrado,
                        altura_barra_vida);
            }
        }
        g.setColor(Color.white);
    }

    @Override
    public void render(Animation sprite, Game p, Color c, Input input, Graphics g) {
        super.render(sprite, p, c, input, g);
        if (WindowCombat.ui.elements.indexOf(this) != -1 || this.vida != this.vida_max) {
            drawLifeBar(g, c, this.vida, this.vida_max, this.y + this.altura / 2);
            if (escudo > 0) {
                drawLifeBar(g, Color.blue, this.escudo, this.escudoInicial,
                        this.y + this.altura / 2 + this.altura_barra_vida);
            }
        }
    }

    @Override
    public void render(Game p, Color c, Input input, Graphics g) {
        this.render(this.animation, p, c, input, g);
    }

    public abstract void destruir(Game p, ElementoAtacante atacante);

    public void receiveDamage(float damageToDeal) {
        if (!(this instanceof Unidad) || ((Unidad) this).puede_ser_danado()) {
            if (this instanceof Unidad) {
                if (((Unidad) this).escudo > 0) {
                    float contador = damageToDeal - ((Unidad) this).escudo;
                    ((Unidad) this).escudo -= damageToDeal;
                    if (((Unidad) this).escudo < 0) {
                        ((Unidad) this).escudo = 0;
                    }
                    damageToDeal = contador;
                }
            }
            if (damageToDeal > 0) {
                this.vida -= damageToDeal;
            }
        }
    }

    public void heal(int amountToHeal) {
        if (amountToHeal > 0) {
            if (vida + amountToHeal >= vida_max) {
                vida = vida_max;
            } else {
                vida += amountToHeal;
            }
        }
    }
}
