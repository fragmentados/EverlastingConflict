/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElementosSlick2D;

import RTS.elementos.ElementoAtacante;
import RTS.gestion.Partida;
import RTS.elementos.implementacion.Edificio;
import RTS.elementos.ElementoComplejo;
import RTS.elementos.ElementoSimple;
import RTS.elementos.implementacion.Habilidad;
import RTS.elementos.implementacion.Manipulador;
import RTS.elementos.implementacion.Taller;
import RTS.elementos.implementacion.Tecnologia;
import RTS.elementos.implementacion.Unidad;
import RTS.estados.Estado;
import RTS.gestion.Evento;
import RTS.gestion.Jugador;
import RTS.mapas.MapaCampo;
import RTS.mapas.Mensaje;

import static RTS.elementos.implementacion.Taller.TALLER_NOMBRE;
import static RTS.mapas.MapaCampo.VIEWPORT_SIZE_Y;
import static RTS.mapas.MapaCampo.playerY;
import RTS.mapas.MapaEjemplo;
import RTS.razas.Clark;
import RTS.razas.Fenix;
import RTS.razas.Fusion;
import RTS.relojes.Reloj;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Elías
 */
public class BotonComplejo extends BotonSimple {

    public String elemento_nombre, elemento_tipo;
    public float elemento_coste;
    public String descripcion;
    public int tecla;
    public String tecla_string;
    public float cooldown, cooldown_contador;
    private float resolucion_contador;
    private static final float resolucion = 0.2f;

    public BotonComplejo(String t) {
        //Boton que posee sólo texto
        super(t);
        try {
            sprite = new Image("media/Iconos/" + t + ".png");
        } catch (SlickException ex) {
            Logger.getLogger(BotonComplejo.class.getName()).log(Level.SEVERE, null, ex);
        }
        altura = sprite.getWidth();
        anchura = sprite.getHeight();
        obtener_descripcion(t);
    }

    public BotonComplejo(ElementoSimple e) {
        //Boton usado para crear Elementos
        elemento_nombre = e.nombre;
        elemento_coste = e.coste;
        if (e instanceof Edificio) {
            elemento_tipo = "Edificio";
        } else {
            if (e instanceof Unidad) {
                elemento_tipo = "Unidad";
            } else {
                if (e instanceof Tecnologia) {
                    elemento_tipo = "Tecnología";
                } else {
                    if (e instanceof Habilidad) {
                        elemento_tipo = "Habilidad";
                        Habilidad h = (Habilidad) e;
                        cooldown = h.cooldown;
                    } else {
                        if (e instanceof Evento) {
                            elemento_tipo = "Evento";
                        }
                    }
                }
            }
        }
        sprite = e.icono;
        altura = e.icono.getWidth();
        anchura = e.icono.getHeight();
        activado = true;
        descripcion = e.descripcion;
    }

    public final void obtener_descripcion(String t) {
        switch (t) {
            case "Lider de hordas":
                descripcion = "Las unidades que tus invocaciones destruyan generan experiencia para el Manipulador.";
                break;
            case "Exterminador de masas":
                descripcion = "Aumenta el ataque y la velocidad de ataque.";
                break;
            case "Sabiduría arcana":
                descripcion = "Aumenta el maná maximo y la regeneración de maná.";
                break;
            case "Cauterización automática":
                descripcion = "El Manipulador obtiene regeneración de vida por segundo.";
                break;
            case "Entrenamiento avanzado":
                descripcion = "Mejora las estadísticas de las invocaciones.";
                break;
            case "Tirador experto":
                descripcion = "Aumenta el alcance y el daño de ataque.";
                break;
            case "Poderío astral":
                descripcion = "Aumenta el daño de tus hechizos.";
                break;
            case "Protección mística":
                descripcion = "Aumenta la vida y la defensa.";
                break;
            case "Invocación eficiente":
                descripcion = "Aumenta el número de unidades que generan tus invocaciones.";
                break;
            case "Incineración ígnea":
                descripcion = "El ataque pasa a ser de área.";
                break;
            case "Control temporal":
                descripcion = "Reducción del mínimo enfriamiento.";
                break;
            case "Entrenamiento supremo":
                descripcion = "Desbloquea las habilidades de tus invocaciones.";
                break;
            case "Reinversión física":
                descripcion = "Un porcentaje del daño causado por ataques se restaura en vida.";
                break;
            case "Reinversión mágica":
                descripcion = "Un porcentaje del daño causado por hechizos se restaura en vida.";
                break;
            case "Inspiración":
                descripcion = "Las invocaciones cercanas al Manipulador aumentan su ataque.";
                break;
            case "Disparo helado":
                descripcion = "Tus ataques ralentizan al objetivo.";
                break;
            case "Eficiencia energética":
                descripcion = "Tus hechizos ven reducido su coste de maná.";
                break;
            case "Fuerzas mixtas":
                descripcion = "Cada habilidad usada potencia tu siguiente ataque.";
                break;
            case "Último recurso":
                descripcion = "Cuando tu vida baja de un porcentaje, tu defensa aumenta drásticamente.";
                break;
            case "Clon":
                descripcion = "Se crea una copia del Manipulador pero se reducen las estadísticas de ambos a la mitad.";
                break;
            case "Tigre":
                descripcion = "El cuartel creará de forma constante Tigres, la unidad básica.\n";
                descripcion += "Tiene capacidades militares básicas pero es rápido de crear y peligroso en número.";
                break;
            case "Halcón":
                descripcion = "El cuartel creará de forma constante Halcones, unidad francotiradora.\n";
                descripcion += "Tiene un gran alcance de ataque y una gran velocidad pero muy baja supervivencia.";
                break;
            case "Fénix":
                descripcion = "El cuartel creará de forma constante Fenixes, la unidad definitiva.\n";
                descripcion += "Tiene una gran vida y ataque e incluso puede mejorarse para poder resucitar.";
                break;
        }
    }

    public boolean presionado(Input input) {
        if (input.isKeyPressed(tecla)) {
            return true;
        } else {
            if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                return presionado((int) MapaCampo.playerX + input.getMouseX(), (int) MapaCampo.playerY + input.getMouseY());
            } else {
                return false;
            }
        }
    }

    public static void dibujar_aguja(Graphics g, float x, float y, float valor, float valor_maximo) {
        //double angle = (cooldown_contador / cooldown) * (Math.PI / 2);
        float startX = x + 20;
        float startY = y + 20;
        float endX = (int) (Math.cos((valor / valor_maximo) * 3.14f * 2 - 3.14f / 2) * 20 + startX);
        float endY = (int) (Math.sin((valor / valor_maximo) * 3.14f * 2 - 3.14f / 2) * 20 + startY);
        //float endX = x + 20 * (float) Math.sin(angle);
        //float endY = y + 40 * (float) Math.cos(angle);
        g.drawLine(startX, startY - 1, endX, endY);
        g.drawLine(startX - 1, startY, endX, endY);
        //g.drawLine(startX, startY, endX, endY);
    }

    @Override
    public void dibujar(Graphics g) {
        g.setColor(Color.white);
        if (sprite != null) {
            sprite.draw(x, y);
        } else {
            if (texto != null) {
                g.drawString(texto, x, y);
            }
        }
        if (activado) {
            if (resolucion_contador > 0) {
                g.setColor(new Color(0f, 0.3f, 0.4f, 0.5f));
                //resolucion = false;
            } else {
                g.setColor(new Color(0f, 0.6f, 0.8f, 0.5f));
            }
        } else {
            g.setColor(new Color(0.3f, 0.3f, 0.3f, 0.7f));
        }
        g.drawRect(x, y, anchura, altura);
        g.fillRect(x, y, anchura, altura);
        g.setColor(Color.white);
        if (tecla_string != null) {
            g.drawString(tecla_string, x, y);
        }
        if (cooldown_contador > 0) {
            BotonComplejo.dibujar_aguja(g, this.x, this.y, this.cooldown_contador, this.cooldown);
        }
    }

    public void dibujar_extendido(Jugador aliado, Graphics g, ElementoComplejo origen) {
        //Origen solo es necesario para botones del Manipulador
        float xbotones = MapaCampo.playerX + 600;
        float ybotones = playerY + VIEWPORT_SIZE_Y;
        float anchura_contador = 565;
        float altura_contador = 200;
        float espacio_lineas = 20;
        float xcontador = xbotones + 1;
        float ycontador = ybotones - altura_contador;
        g.setColor(MapaCampo.iu.color);
        g.fillRect(xbotones, ybotones - altura_contador, anchura_contador, altura_contador);
        g.setColor(Color.white);
        g.drawRect(xbotones, ybotones - altura_contador, anchura_contador, altura_contador);
        if (texto != null) {
            g.drawString(texto, xcontador, ycontador);
            ycontador += espacio_lineas;
        } else {
            if (elemento_nombre != null) {
                ElementoSimple elemento;
                switch (elemento_tipo) {
                    case "Unidad":
                        elemento = new Unidad(elemento_nombre);
                        break;
                    case "Edificio":
                        if (TALLER_NOMBRE.equals(elemento_nombre)) {
                            elemento = new Taller(elemento_nombre);
                        } else {
                            elemento = new Edificio(elemento_nombre);
                        }
                        break;
                    case "Tecnología":
                        elemento = new Tecnologia(elemento_nombre);
                        break;
                    case "Habilidad":
                        elemento = new Habilidad(elemento_nombre);
                        break;
                    case "Evento":
                        elemento = new Evento(elemento_nombre);
                        break;
                    default:
                        elemento = null;
                        break;
                }
                if (elemento != null) {
                    if (tecla_string != null) {
                        g.drawString(elemento.nombre + " (" + tecla_string + ")", xcontador, ycontador);
                    } else {
                        g.drawString(elemento.nombre + " (Sin tecla)", xcontador, ycontador);
                    }
                    ycontador += espacio_lineas;
                    if (elemento.coste > 0) {
                        g.drawString("Coste: " + Integer.toString(elemento.coste), xcontador, ycontador);
                        xcontador += ("Coste: " + Integer.toString(elemento.coste)).length() * 10;
                    } else {
                        g.drawString("Sin coste", xcontador, ycontador);
                        xcontador += "Sin coste".length() * 10;
                    }
                    if (elemento.coste_alternativo > 0) {
                        g.drawString("Nivel: " + elemento.coste_alternativo, xcontador, ycontador);
                        xcontador += ("Nivel: " + elemento.coste_alternativo).length() * 10;
                    }
                    if (cooldown > 0) {
                        float contador;
                        if (origen instanceof Manipulador) {
                            Manipulador m = (Manipulador) origen;
                            contador = m.obtener_cooldown_reducido(cooldown);
                        } else {
                            contador = cooldown;
                        }
                        g.drawString("Cooldown: " + Float.toString(contador), xcontador, ycontador);
                        xcontador += ("Cooldown: " + Float.toString(contador)).length() * 10;
                    }
                    if (elemento instanceof Evento) {
                        Evento evento = (Evento) elemento;
                        if (evento.positivo) {
                            g.drawString("Beneficio: +" + Integer.toString(evento.efecto) + "%", xcontador, ycontador);
                        } else {
                            g.drawString("Efecto: -" + Integer.toString(evento.efecto) + "%", xcontador, ycontador);
                        }
                        xcontador = xbotones + 1;
                        ycontador += espacio_lineas;
                        g.drawString("Tropas necesarias: " + Integer.toString(evento.cantidad_elemento) + " " + evento.nombre_elemento, xcontador, ycontador);
                        xcontador = xbotones + 1;
                        ycontador += espacio_lineas;
                        if (!evento.positivo) {
                            for (Evento e : aliado.eventos.contenido) {
                                if (e.nombre.equals(evento.nombre)) {
                                    g.drawString("Tiempo restante: " + Reloj.tiempo_a_string(e.tiempo_contador), xcontador, ycontador);
                                    break;
                                }
                            }
                        }
                    }
                    if (elemento instanceof Unidad) {
                        g.drawString("Población: " + Integer.toString(((Unidad) elemento).coste_poblacion), xcontador, ycontador);
                    }
                    xcontador = xbotones + 1;
                    ycontador += espacio_lineas;
                    if (elemento.tiempo > 0) {
                        g.drawString("Tiempo: " + Integer.toString(elemento.tiempo), xcontador, ycontador);
                    }
                    ycontador += espacio_lineas;
                }
            }
        }
        if (descripcion != null) {
            g.drawString(Partida.anadir_saltos_de_linea(descripcion, anchura_contador), xcontador, ycontador);
        }
    }

    public void activar_cooldown(ElementoComplejo e) {
        if (cooldown > 0) {
            if (e instanceof Manipulador) {
                Manipulador m = (Manipulador) e;
                cooldown_contador = m.obtener_cooldown_reducido(cooldown);
            } else {
                cooldown_contador = cooldown;
            }
            activado = false;
        }
    }

    public void resolucion(List<ElementoComplejo> el, ElementoComplejo e, Partida partida) {
        //e representa el elemento desde el cual se activa el botón          
        Jugador aliado = partida.jugador_aliado(e);
        this.resolucion_contador = BotonComplejo.resolucion;
        if (activado) {
            if (e instanceof Edificio) {
                Edificio edificio = (Edificio) e;
                if (texto == null) {
                    if (this.elemento_tipo.equals("Unidad")) {
                        //Crear Unidad  
                        edificio.crear_unidad(partida, aliado, new Unidad(this.elemento_nombre));
                    } else {
                        if (this.elemento_tipo.equals("Edificio")) {
                            //Construir Edificio
                            if (!edificio.estado.equals("Construyendo")) {
                                Edificio contador_edificio = new Edificio(this.elemento_nombre);
                                if (edificio.constructor) {
                                    if (aliado.comprobacion_recursos(contador_edificio)) {
                                        if (edificio instanceof Taller) {
                                            if (elemento_nombre.equals("Anexo")) {
                                                Taller t = (Taller) edificio;
                                                t.construir_anexo(partida);
                                            }
                                        } else {
                                            MapaEjemplo.mapac.edificio = contador_edificio;
                                            MapaEjemplo.mapac.edificio.vida = 0;
                                            MapaEjemplo.mapac.constructor = (ElementoAtacante) e;
                                        }
                                    }
                                }
                            }
                        } else {
                            if (this.elemento_tipo.equals("Tecnología")) {
                                //Investigar Tecnología
                                edificio.investigar_tecnologia(partida, aliado, new Tecnologia(this.elemento_nombre));
                            } else {
                                //Desbloquear Evento Positivo
                                Evento evento = new Evento(this.elemento_nombre);
                                if (aliado.comprobacion_recursos(evento)) {
                                    aliado.eventos.anadir_evento(aliado, evento);
                                    evento.efecto(aliado);
                                    edificio.botones.remove(this);
                                }
                            }
                        }
                    }
                } else {
                    switch (texto) {
                        case "Tigre":
                        case "Halcón":
                        case "Fénix":
                            edificio.unidad_actual = texto;
                            break;
                        case "Reanudar":
                            texto = "Detener";
                            try {
                                sprite = new Image("media/Iconos/Detener.png");
                            } catch (SlickException ex) {
                                Logger.getLogger(BotonComplejo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            edificio.estado = "Construyendo";
                            break;
                        case "Detener":
                            texto = "Reanudar";
                            try {
                                sprite = new Image("media/Iconos/Reanudar.png");
                            } catch (SlickException ex) {
                                Logger.getLogger(BotonComplejo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            edificio.detener_produccion();
                            edificio.estado = "Detenido";
                            break;
                    }
                }
            } else {
                Unidad unidad = (Unidad) e;
                if (texto == null) {
                    if (this.elemento_tipo.equals("Unidad")) {
                        //Convertirse en unidad.
                        //e = new Unidad(this.elemento_nombre);
                        unidad.destruir(partida, null);
                        Unidad nueva = new Unidad(this.elemento_nombre, unidad.x, unidad.y);
                        aliado.unidades.add(nueva);
                        nueva.seleccionar();
                    } else {
                        if (this.elemento_tipo.equals("Edificio")) {
                            //Construir Edificio                                    
                            for (ElementoSimple e2 : el) {
                                if (e2 instanceof Unidad) {
                                    Unidad contador = (Unidad) e2;
                                    if (contador.constructor) {
                                        if (!contador.estado.equals("Construyendo")) {
                                            if (!contador.estados.existe_estado(Estado.nombre_amnesia)) {
                                                Edificio contador_edificio = new Edificio(this.elemento_nombre);
                                                if (!contador_edificio.nombre.equals("Cuartel Fénix") || (aliado.cantidad_elemento(contador_edificio) < Fenix.limite_cuarteles)) {
                                                    if (partida.jugador_aliado(e).comprobacion_recursos(contador_edificio)) {
                                                        MapaEjemplo.mapac.edificio = contador_edificio;
                                                        MapaEjemplo.mapac.edificio.vida = 0;
                                                        MapaEjemplo.mapac.constructor = contador;
                                                        break;
                                                    }
                                                } else {
                                                    MapaEjemplo.mapac.anadir_mensaje(new Mensaje("Debes aumentar el límite de Cuarteles"));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (this.elemento_tipo.equals("Habilidad")) {
                                //Activar habilidad
                                if (unidad.puede_usar_habilidad()) {
                                    new Habilidad(this.elemento_nombre).activacion(partida, unidad);
                                }
                            }
                        }
                    }
                } else {
                    switch (texto) {
                        case "Fusion":
                            List<Unidad> contador = new ArrayList<>();
                            for (ElementoSimple e2 : el) {
                                if (e2.nombre.equals("Depredador") || e2.nombre.equals("Devorador") || e2.nombre.equals("Cazador")) {
                                    contador.add((Unidad) e2);
                                }
                            }
                            Clark.fusion_a(partida, new Fusion(contador));
                            break;
                        case "Detener":
                            unidad.parar();
                            break;
                    }
                }
            }
        }
    }

    public void comportamiento(int delta) {
        if (cooldown > 0) {
            if (cooldown_contador > 0) {
                if ((cooldown_contador - (Reloj.velocidad_reloj * delta) <= 0)) {
                    cooldown_contador = 0;
                    activado = true;
                } else {
                    cooldown_contador -= Reloj.velocidad_reloj * delta;
                }
            }
        }
        if (resolucion_contador > 0) {
            if ((resolucion_contador - (Reloj.velocidad_reloj * delta) <= 0)) {
                resolucion_contador = 0;
            } else {
                resolucion_contador -= Reloj.velocidad_reloj * delta;
            }
        }
    }
}
