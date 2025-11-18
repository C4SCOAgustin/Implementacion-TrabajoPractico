package entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
		
		if (!categoriaEsValida(categoria)) {
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

	
	public void asignarResponsableEnTarea(Integer numeroProyecto, String tituloTarea) throws Exception {				
	    Proyecto proyecto = proyectos.get(numeroProyecto);
	    if (proyecto == null || proyecto.retornarEstado() == Estado.finalizado) {
	        throw new Exception("El proyecto no existe o está finalizado.");
	    }

	    for (Empleado e: empleados.values()) {		
	        if (e.retornarDisponibilidad()) {
	            proyecto.asignarEmpleadoDisponibleATarea(tituloTarea, e);
	            e.ocuparEmpleado();
	            return;
	        }
	    }

	    proyecto.establecerPendiente();
	    throw new Exception("No hay suficientes empleados disponibles");
	}


	@Override
	public void asignarResponsableMenosRetraso(Integer numero, String titulo) throws Exception {		
	    Proyecto proyecto = proyectos.get(numero);

	    if (proyecto == null || proyecto.retornarEstado() == Estado.finalizado) {
	        throw new Exception("El proyecto no existe o está finalizado.");
	    }

	    // Elegimos el empleado con menos retrasos
	    Empleado mejorEmpleado = obtenerMenosRetrasos();

	    if (mejorEmpleado == null) {
	        proyecto.establecerPendiente();
	        throw new Exception("No hay empleados disponibles.");
	    }

	    // nuevo método (bajo acoplamiento)
	    proyecto.asignarEmpleadoDisponibleATarea(titulo, mejorEmpleado);

	    // Ocupamos al empleado
	    mejorEmpleado.ocuparEmpleado();
	}

	
	@Override
	public void registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias) throws IllegalArgumentException {        
	    Proyecto proyecto = proyectos.get(numero);
	    if (proyecto == null) {
	        throw new IllegalArgumentException("El proyecto no existe.");
	    }
	    
	    if (cantidadDias <= 0) {
	        throw new IllegalArgumentException("Los días ingresados deben ser mayores a 0.");
	    }

	    // Le pedimos al proyecto que registre el retraso y nos devuelva el legajo del empleado responsable
	    Integer legajoEmpleado = proyecto.registrarRetrasoEnTarea(titulo, cantidadDias);

	    
	    Empleado empleado = empleados.get(legajoEmpleado);
	    empleado.añadirRetraso();
	    double costoExtra = empleado.calcularCostoConRetraso(cantidadDias);
	    proyecto.incrementarCosto(costoExtra);
	}

	
	@Override
	public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias)
			throws IllegalArgumentException {	
		if (!proyectos.containsKey(numero)) {
			throw new IllegalArgumentException("El proyecto no existe.");
		}
		
		if (proyectos.get(numero).retornarEstado() == Estado.finalizado) {
			throw new IllegalArgumentException("El proyecto esta finalizado.");
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
	    
	    Integer empleadoTarea = proyecto.finalizarTarea(titulo);
	    
	    if (empleadoTarea != null) {
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
		
		for (Integer i : empleadosADesocupar) {
			if (i != null) {
				empleados.get(i).desocuparEmpleado();
			}
		}
	}

	@Override
	public void reasignarEmpleadoEnProyecto(Integer numero, Integer legajoNuevo, String titulo) throws Exception {
	    Proyecto proyecto = proyectos.get(numero);
	    if (proyecto == null) 
	        throw new IllegalArgumentException("No existe el proyecto con número: " + numero);

	    Empleado nuevoEmpleado = empleados.get(legajoNuevo);
	    if (nuevoEmpleado == null) 
	        throw new IllegalArgumentException("El empleado reemplazante no existe: " + legajoNuevo);

	    // Pedimos a Proyecto que haga todo y nos devuelva el ajuste de costo
	    Empleado empleadoAnterior = empleados.get(proyecto.obtenerLegajoEmpleadoTarea(titulo));
	    double ajusteCosto = proyecto.reasignarEmpleadoYCalcularCosto(titulo, nuevoEmpleado, empleadoAnterior);

	    // Actualizamos disponibilidad
	    if (empleadoAnterior != null) empleadoAnterior.desocuparEmpleado();
	    nuevoEmpleado.ocuparEmpleado();

	    // Ajustamos costo
	    proyecto.incrementarCosto(ajusteCosto);
	}

	@Override
	public void reasignarEmpleadoConMenosRetraso(Integer numero, String titulo) throws Exception {        
	    Proyecto proyecto = proyectos.get(numero);
	    if (proyecto == null) {
	        throw new IllegalArgumentException("No existe el proyecto con número: " + numero);
	    }

	    // Obtenemos el empleado con menos retrasos
	    Empleado mejorEmpleado = obtenerMenosRetrasos();
	    if (mejorEmpleado == null) {
	        throw new Exception("No hay empleados disponibles para reasignar.");
	    }

	    // Delegamos a Proyecto la reasignación y nos devuelve el legajo del empleado anterior
	    Integer legajoAnterior = proyecto.reasignarEmpleadoConMenosRetraso(titulo, mejorEmpleado);

	    // Solo HomeSolution se encarga de actualizar el estado de los empleados
	    if (legajoAnterior != null) {
	        Empleado empleadoAnterior = empleados.get(legajoAnterior);
	        if (empleadoAnterior != null) {
	            empleadoAnterior.desocuparEmpleado();
	        }
	    }
	    mejorEmpleado.ocuparEmpleado();
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
	
	private boolean categoriaEsValida(String categoria) {
		if (categoria == null || categoria.equals("")) {
			return false;
		}
		
		categoria = categoria.toUpperCase();
		return categoria.equals("INICIAL") || categoria.equals("TECNICO") || categoria.equals("EXPERTO");
	}
	
}
