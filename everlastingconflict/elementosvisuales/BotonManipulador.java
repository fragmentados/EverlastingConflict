/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementosvisuales;

import everlastingconflict.elementos.ElementoComplejo;
import everlastingconflict.elementos.implementacion.Habilidad;
import everlastingconflict.elementos.implementacion.Manipulador;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.elementos.util.ElementosComunes;
import everlastingconflict.estados.StatusEffect;
import everlastingconflict.estados.StatusEffectName;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.Maestros;
import everlastingconflict.relojes.RelojMaestros;
import everlastingconflict.ventanas.UI;
import everlastingconflict.ventanas.VentanaCombate;
import org.newdawn.slick.Graphics;

import java.util.List;

import static everlastingconflict.ventanas.VentanaCombate.VIEWPORT_SIZE_HEIGHT;
import static everlastingconflict.ventanas.VentanaCombate.playerY;

/**
 *
 * @author Elías
 */
public class BotonManipulador extends BotonComplejo {

    public String requisito;

    public BotonManipulador(String t) {
        super(t);
    }

    public BotonManipulador(String t, String description) {
        this(t);
        this.descripcion = description;
    }

    public BotonManipulador(Habilidad h) {
        super(h);
    }

    public BotonManipulador(Habilidad h, String r) {
        super(h);
        requisito = r;
    }

    @Override
    public void resolucion(List<ElementoComplejo> el, ElementoComplejo e, Partida partida) {
        if (canBeUsed && !isPassiveAbility) {
            if (e instanceof Manipulador) {
                Manipulador m = (Manipulador) e;
                if (texto == null) {
                    if (!m.enhancementButtons.isEmpty()) {
                        //El manipulador aprende la habilidad
                        m.applyEnhancement("Habilidades", this);
                    } else {
                        if (m.puede_usar_habilidad()) {
                            new Habilidad(this.elemento_nombre).activacion(partida, m);
                        }
                    }
                } else {
                    switch (texto) {
                        case "Habilidades":
                            m.getSkillsToUnlock();
                            break;
                        case "Atributos":
                            m.obtener_botones_atributos();
                            break;
                        //Pasivas
                        case "Lider de hordas":
                            Manipulador.lider = true;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Exterminador de masas":
                            m.ataque += 10;
                            m.cadencia -= 0.5f;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Sabiduría arcana":
                            m.mana_max += 150;
                            m.mana += 150;
                            m.regeneracion_mana += 0.3f;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Cauterización automática":
                            m.statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.REGENERACION));
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Entrenamiento avanzado":
                            Maestros.cadencia_pugnator -= 0.1f;
                            Maestros.ataque_saggitarius += 5;
                            Maestros.velocidad_exterminatore += 0.2f;
                            Maestros.area_magum += 25;
                            Maestros.ataque_medicum += 5;
                            for (Unidad u : partida.getPlayerFromElement(m).unidades) {
                                switch (u.nombre) {
                                    case "Pugnator":
                                        u.cadencia -= 0.1f;
                                        break;
                                    case "Medicum":
                                    case "Saggitarius":
                                        u.ataque += 5;
                                        break;
                                    case "Exterminatore":
                                        u.velocidad += 0.2f;
                                        break;
                                    case "Magum":
                                        u.area += 25;
                                        break;
                                }
                            }
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Tirador experto":
                            m.ataque += 10;
                            m.alcance += 100;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Poderío astral":
                            m.poder_magico += 25;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Protección mística":
                            m.vida += 150;
                            m.vida_max += 150;
                            m.defensa += 2;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Invocación eficiente":
                            Manipulador.cantidad_invocacion++;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Incineración ígnea":
                            m.area = 50;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Control temporal":
                            Manipulador.minimo_cooldown = 1;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Entrenamiento supremo":
                            Maestros.habilidades = true;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Reinversión física":
                            m.robo_vida = true;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Reinversión mágica":
                            m.succion_hechizo = true;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Fuerzas mixtas":
                            m.fuerzas_mixtas = true;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Último recurso":
                            m.ultimo_recurso = true;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Disparo helado":
                            m.disparo_helado = true;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Clon":
                            Manipulador m2 = new Manipulador(m.x + m.anchura / 2 + 10, m.y);
                            //Dividir las estadísticas del antiguo manipulador a la mitad
                            m.ataque /= 2;
                            m.defensa /= 2;
                            m.vida_max /= 2;
                            m.vida /= 2;
                            m.mana_max /= 2;
                            m.mana /= 2;
                            m.poder_magico /= 2;
                            m.reduccion_enfriamiento /= 2;
                            //Transferir las nuevas estadísticas al antiguo manipulador
                            m2.ataque = m.ataque;
                            m2.defensa = m.defensa;
                            m2.vida = m2.vida_max = m.vida_max;
                            m2.mana = m2.mana_max = m.mana_max;
                            m2.poder_magico = m.poder_magico;
                            m2.reduccion_enfriamiento = m.reduccion_enfriamiento;
                            m2.botones = m.botones;
                            m2.initButtonKeys();
                            partida.getPlayerFromElement(m).unidades.add(m2);
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Eficiencia energética":
                            Manipulador.eficiencia = true;
                            m.applyEnhancement("Habilidades", this);
                            break;
                        case "Inspiración":
                            Manipulador.alentar = true;
                            m.applyEnhancement("Habilidades", this);
                            break;                        
                        //Atributos    
                        case "Maná":
                            m.mana_max += 50;
                            m.mana += 50;
                            m.applyEnhancement("Atributos", null);
                            break;
                        case "Vida":
                            m.vida_max += 50;
                            m.vida += 50;
                            m.applyEnhancement("Atributos", null);
                            break;
                        case "Defensa":
                            m.defensa++;
                            m.applyEnhancement("Atributos", null);
                            break;
                        case "Regeneración maná":
                            m.regeneracion_mana += 0.1f;
                            m.applyEnhancement("Atributos", null);
                            break;
                        case "Ataque":
                            m.ataque += 5;
                            m.applyEnhancement("Atributos", null);
                            break;
                        case "Reducción de enfriamiento":
                            m.aumentar_reduccion(2);
                            m.applyEnhancement("Atributos", null);
                            break;
                        case "Poder mágico":
                            m.poder_magico += 5;
                            m.applyEnhancement("Atributos", null);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void renderExtendedInfo(Jugador aliado, Graphics g, ElementoComplejo origen) {
        if (canBeShown) {
            super.renderExtendedInfo(aliado, g, origen);
            if (requisito != null) {
                float x = VentanaCombate.playerX + 601;
                float y = playerY + VIEWPORT_SIZE_HEIGHT - UI.UI_HEIGHT - 201;
                g.drawString("Esta habilidad sólo se puede utilizar de " + requisito, x, y  + 150);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (canBeShown) {
            super.render(g);
            if (requisito != null) {
                g.drawRect(x + anchura / 2, y + altura / 2, 20, 20);
                if (RelojMaestros.nombre_dia.equals(requisito)) {
                    ElementosComunes.DAY_IMAGE.draw(x + anchura / 2, y + altura / 2, 20, 20);
                } else if (RelojMaestros.nombre_noche.equals(requisito)) {
                    ElementosComunes.NIGHT_IMAGE.draw(x + anchura / 2, y + altura / 2, 20, 20);
                }
            }
        }
    }

    @Override
    public void checkIfEnabled(Jugador aliado) {
        super.checkIfEnabled(aliado);
        if (requisito != null && this.canBeUsed && !RelojMaestros.tiempo.equals(requisito)) {
           this.canBeUsed =  false;
        }
    }
}
