package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import com.sofka.domain.wallet.objetosdevalor.Email;
import com.sofka.domain.wallet.objetosdevalor.Telefono;
import com.sofka.domain.wallet.objetosdevalor.UsuarioID;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearWallet extends Command {

  private UsuarioID usuarioID;

  private Telefono telefono;

  private Email email;

}
