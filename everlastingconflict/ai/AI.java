/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.ai;

import everlastingconflict.elementos.ElementoAtacante;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceNameEnum;
import org.newdawn.slick.Graphics;

import java.util.List;

/**
 *
 * @author Elías
 */
public class AI extends Jugador {

    public int npushear;
    
    public AI(String n, String r) {
        super(n, r);
    }

    public static AI crearAI(String r) {
        if (RaceNameEnum.CLARK.getName().equals(r)) {
            return new AIClark();
        } else if (RaceNameEnum.ETERNIUM.getName().equals(r)) {
            return new AIEternium();
        } else if (RaceNameEnum.FENIX.getName().equals(r)) {
                return new AIFenix();
        }
        return null;
    }

    public void pushear(Partida p) {
        List<Unidad> militares = cantidad_militar();
        if (militares.size() >= npushear) {
            for (Unidad u : militares) {
                if (u.statusBehaviour.equals(StatusBehaviour.PARADO)) {
                    Jugador enemigo = p.jugador_enemigo(this);
                    u.atacarmover(p, enemigo.x_inicial, enemigo.y_inicial);
                }
            }
            if (npushear * 2 <= 50) {
                npushear *= 2;
            }
        }
    }
    
    @Override
    public void iniciar_elementos(Partida p) {
        super.iniciar_elementos(p);
    }

    @Override
    public void comportamiento_unidades(Partida p, Graphics g, int delta) {
        super.comportamiento_unidades(p, g, delta);
    }

    @Override
    public void comportamiento_edificios(Partida p, Graphics g, int delta) {
        super.comportamiento_edificios(p, g, delta);
    }

    @Override
    public void avisar_ataque(Partida p, ElementoAtacante atacante) {
        List<Unidad> militares = cantidad_militar();        
        for (Unidad u : militares) {
            if (u.statusBehaviour.equals(StatusBehaviour.PARADO)) {
                u.atacarmover(p, atacante.x, atacante.y);
            }
        }
    }
}
