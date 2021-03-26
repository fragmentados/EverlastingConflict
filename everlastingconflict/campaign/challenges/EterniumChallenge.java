package everlastingconflict.campaign.challenges;

import everlastingconflict.elements.impl.Manipulador;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.watches.RelojEternium;
import everlastingconflict.windows.WindowCombat;

import java.util.ArrayList;

public class EterniumChallenge extends Challenge {

    public EterniumChallenge() {
        super(RaceEnum.ETERNIUM, RaceEnum.MAESTROS);
    }

    @Override
    public void initElements() {
        super.initElements();
        Jugador jugador = getMainPlayer();
        jugador.unidades = new ArrayList<>();
        jugador.edificios = new ArrayList<>();
        jugador.unidades.add(new Unidad(jugador, "Guerrero", 300, 100));
        jugador.unidades.add(new Unidad(jugador, "Guerrero", 350, 100));
        jugador.unidades.add(new Unidad(jugador, "Ancestro", 300, 200));
        jugador.unidades.add(new Unidad(jugador, "Ancestro", 350, 200));
        Manipulador manipulador = new Manipulador(this.players.get(1), 600, 600);
        manipulador.levelUp();
        manipulador.ataque += 5 * 5;
        manipulador.levelUp();
        manipulador.vida += 50 * 5;
        manipulador.vida_max += 50 * 5;
        manipulador.levelUp();
        manipulador.defensa += 5;
        manipulador.levelUp();
        manipulador.vida += 50 * 5;
        manipulador.vida_max += 50 * 5;
        this.players.get(1).unidades.add(manipulador);
        for (Unidad unidad : jugador.unidades) {
            unidad.iniciarbotones(this);
        }
        RelojEternium reloj = new RelojEternium(this.players.get(0), 0.2f);
        reloj.contador_reloj = reloj.fin_segundo_cuarto;
        WindowCombat.createWatch(reloj);
    }
}
