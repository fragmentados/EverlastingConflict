/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elements;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elements.impl.*;
import everlastingconflict.elements.util.ElementosComunes;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.status.Status;
import everlastingconflict.status.StatusName;
import everlastingconflict.watches.Reloj;
import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static everlastingconflict.RTS.DEBUG_MODE;


class Arma {

    public int ataque;
    public int area;
    public int alcance;
    public float cadenca, cadencia_contador;

    public int ataque_contador;
    public float ataque_tiempo_contador;
    public float ataque_tiempo;
}

public abstract class ElementoAtacante extends ElementoEstado {

    //Cuanto mayor cadencia, mayor el tiempo entre ataques    
    public int ataque;
    public int area;
    public float cadencia, cadencia_contador;
    public int alcance;
    public Animation animacion_ataque;
    public Sound sonido_combate;
    public int ataque_contador;
    public float ataque_tiempo_contador;
    public float ataque_tiempo;

    public ElementoVulnerable objetivo;

    public boolean constructor;
    public boolean hostil;
    public boolean healer;

    //public List<Arma> armas = new ArrayList<>();
    public float obtener_distancia(ElementoCoordenadas e) {
        float distancia_x = Math.abs(this.x - e.x);
        float distancia_y = Math.abs(this.y - e.y);
        return distancia_x + distancia_y;
    }

    public boolean curar_cercanos(Game p) {
        Jugador aliado = p.getPlayerFromElement(this);
        float distancia = -1;
        for (Unidad u : aliado.unidades) {
            if (u.vida < u.vida_max) {
                if (distancia == -1 || this.obtener_distancia(u) < distancia) {
                    distancia = this.obtener_distancia(u);
                    if (alcance(this.alcance, u)) {
                        this.objetivo = u;
                        this.ataque(p);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkToAttackNearbyElements(Game p) {
        boolean isAttacking = false;
        List<Jugador> enemyPlayers = p.getEnemyPlayersFromElement(this);
        float distancia = -1;
        for (Jugador enemyPlayer : enemyPlayers) {
            if (!isAttacking) {
                isAttacking = checkToAttackNearbyElementsFromPlayer(p, enemyPlayer);
            }
        }
        if (!isAttacking) {
            // Atacar a las bestias hostiles cercanas
            for (Bestias be : p.bestias) {
                for (Bestia b : be.contenido) {
                    if (b.behaviour.equals(BehaviourEnum.ATACANDO)) {
                        if (distancia == -1 || this.obtener_distancia(b) < distancia) {
                            distancia = this.obtener_distancia(b);
                            if (alcance(this.alcance, b)) {
                                this.objetivo = b;
                                this.ataque(p);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return isAttacking;
    }

    public boolean checkToAttackNearbyElementsFromPlayer(Game game, Jugador j) {
        float distancia = -1;
        for (Unidad u : j.unidades) {
            if (distancia == -1 || this.obtener_distancia(u) < distancia) {
                distancia = this.obtener_distancia(u);
                if (u.isVisibleByEnemy(game) && alcance(this.alcance, u)) {
                    this.objetivo = u;
                    this.ataque(game);
                    return true;
                }
            }
        }
        distancia = -1;
        for (Edificio e : j.edificios) {
            if (distancia == -1 || this.obtener_distancia(e) < distancia) {
                distancia = this.obtener_distancia(e);
                if (e.isVisibleByEnemy(game) && alcance(this.alcance, e)) {
                    this.objetivo = e;
                    this.ataque(game);
                    return true;
                }
            }
        }
        distancia = -1;
        for (Recurso r : j.lista_recursos) {
            if (distancia == -1 || this.obtener_distancia(r) < distancia) {
                distancia = this.obtener_distancia(r);
                if (r.isVisibleByEnemy(game) && alcance(this.alcance, r)) {
                    this.objetivo = r;
                    this.ataque(game);
                    return true;
                }
            }
        }
        return false;
    }

    public int getAttackBasedOnEterniumWatch(Jugador aliado) {
        boolean allyHasTemporalControl = aliado.hasTecnologyResearched("Control temporal");
        switch (WindowCombat.eterniumWatch().ndivision) {
            case 1:
                return allyHasTemporalControl ? (int) (this.ataque * (90f / 100f)) :
                        (int) (this.ataque * (75f / 100f));
            case 2:
                return allyHasTemporalControl ? (int) (this.ataque * (150f / 100f)) : this.ataque;
            case 3:
                return allyHasTemporalControl ? (int) (this.ataque * (200f / 100f)) :
                        (int) (this.ataque * (150f / 100f));
            //Nunca se debería llegar a este caso
            default:
                return this.ataque;
        }
    }

    public void aumentar_ataque(int n, float t) {
        this.ataque_contador = this.ataque;
        this.ataque += n;
        this.ataque_tiempo = this.ataque_tiempo_contador = t;
    }

    public void disparar_proyectil(Game p, int ataque_contador, ElementoVulnerable objetivo) {
        Proyectil proyectil = new Proyectil(this, objetivo, ataque_contador);
        proyectil.shouldHeal = healer;
        p.proyectiles.add(proyectil);
        cadencia_contador = cadencia;
    }

    public void ataque(Game p) {
        if (canAttack(p) && !BehaviourEnum.DESTRUIDO.equals(objetivo)) {
            if (cadencia_contador == 0) {
                if (sonido_combate != null) {
                    sonido_combate.playAt(1.0f, 0.1f, x, y, 0f);
                }
                int attack = getAttack(p);
                Set<ElementoVulnerable> elementsAffected = getElementsAffected(p);
                for (ElementoVulnerable u : elementsAffected) {
                    if (u.alcance(objetivo.x, objetivo.y, area)) {
                        //dano(p, "Físico", ataque_contador, u);
                        disparar_proyectil(p, attack, u);
                    }
                }
            }
        }
    }

    private Set<ElementoVulnerable> getElementsAffected(Game p) {
        if (healer) {
            return getElementsToHeal(p);
        } else {
            return getElementsToAttack(p);
        }
    }

    private Set<ElementoVulnerable> getElementsToAttack(Game p) {
        Set<ElementoVulnerable> elementsToBeAttacked = new HashSet<>();
        elementsToBeAttacked.add(this.objetivo);
        if (area > 0) {
            //Dañan a todos los elementos menos al objetivo
            if (objetivo instanceof Bestia) {
                for (Bestias be : p.bestias) {
                    if (be.contenido.indexOf(objetivo) != -1) {
                        elementsToBeAttacked.addAll(be.contenido);
                        break;
                    }
                }
            } else {
                List<Jugador> enemies = p.getEnemyPlayersFromElement(this);
                for (Jugador enemy : enemies) {
                    elementsToBeAttacked.addAll(enemy.unidades);
                    elementsToBeAttacked.addAll(enemy.edificios);
                }
            }
        }
        return elementsToBeAttacked;
    }

    private Set<ElementoVulnerable> getElementsToHeal(Game p) {
        return p.getPlayerFromElement(this).unidades
                .stream().filter(u -> u.vida < u.vida_max).collect(Collectors.toSet());
    }

    private int getAttack(Game p) {
        Jugador aliado = p.getPlayerFromElement(this);
        if (!(this instanceof Bestia) && (aliado.raza.equals(RaceEnum.ETERNIUM))) {
            return this.getAttackBasedOnEterniumWatch(aliado);
        } else {
            return this.ataque;
        }
    }

    public boolean dano(Game game, String tipo, int ataque_contador, ElementoVulnerable e) {
        //e representa el objetivo del ataque
        Jugador aliado = game.getPlayerFromElement(this);
        Jugador enemigo = game.getPlayerFromElement(e);
        int defensa_contador;
        if (!(e instanceof Bestia) && (enemigo != null && RaceEnum.ETERNIUM.equals(enemigo.raza))) {
            defensa_contador = e.getDefenseBasedOnEterniumWatch(enemigo);
        } else {
            defensa_contador = e.defensa;
        }
        if (aliado != null && RaceEnum.MAESTROS.equals(aliado.raza)) {
            if (Manipulador.alentar) {
                Manipulador m = null;
                for (Unidad u : aliado.unidades) {
                    if (u.nombre.equals("Manipulador")) {
                        m = (Manipulador) u;
                    }
                }
                if (m != null) {
                    if (this.alcance(200, m)) {
                        ataque_contador += 5;
                    }
                }
            }
        }
        if (this.statusCollection.existe_estado(StatusName.DRENANTES)) {
            Manipulador m = (Manipulador) this;
            if (m.mana >= 10) {
                ataque_contador += 15;
                m.mana -= 10;
            }
        }
        if (this.statusCollection.existe_estado(StatusName.ATAQUE_POTENCIADO)) {
            ataque_contador += this.statusCollection.obtener_estado(StatusName.ATAQUE_POTENCIADO).value;
            this.statusCollection.forceRemoveStatus(StatusName.ATAQUE_POTENCIADO);
        }
        if (this.statusCollection.existe_estado(StatusName.ATAQUE_DISMINUIDO)) {
            ataque_contador -= this.statusCollection.obtener_estado(StatusName.ATAQUE_DISMINUIDO).value;
            if (ataque_contador < 0) {
                ataque_contador = 0;
            }
            this.statusCollection.forceRemoveStatus(StatusName.ATAQUE_DISMINUIDO);
        }
        float cantidad_dano = (ataque_contador - defensa_contador);
        if (!(e instanceof Unidad) || ((Unidad) e).puede_ser_danado()) {
            if (e instanceof Unidad) {
                if (((Unidad) e).escudo > 0) {
                    float contador = cantidad_dano - ((Unidad) e).escudo;
                    ((Unidad) e).escudo -= cantidad_dano;
                    if (((Unidad) e).escudo < 0) {
                        ((Unidad) e).escudo = 0;
                    }
                    cantidad_dano = contador;
                }
            }
            if (cantidad_dano > 0) {
                e.vida -= cantidad_dano;
            }
            if (enemigo != null) {
                enemigo.avisar_ataque(game, this);
            }
        }
        if (this instanceof Manipulador) {
            Manipulador m = (Manipulador) this;
            //Robo de vida
            if (m.robo_vida && tipo.equals("Físico")) {
                m.aumentar_vida(cantidad_dano * 0.35f);
            }
            //Succion de hechizo
            if (m.succion_hechizo && tipo.equals("Mágico")) {
                m.aumentar_vida(cantidad_dano * 0.25f);
            }
            if (m.disparo_helado && e instanceof Unidad) {
                Unidad unidad = (Unidad) e;
                unidad.statusCollection.anadir_estado(new Status(StatusName.RALENTIZACION, 5f,
                        35f));
            }
        }
        if (e instanceof Manipulador) {
            Manipulador m = (Manipulador) e;
            if (m.vida <= (m.vida_max * 0.35)) {
                if (!m.ultimo_recurso_activado) {
                    m.ultimo_recurso_cantidad = m.defensa;
                    m.defensa *= 2;
                    m.ultimo_recurso_activado = true;
                }
            }
        }
        //cadencia_contador = cadencia;
        if (e.vida <= 0) {
            //Destruir e
            e.destruir(game, this);
            return true;
        }
        if (e instanceof Unidad) {
            Unidad uatacada = (Unidad) e;
            if (uatacada.objetivo == null && this.isVisibleByEnemy(game)) {
                uatacada.atacar(game, this);
            }
            if (e instanceof Bestia) {
                for (Bestias b : game.bestias) {
                    if (b.contenido.indexOf(e) != -1) {
                        for (Bestia be : b.contenido) {
                            if (be.objetivo == null) {
                                be.atacar(game, this);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean heal(int ataque_contador, ElementoVulnerable e) {
        //e representa el objetivo de la curacion
        if (healer) {
            if (this.statusCollection.existe_estado(StatusName.ATAQUE_POTENCIADO)) {
                ataque_contador += this.statusCollection.obtener_estado(StatusName.ATAQUE_POTENCIADO).value;
                this.statusCollection.forceRemoveStatus(StatusName.ATAQUE_POTENCIADO);
            }
            if (this.statusCollection.existe_estado(StatusName.ATAQUE_DISMINUIDO)) {
                ataque_contador -= this.statusCollection.obtener_estado(StatusName.ATAQUE_DISMINUIDO).value;
                if (ataque_contador < 0) {
                    ataque_contador = 0;
                }
                this.statusCollection.forceRemoveStatus(StatusName.ATAQUE_DISMINUIDO);
            }
            if (ataque_contador > 0) {
                if (e.vida + ataque_contador >= e.vida_max) {
                    e.vida = e.vida_max;
                    return true;
                } else {
                    e.vida += ataque_contador;
                }
            }
        }
        return false;
    }

    public boolean canAttack(Game game) {
        Jugador ally = game.getPlayerFromElement(this);
        return (hostil || healer)
                && statusCollection.allowsAttack()
                && BehaviourEnum.allowsAttack(behaviour)
                && ataque > 0 && (ally == null || !RaceEnum.ETERNIUM.equals(ally.raza)
                || WindowCombat.eterniumWatch().ndivision != 4);
    }

    @Override
    public void render(Game p, Color c, Input input, Graphics g) {
        super.render(p, c, input, g);
        g.setColor(Color.black);
        if (DEBUG_MODE && hostil) {
            if (alcance > 0) {
                int alcancex = 2 * (alcance + anchura), alcancey = 2 * (alcance + altura);
                g.drawOval(x - alcancex / 2, y - alcancey / 2, alcancex, alcancey);
            }
        }
        g.setColor(Color.white);
    }

    @Override
    public void comportamiento(Game p, Graphics g, int delta) {
        super.comportamiento(p, g, delta);
        if (ataque_tiempo > 0) {
            if (ataque_tiempo_contador - (Reloj.TIME_REGULAR_SPEED * delta) <= 0) {
                ataque_tiempo = ataque_tiempo_contador = 0;
                ataque = ataque_contador;
            } else {
                ataque_tiempo_contador -= (Reloj.TIME_REGULAR_SPEED * delta);
            }
        }
        if (cadencia_contador > 0) {
            if (cadencia_contador - (Reloj.TIME_REGULAR_SPEED * delta) <= 0) {
                cadencia_contador = 0;
            } else {
                cadencia_contador -= (Reloj.TIME_REGULAR_SPEED * delta);
            }
        }
        switch (behaviour) {
            case PARADO:
                if (hostil) {
                    checkToAttackNearbyElements(p);
                } else if (healer) {
                    curar_cercanos(p);
                }
                break;
        }
    }

    @Override
    public abstract void destruir(Game p, ElementoAtacante atacante);

    public boolean construir(Game p, Edificio edificio, float x, float y) {
        if (p.belongsToMainPlayer(this)) {
            ElementosComunes.CONSTRUCTION_SOUND.playAt(1f, 1f, x, y, 0f);
        }
        this.disableBuildingButtons();
        return true;
    }

}
