/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elements;

import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elements.impl.Edificio;
import everlastingconflict.elements.impl.Unidad;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.Raza;
import everlastingconflict.windows.WindowCombat;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class ElementoComplejo extends ElementoVulnerable {
    
    public Image miniatura;    
    public int vision;
    public List<BotonComplejo> botones = new ArrayList<>();

    public void seleccionar() {
        WindowCombat.ui.seleccionar(this);
    }

    public void seleccionar(boolean mayus) {
        WindowCombat.ui.seleccionar(this, mayus);
    }

    public void deseleccionar() {
        WindowCombat.ui.deseleccionar(this);
    }

    public boolean seleccionada() {
        return (WindowCombat.ui.elements.indexOf(this) != -1);
    }

    public void iniciarbotones(Game p) {
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
    public void render(Animation sprite, Game p, Color c, Input input, Graphics g) {
        super.render(sprite, p, c, input, g);
    }

    @Override
    public void render(Game p, Color c, Input input, Graphics g) {
        this.render(this.animation, p, c, input, g);
    }        
    
    @Override
    public abstract void destruir(Game p, ElementoAtacante atacante);

    public void checkButtonResources(Jugador aliado) {
        botones.stream().filter(b -> b.elemento_coste > 0).forEach(b -> b.checkIfEnabled(aliado));
    }

    public void removeTechnologyButtonsAlreadyResearched(Jugador aliado) {
        botones.removeIf(b -> "Tecnología".equals(b.elemento_tipo) && aliado.hasTecnologyResearched(b.elemento_nombre));
    }
}
