/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.campaign.tutorial;

import everlastingconflict.gestion.Partida;
import java.util.List;

/**
 *
 * @author Elías
 */
public abstract class Tutorial extends Partida {

    public List<Paso> pasos;

    public abstract void iniciar_pasos();

    public abstract void initElements(int njugador);
}
