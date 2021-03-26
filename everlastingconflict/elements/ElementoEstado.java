/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elements;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.gestion.Game;
import everlastingconflict.status.Status;
import everlastingconflict.status.StatusCollection;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;


public abstract class ElementoEstado extends ElementoComplejo {

    public BehaviourEnum behaviour;
    public StatusCollection statusCollection;

    @Override
    public abstract void destruir(Game p, ElementoAtacante atacante);

    public void comportamiento(Game p, Graphics g, int delta) {
        statusCollection.comportamiento(p, this, delta);
    }

    @Override
    public void render(Animation sprite, Game p, Color c, Input input, Graphics g) {
        super.render(sprite, p, c, input, g);
        g.setColor(Color.black);
        float xStatus = (this.x - this.anchura / 2) + 2;
        for (Status status : statusCollection.contenido) {
            if (status.time > 0) {
                g.drawRect(xStatus - 2, this.y + this.altura / 2 + 5, 21, 21);
                status.icon.draw(xStatus, this.y + this.altura / 2 + 6, 20, 20);
                xStatus += 20;
            }
        }
        g.setColor(Color.white);
    }

    @Override
    public void render(Game p, Color c, Input input, Graphics g) {
        this.render(this.animation, p, c, input, g);
    }
}
