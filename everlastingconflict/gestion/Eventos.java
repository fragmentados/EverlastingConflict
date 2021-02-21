/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.gestion;

import everlastingconflict.mapas.VentanaCombate;
import everlastingconflict.relojes.Reloj;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Elías
 */
class Comparador implements Comparator<Evento> {

    Comparador() {
    }

    // Overriding the compare method to sort the age 
    @Override
    public int compare(Evento e1, Evento e2) {
        if ((e1.positivo && e2.positivo) && (!e1.positivo && !e2.positivo)) {
            return 0;
        } else {
            if (e1.positivo && !e2.positivo) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}

public class Eventos {

    public List<Evento> contenido;
    public float tiempo, tiempo_contador;
    public boolean activo;
    public boolean desactivacion_permanente;

    public Eventos() {
        contenido = new ArrayList<>();
        tiempo = 20f;
        activar();
    }

    public void reiniciar_temporizador() {
        tiempo_contador = tiempo;
    }

    public void activar() {
        activo = true;
        reiniciar_temporizador();
    }

    public void desactivar() {
        activo = false;
    }

    public void desactivacion_permanente() {
        desactivacion_permanente = true;
        for (int i = 0; i < contenido.size(); i++) {
            Evento ev = contenido.get(i);
            if (!ev.positivo) {
                contenido.remove(i);
                i--;
            }
        }
    }

    public void eliminar_evento(Evento e) {
        contenido.remove(e);
        activar();
    }

    public void anadir_evento(Jugador j, Evento e) {
        boolean anadir = true;
        for (Evento ev : contenido) {
            if (ev.nombre.equals(e.nombre)) {
                anadir = false;
                break;
            }
        }
        if (anadir) {
            contenido.add(e);
            Collections.sort(contenido, new Comparador());
            if (esta_lleno(j)) {
                desactivar();
            }
        }
    }

    public boolean existe_evento(String n) {
        for (Evento ev : contenido) {
            if (ev.nombre.equals(n)) {
                return true;
            }
        }
        return false;
    }

    public boolean esta_lleno(Jugador j) {
        boolean resultado = true;
        if (j.guardiansThreatLevel == 3) {
            if (!existe_evento("Una revolución se acerca")) {
                resultado = false;
            }
        } else if (j.guardiansThreatLevel == 2) {
            if (!existe_evento("Manifestaciones en las plazas")) {
                resultado = false;
            }
        } else {
            if (!existe_evento("Una racha criminal asola las calles")) {
                resultado = false;
            }
        }
        return resultado;
    }

    public void aparece_nuevo_evento(Jugador j) {
        if (j.guardiansThreatLevel == 3) {
            anadir_evento(j, new Evento("Una revolución se acerca"));
        } else {
            if (j.guardiansThreatLevel == 2) {
                anadir_evento(j, new Evento("Manifestaciones en las plazas"));
            } else {
                anadir_evento(j, new Evento("Una racha criminal asola las calles"));
            }
        }
        //Activar reloj
        if (activo) {
            reiniciar_temporizador();
        }
    }

    public void comportamiento(Jugador j, int delta) {
        //Se comprueban los eventos 
        for (int i = 0; i < contenido.size(); i++) {
            if (contenido.get(i).comportamiento(j, delta)) {
                if (!contenido.get(i).positivo) {
                    eliminar_evento(contenido.get(i));
                    i--;
                }
            }
        }
        //Se añade un evento
        if (!desactivacion_permanente) {
            if (activo) {
                if (tiempo_contador > 0) {
                    if (tiempo_contador - Reloj.TIME_REGULAR_SPEED * delta <= 0) {
                        aparece_nuevo_evento(j);
                    } else {
                        tiempo_contador -= Reloj.TIME_REGULAR_SPEED * delta;
                    }
                }
            }
        }
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.black);
        if (!desactivacion_permanente) {
            if (activo) {
                g.drawString(Reloj.tiempo_a_string(tiempo_contador), VentanaCombate.playerX + VentanaCombate.VIEWPORT_SIZE_WIDTH / 2, VentanaCombate.playerY + 5);
            }
        }
        for (int i = 0; i < contenido.size(); i++) {
            Evento e = contenido.get(i);
            e.x = VentanaCombate.playerX + VentanaCombate.VIEWPORT_SIZE_WIDTH / 2 + 100 + (e.sprite.getWidth() + 10) * i;
            e.y = VentanaCombate.playerY + 5;
            e.dibujar(g);
        }
    }

}
