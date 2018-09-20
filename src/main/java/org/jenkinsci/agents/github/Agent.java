package org.jenkinsci.agents.github;

import java.lang.instrument.Instrumentation;

public class Agent {
    
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("[Agent] Start agent during JVM startup using argument '-javaagent'");
        instrumentation.addTransformer(new RequestClassTransformer());
//        Listener.AGENT_INSTALLED = true;
    }
    
    public static void agentmain(String args, Instrumentation instrumentation) {
        System.out.println("[Agent] Load agent into running JVM using Attach API");
        instrumentation.addTransformer(new RequestClassTransformer());
//        Listener.AGENT_INSTALLED = true;
    }
}
