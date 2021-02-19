/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos.implementacion;

import everlastingconflict.elementos.*;
import everlastingconflict.elementos.util.ElementosComunes;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.estados.StatusEffectCollection;
import everlastingconflict.estados.StatusEffectName;
import everlastingconflict.estadoscomportamiento.StatusBehaviour;
import everlastingconflict.gestion.Evento;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.RaceNameEnum;
import everlastingconflict.razas.Raza;
import everlastingconflict.relojes.Reloj;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
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

    public Unidad(Unidad u) {
        this(u.nombre);
    }

    public Unidad(String n) {
        nombre = n;
        descripcion = "";
        statusEffectCollection = new StatusEffectCollection();
        botones = new ArrayList<>();
        //habilidades = new ArrayList<>();
        hostil = true;
        Raza.unidad(this);
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
                } catch (SlickException ex) {
                    Logger.getLogger(Unidad.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            anchura = sprite.getWidth();
            altura = sprite.getHeight();
        }
        anchura_barra_vida = anchura;
        experiencia_al_morir = 15;
    }

    public Unidad(String n, float x, float y) {
        this(n);
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
        Jugador aliado = p.jugador_aliado(this);
        Jugador enemigo = p.jugador_enemigo(this);
        if (statusEffectCollection.existe_estado(StatusEffectName.REGENERACION)) {
            if (this.vida < this.vida_max) {
                aumentar_vida(Reloj.TIME_REGULAR_SPEED * delta);
            }
        }
        if (statusEffectCollection.existe_estado(StatusEffectName.EROSION)) {
            disminuir_vida(p, Reloj.TIME_REGULAR_SPEED * delta * vida_max / 5);
        }
        for (ElementoEspecial e : enemigo.elementos_especiales) {
            if (e.nombre.equals("Agujero negro")) {
                if (this.alcance(e.x, e.y, e.anchura / 2)) {
                    this.destruir(p, null);
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
                            this.enableBuilding(p);
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
                if ((x == edificio_construccion.x) && (y == edificio_construccion.y)) {
                    if (edificio_construccion.vida == 0) {
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
                            this.movil = true;
                            this.mover(p, edificio_construccion.reunion_x, edificio_construccion.reunion_y);
                            edificio_construccion = null;
                            this.enableBuildingButtons();
                        } else {
                            edificio_construccion.vida += (edificio_construccion.vida_max / edificio_construccion.tiempo) * Reloj.TIME_REGULAR_SPEED * delta;
                        }
                    }
                }
                break;
            case ATACAR_MOVER:
                boolean atacar = atacar_cercanos(p);
                if (!atacar) {
                    if (canMove() && (movimiento == null) || (movimiento.puntos.get(0).x != x_atmov || movimiento.puntos.get(0).y != y_atmov)) {
                        anadir_movimiento(x_atmov, y_atmov);
                    }
                } else {
                    movimiento = null;
                }
                break;
            case RECOLECTANDO:
                if ((this.x == recurso.x) && (this.y == recurso.y)) {
                    if (recurso.vida < Recurso.vida_civiles) {
                        if ((recurso.vida + (Recurso.vida_civiles / Recurso.tiempo_captura) * Reloj.TIME_REGULAR_SPEED * delta) >= Recurso.vida_civiles) {
                            this.movil = true;
                            recurso.vida = Recurso.vida_civiles;
                            recurso.capturar(aliado);
                            mover(p, recurso.x, recurso.y + recurso.altura);
                        } else {
                            if (recurso.vida == 0) {
                                aliado.lista_recursos.add(recurso);
                                p.recursos.remove(recurso);
                                this.movil = false;
                            }
                            recurso.vida += (Recurso.vida_civiles / Recurso.tiempo_captura) * Reloj.TIME_REGULAR_SPEED * delta;
                        }
                    }
                }
                break;
            case PARADO:
                for (ElementoEspecial e : enemigo.elementos_especiales) {
                    if (e.nombre.equals("Agujero negro")) {
                        if (this.alcance(300, e)) {
                            this.mover(p, e.x, e.y);
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
                && p.jugador_aliado(this).unidades.indexOf(this) != -1) {
            this.statusBehaviour = StatusBehaviour.DESTRUIDO;
            if (this.seleccionada()) {
                this.deseleccionar();
            }
            p.jugador_aliado(this).poblacion -= this.coste_poblacion;
            p.jugador_aliado(this).unidades.remove(this);
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
        if (canMove()) {
            if (edificio_construccion != null) {
                //Cancelar Edificio
                if (!p.jugador_aliado(this).raza.equals(RaceNameEnum.FENIX.getName())) {
                    p.jugador_aliado(this).addResources(edificio_construccion.coste);
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
        anadir_recoleccion(p, recurso.x, recurso.y);
    }

    public void atacar(ElementoVulnerable atacada) {
        if (canAttack()) {
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
    public void dibujar(Partida p, Color c, Input input, Graphics g) {
        super.dibujar(p, c, input, g);
    }

    public void provocar(ElementoSimple provocador, float t) {
        if (this.objetivo != null) {
            this.objetivo = null;
            this.statusBehaviour = StatusBehaviour.PARADO;
        }
        this.nombre_provocador = provocador.nombre;
        this.provocado_tiempo_contador = this.provocado_tiempo = t;
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
    public void construir(Partida p, Edificio edificio, float x, float y) {
        super.construir(p, edificio, x, y);
        statusBehaviour = StatusBehaviour.CONSTRUYENDO;
        p.jugador_aliado(this).resta_recursos(edificio.coste);
        edificio_construccion = edificio;
        edificio.statusBehaviour = StatusBehaviour.CONSTRUYENDOSE;
        edificio_construccion.cambiar_coordenadas(x, y);
        anadir_movimiento(x, y);
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
        for (Edificio e : p.jugador_aliado(this).edificios) {
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

    public void transformacion_torreta() {
        Edificio torreta = (Edificio) objetivo;
        switch (this.nombre) {
            case "Artillero":
                torreta.nombre = "Torreta demoledora";
                torreta.ataque += 20;
                torreta.cadencia += 0.5f;
                break;
            case "Amparador":
                torreta.nombre = "Muro";
                torreta.hostil = false;
                break;
            case "Ingeniero":
                torreta.nombre = "Estación reparadora";
                torreta.hostil = false;
                break;
            case "Armero":
                torreta.nombre = "Ametralladora";
                torreta.ataque -= 20;
                torreta.cadencia -= 1f;
                break;
            case "Oteador":
                torreta.nombre = "Torreta artillería";
                torreta.ataque += 10;
                torreta.alcance += 100;
                torreta.cadencia += 1.0f;
                break;
            case "Explorador":
                torreta.nombre = "Estación de vigilancia";
                torreta.vision = 1750;
                torreta.hostil = false;
                break;
            case "Corredor":
                torreta.nombre = "Distorsionador temporal";
                torreta.hostil = false;
                break;
        }
        try {
            torreta.sprite = new Animation(new Image[]{new Image("media/Torretas/" + torreta.nombre + ".png")}, 300, true);
            torreta.icono = new Image("media/Torretas/" + torreta.nombre + "_icono.png");
        } catch (SlickException ex) {
            Logger.getLogger(Unidad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enableBuilding(Partida p) {
        Jugador aliado = p.jugador_aliado(this);
        if (objetivo.nombre.equals("Torreta defensiva")) {
            transformacion_torreta();
        } else {
            ((Edificio) objetivo).iniciarbotones(p);
            objetivo.vida = objetivo.vida_max;
        }
        ((Edificio) objetivo).activo = true;
        this.destruir(p, null, false);
        boolean allBuildingsEnabled = aliado.edificios.stream().allMatch(e -> e.activo);
        if (allBuildingsEnabled) {
            // Makes no sense to keep creating enablers
            Edificio ayuntamiento = aliado.edificios.stream().filter(e -> "Ayuntamiento".equals(e.nombre)).findFirst().get();
            ayuntamiento.botones.removeIf(b -> "Activador".equals(b.elemento_nombre));
            ayuntamiento.initButtonKeys();
        }
    }
}
