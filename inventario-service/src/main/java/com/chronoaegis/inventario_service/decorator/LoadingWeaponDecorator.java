package com.chronoaegis.inventario_service.decorator;

public class LoadingWeaponDecorator extends WeaponDecorator {

    public boolean isLoaded = true;

    public LoadingWeaponDecorator(WeaponComponent weapon) {
        super(weapon);
    }

    @Override
    public boolean podeAtacar(){
        return isLoaded;
    }

    @Override
    public void recarregar(){
        this.isLoaded = true;
    }

    public void disparar(){
        this.isLoaded = false;
    }

    @Override
    public String getNome(){
        return super.getNome() + "[Recarga" + (isLoaded ? "Pronta" : "Vazia") + "]";
    }
}
