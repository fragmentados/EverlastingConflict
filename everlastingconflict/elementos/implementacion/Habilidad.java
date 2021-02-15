/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos.implementacion;

import everlastingconflict.estados.StatusEffect;
import everlastingconflict.estados.StatusEffectName;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.gestion.Vision;
import everlastingconflict.mapas.VentanaCombate;
import everlastingconflict.mapas.VentanaPrincipal;
import everlastingconflict.razas.Clark;
import everlastingconflict.elementos.ElementoComplejo;
import everlastingconflict.elementos.ElementoSimple;
import everlastingconflict.elementos.ElementoVulnerable;
import everlastingconflict.elementosvisuales.BotonComplejo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import everlastingconflict.relojes.RelojMaestros;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Elías
 */
public class Habilidad extends ElementoSimple {

    public String tipo_seleccion;
    public float alcance;
    public float area;
    public float cooldown;

    public Habilidad(String n) {
        nombre = n;
        descripcion = "";
        inicializar_datos();
        try {
            sprite = new Animation(new Image[]{new Image("media/Iconos/" + nombre + ".png")}, new int[]{300}, false);
            icono = new Image("media/Iconos/" + nombre + ".png");
        } catch (SlickException ex) {
            Logger.getLogger(Habilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final void inicializar_datos() {
        boolean magia = false;
        switch (nombre) {
            case "Parálisis temporal":
                alcance = 0;
                cooldown = 300;
                descripcion = "Detiene el Reloj Eternium durante un minuto.";
                break;
            case "Cambiar modo":
                alcance = 0;
                cooldown = 10;
                descripcion = "Cambia el modo de ataque del Moldeador de ataque a distancia a ataque cuerpo a cuerpo y viceversa.";
                break;
            case "Eliminación":
                alcance = 200;
                tipo_seleccion = "Bestia";
                cooldown = 30;
                descripcion = "Destruye una bestia instantáneamente y recolecta los recursos que proporciona la bestia.";
                break;
            case "Domesticar":
                alcance = 100;
                tipo_seleccion = "Bestia";
                cooldown = 60;
                descripcion = "Destruye un grupo de bestias instantáneamente y recolecta los recursos que proporcionan las bestias.";
                break;
            case "Redención":
                alcance = 100;
                area = 100;
                tipo_seleccion = "Coordenadas";
                cooldown = 60;
                descripcion = "Cura por completo a las unidades aliadas y enemigas en una pequeña área.";
                break;
            case "Inmolación":
                alcance = 150;
                tipo_seleccion = "UnidadEnemiga";
                cooldown = 0;
                descripcion = "Destruye a la unidad objetivo y al Inspirador instantáneamente.";
                break;
            case "Provocar":
                alcance = 150;
                tipo_seleccion = "UnidadEnemigaoBestia";
                cooldown = 30;
                descripcion = "Obliga a la unidad objetivo a atacar al Defensor durante un tiempo.";
                break;
            case "Alentar":
                alcance = 200;
                area = 100;
                tipo_seleccion = "Coordenadas";
                cooldown = 30;
                descripcion = "Aumenta el ataque de las unidades aliadas en una pequeña área durante un tiempo.";
                break;
            case "Presencia abrumadora":
                alcance = 200;
                tipo_seleccion = "Coordenadas";
                cooldown = 20;
                area = 100;
                descripcion = "Obliga a las unidades objetivo en una pequeña área a atacar al Manipulador durante un tiempo.";
                break;
            case "Tiempos de necesidad":
                cooldown = 20;
                descripcion = "Vuelve invulnerable al Manipulador y le impide usar habilidades durante un tiempo.";
                break;
            case "Pacifismo":
                alcance = 100;
                tipo_seleccion = "UnidadEnemiga";
                cooldown = 20;
                descripcion = "Impide a la unidad enemiga objetiva atacar durante un  tiempo.";
                break;
            case "Silencio":
                alcance = 100;
                tipo_seleccion = "UnidadEnemiga";
                cooldown = 20;
                descripcion = "Impide a la unidad enemiga objetiva usar habilidades durante un  tiempo.";
                break;
            case "Intervención divina":
                alcance = 150;
                tipo_seleccion = "Coordenadas";
                area = 50;
                cooldown = 10;
                descripcion = "Cura a las unidades amigas afectadas y daña a las unidades enemigas afectadas.";
                break;
            case "Defensa férrea":
                cooldown = 3;
                descripcion = "Duplica la defensa del Inquisidor pero reduce su velocidad a la mitad. Pulsa otra vez para cancelar.";
                break;
            //Magias Manipulador    
            case "Eclipse Amanecer":
                cooldown = 300;
                descripcion = "Cambia de día a noche y viceversa instantáneamente.";
                break;
            case "Invocar pugnator":
                alcance = 100;
                tipo_seleccion = "Coordenadas";
                coste = 50;
                cooldown = 10;
                magia = true;
                descripcion = "Invoca un grupo de pugnators en la localización seleccionada.";
                break;
            case "Invocar sagittarius":
                alcance = 100;
                tipo_seleccion = "Coordenadas";
                coste = 75;
                cooldown = 10;
                magia = true;
                descripcion = "Invoca un grupo de sagittarius en la localización seleccionada.";
                break;
            case "Invocar medicum":
                alcance = 100;
                tipo_seleccion = "Coordenadas";
                coste = 100;
                cooldown = 10;
                magia = true;
                descripcion = "Invoca un grupo de medicums en la localización seleccionada.";
                break;
            case "Invocar magum":
                alcance = 100;
                tipo_seleccion = "Coordenadas";
                coste = 100;
                cooldown = 10;
                magia = true;
                descripcion = "Invoca un grupo de magums en la localización seleccionada.";
                break;
            case "Invocar exterminatore":
                alcance = 100;
                tipo_seleccion = "Coordenadas";
                coste = 100;
                cooldown = 10;
                magia = true;
                descripcion = "Invoca un grupo de exterminatores en la localización seleccionada.";
                break;
            case "Deflagración":
                alcance = 150;
                tipo_seleccion = "UnidadEnemigaoBestia";
                coste = 60;
                cooldown = 5;
                magia = true;
                descripcion = "Inflige 200 puntos de daño más el poder mágico del Manipulador a la unidad o bestia enemiga objetiva.";
                break;
            case "Pesadilla":
                alcance = 100;
                tipo_seleccion = "UnidadEnemiga";
                coste = 30;
                cooldown = 5;
                magia = true;
                descripcion = "Stunea a la unidad objetivo durante diez segundos.";
                break;
            case "Protección":
                alcance = 100;
                tipo_seleccion = "UnidadAliada";
                coste = 40;
                cooldown = 6;
                magia = true;
                descripcion = "La unidad aliada objetivo obtiene un escudo de 100 puntos más el 120% del poder mágico del Manipulador.";
                break;
            case "Lluvia de estrellas":
                alcance = 200;
                coste = 50;
                tipo_seleccion = "Coordenadas";
                cooldown = 6;
                area = 100;
                magia = true;
                descripcion = "Inflige 50 puntos de daño más la mitad del poder mágico del manipulador en una gran área.";
                break;
            case "Meteoro":
                alcance = 200;
                coste = 50;
                tipo_seleccion = "Coordenadas";
                cooldown = 6;
                area = 50;
                magia = true;
                descripcion = "Inflige 65 puntos de daño más el 70% del poder de mágico del manipulador en una pequeña área.";
                break;
            case "Regeneración":
                alcance = 200;
                coste = 50;
                tipo_seleccion = "UnidadAliada";
                cooldown = 6;
                magia = true;
                descripcion = "La unidad aliada objetivo ve restaurada su vida continuamente durante 10 segundos.";
                break;
            case "Teletransporte":
                alcance = 100;
                coste = 70;
                tipo_seleccion = "Coordenadas";
                cooldown = 15;
                magia = true;
                descripcion = "El Manipulador se mueve a la localización seleccionada automáticamente.";
                break;
            case "Apocalipsis":
                alcance = 100;
                coste = 150;
                tipo_seleccion = "Coordenadas";
                cooldown = 30;
                area = 100;
                magia = true;
                descripcion = "Destruye instantáneamente a todas las unidades enemigas en una gran área.";
                break;
            case "Erosión":
                alcance = 100;
                coste = 70;
                tipo_seleccion = "UnidadEnemiga";
                cooldown = 10;
                magia = true;
                descripcion = "La unidad enemiga objetivo ve reducida su vida paulatinamente hasta que es destruida.";
                break;
            case "Trampa solar":
                alcance = 200;
                coste = 80;
                area = 150;
                tipo_seleccion = "Coordenadas";
                cooldown = 20;
                descripcion = "Inmoviliza a las unidades en una gran área durante un tiempo.";
                break;
            case "Sacrificio":
                coste = 10;
                alcance = 150;
                tipo_seleccion = "Invocacion";
                cooldown = 60;
                descripcion = "Destruye a la invocacion aliada objetivo y aumenta la vida y el maná del Manipulador en una cantidad igual a la vida de la invocación detruida.";
                break;
            case "Visión interestelar":
                coste = 50;
                alcance = 1000;
                tipo_seleccion = "Coordenadas";
                area = 300;
                descripcion = "Permite ver una zona del mapa durante un tiempo.";
                break;
            case "Impactos drenantes":
                cooldown = 20;
                descripcion = "Aumenta el daño producido por los ataques del Manipulador pero estos disminuyen su maná durante un tiempo.";
                break;
            case "Agujero negro":
                cooldown = 300;
                coste = 150;
                alcance = 200;
                tipo_seleccion = "Coordenadas";
                descripcion = "Invoca un agujero negro en la situación que atrae a las unidades paradas cercanas y las destruye en el momento en el que entran en contacto con él.";
                break;
        }
        if (magia) {
            if (Manipulador.eficiencia) {
                coste -= 0.35f * coste;
            }
        }
    }

    public ElementoComplejo seleccion_objetivo(Partida p, ElementoComplejo origen, float x, float y) {
        Jugador aliado = p.jugador_aliado(origen), enemigo = p.jugador_enemigo(origen);
        List<ElementoComplejo> contador = new ArrayList<>();
        switch (tipo_seleccion) {
            case "EdificioAliado":
                contador.addAll(aliado.edificios);
                break;
            case "EdificioEnemigo":
                contador.addAll(enemigo.edificios);
                break;
            case "UnidadAliada":
                contador.addAll(aliado.unidades);
                break;
            case "UnidadEnemiga":
                contador.addAll(enemigo.unidades);
                break;
            case "Invocacion":
                contador.addAll(aliado.unidades);
                for (ElementoComplejo e : contador) {
                    if (e instanceof Manipulador) {
                        contador.remove(e);
                        break;
                    }
                }
                break;
            case "Bestia":
                for (Bestias be : p.bestias) {
                    contador.addAll(be.contenido);
                }
                break;
            case "UnidadEnemigaoBestia":
                contador.addAll(enemigo.unidades);
                for (Bestias be : p.bestias) {
                    contador.addAll(be.contenido);
                }
                break;
            case "Coordenadas":
                return new Unidad("No hay", x, y);
        }
        for (ElementoComplejo e : contador) {
            if (e.hitbox(x, y)) {
                return e;
            }
        }
        return null;
    }

    public void activacion(Partida p, ElementoComplejo origen) {
        if ((coste == 0) || (((Manipulador) origen).mana >= coste)) {
            if (alcance == 0) {
                resolucion(p, origen, origen);
            } else {
                VentanaPrincipal.mapac.elemento_habilidad = origen;
                VentanaPrincipal.mapac.habilidad = this;
            }
        } else {

        }
    }

    public List<ElementoComplejo> area(Partida p, ElementoComplejo origen, float x, float y, String t) {
        Jugador aliado = p.jugador_aliado(origen), enemigo = p.jugador_enemigo(origen);
        List<ElementoComplejo> resultado = new ArrayList<>();
        switch (t) {
            case "UnidadEnemiga":
                for (Unidad u : enemigo.unidades) {
                    if (u.alcance(x, y, area)) {
                        resultado.add(u);
                    }
                }
                break;
            case "UnidadAliada":
                for (Unidad u : aliado.unidades) {
                    if (u.alcance(x, y, area)) {
                        resultado.add(u);
                    }
                }
                break;
            case "UnidadCualquiera":
                for (Unidad u : aliado.unidades) {
                    if (u.alcance(x, y, area)) {
                        resultado.add(u);
                    }
                }
                for (Unidad u : enemigo.unidades) {
                    if (u.alcance(x, y, area)) {
                        resultado.add(u);
                    }
                }
                break;
        }
        return resultado;
    }

    public void resolucion(Partida p, ElementoComplejo origen, ElementoVulnerable objetivo) {
        Bestia bestia;
        List<ElementoComplejo> elementos_area;
        List<Unidad> unidades = new ArrayList<>();
        Jugador aliado = p.jugador_aliado(origen);
        Manipulador m;
        if (coste > 0) {
            m = (Manipulador) origen;
            m.mana -= coste;
            switch (nombre) {
                case "Invocar pugnator":
                    for (int i = 0; i < Manipulador.cantidad_invocacion; i++) {
                        unidades.add(new Unidad("Pugnator"));
                    }
                    VentanaPrincipal.mapac.pathing_prueba(false, unidades, objetivo.x, objetivo.y);
                    aliado.unidades.addAll(unidades);
                    break;
                case "Invocar sagittarius":
                    for (int i = 0; i < Manipulador.cantidad_invocacion; i++) {
                        unidades.add(new Unidad("Sagittarius"));
                    }
                    VentanaPrincipal.mapac.pathing_prueba(false, unidades, objetivo.x, objetivo.y);
                    aliado.unidades.addAll(unidades);
                    break;
                case "Invocar medicum":
                    for (int i = 0; i < Manipulador.cantidad_invocacion; i++) {
                        unidades.add(new Unidad("Medicum"));
                    }
                    VentanaPrincipal.mapac.pathing_prueba(false, unidades, objetivo.x, objetivo.y);
                    aliado.unidades.addAll(unidades);
                    break;
                case "Invocar magum":
                    for (int i = 0; i < Manipulador.cantidad_invocacion; i++) {
                        unidades.add(new Unidad("Magum"));
                    }
                    VentanaPrincipal.mapac.pathing_prueba(false, unidades, objetivo.x, objetivo.y);
                    aliado.unidades.addAll(unidades);
                    break;
                case "Invocar exterminatore":
                    for (int i = 0; i < Manipulador.cantidad_invocacion; i++) {
                        unidades.add(new Unidad("Exterminatore"));
                    }
                    VentanaPrincipal.mapac.pathing_prueba(false, unidades, objetivo.x, objetivo.y);
                    aliado.unidades.addAll(unidades);
                    break;
                case "Deflagración":
                    m.dano(p, "Mágico", 200 + (int) m.poder_magico, objetivo);
                    break;
                case "Pesadilla":
                    ((Unidad) objetivo).statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.STUN, 10));
                    break;
                case "Protección":
                    ((Unidad) objetivo).escudo += 100 + (int) (m.poder_magico * 1.2f);
                    break;
                case "Lluvia de estrellas":
                    elementos_area = area(p, origen, objetivo.x, objetivo.y, "UnidadEnemiga");
                    for (ElementoComplejo e : elementos_area) {
                        m.dano(p, "Mágico", 50 + (int) (m.poder_magico * 0.5f), e);
                    }
                    break;
                case "Meteoro":
                    elementos_area = area(p, origen, objetivo.x, objetivo.y, "UnidadEnemiga");
                    for (ElementoComplejo e : elementos_area) {
                        m.dano(p, "Mágico", 65 + (int) (m.poder_magico * 0.7f), e);
                    }
                    break;
                case "Apocalipsis":
                    elementos_area = area(p, origen, objetivo.x, objetivo.y, "UnidadEnemiga");
                    for (ElementoComplejo e : elementos_area) {
                        e.destruir(p, m);
                    }
                    break;
                case "Regeneración":
                    ((Unidad) objetivo).statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.REGENERACION, 10f));
                    break;
                case "Teletransporte":
                    m.x = objetivo.x;
                    m.y = objetivo.y;
                    break;
                case "Erosión":
                    ((Unidad) objetivo).statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.EROSION));
                    break;
                case "Trampa solar":
                    elementos_area = area(p, origen, objetivo.x, objetivo.y, "UnidadEnemiga");
                    for (ElementoComplejo e : elementos_area) {
                        ((Unidad) e).statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.SNARE, 10));
                    }
                    break;
                case "Sacrificio":
                    m.aumentar_vida(((Unidad) objetivo).vida);
                    m.aumentar_mana(((Unidad) objetivo).vida);
                    ((Unidad) objetivo).destruir(p, m);
                    break;
                case "Visión interestelar":
                    aliado.visiones.add(new Vision(objetivo.x, objetivo.y, area, area, 3f));
                    break;
                case "Agujero negro":
                    aliado.elementos_especiales.add(new ElementoEspecial("Agujero negro", objetivo.x, objetivo.y));
                    break;
            }
        } else {
            switch (nombre) {
                case "Cambiar modo":
                    Unidad unidad = (Unidad) origen;
                    if (unidad.alcance == Clark.alcance_moldeador_cac) {
                        unidad.alcance = Clark.alcance_moldeador_distancia;
                        unidad.ataque = Clark.ataque_moldeador_distancia;
                    } else {
                        unidad.alcance = Clark.alcance_moldeador_cac;
                        unidad.ataque = Clark.ataque_moldeador_cac;
                    }
                    break;
                case "Eliminación":
                    bestia = (Bestia) objetivo;
                    bestia.destruir(p, (Unidad) origen);
                    break;
                case "Domesticar":
                    bestia = (Bestia) objetivo;
                    for (Bestias be : p.bestias) {
                        if (be.contenido.indexOf(bestia) != -1) {
                            be.destruir(p, (Unidad) origen);
                            break;
                        }
                    }
                    break;
                case "Inmolación":
                    objetivo.destruir(p, (Unidad) origen);
                    origen.destruir(p, (Unidad) origen);
                    break;
                case "Redención":
                    elementos_area = area(p, origen, objetivo.x, objetivo.y, "UnidadCualquiera");
                    for (ElementoComplejo e : elementos_area) {
                        e.vida = e.vida_max;
                    }
                    break;
                case "Provocar":
                    ((Unidad) objetivo).provocar(origen, 3000);
                    break;
                case "Parálisis temporal":
                    VentanaCombate.relojEternium().detener_reloj(60);
                    break;
                case "Alentar":
                    elementos_area = area(p, origen, objetivo.x, objetivo.y, "UnidadAliada");
                    for (ElementoSimple e : elementos_area) {
                        ((Unidad) e).aumentar_ataque(5, 1000);
                    }
                    break;
                case "Presencia abrumadora":
                    elementos_area = area(p, origen, objetivo.x, objetivo.y, "UnidadEnemiga");
                    for (ElementoSimple e : elementos_area) {
                        ((Unidad) e).provocar(origen, 10f);
                    }
                    break;
                case "Tiempos de necesidad":
                    ((Unidad) origen).statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.NECESIDAD, 10));
                    break;
                case "Ansia de supervivencia":
                    ((Unidad) origen).statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.SUPERVIVENCIA, 10));
                    break;
                case "Eclipse Amanecer":
                    RelojMaestros relojMaestros = VentanaCombate.relojMaestros();
                    if (relojMaestros.ndivision == 1) {
                        relojMaestros.contador_reloj = relojMaestros.fin_primera_mitad;
                    } else {
                        relojMaestros.contador_reloj = 0;
                    }
                    break;
                case "Meditar":
                    if (((Unidad) origen).statusEffectCollection.existe_estado(StatusEffectName.MEDITACION)) {
                        ((Unidad) origen).statusEffectCollection.eliminar_estado(StatusEffectName.MEDITACION);
                    } else {
                        ((Unidad) origen).statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.MEDITACION));
                    }
                    break;
                case "Impactos drenantes":
                    ((Unidad) origen).statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.DRENANTES, 10f));
                    break;
                case "Pacifismo":
                    ((Unidad) objetivo).statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.PACIFISMO, 10f));
                    break;
                case "Silencio":
                    ((Unidad) objetivo).statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.SILENCIO, 10f));
                    break;
                case "Defensa férrea":
                    Unidad objetivo_u = (Unidad) objetivo;
                    if (objetivo_u.statusEffectCollection.existe_estado(StatusEffectName.DEFENSA)) {
                        objetivo_u.defensa /= 2;
                        objetivo_u.velocidad *= 2;
                        objetivo_u.statusEffectCollection.eliminar_estado(StatusEffectName.DEFENSA);
                    } else {
                        objetivo_u.defensa *= 2;
                        objetivo_u.velocidad /= 2;
                        objetivo_u.statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.DEFENSA));
                    }
                    break;
                case "Intervención divina":
                    elementos_area = area(p, origen, objetivo.x, objetivo.y, "UnidadEnemiga");
                    for (ElementoComplejo e : elementos_area) {
                        ((Unidad) e).aumentar_vida(20);
                    }
                    elementos_area = area(p, origen, objetivo.x, objetivo.y, "UnidadAliada");
                    for (ElementoComplejo e : elementos_area) {
                        ((Unidad)origen).dano(p, "Mágico", 30, e);                        
                    }
                    break;
            }
        }
        VentanaPrincipal.mapac.elemento_habilidad = null;
        VentanaPrincipal.mapac.habilidad = null;
        for (BotonComplejo b : origen.botones) {
            if (b.elemento_nombre != null && b.elemento_nombre.equals(nombre)) {
                b.activar_cooldown(origen);
                break;
            }
        }
        if (origen instanceof Manipulador) {
            Manipulador mani = (Manipulador) origen;
            if (mani.fuerzas_mixtas) {
                mani.statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.ATAQUE_POTENCIADO, 15f, 10));
            }
        }
    }
}
