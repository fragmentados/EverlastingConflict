/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos;

import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.mapas.VentanaCombate;
import everlastingconflict.razas.Raza;
import everlastingconflict.elementosvisuales.BotonComplejo;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

/**
 *
 * @author Elías
 */
public abstract class ElementoComplejo extends ElementoVulnerable {
    
    public Image miniatura;    
    public int vision;
    public List<BotonComplejo> botones = new ArrayList<>();

    public void seleccionar() {
        VentanaCombate.ui.seleccionar(this);
    }

    public void seleccionar(boolean mayus) {
        VentanaCombate.ui.seleccionar(this, mayus);
    }

    public void deseleccionar() {
        VentanaCombate.ui.deseleccionar(this);
    }

    public boolean seleccionada() {
        return (VentanaCombate.ui.elementos.indexOf(this) != -1);
    }

    public void iniciarbotones(Partida p) {
        Jugador aliado = p.jugador_aliado(this);
        if (this instanceof Edificio) {
            Raza.iniciar_botones_edificio(p, (Edificio) this);
        } else {
            Raza.iniciar_botones_unidad(p, (Unidad) this);
        }
        removeTechnologyButtonsAlreadyResearched(aliado);
    }

    public void inicializar_teclas_botones(List<BotonComplejo> botones) {
        if (botones.size() > 0) {
            botones.get(0).tecla = Input.KEY_Q;
            botones.get(0).tecla_string = "Q";
        }
        if (botones.size() > 1) {
            botones.get(1).tecla = Input.KEY_W;
            botones.get(1).tecla_string = "W";
        }
        if (botones.size() > 2) {
            botones.get(2).tecla = Input.KEY_E;
            botones.get(2).tecla_string = "E";
        }
        if (botones.size() > 3) {
            botones.get(3).tecla = Input.KEY_R;
            botones.get(3).tecla_string = "R";
        }
        if (botones.size() > 4) {
            botones.get(4).tecla = Input.KEY_A;
            botones.get(4).tecla_string = "A";
        }
        if (botones.size() > 5) {
            botones.get(5).tecla = Input.KEY_S;
            botones.get(5).tecla_string = "S";
        }
        if (botones.size() > 6) {
            botones.get(6).tecla = Input.KEY_D;
            botones.get(6).tecla_string = "D";
        }
        if (botones.size() > 7) {
            botones.get(7).tecla = Input.KEY_F;
            botones.get(7).tecla_string = "F";
        }
    }

    public void disableBuildingButtons() {
        this.botones.stream().filter(b -> "Edificio".equals(b.elemento_tipo)).forEach(b -> b.activado = false);
    }

    public void enableBuildingButtons() {
        this.botones.stream().filter(b -> "Edificio".equals(b.elemento_tipo)).forEach(b -> b.activado = true);
    }

    @Override
    public void dibujar(Partida p, Color c, Input input, Graphics g) {
        super.dibujar(p, c, input, g);        
    }        
    
    @Override
    public abstract void destruir(Partida p, ElementoAtacante atacante);

    public void checkButtonResources(Jugador aliado) {
        botones.stream().filter(b -> b.elemento_coste > 0).forEach(b -> b.checkIfEnabled(aliado));
    }

    public void removeTechnologyButtonsAlreadyResearched(Jugador aliado) {
        botones.removeIf(b -> "Tecnología".equals(b.elemento_tipo) && aliado.hasTecnologyResearched(b.elemento_nombre));
    }
}
