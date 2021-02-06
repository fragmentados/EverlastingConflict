/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.ai;

import RTS.gestion.Partida;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Elías
 */
public class AIClark extends AI {

    public AIClark() {
        super("AIClark","Clark");        
    }

    @Override
    public void comportamiento_unidades(Partida p,Graphics g, int delta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void comportamiento_edificios(Partida p, Graphics g, int delta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
