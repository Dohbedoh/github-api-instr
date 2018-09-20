package org.jenkinsci.agents.github;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class RequestClassTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, 
                            String className, 
                            Class classBeingRedefined,
                            ProtectionDomain protectionDomain, 
                            byte[] classfileBuffer) throws IllegalClassFormatException {

        byte [] byteCode = classfileBuffer;
        
        if (className.equals("org/kohsuke/github/Requester")) {

            try {
                ClassPool cp = ClassPool.getDefault();
                // Following is required to find the class (Not in the locatable with the system class loader)
                cp.insertClassPath(new LoaderClassPath(loader));
                CtClass cc = cp.get("org.kohsuke.github.Requester");
                
                CtMethod m = cc.getDeclaredMethod("setupConnection");
                m.insertAfter("{LOGGER.info(\"Calling: \" + $args[0]);}");
//                Path path = Paths.get("/Users/allan/test/before.class");
//                Files.write(path, classfileBuffer);
                byteCode = cc.toBytecode();
                cc.detach();
            } catch (Throwable ex) {
                System.out.println("Exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        return byteCode;
    }
}
