package com.cel.dataxfer.jython;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import com.jfinal.kit.PathKit;

public class TransferFactory {

    private PyObject buildingClass;

    /**
     * Create a new PythonInterpreter object, then use it to
     * execute some python code. In this case, we want to
     * import the python module that we will coerce.
     *
     * Once the module is imported than we obtain a reference to
     * it and assign the reference to a Java variable
     */

    public TransferFactory() {
        PythonInterpreter interpreter = new PythonInterpreter();
        PySystemState sys = Py.getSystemState();
        
        sys.setCurrentWorkingDir(PathKit.getWebRootPath().replace("\\", "/") + "/script");
        //include the jython script dir into sys.path
        sys.path.append(new PyString(PathKit.getWebRootPath().replace("\\", "/") + "/script"));
        
        interpreter.exec("from transfer import Transfer");
        buildingClass = interpreter.get("Transfer");
    }

    /**
     * The create method is responsible for performing the actual
     * coercion of the referenced python module into Java bytecode
     */

    public TransferType create (String pkg) {

        PyObject buildingObject = buildingClass.__call__(new PyString(pkg));
        return (TransferType)buildingObject.__tojava__(TransferType.class);
    }
    
    /**
     * Get the Jython home from system variable
     * @return
     */
    public String getJythonHome(){
    	String home = System.getenv("JYTHON_HOME");
    	if(home == null){
    		throw new JythonException("JYTHON_HOME system variable is not set.");
    	}
    	
    	if(home.indexOf("Lib") == -1){
    		return home.replace("\\", "/") + "/Lib";
    	} else {
    		return home;
    	}
    }

}