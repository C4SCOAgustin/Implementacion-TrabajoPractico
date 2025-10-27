package entidades;

import java.util.Objects;

public class Tupla <T1,T2>{
    private T1 valor1;
    private T2 valor2;

    public Tupla(T1 valor1, T2 valor2) {
        this.valor1 = valor1;
        this.valor2 = valor2;
    }

    public T1 getValor1() {
        return valor1;
    }

    public void setValor1(T1 valor1) {
        this.valor1 = valor1;
    }

    public T2 getValor2() {
        return valor2;
    }

    public void setValor2(T2 valor2) {
        this.valor2 = valor2;
    }
    
    @Override
    public boolean equals(Object objeto) {
    	
    	if (this == objeto) {
    		return true;
    	}
    	
    	if (objeto == null || this.getClass() != objeto.getClass()) {
    		return false;
    	}
    	
    	Tupla<Integer, String> objetoTupla = (Tupla<Integer, String>) objeto;
    	
    	return Objects.equals(this.valor1, objetoTupla.valor1) &&
    			Objects.equals(this.valor2, objetoTupla.valor2);
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(this.valor1, this.valor2);
    }
}
