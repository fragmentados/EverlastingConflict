/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos;

import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.razas.Raza;
import everlastingconflict.ventanas.VentanaCombate;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        Jugador aliado = p.getPlayerFromElement(this);
        if (this instanceof Edificio) {
            Raza.iniciar_botones_edificio(p, (Edificio) this);
        } else {
            Raza.iniciar_botones_unidad(p, (Unidad) this);
        }
        removeTechnologyButtonsAlreadyResearched(aliado);
    }

    public void initButtonKeys() {
        this.initButtonKeys(this.botones);
    }

    public void initButtonKeys(List<BotonComplejo> botones) {
        List<Integer> keysNumber = Arrays.asList(Input.KEY_Q, Input.KEY_W, Input.KEY_E, Input.KEY_R,
                Input.KEY_A, Input.KEY_S, Input.KEY_D, Input.KEY_F, Input.KEY_Z, Input.KEY_X, Input.KEY_C, Input.KEY_V);
        List<String> keysText = Arrays.asList("Q", "W", "E", "R", "A", "S", "D", "F", "Z", "X", "C", "V");
        Integer keysIndex = 0;
        for (int i = 0; i < botones.size(); i++) {
            if (!botones.get(i).isPassiveAbility) {
                botones.get(i).tecla = keysNumber.get(keysIndex);
                botones.get(i).tecla_string = keysText.get(keysIndex);
                keysIndex++;
            }
        }
    }

    public void disableBuildingButtons() {
        this.botones.stream().filter(b -> "Edificio".equals(b.elemento_tipo)).forEach(b -> b.canBeUsed = false);
    }

    public void enableBuildingButtons() {
        this.botones.stream().filter(b -> "Edificio".equals(b.elemento_tipo)).forEach(b -> b.canBeUsed = true);
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
