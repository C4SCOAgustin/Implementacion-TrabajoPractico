package entidades;

import java.time.LocalDate;

public class Tarea {
	//DATOS-ATRIBUTOS 	
	private final String titulo; 
	private String descripcion;
	private double diasNecesarios;
	private double mediosDiasNecesarios;
	private Integer empleadoResponsable;
	private double diasRetraso;
	private double mediosDiasRetraso;
	private boolean finalizada;
	private LocalDate diaInicioActividad;

	//MÉTODOS-OPERACIONES
	//Constructor.
	public Tarea(String titulo, String descripcion, double diasNecesarios) {
		if (titulo == null || titulo == "") {
			throw new IllegalArgumentException("El título de la tarea no puede ser vacio.");
		}
		
//		if (descripcion == null || descripcion == "") {
//			throw new IllegalArgumentException("La descripción de la tarea no puede ser vacia.");
//		}
		
		if (diasNecesarios <= 0) {
			throw new IllegalArgumentException("Los días necesarios la tarea no pueden menores o iguales a 0.");
		}
			
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.diasNecesarios = diasNecesarios;
		diasRetraso = 0;
		mediosDiasRetraso = 0;
		mediosDiasNecesarios = 0;
		
		if (diasNecesarios%1 == 0.5) {
			this.diasNecesarios -= 0.5;
			mediosDiasNecesarios ++;
		}
	}
	
	//Asigna un número de legajo relacionado a un empleado como responsable de la tarea (void).
	public void asignarEmpleado(Integer nLegajo) {
		if (this.retornarFinalizada()) {
			throw new IllegalArgumentException("La tarea " + this.retornarTitulo() + "está finalizada.");
		}
		
		diaInicioActividad = LocalDate.now();
		this.empleadoResponsable = nLegajo;
	}
	
	//Añade días de retraso a la tarea y retorna al empleadoResponsable (Integer).
	public Integer registrarRetraso(double añadirRetraso) {
		if (this.retornarFinalizada()) {
			throw new IllegalArgumentException("La tarea " + this.retornarTitulo() + "está finalizada");
		}
		
		if (añadirRetraso <= 0) {			
			throw new IllegalArgumentException("La cantidad de días de retraso ingresada es invalida.");
		}
			
		diasRetraso += añadirRetraso;
		
		if (añadirRetraso % 1 == 0.5) {
			diasRetraso -= 0.5;
			mediosDiasRetraso ++;
		}
		
		return empleadoResponsable;
	}
	
	//Finaliza la tarea y el número de legajo del empleado responsable o -1 si no hay ningun empleado (Integer)
	public Integer finalizarTarea() {
		if (finalizada) {
	        throw new IllegalArgumentException("La tarea " + titulo + " está finalizada.");
		}
		
		if (empleadoResponsable == null || empleadoResponsable == 0) {
			throw new IllegalArgumentException("La tarea " + titulo + " no tiene ningun empleado asignado.");
		}
		
		finalizada = true;
		Integer leg = empleadoResponsable;
		empleadoResponsable = null;
		return leg;
	}
	
	public Integer reasignarEmpleado(Integer empleadoNuevo) {
		if (finalizada) {
	        throw new IllegalArgumentException("La tarea " + titulo + " está finalizada.");
		}
		
		if (empleadoResponsable == null || empleadoResponsable == 0) {
			throw new IllegalArgumentException("La tarea " + titulo + " no tiene ningun empleado asignado.");
		}
		
		Integer empleadoAntiguo = empleadoResponsable;
		empleadoResponsable = empleadoNuevo;
		diaInicioActividad = LocalDate.now();
		return empleadoAntiguo;
	}
	
	//Retorna el título de la tarea como una cadena(String).
	public String retornarTitulo() {		
		return titulo;
	}
	
	//Retorna el número de legajo del empleado responsable de la tarea (Integer).
	public Integer retornarEmpleadoResponsable() {
		return empleadoResponsable;
	}
	
	//Retorna los días necesarios para la finalización de la tarea como un decimal (double).
	public double retornarDiasNecesarios() {
		return diasNecesarios + diasRetraso + mediosDiasNecesarios + mediosDiasRetraso;
	}
	
	public double retornarMediosDiasNecesarios() {
		return mediosDiasNecesarios + mediosDiasRetraso;
	}
	
	//retorna los días de retraso de la tarea como decimal (double)
	public double retornarDiasRetraso() {
		return diasRetraso;
	}
	
	//Retorna el estado de la tarea en verdadero si está terminada o falso si no lo está (boolean).
	public boolean retornarFinalizada() {		
		return finalizada;
	}
	
	public LocalDate retornarDiaInicioActividad() {
		return diaInicioActividad;
	}
	
	public String toString() {
		return this.titulo;
	}

}