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
	
	//Retorna la categoria del empleado de planta permanente como una cadena (String).
	public String retornarCategoria() {	
		return categoria;
	}
}
