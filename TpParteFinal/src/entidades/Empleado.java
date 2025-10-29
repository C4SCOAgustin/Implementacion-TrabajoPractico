package entidades;

public abstract class Empleado {	
	//DATOS-ATRIBUTOS
	private final String nombre;
	private final double valor;
	private static int ultimoNumeroLegajo = 0;
	private final int numeroLegajo;
	private boolean estaDisponible;
	private int retrasos;
	
	//MÉTODOS-OPERACIONES	
	//Constructor.
	public Empleado(String nombre, double valor) {	
		this.nombre = nombre;
		this.valor = valor;
		ultimoNumeroLegajo ++;
		numeroLegajo = ultimoNumeroLegajo;
		estaDisponible = true;
		retrasos = 0;
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
	
	@Override
	public String toString() {
		return nombre;
	}
}
