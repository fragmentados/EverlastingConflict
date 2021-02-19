/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.elementos.implementacion;

import everlastingconflict.elementos.ElementoAtacante;
import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elementosvisuales.BotonManipulador;
import everlastingconflict.estados.StatusEffect;
import everlastingconflict.estados.StatusEffectName;
import everlastingconflict.gestion.Jugador;
import everlastingconflict.gestion.Partida;
import everlastingconflict.mapas.Mensaje;
import everlastingconflict.mapas.VentanaCombate;
import everlastingconflict.mapas.VentanaPrincipal;
import everlastingconflict.razas.RaceNameEnum;
import everlastingconflict.razas.Raza;
import everlastingconflict.relojes.Reloj;
import everlastingconflict.relojes.RelojMaestros;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<BotonComplejo> enhancementButtons;
    public List<BotonManipulador> skillsToLearn = new ArrayList<>();
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

    public void applyEnhancement(String t, BotonManipulador skillButton) {
        enhancementsRemaining--;
        if ("Habilidades".equals(t)) {
            markSkillToBeLearned(skillButton);
        }
        if (enhancementsRemaining == 0) {
            enhancementButtons = new ArrayList<>();
            if (!skillsToLearn.isEmpty()) {
                skillsToLearn.stream().forEach(b -> learnSkill(b));
                skillsToLearn = new ArrayList<>();
            }
            if ("Habilidades".equals(t)) {
                reduceRemainingClicksFromEnhancementButton(SKILL_BUTTON);
            } else {
                reduceRemainingClicksFromEnhancementButton(ATTRIBUTES_BUTTON);
            }
            this.sortSkillButtons();
        }
    }

    private void reduceRemainingClicksFromEnhancementButton(BotonComplejo b) {
        b.remainingClicks--;
        if (b.remainingClicks == 0) {
            botones.remove(b);
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

    public void markSkillToBeLearned(BotonManipulador b) {
        b.canBeShown = false;
        b.canBeUsed = false;
        skillsToLearn.add(b);
    }

    public void learnSkill(BotonManipulador b) {
        b.canBeShown = true;
        b.canBeUsed = b.requisito == null || RelojMaestros.tiempo.equals(b.requisito);
        if (!"Habilidad".equals(b.elemento_tipo)) {
            b.isPassiveAbility = true;
            b.tecla_string = null;
            b.tecla = 0;
        }
        botones.add(b);
    }

    public void obtener_botones_atributos() {
        enhancementButtons = new ArrayList<>();
        enhancementButtons.add(new BotonManipulador("Vida"));
        enhancementButtons.add(new BotonManipulador("Ataque"));
        enhancementButtons.add(new BotonManipulador("Maná"));
        enhancementButtons.add(new BotonManipulador("Defensa"));
        enhancementButtons.add(new BotonManipulador("Regeneración maná"));
        enhancementButtons.add(new BotonManipulador("Reducción de enfriamiento"));
        enhancementButtons.add(new BotonManipulador("Poder mágico"));
        enhancementsRemaining = 5;
        this.initButtonKeys(this.enhancementButtons);
    }

    public void getSkillsToUnlock() {
        enhancementButtons = new ArrayList<>();
        int skillSet = nivel - SKILL_BUTTON.remainingClicks + 1;
        switch (skillSet) {
            case 1:
                enhancementButtons.add(new BotonManipulador(new Habilidad("Invocar pugnator")));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Deflagración"), RelojMaestros.nombre_dia));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Pesadilla"), RelojMaestros.nombre_noche));
                enhancementButtons.add(new BotonManipulador("Lider de hordas"));
                enhancementButtons.add(new BotonManipulador("Exterminador de masas"));
                enhancementButtons.add(new BotonManipulador("Sabiduría arcana"));
                enhancementButtons.add(new BotonManipulador("Cauterización automática"));
                break;
            case 2:
                enhancementButtons.add(new BotonManipulador(new Habilidad("Invocar sagittarius")));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Protección"), RelojMaestros.nombre_dia));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Lluvia de estrellas"), RelojMaestros.nombre_noche));
                enhancementButtons.add(new BotonManipulador("Entrenamiento avanzado"));
                enhancementButtons.add(new BotonManipulador("Tirador experto"));
                enhancementButtons.add(new BotonManipulador("Poderío astral"));
                enhancementButtons.add(new BotonManipulador("Protección mística"));
                break;
            case 3:
                enhancementButtons.add(new BotonManipulador(new Habilidad("Invocar medicum")));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Meteoro"), RelojMaestros.nombre_dia));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Regeneración"), RelojMaestros.nombre_noche));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Presencia abrumadora")));
                enhancementButtons.add(new BotonManipulador("Invocación eficiente"));
                enhancementButtons.add(new BotonManipulador("Incineración ígnea"));
                enhancementButtons.add(new BotonManipulador("Control temporal"));
                break;
            case 4:
                enhancementButtons.add(new BotonManipulador(new Habilidad("Invocar magum")));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Teletransporte")));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Apocalipsis")));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Tiempos de necesidad")));
                enhancementButtons.add(new BotonManipulador("Entrenamiento supremo"));
                enhancementButtons.add(new BotonManipulador("Reinversión física"));
                enhancementButtons.add(new BotonManipulador("Reinversión mágica"));
                break;
            case 5:
                enhancementButtons.add(new BotonManipulador(new Habilidad("Invocar exterminatore")));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Erosión"), RelojMaestros.nombre_noche));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Trampa solar"), RelojMaestros.nombre_dia));
                enhancementButtons.add(new BotonManipulador("Inspiración"));
                enhancementButtons.add(new BotonManipulador("Disparo helado"));
                enhancementButtons.add(new BotonManipulador("Eficiencia energética"));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Ansia de supervivencia")));
                break;
            case 6:
                enhancementButtons.add(new BotonManipulador(new Habilidad("Sacrificio")));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Visión interestelar"), RelojMaestros.nombre_dia));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Agujero negro"), RelojMaestros.nombre_noche));
                enhancementButtons.add(new BotonManipulador(new Habilidad("Impactos drenantes")));
                enhancementButtons.add(new BotonManipulador("Fuerzas mixtas"));
                enhancementButtons.add(new BotonManipulador("Último recurso"));
                enhancementButtons.add(new BotonManipulador("Clon"));
                break;
        }
        enhancementsRemaining = 2;
        this.initButtonKeys(this.enhancementButtons);
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
        this.initButtonKeys();
        iniciar_imagenes_manipulador();
    }

    public void aumentar_experiencia(float e) {
        if (experiencia + e >= experiencia_max) {
            experiencia = ((experiencia + e) - experiencia_max);
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
        learnSkill(new BotonManipulador(new Habilidad("Eclipse Amanecer")));
        BotonManipulador b = new BotonManipulador(new Habilidad("Meditar"));
        b.descripcion = "El Manipulador deja de ser capaz de moverse pero obtiene maná a un ritmo mayor. Si está en nivel 1 también obtiene experiencia regularmente. Pulsa el botón otra vez para cancelar.";
        learnSkill(b);
        initButtonKeys();
    }


    @Override
    public void aumentar_vida(float c) {
        super.aumentar_vida(c);
        if (ultimo_recurso_activado) {
            ultimo_recurso_activado = false;
            this.defensa -= this.ultimo_recurso_cantidad;
        }
    }

    public void recoverMana(Jugador aliado, float c) {
        if (mana + c >= mana_max) {
            mana = mana_max;
        } else {
            mana += c;
        }
        this.checkButtonResources(aliado);
    }

    public Manipulador(float x, float y) {
        super("No hay", x, y);
        nombre = "Manipulador";
        botones = new ArrayList<>();
        enhancementButtons = new ArrayList<>();
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
            recoverMana(aliado, Reloj.TIME_REGULAR_SPEED * regeneracion_mana * delta);
        }
        if (statusEffectCollection.existe_estado(StatusEffectName.MEDITACION) && this.nivel == 1) {
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
        // TODO EFB Enhance life circle to show the color in the outer circle
        /*Color outerCircleColor = Color.green;
        if (vida < vida_max / 2) {
            outerCircleColor = Color.red;
        } else if (vida < vida_max) {
            outerCircleColor = Color.yellow;
        }*/
        float x = VentanaCombate.playerX + VentanaCombate.VIEWPORT_SIZE_X / 2;
        drawCircle(g, x, vida, vida_max, (int) vida, 23,
                new Color(0f, 0.8f, 0f, 1f), Color.green);
    }

    public void drawManaCircle(Graphics g) {
        float x = VentanaCombate.playerX + VentanaCombate.VIEWPORT_SIZE_X / 2 + 100;
        drawCircle(g, x, mana, mana_max, (int) mana, 28, new Color(0f, 0f, 0.8f, 1f),
                new Color(0f, 0f, 1f, 1f));
    }

    public void drawXpCircle(Graphics g) {
        float x = VentanaCombate.playerX + VentanaCombate.VIEWPORT_SIZE_X / 2 + 200;
        drawCircle(g, x, experiencia, experiencia_max, nivel, 35, new Color(204, 160, 0, 255),
                Color.orange);
    }

    public void drawCircle(Graphics g, float x, float initialValue, float maxValue, int middleNumber, int middleNumberXOffset, Color... colors) {
        float yg = VentanaCombate.playerY + 5;
        //Círculo exterior
        g.setColor(colors[0]);
        g.fillOval(x, yg, 80, 80);
        //Barra de experiencia
        g.setColor(colors[1]);
        float inicio = -270;
        float fin;
        fin = (initialValue / maxValue) * 360 - 270;
        g.fillArc(x, yg, 80, 80, inicio, fin);
        g.setColor(Color.black);
        g.drawOval(x, yg, 80, 80);
        //Círculo con el nivel
        g.setColor(colors[1]);
        g.fillOval(x + 10, yg + 10, 60, 60);
        g.setColor(Color.black);
        g.drawOval(x + 10, yg + 10, 60, 60);
        g.setColor(Color.white);
        g.drawString(Integer.toString(middleNumber), x + middleNumberXOffset, yg + 30);
    }

    @Override
    public void dibujar(Partida p, Color c, Input input, Graphics g) {
        super.dibujar(p, c, input, g);
        if (alentar) {
            g.setColor(Color.green);
            int alcancex = 2 * (200 + anchura / 2), alcancey = 2 * (200 + altura / 2);
            g.drawOval(x - alcancex / 2, y - alcancey / 2, alcancex, alcancey);
        }
    }

    public static void checkToGainExperience(Partida p, ElementoAtacante atacante, float xpValue, float destroyedElementX, float destroyedElementY, float destroyedElementHeight) {
        if (atacante instanceof Manipulador) {
            Manipulador m = (Manipulador) atacante;
            VentanaPrincipal.mapac.anadir_mensaje(new Mensaje("+" + xpValue, Color.orange, destroyedElementX, destroyedElementY - destroyedElementHeight / 2 - 20, 2f));
            m.aumentar_experiencia(xpValue);
        } else if (Manipulador.lider && p.jugador_aliado(atacante).raza.equals(RaceNameEnum.MAESTROS.getName())) {
            for (Unidad u : p.jugador_aliado(atacante).unidades) {
                if (u.nombre.equals("Manipulador")) {
                    Manipulador m = (Manipulador) u;
                    VentanaPrincipal.mapac.anadir_mensaje(new Mensaje("+" + xpValue, Color.orange, destroyedElementX, destroyedElementY - destroyedElementHeight / 2 - 20, 2f));
                    m.aumentar_experiencia(xpValue);
                    break;
                }
            }
        }
    }

    private void sortSkillButtons() {
        this.botones = this.botones
                .stream().sorted((b1, b2) -> !b1.isPassiveAbility && b2.isPassiveAbility ? -1
                        : b1.isPassiveAbility && !b2.isPassiveAbility? 1 : 0).collect(Collectors.toList());
        this.initButtonKeys();
    }

}
