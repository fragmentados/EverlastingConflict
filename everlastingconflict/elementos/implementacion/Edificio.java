 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos.implementacion;

import everlastingconflict.elementos.util.ElementosComunes;
import everlastingconflict.estados.Estado;
import everlastingconflict.estados.Estados;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.gestion.ProgressBar;
import everlastingconflict.mapas.MapaCampo;
import everlastingconflict.mapas.MapaPrincipal;
import everlastingconflict.mapas.Mensaje;
import everlastingconflict.razas.Eternium;
import everlastingconflict.razas.Fenix;
import everlastingconflict.razas.Raza;
import everlastingconflict.relojes.Reloj;
import everlastingconflict.elementos.ElementoComplejo;
import everlastingconflict.elementos.ElementoSimple;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elementos.ElementoAtacante;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import static everlastingconflict.razas.Eternium.MAX_UNIT_PER_QUEUE;

 /**
 *
 * @author Elías
 */
public class Edificio extends ElementoAtacante {

    public float reunion_x, reunion_y;
    public ProgressBar barra;
    public List<ElementoSimple> cola_construccion;
    public float recurso_int;
    //Atributo para CuartelFenix
    public String unidad_actual;
    //Atributos para Mando Central
    public Edificio edificio_construccion;
    public float ed_x, ed_y;
    //Atributo para el primarca
    public boolean mostrarAyudaFusion = false;
    public List<Integer> cantidad_produccion;
    public float radio_construccion;
    public boolean activo;

    //Valores estáticos
    public static final int tiempo_mineria = 10;
    public static final int tiempo_centro = 1000;
    public static final int recursos_centro = 50;
    public static int recursos_refineria = 70;
    //Valores estándares
    public static float vida_estandar = 1000;
    public static int defensa_estandar = 1;
    public static int vision_estandar = 800;
    public static int tiempo_estandar = 12;

    public final void initImages() {
        try {
            Integer contador = 1;
            List<Image> imageList = new ArrayList<>();
            try {
                do {
                    imageList.add(new Image("media/Edificios/" + nombre + contador + ".png"));
                    contador++;
                } while (contador < 3);
            } catch (Exception e) {
            }
            Image[] images = new Image[imageList.size()];
            imageList.toArray(images);
            sprite = new Animation(images, 450, false);
            icono = new Image("media/Iconos/" + nombre + ".png");
            miniatura = new Image("media/Miniaturas/Prueba.png");
        } catch (SlickException e) {

        }
    }

    public Edificio(String n) {
        this.nombre = n;
        initImages();
        this.estado = "Parado";
        this.descripcion = "";
        this.estados = new Estados();
        Raza.edificio(this);
        vida = vida_max;
        this.botones = new ArrayList<>();
        cola_construccion = new ArrayList<>();
        if (nombre.equals("Mando Central")) {
            cantidad_produccion = new ArrayList<>();
        }
        this.anchura = this.sprite.getWidth();
        this.altura = this.sprite.getHeight();
        this.anchura_barra_vida = this.anchura;
        this.altura_barra_vida = 5;
        recurso_int = Edificio.tiempo_mineria;
        barra = new ProgressBar(this);
        this.reunion_x = this.x;
        this.reunion_y = this.y + this.altura + this.altura_barra_vida + barra.altura;
        experiencia_al_morir = 50;
    }

    public Edificio(String n, float x, float y) {
        this(n);
        cambiar_coordenadas(x, y);
    }

    public Edificio(Edificio e) {
        this(e.nombre, e.x, e.y);
    }

    public final void cambiar_coordenadas(float x, float y) {
        this.x = x;
        this.y = y;
        barra = new ProgressBar(this);
        this.reunion_x = this.x;
        this.reunion_y = this.y + this.altura + this.altura_barra_vida + barra.altura;
    }

    public void investigar_tecnologia(Partida p, Jugador j, Tecnologia t) {
        if (j.comprobacion_recursos(t)) {
            for (BotonComplejo b : botones) {
                if (b.elemento_nombre.equals(t.nombre)) {
                    b.activado = false;
                    break;
                }
            }
            if (cola_construccion.isEmpty()) {
                barra.activar(t.tiempo);
            }
            cola_construccion.add(t);
        }
    }

    public int obtener_indice_elemento(String n) {
        //n representa el nombre de la unidad cuyo indice se quiere obtener
        for (int i = 0; i < cola_construccion.size(); i++) {
            if (cola_construccion.get(i).nombre.equals(n) && cantidad_produccion.get(i) < Eternium.MAX_UNIT_PER_QUEUE) {
                return i;
            }
        }
        return -1;
    }

    public Point2D obtener_punto_salida() {
        float x_contador, y_contador;
        if (reunion_x > x) {
            x_contador = x + (anchura / 2) * (reunion_x / (x + anchura / 2));
        } else {
            if (reunion_x < x) {
                x_contador = x - (anchura / 2) * (reunion_x / (x - anchura / 2));
            } else {
                x_contador = x;
            }
        }
        if (reunion_y > y) {
            y_contador = y + (altura / 2) * (reunion_y / (y + altura / 2));
        } else {
            if (reunion_y < y) {
                y_contador = y - (altura / 2) * (reunion_y / (y - altura / 2));
            } else {
                y_contador = y;
            }
        }
        return new Point2D.Float(x_contador, y_contador);
    }

    public void cancelar_produccion(Partida p, ElementoSimple e) {
        if (e instanceof Unidad && cantidad_produccion != null && cantidad_produccion.get(obtener_indice_elemento(e.nombre)) > 1) {
            //El edificio puede crear varias unidades de vez
            int indice = obtener_indice_elemento(e.nombre);
            int contador = cantidad_produccion.get(indice).intValue();
            contador--;
            cantidad_produccion.set(indice, new Integer(contador));
        } else {
            if (e instanceof Tecnologia) {
                //Reactivar Botón
                for (BotonComplejo b : botones) {
                    if (b.elemento_nombre.equals(e.nombre)) {
                        b.activado = true;
                        break;
                    }
                }
            }
            eliminar_cola(e);
        }
        if (!p.jugador_aliado(this).raza.equals(Fenix.nombre_raza)) {
            p.jugador_aliado(this).recursos += e.coste;
        }
    }

    public void crear_unidad(Partida partida, Jugador jugador, Unidad unidadACrear) {
        if (jugador.comprobacion_recursos(unidadACrear)) {
            if ((jugador.poblacion + unidadACrear.coste_poblacion) <= jugador.poblacion_max) {
                if (!jugador.raza.equals(Fenix.nombre_raza) || !unidadACrear.constructor || (jugador.cantidad_no_militar() < Fenix.limite_unidades_no_militares)) {
                    unidadACrear.x = this.x;
                    unidadACrear.y = this.y;
                    jugador.poblacion += unidadACrear.coste_poblacion;
                    if (cola_construccion.isEmpty()) {
                        barra.activar(unidadACrear.tiempo);
                    }
                    if (this.nombre.equals("Mando Central")) {
                        mandoCentralCrearUnidad(jugador, unidadACrear);
                    } else {
                        //Añadir la unidad a la cola
                        cola_construccion.add(unidadACrear);
                    }
                    jugador.recursos -= unidadACrear.coste;
                }
            }
        }
    }

    private void mandoCentralCrearUnidad(Jugador jugador, Unidad unidadACrear) {
        int indice = obtener_indice_elemento(unidadACrear.nombre);
        if (indice == -1) {
            cola_construccion.add(unidadACrear);
            cantidad_produccion.add(new Integer(1));
        } else {
            cantidad_produccion.set(indice, cantidad_produccion.get(indice) + 1);
        }
    }

    public void detener_produccion() {
        barra.desactivar();
        cola_construccion = new ArrayList<>();
    }

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

    public void eliminar_cola(ElementoSimple e) {
        if (e == cola_construccion.get(0)) {
            if (cola_construccion.size() != 1) {
                barra.activar(cola_construccion.get(0).tiempo);
            }
        }
        cola_construccion.remove(e);
        if (cola_construccion.isEmpty()) {
            barra.progreso = 0;
            barra.desactivar();
        }
    }

    public void comprobar_barra(Partida p, Jugador j) {
        if (barra.terminado()) {
            if (cola_construccion.size() > 0) {
                //System.out.println("Unidad creada");
                if (cola_construccion.get(0) instanceof Unidad) {
                    if (this.nombre.equals("Mando Central")) {
                        for (int i = 0; i < cantidad_produccion.get(0); i++) {
                            Unidad unidad = (Unidad) cola_construccion.get(0);
                            Unidad u = new Unidad(unidad);
                            u.x = unidad.x;
                            u.y = unidad.y;
                            j.unidades.add(u);
                            u.iniciarbotones(p);
                            Point2D.Float contador = obtener_coordenadas(p, u);
                            u.anadir_movimiento(contador.x + i * (unidad.anchura + 10), contador.y);
                        }
                        cantidad_produccion.remove(0);
                    } else {
                        Unidad u = (Unidad) cola_construccion.get(0);
                        j.unidades.add(u);
                        u.iniciarbotones(p);
                        Point2D.Float contador = obtener_coordenadas(p, u);
                        u.anadir_movimiento(contador.x, contador.y);

                    }
                } else {
                    Tecnologia t = (Tecnologia) cola_construccion.get(0);
                    t.resolver_efecto(p, j);
                    for (BotonComplejo b : botones) {
                        if (b.elemento_nombre.equals(t.nombre)) {
                            botones.remove(b);
                            break;
                        }
                    }
                }
                eliminar_cola(cola_construccion.get(0));
            }
        }
    }

    public void dibujar_fin_movimiento(Graphics g) {
        g.setColor(Color.green);
        g.drawOval(reunion_x - 10, reunion_y - 10, 20, 20);
        g.drawLine(this.x, this.y, reunion_x, reunion_y);
        g.setColor(Color.white);
    }

    @Override
    public void dibujar(Partida p, Color c, Input input, Graphics g) {
        this.chechAnimationStatus(p);
        super.dibujar(p, c, input, g);
        barra.dibujar(g);
        if (MapaCampo.iu.elementos.indexOf(this) != -1) {
            dibujar_fin_movimiento(g);
        }
    }

    private void chechAnimationStatus(Partida p) {
        // Cant be under construction
        if (!"Construyendose".equals(estado)) {
            // Eternium collecting buildings only animate when they all work
            if (this.nombre.equals("Cámara de asimilación") || this.nombre.equals("Teletransportador") || this.nombre.equals("Refinería")) {
                this.sprite.setAutoUpdate(p.jugador_aliado(this).perforacion);
            } else {
                // Other buildings animate when they are creating something
                this.sprite.setAutoUpdate(barra.isActive());
            }
        }
    }

    @Override
    public void destruir(Partida p, ElementoAtacante atacante) {
        if (this.seleccionada()) {
            this.deseleccionar();
        }
        p.jugador_aliado(this).edificios.remove(this);
        if (p.jugador_aliado(this).raza.equals(Eternium.nombre_raza)) {
            p.jugador_aliado(this).comprobacion_perforacion();
        }
        if (atacante instanceof Manipulador) {
            Manipulador m = (Manipulador) atacante;
            m.aumentar_experiencia(experiencia_al_morir);
        }
        ElementosComunes.BUILDING_DEATH_SOUND.playAt(1f, 1f, x, y, 0f);
    }

    @Override
    public void construir(Partida p, Edificio edificio, float x, float y) {
        super.construir(p, edificio, x, y);
        estado = "Construyendo";
        p.jugador_aliado(this).resta_recursos(edificio.coste);
        edificio.estado = "Construyendose";
        edificio_construccion = edificio;
        edificio_construccion.cambiar_coordenadas(x, y);
    }

    @Override
    public void comportamiento(Partida p, Graphics g, int delta) {
        super.comportamiento(p, g, delta);
        Jugador aliado = p.jugador_aliado(this);
        Jugador enemigo = p.jugador_enemigo(this);
        if (nombre.equals("Estación reparadora")) {
            for (Unidad u : aliado.unidades) {
                if (u.vehiculo) {
                    if (this.alcance(200, u)) {
                        u.aumentar_vida(Reloj.velocidad_reloj * delta * 5);
                    }
                }
            }
        }
        if (nombre.equals("Distorsionador temporal")) {
            for (Unidad u : enemigo.unidades) {
                if (this.alcance(200, u)) {
                    u.estados.anadir_estado(new Estado(Estado.nombre_ralentizacion, 1f, 40f));
                }
            }
        }
        if (constructor && estado.equals("Construyendo")) {
            if (edificio_construccion.vida == 0) {
                aliado.edificios.add(edificio_construccion);
            }
            if (edificio_construccion.vida < edificio_construccion.vida_max) {
                //Continúa la construcción
                edificio_construccion.vida += (edificio_construccion.vida_max / edificio_construccion.tiempo) * Reloj.velocidad_reloj * delta;
            } else {
                //Acaba la construcción
                edificio_construccion.vida = edificio_construccion.vida_max;
                edificio_construccion.iniciarbotones(p);
                edificio_construccion.estado = "Parado";
                estado = "Parado";
                edificio_construccion = null;
                aliado.comprobacion_perforacion();
                this.enableBuildingButtons();
            }
        }
        switch (nombre) {
            case "Cuartel Fénix":
                if (estado.equals("Construyendo")) {
                    if (unidad_actual != null) {
                        if (!barra.isActive()) {
                            this.crear_unidad(p, aliado, new Unidad(unidad_actual));
                        }
                    }
                }
                break;
            case "Refinería":
                if (aliado.perforacion && !"Construyendose".equals(estado)) {
                    if (recurso_int > 0) {
                        if (recurso_int - Reloj.velocidad_reloj * delta <= 0) {
                            recurso_int = Edificio.tiempo_mineria;
                            aliado.recursos += Edificio.recursos_refineria;
                            MapaPrincipal.mapac.anadir_mensaje(new Mensaje("+" + Edificio.recursos_refineria, Color.green, x, y - altura / 2 - 20, 2f));
                        } else {
                            recurso_int -= Reloj.velocidad_reloj * delta;
                        }
                    }
                }
                break;
            case "Centro de restauración":
                if (recurso_int < Edificio.tiempo_centro) {
                    recurso_int += delta;
                } else {
                    recurso_int = 0;
                    aliado.recursos += Edificio.recursos_centro;
                    for (int i = 0; i < p.recursos.size(); i++) {
                        Recurso r = p.recursos.get(i);
                        if (r.x == x && r.y == y) {
                            p.recursos.remove(r);
                            break;
                        }
                    }
                    this.destruir(p, null);
                }
                break;
        }
        for (BotonComplejo b : botones) {
            b.comportamiento(delta);
        }
    }

    public List<Rectangle2D> obtener_intersecciones(Partida partida, Input input) {
        List<Rectangle2D> intersecciones = new ArrayList<>();
        int anchura_contador = this.sprite.getWidth();
        int altura_contador = this.sprite.getHeight();
        float posx, posy;
        posx = MapaCampo.playerX + input.getMouseX() - anchura_contador / 2;
        posy = MapaCampo.playerY + input.getMouseY() - altura_contador / 2;
        Rectangle re = new Rectangle((int) posx, (int) posy, anchura_contador, altura_contador);
        if ((nombre.equals("Refinería")) || (nombre.equals("Centro de restauración"))) {
            for (int i = 0; i < partida.recursos.size(); i++) {
                Recurso r = partida.recursos.get(i);
                if (r.nombre.equals("Hierro")) {
                    Rectangle2D r2 = new Rectangle2D.Float(r.x - r.anchura / 2, r.y - r.altura / 2, r.anchura, r.altura);
                    if (r2.intersects(re)) {
                        Jugador aliado = partida.jugador_aliado(MapaPrincipal.mapac.constructor);
                        for (Edificio e : aliado.edificios) {
                            if (e.nombre.equals("Refinería") && e.hitbox(r.x, r.y)) {
                                return new ArrayList<>();
                            }
                        }
                        Rectangle2D r3 = r2.createIntersection(re);
                        intersecciones.add(r3);
                    }
                }
            }
        } else {
            for (int i = 0; i < partida.j1.unidades.size(); i++) {
                Unidad u = partida.j1.unidades.get(i);
                Rectangle2D r = new Rectangle2D.Float(u.x - u.anchura / 2, u.y - u.altura / 2, u.anchura, u.altura);
                if (r.intersects(re)) {
                    Rectangle2D r2 = r.createIntersection(re);
                    intersecciones.add(r2);
                }
            }
            for (int i = 0; i < partida.j2.unidades.size(); i++) {
                Unidad u = partida.j2.unidades.get(i);
                Rectangle2D r = new Rectangle2D.Float(u.x - u.anchura / 2, u.y - u.altura / 2, u.anchura, u.altura);
                if (r.intersects(re)) {
                    Rectangle2D r2 = r.createIntersection(re);
                    intersecciones.add(r2);
                }
            }
            for (int i = 0; i < partida.j1.edificios.size(); i++) {
                Edificio u = partida.j1.edificios.get(i);
                Rectangle2D r = new Rectangle2D.Float(u.x - u.anchura / 2, u.y - u.altura / 2, u.anchura, u.altura);
                if (r.intersects(re)) {
                    Rectangle2D r2 = r.createIntersection(re);
                    intersecciones.add(r2);
                }
            }
            for (int i = 0; i < partida.j2.edificios.size(); i++) {
                Edificio u = partida.j2.edificios.get(i);
                Rectangle2D r = new Rectangle2D.Float(u.x - u.anchura / 2, u.y - u.altura / 2, u.anchura, u.altura);
                if (r.intersects(re)) {
                    Rectangle2D r2 = r.createIntersection(re);
                    intersecciones.add(r2);
                }
            }
            for (int i = 0; i < partida.recursos.size(); i++) {
                Recurso r = partida.recursos.get(i);
                Rectangle2D r2 = new Rectangle2D.Float(r.x - r.anchura / 2, r.y - r.altura / 2, r.anchura, r.altura);
                if (r2.intersects(re)) {
                    Rectangle2D r3 = r2.createIntersection(re);
                    intersecciones.add(r3);
                }
            }
        }
        return intersecciones;
    }

    public boolean construible(ElementoComplejo constructor, Partida p, Input input, int x, int y) {
        if (constructor instanceof Edificio) {
            Ellipse2D circulo = new Ellipse2D.Float(constructor.x - ((Edificio) constructor).radio_construccion / 2, constructor.y - ((Edificio) constructor).radio_construccion / 2, ((Edificio) constructor).radio_construccion, ((Edificio) constructor).radio_construccion);
            int anchura_contador = this.sprite.getWidth();
            int altura_contador = this.sprite.getHeight();
            float posx = MapaCampo.playerX + input.getMouseX() - anchura_contador / 2;
            float posy = MapaCampo.playerY + input.getMouseY() - altura_contador / 2;
            Rectangle re = new Rectangle((int) posx, (int) posy, anchura_contador, altura_contador);
            if (!circulo.contains(re)) {
                return false;
            }
        }
        List<Rectangle2D> intersecciones = obtener_intersecciones(p, input);
        if ((nombre.equals("Refinería")) || (nombre.equals("Centro de restauración"))) {
            return !intersecciones.isEmpty();
        } else {
            return intersecciones.isEmpty();
        }
    }
}
