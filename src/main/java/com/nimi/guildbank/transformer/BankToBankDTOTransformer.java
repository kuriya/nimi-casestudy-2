package com.nimi.guildbank.transformer;

import com.nimi.guildbank.domain.Bank;
import com.nimi.guildbank.dto.BankDTO;

/**
 * This class transforms Bank domain object to BankDTO
 */
public class BankToBankDTOTransformer implements Transformer<Bank, BankDTO> {
	@Override
	public BankDTO transform(final Bank bank) {
		return BankDTO.builder().bankId(bank.getId()).name(bank.getName())
				.account(new AccountToAccountDTOTransformer().transform(bank.getAccount()))
				.creatorId(bank.getCreatorId()).build();
	}
}
