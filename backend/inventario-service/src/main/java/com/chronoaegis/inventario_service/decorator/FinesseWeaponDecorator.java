package com.chronoaegis.inventario_service.decorator;

public class FinesseWeaponDecorator extends WeaponDecorator {

    private int agilidade;

    public FinesseWeaponDecorator(WeaponComponent weapon, int agilidade) {
        super(weapon);
        this.agilidade = agilidade;
    }

    @Override
    public int getDano(){
        return Math.max(super.getDano(), agilidade);
    }

    @Override
    public String getNome(){
        return super.getNome() + "Acuidade";
    }
}
