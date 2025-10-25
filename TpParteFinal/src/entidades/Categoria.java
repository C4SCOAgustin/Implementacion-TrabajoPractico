package entidades;

public enum Categoria {
	
	INICIAL, TECNICO, EXPERTO;
	
	public static boolean contains(String valor) {
		
		for(Categoria c:Categoria.values()) {
			
			if(c.toString().equals(valor.toUpperCase())) {
				
				return true;
			}
		}
		
		return false;
	}

}
