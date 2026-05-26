package com.chronoaegis.inventario_service.decorator;

public interface WeaponComponent {
    int getDano();
    boolean podeEquipar(int forca);
    boolean podeAtacar();
    String getNome();
    void recarregar();
}
