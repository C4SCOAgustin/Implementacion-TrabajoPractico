package entidades;

public enum TipoContrato {

	PERMANENTE, CONTRATADO;
	
	public static boolean contains(String valor) {
		
		for(TipoContrato t:TipoContrato.values()) {
			
			if(t.toString().equals(valor.toUpperCase())) {
				
				return true;
			}
		}
		
		return false;
	}
}
