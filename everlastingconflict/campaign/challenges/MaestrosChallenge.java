package everlastingconflict.campaign.challenges;

import everlastingconflict.elementosvisuales.BotonManipulador;
import everlastingconflict.elements.impl.Habilidad;
import everlastingconflict.elements.impl.Manipulador;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.watches.RelojMaestros;
import everlastingconflict.windows.WindowCombat;

import java.util.ArrayList;

public class MaestrosChallenge extends Challenge {
    public MaestrosChallenge() {
        super(RaceEnum.MAESTROS, RaceEnum.CLARK);
    }

    @Override
    public void initElements() {
        super.initElements();
        Jugador mainPlayer = getMainPlayer();
        Jugador opposingPlayer = this.players.get(1);
        mainPlayer.unidades = new ArrayList<>();
        mainPlayer.edificios = new ArrayList<>();
        RelojMaestros reloj = new RelojMaestros(this.players.get(0));
        reloj.cambio_temporal(RelojMaestros.nombre_noche);
        WindowCombat.createWatch(reloj);
        Manipulador manipulador = new Manipulador(this.players.get(0), 200, 200);
        manipulador.levelUp();
        manipulador.levelUp();
        manipulador.levelUp();
        manipulador.mana = manipulador.mana_max = 400;
        manipulador.vida = manipulador.vida_max = 1200;
        manipulador.botones = new ArrayList<>();
        manipulador.learnSkill(new BotonManipulador(new Habilidad("Eclipse Amanecer")));
        manipulador.learnSkill(new BotonManipulador(new Habilidad("Meteoro"), RelojMaestros.nombre_dia));
        manipulador.learnSkill(new BotonManipulador(new Habilidad("Lluvia de estrellas"), RelojMaestros.nombre_noche));
        manipulador.initButtonKeys();
        mainPlayer.unidades.add(manipulador);
        opposingPlayer.unidades.add(new Unidad(this.players.get(1), "Despedazador", 600, 600));
        opposingPlayer.unidades.add(new Unidad(this.players.get(1), "Despedazador", 650, 600));
        opposingPlayer.unidades.add(new Unidad(this.players.get(1), "Despedazador", 700, 600));
        opposingPlayer.unidades.add(new Unidad(this.players.get(1), "Despedazador", 600, 700));
        opposingPlayer.unidades.add(new Unidad(this.players.get(1), "Despedazador", 650, 700));
        opposingPlayer.unidades.add(new Unidad(this.players.get(1), "Despedazador", 700, 700));
    }
}
