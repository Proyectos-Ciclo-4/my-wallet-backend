package com.sofka.domain.wallet;

import co.com.sofka.domain.generic.EventChange;
import com.sofka.domain.wallet.comandos.ModificarSaldo;
import com.sofka.domain.wallet.eventos.ContactoAnadido;
import com.sofka.domain.wallet.eventos.ContactoEliminado;
import com.sofka.domain.wallet.eventos.MotivoCreado;
import com.sofka.domain.wallet.eventos.SaldoModificado;
import com.sofka.domain.wallet.eventos.TransferenciaCreada;
import com.sofka.domain.wallet.eventos.TransferenciaExitosa;
import com.sofka.domain.wallet.eventos.TransferenciaFallida;
import com.sofka.domain.wallet.eventos.UsuarioAsignado;
import com.sofka.domain.wallet.eventos.WalletCreada;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.Estado.TipoDeEstado;
import com.sofka.domain.wallet.objetosdevalor.FechayHora;
import com.sofka.domain.wallet.objetosdevalor.Motivo;

import com.sofka.domain.wallet.objetosdevalor.Saldo;
import java.time.LocalDate;
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
      wallet.motivos.add(event.getDescripcion());
    });

    apply((SaldoModificado event) -> {
      wallet.saldo = new Saldo(wallet.saldo.value() + event.getSaldo().value());
    });

    apply((TransferenciaCreada event) -> {

      if (wallet.saldo.value() > event.getValor().value()) {

        Transferencia transferencia = new Transferencia(
            event.getTransferenciaID(),
            new Estado(TipoDeEstado.PENDIENTE),
            new FechayHora(LocalDate.now()),
            event.getValor(),
            event.getMotivo());

        wallet.transferencias.add(transferencia);

      } else {
        throw new IllegalArgumentException(
            "No se puede crear una transferencia de mayor valor al saldo de la wallet asociada.");
      }

    });

    apply((TransferenciaExitosa event) -> {
      Transferencia transferencia = wallet.getTransferenciaPorId(event.getTransferenciaID())
          .orElseThrow();
      transferencia.setEstado(new Estado(TipoDeEstado.EXITOSA));
    });

    apply((TransferenciaFallida event) -> {
      Transferencia transferencia = wallet.getTransferenciaPorId(event.getTransferenciaID())
          .orElseThrow();
      transferencia.setEstado(new Estado(TipoDeEstado.RECHAZADA));
    });
  }
}
