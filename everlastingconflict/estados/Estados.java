/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.estados;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Elías
 */
public class Estados {

    public List<Estado> contenido;

    public Estados() {
        contenido = new ArrayList<>();
    }

    public void comportamiento(int delta) {
        for (int i = 0; i < contenido.size(); i++) {
            if (contenido.get(i).comportamiento(delta)) {
                contenido.remove(i);
                i--;
            }
        }
    }

    public Estado obtener_estado(String t) {
        for (Estado e : contenido) {
            if (e.tipo.equals(t)) {
                return e;
            }
        }
        return null;
    }

    public boolean existe_estado(String t) {
        for (Estado e : contenido) {
            if (e.tipo.equals(t)) {
                return true;
            }
        }
        return false;
    }

    public void anadir_estado(Estado e) {
        boolean anadir = true;
        for (Estado es : contenido) {
            if (es.tipo.equals(e.tipo)) {
                if (e.contador >= es.contador) {
                    contenido.remove(es);                                        
                }else{
                    anadir = false;
                }
                break;
            }
        }
        if (anadir) {
            contenido.add(e);
        }
    }

    public void eliminar_estado(String t) {
        for (Estado es : contenido) {
            if (es.tipo.equals(t)) {
                contenido.remove(es);
                break;
            }
        }
    }
}
