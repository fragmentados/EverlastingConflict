/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos.implementacion;

import everlastingconflict.elementos.ElementoAtacante;
import everlastingconflict.elementos.ElementoComplejo;
import everlastingconflict.elementos.ElementoMovil;
import everlastingconflict.elementos.ElementoVulnerable;
import everlastingconflict.elementos.util.ElementosComunes;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.estados.StatusEffectCollection;
import everlastingconflict.estados.StatusEffectName;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Evento;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceEnum;
import everlastingconflict.razas.Raza;
import everlastingconflict.relojes.Reloj;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elías
 */
public class Unidad extends ElementoMovil {
    //Recoleccion
    public Recurso recurso;
    public int recurso_int;
    public int coste_poblacion;
    //Construccion
    public Edificio edificio_construccion;
    public float ed_x, ed_y;
    //Atributos Guardianes
    public boolean piloto, vehiculo;
    public static boolean activacion_resurreccion = false;
    //Habilidades
    public Habilidad habilidad_seleccionada;
    //Valores estándares
    public static float vida_estandar = 100;
    public static int defensa_estandar = 0;
    public static int ataque_estandar = 10;
    public static float cadencia_estandar = 1.5f;
    public static int alcance_estandar = 100;
    public static float velocidad_estandar = 1.5f;
    public static int vision_estandar = 700;
    public static int tiempo_estandar = 10;
    public static int area_estandar = 100;

    public Unidad(Bestia b) {
        this.nombre = b.nombre;
        this.descripcion = b.descripcion;
        this.botones = b.botones;
        this.statusEffectCollection = b.statusEffectCollection;
        this.hostil = this.movil = true;
        this.ataque = b.ataque;
        this.alcance = b.alcance;
        this.cadencia = b.cadencia;
        this.velocidad = b.velocidad;
        this.vida = b.vida;
        this.vida_max = b.vida_max;
        this.sprite = b.sprite;
        this.miniatura = b.miniatura;
        this.icono = b.icono;
        anchura_barra_vida = anchura = sprite.getWidth();
        altura = sprite.getHeight();
        this.x = b.x;
        this.y = b.y;
        this.sonido_combate = b.sonido_combate;
        this.vision = b.vision;
    }

    public Unidad(Jugador aliado, Unidad u) {
        this(aliado, u.nombre);
    }

    public Unidad(Jugador aliado, String n) {
        nombre = n;
        descripcion = "";
        statusEffectCollection = new StatusEffectCollection();
        botones = new ArrayList<>();
        //habilidades = new ArrayList<>();
        hostil = true;
        Raza.unidad(aliado, this);
        vida = vida_max;
        coste_poblacion = 1;
        cadencia_contador = 0;
        statusBehaviour = StatusBehaviour.PARADO;
        x = 0;
        y = 0;
        recurso_int = 0;
        if (!n.equals("No hay")) {
            this.iniciar_imagenes();
            if (ataque > 0) {
                try {
                    sonido_combate = new Sound("media/Sonidos/" + nombre + "Ataque.ogg");
                } catch (Exception e) {
                }
            }
            anchura = sprite.getWidth();
            altura = sprite.getHeight();
        }
        anchura_barra_vida = anchura;
        experiencia_al_morir = 15;
    }

    public Unidad(Jugador aliado, String n, float x, float y) {
        this(aliado, n);
        this.x = x;
        this.y = y;
    }

    public final void iniciar_imagenes() {
        try {
            sprite = new Animation(new Image[]{new Image("media/Unidades/" + nombre + ".png")}, new int[]{300}, false);
            icono = new Image("media/Iconos/" + nombre + ".png");
            miniatura = new Image("media/Miniaturas/Prueba.png");
            //miniatura = new Image("media/Miniaturas/" + nombre + ".png");
        } catch (SlickException e) {

        }
    }

    public void aumentar_vida(float c) {
        if (this.vida + c >= vida_max) {
            vida = vida_max;
        } else {
            vida += c;
        }
    }

    public void disminuir_vida(Partida p, float c) {
        if (this.vida - c <= 0) {
            this.destruir(p, null);
        } else {
            vida -= c;
        }
    }

    @Override
    public void comportamiento(Partida p, Graphics g, int delta) {
        super.comportamiento(p, g, delta);
        Jugador aliado = p.getPlayerFromElement(this);
        List<Jugador> enemies = p.getEnemyPlayersFromElement(this);
        if (statusEffectCollection.existe_estado(StatusEffectName.REGENERACION)) {
            if (this.vida < this.vida_max) {
                aumentar_vida(Reloj.TIME_REGULAR_SPEED * delta);
            }
        }
        if (statusEffectCollection.existe_estado(StatusEffectName.EROSION)) {
            disminuir_vida(p, Reloj.TIME_REGULAR_SPEED * delta * vida_max / 5);
        }
        for (Jugador enemy : enemies) {
            for (ElementoEspecial e : enemy.elementos_especiales) {
                if (e.nombre.equals("Agujero negro")) {
                    if (this.alcance(e.x, e.y, e.anchura / 2)) {
                        this.destruir(p, null);
                    }
                }
            }
        }
        switch (statusBehaviour) {
            case EMBARCANDO:
                if (this.hitbox(objetivo.x, objetivo.y)) {
                    if (objetivo instanceof Edificio) {
                        if (((Edificio) objetivo).activo) {
                            //Unidad soluciona evento negativo
                            this.solucionar(p, aliado);
                        } else {
                            ((Edificio) objetivo).enable(this, p);
                        }
                    } else {
                        //Piloto embarca vehículo
                        this.pilotar(p, (Unidad) objetivo);
                    }
                } else {
                    anadir_movimiento(objetivo.x, objetivo.y);
                }
                break;
            case CONSTRUYENDO:
                // Ha llegado a las coordenadas
                if (this.movimiento == null) {
                    if (edificio_construccion.vida == 0) {
                        if (p.belongsToMainPlayer(this)) {
                            ElementosComunes.CONSTRUCTION_SOUND.playAt(1f, 1f, x, y, 0f);
                        }
                        this.movil = false;
                        if (edificio_construccion.nombre.equals("Refinería")) {
                            for (Recurso r : p.recursos) {
                                if (r.x == edificio_construccion.x && edificio_construccion.y == r.y) {
                                    r.capturador = aliado.nombre;
                                    break;
                                }
                            }
                        }
                        aliado.edificios.add(edificio_construccion);
                    }
                    if (edificio_construccion.vida < edificio_construccion.vida_max) {
                        if (edificio_construccion.vida + (edificio_construccion.vida_max / edificio_construccion.tiempo) * Reloj.TIME_REGULAR_SPEED * delta >= edificio_construccion.vida_max) {
                            //Acaba la construccion
                            edificio_construccion.vida = edificio_construccion.vida_max;
                            edificio_construccion.iniciarbotones(p);
                            edificio_construccion.statusBehaviour = StatusBehaviour.PARADO;
                            statusBehaviour = StatusBehaviour.PARADO;
                            this.movil = true;
                            edificio_construccion = null;
                            this.enableBuildingButtons();
                        } else {
                            edificio_construccion.vida += (edificio_construccion.vida_max / edificio_construccion.tiempo) * Reloj.TIME_REGULAR_SPEED * delta;
                        }
                    }
                }
                break;
            case ATACAR_MOVER:
                boolean atacar = checkToAttackNearbyElements(p);
                if (!atacar) {
                    if (canMove(p) && (movimiento == null || (movimiento.puntos.get(0).x != x_atmov || movimiento.puntos.get(0).y != y_atmov))) {
                        anadir_movimiento(x_atmov, y_atmov);
                    }
                } else {
                    movimiento = null;
                }
                break;
            case RECOLECTANDO:
                if (this.movimiento == null) {
                    if (recurso instanceof TorreVision) {
                        recurso.capturar(p, aliado, this);
                    } else {
                        recurso.recolect(p, aliado, this, delta);
                    }
                }
                break;
            case PARADO:
                for (Jugador enemy : enemies) {
                    for (ElementoEspecial e : enemy.elementos_especiales) {
                        if (e.nombre.equals("Agujero negro")) {
                            if (this.alcance(300, e)) {
                                this.mover(p, e.x, e.y);
                            }
                        }
                    }
                }
                break;
            case ATACANDO:
                if (alcance(this.alcance, objetivo)) {
                    movimiento = null;
                    ataque(p);
                } else {
                    anadir_movimiento(objetivo.x, objetivo.y);
                }
                break;
            case HABILIDAD:
                if (alcance(habilidad_seleccionada.alcance, objetivo)) {
                    movimiento = null;
                    habilidad_seleccionada.resolucion(p, this, objetivo);
                    parar();
                } else {
                    anadir_movimiento(objetivo.x, objetivo.y);
                }
                break;
        }
        List<BotonComplejo> botones_contador;
        if (!(this instanceof Manipulador) || ((Manipulador) this).enhancementButtons.isEmpty()) {
            botones_contador = this.botones;
        } else {
            botones_contador = ((Manipulador) this).enhancementButtons;
        }
        for (BotonComplejo b : botones_contador) {
            b.comportamiento(delta);
        }
    }

    @Override
    public void destruir(Partida p, ElementoAtacante atacante) {
        this.destruir(p, atacante, true);
    }

    public void destruir(Partida p, ElementoAtacante atacante, boolean shouldPlaySound) {
        //La unidad no ha sido destruida previamente
        if (!StatusBehaviour.DESTRUIDO.equals(this.statusBehaviour)
                && p.getPlayerFromElement(this).unidades.indexOf(this) != -1) {
            this.statusBehaviour = StatusBehaviour.DESTRUIDO;
            if (this.seleccionada()) {
                this.deseleccionar();
            }
            p.getPlayerFromElement(this).poblacion -= this.coste_poblacion;
            p.getPlayerFromElement(this).unidades.remove(this);
            Manipulador.checkToGainExperience(p, atacante, experiencia_al_morir, x, y, altura);
            if (shouldPlaySound) {
                ElementosComunes.UNIT_DEATH_SOUND.playAt(1f, 1f, x, y, 0f);
            }
        }
    }

    /*Inicio de Métodos Cambiantes de Estado */
    public void habilidad(Habilidad habilidad, ElementoComplejo elemento) {
        statusBehaviour = StatusBehaviour.HABILIDAD;
        objetivo = elemento;
        habilidad_seleccionada = habilidad;
    }

    @Override
    public void mover(Partida p, float x, float y) {
        super.mover(p, x, y);
        if (canMove(p)) {
            if (edificio_construccion != null) {
                //Cancelar Edificio
                if (!p.getPlayerFromElement(this).raza.equals(RaceEnum.FENIX)) {
                    p.getPlayerFromElement(this).addResources(edificio_construccion.coste);
                }
                edificio_construccion = null;
            }
            if (recurso != null) {
                if (recurso.ocupado) {
                    recurso.ocupado = false;
                }
                recurso = null;
            }
        }
    }

    public void recolectar(Partida p, Recurso r) {
        statusBehaviour = StatusBehaviour.RECOLECTANDO;
        recurso = r;
        anadir_recoleccion(p, getCoordinateForBuildingCreation(this.x, recurso.x, recurso.anchura, this.anchura), getCoordinateForBuildingCreation(this.y, recurso.y, recurso.altura, this.altura));
    }

    public void atacar(Partida p, ElementoVulnerable atacada) {
        if (canAttack(p)) {
            statusBehaviour = StatusBehaviour.ATACANDO;
            objetivo = atacada;
        }
    }

    public void embarcar(ElementoVulnerable e) {
        //Método empleado para que los pilotos embarquen los vehículos
        //o para que las unidades embarquen el Ayuntamiento para solu-
        //cionen eventos
        objetivo = e;
        statusBehaviour = StatusBehaviour.EMBARCANDO;
    }

    /*Fin de Métodos Cambiantes de Estado */
    @Override
    public void render(Partida p, Color c, Input input, Graphics g) {
        super.render(p, c, input, g);
    }

    public boolean puede_usar_habilidad() {
        if (statusEffectCollection.existe_estado(StatusEffectName.STUN)) {
            return false;
        }
        if (statusEffectCollection.existe_estado(StatusEffectName.SILENCIO)) {
            return false;
        }
        if (statusEffectCollection.existe_estado(StatusEffectName.NECESIDAD)) {
            return false;
        }
        if (statusBehaviour.equals(StatusBehaviour.EMERGIENDO)) {
            return false;
        }
        return true;
    }

    public boolean puede_ser_danado() {
        if (statusEffectCollection.existe_estado(StatusEffectName.NECESIDAD)) {
            return false;
        }
        if (statusEffectCollection.existe_estado(StatusEffectName.SUPERVIVENCIA)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean construir(Partida p, Edificio edificio, float x, float y) {
        Jugador aliado = p.getPlayerFromElement(this);
        if (aliado.comprobacion_recursos(edificio)) {
            super.construir(p, edificio, x, y);
            statusBehaviour = StatusBehaviour.CONSTRUYENDO;
            p.getPlayerFromElement(this).removeResources(edificio.coste);
            edificio_construccion = edificio;
            edificio.statusBehaviour = StatusBehaviour.CONSTRUYENDOSE;
            edificio_construccion.cambiar_coordenadas(x, y);
            anadir_movimiento(getCoordinateForBuildingCreation(this.x, x, edificio.anchura, this.anchura),
                    getCoordinateForBuildingCreation(this.y, y, edificio.altura, this.altura));
            return true;
        }
        return false;
    }

    protected float getCoordinateForBuildingCreation(float initialValue, float maxValue, float buildingOffset, float unitOffset) {
        float result;
        if (maxValue > initialValue + buildingOffset / 2) {
            result = maxValue - buildingOffset / 2 - unitOffset / 2;
        } else if (maxValue < initialValue - buildingOffset / 2) {
            result = maxValue + buildingOffset / 2 + unitOffset / 2;
        } else {
            result = initialValue;
        }
        return result;
    }

    public void efecto_piloto(Partida p, Unidad v) {
        switch (this.nombre) {
            case "Artillero":
                v.ataque *= 1.25f;
                break;
            case "Corredor":
                v.velocidad += 0.2f;
                break;
            case "Amparador":
                v.defensa *= 1.25f;
                break;
            case "Ingeniero":
                v.vida *= 1.5f;
                v.vida_max *= 1.5f;
                break;
            case "Armero":
                v.cadencia -= 0.2f;
                break;
            case "Oteador":
                v.alcance *= 1.25f;
                break;
            case "Explorador":
                v.vision += 100;
                break;
        }
    }

    public void solucionar(Partida p, Jugador aliado) {
        for (Evento e : aliado.eventos.contenido) {
            if (!e.positivo) {
                if (e.nombre_elemento.equals(this.nombre)) {
                    e.cantidad_elemento--;
                    if (e.cantidad_elemento <= 0) {
                        aliado.eventos.eliminar_evento(e);
                    }
                    break;
                }
            }
        }
        this.destruir(p, null);
    }

    public void pilotar(Partida p, Unidad v) {
        efecto_piloto(p, v);
        //Se permite al vehículo moverse y atacar
        v.movil = true;
        v.hostil = true;
        //Eliminación del vehículo de la lista de las unidades creadas del Taller
        for (Edificio e : p.getPlayerFromElement(this).edificios) {
            if (e instanceof Taller) {
                Taller t = (Taller) e;
                if (t.unidades_creadas.indexOf(v) != -1) {
                    t.unidades_creadas.remove(v);
                    v.mover(p, t.reunion_x, t.reunion_y);
                    break;
                }
            }
        }
        //Se destruye el Piloto
        this.destruir(p, null, false);
    }

}
