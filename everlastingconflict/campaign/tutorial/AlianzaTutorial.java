package everlastingconflict.campaign.tutorial;

import everlastingconflict.gestion.Game;
import everlastingconflict.races.Alianza;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.windows.WindowCombat;

public class AlianzaTutorial extends GuidedGame {

    public AlianzaTutorial() {
        super(RaceEnum.ALIANZA, RaceEnum.ETERNIUM);
    }

    @Override
    public final void initSteps() {
        steps.add(new GameStep("La alianza estelar es una raza de humanos que conquistaron las estrellas mucho antes " +
                "de que sus compatriotas terrestres llegaran a la Luna. Sus unidades están mucho más avanzadas " +
                "tecnológicamente que las de los Fénix o los Guardianes pero, al ser nómadas por naturaleza, no traen" +
                " " +
                "a la batalla materiales para construir grandes bases."));
        steps.add(new GameStep("Su forma de reclutar unidades se basa en que su nave espacial viaje a toda velocidad " +
                "a la base de la Alianza más cercana y vuelva al combate desembarcando las tropas necesarias."));
        steps.add(new GameStep("Cuando unos refuerzos de la Alianza estén listos, el reloj llegará a cero con el " +
                "simbolo de despegue activo. Para seleccionar una localización de desembarco haz click en el reloj", false) {
            @Override
            public boolean check(Game p) {
                return WindowCombat.edificio != null;
            }
        });
        steps.add(new GameStep("Bien hecho! Ahora puedes elegir un punto del mapa en el que la nave aterrizará cuando" +
                " consiga los refuerzos, prueba a hacerlo.", false) {
            @Override
            public boolean check(Game p) {
                return WindowCombat.alianceWatch(getMainPlayer().edificios.get(0)).ndivision == 2;
            }
        });
        steps.add(new GameStep("La nave desaparecerá del mapa durante unos instantes y volverá al punto indicado con " +
                "los refuerzos. Por defecto, siempre desembarca granaderos pero puedes indicarle que traiga otras unidades como Luchadoras. Prueba a hacerlo.", false) {
            @Override
            public boolean check(Game p) {
                return p.getMainPlayer().unidades.stream().anyMatch(u -> u.nombre.equals("Luchadora"));
            }
        });
        steps.add(new GameStep("Perfecto! Ya tienes las nociones básicas para llevar a tu ejército de Alianza Estelar a la victoria"));
    }

    @Override
    public void initElements() {
        super.initElements();
        Alianza.initAlianceWatches(this.getMainPlayer());
    }
}
