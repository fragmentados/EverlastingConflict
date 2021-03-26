/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elements;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;


public abstract class ElementoSimple {

    //Tecnologia Habilidad
    public String nombre, descripcion;   //Identificador
    public Animation animation;
    public Animation aditionalSprite;
    public Image icono;
    public int coste;
    public int guardiansThreatLevelNeeded;
    public int tiempo;
}
