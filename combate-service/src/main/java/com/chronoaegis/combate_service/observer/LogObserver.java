package com.chronoaegis.combate_service.observer;

import com.chronoaegis.combate_service.model.BatalhaSession;

public class LogObserver implements BatalhaObserver {

    @Override
    public void notificar(BatalhaSession sessao, String evento) {
        sessao.getLog().add("[LOG] " + evento);
    }
}
