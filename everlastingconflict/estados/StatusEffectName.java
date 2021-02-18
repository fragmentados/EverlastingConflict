package everlastingconflict.estados;

import jdk.net.SocketFlow;

public enum StatusEffectName {
    STUN("Stun"),
    SILENCIO("Silencio"),
    PACIFISMO("Pacifismo"),
    SNARE("Snare"),
    INHABILITACION("Inhabilitación"),
    RALENTIZACION("Ralentización"),
    ATAQUE_POTENCIADO("Ataque potenciado"),
    AMNESIA("Amnesia"),
    REGENERACION("Regeneración"),
    NECESIDAD("Tiempos de necesidad"),
    EROSION("Erosión"),
    MEDITACION("Meditación"),
    SUPERVIVENCIA("Supervivencia"),
    DRENANTES("Impactos drenantes"),
    DEFENSA("Defensa férrea");

    private String name;

    StatusEffectName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean allowsAttack(StatusEffectName status) {
        return !StatusEffectName.STUN.equals(status)
                && !StatusEffectName.PACIFISMO.equals(status)
                && !StatusEffectName.MEDITACION.equals(status)
                && !StatusEffectName.SUPERVIVENCIA.equals(status);
    }

    public static boolean allowsMove(StatusEffectName status) {
        return !StatusEffectName.STUN.equals(status)
                && !StatusEffectName.SNARE.equals(status)
                && !StatusEffectName.MEDITACION.equals(status);
    }
}