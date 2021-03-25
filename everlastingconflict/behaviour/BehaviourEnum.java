package everlastingconflict.behaviour;

public enum BehaviourEnum {
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

    public static boolean allowsAttack(BehaviourEnum behaviour) {
        return !BehaviourEnum.EMERGIENDO.equals(behaviour);
    }

    public static boolean allowsMove(BehaviourEnum behaviour) {
        return !BehaviourEnum.EMERGIENDO.equals(behaviour);
    }
}
