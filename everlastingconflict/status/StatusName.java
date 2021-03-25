package everlastingconflict.status;

public enum StatusName {
    STUN("Stun"),
    SILENCIO("Silencio"),
    PACIFISMO("Pacifismo"),
    SNARE("Snare"),
    INHABILITACION("Inhabilitación"),
    RALENTIZACION("Ralentización"),
    ATAQUE_POTENCIADO("Ataque potenciado"),
    ATAQUE_DISMINUIDO("Ataque disminuido"),
    AMNESIA("Amnesia"),
    REGENERACION("Regeneración"),
    NECESIDAD("Tiempos de necesidad"),
    EROSION("Erosión"),
    MEDITACION("Meditación"),
    SUPERVIVENCIA("Supervivencia"),
    DRENANTES("Impactos drenantes"),
    DEFENSA("Defensa férrea"),
    CEGUERA("Ceguera"),
    MODO_SUPREMO("Modo supremo"),
    PROVOCAR("Provocar");

    private String name;

    StatusName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean allowsAttack(StatusName status) {
        return !StatusName.STUN.equals(status)
                && !StatusName.PACIFISMO.equals(status)
                && !StatusName.MEDITACION.equals(status)
                && !StatusName.SUPERVIVENCIA.equals(status);
    }

    public static boolean allowsMove(StatusName status) {
        return !StatusName.STUN.equals(status)
                && !StatusName.SNARE.equals(status)
                && !StatusName.MEDITACION.equals(status);
    }
}
