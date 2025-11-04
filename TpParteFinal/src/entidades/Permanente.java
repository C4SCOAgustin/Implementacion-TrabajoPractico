package entidades;

public class Permanente extends Empleado {
	//DATOS-ATRIBUTOS
	private String categoria;
	
	//MÉTODOS-OPERACIONES	
	//Constructor
	public Permanente(String nombre, double valor, String categoria) {	
		super(nombre, valor);	
		this.categoria = categoria;
	}
	
	//Retorna el tipo de contrato del empleado (PERMANENTE) como un enum cadena (String)
	@Override
	public TipoContrato retornarTipoContrato() {		
		return TipoContrato.PERMANENTE;
	}
	
	@Override
	public double calcularCosto(double dias) {		
	    double valorFinal = Math.round(dias) * super.retornarValor() * 1.02;
	    return valorFinal;
	}
	
	@Override
	public double calcularCostoConRetraso(double dias) {		
	    double valorFinal = Math.round(dias) * super.retornarValor();
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
