package com.sofka.domain.wallet.comandos;

import co.com.sofka.domain.generic.Command;
import com.sofka.domain.wallet.Usuario;
import com.sofka.domain.wallet.objetosdevalor.WalletID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnadirContacto extends Command {

    private String walletID;
    private String contacto;

}
