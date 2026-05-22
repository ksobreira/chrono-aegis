package com.chronoaegis.auth_service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class AuditoriaAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaAspect.class);

    @Around("execution(* com.chronoaegis.auth_service.service.AuthService.login(..))")
    public Object auditarLogin(ProceedingJoinPoint jp) throws Throwable {
        String email = ((com.chronoaegis.auth_service.dto.LoginDTO) jp.getArgs()[0]).getEmail();
        String timestamp = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss"));

        try {
            Object resultado = jp.proceed();
            log.info("Auditoria: LOGIN OK | Email {} | {}", email, timestamp);
            return resultado;
        } catch (Exception e) {
            log.warn("Auditoria: LOGIN FALHOU | Email {} | {} | motivo: {}", email, timestamp, e.getMessage());
            throw e;
        }
    }

    @After("execution(* com.chronoaegis.auth_service.service.AuthService.registrar(..))")
    public void auditarRegistro(JoinPoint jp) {
        String email = ((com.chronoaegis.auth_service.dto.RegistroDTO) jp.getArgs()[0]).getEmail();
        log.info("Auditoria: NOVO USUARIO REGISTRADO: Email {} | {}", email, LocalDateTime.now());
    }



}
