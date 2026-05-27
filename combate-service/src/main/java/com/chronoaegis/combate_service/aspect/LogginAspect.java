package com.chronoaegis.combate_service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogginAspect {

    @Around("execution(public * com.chronoaegis.combate_service.controller.BatalhaController.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long inicio = System.currentTimeMillis();
        String metodo = joinPoint.getSignature().getName();
        String params = Arrays.toString(joinPoint.getArgs());
        System.out.println("[LOG] Chamando método: " + metodo + " | Parâmetros: " + params);

        Object resultado = joinPoint.proceed();

        long duracao = System.currentTimeMillis() - inicio;
        System.out.println("[LOG] Método " + metodo + " concluído em " + duracao + "ms");

        return resultado;
    }
}
