package com.chronoaegis.combate_service.factory;

import com.chronoaegis.combate_service.model.Monstro;

public class MonstroFactory {

    public static Monstro gerar(int nivel) {
        if (nivel <= 2) {
            return new Monstro("Goblin", 40, 40, 8, 5);
        } else if (nivel <= 4) {
            return new Monstro("Ogro", 80, 80, 15, 18);
        } else {
            return new Monstro("Dragão", 150, 150, 28, 22);
        }
    }
}