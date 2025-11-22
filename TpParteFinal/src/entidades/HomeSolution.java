package entidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HomeSolution implements IHomeSolution {
	HashMap<Integer, Empleado> empleados = new HashMap<>();
	HashMap<Integer, Proyecto> proyectos = new HashMap<>();

	@Override
	public void registrarEmpleado(String nombre, double valor) throws IllegalArgumentException {		
		Empleado e = new Contratado(nombre, valor);
		empleados.put(e.retornarLegajo(), e);
	}

	@Override
	public void registrarEmpleado(String nombre, double valor, String categoria) throws IllegalArgumentException {				
		Empleado e = new Permanente(nombre, valor, categoria);
		empleados.put(e.retornarLegajo(), e);		
	}

	@Override
	public void registrarProyecto(String[] titulos, String[] descripciones, double[] dias, String domicilio,
			String[] datosCliente, String fechaInicio, String fechaEstimadaFin) throws IllegalArgumentException {					
		Proyecto p = new Proyecto(titulos, descripciones, dias, domicilio, datosCliente, fechaInicio, fechaEstimadaFin);
		proyectos.put(p.retornarNumeroProyecto(), p);
	}
	
	public void asignarResponsableEnTarea(Integer numeroProyecto, String tituloTarea) throws Exception {				
	    Proyecto proyecto = proyectos.get(numeroProyecto);
	    if (proyecto == null || proyecto.retornarEstado() == Estado.finalizado) {
	        throw new IllegalArgumentException("El proyecto no existe o está finalizado.");
	    }

	    for (Empleado e: empleados.values()) {		
	        if (e.retornarDisponibilidad()) {	        	
	        	//Asignamos al empleado.
	            proyecto.asignarEmpleadoDisponibleATarea(tituloTarea, e.retornarLegajo());                  
	            //Calculamos el costo del trabajo y lo añadimos a proyecto.
	            double dias = proyecto.retornarDiasTarea(tituloTarea);
	            
	            if (e.retornarTipoContrato().equalsIgnoreCase("CONTRATADO")) {
	            	double mediosDias = proyecto.retornarMediosDiasTarea(tituloTarea);
	            	dias = dias - mediosDias + mediosDias/2;
	            }
	            
	            double costo = e.calcularCosto(dias);
	            proyecto.incrementarCosto(costo);            
	            //Ocupamos al empleado.
	            e.ocuparEmpleado();	            
	            return;
	        }
	    }

	    proyecto.establecerPendiente();
	    throw new IllegalArgumentException("No hay suficientes empleados disponibles");
	}

	@Override
	public void asignarResponsableMenosRetraso(Integer numero, String titulo) throws Exception {		
	    Proyecto proyecto = proyectos.get(numero);

	    if (proyecto == null || proyecto.retornarEstado() == Estado.finalizado) {
	        throw new Exception("El proyecto no existe o está finalizado.");
	    }

	    //Elegimos el empleado con menos retrasos.
	    Empleado mejorEmpleado = obtenerMenosRetrasos();

	    if (mejorEmpleado == null) {
	        proyecto.establecerPendiente();
	        throw new Exception("No hay empleados disponibles.");
	    }

	    //Asignamos empleado.
	    proyecto.asignarEmpleadoDisponibleATarea(titulo, mejorEmpleado.retornarLegajo());	    
        //Calculamos el costo del trabajo y lo añadimos a proyecto.
        double dias = proyecto.retornarDiasTarea(titulo);
        
        if (mejorEmpleado.retornarTipoContrato().equalsIgnoreCase("CONTRATADO")) {
        	double mediosDias = proyecto.retornarMediosDiasTarea(titulo);
        	dias = dias - mediosDias + mediosDias/2;
        }
        
        double costo = mejorEmpleado.calcularCosto(dias);
        proyecto.incrementarCosto(costo);
	    //Ocupamos al empleado.
	    mejorEmpleado.ocuparEmpleado();
	}
	
	@Override
	public void registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias) throws IllegalArgumentException {        
	    Proyecto proyecto = proyectos.get(numero);
	    if (proyecto == null) {
	        throw new IllegalArgumentException("El proyecto no existe.");
	    }
	   
	    //Le pedimos al proyecto que registre el retraso y nos devuelva el legajo del empleado responsable.
	    Integer legajoEmpleado = proyecto.registrarRetrasoEnTarea(titulo, cantidadDias);
	    //Le contabilizamos el retraso al empleado.
	    Empleado empleado = empleados.get(legajoEmpleado);
	    empleado.añadirRetraso();
	    //Calculamos el costo del retraso y se lo añadimos al proyecto.
	    double costoExtra = empleado.calcularCostoConRetraso(cantidadDias);
	    proyecto.incrementarCosto(costoExtra);
	}
	
	@Override
	public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias)
			throws IllegalArgumentException {	
		Proyecto p = proyectos.get(numero);
		
		if (p == null) {
			throw new IllegalArgumentException("El proyecto no existe.");
		}
		
		proyectos.get(numero).registrarTarea(titulo, descripcion, dias);
	}

	@Override
	public void finalizarTarea(Integer numero, String titulo) throws Exception {	
		Proyecto proyecto = proyectos.get(numero);
	    if (proyecto == null) {
	        throw new IllegalArgumentException("No existe el proyecto con número: " + numero);
	    }
	    
	    Integer empleadoTarea = proyecto.finalizarTarea(titulo);
	    
	    if (empleadoTarea != null || empleadoTarea != 0) {
	    	empleados.get(empleadoTarea).desocuparEmpleado();
	    }
	}

	@Override
	public void finalizarProyecto(Integer numero, String fin) throws IllegalArgumentException {		
		Proyecto proyecto = proyectos.get(numero);
		if (proyecto == null) {
			throw new IllegalArgumentException("No existe el proyecto con número: " + numero);
		}
		
		Set<Integer> empleadosADesocupar = proyecto.finalizarProyecto(fin);
		
		for (Integer legajos : empleadosADesocupar) {
			if (legajos != null) {
				empleados.get(legajos).desocuparEmpleado();
			}
		}
	}

	@Override
	public void reasignarEmpleadoEnProyecto(Integer numero, Integer legajoNuevo, String titulo) throws Exception {
	    Proyecto proyecto = proyectos.get(numero);
	    Empleado empleadoNuevo = empleados.get(legajoNuevo);
	    if (proyecto == null) {
	        throw new IllegalArgumentException("No existe el proyecto con número: " + numero);
	    }
	    
	    if (empleadoNuevo == null) {
	        throw new IllegalArgumentException("El empleado reemplazante no existe: " + legajoNuevo);
	    }
	    
	    //Asignamos el empleado nuevo a la tarea y obtenemos al empleado anterior.    
	    Integer legajoAnterior = proyecto.reasignarEmpleado(titulo, legajoNuevo);
	    Empleado empleadoAnterior = empleados.get(legajoAnterior);	    
	    //Actualizamos disponibilidades.
	    empleadoAnterior.desocuparEmpleado();
	    empleadoNuevo.ocuparEmpleado();
	    //calculamos costo del empleado nuevo
	    double diasTarea = proyecto.retornarDiasTarea(titulo);
	    
        if (empleadoNuevo.retornarTipoContrato().equalsIgnoreCase("CONTRATADO")) {
        	double mediosDias = proyecto.retornarMediosDiasTarea(titulo);
        	diasTarea = diasTarea - mediosDias + mediosDias/2;
        }
        
        else {      	
        	diasTarea = proyecto.retornarDiasTarea(titulo);
        }
        
	    double costoNuevo = empleadoNuevo.calcularCosto(diasTarea);
	    //calculamos costo del empleado anterior
	    double costoAnterior;
        
	    //comprobamos si había retraso en la tarea y calculamos costo del empleado anterior en consecuencia.
	    if (proyecto.tareaConRetraso(titulo)) {
	    	//Calculamos costo del empleado anterior con retraso.	    	
            if (empleadoAnterior.retornarTipoContrato().equalsIgnoreCase("CONTRATADO")) {
            	double mediosDias = proyecto.retornarMediosDiasTarea(titulo);
            	diasTarea = diasTarea - mediosDias + mediosDias/2;
            }
            
            else {          	
            	diasTarea = proyecto.retornarDiasTarea(titulo);
            }
            
	    	costoAnterior = empleadoAnterior.calcularCostoConRetraso(diasTarea);
	    }
	    
	    else {
	    	//Calculamos costo del empleado anterior sin retraso.
            if (empleadoAnterior.retornarTipoContrato().equalsIgnoreCase("CONTRATADO")) {
            	double mediosDias = proyecto.retornarMediosDiasTarea(titulo);
            	diasTarea = diasTarea - mediosDias + mediosDias/2;
            }
            
            else {      	
            	diasTarea = proyecto.retornarDiasTarea(titulo);
            }
            
	    	costoAnterior = empleadoAnterior.calcularCosto(diasTarea);
	    }
	   
	    //Reducimos el costo antiguo y incrementamos el nuevo.
	    proyecto.reducirCosto(costoAnterior);
	    proyecto.incrementarCosto(costoNuevo);
	}

	@Override
	public void reasignarEmpleadoConMenosRetraso(Integer numero, String titulo) throws Exception {        
	    Proyecto proyecto = proyectos.get(numero);    
	    Empleado empleadoNuevo = obtenerMenosRetrasos();
	    
	    if (proyecto == null) {
	        throw new IllegalArgumentException("No existe el proyecto con número: " + numero);
	    }
	    
	    if (empleadoNuevo == null) {
	        throw new IllegalArgumentException("No hay empleados disponibles para reasignar,");
	    }
	    
	    //Asignamos el empleado nuevo a la tarea y obtenemos al empleado anterior.    
	    Integer legajoAnterior = proyecto.reasignarEmpleado(titulo, empleadoNuevo.retornarLegajo());
	    Empleado empleadoAnterior = empleados.get(legajoAnterior);	    
	    //Actualizamos disponibilidades.
	    empleadoAnterior.desocuparEmpleado();
	    empleadoNuevo.ocuparEmpleado();
	    //Obtenemos los dias trabajados de la tarea.
	    double diasTarea = proyecto.retornarDiasTarea(titulo);
	    //calculamos costo del empleado nuevo
	    
        if (empleadoNuevo.retornarTipoContrato().equalsIgnoreCase("CONTRATADO")) {
        	double mediosDias = proyecto.retornarMediosDiasTarea(titulo);
        	diasTarea = diasTarea - mediosDias + mediosDias/2;
        }
        
        else {          	
        	diasTarea = proyecto.retornarDiasTarea(titulo);
        }
        
	    double costoNuevo = empleadoNuevo.calcularCosto(diasTarea);
	    //calculamos costo del empleado anterior
	    double costoAnterior;
	    
	    //comprobamos si había retraso en la tarea y calculamos costo del empleado anterior en consecuencia.
	    if (proyecto.tareaConRetraso(titulo)) {
	    	//Calculamos costo del empleado anterior con retraso.
	        if (empleadoAnterior.retornarTipoContrato().equalsIgnoreCase("CONTRATADO")) {
	        	double mediosDias = proyecto.retornarMediosDiasTarea(titulo);
	        	diasTarea = diasTarea - mediosDias + mediosDias/2;
	        }
	        
	        else {          	
	        	diasTarea = proyecto.retornarDiasTarea(titulo);
	        }
	        
	    	costoAnterior = empleadoAnterior.calcularCostoConRetraso(diasTarea);
	    }
	    
	    else {
	    	//Calculamos costo del empleado anterior sin retraso.
	        if (empleadoAnterior.retornarTipoContrato().equalsIgnoreCase("CONTRATADO")) {
	        	double mediosDias = proyecto.retornarMediosDiasTarea(titulo);
	        	diasTarea = diasTarea - mediosDias + mediosDias/2;
	        }
	        
	        else {          	
	        	diasTarea = proyecto.retornarDiasTarea(titulo);
	        }
	        
	    	costoAnterior = empleadoAnterior.calcularCosto(diasTarea);
	    }
	   
	    //Reducimos el costo antiguo y incrementamos el nuevo.
	    proyecto.reducirCosto(costoAnterior);
	    proyecto.incrementarCosto(costoNuevo);
	}
	
	@Override
	public double costoProyecto(Integer numeroProyecto) {
		
		return proyectos.get(numeroProyecto).calcularCostoProyecto();
	}
		
	//Esta finalizado.
	@Override
	public List<Tupla<Integer, String>> proyectosFinalizados() {		
        List<Tupla<Integer, String>> lista = new ArrayList<>();
        
        for (Proyecto proyecto : proyectos.values()) {
        	if (proyecto.retornarEstado() == Estado.finalizado) {
        		lista.add(new Tupla<>(proyecto.retornarNumeroProyecto(), proyecto.retornarDomicilio()));
        	}
        }
        
        return lista;		
	}

	//No esta finalizado, no fue iniciado o quedo alguna tarea pendiente de asignar.
	@Override
	public List<Tupla<Integer, String>> proyectosPendientes() {
		List<Tupla<Integer, String>> pendientes = new ArrayList<>();

	    for (Proyecto p : proyectos.values()) {
//	        if (p.retornarEstado() != Estado.finalizado && !p.retornarTareasPendientes().isEmpty() && !p.retornarTareasNoAsignadas().isEmpty()) {
//	        	pendientes.add(new Tupla<>(p.retornarNumeroProyecto(), p.retornarDomicilio()));
//	        }
	    	if (p.retornarEstado() == Estado.pendiente) {
	    		pendientes.add(new Tupla<>(p.retornarNumeroProyecto(), p.retornarDomicilio()));
	    	}
	    }

	    return pendientes;
	}

	//No esta finalizado y se asignaron sus tareas sin problemas o tiene todas sus tareas finalizadas.
	@Override
	public List<Tupla<Integer, String>> proyectosActivos() {		
		List<Tupla<Integer, String>> activos = new ArrayList<>();
		
		for (Proyecto p : proyectos.values()) {			
//			if (p.retornarEstado() != Estado.finalizado && p.retornarTareasNoAsignadas().isEmpty()) {
//				activos.add(new Tupla<>(p.retornarNumeroProyecto(), p.retornarDomicilio()));
//			}
			if(p.retornarEstado() == Estado.activo) {
				activos.add(new Tupla<>(p.retornarNumeroProyecto(), p.retornarDomicilio()));
			}
		}
		
		return activos;
	}

	@Override
	public Object[] empleadosNoAsignados() {
		List<Integer> empleadosLibres = new ArrayList<>();
		
		for (Empleado e : empleados.values()) {
			if (e.retornarDisponibilidad()) {
				empleadosLibres.add(e.retornarLegajo());
			}
		}
		
		return empleadosLibres.toArray(new Integer[0]);
	}

	@Override
	public boolean estaFinalizado(Integer numero) {		
		return proyectos.get(numero).retornarEstado() == Estado.finalizado;
	}

	@Override
	public int consultarCantidadRetrasosEmpleado(Integer legajo) {		
		return empleados.get(legajo).retornarRetrasos();
	}

	@Override
	public List<Tupla<Integer, String>> empleadosAsignadosAProyecto(Integer numero) {
		List<Tupla<Integer, String>> empleadosProyecto = new ArrayList<>();
		
		for (Integer legajo : proyectos.get(numero).retornarEmpleados()) {
			empleadosProyecto.add(new Tupla<>(legajo, empleados.get(legajo).retornarNombre()));
		}
		
		return empleadosProyecto;
	}

	@Override
	public Object[] tareasProyectoNoAsignadas(Integer numero) {
		return proyectos.get(numero).retornarTareasNoAsignadas().toArray(new Tarea[0]);
	}

	@Override
	public Object[] tareasDeUnProyecto(Integer numero) {
		return proyectos.get(numero).retornarTareas().values().toArray(new Tarea[0]);
	}

	@Override
	public String consultarDomicilioProyecto(Integer numero) {		
		return proyectos.get(numero).retornarDomicilio();
	}

	@Override
	public boolean tieneRetrasos(Integer legajo) {		
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
	
	@Override
	public String toString() {
		StringBuilder cadena = new StringBuilder();
				
		//proyectos
		StringBuilder proyectosFinalizados = new StringBuilder("Detalles proyectos finalizados: \n");
		StringBuilder proyectosActivos = new StringBuilder("Detalles proyectos activos: \n");
		StringBuilder proyectosPendientes = new StringBuilder("Detalles proyectos pendientes: \n");
		int contFinalizados = 0;
		int contActivos = 0;
		int contPendientes = 0;
		
		cadena.append("Cantidad proyectos totales: " + proyectos.size() + "\n");
		Iterator<Proyecto> it = proyectos.values().iterator();
		
		while (it.hasNext()) {
			Proyecto p = it.next();
			if (p.retornarEstado() == Estado.finalizado) {
				contFinalizados ++;
				proyectosFinalizados.append(p.toString() + "\n");
			}
			
			if (p.retornarEstado() == Estado.activo) {
				contActivos ++;
				proyectosActivos.append(p.toString() + "\n");
			}

			if (p.retornarEstado() == Estado.pendiente) {
				contPendientes ++;
				proyectosPendientes.append(p.toString() + "\n");
			}
		}
		
		cadena.append("cantidad proyectos finalizados: " + contFinalizados + "\n");
		cadena.append("cantidad proyectos activos: " + contActivos + "\n");
		cadena.append("cantidad proyectos pendientes: " + contPendientes + "\n" + "\n");
		
		if (contFinalizados == 0) {
			proyectosFinalizados.append("Sin proyectos finalizados.\n");
		}
		
		if (contActivos == 0) {
			proyectosActivos.append("Sin proyectos activos.\n");
		}
		
		if (contPendientes == 0) {
			proyectosFinalizados.append("Sin proyectos pendientes.\n");
		}
		
		cadena.append(proyectosFinalizados + "\n");
		cadena.append(proyectosActivos + "\n");
		cadena.append(proyectosPendientes);
		
		//empleados		
		cadena.append("Cantidad empleados totales: " + empleados.size() + "\n");		
		Iterator<Empleado> itEmp = empleados.values().iterator();
		
		while (itEmp.hasNext()) {
			Empleado e = itEmp.next();			
			cadena.append(e.toString() + "\n");
		}
		
		cadena.delete(cadena.length() - 2, cadena.length());
		return cadena.toString();
	}
	
	//Método auxiliar que devuelve al empleado disponible con menos retrasos.
	//En caso de no haber ningun empleado disponible devuelve null.
	private Empleado obtenerMenosRetrasos() {	
		Empleado empleadoMenosRetrasos = null;
		
		for (Empleado e : empleados.values()) {			
			if (e.retornarDisponibilidad() &&
					((empleadoMenosRetrasos == null || e.retornarRetrasos() == 0) ||
					(e.retornarRetrasos() < empleadoMenosRetrasos.retornarRetrasos()))) {
				empleadoMenosRetrasos = e;
			}
		}
		
		return empleadoMenosRetrasos;	
	}
}
