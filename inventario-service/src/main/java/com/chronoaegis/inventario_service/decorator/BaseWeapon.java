package com.chronoaegis.inventario_service.decorator;

public class BaseWeapon implements WeaponComponent {

    private String nome;
    private int dano;

    public BaseWeapon(String nome, int dano) {
        this.nome = nome;
        this.dano = dano;
    }

    @Override
    public int getDano() {
        return dano;
    }

    @Override
    public boolean podeEquipar(int forca) {
        return true;
    }

    @Override
    public boolean podeAtacar() {
        return true;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void recarregar() {
    }
}
