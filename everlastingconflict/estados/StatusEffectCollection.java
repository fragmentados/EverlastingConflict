/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.estados;

import everlastingconflict.elementos.ElementoEstado;
import everlastingconflict.gestion.Partida;

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

    public void comportamiento(Partida partida, ElementoEstado elementAffectedByStatus, int delta) {
        for (int i = 0; i < contenido.size(); i++) {
            if (contenido.get(i).comportamiento(delta)) {
                removeStatusByTime(partida, elementAffectedByStatus, contenido.get(i).name);
                contenido.remove(i);
                i--;
            }
        }
    }

    public StatusEffect obtener_estado(StatusEffectName status) {
        return contenido.stream()
                .filter(s -> status.equals(s.name))
                .findFirst().orElse(null);
    }

    public boolean existe_estado(StatusEffectName status) {
        return contenido.stream().anyMatch(s -> status.equals(s.name));
    }

    public void anadir_estado(StatusEffect e) {
        boolean anadir = true;
        for (StatusEffect es : contenido) {
            if (es.name.equals(e.name)) {
                if (e.value >= es.value) {
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

    public void forceRemoveStatus(StatusEffectName status) {
        contenido.removeIf(s -> status.equals(s.name));
    }

    public void removeStatusByTime(Partida partida, ElementoEstado elementAffectedByStatus, StatusEffectName statusName) {
        this.forceRemoveStatus(statusName);
        if (StatusEffectName.MODO_SUPREMO.equals(statusName)) {
            elementAffectedByStatus.destruir(partida, null);
        } else if (StatusEffectName.PROVOCAR.equals(statusName)) {
            elementAffectedByStatus.isProyectileAttraction = false;
        }
    }

    public boolean allowsAttack() {
        return contenido.stream().allMatch(s -> StatusEffectName.allowsAttack(s.name));
    }

    public boolean allowsMove() {
        return contenido.stream().allMatch(s -> StatusEffectName.allowsMove(s.name));
    }
}
