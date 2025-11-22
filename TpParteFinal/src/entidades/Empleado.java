package entidades;

public abstract class Empleado {	
	//DATOS-ATRIBUTOS
	private final String nombre;
	private final double valor;
	private String tipoContrato;
	private static int ultimoNumeroLegajo = 0;
	private final int numeroLegajo;
	private boolean estaDisponible;
	private int retrasos;
	
	//MÉTODOS-OPERACIONES	
	//Constructor.
	public Empleado(String nombre, double valor, String tipoContrato) {
		if (nombre == null || nombre.equals("")) {			
			throw new IllegalArgumentException("El nombre ingresado es invalido.");
		}	
		
		if (valor <= 0) {			
			throw new IllegalArgumentException("El valor ingresado es invalido.");
		}
		
		if (!(tipoContrato.equalsIgnoreCase("CONTRATADO") || tipoContrato.equalsIgnoreCase("PERMANENTE"))) {			
			throw new IllegalArgumentException("El tipo de contrato del empleado es invalido.");
		}
		
		this.nombre = nombre;
		this.valor = valor;
		this.tipoContrato = tipoContrato;
		ultimoNumeroLegajo ++;
		numeroLegajo = ultimoNumeroLegajo;
		estaDisponible = true;
		retrasos = 0;
	}
	
	//Método abstracto que retorna el tipo de contrato.
	public String retornarTipoContrato() {
		return tipoContrato;
	}
	
	public abstract double calcularCosto(double dias);
	
	public abstract double calcularCostoConRetraso(double dias);
	
	//Ocupa al empleado.
	public void ocuparEmpleado() {
		if (!estaDisponible) {
			throw new IllegalArgumentException("El empleado " + numeroLegajo + " ya esta ocupado");
		}
		
		estaDisponible = false;
	}
	
	//Desocupa al empleado.
	public void desocuparEmpleado() {
		if (estaDisponible) {
			throw new IllegalArgumentException("El empleado " + numeroLegajo + " ya esta desocupado");
		}
		
		estaDisponible = true;
	}	
	//añade 1 retraso al contador de retrasos del empleado (void).
	public void añadirRetraso() {
		if (retrasos < 0) {
			throw new IllegalArgumentException("El empleado " + numeroLegajo + " tiene una cantidad de retrasos invalidos");
		}
		
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
		StringBuilder cadena = new StringBuilder();
		cadena.append("Nombre: " + nombre + "\n");
		cadena.append("Legajo: " + numeroLegajo + "\n");
		cadena.append("Retrasos: " + retrasos + "\n");
		
		if (estaDisponible) {
			cadena.append("Disponibilidad: Disponible" + "\n");
		}
		
		else {
			cadena.append("Disponibilidad: Ocupado" + "\n");
		}
		
		cadena.append("Valor: " + valor + "\n");
		
		return cadena.toString();
	}	
}
