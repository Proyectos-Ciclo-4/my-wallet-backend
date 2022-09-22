package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import com.sofka.domain.wallet.Usuario;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EliminarContacto extends Command {

  private  WalletID walletID;
  private  UsuarioID contactoID;

}
