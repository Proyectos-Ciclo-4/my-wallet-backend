package com.sofka.domain.wallet;

import co.com.sofka.domain.generic.AggregateEvent;
import co.com.sofka.domain.generic.DomainEvent;
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
import com.sofka.domain.wallet.objetosdevalor.Email;
import com.sofka.domain.wallet.objetosdevalor.Estado;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.Nombre;
import com.sofka.domain.wallet.objetosdevalor.Saldo;
import com.sofka.domain.wallet.objetosdevalor.Telefono;
import com.sofka.domain.wallet.objetosdevalor.TransferenciaID;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Wallet extends AggregateEvent<WalletID> {

  protected Saldo saldo;

  protected List<Motivo> motivos;

  protected Usuario dueño;

  protected List<Usuario> contactos;

  protected List<Transferencia> transferencias;


  public Wallet(WalletID entityId, UsuarioID usuarioID, Saldo saldo, List<Motivo> motivos) {
    super(entityId);
    subscribe(new WalletChange(this));
    appendChange(new WalletCreada(entityId, usuarioID, saldo, motivos)).apply();
  }

  private Wallet(WalletID entityId) {
    super(entityId);
    subscribe(new WalletChange(this));
  }

  public static Wallet from(WalletID walletID, List<DomainEvent> events) {
    Wallet wallet = new Wallet(walletID);
    events.forEach(event -> {
      wallet.applyEvent(event);
    });
    return wallet;
  }

  public void asignarUsuario(UsuarioID usuarioID, Nombre nombre, Email email, Telefono telefono) {
    Objects.requireNonNull(usuarioID);
    Objects.requireNonNull(nombre);
    Objects.requireNonNull(email);
    Objects.requireNonNull(telefono);
    appendChange(new UsuarioAsignado(usuarioID, nombre, email, telefono));
  }

  public void anadirContacto(WalletID walletID, Usuario usuario) {
    Objects.requireNonNull(walletID);
    Objects.requireNonNull(usuario);
    appendChange(new ContactoAnadido(walletID, usuario));
  }

  public void eliminarContacto(WalletID walletID, UsuarioID usuarioID) {
    Objects.requireNonNull(walletID);
    Objects.requireNonNull(usuarioID);
    appendChange(new ContactoEliminado(walletID, usuarioID));
  }

  public void anadirMotivo(WalletID walletID, Motivo motivo) {
    Objects.requireNonNull(walletID);
    Objects.requireNonNull(motivo);
    appendChange(new MotivoCreado(walletID, motivo));
  }

  public void crearTransferencia(WalletID walletID, TransferenciaID transferenciaID, Estado estado,
      Cantidad cantidad, Motivo motivo) {
    Objects.requireNonNull(walletID);
    Objects.requireNonNull(transferenciaID);
    Objects.requireNonNull(estado);
    Objects.requireNonNull(cantidad);
    Objects.requireNonNull(motivo);
    appendChange(new TransferenciaCreada(walletID, transferenciaID, estado, cantidad, motivo));
  }

  public void concretarTransferencia(TransferenciaID transferenciaID
  ) {

    Objects.requireNonNull(transferenciaID);

    appendChange(new TransferenciaExitosa(transferenciaID));
  }

  public void cancelarTransferencia(TransferenciaID transferenciaID) {
    Objects.requireNonNull(transferenciaID);

    appendChange(new TransferenciaFallida(transferenciaID));
  }

  public void ModificarSaldo(WalletID walletID, Cantidad cantidad) {
    Objects.requireNonNull(walletID);
    Objects.requireNonNull(cantidad);
    appendChange(new SaldoModificado(walletID, cantidad));
  }

  public Optional<Transferencia> getTransferenciaPorId(TransferenciaID transferenciaID) {
    return transferencias.stream()
        .filter((transferencia -> transferencia.identity().equals(transferenciaID)))
        .findFirst();
  }

  public Optional<Usuario> getContactoPorId(UsuarioID usuarioID) {
    return contactos.stream().filter((usuario -> usuario.identity().equals(usuarioID)))
        .findFirst();
  }
}