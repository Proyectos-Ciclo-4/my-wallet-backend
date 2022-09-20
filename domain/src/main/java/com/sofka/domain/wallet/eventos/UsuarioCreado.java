package com.sofka.domain.wallet.eventos;

import co.com.sofka.domain.generic.DomainEvent;
import com.sofka.domain.wallet.objetosdevalor.Email;
import com.sofka.domain.wallet.objetosdevalor.Nombre;
import com.sofka.domain.wallet.objetosdevalor.Telefono;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;

public class UsuarioCreado extends DomainEvent {

    private final UsuarioID usuarioID;

    private final Nombre nombre;

    private final Email email;

    private final Telefono numero;

    public UsuarioCreado( UsuarioID usuarioID, Nombre nombre, Email email, Telefono numero) {
        super("com.sofka.domain.wallet.UsuarioCreado");
        this.usuarioID = usuarioID;
        this.nombre = nombre;
        this.email = email;
        this.numero = numero;
    }

    public UsuarioID getUsuarioID() {
        return usuarioID;
    }

    public Nombre getNombre() {
        return nombre;
    }

    public Email getEmail() {
        return email;
    }

    public Telefono getNumero() {
        return numero;
    }
}
