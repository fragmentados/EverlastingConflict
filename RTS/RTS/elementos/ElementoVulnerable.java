/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.elementos;

import RTS.gestion.Partida;
import RTS.mapas.MapaCampo;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 *
 * @author Elías
 */
public abstract class ElementoVulnerable extends ElementoCoordenadas {

    public int anchura_barra_vida;
    public int altura_barra_vida = 5;
    public float vida, vida_max;
    public int defensa;
    public float experiencia_al_morir;    
    
    public int defensa_eternium() {
        switch (MapaCampo.reloj_eternium.ndivision) {
            case 1:
                return (int) (this.defensa * (75f / 100f));
            case 2:
                return this.defensa;
            case 3:
                return (int) (this.defensa * (150f / 100f));
            default:
                //N_cuarto = 4 -> Defensa mejorada
                if (this.defensa == 0) {
                    return 4;
                } else {
                    return (int) (this.defensa * (500f / 100f));
                }
        }
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
                g.fillRect(x_contador + i * anchura_mini_cuadrado, y_contador, anchura_contador - anchura_hasta_el_momento, altura_barra_vida);
            } else {
                g.fillRect(x_contador + i * anchura_mini_cuadrado, y_contador, anchura_mini_cuadrado, altura_barra_vida);
            }
        }
        g.setColor(Color.black);
        //Dibujar cuadrados
        n_cuadrados = anchura_contador / anchura_mini_cuadrado;
        for (int i = 0; i < n_cuadrados; i++) {
            if ((i + 1) * anchura_mini_cuadrado > anchura_contador) {
                //Penúltimo cuadrado
                float anchura_hasta_el_momento = i * anchura_mini_cuadrado;
                g.drawRect(x_contador + i * anchura_mini_cuadrado, y_contador, anchura_contador - anchura_hasta_el_momento, altura_barra_vida);
            } else {
                g.drawRect(x_contador + i * anchura_mini_cuadrado, y_contador, anchura_mini_cuadrado, altura_barra_vida);
            }
        }
        g.setColor(Color.white);
    }

    public void dibujar_barra_de_vida(Graphics g, Color c) {
        //Rellenar cuadrados
        float anchura_contador;
        anchura_contador = anchura_barra_vida;
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
                g.fillRect(x - anchura / 2 + i * anchura_mini_cuadrado, y + altura / 2, anchura_contador - anchura_hasta_el_momento, altura_barra_vida);
            } else {
                g.fillRect(x - anchura / 2 + i * anchura_mini_cuadrado, y + altura / 2, anchura_mini_cuadrado, altura_barra_vida);
            }
        }
        g.setColor(Color.black);
        //Dibujar cuadrados
        n_cuadrados = anchura_contador / anchura_mini_cuadrado;
        for (int i = 0; i < n_cuadrados; i++) {
            if ((i + 1) * anchura_mini_cuadrado > anchura_contador) {
                //Penúltimo cuadrado
                float anchura_hasta_el_momento = i * anchura_mini_cuadrado;
                g.drawRect(x - anchura / 2 + i * anchura_mini_cuadrado, y + altura / 2, anchura_contador - anchura_hasta_el_momento, altura_barra_vida);
            } else {
                g.drawRect(x - anchura / 2 + i * anchura_mini_cuadrado, y + altura / 2, anchura_mini_cuadrado, altura_barra_vida);
            }
        }
        g.setColor(Color.white);
    }

    @Override
    public void dibujar(Partida p, Color c, Input input, Graphics g) {
        super.dibujar(p, c, input, g);
        dibujar_barra_de_vida(g, c);
    }

    public abstract void destruir(Partida p, ElementoAtacante atacante);
}
