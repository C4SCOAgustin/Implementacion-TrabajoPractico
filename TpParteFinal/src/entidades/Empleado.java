package entidades;

public abstract class Empleado {
	
	//DATOS-ATRIBUTOS
	
	private final String nombre;
	private final double valor;
	private static int ultimoNumeroLegajo = 1;
	private final int numeroLegajo;
	private boolean estaDisponible;
	private int retrasos;
	
	//INVARIANTE DE REPRESENTACIÓN
	
	//el empleado no puede estar en más de una tarea.
	//nombre no puede ser null ni vacio.
	//tipoContrato debe ser "permanente" o "temporal".
	//numeroLegajo no puede ser null ni vacio.
	//valorHora debe ser mayor a 0.
	//los retrasos no pueden ser menos a 0.
	
	//MÉTODOS-OPERACIONES
	
	//Constructor.
	public Empleado(String nombre, double valor) {
		
		this.nombre = nombre;
		this.valor = valor;
		ultimoNumeroLegajo ++;
		numeroLegajo = ultimoNumeroLegajo;
	}
	
	public abstract TipoContrato retornarTipoContrato();
	
	//Ocupa al empleado.
	public void ocuparEmpleado() {
		
		estaDisponible = false;
	}
	
	//Desocupa al empleado.
	public void desocuparEmpleado() {
		
		estaDisponible = true;
	}
	
	//añade 1 retraso al contador de retrasos del empleado (void).
	public void añadirRetraso() {
		
		retrasos ++;
	}
	
	//Retorna el nombre del empleado como una cadena (String).
	public String retornarNombre() {
		
		return nombre;
	}
	
	//Retorna el valor del trabajo del empleado como un decimal (double).
	public double retornarValor() {
		
		return valor;
	}
	
	//Retorna el número de legajo del empleado (int).
	public int retornarLegajo() {
			
		return numeroLegajo;
	}
		
	//Retorna la disponibilidad del empleado en verdadero si está disponible o falso si no lo está (boolean).
	public boolean retornarDisponibilidad() {
		
		return estaDisponible;
	}
	
	//Retorna la cantidad de veces que el empleado se retraso en la entrega de tareas como un número (int).
	public int retornarRetrasos() {
	
		return retrasos;
	}
	
	
	
	
	
	
}
