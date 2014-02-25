package com.cel.dataxfer.jython;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import com.jfinal.kit.PathKit;

public class TransferFactory {
	
	//Include the script path , JYTHON_HOME to the sys.path for module search.
	static{
		PySystemState sys = Py.getSystemState();                
        sys.setCurrentWorkingDir(PathKit.getWebRootPath().replace("\\", "/") + "/script");        
        sys.path.append(new PyString(PathKit.getWebRootPath().replace("\\", "/") + "/script"));
        sys.path.append(new PyString(getJythonHome()));
	}

    private PyObject buildingClass;

    public TransferFactory() {
        PythonInterpreter interpreter = new PythonInterpreter();                
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
    public static String getJythonHome(){
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