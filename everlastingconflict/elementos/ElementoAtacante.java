/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos;

import everlastingconflict.elementos.implementacion.*;
import everlastingconflict.elementos.util.ElementosComunes;
import everlastingconflict.estados.StatusEffect;
import everlastingconflict.estados.StatusEffectName;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceNameEnum;
import everlastingconflict.relojes.Reloj;
import everlastingconflict.ventanas.VentanaCombate;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static everlastingconflict.RTS.DEBUG_MODE;

/**
 *
 * @author Elías
 */
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
    public String nombre_provocador;
    public float provocado_tiempo;
    public float provocado_tiempo_contador;
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

    public boolean curar_cercanos(Partida p) {
        Jugador aliado = p.getPlayerFromElement(this);
        float distancia = -1;
        for (Unidad u : aliado.unidades) {
            if(u.vida < u.vida_max) {
                if (distancia == -1 || this.obtener_distancia(u) < distancia) {
                    distancia = this.obtener_distancia(u);
                    if (provocado_tiempo == 0 || u.nombre.equals(nombre_provocador)) {
                        if (alcance(this.alcance, u)) {
                            this.objetivo = u;
                            this.ataque(p);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean checkToAttackNearbyElements(Partida p) {
        boolean isAttacking = false;
        List<Jugador> enemyPlayers = p.enemyPlayersFromElement(this);
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
                    if (b.statusBehaviour.equals(StatusBehaviour.ATACANDO)) {
                        if (distancia == -1 || this.obtener_distancia(b) < distancia) {
                            distancia = this.obtener_distancia(b);
                            if (provocado_tiempo == 0 || b.nombre.equals(nombre_provocador)) {
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
        }
        return isAttacking;
    }

    public boolean checkToAttackNearbyElementsFromPlayer(Partida p, Jugador j) {
        float distancia = -1;
        for (Unidad u : j.unidades) {
            if (distancia == -1 || this.obtener_distancia(u) < distancia) {
                distancia = this.obtener_distancia(u);
                if (provocado_tiempo == 0 || u.nombre.equals(nombre_provocador)) {
                    if (alcance(this.alcance, u)) {
                        this.objetivo = u;
                        this.ataque(p);
                        return true;
                    }
                }
            }
        }
        distancia = -1;
        for (Edificio e : j.edificios) {
            if (distancia == -1 || this.obtener_distancia(e) < distancia) {
                distancia = this.obtener_distancia(e);
                if (provocado_tiempo == 0 || e.nombre.equals(nombre_provocador)) {
                    if (alcance(this.alcance, e)) {
                        this.objetivo = e;
                        this.ataque(p);
                        return true;
                    }
                }
            }
        }
        distancia = -1;
        for (Recurso r : j.lista_recursos) {
            if (distancia == -1 || this.obtener_distancia(r) < distancia) {
                distancia = this.obtener_distancia(r);
                if (provocado_tiempo == 0 || r.nombre.equals(nombre_provocador)) {
                    if (alcance(this.alcance, r)) {
                        this.objetivo = r;
                        this.ataque(p);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int ataque_eternium() {
        switch (VentanaCombate.relojEternium().ndivision) {
            case 1:
                return (int) (this.ataque * (75f / 100f));
            case 2:
                return this.ataque;
            case 3:
                return (int) (this.ataque * (150f / 100f));
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

    public void disparar_proyectil(Partida p, int ataque_contador, ElementoVulnerable objetivo) {
        Proyectil proyectil = new Proyectil(this, objetivo, ataque_contador);
        proyectil.shouldHeal = healer;
        p.proyectiles.add(proyectil);
        cadencia_contador = cadencia;
    }

    public void ataque(Partida p) {
        if (canAttack() && !StatusBehaviour.DESTRUIDO.equals(objetivo)) {
            if (cadencia_contador == 0) {
                if (sonido_combate != null) {
                    sonido_combate.playAt(1.0f, 0.1f, x, y, 0f);
                }
                int attack = getAttack(p);
                List<ElementoVulnerable> elementsAffected = getElementsAffected(p);
                for (ElementoVulnerable u : elementsAffected) {
                    if (u.alcance(objetivo.x, objetivo.y, area)) {
                        //dano(p, "Físico", ataque_contador, u);
                        disparar_proyectil(p, attack, u);
                    }
                }
            }
        }
    }

    private List<ElementoVulnerable> getElementsAffected(Partida p) {
        if (healer) {
            return getElementsToHeal(p);
        } else {
            return getElementsToAttack(p);
        }
    }

    private List<ElementoVulnerable> getElementsToAttack(Partida p) {
        List<ElementoVulnerable> elementsToBeAttacked = new ArrayList<>();
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
                List<Jugador> enemies = p.enemyPlayersFromElement(this);
                for (Jugador enemy : enemies) {
                    elementsToBeAttacked.addAll(enemy.unidades);
                    elementsToBeAttacked.addAll(enemy.edificios);
                }
            }
        }
        return elementsToBeAttacked;
    }

    private List<ElementoVulnerable> getElementsToHeal(Partida p) {
        return p.getPlayerFromElement(this).unidades
                .stream().filter(u -> u.vida < u.vida_max).collect(Collectors.toList());
    }

    private int getAttack(Partida p) {
        if (!(this instanceof Bestia) && (p.getPlayerFromElement(this).raza.equals(RaceNameEnum.ETERNIUM.getName()))) {
            return this.ataque_eternium();
        } else {
            return this.ataque;
        }
    }

    public boolean dano(Partida p, String tipo, int ataque_contador, ElementoVulnerable e) {
        //e representa el objetivo del ataque
        Jugador aliado = p.getPlayerFromElement(this);
        Jugador enemigo = p.getPlayerFromElement(e);
        if (hostil) {
            int defensa_contador;
            if (!(e instanceof Bestia) && (enemigo != null && RaceNameEnum.ETERNIUM.getName().equals(enemigo.raza))) {
                defensa_contador = e.defensa_eternium();
            } else {
                defensa_contador = e.defensa;
            }
            if (aliado != null && RaceNameEnum.MAESTROS.getName().equals(aliado.raza)) {
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
            if (this.statusEffectCollection.existe_estado(StatusEffectName.DRENANTES)) {
                Manipulador m = (Manipulador) this;
                if (m.mana >= 10) {
                    ataque_contador += 15;
                    m.mana -= 10;
                }
            }
            if (this.statusEffectCollection.existe_estado(StatusEffectName.ATAQUE_POTENCIADO)) {
                ataque_contador += this.statusEffectCollection.obtener_estado(StatusEffectName.ATAQUE_POTENCIADO).contador;
                this.statusEffectCollection.eliminar_estado(StatusEffectName.ATAQUE_POTENCIADO);
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
                    enemigo.avisar_ataque(p, this);
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
                    unidad.statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.RALENTIZACION, 5f, 35f));
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
                e.destruir(p, this);
                return true;
            }
            if (e instanceof Unidad) {
                Unidad uatacada = (Unidad) e;
                if (uatacada.objetivo == null) {
                    uatacada.atacar(this);
                }
                if (e instanceof Bestia) {
                    for (Bestias b : p.bestias) {
                        if (b.contenido.indexOf(e) != -1) {
                            for (Bestia be : b.contenido) {
                                if (be.objetivo == null) {
                                    be.atacar(this);
                                }
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
            if (this.statusEffectCollection.existe_estado(StatusEffectName.ATAQUE_POTENCIADO)) {
                ataque_contador += this.statusEffectCollection.obtener_estado(StatusEffectName.ATAQUE_POTENCIADO).contador;
                this.statusEffectCollection.eliminar_estado(StatusEffectName.ATAQUE_POTENCIADO);
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

    public boolean canAttack() {
        return (hostil || healer)
                && statusEffectCollection.allowsAttack()
                && StatusBehaviour.allowsAttack(statusBehaviour)
                && ataque > 0;
    }

    @Override
    public void dibujar(Partida p, Color c, Input input, Graphics g) {
        super.dibujar(p, c, input, g);
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
    public void comportamiento(Partida p, Graphics g, int delta) {
        super.comportamiento(p, g, delta);
        if (ataque_tiempo > 0) {
            if (ataque_tiempo_contador - (Reloj.TIME_REGULAR_SPEED * delta) <= 0) {
                ataque_tiempo = ataque_tiempo_contador = 0;
                ataque = ataque_contador;
            } else {
                ataque_tiempo_contador -= (Reloj.TIME_REGULAR_SPEED * delta);
            }
        }
        if (provocado_tiempo > 0) {
            if (provocado_tiempo_contador - (Reloj.TIME_REGULAR_SPEED * delta) <= 0) {
                provocado_tiempo = provocado_tiempo_contador = 0;
                nombre_provocador = null;
            } else {
                provocado_tiempo_contador -= (Reloj.TIME_REGULAR_SPEED * delta);
            }
        }
        if (cadencia_contador > 0) {
            if (cadencia_contador - (Reloj.TIME_REGULAR_SPEED * delta) <= 0) {
                cadencia_contador = 0;
            } else {
                cadencia_contador -= (Reloj.TIME_REGULAR_SPEED * delta);
            }
        }
        switch (statusBehaviour) {
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
    public abstract void destruir(Partida p, ElementoAtacante atacante);

    public boolean construir(Partida p, Edificio edificio, float x, float y) {
        if (p.belongsToMainPlayer(this)) {
            ElementosComunes.CONSTRUCTION_SOUND.playAt(1f, 1f, x, y, 0f);
        }
        this.disableBuildingButtons();
        return true;
    }

}
