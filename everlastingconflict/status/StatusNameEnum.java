package everlastingconflict.status;

public enum StatusNameEnum {
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

    StatusNameEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean allowsAttack(StatusNameEnum status) {
        return !StatusNameEnum.STUN.equals(status)
                && !StatusNameEnum.PACIFISMO.equals(status)
                && !StatusNameEnum.MEDITACION.equals(status)
                && !StatusNameEnum.SUPERVIVENCIA.equals(status);
    }

    public static boolean allowsMove(StatusNameEnum status) {
        return !StatusNameEnum.STUN.equals(status)
                && !StatusNameEnum.SNARE.equals(status)
                && !StatusNameEnum.MEDITACION.equals(status);
    }
}
