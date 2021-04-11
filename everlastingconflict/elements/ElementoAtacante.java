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
import everlastingconflict.races.Maestros;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.watches.Reloj;
import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static everlastingconflict.RTS.DEBUG_MODE;

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
    public boolean hostil = true;
    public boolean healer;
    public float delay = 0;

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
        p.proyectiles.add(proyectil);
    }

    public void ataque(Game p) {
        if (canAttack(p) && !BehaviourEnum.DESTRUIDO.equals(objetivo)) {
            if (cadencia_contador == 0) {
                if (sonido_combate != null) {
                    sonido_combate.playAt(1.0f, 0.1f, x, y, 0f);
                }
                int attack = getAttack(p);
                if (this.alcance == Unidad.MELEE_RANGE) {
                    // Melee units won't fire proyectiles
                    dealDamage(p, "Físico", attack, objetivo);
                } else {
                    disparar_proyectil(p, attack, objetivo);
                }
                cadencia_contador = cadencia;
            }
        }
    }

    private Set<ElementoVulnerable> getElementsToAttackBasedOnArea(Game p, ElementoVulnerable elementAttacked) {
        Set<ElementoVulnerable> elementsToBeAttacked = new HashSet<>();
        elementsToBeAttacked.add(elementAttacked);
        if (area > 0) {
            //Dañan a todos los elementos menos al objetivo
            if (elementAttacked instanceof Bestia) {
                for (Bestias be : p.bestias) {
                    if (be.contenido.indexOf(elementAttacked) != -1) {
                        elementsToBeAttacked.addAll(be.contenido.stream().filter(b -> b.alcance(elementAttacked.x,
                                elementAttacked.y, area)).collect(Collectors.toList()));
                        break;
                    }
                }
            } else {
                List<Jugador> enemies = p.getEnemyPlayersFromElement(this);
                for (Jugador enemy : enemies) {
                    elementsToBeAttacked.addAll(enemy.unidades.stream().filter(u -> u.alcance(elementAttacked.x,
                            elementAttacked.y, area)).collect(Collectors.toList()));
                    elementsToBeAttacked.addAll(enemy.edificios.stream().filter(e -> e.alcance(elementAttacked.x,
                            elementAttacked.y, area)).collect(Collectors.toList()));
                }
            }
        }
        return elementsToBeAttacked;
    }

    private Set<ElementoVulnerable> getElementsToHealBasedOnArea(Game p, ElementoVulnerable elementAttacked) {
        Set<ElementoVulnerable> elementsToHeal = p.getPlayerFromElement(this).unidades
                .stream().filter(u -> u.vida < u.vida_max).collect(Collectors.toSet());
        if (area > 0) {
            elementsToHeal = elementsToHeal.stream().filter(e -> e.alcance(elementAttacked.x,
                    elementAttacked.y, area)).collect(Collectors.toSet());
        }
        return elementsToHeal;
    }

    private int getAttack(Game p) {
        Jugador aliado = p.getPlayerFromElement(this);
        if (!(this instanceof Bestia) && (aliado.raza.equals(RaceEnum.ETERNIUM))) {
            return this.getAttackBasedOnEterniumWatch(aliado);
        } else {
            return this.ataque;
        }
    }

    public boolean dealDamage(Game game, String damageType, int attackAmount, ElementoVulnerable elementAttacked) {
        Jugador aliado = game.getPlayerFromElement(this);
        Jugador enemigo = game.getPlayerFromElement(elementAttacked);
        // Check increases of attack
        attackAmount = Maestros.getAttackAmountBasedOnManipulatorEffects(aliado, this, attackAmount);
        attackAmount = this.statusCollection.checkAttackStatusEffects(this, attackAmount);
        Set<ElementoVulnerable> elementsToDamage = getElementsToAttackBasedOnArea(game, elementAttacked);
        for (ElementoVulnerable elementToDamage : elementsToDamage) {
            // Do the actual damage
            float damageToDeal = (attackAmount - elementToDamage.getDefense(enemigo));
            elementToDamage.receiveDamage(damageToDeal);
            if (this instanceof Manipulador) {
                ((Manipulador) this).checkDamageDealtEffects(damageType, damageToDeal, elementToDamage);
            } else if (elementToDamage instanceof Manipulador) {
                ((Manipulador) elementToDamage).checkDamageReceivedEffects();
            }
            if (elementToDamage.vida <= 0) {
                //Destruir e
                elementToDamage.destruir(game, this);
            } else {
                warnAttackedUnitOfAttack(game, elementToDamage);
            }
        }
        // If the attack also heals we check the elements that should be healed
        if (healer) {
            Set<ElementoVulnerable> elementsToHeal = getElementsToHealBasedOnArea(game, elementAttacked);
            for (ElementoVulnerable elementToHeal : elementsToHeal) {
                this.heal(attackAmount, elementToHeal);
            }
        }
        if (enemigo != null) {
            enemigo.avisar_ataque(game, this);
        }
        return elementAttacked.vida <= 0;
    }

    public void warnAttackedUnitOfAttack(Game game, ElementoVulnerable e) {
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
    }

    public boolean heal(int attackAmount, ElementoVulnerable e) {
        //e representa el objetivo de la curacion
        if (healer) {
            attackAmount = this.statusCollection.checkAttackStatusEffects(this, attackAmount);
            e.heal(attackAmount);
        }
        return e.vida == e.vida_max;
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
    public void render(Animation sprite, Game p, Color c, Input input, Graphics g) {
        super.render(sprite, p, c, input, g);
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
    public void render(Game p, Color c, Input input, Graphics g) {
        this.render(this.animation, p, c, input, g);
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
