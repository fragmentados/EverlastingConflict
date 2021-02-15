/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict;

import everlastingconflict.elementos.ElementoComplejo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Elías
 */
public class ControlGroup {

    public List<ElementoComplejo> contenido;
    public int tecla;    



    public ControlGroup(List<ElementoComplejo> c, int t) {
        contenido = new ArrayList<>();
        for(ElementoComplejo e: c){
            contenido.add(e);
        }        
        tecla = t;
    }
        
}
