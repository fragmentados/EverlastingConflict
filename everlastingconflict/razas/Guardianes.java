/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.razas;

import everlastingconflict.elementosvisuales.BotonComplejo;
import everlastingconflict.elementos.implementacion.Edificio;
import everlastingconflict.elementos.implementacion.Habilidad;
import everlastingconflict.elementos.implementacion.Taller;
import everlastingconflict.elementos.implementacion.Tecnologia;
import everlastingconflict.elementos.implementacion.Unidad;
import everlastingconflict.gestion.Evento;

public class Guardianes {

    public static boolean ruedas_mejoradas = false;
    public static int maximo_felicidad = 100;
    public static int recursos_por_segundo = 20;
    public static boolean habilidad_enviado = false;
    public static boolean armas_biblicas = false;

    //Vehículos
    private static void vehiculo(Unidad u) {
        u.vehiculo = true;
        u.movil = false;
        u.hostil = false;
        if (Guardianes.ruedas_mejoradas) {
            u.velocidad += 0.3f;
        }
    }

    public static final void Patrulla(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 10;
        u.defensa = Unidad.defensa_estandar + 4;
        u.vida_max = Unidad.vida_estandar + 150;
        u.alcance = Unidad.alcance_estandar - 50;
        u.cadencia = Unidad.cadencia_estandar + 2;
        u.velocidad = Unidad.velocidad_estandar + 0.2f;
        u.vision = Unidad.vision_estandar - 100;
        u.coste = 30;
        u.tiempo = Unidad.tiempo_estandar;
        u.descripcion = "Vehículo básico capaz de elminar pequeñas amenazas.";
        Guardianes.vehiculo(u);
        u.coste_alternativo = 1;
    }

    public static final void Tanque(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 30;
        u.defensa = Unidad.defensa_estandar + 6;
        u.vida_max = Unidad.vida_estandar + 250;
        u.alcance = Unidad.alcance_estandar + 50;
        u.cadencia = Unidad.cadencia_estandar + 2.2f;
        u.velocidad = Unidad.velocidad_estandar;
        u.vision = Unidad.vision_estandar - 120;
        u.coste = 60;
        u.tiempo = Unidad.tiempo_estandar + 5;
        u.descripcion = "Vehículo medio de velocidad media y buena resistencia. Ataque potente pero de alta cadencia.";
        Guardianes.vehiculo(u);
        u.coste_alternativo = 2;
    }

    public static final void Acribillador(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 15;
        u.defensa = Unidad.defensa_estandar + 4;
        u.vida_max = Unidad.vida_estandar + 150;
        u.alcance = Unidad.alcance_estandar + 50;
        u.cadencia = Unidad.cadencia_estandar - 1f;
        u.velocidad = Unidad.velocidad_estandar + 0.2f;
        u.vision = Unidad.vision_estandar - 100;
        u.coste = 60;
        u.tiempo = Unidad.tiempo_estandar + 2;
        u.descripcion = "Vehículo ligero y rápido con un arma de baja potencia pero alta cadencia.";
        Guardianes.vehiculo(u);
        u.coste_alternativo = 2;
    }

    public static final void Destructor(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 45;
        u.defensa = Unidad.defensa_estandar + 8;
        u.vida_max = Unidad.vida_estandar + 450;
        u.alcance = Unidad.alcance_estandar + 75;
        u.cadencia = Unidad.cadencia_estandar + 2.4f;
        u.velocidad = Unidad.velocidad_estandar - 0.2f;
        u.vision = Unidad.vision_estandar - 120;
        u.coste = 90;
        u.tiempo = Unidad.tiempo_estandar + 7;
        u.descripcion = "Vehículo con la mayor potencia de fuego del arsenal de los Guardianes. Posee dos armas: una ametralladora pesada y un cañón penetrador.";
        Guardianes.vehiculo(u);
        u.coste_alternativo = 3;
    }

    //Pilotos
    private static void piloto(Unidad u) {
        u.hostil = false;
        u.ataque = 0;
        u.defensa = Unidad.defensa_estandar + 4;
        u.vida_max = Unidad.vida_estandar + 150;
        u.alcance = Unidad.alcance_estandar - 50;
        u.cadencia = Unidad.cadencia_estandar + 2;
        u.velocidad = Unidad.velocidad_estandar + 0.2f;
        u.vision = Unidad.vision_estandar - 100;
        u.tiempo = Unidad.tiempo_estandar;
        u.piloto = true;
        u.coste_alternativo = 1;
    }

    public static final void Artillero(Unidad u) {
        Guardianes.piloto(u);
        u.coste = 20;
        u.descripcion = "Piloto encargado de aumentar la capacidad ofensiva del arma principal del vehículo que maneja.Torreta mejorada: Torreta demoledora.";

    }

    public static final void Explorador(Unidad u) {
        Guardianes.piloto(u);
        u.coste = 20;
        u.descripcion = "Piloto encargado de aumentar la visión del vehículo que maneja. Torreta mejorada: Estación de vigilancia.";
    }

    public static final void Corredor(Unidad u) {
        Guardianes.piloto(u);
        u.coste = 30;
        u.descripcion = "Piloto encargado de aumentar la velocidad del vehículo que maneja. Torreta mejorada: Distorsionador temporal.";
    }

    public static final void Ingeniero(Unidad u) {
        Guardianes.piloto(u);
        u.coste = 30;
        u.descripcion = "Piloto encargado de aumentar la vida del vehículo que maneja. Torreta mejorada: Estación reparadora.";
    }

    public static final void Armero(Unidad u) {
        Guardianes.piloto(u);
        u.coste = 30;
        u.descripcion = "Piloto encargado de reducir la cadencia del arma principal del vehículo que maneja. Torreta mejorada: Ametralladora.";
    }

    public static final void Amparador(Unidad u) {
        Guardianes.piloto(u);
        u.coste = 40;
        u.descripcion = "Piloto encargado de aumentar la capacidad defensiva del vehículo que maneja. Torreta mejorada: Muro.";
    }

    public static final void Oteador(Unidad u) {
        Guardianes.piloto(u);
        u.coste = 50;
        u.descripcion = "Piloto encargado de aumentar el alcance del arma principal del vehículo que maneja. Torreta mejorada: Torreta artillería.";
    }

    public static final void Pacificador(Unidad u) {
        u.ataque = Unidad.ataque_estandar;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar;
        u.alcance = Unidad.alcance_estandar;
        u.cadencia = Unidad.cadencia_estandar;
        u.velocidad = Unidad.velocidad_estandar + 0.2f;
        u.vision = Unidad.vision_estandar;
        u.coste = 50;
        u.tiempo = Unidad.tiempo_estandar + 3;
        u.descripcion = "Unidad sagrada capaz de inhabilitar la capacidad ofensiva del enemigo.";
        if (Guardianes.armas_biblicas) {
            u.ataque += 2;
        }
        u.coste_alternativo = 1;
    }

    public static final void Silenciador(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 60;
        u.defensa = Unidad.defensa_estandar;
        u.vida_max = Unidad.vida_estandar - 20;
        u.alcance = Unidad.alcance_estandar + 50;
        u.cadencia = Unidad.cadencia_estandar + 0.2f;
        u.velocidad = Unidad.velocidad_estandar;
        u.vision = Unidad.vision_estandar;
        u.coste = 80;
        u.tiempo = Unidad.tiempo_estandar + 5;
        u.descripcion = "Unidad sagrada capaz de inhabilitar el uso de habilidades del enemigo.";
        if (Guardianes.armas_biblicas) {
            u.ataque += 2;
        }
        u.coste_alternativo = 2;
    }

    public static final void Inquisidor(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 20;
        u.defensa = Unidad.defensa_estandar + 2;
        u.vida_max = Unidad.vida_estandar + 150;
        u.alcance = Unidad.alcance_estandar - 90;
        u.cadencia = Unidad.cadencia_estandar + 0.3f;
        u.velocidad = Unidad.velocidad_estandar + 0.5f;
        u.vision = Unidad.vision_estandar - 100;
        u.coste = 90;
        u.tiempo = Unidad.tiempo_estandar + 7;
        u.descripcion = "Unidad sagrada capaz de recibir gran cantidad de castigo antes de morir.";
        if (Guardianes.armas_biblicas) {
            u.ataque += 2;
        }
        u.coste_alternativo = 2;
    }

    public static final void Enviado(Unidad u) {
        u.ataque = Unidad.ataque_estandar + 30;
        u.defensa = Unidad.defensa_estandar + 2;
        u.vida_max = Unidad.vida_estandar + 250;
        u.alcance = Unidad.alcance_estandar + 50;
        u.cadencia = Unidad.cadencia_estandar + 0.5f;
        u.velocidad = Unidad.velocidad_estandar + 1f;
        u.vision = Unidad.vision_estandar - 50;
        u.coste = 110;
        u.tiempo = Unidad.tiempo_estandar + 8;
        u.descripcion = "Unidad voladora con habilidades curativas.";
        if (Guardianes.armas_biblicas) {
            u.ataque += 2;
        }
        u.coste_alternativo = 1;
    }

    public static final void Activador(Unidad u) {
        Guardianes.piloto(u);
        u.coste = 50;
        u.descripcion = "Unidad encargada de activar los edificios de los Guardianes.";
        u.piloto = false;
        u.constructor = true;        
    }

    public static final void Anexo(Edificio e) {
        e.vida_max = Edificio.vida_estandar;
        e.vision = Edificio.vision_estandar;
        e.coste = 20;
        e.tiempo = 5;
        e.descripcion = "Edificio encargado de almacenar los vehículos de los Guardianes.";
    }

    public static final void Taller(Edificio e) {        
        e.vida_max = Edificio.vida_estandar;
        e.vision = Edificio.vision_estandar;        
        ((Taller) e).maximo_anexos = 5;
        e.descripcion = "Edificio encargado de producir los vehículos de los Guardianes.";
        e.constructor = true;
    }

    public static final void Pilotos(Edificio e) {        
        e.vida_max = Edificio.vida_estandar;
        e.vision = Edificio.vision_estandar;        
        e.descripcion = "Edificio encargado de entrenar a los pilotos de los Guardianes.";
    }

    public static final void Ayuntamiento(Edificio e) {        
        e.vida_max = Edificio.vida_estandar;
        e.vision = Edificio.vision_estandar;        
        e.descripcion = "Edificio encargado de gestionar los eventos positivos y negativos de los Guardianes.";
        e.constructor = true;
        e.radio_construccion = 1500f;
        e.activo = true;
    }

    public static final void Templo(Edificio e) {        
        e.vida_max = Edificio.vida_estandar;
        e.vision = Edificio.vision_estandar;        
        e.descripcion = "Edificio encargado de entrenar a las unidades sagradas de los Guardianes.";
    }

    public static final void Laboratorio(Edificio e) {        
        e.vida_max = Edificio.vida_estandar;
        e.vision = Edificio.vision_estandar;        
        e.descripcion = "Edificio encargado de desarrollar las mejoras de los vehículos de Guardianes.";
    }

    public static final void Vaticano(Edificio e) {        
        e.vida_max = Edificio.vida_estandar;
        e.vision = Edificio.vision_estandar;        
        e.descripcion = "Edificio encargado de desarrollar las mejoras de las unidades sagradas de los Guardianes.";
    }

    public static final void Gubernamental(Edificio e) {        
        e.vida_max = Edificio.vida_estandar;
        e.vision = Edificio.vision_estandar;        
        e.descripcion = "Edificio encargado de desarrollar las mejoras económicas de los Guardianes.";
    }

    public static final void Torreta(Edificio e) { 
        e.ataque = Unidad.ataque_estandar + 20;
        e.cadencia = Unidad.cadencia_estandar + 0.5f;
        e.alcance = Unidad.alcance_estandar;
        e.defensa = Unidad.defensa_estandar + 2;
        e.vida_max = Edificio.vida_estandar;
        e.vision = Edificio.vision_estandar;
        e.coste = 50;
        e.tiempo = 15;
        e.descripcion = "Edificio defensivo capaz de ser personalizado al introducir en él pilotos.";
        e.coste_alternativo = 2;
        e.hostil = true;        
    }

    public static void iniciar_botones_unidad(Unidad u) {
        switch (u.nombre) {
            case "Pacificador":
                u.botones.add(new BotonComplejo(new Habilidad("Pacifismo")));
                break;
            case "Silenciador":
                u.botones.add(new BotonComplejo(new Habilidad("Silencio")));
                break;
            case "Inquisidor":
                u.botones.add(new BotonComplejo(new Habilidad("Defensa férrea")));
                break;
            case "Enviado celeste":
                u.botones.add(new BotonComplejo(new Habilidad("Intervención divina")));
                if (!Guardianes.habilidad_enviado) {
                    u.botones.get(u.botones.size() - 1).activado = false;
                }
                break;

        }
    }

    public static void iniciar_botones_edificio(Edificio e) {
        switch (e.nombre) {
            case "Taller bélico":
                e.botones.add(new BotonComplejo(new Edificio("Anexo")));
                e.botones.add(new BotonComplejo(new Unidad("Patrulla")));
                e.botones.add(new BotonComplejo(new Unidad("Acribillador")));
                e.botones.add(new BotonComplejo(new Unidad("Tanque")));
                e.botones.add(new BotonComplejo(new Unidad("Destructor")));
                break;
            case "Academia de pilotos":
                e.botones.add(new BotonComplejo(new Unidad("Artillero")));
                e.botones.add(new BotonComplejo(new Unidad("Explorador")));
                e.botones.add(new BotonComplejo(new Unidad("Corredor")));
                e.botones.add(new BotonComplejo(new Unidad("Ingeniero")));
                e.botones.add(new BotonComplejo(new Unidad("Armero")));
                e.botones.add(new BotonComplejo(new Unidad("Amparador")));
                e.botones.add(new BotonComplejo(new Unidad("Oteador")));
                break;
            case "Ayuntamiento":
                e.botones.add(new BotonComplejo(new Unidad("Activador")));
                e.botones.add(new BotonComplejo(new Edificio("Torreta defensiva")));
                e.botones.add(new BotonComplejo(new Evento("Decorar los parques")));
                e.botones.add(new BotonComplejo(new Evento("Construir colegios")));
                e.botones.add(new BotonComplejo(new Evento("Construir bloques de viviendas")));
                e.botones.add(new BotonComplejo(new Evento("Sufragio universal")));
                break;
            case "Templo":
                e.botones.add(new BotonComplejo(new Unidad("Pacificador")));
                e.botones.add(new BotonComplejo(new Unidad("Silenciador")));
                e.botones.add(new BotonComplejo(new Unidad("Inquisidor")));
                e.botones.add(new BotonComplejo(new Unidad("Enviado celeste")));
                break;
            case "Edificio gubernamental":
                e.botones.add(new BotonComplejo(new Tecnologia("Reforma gubernamental")));
                e.botones.add(new BotonComplejo(new Tecnologia("Propaganda positiva")));
                e.botones.add(new BotonComplejo(new Tecnologia("Cobradores de impuestos")));
                e.botones.add(new BotonComplejo(new Tecnologia("Aumentar nivel de amenaza1")));
                e.botones.add(new BotonComplejo(new Tecnologia("Ley marcial")));
                break;
            case "Laboratorio de I+D":
                e.botones.add(new BotonComplejo(new Tecnologia("Ruedas mejoradas")));
                break;
            case "Vaticano":
                e.botones.add(new BotonComplejo(new Tecnologia("Intervención divina")));
                e.botones.add(new BotonComplejo(new Tecnologia("Armas bíblicas")));
                break;
        }
    }
}
