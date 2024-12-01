package Data;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.UMLComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class CodeGenerator {

    ArrayList<UMLComponent> components;

    ArrayList<Hashtable<String,String>> classesArr;

    public CodeGenerator(ArrayList<UMLComponent> components){
        this.components = components;
        classesArr = new ArrayList<>();
    }

    public void generateCode(String directory){

        for(UMLComponent component: components){

            Hashtable<String,String> aClass = new Hashtable<>();


            if(component instanceof ClassBox){

                //aClass.put("class_")



            }
        }
    }
}
