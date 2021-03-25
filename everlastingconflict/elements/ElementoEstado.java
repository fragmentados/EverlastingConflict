/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elements;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.gestion.Game;
import everlastingconflict.status.StatusCollection;
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
    public void render(Game p, Color c, Input input, Graphics g) {
        super.render(p, c, input, g);
    }
}
