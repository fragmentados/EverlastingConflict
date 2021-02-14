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
public class StatusEffectCollection {

    public List<StatusEffect> contenido;

    public StatusEffectCollection() {
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

    public StatusEffect obtener_estado(StatusEffectName status) {
        return contenido.stream()
                .filter(s -> status.equals(s.tipo))
                .findFirst().orElse(null);
    }

    public boolean existe_estado(StatusEffectName status) {
        return contenido.stream().anyMatch(s -> status.equals(s.tipo));
    }

    public void anadir_estado(StatusEffect e) {
        boolean anadir = true;
        for (StatusEffect es : contenido) {
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

    public void eliminar_estado(StatusEffectName status) {
        contenido.removeIf(s -> status.equals(s.tipo));
    }

    public boolean allowsAttack() {
        return contenido.stream().allMatch(s -> StatusEffectName.allowsAttack(s.tipo));
    }

    public boolean allowsMove() {
        return contenido.stream().allMatch(s -> StatusEffectName.allowsMove(s.tipo));
    }
}
