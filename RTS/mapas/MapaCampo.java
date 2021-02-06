/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.mapas;

import ElementosSlick2D.BotonComplejo;
import ElementosSlick2D.BotonSimple;
import RTS.campaign.tutorial.Tutorial;
import RTS.Control;
import RTS.elementos.ElementoAtacante;
import RTS.gestion.Partida;
import RTS.elementos.implementacion.Bestia;
import RTS.elementos.implementacion.Bestias;
import RTS.elementos.implementacion.Edificio;
import RTS.elementos.ElementoComplejo;
import RTS.elementos.ElementoSimple;
import RTS.elementos.ElementoVulnerable;
import RTS.elementos.implementacion.Habilidad;
import RTS.elementos.implementacion.Manipulador;
import RTS.elementos.implementacion.Recurso;
import RTS.elementos.implementacion.Unidad;
import RTS.Experimento_Multiplayer_Real.Client;
import RTS.Experimento_Multiplayer_Real.Message;
import RTS.gestion.Evento;
import RTS.gestion.Jugador;
import RTS.gestion.Vision;
import RTS.razas.Clark;
import RTS.razas.Fenix;
import RTS.razas.Fusion;
import RTS.razas.Guardianes;
import RTS.relojes.RelojEternium;
import RTS.relojes.RelojMaestros;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.TextField;

/**
 *
 * @author Elías
 */
public class MapaCampo extends Mapa {

    public Partida partida;
    public static UI iu = new UI();
    public boolean ctrl, deseleccionar;
    public static boolean mayus;
    //Atacar
    public boolean atacar_boolean;
    public Image atacar;
    //Construccion
    public Edificio edificio;
    public ElementoAtacante constructor;
    public Image construccion;
    //Scroll Mapa
    public int scrollspeed = 1;
    public static float playerX = 0;
    public static float playerY = 0;
    public static float WORLD_SIZE_X;
    public static float WORLD_SIZE_Y;
    public static final float VIEWPORT_SIZE_X = 1365;
    public static final float VIEWPORT_SIZE_Y = 567;
    public static float offsetMinX = 0;
    public static float offsetMinY = 0;
    public static float offsetMaxX;
    public static float offsetMaxY;
    public boolean debugg = false;
    //Click
    public boolean click;
    public int x_click, y_click;
    //Grupos de control
    public List<Control> control = new ArrayList<>();
    //Mensajes
    private List<Mensaje> mensajes;
    //RTS.Relojes
    public static RelojEternium reloj_eternium;
    public static RelojMaestros reloj_maestros;
    public static Image detencion;
    //Muerte Bestias
    public static Image muerte;
    //Habilidades
    public ElementoComplejo elemento_habilidad;
    public Habilidad habilidad;
    //Tutorial y Fin de Partida
    public static BotonSimple continuar;
    //Movimiento pantalla
    public float x_movimiento = -1, y_movimiento = -1;
    //Animaciones
    public ElementoVulnerable elemento_atacado = null;
    public Animation victoria, derrota;
    //Zoom
    public float zoomLevel = 1f;
    public static final float zoomStep = 0.0625f;
    //Chat    
    public boolean chat = false;
    public TextField chat_texto;
    List<MensajeChat> mensajes_chat;
    public float x_chat = 200, y_chat = 500;
    //Cliente
    public Client client = new Client("server", 8080, "elias");

    public Vector2f getMousePos(Input input) {
        return new Vector2f(playerX + input.getMouseX(), playerY + input.getMouseY());
    }

    public void addZoomLevel(float a) {
        zoomLevel += a;
        if (zoomLevel > 1.2375f) {
            zoomLevel = 1.2375f;
        }
        if (zoomLevel < .1f) {
            zoomLevel = .1f;
        }
    }

    public void mouseWheelZoom(Input input) {
        int mouseWheel = Mouse.getDWheel();
        if (mouseWheel == 0) {
            return;
        }
        if (mouseWheel != 0) {
            Vector2f mousePosAbs = getMousePos(input);
            Vector2f mousePosRel = mousePosAbs.copy().scale(1 / zoomLevel).add(new Vector2f(playerX, playerY));
            if (mouseWheel > 0) {
                addZoomLevel(zoomStep);
            } else {
                addZoomLevel(-zoomStep);
            }
            Vector2f result = mousePosRel.sub(mousePosAbs.scale(1 / zoomLevel));
            playerX = result.x;
            playerY = result.y;
        }
    }

    public void movimiento_pantalla(float x, float y) {
        x_movimiento = x;
        y_movimiento = y;
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        container.setShowFPS(false);
        container.setVSync(true);
        continuar = new BotonSimple("Continuar");
        if (!(partida instanceof Tutorial)) {
            continuar.activado = false;
        }
        ctrl = atacar_boolean = mayus = click = false;
        deseleccionar = true;
        //partida.j1.unidades.add(new Unidad("Soldado", 1200, 1200));
        //partida.j1.unidades.add(new Unidad("Soldado", 350, 300));
        //partida.j2.unidades.add(new Unidad("Soldado", 500, 300));
        partida.iniciar_elementos(2000, 2000, 1);
        atacar = new Image("media/Cursores/atacar.png");
        construccion = new Image("media/Edificios/construccion.png");
        chat_texto = new TextField(container, container.getDefaultFont(), (int) x_chat, (int) y_chat, 300, 20);
        mensajes = new ArrayList<>();
        mensajes_chat = new ArrayList<>();
        //reloj.detener_reloj(1000);
        detencion = new Image("media/Unidades/Detención.png");
        muerte = new Image("media/Unidades/Muerte.png");
    }

    public void deseleccion() {
        for (Unidad u : partida.j1.unidades) {
            if (u.seleccionada()) {
                u.deseleccionar();
            }
        }
        for (Unidad u : partida.j2.unidades) {
            if (u.seleccionada()) {
                u.deseleccionar();
            }
        }
        for (Edificio e : partida.j1.edificios) {
            if (e.seleccionada()) {
                e.deseleccionar();
            }
        }
        for (Edificio e : partida.j2.edificios) {
            if (e.seleccionada()) {
                e.deseleccionar();
            }
        }
        for (Bestias be : partida.bestias) {
            for (Bestia b : be.contenido) {
                if (b.seleccionada()) {
                    b.deseleccionar();
                }
            }
        }
    }

    public void coordenadas_errores() {
        for (int i = (mensajes.size() - 1); i >= 0; i--) {
            if (mensajes.get(i).error) {
                mensajes.get(i).x = MapaCampo.playerX + MapaCampo.VIEWPORT_SIZE_X / 2 - (mensajes.get(i).mensaje.length() * 10) / 2;
                mensajes.get(i).y = MapaCampo.playerY + MapaCampo.VIEWPORT_SIZE_Y - 20 * (mensajes.size() - i);
            }
        }
    }

    public void anadir_mensaje(Mensaje m) {
        mensajes.add(m);
    }

    public void coordenadas_chat() {
        for (int i = (mensajes_chat.size() - 1); i >= 0; i--) {
            mensajes_chat.get(i).x = x_chat - (mensajes_chat.get(i).texto.length() * 10) / 2;
            mensajes_chat.get(i).y = y_chat - 20 * (mensajes.size() - i);
        }
    }

    public void anadir_mensaje_chat(MensajeChat m) {
        mensajes_chat.add(m);
    }

    public void dibujar_edificio(Graphics g, Edificio edificio) {
        int anchura = edificio.sprite.getWidth();
        int altura = edificio.sprite.getHeight();
        float posx, posy;
        posx = edificio.x - anchura / 2;
        posy = edificio.y - altura / 2;
        if (edificio.vida == 0) {
            g.setColor(new Color(0f, 1f, 0f, 0.8f));
            g.fillRect(posx, posy, anchura, altura);
            edificio.sprite.draw(posx, posy);
        }
//        } else {
//            construccion.draw(edificio.x - anchura / 2, edificio.y - altura / 2, edificio.anchura, edificio.altura);
//            g.setColor(Color.blue);
//            g.fillRect(edificio.x - edificio.anchura / 2, edificio.y + edificio.altura / 2, edificio.anchura_barra_vida * (edificio.vida / edificio.vida_max), edificio.altura_barra_vida);
//            g.setColor(Color.white);
//        }
    }

    public void dibujar_preview_edificio(Graphics g, Edificio edificio, Input input) {
        int anchura = edificio.sprite.getWidth();
        int altura = edificio.sprite.getHeight();
        //Dibujar edificio construcción                
        float posx, posy;
        posx = playerX + input.getMouseX() - anchura / 2;
        posy = playerY + input.getMouseY() - altura / 2;

        if (constructor instanceof Edificio) {
            Ellipse2D circulo = new Ellipse2D.Float(constructor.x - ((Edificio) constructor).radio_construccion / 2, constructor.y - ((Edificio) constructor).radio_construccion / 2, ((Edificio) constructor).radio_construccion, ((Edificio) constructor).radio_construccion);
            Rectangle re = new Rectangle((int) posx, (int) posy, anchura, altura);
            if (!circulo.contains(re)) {
                g.setColor(Color.red);
            } else {
                g.setColor(new Color(0f, 1f, 0f, 0.8f));
            }
        } else {
            if (edificio.nombre.equals("Refinería") || edificio.nombre.equals("Centro de restauración")) {
                g.setColor(Color.red);
            } else {
                g.setColor(new Color(0f, 1f, 0f, 0.8f));
            }
        }
        g.fillRect(posx, posy, anchura, altura);
        g.setColor(Color.red);
        List<Rectangle2D> intersecciones = edificio.obtener_intersecciones(partida, input);
        if (edificio.nombre.equals("Refinería") || edificio.nombre.equals("Centro de restauración")) {
            if (!intersecciones.isEmpty()) {
                g.setColor(new Color(0f, 1f, 0f, 0.8f));
                g.fillRect(posx, posy, anchura, altura);
            }
        } else {
            for (Rectangle2D r : intersecciones) {
                g.fillRect((float) r.getX(), (float) r.getY(), (float) r.getWidth(), (float) r.getHeight());
            }
        }
        edificio.sprite.draw(posx, posy);
    }

//    public Point obtener_coordenadas(ElementoSimple u) {
//        for (int i = 0; i < mapa.getWidth(); i++) {
//            for (int j = 0; j < mapa.getHeight(); j++) {
//                if ((u.x >= i * mapa.getTileWidth()) && (u.x <= (i + 1) * mapa.getTileWidth())) {
//                    if ((u.y >= j * mapa.getTileHeight()) && (u.y <= (j + 1) * mapa.getTileHeight())) {
//                        return new Point(i, j);
//                    }
//                }
//            }
//        }
//        return new Point(-1, -1);
//    }
    public void dibujar_visibilidad(Graphics g, Color c, int desplazamiento) {
        //Modo 1: Ovalos
        g.setColor(c);
        for (Unidad u : partida.j1.unidades) {
            int alcance = u.vision + desplazamiento;
            g.fillOval(u.x - alcance / 2, u.y - alcance / 2, alcance, alcance);
        }
        for (Edificio u : partida.j1.edificios) {
            int alcance = u.vision + desplazamiento;
            g.fillOval(u.x - alcance / 2, u.y - alcance / 2, alcance, alcance);
        }
        for (Vision v : partida.j1.visiones) {
            float alcance = (float) (v.forma.getWidth() + desplazamiento);
            g.fillOval((float) (v.forma.getX() - alcance / 2), (float) (v.forma.getY() - alcance / 2), alcance, alcance);
        }
    }

    public void pathing_prueba(boolean movimiento, List<Unidad> unidades, float x, float y) {
        int i = 0;
        List<Unidad> contador = new ArrayList<>();
        contador.addAll(unidades);
        for (int j = 8; contador.size() > 0; j += 8) {
            int horizontal = 2 * (j / 8) + 1;
            float contador_x = x, contador_y = y;
            while (!contador.isEmpty()) {
                Unidad u = contador.remove(0);
                if (movimiento) {
                    u.mover(partida, contador_x, contador_y);
                } else {
                    u.x = contador_x;
                    u.y = contador_y;
                }
                i++;
                if (i == horizontal) {
                    contador_x = x;
                    contador_y += u.altura + 10;
                    i = 0;
                } else {
                    contador_x += u.anchura + 10;
                }
            }
        }
    }

    public void obtener_ataque_mover(List<Unidad> unidades, float x, float y) {
        float distancia_x = x - unidades.get(0).x;
        float distancia_y = y - unidades.get(0).y;
        unidades.get(0).atacarmover(partida, x, y);
        unidades.remove(unidades.get(0));
        for (Unidad u : unidades) {
            u.atacarmover(partida, u.x + distancia_x, u.y + distancia_y);
        }
    }

    public void obtener_movimiento(List<Unidad> unidades, int x, int y) {
        float distancia_x = x - unidades.get(0).x;
        float distancia_y = y - unidades.get(0).y;
        unidades.get(0).mover(partida, x, y);
        unidades.remove(unidades.get(0));
        for (Unidad u : unidades) {
            u.mover(partida, u.x + distancia_x, u.y + distancia_y);
        }
    }

    public void gestionar_click_derecho(float x, float y) {
        boolean atacada = false;
        List<Unidad> seleccionadas = new ArrayList<>();
        for (ElementoSimple e : iu.elementos) {
            if (e instanceof Edificio) {
                Edificio contador = (Edificio) e;
                contador.reunion_x = x;
                contador.reunion_y = y;
            } else {
                if (e instanceof Unidad) {
                    Unidad u = (Unidad) e;
                    if (u.nombre.equals("Recolector")) {
                        for (Recurso r : partida.recursos) {
                            if (r.hitbox(x, y)) {
                                u.recolectar(partida, r);
                                atacada = true;
                                break;
                            }
                        }
                    }
                    Jugador aliado = partida.jugador_aliado(u);
                    if (u.piloto) {
                        //Embarca vehículo
                        for (Unidad u2 : aliado.unidades) {
                            if (u2.vehiculo) {
                                if (u2.hitbox(x, y)) {
                                    u.embarcar(u2);
                                    return;
                                }
                            }
                        }
                        //Embarca torreta
                        for (Edificio ed : aliado.edificios) {
                            if (ed.hitbox(x, y)) {
                                if (ed.nombre.equals("Torreta defensiva")) {
                                    u.embarcar(ed);
                                    return;
                                }
                            }
                        }
                    }
                    if (aliado.raza.equals(Guardianes.nombre_raza)) {
                        if (u.constructor) {
                            //Constructor activa edificio inactivo
                            for (Edificio ed : aliado.edificios) {
                                if (!ed.activo) {
                                    if (ed.hitbox(x, y)) {
                                        u.embarcar(ed);
                                        return;
                                    }
                                }
                            }
                        } else {
                            //Unidad solucion evento negativo
                            if (aliado.edificios.get(0).hitbox(x, y)) {
                                //Comprobación de si la unidad es necesaria o no
                                for (Evento ev : aliado.eventos.contenido) {
                                    if (!ev.positivo) {
                                        if (ev.nombre_elemento.equals(u.nombre)) {
                                            u.embarcar(aliado.edificios.get(0));
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (ElementoSimple e : iu.elementos) {
            if ((e instanceof Unidad) && !(e instanceof Bestia)) {
                seleccionadas.add((Unidad) e);
            }
        }
        for (Unidad u : partida.j2.unidades) {
            if (u.visible(partida)) {
                if (u.hitbox(x, y)) {
                    atacada = true;
                    for (Unidad u2 : seleccionadas) {
                        u2.atacar(u);
                        elemento_atacado = u;
                    }
                }
            }
        }
        for (Edificio e : partida.j2.edificios) {
            if (e.visible(partida)) {
                if (e.hitbox(x, y)) {
                    atacada = true;
                    for (Unidad u2 : seleccionadas) {
                        u2.atacar(e);
                        elemento_atacado = e;
                    }
                }
            }
        }
        for (Recurso r : partida.j2.lista_recursos) {
            if (r.visible(partida)) {
                if (r.hitbox(x, y)) {
                    atacada = true;
                    for (Unidad u2 : seleccionadas) {
                        u2.atacar(r);
                        elemento_atacado = r;
                    }
                }
            }
        }
        for (Bestias be : partida.bestias) {
            for (Bestia b : be.contenido) {
                if (b.visible(partida)) {
                    if (b.hitbox(x, y)) {
                        atacada = true;
                        for (Unidad u2 : seleccionadas) {
                            u2.atacar(b);
                            elemento_atacado = b;
                        }
                    }
                }
            }
        }
        if (!atacada) {
            if (!seleccionadas.isEmpty()) {
                //Mover unidades
                //pathing(seleccionadas, (int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
                pathing_prueba(true, seleccionadas, x, y);
            }
        }
    }

    public MapaCampo() {
    }

    public void gestion_movimiento_camara(int delta) {
        if (x_movimiento != -1 || y_movimiento != -1) {
            if (x_movimiento != -1) {
                if (playerX < x_movimiento) {
                    if (playerX + 0.5f * delta >= x_movimiento) {
                        playerX = x_movimiento;
                    } else {
                        playerX += 0.5f * delta;
                    }
                } else {
                    if (playerX > x_movimiento) {
                        if (playerX - 0.5f * delta <= x_movimiento) {
                            playerX = x_movimiento;
                        } else {
                            playerX += 0.5f * delta;
                        }
                    } else {
                        x_movimiento = -1;
                    }
                }
            } else {
                if (playerY < y_movimiento) {
                    if (playerY + 0.5f * delta >= y_movimiento) {
                        playerY = y_movimiento;
                    } else {
                        playerY += 0.5f * delta;
                    }
                } else {
                    if (playerY > y_movimiento) {
                        if (playerY - 0.5f * delta <= y_movimiento) {
                            playerY = y_movimiento;
                        } else {
                            playerY += 0.5f * delta;
                        }
                    } else {
                        y_movimiento = -1;
                    }
                }
            }
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        Input input = container.getInput();
        ctrl = input.isKeyDown(Input.KEY_LCONTROL);
        mayus = input.isKeyDown(Input.KEY_LSHIFT);
        // Check Fenix Resources
        Fenix.checkButtonResources(partida.getPlayerByRace(Fenix.nombre_raza));
        // Mensajes
        List<Mensaje> messagesToBeRemoved = new ArrayList<>();
        for (Mensaje m: mensajes) {
            if (m.comprobar_mensaje(delta)) {
                messagesToBeRemoved.add(m);
            }
        }
        mensajes.removeAll(messagesToBeRemoved);
        gestion_movimiento_camara(delta);
        if (!(partida instanceof Tutorial)) {
            if (partida.check_victory_player_1() && victoria == null) {
                this.playerX = 0;
                this.playerY = 0;
                victoria = new Animation(new Image[]{new Image("media/Victoria1.png"), new Image("media/Victoria2.png")}, new int[]{300, 300}, true);
                continuar.x = 650;
                continuar.y = 400;
            }
            if (partida.check_victory_player_2() && derrota == null) {
                this.playerX = 0;
                this.playerY = 0;
                derrota = new Animation(new Image[]{new Image("media/Derrota1.png"), new Image("media/Derrota2.png")}, new int[]{300, 300}, true);
                continuar.x = 650;
                continuar.y = 400;
            }
        }
        if (reloj_maestros != null) {
            if (partida.j1.raza.equals("Maestros")) {
                reloj_maestros.avanzar_reloj(partida.j1, delta);
            } else {
                reloj_maestros.avanzar_reloj(partida.j2, delta);
            }

        }
        if (reloj_eternium != null) {
            if (partida.j1.raza.equals("Eternium")) {
                reloj_eternium.avanzar_reloj(partida.j1, delta);
            } else {
                reloj_eternium.avanzar_reloj(partida.j2, delta);
            }

        }
        //Comprobar fusiones                
        for (int j = 0; j < Clark.fusiones.size(); j++) {
            Fusion f = Clark.fusiones.get(j);
            if (f.comprobacion()) {
                if (f.resultado == null) {
                    f.resultado = Clark.fusion_b(partida, f);
                } else {
                    if (f.comportamiento(delta)) {
                        Clark.fusion_c(f);
                        Clark.fusiones.remove(f);
                        j--;
                    }
                }
            }
        }
        //Controlar que presionen botones mediante teclas
        for (ElementoComplejo e : iu.seleccion_actual) {
            List<BotonComplejo> botones;
            if (!(e instanceof Manipulador) || ((Manipulador) e).botones_mejora.isEmpty()) {
                botones = e.botones;
            } else {
                botones = ((Manipulador) e).botones_mejora;
            }
            for (BotonComplejo b : botones) {
                if (input.isKeyPressed(b.tecla)) {
                    b.resolucion(iu.elementos, e, partida);
                }
            }
        }
        //Comportamientos
        partida.j1.comportamiento_elementos(partida, container.getGraphics(), delta);
        partida.j2.comportamiento_elementos(partida, container.getGraphics(), delta);
        partida.comportamiento_elementos(container.getGraphics(), delta);
        //Mover la pantalla
        if (x_movimiento == -1 && y_movimiento == -1) {
            if ((input.getMouseX() >= 1360) || (input.isKeyDown(Input.KEY_RIGHT))) {
                if (playerX < offsetMaxX) {
                    playerX += scrollspeed * delta;
                }
            }
            if ((input.getMouseY() >= 760) || (input.isKeyDown(Input.KEY_DOWN))) {
                if (playerY < offsetMaxY) {
                    playerY += scrollspeed * delta;
                }
            }
            if ((input.getMouseX() <= 10) || (input.isKeyDown(Input.KEY_LEFT))) {
                if (playerX > offsetMinX) {
                    playerX -= scrollspeed * delta;
                }
            }
            if ((input.getMouseY() <= 10) || (input.isKeyDown(Input.KEY_UP))) {
                if (playerY > offsetMinY) {
                    playerY -= scrollspeed * delta;
                }
            }
        }
        //Grupos de Control
        for (int i = Input.KEY_1; i <= Input.KEY_0; i++) {
            if (input.isKeyPressed(i)) {
                if (input.isKeyDown(Input.KEY_LCONTROL)) {
                    //Asignar grupo de control
                    for (Control c : control) {
                        if (c.tecla == i) {
                            control.remove(c);
                            break;
                        }
                    }
                    control.add(new Control(iu.elementos, i));
                } else {
                    //Seleccionar grupo de control                    
                    for (Control c : control) {
                        if (c.tecla == i) {
                            boolean cambiar_perspectiva = true;
                            if (iu.elementos.isEmpty()) {
                                cambiar_perspectiva = false;
                            }
                            for (int j = 0; j < iu.elementos.size(); j++) {
                                if (iu.elementos.get(j) != c.contenido.get(j)) {
                                    cambiar_perspectiva = false;
                                    break;
                                }
                            }
                            if (cambiar_perspectiva) {
                                //El grupo de control ya está seleccionado: Se cambia a la perspectiva del g.c.
                                //Obtener coordenadas mínimas y máximas
                                float x_min = -1, y_min = -1;
                                float x_max = -1, y_max = -1;
                                for (ElementoComplejo e : c.contenido) {
                                    if (x_min == -1 || e.x < x_min) {
                                        x_min = e.x;
                                    }
                                    if (y_min == -1 || e.y < y_min) {
                                        y_min = e.y;
                                    }
                                    if (x_max == -1 || e.x > x_max) {
                                        x_max = e.x;
                                    }
                                    if (y_max == -1 || e.y > y_max) {
                                        y_max = e.y;
                                    }
                                }
                                //Obtener coordenadas medias
                                float x_medio = (x_max + x_min) / 2;
                                float y_medio = (y_max + y_min) / 2;
                                if (x_medio - MapaCampo.VIEWPORT_SIZE_X / 2 <= 0) {
                                    MapaCampo.playerX = 0;
                                } else {
                                    MapaCampo.playerX = x_medio - MapaCampo.VIEWPORT_SIZE_X / 2;
                                }
                                if (y_medio - MapaCampo.VIEWPORT_SIZE_Y / 2 <= 0) {
                                    MapaCampo.playerY = 0;
                                } else {
                                    MapaCampo.playerY = y_medio - MapaCampo.VIEWPORT_SIZE_Y / 2;
                                }
                            } else {
                                //El grupo de control no está seleccionado: Se selecciona.
                                deseleccion();
                                for (ElementoComplejo e : c.contenido) {
                                    e.seleccionar();
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        //Chat
        if (input.isKeyPressed(Input.KEY_ENTER)) {
            if (chat) {
                anadir_mensaje_chat(new MensajeChat(client.username, chat_texto.getText()));
                client.send_message(new Message(Message.text_type, client.username + "\n" + chat_texto.getText()));
                chat_texto.setText("");
                chat_texto.setFocus(false);
            } else {
                chat_texto.setFocus(true);
            }
            chat = !chat;
        }
        //Rueda del ratón: Zoom
        mouseWheelZoom(input);
        //Tabulación
        if (input.isKeyPressed(Input.KEY_TAB)) {
            iu.siguiente_seleccion();
        }
        //Cambiar a Atacar-Mover
        if (input.isKeyPressed(Input.KEY_A)) {
            boolean contador_boolean = false;
            for (ElementoSimple e : iu.elementos) {
                if (e instanceof Unidad) {
                    contador_boolean = true;
                }
            }
            if (contador_boolean) {
                atacar_boolean = true;
                container.setMouseCursor(atacar, 10, 10);
            }
        }
        /*
         if (partida instanceof Tutorial) {
         if (!continuar.activado) {
         Tutorial t = (Tutorial) partida;
         if (t.pasos.get(0).comprobacion(partida)) {
         continuar.activado = true;
         t.pasos.get(0).efecto(partida);
         t.pasos.remove(0);                    
         if (t.pasos.isEmpty()) {
         RTS.RTS.mapa_ejemplo.cambio_de_mapa(container, partida, "Menu");
         }
         }
         }
         }
         */
        //Click izquierdo
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            x_click = (int) playerX + input.getMouseX();
            y_click = (int) playerY + input.getMouseY();
            if (!(partida instanceof Tutorial)) {
                if (continuar.presionado(x_click, y_click)) {
                    RTS.RTS.mapa_ejemplo.cambio_de_mapa(container, new Partida(), "Menu");
                }
            }
            click = true;
            if (partida instanceof Tutorial) {
                Tutorial t = (Tutorial) partida;
                if (t.pasos.get(0).comprobacion(partida)) {
                    if (!continuar.activado) {
                        continuar.activado = true;
                    }
                    t.pasos.get(0).efecto(partida);
                    t.pasos.remove(0);
                    click = false;
                    if (t.pasos.isEmpty()) {
                        RTS.RTS.mapa_ejemplo.cambio_de_mapa(container, partida, "Menu");
                    }
                }
            }
            if (y_click >= ((int) playerY + VIEWPORT_SIZE_Y)) {
                iu.gestionar_click_izquierdo(input, partida, x_click, y_click, mayus);
                click = false;
            } else if ((edificio != null)) {
                //Decidir la localización de un edificio
                if (!constructor.nombre.equals("No hay")) {
                    if (edificio.construible(constructor, partida, input, x_click, y_click)) {
                        if (edificio.nombre.equals("Refinería") || edificio.nombre.equals("Centro de restauración")) {
                            Recurso r = partida.recurso_mas_cercano(null, null, "Hierro", (int) MapaCampo.playerX + input.getMouseX(), (int) MapaCampo.playerY + input.getMouseY());
                            if (r != null) {
                                constructor.construir(partida, edificio, r.x, r.y);
                            }
                        } else {
                            constructor.construir(partida, edificio, x_click, y_click);
                        }
                        edificio = null;
                        constructor = null;
                    }
                    click = false;
                }
            } else if (elemento_habilidad != null) {
                //Selección de objetivo para habilidad
                ElementoComplejo elemento;
                if ((elemento = habilidad.seleccion_objetivo(partida, elemento_habilidad, x_click, y_click)) != null) {
                    Unidad unidad = (Unidad) elemento_habilidad;
                    unidad.habilidad(habilidad, elemento);
                    elemento_habilidad = null;
                    habilidad = null;
                    click = false;
                }
            } else if (atacar_boolean) {
                //Atacar - Mover
                List<Unidad> seleccionadas = new ArrayList<>();
                for (ElementoSimple e : iu.elementos) {
                    if (e instanceof Unidad) {
                        seleccionadas.add((Unidad) e);
                    }
                }
                obtener_ataque_mover(seleccionadas, (int) playerX + input.getMouseX(), (int) playerY + input.getMouseY());
                atacar_boolean = false;
                container.setDefaultMouseCursor();
                deseleccionar = false;
            } else {
                //Seleccionar un Elemento
                List<ElementoComplejo> contador = new ArrayList<>();
                contador.addAll(partida.j1.unidades);
                contador.addAll(partida.j1.edificios);
                for (ElementoComplejo e : contador) {
                    if (e.hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY())) {
                        deseleccion();
                        e.seleccionar();
                        break;
                    }
                }
                for (Bestias be : partida.bestias) {
                    for (Bestia b : be.contenido) {
                        if (b.hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY())) {
                            deseleccion();
                            b.seleccionar();
                            click = false;
                            break;
                        }
                    }
                }
                for (Unidad u : partida.j2.unidades) {
                    if (u.hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY())) {
                        deseleccion();
                        u.seleccionar();
                        click = false;
                        break;
                    }
                }
                for (Edificio e : partida.j2.edificios) {
                    if (e.hitbox((int) playerX + input.getMouseX(), (int) playerY + input.getMouseY())) {
                        deseleccion();
                        e.seleccionar();
                        click = false;
                        break;
                    }
                }
            }

        }

        if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (!click) {
                x_click = (int) playerX + input.getMouseX();
                y_click = (int) playerY + input.getMouseY();
                if (y_click >= ((int) playerY + VIEWPORT_SIZE_Y)) {
                    if ((x_click >= (MapaCampo.playerX + iu.anchura_miniatura + iu.anchura_seleccion + iu.anchura_botones)) && (x_click <= (MapaCampo.playerX + MapaCampo.VIEWPORT_SIZE_X))) {
                        iu.mover_perspectiva(x_click, y_click);
                    }
                }
            }
        }

        //Control de cuadrado de selección
        if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (click) {
                if ((!mayus) && (deseleccionar)) {
                    deseleccion();
                } else {
                    deseleccionar = true;
                }
                int x_final = (int) playerX + input.getMouseX();
                int y_final = (int) playerY + input.getMouseY();
                if (x_click > x_final) {
                    int contador = x_click;
                    x_click = x_final;
                    x_final = contador;
                }
                if (y_click > y_final) {
                    int contador = y_click;
                    y_click = y_final;
                    y_final = contador;
                }
                for (Unidad u : partida.j1.unidades) {
                    if (u.en_rango(x_click, y_click, x_final, y_final)) {
                        if (ctrl) {
                            for (Unidad u2 : partida.j1.unidades) {
                                if (u2 != u) {
                                    if (u2.nombre.equals(u.nombre)) {
                                        if (!u2.seleccionada()) {
                                            u2.seleccionar();
                                        }
                                    }
                                }
                            }
                        }

                        u.seleccionar();
                    }
                }
                boolean seleccionar_edificios = true;
                for (ElementoComplejo e : iu.elementos) {
                    if (e instanceof Unidad) {
                        seleccionar_edificios = false;
                        break;
                    }
                }
                if (seleccionar_edificios) {
                    for (Edificio e : partida.j1.edificios) {
                        if (e.en_rango(x_click, y_click, x_final, y_final)) {
                            if (ctrl) {
                                for (Edificio e2 : partida.j1.edificios) {
                                    if (e2 != e) {
                                        if (e2.nombre.equals(e.nombre)) {
                                            e2.seleccionar();
                                        }
                                    }
                                }
                            }
                            e.seleccionar();
                        }
                    }
                }
                click = false;
            }
        }
        //Click derecho
        if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
            if (partida instanceof Tutorial) {
                Tutorial t = (Tutorial) partida;
                if (t.pasos.get(0).comprobacion(partida)) {
                    if (!continuar.activado) {
                        continuar.activado = true;
                    }
                    t.pasos.get(0).efecto(partida);
                    t.pasos.remove(0);
                    if (t.pasos.isEmpty()) {
                        RTS.RTS.mapa_ejemplo.cambio_de_mapa(container, partida, "Menu");
                    }
                }
            }
            x_click = (int) playerX + input.getMouseX();
            y_click = (int) playerY + input.getMouseY();
            if (y_click >= ((int) playerY + VIEWPORT_SIZE_Y)) {
                if ((x_click >= (MapaCampo.playerX + iu.anchura_miniatura + iu.anchura_seleccion + iu.anchura_botones)) && (x_click <= (MapaCampo.playerX + MapaCampo.VIEWPORT_SIZE_X))) {
                    Point2D resultado = iu.obtener_coordenadas_minimapa(x_click, y_click);
                    gestionar_click_derecho((float) resultado.getX(), (float) resultado.getY());
                }
                click = false;
            } else if (atacar_boolean) {
                container.setDefaultMouseCursor();
                atacar_boolean = false;
            } else if (edificio != null) {
                //Cancelar Edificio                                
                edificio = null;
            } else if (elemento_habilidad != null) {
                elemento_habilidad = null;
                habilidad = null;
            } else {
                gestionar_click_derecho(x_click, y_click);
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        Input input = container.getInput();
        //invertir.dibujar(g);
        //Negro        
        g.setBackground(new Color(0.1f, 0.1f, 0.05f, 1f));
        g.scale(zoomLevel, zoomLevel);
        g.translate(-playerX, -playerY);
        //mapa.render(0, 0);   
        if (victoria != null || derrota != null) {
            if (victoria != null) {
                victoria.draw(250, 200);
            } else {
                derrota.draw(250, 200);
            }
        } else {
            //Gris
            dibujar_visibilidad(g, new Color(0.2f, 0.2f, 0.05f, 1f), 100);
            //Verde
            dibujar_visibilidad(g, new Color(0.3f, 0.3f, 0.1f, 1f), 0);
            partida.dibujar_elementos(g, input);
            partida.j1.dibujar_elementos(partida, g, input);
            for (Recurso r : partida.j2.lista_recursos) {
                if (r.visible(partida)) {
                    r.dibujar(partida, partida.j2.color, input, g);
                    r.dibujar_barra_de_vida(g, partida.j2.color);
                }
            }
            for (Edificio e : partida.j2.edificios) {
                if (e.visible(partida)) {
                    e.dibujar(partida, partida.j2.color, input, g);
                }
            }
            for (Unidad u : partida.j2.unidades) {
                if (u.visible(partida)) {
                    u.dibujar(partida, partida.j2.color, input, g);
                }
            }
            if (elemento_atacado != null) {
                if (elemento_atacado instanceof Bestia) {
                    elemento_atacado.circulo_extendido(g, Color.blue);
                } else {
                    elemento_atacado.circulo_extendido(g, partida.jugador_aliado(elemento_atacado).color);
                }
            }
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                if (click) {
                    g.setColor(Color.green);
                    g.drawRect(x_click, y_click, playerX + input.getMouseX() - x_click, playerY + input.getMouseY() - y_click);
                    g.setColor(Color.white);
                }
            }

            //Construcción        
            if ((edificio != null)) {
                if (constructor instanceof Edificio) {
                    g.setColor(Color.green);
                    g.drawOval(constructor.x + -((Edificio) constructor).radio_construccion / 2, constructor.y - ((Edificio) constructor).radio_construccion / 2, ((Edificio) constructor).radio_construccion, ((Edificio) constructor).radio_construccion);
                    g.setColor(Color.white);
                }
                dibujar_preview_edificio(g, edificio, input);
            }
            for (Unidad u : partida.j1.unidades) {
                Edificio contador = u.edificio_construccion;
                if ((contador != null) && (contador.vida < contador.vida_max)) {
                    dibujar_edificio(g, u.edificio_construccion);
                }
            }
            for (Edificio e : partida.j1.edificios) {
                Edificio contador = e.edificio_construccion;
                if ((contador != null) && (contador.vida < contador.vida_max)) {
                    dibujar_edificio(g, e.edificio_construccion);
                }
            }
            //Habilidad
            if (elemento_habilidad != null) {
                g.setColor(Color.cyan);
                float alcance_anchura = 2 * (habilidad.alcance + elemento_habilidad.anchura / 2), alcance_altura = 2 * (habilidad.alcance + elemento_habilidad.altura / 2);
                g.drawOval(elemento_habilidad.x - alcance_anchura / 2, elemento_habilidad.y - alcance_altura / 2, alcance_anchura, alcance_altura);
                if (habilidad.area != 0) {
                    g.drawOval(playerX + input.getMouseX() - habilidad.area, playerY + input.getMouseY() - habilidad.area, habilidad.area * 2, habilidad.area * 2);
                }
                g.setColor(Color.white);
            }
            iu.dibujar_UI(partida, g);
            //Grupos de control
            for (int i = Input.KEY_1; i <= Input.KEY_0; i++) {
                int numero;
                if (i == 11) {
                    numero = 0;
                } else {
                    numero = i - 1;
                }
                g.drawRect(playerX + 1100 + i * 22, playerY + VIEWPORT_SIZE_Y - 40, 20, 20);
                g.drawString(Integer.toString(numero), playerX + 1100 + i * 22, playerY + VIEWPORT_SIZE_Y - 40);
                g.drawRect(playerX + 1100 + i * 22, playerY + VIEWPORT_SIZE_Y - 20, 20, 20);
                for (Control c : control) {
                    if (c.tecla == i) {
                        g.drawString(Integer.toString(c.contenido.size()), playerX + 1100 + i * 22, playerY + VIEWPORT_SIZE_Y - 20);
                        break;
                    }
                }
            }
            if (atacar_boolean) {
                g.setColor(Color.red);
                if (iu.seleccion_actual.get(0) instanceof Unidad) {
                    if (((Unidad) iu.seleccion_actual.get(0)).area > 0) {
                        Unidad unidad = (Unidad) iu.seleccion_actual.get(0);
                        g.drawOval(playerX + input.getMouseX() - unidad.area, playerY + input.getMouseY() - unidad.area, unidad.area * 2, unidad.area * 2);
                    }
                }
                g.setColor(Color.white);
            }
            //Mensajes
            coordenadas_errores();
            for (Mensaje m : mensajes) {
                m.dibujar(g);
            }
            //Mensajes chat
            coordenadas_chat();
            for (MensajeChat me : mensajes_chat) {
                me.dibujar(partida, g);
            }
            //RTS.Relojes
            if (reloj_eternium != null) {
                reloj_eternium.dibujar(g);
            }
            if (reloj_maestros != null) {
                reloj_maestros.dibujar(g);
            }
            //Boton seleccionado
            BotonComplejo boton_seleccionado = iu.obtener_boton_seleccionado(playerX + input.getMouseX(), playerY + input.getMouseY());
            if (boton_seleccionado != null) {
                boton_seleccionado.dibujar_extendido(null, g, iu.seleccion_actual.get(0));
            }
            //Evento seleccionado Guardián
            if (partida.j1.raza.equals(Guardianes.nombre_raza)) {
                Evento evento_seleccionado = partida.j1.obtener_evento_seleccionado(playerX + input.getMouseX(), playerY + input.getMouseY());
                if (evento_seleccionado != null) {
                    new BotonComplejo(evento_seleccionado).dibujar_extendido(partida.j1, g, null);
                }
            } else {
                if (partida.j2.raza.equals(Guardianes.nombre_raza)) {
                    Evento evento_seleccionado = partida.j2.obtener_evento_seleccionado(playerX + input.getMouseX(), playerY + input.getMouseY());
                    if (evento_seleccionado != null) {
                        new BotonComplejo(evento_seleccionado).dibujar_extendido(partida.j2, g, null);
                    }
                }
            }
            //Pasos tutorial
            if (partida instanceof Tutorial) {
                Tutorial t = (Tutorial) partida;
                g.setColor(new Color(0.2f, 0.2f, 0.2f, 0.8f));
                g.fillRect(playerX, playerY, MapaCampo.VIEWPORT_SIZE_X, 100);
                g.setColor(Color.white);
                g.drawString(t.pasos.get(0).texto, playerX, playerY);
                continuar.x = playerX + MapaCampo.VIEWPORT_SIZE_X - continuar.anchura;
                continuar.y = playerY + 100 - 21;
                partida.j1.dibujar_recursos(g, playerX + VIEWPORT_SIZE_X - 100, playerY);
            } else {
                partida.j1.dibujar_recursos(g, playerX + VIEWPORT_SIZE_X - 100, playerY);
                // TODO EFB No pintar recursos del segundo jugador
                //partida.j2.dibujar_recursos(g, playerX + VIEWPORT_SIZE_X - 100, playerY + 15);
            }
        }
        continuar.dibujar(g);
        if (chat) {
            g.drawString(client.username, x_chat - client.username.length() * 10, y_chat);
            chat_texto.render(container, g);
        }
    }
}
