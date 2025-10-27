package entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HomeSolution implements IHomeSolution {
	HashMap<Integer, Empleado> empleados = new HashMap<>();
	HashMap<Integer, Proyecto> proyectos = new HashMap<>();

	@Override
	public void registrarEmpleado(String nombre, double valor) throws IllegalArgumentException {
		if (nombre == null || nombre.equals("")) {			
			throw new IllegalArgumentException("El nombre ingresado es invalido.");
		}	
		
		if (valor <= 0) {			
			throw new IllegalArgumentException("El valor ingresado es invalido.");
		}
		
		Empleado e = new Contratado(nombre, valor);
		empleados.put(e.retornarLegajo(), e);
	}

	@Override
	public void registrarEmpleado(String nombre, double valor, String categoria) throws IllegalArgumentException {		
		if (nombre == null || nombre.equals("")) {
			throw new IllegalArgumentException("El nombre ingresado es invalido.");
		}
		
		if (valor <= 0) {
			throw new IllegalArgumentException("El valor ingresado es invalidos.");
		}
		
		if (!Categoria.contains(categoria)) {
			throw new IllegalArgumentException("La categoria ingresada es invalida.");
		}
		
		Empleado e = new Permanente(nombre, valor, categoria);
		empleados.put(e.retornarLegajo(), e);		
	}

	@Override
	public void registrarProyecto(String[] titulos, String[] descripciones, double[] dias, String domicilio,
			String[] datosCliente, String fechaInicio, String fechaEstimadaFin) throws IllegalArgumentException {
		if (titulos.length != descripciones.length || descripciones.length != dias.length) {
			throw new IllegalArgumentException("Deben ingresarse la misma cantidad de titulos, descripciones y días.");
		}
		
		if (titulos.length <= 0) {
			throw new IllegalArgumentException("Los títulos ingresados son invalidos.");
		}
		
		if (descripciones.length <= 0) {
			throw new IllegalArgumentException("Las descripciones ingresadas son invalidas.");
		}
		
		if (dias.length <= 0) {
			throw new IllegalArgumentException("Los días ingresados son invalidos.");
		}
		
		if (domicilio == null ||domicilio.equals("")) {
			throw new IllegalArgumentException("El domicilio ingresado es invalido.");
		}
		
		if (datosCliente.length != 3) {
			throw new IllegalArgumentException("Los datos del cliente ingresados son invalidos; Debe ingresar el nombre, email y telefono.");
		}
		
		if (fechaInicio == null || fechaInicio.equals("")) {
			throw new IllegalArgumentException("La fecha de inicio ingresada es invalida.");
		}
		
		if (fechaEstimadaFin == null || fechaEstimadaFin.equals("")) {
			throw new IllegalArgumentException("La fecha estimada de fin ingresada es invalida.");
		}
		
		Proyecto p = new Proyecto(titulos, descripciones, dias, domicilio, datosCliente, fechaInicio, fechaEstimadaFin);
		proyectos.put(p.retornarNumeroProyecto(), p);
	}

	@Override
	public void asignarResponsableEnTarea(Integer numero, String titulo) throws Exception {				
		if (!proyectos.containsKey(numero) || proyectos.get(numero).estaFinalizado()) {
			throw new Exception("El proyecto no existe o esta finalizado.");
		}
		
		for (Empleado e: empleados.values()) {		
			if (e.retornarDisponibilidad()) {		
				proyectos.get(numero).asignarEmpleadoATarea(titulo, e.retornarLegajo());
				e.ocuparEmpleado();
				return;
			}
		}
	}

	@Override
	public void asignarResponsableMenosRetraso(Integer numero, String titulo) throws Exception {		
		if (!proyectos.containsKey(numero) || proyectos.get(numero).estaFinalizado()) {
			throw new Exception("El proyecto no existe o esta finalizado.");
		}
		
		Empleado mejorEmpleado = obtenerMenosRetrasos();
		
		if (mejorEmpleado == null) {
			throw new Exception("No hay empleados disponibles.");
		}
		
		proyectos.get(numero).asignarEmpleadoATarea(titulo, mejorEmpleado.retornarLegajo());
	}

	@Override
	public void registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias)
			throws IllegalArgumentException {		
		if (!proyectos.containsKey(numero)) {
			throw new IllegalArgumentException("El proyecto no existe.");
		}
		
		if (cantidadDias <= 0) {
			throw new IllegalArgumentException("Los días ingresados deben ser mayores a 0.");
		}
		
		int empleadoResponsable = proyectos.get(numero).registrarRetrasoTarea(titulo, cantidadDias);
		empleados.get(empleadoResponsable).añadirRetraso();
	}

	@Override
	public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias)
			throws IllegalArgumentException {	
		if (!proyectos.containsKey(numero)) {
			throw new IllegalArgumentException("El proyecto no existe.");
		}
		
		if (titulo == null || titulo.equals("")) {
			throw new IllegalArgumentException("El título ingresado es invalido.");
		}
		
		if (descripcion == null || descripcion.equals("")) {
			throw new IllegalArgumentException("La descripción ingresada es invalida.");
		}
		
		if (dias <= 0) {
			throw new IllegalArgumentException("Los días ingresados deben ser mayores a 0.");
		}
		
		proyectos.get(numero).registrarTarea(titulo, descripcion, dias);
	}

	@Override
	public void finalizarTarea(Integer numero, String titulo) throws Exception {	
		Proyecto proyecto = proyectos.get(numero);
	    if (proyecto == null) {
	        throw new IllegalArgumentException("No existe el proyecto con número: " + numero);
	    }
	    
	    proyecto.finalizarTarea(titulo); 
	}

	@Override
	public void finalizarProyecto(Integer numero, String fin) throws IllegalArgumentException {		
		Proyecto proyecto = proyectos.get(numero);
		if (proyecto == null) {
			throw new IllegalArgumentException("No existe el proyecto con número: " + numero);
		}
		
		proyecto.finalizarProyecto();		
	}

	@Override
	public void reasignarEmpleadoEnProyecto(Integer numero, Integer legajo, String titulo) throws Exception {
		Proyecto proyecto = proyectos.get(numero);
	    
	    if (proyecto == null) {
	        throw new IllegalArgumentException("No existe el proyecto con número: " + numero);
	    }
	    
	    Tarea tarea = proyecto.retornarTareas().get(titulo);
	    
	    if (tarea == null) {
	        throw new IllegalArgumentException("No existe la tarea con título: " + titulo);
	    }
	    
	    int legajoAnterior = tarea.retornarEmpleadoResponsable();
	    
	    if (legajoAnterior == 0) {
	        throw new Exception("La tarea no tiene empleado asignado previamente.");
	    }
	    
	    Empleado empleadoNuevo = empleados.get(legajo);
	    
	    if (empleadoNuevo == null) {
	        throw new IllegalArgumentException("El empleado reemplazante no existe: " + legajo);
	    }
	    
	    tarea.asignarEmpleado(legajo);
	    
	    Empleado empleadoAnterior = empleados.get(legajoAnterior);
	    
	    if (empleadoAnterior != null) {
	    	empleadoAnterior.desocuparEmpleado();
	    }
	    
	    empleadoNuevo.ocuparEmpleado();
	    
	    double diasTotales = tarea.retornarDiasNecesarios() + tarea.retornarDiasRetraso();
	    
	    if (empleadoAnterior != null) {
	    	proyecto.reducirCosto(diasTotales * empleadoAnterior.retornarValor());
	    }
	    
	    proyecto.incrementarCosto(diasTotales * empleadoNuevo.retornarValor());

	    System.out.println("Empleado reasignado correctamente en la tarea '" + titulo + "'");
    }

	@Override
	public void reasignarEmpleadoConMenosRetraso(Integer numero, String titulo) throws Exception {		
		 Proyecto proyecto = proyectos.get(numero);
		 
		 if (proyecto == null) {
			 throw new IllegalArgumentException("No existe el proyecto con número: " + numero);
		 }
		 
		 Tarea tarea = proyecto.obtenerTareaPorTitulo(titulo);
		    
		 if (tarea == null) {
			 throw new IllegalArgumentException("No existe la tarea con título: " + titulo);
		 }
		 
		 int legajoActual = tarea.retornarEmpleadoResponsable();
		 
		 if (legajoActual == 0) {
			 throw new Exception("La tarea no tiene un empleado asignado actualmente.");
		 }

		 Empleado empleadoActual = empleados.get(legajoActual);
		 
		 if (empleadoActual == null) {
		        throw new Exception("El empleado actual asignado a la tarea no existe en el sistema.");
		 }
		 
		 Empleado mejorEmpleado = obtenerMenosRetrasos();
		 
		 if (mejorEmpleado == null) {
			 throw new Exception("No hay empleados disponibles para reasignar.");
		 }
		 
		 empleadoActual.desocuparEmpleado();		    
		 tarea.asignarEmpleado(mejorEmpleado.retornarLegajo());
		 mejorEmpleado.ocuparEmpleado();
		 proyecto.reducirCosto(empleadoActual.retornarValor());
		 proyecto.incrementarCosto(mejorEmpleado.retornarValor());

		 System.out.println("Empleado '" + empleadoActual.retornarNombre() + 
				 "' fue reemplazado por '" + mejorEmpleado.retornarNombre() +
				 "' en la tarea '" + titulo + "'.");
		}

	@Override
	public double costoProyecto(Integer numeroProyecto) {		
		Proyecto proyecto = proyectos.get(numeroProyecto);
		
        return proyecto.calcularCostoProyecto(); // O(1)
	}
				
	@Override
	public List<Tupla<Integer, String>> proyectosFinalizados() {		
        List<Tupla<Integer, String>> lista = new ArrayList<>();
        
        for (Proyecto proyecto : proyectos.values()) {
        	if (proyecto.estaFinalizado()) {
        		lista.add(new Tupla<>(proyecto.retornarNumeroProyecto(), proyecto.retornarDomicilio()));
        	}
        }
        
        return lista;		
	}

	@Override
	public List<Tupla<Integer, String>> proyectosPendientes() {
		List<Tupla<Integer, String>> pendientes = new ArrayList<>();

	    for (Proyecto p : proyectos.values()) {	        
	        if (!p.estaFinalizado() && !p.listarTareasPendientes().isEmpty()) {
	        	pendientes.add(new Tupla<>(p.retornarNumeroProyecto(), p.retornarDomicilio()));
	        }
	    }

	    return pendientes;
	}

	@Override
	public List<Tupla<Integer, String>> proyectosActivos() {		
		List<Tupla<Integer, String>> activos = new ArrayList<>();
		
		for (Proyecto p : proyectos.values()) {			
			if (p.tieneTareasActivas()) {
				activos.add(new Tupla<>(p.retornarNumeroProyecto(), p.retornarDomicilio()));
			}
		}
		
		return activos;
	}

	@Override
	public Object[] empleadosNoAsignados() {
		List<Empleado> empleadosLibres = new ArrayList<>();
		
		for (Empleado e : empleados.values()) {
			if (e.retornarDisponibilidad()) {
				empleadosLibres.add(e);
			}
		}
		
		return empleadosLibres.toArray(new Empleado[0]);
	}

	@Override
	public boolean estaFinalizado(Integer numero) {		
		return proyectos.get(numero).estaFinalizado();
	}

	@Override
	public int consultarCantidadRetrasosEmpleado(Integer legajo) {		
		return empleados.get(legajo).retornarRetrasos();
	}

	@Override
	public List<Tupla<Integer, String>> empleadosAsignadosAProyecto(Integer numero) {	
		return proyectos.get(numero).retornarEmpleados();
	}

	@Override
	public Object[] tareasProyectoNoAsignadas(Integer numero) {	
		return proyectos.get(numero).listarTareasPendientes().toArray(new Tarea[0]);
	}

	@Override
	public Object[] tareasDeUnProyecto(Integer numero) {
		System.out.println("aca: " + proyectos.get(numero).retornarTareas().values().toArray(new Tarea[0])[0].retornarTitulo());
		return proyectos.get(numero).retornarTareas().values().toArray(new Tarea[0]);
	}

	@Override
	public String consultarDomicilioProyecto(Integer numero) {		
		return proyectos.get(numero).retornarDomicilio();
	}

	@Override
	public boolean tieneRestrasos(Integer legajo) {		
		if (empleados.get(legajo).retornarRetrasos() > 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public List<Tupla<Integer, String>> empleados() {		
		List<Tupla<Integer, String>> listaEmpleados = new ArrayList<>();
		
		for (Empleado e : empleados.values()) {
			listaEmpleados.add(new Tupla<>(e.retornarLegajo(), e.retornarNombre()));
		}
		
		return listaEmpleados;
	}

	@Override
	public String consultarProyecto(Integer numero) {	
		return proyectos.get(numero).toString();
	}
	
	//Método auxiliar que devuelve al empleado disponible con menos retrasos.
	//En caso de no haber ningun empleado disponible devuelve null.
	private Empleado obtenerMenosRetrasos() {	
		Empleado empleadoMenosRetrasos = null;
		
		for (Empleado e : empleados.values()) {			
			if (e.retornarDisponibilidad() &&
					(empleadoMenosRetrasos == null ||
					(e.retornarRetrasos() < empleadoMenosRetrasos.retornarRetrasos()))) {
				empleadoMenosRetrasos = e;
			}
		}
		
		return empleadoMenosRetrasos;	
	}
}
