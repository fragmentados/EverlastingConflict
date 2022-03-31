 /*
  * To change this license header, choose License Headers in Project Properties.
  * To change this template file, choose Tools | Templates
  * and open the template in the editor.
  */
 package everlastingconflict.elements.impl;

 import everlastingconflict.behaviour.BehaviourEnum;
 import everlastingconflict.elementosvisuales.BotonComplejo;
 import everlastingconflict.elements.ElementoAtacante;
 import everlastingconflict.elements.ElementoComplejo;
 import everlastingconflict.elements.ElementoSimple;
 import everlastingconflict.elements.util.ElementosComunes;
 import everlastingconflict.gestion.Game;
 import everlastingconflict.gestion.Jugador;
 import everlastingconflict.gestion.ProgressBar;
 import everlastingconflict.races.*;
 import everlastingconflict.races.enums.RaceEnum;
 import everlastingconflict.status.Status;
 import everlastingconflict.status.StatusCollection;
 import everlastingconflict.status.StatusNameEnum;
 import everlastingconflict.watches.Reloj;
 import everlastingconflict.windows.Mensaje;
 import everlastingconflict.windows.WindowCombat;
 import everlastingconflict.windows.WindowMain;
 import org.newdawn.slick.Color;
 import org.newdawn.slick.Graphics;
 import org.newdawn.slick.Image;
 import org.newdawn.slick.*;

 import java.awt.*;
 import java.awt.geom.Ellipse2D;
 import java.awt.geom.Point2D;
 import java.awt.geom.Rectangle2D;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.logging.Level;
 import java.util.logging.Logger;

 import static everlastingconflict.races.Eternium.MAX_UNIT_PER_QUEUE;


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
     public List<Integer> cantidad_produccion = new ArrayList<>();
     public float radio_construccion;
     public boolean activo = true;
     public boolean unitCreator = false;
     public boolean main = false;
     public Image spriteDisabled;
     public boolean isVisible = true;

     //Valores estáticos
     public static final int tiempo_centro = 1000;
     public static final int recursos_centro = 50;
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
             animation = new Animation(images, 450, false);
             icono = new Image("media/Iconos/" + nombre + ".png");
             if (!activo) {
                 spriteDisabled = new Image("media/Edificios/" + nombre + "Desactivado.png");
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     public Edificio(Jugador aliado, String n) {
         this.nombre = n;
         this.behaviour = BehaviourEnum.PARADO;
         this.descripcion = "";
         this.statusCollection = new StatusCollection();
         Raza.edificio(aliado, this);
         initImages();
         vida = vida_max;
         this.botones = new ArrayList<>();
         cola_construccion = new ArrayList<>();
         this.anchura = this.animation.getWidth();
         this.altura = this.animation.getHeight();
         this.anchura_barra_vida = this.anchura;
         this.altura_barra_vida = 5;
         recurso_int = Eternium.tiempo_mineria;
         barra = new ProgressBar(this);
         this.reunion_x = this.x;
         this.reunion_y = this.y + this.altura + this.altura_barra_vida + barra.altura;
         experiencia_al_morir = 50;
         if (aliado != null) {
             this.delay = aliado.getDelay();
         }
     }

     public Edificio(Jugador aliado, String n, float x, float y) {
         this(aliado, n);
         cambiar_coordenadas(x, y);
     }

     public Edificio(Jugador aliado, Edificio e) {
         this(aliado, e.nombre, e.x, e.y);
     }

     public final void cambiar_coordenadas(float x, float y) {
         this.x = x;
         this.y = y;
         barra = new ProgressBar(this);
         this.reunion_x = this.x;
         this.reunion_y = this.y + this.altura + this.altura_barra_vida + barra.altura;
     }

     public boolean researchTechnology(Game p, Jugador j, Tecnologia t) {
         if (j.comprobacion_recursos(t)) {
             // Disable all buttons that research the tecnology
             j.edificios.stream().filter(e -> e.nombre.equals(this.nombre))
                     .flatMap(e -> e.botones.stream())
                     .filter(b -> b.elemento_nombre.equals(t.nombre)).forEach(b -> b.canBeUsed = false);
             if (cola_construccion.isEmpty()) {
                 barra.activar(t.tiempo, this);
             }
             cola_construccion.add(t);
             j.tecnologias.add(t);
             return true;
         }
         return false;
     }

     public int obtener_indice_elemento(String n) {
         //n representa el nombre de la unidad cuyo indice se quiere obtener
         for (int i = 0; i < cola_construccion.size(); i++) {
             if (cola_construccion.get(i).nombre.equals(n)) {
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

     public void cancelLastElementBeingProduced(Game g) {
         if (cola_construccion != null && !cola_construccion.isEmpty()) {
             cancelProduction(g, cola_construccion.get(cola_construccion.size() - 1));
         }
     }

     public void cancelProduction(Game p, ElementoSimple elementProduced) {
         int elementProducedIndex = obtener_indice_elemento(elementProduced.nombre);
         if (elementProduced instanceof Unidad && cantidad_produccion != null && !cantidad_produccion.isEmpty() && cantidad_produccion.get(elementProducedIndex) > 1) {
             //El edificio puede crear varias unidades de vez
             int indice = elementProducedIndex;
             int contador = cantidad_produccion.get(indice).intValue();
             contador--;
             cantidad_produccion.set(indice, new Integer(contador));
         } else {
             if (elementProduced instanceof Tecnologia) {
                 Jugador aliado = p.getPlayerFromElement(this);
                 // Enable all buttons that research the tecnology
                 aliado.edificios.stream().filter(e -> e.nombre.equals(this.nombre))
                         .flatMap(e -> e.botones.stream())
                         .filter(b -> b.elemento_nombre.equals(elementProduced.nombre)).forEach(b -> b.canBeUsed =
                         true);
                 aliado.tecnologias.remove(elementProduced);
             }
             eliminar_cola(elementProduced);
         }
         if (!p.getPlayerFromElement(this).raza.equals(RaceEnum.FENIX)) {
             p.getPlayerFromElement(this).addResources(elementProduced.coste);
         }
     }

     public void createUnit(Game game, Jugador jugador, Unidad unidadACrear) {
         if (jugador.comprobacion_recursos(unidadACrear) && cola_construccion != null && cola_construccion.size() < MAX_UNIT_PER_QUEUE) {
             if (!jugador.raza.equals(RaceEnum.FENIX)) {
                 jugador.removeResources(unidadACrear.coste);
             }
             if (!jugador.raza.equals(RaceEnum.FENIX) || !unidadACrear.constructor || (jugador.cantidad_no_militar() < Fenix.limite_unidades_no_militares)) {
                 setInitialCoordinatesForCreatedUnit(unidadACrear);
                 if (cola_construccion.isEmpty()) {
                     barra.activar(unidadACrear.tiempo, this);
                 }
                 if (this.nombre.equals("Mando Central")) {
                     mandoCentralCrearUnidad(jugador, unidadACrear);
                 } else {
                     cola_construccion.add(unidadACrear);
                 }
             }
         }
     }

     public void setInitialCoordinatesForCreatedUnit(Unidad unitToCreate) {
         float x = getCoordinateForCreateUnit(this.x, reunion_x, anchura);
         float y = getCoordinateForCreateUnit(this.y, reunion_y, altura);
         unitToCreate.x = x;
         unitToCreate.y = y;
     }

     public float getCoordinateForCreateUnit(float initialValue, float maxValue, float buildingOffset) {
         float result;
         if (maxValue > initialValue + buildingOffset / 2) {
             result = initialValue + buildingOffset / 2;
         } else if (maxValue < initialValue - buildingOffset / 2) {
             result = initialValue - buildingOffset / 2;
         } else {
             result = initialValue;
         }
         return result;
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
         barra.desactivar(this);
         cola_construccion = new ArrayList<>();
     }

     public Point2D.Float obtener_coordenadas(Game p, Unidad u) {
         Point2D.Float resultado = new Point2D.Float(0, 0);
         float contador_x = reunion_x, contador_y = reunion_y;
         int i = 0;
         while (u.colision(p, contador_x, contador_y)) {
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
                 barra.activar(cola_construccion.get(0).tiempo, this);
             }
         }
         cola_construccion.remove(e);
         if (cola_construccion.isEmpty()) {
             barra.progreso = 0;
             barra.desactivar(this);
         }
     }

     public void checkProgressBar(Game p, Jugador aliado) {
         if (barra.terminado()) {
             if (cola_construccion.size() > 0) {
                 if (cola_construccion.get(0) instanceof Unidad) {
                     if (this.nombre.equals("Mando Central") || "Nave".equals(this.nombre)) {
                         for (int i = 0; i < cantidad_produccion.get(0); i++) {
                             Unidad unidad = (Unidad) cola_construccion.get(0);
                             Unidad u = new Unidad(aliado, unidad);
                             u.x = unidad.x;
                             u.y = unidad.y;
                             aliado.unidades.add(u);
                             u.iniciarbotones(p);
                             Point2D.Float contador = obtener_coordenadas(p, u);
                             u.anadir_movimiento(contador.x + i * (unidad.anchura + 10), contador.y);
                         }
                         cantidad_produccion.remove(0);
                     } else {
                         Unidad u = (Unidad) cola_construccion.get(0);
                         aliado.unidades.add(u);
                         u.iniciarbotones(p);
                         Point2D.Float contador = obtener_coordenadas(p, u);
                         u.anadir_movimiento(contador.x, contador.y);
                     }
                 } else {
                     Tecnologia t = (Tecnologia) cola_construccion.get(0);
                     t.resolver_efecto(p, aliado);
                     // Remove technology button from every same building
                     aliado.edificios.stream().filter(e -> e.nombre.equals(this.nombre)).forEach(e -> {
                         e.botones.removeIf(b -> b.elemento_nombre.equals(t.nombre));
                     });
                 }
                 eliminar_cola(cola_construccion.get(0));
             }
         }
     }

     public void renderRallyPoint(Graphics g) {
         g.setColor(Color.green);
         g.drawOval(reunion_x - 10, reunion_y - 10, 20, 20);
         g.drawLine(this.x, this.y, reunion_x, reunion_y);
         g.setColor(Color.white);
     }

     @Override
     public void render(Game p, Color c, Input input, Graphics g) {
         this.render(animation, p, c, input, g);
     }

     @Override
     public void render(Animation sprite, Game p, Color c, Input input, Graphics g) {
         if (isVisible) {
             if (WindowCombat.ui.elements.indexOf(this) != -1 && unitCreator) {
                 renderRallyPoint(g);
             }
             super.render(sprite, p, c, input, g);
             if (!activo) {
                 spriteDisabled.draw(x - anchura / 2, y - altura / 2);
             }
             barra.dibujar(g);
         } else {
             Alianza.LANDING_ANIMATION.draw(x - anchura / 2, y - altura / 2);
         }
     }

     @Override
     public void destruir(Game p, ElementoAtacante atacante) {
         Jugador aliado = p.getPlayerFromElement(this);
         if (!BehaviourEnum.DESTRUIDO.equals(this.behaviour)
                 && aliado.edificios.indexOf(this) != -1) {
             this.behaviour = BehaviourEnum.DESTRUIDO;
             if (this.isSelected()) {
                 this.deseleccionar();
             }
             this.removeFromControlGroups();
             aliado.edificios.remove(this);
             if (aliado.raza.equals(RaceEnum.ETERNIUM)) {
                 aliado.perforationCheck();
             }
             Manipulador.checkToGainExperience(p, atacante, experiencia_al_morir, x, y, altura);
             ElementosComunes.BUILDING_DEATH_SOUND.playAt(1f, 1f, x, y, 0f);
         }
     }

     @Override
     public boolean construir(Game p, Edificio edificio, float x, float y) {
         Jugador aliado = p.getPlayerFromElement(this);
         if (aliado.comprobacion_recursos(edificio)) {
             super.construir(p, edificio, x, y);
             behaviour = BehaviourEnum.CONSTRUYENDO;
             aliado.removeResources(edificio.coste);
             edificio.behaviour = BehaviourEnum.CONSTRUYENDOSE;
             edificio_construccion = edificio;
             edificio_construccion.cambiar_coordenadas(x, y);
             return true;
         }
         return false;
     }

     @Override
     public void comportamiento(Game p, Graphics g, int delta) {
         super.comportamiento(p, g, delta);
         Jugador aliado = p.getPlayerFromElement(this);
         if (nombre.equals("Estación reparadora")) {
             for (Unidad u : aliado.unidades) {
                 if (u.vehiculo) {
                     if (this.alcance(200, u)) {
                         u.aumentar_vida(Reloj.TIME_REGULAR_SPEED * delta * 5);
                     }
                 }
             }
         }
         if (nombre.equals("Distorsionador temporal")) {
             List<Jugador> enemies = p.getEnemyPlayersFromElement(this);
             for (Jugador enemy : enemies) {
                 for (Unidad u : enemy.unidades) {
                     if (this.alcance(200, u)) {
                         u.statusCollection.addStatus(new Status(StatusNameEnum.RALENTIZACION, 1f,
                                 40f));
                     }
                 }
             }
         }
         if (constructor && behaviour.equals(BehaviourEnum.CONSTRUYENDO)) {
             if (edificio_construccion.vida == 0) {
                 aliado.edificios.add(edificio_construccion);
             }
             if (edificio_construccion.vida < edificio_construccion.vida_max) {
                 //Continúa la construcción
                 edificio_construccion.vida += (edificio_construccion.vida_max / edificio_construccion.tiempo) * Reloj.TIME_REGULAR_SPEED * delta;
             } else {
                 //Acaba la construcción
                 edificio_construccion.vida = edificio_construccion.vida_max;
                 edificio_construccion.iniciarbotones(p);
                 edificio_construccion.behaviour = BehaviourEnum.PARADO;
                 behaviour = BehaviourEnum.PARADO;
                 edificio_construccion = null;
                 aliado.perforationCheck();
                 this.enableBuildingButtons();
             }
         }
         switch (nombre) {
             case "Primarca":
                 if (recurso_int > 0) {
                     if (recurso_int - Reloj.TIME_REGULAR_SPEED * delta <= 0) {
                         recurso_int = Clark.tiempo_mineria;
                         aliado.addResources(Clark.recursos_primarca);
                         WindowMain.combatWindow.anadir_mensaje(new Mensaje("+" + Clark.recursos_primarca, Color.green, x, y - altura / 2 - 20, 2f));
                     } else {
                         recurso_int -= Reloj.TIME_REGULAR_SPEED * delta;
                     }
                 }
                 break;
             case "Cuartel Fénix":
                 if (behaviour.equals(BehaviourEnum.CONSTRUYENDO)) {
                     if (unidad_actual != null) {
                         if (!barra.isActive()) {
                             this.createUnit(p, aliado, new Unidad(aliado, unidad_actual));
                         }
                     }
                 }
                 break;
             case "Refinería":
                 if (aliado.perforacion && !behaviour.equals(BehaviourEnum.CONSTRUYENDOSE)) {
                     if (recurso_int > 0) {
                         if (recurso_int - Reloj.TIME_REGULAR_SPEED * delta <= 0) {
                             recurso_int = Eternium.tiempo_mineria;
                             aliado.addResources(Eternium.recursos_refineria);
                             WindowMain.combatWindow.anadir_mensaje(new Mensaje("+" + Eternium.recursos_refineria,
                                     Color.green, x, y - altura / 2 - 20, 2f));
                         } else {
                             recurso_int -= Reloj.TIME_REGULAR_SPEED * delta;
                         }
                     }
                 }
                 break;
             case "Centro de restauración":
                 if (recurso_int < Edificio.tiempo_centro) {
                     recurso_int += delta;
                 } else {
                     recurso_int = 0;
                     aliado.addResources(Edificio.recursos_centro);
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

     public List<Rectangle2D> obtener_intersecciones(Game game, float x, float y) {
         List<Rectangle2D> intersecciones = new ArrayList<>();
         int anchura_contador = this.animation.getWidth();
         int altura_contador = this.animation.getHeight();
         float posx, posy;
         posx = x - anchura_contador / 2;
         posy = y - altura_contador / 2;
         Rectangle re = new Rectangle((int) posx, (int) posy, anchura_contador, altura_contador);
         if ((nombre.equals("Refinería")) || (nombre.equals("Centro de restauración"))) {
             for (int i = 0; i < game.recursos.size(); i++) {
                 Recurso r = game.recursos.get(i);
                 if (r.nombre.equals("Hierro")) {
                     Rectangle2D r2 = new Rectangle2D.Float(r.x - r.anchura / 2, r.y - r.altura / 2, r.anchura,
                             r.altura);
                     if (r2.intersects(re)) {
                         Jugador aliado = game.getPlayerFromElement(WindowMain.combatWindow.constructor);
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
             for (Jugador player : game.players) {
                 for (Unidad u : player.unidades) {
                     Rectangle2D r = new Rectangle2D.Float(u.x - u.anchura / 2, u.y - u.altura / 2, u.anchura,
                             u.altura);
                     if (r.intersects(re)) {
                         Rectangle2D r2 = r.createIntersection(re);
                         intersecciones.add(r2);
                     }
                 }
                 for (Edificio e : player.edificios) {
                     Rectangle2D r = new Rectangle2D.Float(e.x - e.anchura / 2, e.y - e.altura / 2, e.anchura,
                             e.altura);
                     if (r.intersects(re)) {
                         Rectangle2D r2 = r.createIntersection(re);
                         intersecciones.add(r2);
                     }
                 }
             }
             for (int i = 0; i < game.recursos.size(); i++) {
                 Recurso r = game.recursos.get(i);
                 Rectangle2D r2 = new Rectangle2D.Float(r.x - r.anchura / 2, r.y - r.altura / 2, r.anchura, r.altura);
                 if (r2.intersects(re)) {
                     Rectangle2D r3 = r2.createIntersection(re);
                     intersecciones.add(r3);
                 }
             }
         }
         return intersecciones;
     }

     public boolean construible(ElementoComplejo constructor, Game p, int x, int y) {
         if (constructor instanceof Edificio) {
             Ellipse2D circulo = new Ellipse2D.Float(constructor.x - ((Edificio) constructor).radio_construccion / 2,
                     constructor.y - ((Edificio) constructor).radio_construccion / 2,
                     ((Edificio) constructor).radio_construccion, ((Edificio) constructor).radio_construccion);
             int anchura_contador = this.animation.getWidth();
             int altura_contador = this.animation.getHeight();
             float posx = x - anchura_contador / 2;
             float posy = y - altura_contador / 2;
             Rectangle re = new Rectangle((int) posx, (int) posy, anchura_contador, altura_contador);
             if (!circulo.contains(re)) {
                 return false;
             }
         }
         List<Rectangle2D> intersecciones = obtener_intersecciones(p, x, y);
         if ((nombre.equals("Refinería")) || (nombre.equals("Centro de restauración"))) {
             return !intersecciones.isEmpty();
         } else {
             return intersecciones.isEmpty();
         }
     }

     public void enable(Unidad enabler, Game p) {
         Jugador aliado = p.getPlayerFromElement(this);
         this.activo = true;
         iniciarbotones(p);
         this.vida = this.vida_max;
         boolean allBuildingsEnabled = aliado.edificios.stream().allMatch(e -> e.activo);
         if (allBuildingsEnabled) {
             // Makes no sense to keep creating enablers
             Edificio ayuntamiento =
                     aliado.edificios.stream().filter(e -> "Ayuntamiento".equals(e.nombre)).findFirst().get();
             ayuntamiento.botones.removeIf(b -> "Activador".equals(b.elemento_nombre));
             ayuntamiento.initButtonKeys();
         }
         // If an enabler was used, we destroy it
         if (enabler != null) {
             enabler.destruir(p, null, false);
         }
     }

     public void turretTransformation(Unidad pilot) {
         switch (pilot.nombre) {
             case "Artillero":
                 nombre = "Torreta demoledora";
                 ataque += 20;
                 cadencia += 0.5f;
                 break;
             case "Amparador":
                 nombre = "Muro";
                 hostil = false;
                 break;
             case "Ingeniero":
                 nombre = "Estación reparadora";
                 hostil = false;
                 break;
             case "Armero":
                 nombre = "Ametralladora";
                 cadencia -= 1.5f;
                 break;
             case "Oteador":
                 nombre = "Torreta artillería";
                 ataque += 10;
                 alcance += 100;
                 cadencia += 1.0f;
                 break;
             case "Explorador":
                 nombre = "Estación de vigilancia";
                 vision = 1750;
                 hostil = false;
                 break;
             case "Corredor":
                 nombre = "Distorsionador temporal";
                 hostil = false;
                 break;
         }
         try {
             animation = new Animation(new Image[]{new Image("media/Torretas/" + nombre + ".png")}, 300, true);
             icono = new Image("media/Torretas/" + nombre + "_icono.png");
         } catch (SlickException ex) {
             Logger.getLogger(Unidad.class.getName()).log(Level.SEVERE, null, ex);
         }
     }

 }
