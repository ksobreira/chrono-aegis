package com.chronoaegis.inventario_service.decorator;

public abstract class WeaponDecorator implements WeaponComponent{
    protected WeaponComponent weapon;

    public WeaponDecorator(WeaponComponent weapon){
        this.weapon = weapon;
    }

    @Override
    public int getDano() {
        return weapon.getDano();
    }

    @Override
    public boolean podeEquipar(int forca){
        return weapon.podeEquipar(forca);
    }

    @Override
    public boolean podeAtacar(){
        return weapon.podeAtacar();
    }

    @Override
    public String getNome(){
        return weapon.getNome();
    }

    @Override
    public void recarregar(){
        weapon.recarregar();
    }
}
