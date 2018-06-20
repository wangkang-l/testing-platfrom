package com.bgw.testing.server.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class FuncUtils {

    private static Set<Class> funcClasses = new HashSet<>();

    static {
        funcClasses.add(BaseFuncUtils.class);
    }

    public static Object getDynamicFuncValue(String methodName, Object[] args, Map<String, String> caseVariable, Map<String, String> stepVariable) {
        try {
            for (Class cls : funcClasses) {
                for (Method method : cls.getMethods()) {
                    if (method.getName().equalsIgnoreCase(methodName)) {
                        if (methodName.equalsIgnoreCase("replace")) {
                            Object[] finalArgs = new Object[3];
                            if (args.length > 3) {
                                finalArgs[0] = args[0];
                                finalArgs[1] = args[1];
                                for (int i = 2; i < args.length - 1; i++) {
                                    finalArgs[1] = finalArgs[1] + "," + args[i].toString();
                                }
                                finalArgs[2] = args[args.length - 1];
                            } else if (args.length == 3) {
                                System.arraycopy(args, 0, finalArgs, 0, 3);
                            }
                            fillArgs(finalArgs, caseVariable, stepVariable);
                            return method.invoke(null, finalArgs);
                        } else {
                            fillArgs(args, caseVariable, stepVariable);
                            if (method.getParameterCount() == args.length) {
                                return method.invoke(null, args);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("无法获取方法值[{}]({}): {}", methodName, Arrays.asList(args), ExceptionUtils.getStackTrace(e));
        }

        return null;
    }

    private static void fillArgs(Object[] args, Map<String, String> caseVariable, Map<String, String> stepVariable) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                args[i] = ContextUtils.fillValue(args[i].toString(), caseVariable, stepVariable);
            }
        }
    }

}
