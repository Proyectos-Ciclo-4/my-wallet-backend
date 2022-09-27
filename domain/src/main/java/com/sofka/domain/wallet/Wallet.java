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
import com.sofka.domain.wallet.eventos.TransferenciaValidada;
import com.sofka.domain.wallet.eventos.UsuarioAsignado;
import com.sofka.domain.wallet.eventos.UsuarioExistente;
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

  protected List<Motivo> motivos = List.of(new Motivo("Indefinido"));

  protected Usuario dueño;

  protected List<Usuario> contactos;

  protected List<Transferencia> transferencias;

  public Wallet(UsuarioID usuarioID, Saldo saldo) {
    super(WalletID.of(usuarioID.value()));
    subscribe(new WalletChange(this));
    appendChange(new WalletCreada(entityId, usuarioID, saldo)).apply();
  }

  public Wallet(WalletID entityId) {
    super(entityId);
    subscribe(new WalletChange(this));
  }

  public static Wallet from(WalletID walletID, List<DomainEvent> events) {
    Wallet wallet = new Wallet(walletID);
    events.forEach(wallet::applyEvent);
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

  public void anadirMotivo(String motivo) {
    Objects.requireNonNull(motivo);
    appendChange(new MotivoCreado(new Motivo(motivo)));
  }

  public void crearTransferencia(WalletID walletOrigen, WalletID walletDestinoID, Cantidad cantidad,
      Motivo motivo) {

    Objects.requireNonNull(walletDestinoID);
    Objects.requireNonNull(cantidad);
    Objects.requireNonNull(motivo);
    Objects.requireNonNull(walletOrigen);

    appendChange(new TransferenciaCreada(walletOrigen, walletDestinoID, cantidad, motivo));
  }

  public void concretarTransferencia(WalletID walletOrigen, WalletID walletDestino,
      TransferenciaID transferenciaID, Cantidad valor, Motivo motivo, Estado estado) {

    Objects.requireNonNull(transferenciaID);
    Objects.requireNonNull(valor);
    Objects.requireNonNull(motivo);
    Objects.requireNonNull(estado);

    appendChange(
        new TransferenciaExitosa(walletOrigen, walletDestino, transferenciaID, valor, motivo,
            estado));
  }

  public void rechazarCreacion(String usuarioId) {
    appendChange(new UsuarioExistente(usuarioId));
  }

  public void cancelarTransferencia(WalletID walletOrigen, WalletID walletDestino,
      TransferenciaID transferenciaID, Estado estadoDeTransferencia, Cantidad valor,
      Motivo motivo) {

    Objects.requireNonNull(transferenciaID);
    Objects.requireNonNull(estadoDeTransferencia);
    Objects.requireNonNull(valor);
    Objects.requireNonNull(motivo);
    Objects.requireNonNull(walletOrigen);
    Objects.requireNonNull(walletDestino);

    appendChange(new TransferenciaFallida(walletOrigen, walletDestino, transferenciaID,
        estadoDeTransferencia, valor, motivo));
  }

  public void ModificarSaldo(WalletID walletID, Cantidad cantidad,
      TransferenciaID transferenciaID) {
    Objects.requireNonNull(walletID);
    Objects.requireNonNull(cantidad);
    appendChange(new SaldoModificado(walletID, cantidad, transferenciaID));
  }

  public void validarTransferencia(WalletID walletOrigen, WalletID walletDestino,
      TransferenciaID transferenciaID, Estado estadoDeTransferencia, Cantidad valor,
      Motivo motivo) {

    Objects.requireNonNull(transferenciaID);
    Objects.requireNonNull(estadoDeTransferencia);
    Objects.requireNonNull(valor);
    Objects.requireNonNull(motivo);
    Objects.requireNonNull(walletOrigen);
    Objects.requireNonNull(walletDestino);

    appendChange(new TransferenciaValidada(walletOrigen, walletDestino, transferenciaID,
        valor, motivo, estadoDeTransferencia));
  }

  public Optional<Transferencia> getTransferenciaPorId(TransferenciaID transferenciaID) {
    return transferencias.stream()
        .filter((transferencia -> transferencia.identity().equals(transferenciaID))).findFirst();
  }

  public Optional<Usuario> getContactoPorId(UsuarioID usuarioID) {
    return contactos.stream().filter((usuario -> usuario.identity().equals(usuarioID))).findFirst();
  }
}