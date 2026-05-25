package com.chronoaegis.inventario_service.decorator;

public class HeavyWeaponDecorator extends WeaponDecorator{

    public HeavyWeaponDecorator(WeaponDecorator weapon){
        super(weapon);
    }

    @Override
    public boolean podeEquipar(int forca){
        return forca >= 15 && super.podeEquipar(forca);
    }

    @Override
    public String getNome(){
        return super.getNome() + "[Pesada]";
    }

}
