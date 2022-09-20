package com.sofka.domain.wallet;

import co.com.sofka.domain.generic.Entity;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.FechayHora;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;

public class Transferencia extends Entity<TransferenciaID> {

    private Estado estado;
    private FechayHora fechayHora;
    private Cantidad cantidad;

    public Transferencia(TransferenciaID entityId, Estado estado, FechayHora fechayHora, Cantidad cantidad) {
        super(entityId);
        this.estado = estado;
        this.fechayHora = fechayHora;
        this.cantidad = cantidad;
    }

    public Estado getEstado() {
        return estado;
    }

    public FechayHora getFechayHora() {
        return fechayHora;
    }

    public Cantidad getCantidad() {
        return cantidad;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setFechayHora(FechayHora fechayHora) {
        this.fechayHora = fechayHora;
    }

    public void setCantidad(Cantidad cantidad) {
        this.cantidad = cantidad;
    }
}
