/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos.implementacion;

import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Elías
 */
public class Taller extends Edificio {

    public static final String TALLER_NOMBRE = "Taller bélico";

    public List<Unidad> unidades_creadas;
    public List<Edificio> anexos;
    public float maximo_anexos;

    public Taller(String n) {
        super(n);
        unidades_creadas = new ArrayList<>();
        anexos = new ArrayList<>();

    }

    public Taller(String n, float x, float y) {
        this(n);
        this.cambiar_coordenadas(x, y);
    }

    @Override
    public void createUnit(Partida partida, Jugador jugador, Unidad unidadACrear) {
        if(jugador.comprobacion_recursos(unidadACrear)){
            if ((jugador.poblacion + unidadACrear.coste_poblacion) <= jugador.poblacion_max) {
                if (unidades_creadas.size() < anexos.size()) {
                    //Cálculo de coordenadas iniciales de la unidad                
//                    Point2D resultado = obtener_punto_salida();
//                    u.x = (int) resultado.getX();
//                    u.y = (int) resultado.getY();
                    unidadACrear.x = this.x;
                    unidadACrear.y = this.y;
                    //Añadir la unidad a la cola                                        
                    jugador.poblacion += unidadACrear.coste_poblacion;
                    if (cola_construccion.isEmpty()) {
                        barra.activar(unidadACrear.tiempo);
                    }
                    cola_construccion.add(unidadACrear);
                }
            }
        }
    }

    @Override
    public Point2D.Float obtener_coordenadas(Partida p, Unidad u) {
        Point2D.Float resultado = new Point2D.Float(0, 0);
        float contador_x = reunion_x, contador_y = reunion_y;
        int i = 0;
        while (!u.colision(p, contador_x, contador_y).nombre.equals("No hay")) {
            i++;
            if (i == 10) {
                contador_x = reunion_x;
                contador_y += u.altura + 10;
                i = 0;
            } else {
                contador_x += u.anchura + 10;
            }

        }
        resultado.x = contador_x;
        resultado.y = contador_y;
        return resultado;
    }

    public void mover_unidades_creadas(Partida p) {
        for (Unidad u : unidades_creadas) {
            u.anadir_movimiento(u.x - anexos.get(0).anchura, u.y);
        }
    }

    @Override
    public void comprobar_barra(Partida p, Jugador j) {
        if (barra.terminado()) {
            if (cola_construccion.size() > 0) {
                //System.out.println("Unidad creada");
                if (cola_construccion.get(0) instanceof Unidad) {
                    mover_unidades_creadas(p);
                    Unidad u = (Unidad) cola_construccion.get(0);
                    j.unidades.add(u);
                    unidades_creadas.add(u);
                    u.iniciarbotones(p);
                    Point2D.Float contador = new Point2D.Float(anexos.get(0).x, anexos.get(0).y);
                    u.anadir_movimiento(contador.x, contador.y);
                }
                eliminar_cola(cola_construccion.get(0));
            }
        }
    }

    public void construir_anexo(Partida p) {
        if (this.anexos.size() < maximo_anexos) {
            Edificio edificio = new Edificio("Anexo");
            edificio.vida = 0;
            float x_contador = this.x - this.anchura / 2 - edificio.anchura / 2;
            for (Edificio ed : anexos) {
                x_contador -= ed.anchura;
            }
            anexos.add(edificio);
            this.construir(p, edificio, x_contador, y);
        }
    }

}
