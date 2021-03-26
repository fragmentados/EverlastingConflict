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

    public List<Status> contenido = new ArrayList<>();

    public void comportamiento(Game game, ElementoEstado elementAffectedByStatus, int delta) {
        for (int i = 0; i < contenido.size(); i++) {
            if (contenido.get(i).comportamiento(delta)) {
                removeStatusByTime(game, elementAffectedByStatus, contenido.get(i).name);
                contenido.remove(i);
                i--;
            }
        }
    }

    public Status getStatusByBasicInfo(StatusNameEnum status) {
        return contenido.stream()
                .filter(s -> status.equals(s.name))
                .findFirst().orElse(null);
    }

    public boolean containsStatus(StatusNameEnum status) {
        return contenido.stream().anyMatch(s -> status.equals(s.name));
    }

    public void addStatus(Status e) {
        boolean anadir = true;
        for (Status es : contenido) {
            if (es.name.equals(e.name)) {
                if (e.value >= es.value) {
                    contenido.remove(es);
                } else {
                    anadir = false;
                }
                break;
            }
        }
        if (anadir) {
            contenido.add(e);
        }
    }

    public void forceRemoveStatus(StatusNameEnum status) {
        contenido.removeIf(s -> status.equals(s.name));
    }

    public void removeStatusByTime(Game game, ElementoEstado elementAffectedByStatus,
                                   StatusNameEnum StatusNameEnum) {
        this.forceRemoveStatus(StatusNameEnum);
        if (StatusNameEnum.MODO_SUPREMO.equals(StatusNameEnum)) {
            elementAffectedByStatus.destruir(game, null);
        } else if (StatusNameEnum.PROVOCAR.equals(StatusNameEnum)) {
            elementAffectedByStatus.isProyectileAttraction = false;
        }
    }

    public boolean allowsAttack() {
        return contenido.stream().allMatch(s -> StatusNameEnum.allowsAttack(s.name));
    }

    public boolean allowsMove() {
        return contenido.stream().allMatch(s -> StatusNameEnum.allowsMove(s.name));
    }
}
