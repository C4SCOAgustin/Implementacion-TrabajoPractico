import entidades.HomeSolution;
import gui.PanelManager;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        HomeSolution homeSolution=new HomeSolution();
        String titulos[]={"Pintar","Instalacion electrica","Trabajos jardineria","Instalar AA"};
        String descripciones[]={"","","",""};
        double duracion[]={4,2,1,.5};
        String cliente[]={"Pedro Gomez","",""};
        homeSolution.registrarProyecto(titulos,descripciones,duracion,"San Martin 1000",cliente,"2025-11-22","2025-11-22");
        
        homeSolution.registrarEmpleado("emp_contratado",15000);
        homeSolution.registrarEmpleado("emp_inicial",80000, "EXPERTO");
        homeSolution.registrarEmpleado("emp_tecnico",15000);
        homeSolution.registrarEmpleado("emp_experto",50000, "inicial");
        
        homeSolution.asignarResponsableEnTarea(1,"Pintar");
        homeSolution.asignarResponsableEnTarea(1,"Instalacion electrica");
        homeSolution.asignarResponsableEnTarea(1,"Trabajos jardineria");
        homeSolution.asignarResponsableEnTarea(1,"Instalar AA");
        
        System.out.println(homeSolution.costoProyecto(1));
        
        homeSolution.registrarEmpleado("Lidia",20000);
        
        homeSolution.reasignarEmpleadoEnProyecto(1,5,"Instalacion electrica");
        System.out.println(homeSolution.costoProyecto(1));
        homeSolution.finalizarProyecto(1, "2025-11-26");
        System.out.println(homeSolution.costoProyecto(1));
        PanelManager panelManager=new PanelManager(homeSolution);   
    }
}