package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import com.sofka.domain.wallet.objetosdevalor.Cantidad;
import com.sofka.domain.wallet.objetosdevalor.Motivo;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealizarTransferencia extends Command {

  private WalletID walletOrigen;

  private WalletID walletDestino;

  private Cantidad valor;

  private Motivo motivo;

}
