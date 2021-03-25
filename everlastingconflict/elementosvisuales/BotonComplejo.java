/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementosvisuales;

import everlastingconflict.behaviour.BehaviourEnum;
import everlastingconflict.elements.ElementoAtacante;
import everlastingconflict.elements.ElementoComplejo;
import everlastingconflict.elements.ElementoSimple;
import everlastingconflict.elements.impl.*;
import everlastingconflict.gestion.Evento;
import everlastingconflict.gestion.Game;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.races.Clark;
import everlastingconflict.races.Fusion;
import everlastingconflict.races.enums.RaceEnum;
import everlastingconflict.status.StatusName;
import everlastingconflict.watches.Reloj;
import everlastingconflict.windows.Mensaje;
import everlastingconflict.windows.UI;
import everlastingconflict.windows.WindowCombat;
import everlastingconflict.windows.WindowMain;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static everlastingconflict.elements.impl.Taller.TALLER_NOMBRE;
import static everlastingconflict.elements.util.ElementosComunes.UI_COLOR;
import static everlastingconflict.windows.WindowCombat.VIEWPORT_SIZE_HEIGHT;
import static everlastingconflict.windows.WindowCombat.playerY;


public class BotonComplejo extends BotonSimple {

    public String elemento_nombre, elemento_tipo;
    public float elemento_coste;
    public int elementThreatLevelNeeded;
    public String descripcion;
    public int tecla;
    public String tecla_string;
    public float cooldown, cooldown_contador;
    public Integer remainingClicks = null;
    public Integer timesClicked = 0;
    private float resolucion_contador;
    private static final float resolucion = 0.2f;
    public boolean canBeShown = true;
    public boolean isPassiveAbility = false;


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
        elementThreatLevelNeeded = e.guardiansThreatLevelNeeded;
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
            case "Oso":
                descripcion = "El cuartel creará de forma constante Osos.\n";
                descripcion += "Tiene una gran ataque cuerpo a cuerpo y velocidad.";
                break;
            case "Cuervo":
                descripcion = "El cuartel creará de forma constante Fenixes, la unidad de apoyo.\n";
                descripcion += "No tiene capacidad ofensiva pero tiene unas habilidades poderosas.";
                break;
            case "Tortuga":
                descripcion = "El cuartel creará de forma constante Fenixes, la unidad defensiva.\n";
                descripcion += "Tiene una gran vida y defensa y absorbe automáticamente los ataques enemigos.";
                break;
            case "Ayuda Fusión":
                descripcion = "Muestra una guía informativa de qué combinaciones producen qué unidades en la fusión.\n";
                break;
            case "Ayuda piloto":
                descripcion = "Muestra una guía informativa de qué combinaciones producen qué torretas en la embarcación.\n";
                break;
        }
    }

    public boolean presionado(Input input) {
        if (input.isKeyPressed(tecla)) {
            return true;
        } else {
            if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                return isHovered((int) WindowCombat.playerX + input.getMouseX(), (int) WindowCombat.playerY + input.getMouseY());
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
    public void render(Graphics g) {
        if (canBeShown) {
            g.setColor(Color.white);
            if (sprite != null) {
                sprite.draw(x, y);
            } else {
                if (texto != null) {
                    g.drawString(texto, x, y);
                }
            }
            if (canBeUsed) {
                if (resolucion_contador > 0) {
                    g.setColor(new Color(0f, 0.3f, 0.4f, 0.5f));
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
            if (remainingClicks != null) {
                g.setColor(Color.green);
                g.drawString(String.valueOf(remainingClicks), x + anchura - 10, y + altura - 13);
            }
            if (timesClicked > 0) {
                g.setColor(Color.orange);
                g.drawString(String.valueOf(timesClicked), x + anchura - 10, y + altura - 13);
            }
            if (cooldown_contador > 0) {
                BotonComplejo.dibujar_aguja(g, this.x, this.y, this.cooldown_contador, this.cooldown);
            }
        }
    }

    public void renderExtendedInfo(Jugador aliado, Graphics g, ElementoComplejo origen) {
        if (canBeShown) {
            //Origen solo es necesario para botones del Manipulador
            float xbotones = WindowCombat.playerX + 600;
            float ybotones = playerY + VIEWPORT_SIZE_HEIGHT - UI.UI_HEIGHT - 1;
            float anchura_contador = 565;
            float altura_contador = 200;
            float espacio_lineas = 20;
            float xcontador = xbotones + 1;
            float ycontador = ybotones - altura_contador;
            g.setColor(UI_COLOR);
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
                            elemento = new Unidad(aliado, elemento_nombre);
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
                            elemento = new Evento(aliado, elemento_nombre);
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
                        if (elemento.guardiansThreatLevelNeeded > 0) {
                            g.drawString("Nivel: " + elemento.guardiansThreatLevelNeeded, xcontador, ycontador);
                            xcontador += ("Nivel: " + elemento.guardiansThreatLevelNeeded).length() * 10;
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
                g.drawString(Game.anadir_saltos_de_linea(descripcion, anchura_contador), xcontador, ycontador);
            }
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
            canBeUsed = false;
        }
    }

    public void resolucion(List<ElementoComplejo> el, ElementoComplejo e, Game game) {
        //e representa el elemento desde el cual se activa el botón          
        Jugador aliado = game.getPlayerFromElement(e);
        this.resolucion_contador = BotonComplejo.resolucion;
        if (canBeUsed) {
            if (e instanceof Edificio) {
                Edificio edificio = (Edificio) e;
                if (texto == null) {
                    if (this.elemento_tipo.equals("Unidad")) {
                        //Crear Unidad  
                        edificio.createUnit(game, aliado, new Unidad(aliado, this.elemento_nombre));
                    } else {
                        if (this.elemento_tipo.equals("Edificio")) {
                            //Construir Edificio
                            if (!edificio.behaviour.equals(BehaviourEnum.CONSTRUYENDO)) {
                                Edificio contador_edificio = new Edificio(this.elemento_nombre);
                                if (edificio.constructor) {
                                    if (aliado.comprobacion_recursos(contador_edificio)) {
                                        if (edificio instanceof Taller) {
                                            if (elemento_nombre.equals("Anexo")) {
                                                Taller t = (Taller) edificio;
                                                t.construir_anexo(game);
                                            }
                                        } else {
                                            WindowMain.combatWindow.edificio = contador_edificio;
                                            WindowMain.combatWindow.edificio.vida = 0;
                                            WindowMain.combatWindow.constructor = (ElementoAtacante) e;
                                        }
                                    }
                                }
                            }
                        } else {
                            if (this.elemento_tipo.equals("Tecnología")) {
                                //Investigar Tecnología
                                edificio.researchTechnology(game, aliado, new Tecnologia(this.elemento_nombre));
                            } else {
                                //Desbloquear Evento Positivo
                                Evento evento = new Evento(aliado, this.elemento_nombre);
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
                        case "Oso":
                        case "Tortuga":
                        case "Cuervo":
                            edificio.unidad_actual = texto;
                            edificio.behaviour = BehaviourEnum.CONSTRUYENDO;
                            break;
                        case "Reanudar":
                            texto = "Detener";
                            try {
                                sprite = new Image("media/Iconos/Detener.png");
                            } catch (SlickException ex) {
                                Logger.getLogger(BotonComplejo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            edificio.behaviour = BehaviourEnum.CONSTRUYENDO;
                            break;
                        case "Detener":
                            texto = "Reanudar";
                            try {
                                sprite = new Image("media/Iconos/Reanudar.png");
                            } catch (SlickException ex) {
                                Logger.getLogger(BotonComplejo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            edificio.detener_produccion();
                            edificio.behaviour = BehaviourEnum.DETENIDO;
                            break;
                        case "Ayuda Fusión":
                            edificio.mostrarAyudaFusion = !edificio.mostrarAyudaFusion;
                            break;
                    }
                }
            } else {
                Unidad unidad = (Unidad) e;
                if (texto == null) {
                    if (this.elemento_tipo.equals("Unidad")) {
                        //Convertirse en unidad.
                        //e = new Unidad(this.elemento_nombre);
                        unidad.destruir(game, null);
                        Unidad nueva = new Unidad(aliado, this.elemento_nombre, unidad.x, unidad.y);
                        aliado.unidades.add(nueva);
                        nueva.seleccionar();
                    } else {
                        if (this.elemento_tipo.equals("Edificio")) {
                            //Construir Edificio                                    
                            for (ElementoSimple e2 : el) {
                                if (e2 instanceof Unidad) {
                                    Unidad contador = (Unidad) e2;
                                    if (contador.constructor) {
                                        if (!contador.behaviour.equals(BehaviourEnum.CONSTRUYENDO)) {
                                            if (!contador.statusCollection.existe_estado(StatusName.AMNESIA)) {
                                                Edificio contador_edificio = new Edificio(this.elemento_nombre);
                                                if (!contador_edificio.nombre.equals("Cuartel Fénix") || (aliado.cantidad_elemento(contador_edificio) < aliado.limite_cuarteles)) {
                                                    if (game.getPlayerFromElement(e).comprobacion_recursos(contador_edificio)) {
                                                        WindowMain.combatWindow.edificio = contador_edificio;
                                                        WindowMain.combatWindow.edificio.vida = 0;
                                                        WindowMain.combatWindow.constructor = contador;
                                                        break;
                                                    }
                                                } else {
                                                    WindowMain.combatWindow.anadir_mensaje(new Mensaje("Debes aumentar el límite de Cuarteles"));
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
                                    new Habilidad(this.elemento_nombre).activacion(game, unidad);
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
                            Clark.iniciarFusion(game, new Fusion(contador));
                            break;
                        case "Detener":
                            unidad.parar();
                            break;
                        case "Ayuda piloto":
                            unidad.mostrarAyudaPiloto = !unidad.mostrarAyudaPiloto;
                            break;
                    }
                }
            }
        }
    }

    public void comportamiento(int delta) {
        if (cooldown > 0) {
            if (cooldown_contador > 0) {
                if ((cooldown_contador - (Reloj.TIME_REGULAR_SPEED * delta) <= 0)) {
                    cooldown_contador = 0;
                    canBeUsed = true;
                } else {
                    cooldown_contador -= Reloj.TIME_REGULAR_SPEED * delta;
                }
            }
        }
        if (resolucion_contador > 0) {
            if ((resolucion_contador - (Reloj.TIME_REGULAR_SPEED * delta) <= 0)) {
                resolucion_contador = 0;
            } else {
                resolucion_contador -= Reloj.TIME_REGULAR_SPEED * delta;
            }
        }
    }

    public void checkIfEnabled(Jugador aliado) {
        if (!this.isPassiveAbility) {
            boolean enabled = this.canBeUsed;
            if ("Tecnología".equals(elemento_tipo)) {
                enabled = aliado.tecnologias.stream().noneMatch(t -> t.nombre.equals(elemento_nombre)) && aliado.getRecursos() >= this.elemento_coste;
            } else if ("Cuartel Fénix".equals(elemento_nombre)) {
                enabled = aliado.cantidad_edificio(elemento_nombre) < aliado.limite_cuarteles;
            } else if (elemento_coste > 0) {
                if (RaceEnum.MAESTROS.equals(aliado.raza)) {
                    Manipulador m = aliado.getManipulator();
                    enabled = m.mana >= this.elemento_coste;
                } else {
                    enabled = aliado.getRecursos() >= this.elemento_coste;
                    if (RaceEnum.GUARDIANES.equals(aliado.raza)) {
                        enabled = enabled && aliado.guardiansThreatLevel >= this.elementThreatLevelNeeded;
                    }
                }
            }
            this.canBeUsed = enabled && cooldown_contador == 0;
        }
    }
}
