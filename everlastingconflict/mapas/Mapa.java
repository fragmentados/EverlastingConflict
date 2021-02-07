/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.mapas;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public abstract class Mapa {

    public abstract void init(GameContainer container) throws SlickException;

    public abstract void update(GameContainer container, int delta) throws SlickException;

    public abstract void render(GameContainer container, Graphics g) throws SlickException;

}
