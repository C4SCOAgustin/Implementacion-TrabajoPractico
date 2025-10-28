package entidades;

public class Tarea {
	//DATOS-ATRIBUTOS 	
	private String titulo; 
	private String descripcion;
	private double diasNecesarios;
	private int empleadoResponsable;
	private double diasRetraso;
	private boolean finalizada;

	//MÉTODOS-OPERACIONES
	//Constructor.
	public Tarea(String titulo, String descripcion, double diasNecesarios) {		
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.diasNecesarios = diasNecesarios;	
	}
	
	//Asigna un número de legajo relacionado a un empleado como responsable de la tarea (void).
	public void asignarEmpleado(int nLegajo) {	
		this.empleadoResponsable = nLegajo;
	}
	
	//Añade días de retraso a la tarea y retorna al empleadoResponsable (int).
	public int registrarRetraso(double añadirRetraso) {	
		return empleadoResponsable;
	}
	
	//Finaliza la tarea y devuelve verdadero si pudo hacerlo o falso si ya estaba finalizada (boolean)
	public boolean finalizarTarea() {	
		if (finalizada) {
		        return false;
		    }
		
		finalizada = true; 
		return true; 
	}
	
	//Retorna el título de la tarea como una cadena(String).
	public String retornarTitulo() {		
		return titulo;
	}
	
	//Retorna el número de legajo del empleado responsable de la tarea (int).
	public int retornarEmpleadoResponsable() {	
		return empleadoResponsable;
	}
	
	//Retorna los días necesarios para la finalización de la tarea como un decimal (double).
	public double retornarDiasNecesarios() {
		return diasNecesarios;
	}
	
	//Retorna los días de retraso de la tarea como un decimal (double).
	public double retornarDiasRetraso() {
		return diasRetraso;
	}
	
	//Retorna el estado de la tarea en verdadero si está terminada o falso si no lo está (boolean).
	public boolean retornarFinalizada() {		
		return finalizada;
	}
	
	public String toString() {
		return this.titulo;
	}
}