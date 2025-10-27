package entidades;

public class Contratado extends Empleado {
	//DATOS-ATRIBUTOS
	
	//MÉTODOS-OPERACIONES
	//Constructor
	public Contratado(String nombre, double valor) {	
		super(nombre, valor);
	}
	
	//Retorna el tipo de contrato del empleado (CONTRATADO) como un enum cadena (String)
	@Override
	public TipoContrato retornarTipoContrato() {		
		return TipoContrato.CONTRATADO;
	}
}
