package entidades;

public class Tarea {

	//DATOS-ATRIBUTOS 
	
	private String titulo; 
	private String descripcion;
	private double diasNecesarios;
	private int empleadoResponsable;
	private double diasRetraso;
	private boolean estaFinalizada;
	
	//INVARIANTE DE REPRESENTACIÓN

	//titulo no puede ser null ni vacio.
	//descripcion no puede ser null ni vacio.
	//empleadoResponsable debe pertenecer al diccionario de empleados.
	//diasNecesarios debe ser entero o multiplo de 1/2.
	//diasNecesarios debe ser mayor a 0.
	//diasRetraso debe ser entero o multiplo de 1/2.
	//diasRetraso debe ser mayor a 0.

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
	
	//Añade días de retraso a la tarea y el empleadoResponsable (int).
	public int registrarRetraso(double añadirRetraso) {
		
		return empleadoResponsable;
	}
	
	//Finaliza la tarea y devuelve verdadero si pudo hacerlo o falso si ya estaba finalizada (boolean)
	public boolean finalizarTarea() {
		
	}
	
	//Retorna el título de la tarea como una cadena(String).
	public String retornarTitulo() {
		
	}
	
	//Retorna el número de legajo del empleado responsable de la tarea (int).
	public int retornarEmpleadoResponsable() { //Devuelve empleadoResponsable de Tarea.
	
	}
	
	//Retorna los días necesarios para la finalización de la tarea como un decimal (double).
	public double retornarDiasNecesarios() { //Devuelve diasNecesarios de Tarea.
		
	}
	
	//Retorna los días de retraso de la tarea como un decimal (double).
	public double retornarDiasRetraso() { //Devuelve diasRetraso de Tarea.
		
	}
	
	//Retorna el estado de la tarea en verdadero si está terminada o falso si no lo está (boolean).
	public boolean retornarEstadoTarea() {
		
	}
}
