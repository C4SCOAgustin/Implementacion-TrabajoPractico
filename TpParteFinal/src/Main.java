import static org.junit.Assert.assertTrue;

import entidades.Contratado;
import entidades.Empleado;
import entidades.Permanente;
import entidades.HomeSolution;
import gui.PanelManager;
import entidades.Tarea;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        HomeSolution homeSolution=new HomeSolution();             
        
        String titulos[]={"Pintar","Instalacion electrica","Trabajos jardineria","Instalar AA"};
        String descripciones[]={"","","",""};
        double duracion[]={4,2,1,.5};
        String cliente[]={"Pedro Gomez", "mail@mail.com", "123456"};
        homeSolution.registrarProyecto(titulos,descripciones,duracion,"San Martin 1000",cliente,"2025-12-01","2025-12-05");
        homeSolution.registrarEmpleado("Juan",15000);
        homeSolution.registrarEmpleado("Luis",80000, "EXPERTO");
        homeSolution.registrarEmpleado("Julieta",15000);
        homeSolution.registrarEmpleado("Carlos", 50000,"INICIAL");
        homeSolution.registrarProyecto(titulos,descripciones,duracion,"Libertador 500", cliente,"2025-12-10","2025-12-15");
        
        PanelManager panelManager = new PanelManager(homeSolution);
        
        System.out.println(homeSolution.toString());
 
    }
}
