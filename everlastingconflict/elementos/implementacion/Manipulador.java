/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos.implementacion;

import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elementosvisuales.BotonManipulador;
import everlastingconflict.estados.StatusEffect;
import everlastingconflict.estados.StatusEffectName;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.mapas.VentanaCombate;
import everlastingconflict.razas.Raza;
import everlastingconflict.relojes.Reloj;
import everlastingconflict.relojes.RelojMaestros;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Elías
 */
public class Manipulador extends Unidad {

    public float mana, mana_max;
    public float experiencia, experiencia_max;
    public float regeneracion_mana;
    public float reduccion_enfriamiento;
    public int nivel;
    public float poder_magico;
    public List<BotonComplejo> botones_mejora;
    public int enhancementsRemaining;
    public boolean robo_vida;
    public boolean succion_hechizo;
    public boolean fuerzas_mixtas;
    public boolean ultimo_recurso, ultimo_recurso_activado;
    public boolean disparo_helado;
    public static boolean lider;
    public static boolean alentar;
    public int ultimo_recurso_cantidad;
    public static boolean eficiencia;
    public static int cantidad_invocacion = 2;
    public static float minimo_cooldown = 2;

    private static BotonManipulador SKILL_BUTTON = new BotonManipulador("Habilidades", "Selecciona hasta dos habilidades para que el Manipulador las aprenda");
    private static BotonManipulador ATTRIBUTES_BUTTON = new BotonManipulador("Atributos", "Selecciona hasta cinco atributos para que se mejoren");

    public final int experiencia_maxima() {
        return 100 + 50 * (nivel - 1);
    }

    public void applyEnhancement(String t, BotonComplejo boton) {
        enhancementsRemaining--;
        if (enhancementsRemaining == 0) {
            botones_mejora = new ArrayList<>();
            botones.stream().filter(b -> t.equals(b.texto)).forEach(b -> {
                b.remainingClicks--;
                if (b.remainingClicks == 0) {
                    botones.remove(b);
                }
            });
            this.inicializar_teclas_botones(botones);
        } else {
            if (t.equals("Habilidades")) {
                botones_mejora.remove(boton);
                this.inicializar_teclas_botones(botones_mejora);
            }
        }
    }

    public float obtener_cooldown_reducido(float cooldown) {
        float cooldown_contador = cooldown;
        float reduccion = cooldown_contador * reduccion_enfriamiento / 100;
        if (cooldown_contador - reduccion <= Manipulador.minimo_cooldown) {
            return Manipulador.minimo_cooldown;
        } else {
            return cooldown - reduccion;
        }
    }

    public void aumentar_reduccion(float c) {
        reduccion_enfriamiento += c;
    }

    public void aprender_habilidad(BotonManipulador b) {
        botones.add(b);
        if (!b.requisito.equals(RelojMaestros.tiempo) && !b.requisito.equals("Cualquiera")) {
            b.activado = false;
        }
    }

    public void obtener_botones_atributos() {
        botones_mejora = new ArrayList<>();
        botones_mejora.add(new BotonManipulador("Vida"));
        botones_mejora.add(new BotonManipulador("Ataque"));
        botones_mejora.add(new BotonManipulador("Maná"));
        botones_mejora.add(new BotonManipulador("Defensa"));
        botones_mejora.add(new BotonManipulador("Regeneración maná"));
        botones_mejora.add(new BotonManipulador("Reducción de enfriamiento"));
        botones_mejora.add(new BotonManipulador("Poder mágico"));
        enhancementsRemaining = 5;
        this.inicializar_teclas_botones(this.botones_mejora);
    }

    public void getSkillToUnlockButton(BotonManipulador botonHabilidad) {
        botones_mejora = new ArrayList<>();
        int skillSet = nivel - botonHabilidad.remainingClicks + 1;
        switch (skillSet) {
            case 1:
                botones_mejora.add(new BotonManipulador(new Habilidad("Invocar pugnator"), "Cualquiera"));
                botones_mejora.add(new BotonManipulador(new Habilidad("Deflagración"), RelojMaestros.nombre_dia));
                botones_mejora.add(new BotonManipulador(new Habilidad("Pesadilla"), RelojMaestros.nombre_noche));
                botones_mejora.add(new BotonManipulador("Lider de hordas"));
                botones_mejora.add(new BotonManipulador("Exterminador de masas"));
                botones_mejora.add(new BotonManipulador("Sabiduría arcana"));
                botones_mejora.add(new BotonManipulador("Cauterización automática"));
                break;
            case 2:
                botones_mejora.add(new BotonManipulador(new Habilidad("Invocar sagittarius"), "Cualquiera"));
                botones_mejora.add(new BotonManipulador(new Habilidad("Protección"), RelojMaestros.nombre_dia));
                botones_mejora.add(new BotonManipulador(new Habilidad("Lluvia de estrellas"), RelojMaestros.nombre_noche));
                botones_mejora.add(new BotonManipulador("Entrenamiento avanzado"));
                botones_mejora.add(new BotonManipulador("Tirador experto"));
                botones_mejora.add(new BotonManipulador("Poderío astral"));
                botones_mejora.add(new BotonManipulador("Protección mística"));
                break;
            case 3:
                botones_mejora.add(new BotonManipulador(new Habilidad("Invocar medicum"), "Cualquiera"));
                botones_mejora.add(new BotonManipulador(new Habilidad("Meteoro"), RelojMaestros.nombre_dia));
                botones_mejora.add(new BotonManipulador(new Habilidad("Regeneración"), RelojMaestros.nombre_noche));
                botones_mejora.add(new BotonManipulador(new Habilidad("Presencia abrumadora"), "Cualquiera"));
                botones_mejora.add(new BotonManipulador("Invocación eficiente"));
                botones_mejora.add(new BotonManipulador("Incineración ígnea"));
                botones_mejora.add(new BotonManipulador("Control temporal"));
                break;
            case 4:
                botones_mejora.add(new BotonManipulador(new Habilidad("Invocar magum"), "Cualquiera"));
                botones_mejora.add(new BotonManipulador(new Habilidad("Teletransporte"), "Cualquiera"));
                botones_mejora.add(new BotonManipulador(new Habilidad("Apocalipsis"), "Cualquiera"));
                botones_mejora.add(new BotonManipulador(new Habilidad("Tiempos de necesidad"), "Cualquiera"));
                botones_mejora.add(new BotonManipulador("Entrenamiento supremo"));
                botones_mejora.add(new BotonManipulador("Reinversión física"));
                botones_mejora.add(new BotonManipulador("Reinversión mágica"));
                break;
            case 5:
                botones_mejora.add(new BotonManipulador(new Habilidad("Invocar exterminatore"), "Cualquiera"));
                botones_mejora.add(new BotonManipulador(new Habilidad("Erosión"), RelojMaestros.nombre_noche));
                botones_mejora.add(new BotonManipulador(new Habilidad("Trampa solar"), RelojMaestros.nombre_dia));
                botones_mejora.add(new BotonManipulador("Inspiración"));
                botones_mejora.add(new BotonManipulador("Disparo helado"));
                botones_mejora.add(new BotonManipulador("Eficiencia energética"));
                botones_mejora.add(new BotonManipulador(new Habilidad("Ansia de supervivencia"), "Cualquiera"));
                break;
            case 6:
                botones_mejora.add(new BotonManipulador(new Habilidad("Sacrificio"), "Cualquiera"));
                botones_mejora.add(new BotonManipulador(new Habilidad("Visión interestelar"), RelojMaestros.nombre_dia));
                botones_mejora.add(new BotonManipulador(new Habilidad("Agujero negro"), RelojMaestros.nombre_noche));
                botones_mejora.add(new BotonManipulador(new Habilidad("Impactos drenantes"), "Cualquiera"));
                botones_mejora.add(new BotonManipulador("Fuerzas mixtas"));
                botones_mejora.add(new BotonManipulador("Último recurso"));
                //botones_mejora.add(new BotonManipulador("Clon"));
                break;
        }
        enhancementsRemaining = 2;
        this.inicializar_teclas_botones(this.botones_mejora);
    }

    public final void levelUp() {
        nivel++;
        experiencia = 0;
        experiencia_max = experiencia_maxima();
        if (SKILL_BUTTON.remainingClicks == null) {
           SKILL_BUTTON.remainingClicks = 1;
        } else {
            SKILL_BUTTON.remainingClicks++;
        }
        if (ATTRIBUTES_BUTTON.remainingClicks == null) {
            ATTRIBUTES_BUTTON.remainingClicks = 1;
        } else {
            ATTRIBUTES_BUTTON.remainingClicks++;
        }
        if (!botones.contains(SKILL_BUTTON)) {
            botones.add(SKILL_BUTTON);
        }
        if (!botones.contains(ATTRIBUTES_BUTTON)) {
            botones.add(ATTRIBUTES_BUTTON);
        }
        this.inicializar_teclas_botones(this.botones);
        iniciar_imagenes_manipulador();
    }

    public void aumentar_experiencia(float e) {
        if (experiencia + e >= experiencia_max) {
            experiencia = ((experiencia + e) - experiencia_max);
            if (nivel == 1) {
                statusEffectCollection.eliminar_estado(StatusEffectName.MEDITACION);
                for (BotonComplejo b : botones) {
                    if (b.elemento_nombre != null && b.elemento_nombre.equals("Meditar")) {
                        botones.remove(b);
                        break;
                    }
                }
            }
            levelUp();
        } else {
            experiencia += e;
        }
    }

    public final void iniciar_imagenes_manipulador() {
        try {
            sprite = new Animation(new Image[]{new Image("media/Manipulador/" + nombre + nivel + ".png")}, new int[]{300}, false);
            icono = new Image("media/Manipulador/" + nombre + nivel + "_icono.png");
            miniatura = new Image("media/Miniaturas/Prueba.png");
            anchura = sprite.getWidth();
            altura = sprite.getHeight();
            anchura_barra_vida = anchura;
            //miniatura = new Image("media/Miniaturas/" + nombre + ".png");
        } catch (SlickException e) {

        }
    }

    @Override
    public void iniciarbotones(Partida p) {
        botones.add(new BotonComplejo("Detener"));
        aprender_habilidad(new BotonManipulador(new Habilidad("Eclipse Amanecer"), "Cualquiera"));
        BotonManipulador b = new BotonManipulador(new Habilidad("Meditar"), "Cualquiera");
        b.descripcion = "El Manipulador deja de ser capaz de moverse pero obtiene experiencia regularmente hasta llegar al nivel 2. Pulsa el botón otra vez para cancelar.";
        aprender_habilidad(b);
        //aprender_habilidad(new BotonManipulador(new Habilidad("Deflagración"), "Cualquiera"));
        //aprender_habilidad(new BotonManipulador(new Habilidad("Invocar pugnator"), "Cualquiera"));
        //aprender_habilidad(new BotonManipulador(new Habilidad("Visión interestelar"), RelojMaestros.nombre_dia));
        //aprender_habilidad(new BotonManipulador(new Habilidad("Pesadilla"), RelojMaestros.nombre_noche));        
//        aprender_habilidad(new BotonManipulador(new Habilidad("Invocar pugnator"), "Cualquiera"));
//        aprender_habilidad(new BotonManipulador(new Habilidad("Invocar sagittarius"), "Cualquiera"));
//        aprender_habilidad(new BotonManipulador(new Habilidad("Invocar exterminatore"), "Cualquiera"));
//        aprender_habilidad(new BotonManipulador(new Habilidad("Invocar magum"), "Cualquiera"));
//        aprender_habilidad(new BotonManipulador(new Habilidad("Invocar medicum"), "Cualquiera"));
        inicializar_teclas_botones(botones);
    }


    @Override
    public void aumentar_vida(float c) {
        super.aumentar_vida(c);
        if (ultimo_recurso_activado) {
            ultimo_recurso_activado = false;
            this.defensa -= this.ultimo_recurso_cantidad;
        }
    }

    public void aumentar_mana(float c) {
        if (mana + c >= mana_max) {
            mana = mana_max;
        } else {
            mana += c;
        }
    }

    public Manipulador(float x, float y) {
        super("No hay", x, y);
        nombre = "Manipulador";
        botones = new ArrayList<>();
        botones_mejora = new ArrayList<>();
        Raza.unidad(this);
        vida = vida_max;
        nivel = 0;
        levelUp();
        regeneracion_mana = 1;
        mana_max = 200;
        mana = mana_max;
        experiencia = 0;
        poder_magico = 0;
        reduccion_enfriamiento = 0;
    }

    @Override
    public void comportamiento(Partida p, Graphics g, int delta) {
        super.comportamiento(p, g, delta);
        Jugador aliado = p.jugador_aliado(this);
        if (this.mana < this.mana_max) {
            aumentar_mana(Reloj.TIME_REGULAR_SPEED * regeneracion_mana * delta);
        }
        if (statusEffectCollection.existe_estado(StatusEffectName.MEDITACION)) {
            this.aumentar_experiencia(Reloj.TIME_REGULAR_SPEED * 5 * delta);
        }
        if (Manipulador.alentar) {
            for (Unidad u : aliado.unidades) {
                if (!u.nombre.equals("Manipulador") && u.alcance(200, this)) {
                    u.statusEffectCollection.anadir_estado(new StatusEffect(StatusEffectName.ATAQUE_POTENCIADO, 0, 1));
                }
            }
        }
    }

    public void drawLifeCircle(Graphics g) {
        float xg = VentanaCombate.playerX + VentanaCombate.VIEWPORT_SIZE_X / 2;
        float yg = VentanaCombate.playerY + 5;
        //Círculo exterior
        g.setColor(Color.green);
        g.fillOval(xg, yg, 80, 80);
        //Barra de vida
        float inicio = -270;
        float fin;
        fin = (vida / vida_max) * 360 - 270;
        g.fillArc(xg, yg, 80, 80, inicio, fin);
        g.setColor(Color.black);
        g.drawOval(xg, yg, 80, 80);
        //Circulo interior
        g.setColor(new Color(11, 156, 49, 80));
        g.fillOval(xg + 10, yg + 10, 60, 60);
        g.setColor(Color.black);
        g.drawOval(xg + 10, yg + 10, 60, 60);
        g.setColor(Color.white);
        g.drawString(Integer.toString((int) vida), xg + 25, yg + 30);
    }

    public void drawManaCircle(Graphics g) {
        float xg = VentanaCombate.playerX + VentanaCombate.VIEWPORT_SIZE_X / 2 + 100;
        float yg = VentanaCombate.playerY + 5;
        //Círculo exterior
        g.setColor(new Color(0f, 0f, 0.8f, 1f));
        g.fillOval(xg, yg, 80, 80);
        //Barra de mana
        g.setColor(new Color(0f, 0f, 1f, 1f));
        float inicio = -270;
        float fin;
        fin = (mana / mana_max) * 360 - 270;
        g.fillArc(xg, yg, 80, 80, inicio, fin);
        g.setColor(Color.black);
        g.drawOval(xg, yg, 80, 80);
        //Círculo interior
        g.setColor(new Color(0.3f, 0.3f, 1f, 1f));
        g.fillOval(xg + 10, yg + 10, 60, 60);
        g.setColor(Color.black);
        g.drawOval(xg + 10, yg + 10, 60, 60);
        g.setColor(Color.white);
        g.drawString(Integer.toString((int) mana), xg + 28, yg + 30);
    }

    public void drawLevelCircle(Graphics g) {
        float xg = VentanaCombate.playerX + VentanaCombate.VIEWPORT_SIZE_X / 2 + 200;
        float yg = VentanaCombate.playerY + 5;
        //Círculo exterior
        g.setColor(Color.orange);
        g.fillOval(xg, yg, 80, 80);
        //Barra de experiencia
        //g.setColor(new Color(0f, 1f, 0f, 1f));
        g.setColor(new Color(250, 192, 128, 100));
        float inicio = -270;
        float fin;
        fin = (experiencia / experiencia_max) * 360 - 270;
        g.fillArc(xg, yg, 80, 80, inicio, fin);
        g.setColor(Color.black);
        g.drawOval(xg, yg, 80, 80);
        //Círculo con el nivel
        g.setColor(new Color(250, 192, 128, 255));
        g.fillOval(xg + 10, yg + 10, 60, 60);
        g.setColor(Color.black);
        g.drawOval(xg + 10, yg + 10, 60, 60);
        g.setColor(Color.white);
        g.drawString(Integer.toString(nivel), xg + 35, yg + 30);
    }

    @Override
    public void dibujar(Partida p, Color c, Input input, Graphics g) {
        super.dibujar(p, c, input, g);
    }

}
