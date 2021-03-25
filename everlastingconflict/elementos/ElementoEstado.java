/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos;

import everlastingconflict.estados.StatusEffectCollection;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Partida;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 *
 * @author Elías
 */
public abstract class ElementoEstado extends ElementoComplejo {

    public StatusBehaviour statusBehaviour;
    public StatusEffectCollection statusEffectCollection;

    @Override
    public abstract void destruir(Partida p, ElementoAtacante atacante);

    public void comportamiento(Partida p, Graphics g, int delta) {
        statusEffectCollection.comportamiento(p, this, delta);
    }

    @Override
    public void render(Partida p, Color c, Input input, Graphics g) {
        super.render(p, c, input, g);
    }
}
