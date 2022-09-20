package com.sofka.domain.wallet;

import co.com.sofka.domain.generic.EventChange;
import com.sofka.domain.wallet.eventos.ContactoAnadido;
import com.sofka.domain.wallet.eventos.ContactoEliminado;
import com.sofka.domain.wallet.eventos.MotivoCreado;
import com.sofka.domain.wallet.eventos.UsuarioAsignado;
import com.sofka.domain.wallet.eventos.WalletCreada;
import com.sofka.domain.wallet.objetosdevalor.Motivo;

import com.sofka.domain.wallet.objetosdevalor.Saldo;
import java.util.ArrayList;

public class WalletChange extends EventChange {

    public WalletChange(Wallet wallet) {

        apply((WalletCreada event) -> {
            wallet.saldo = new Saldo(100.00);
            wallet.dueño = null;
            wallet.motivos = new ArrayList<Motivo>();
            wallet.contactos = new ArrayList<Usuario>();
            wallet.transferencias = new ArrayList<Transferencia>();
        });

        apply((UsuarioAsignado event) -> {
            wallet.dueño = new Usuario(event.getUsuarioID(), event.getNombre(), event.getEmail(),
                event.getNumero());
        });

        apply((ContactoAnadido event) -> {
            wallet.contactos.add(event.getContacto());
        });

        apply((ContactoEliminado event) -> {
            Usuario contactoAEliminar = wallet.getContactoPorId(event.getContactoID())
                .orElseThrow();
            wallet.contactos.remove(contactoAEliminar);
        });

        apply((MotivoCreado event) -> {

        });


    }
}
