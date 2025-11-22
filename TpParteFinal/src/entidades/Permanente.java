package entidades;

public class Permanente extends Empleado {
	//DATOS-ATRIBUTOS
	private String categoria;
	
	//MÉTODOS-OPERACIONES	
	//Constructor
	public Permanente(String nombre, double valor, String categoria) {	
		super(nombre, valor, "PERMANENTE");
		
		if (!(categoria.equalsIgnoreCase("INICIAL") || categoria.equalsIgnoreCase("TECNICO") || categoria.equalsIgnoreCase("EXPERTO"))) {			
			throw new IllegalArgumentException("la categoria del empleado" + super.retornarLegajo() + " es invalida.");
		}
		
		this.categoria = categoria;
	}
	
	//Retorna el tipo de contrato del empleado (PERMANENTE) como un enum cadena (String)
	@Override
	public String retornarTipoContrato() {	
		return super.retornarTipoContrato();
	}
	
	@Override
	public double calcularCosto(double dias) {
		if (dias <= 0) {			
			throw new IllegalArgumentException("La cantidad de días ingresada es invalida.");
		}
		
	    double valorFinal = dias * super.retornarValor() * 1.02;
	    return valorFinal;
	}
	
	@Override
	public double calcularCostoConRetraso(double dias) {
		if (dias <= 0) {			
			throw new IllegalArgumentException("La cantidad de días ingresada es invalida.");
		}
		
	    double valorFinal = dias * super.retornarValor();
	    return valorFinal;
	}
	
	//Retorna la categoria del empleado de planta permanente como una cadena (String).
	public String retornarCategoria() {	
		return categoria;
	}
	
	@Override
	public String toString() {
		StringBuilder cadena = new StringBuilder();
		cadena.append(super.toString());
		cadena.append("Tipo contrato: " + retornarTipoContrato() + "\n");
		cadena.append("Categoria: " + categoria + "\n");
		return cadena.toString();
	}
}
