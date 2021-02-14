package everlastingconflict.estadoscomportamiento;

import everlastingconflict.estados.StatusEffectName;

public enum StatusBehaviour {
    PARADO,
    EMERGIENDO,
    MOVER,
    ATACAR_MOVER,
    CONSTRUYENDO,
    CONSTRUYENDOSE,
    EMBARCANDO,
    HABILIDAD,
    RECOLECTANDO,
    ATACANDO,
    DETENIDO,
    PROYECTIL,
    DESTRUIDO;

    public static boolean allowsAttack(StatusBehaviour status) {
        return !StatusBehaviour.EMERGIENDO.equals(status);
    }

    public static boolean allowsMove(StatusBehaviour status) {
        return !StatusBehaviour.EMERGIENDO.equals(status);
    }
}
