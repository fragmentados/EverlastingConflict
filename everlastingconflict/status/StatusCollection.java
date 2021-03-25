/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.status;

import everlastingconflict.elements.ElementoEstado;
import everlastingconflict.gestion.Game;

import java.util.ArrayList;
import java.util.List;


public class StatusCollection {

    public List<Status> contenido;

    public StatusCollection() {
        contenido = new ArrayList<>();
    }

    public void comportamiento(Game game, ElementoEstado elementAffectedByStatus, int delta) {
        for (int i = 0; i < contenido.size(); i++) {
            if (contenido.get(i).comportamiento(delta)) {
                removeStatusByTime(game, elementAffectedByStatus, contenido.get(i).name);
                contenido.remove(i);
                i--;
            }
        }
    }

    public Status obtener_estado(StatusName status) {
        return contenido.stream()
                .filter(s -> status.equals(s.name))
                .findFirst().orElse(null);
    }

    public boolean existe_estado(StatusName status) {
        return contenido.stream().anyMatch(s -> status.equals(s.name));
    }

    public void anadir_estado(Status e) {
        boolean anadir = true;
        for (Status es : contenido) {
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

    public void forceRemoveStatus(StatusName status) {
        contenido.removeIf(s -> status.equals(s.name));
    }

    public void removeStatusByTime(Game game, ElementoEstado elementAffectedByStatus, StatusName statusName) {
        this.forceRemoveStatus(statusName);
        if (StatusName.MODO_SUPREMO.equals(statusName)) {
            elementAffectedByStatus.destruir(game, null);
        } else if (StatusName.PROVOCAR.equals(statusName)) {
            elementAffectedByStatus.isProyectileAttraction = false;
        }
    }

    public boolean allowsAttack() {
        return contenido.stream().allMatch(s -> StatusName.allowsAttack(s.name));
    }

    public boolean allowsMove() {
        return contenido.stream().allMatch(s -> StatusName.allowsMove(s.name));
    }
}
