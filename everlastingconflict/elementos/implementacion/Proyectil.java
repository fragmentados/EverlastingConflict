/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos.implementacion;

import everlastingconflict.estados.StatusEffectCollection;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Partida;
import everlastingconflict.elementos.ElementoAtacante;
import everlastingconflict.elementos.ElementoMovil;
import everlastingconflict.elementos.ElementoVulnerable;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Elías
 */
public class Proyectil extends ElementoMovil {

    public ElementoAtacante origen;

    public final void iniciar_datos(ElementoAtacante origen) {
        try {
            this.sprite = new Animation(new Image[]{new Image("media/Proyectiles/" + nombre + "_derecha.png")}, 300, false);
            this.izquierda = new Animation(new Image[]{new Image("media/Proyectiles/" + nombre + "_izquierda.png")}, 300, false);
            this.derecha = new Animation(new Image[]{new Image("media/Proyectiles/" + nombre + "_derecha.png")}, 300, false);
            this.arriba = new Animation(new Image[]{new Image("media/Proyectiles/" + nombre + "_arriba.png")}, 300, false);
            this.abajo = new Animation(new Image[]{new Image("media/Proyectiles/" + nombre + "_abajo.png")}, 300, false);
        } catch (SlickException ex) {
            Logger.getLogger(Proyectil.class.getName()).log(Level.SEVERE, null, ex);
        }
        switch (origen.nombre) {
            case "":
                velocidad = 0;
                break;
        }
    }

    public Proyectil(ElementoAtacante origen, ElementoVulnerable objetivo, int ataque) {
        this.nombre = "Proyectil";
        this.objetivo = objetivo;
        this.origen = origen;
        this.ataque = ataque;
        statusEffectCollection = new StatusEffectCollection();
        statusBehaviour = StatusBehaviour.PROYECTIL;
        this.x = this.origen.x;
        this.y = this.origen.y;
        this.velocidad = 3.0f;
        iniciar_datos(origen);

    }

    @Override
    public void destruir(Partida p, ElementoAtacante atacante) {
        p.proyectiles.remove(this);
    }

    @Override
    public void comportamiento(Partida p, Graphics g, int delta) {
        if (movimiento != null) {
            if (movimiento.resolucion(p, delta)) {
            }
        }
        if (objetivo.hitbox(this.x, this.y)) {
            if (origen.dano(p, "Físico", ataque, objetivo)) {
                if (origen instanceof ElementoMovil) {
                    ((ElementoMovil)origen).parar();
                }
            }
            this.destruir(p, null);
        } else {
            anadir_movimiento(objetivo.x, objetivo.y);
        }
    }

}
