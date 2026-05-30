package com.chronoaegis.combate_service.observer;

import com.chronoaegis.combate_service.model.BatalhaSession;

public interface BatalhaObserver {
    void notificar(BatalhaSession sessao, String evento);
}
