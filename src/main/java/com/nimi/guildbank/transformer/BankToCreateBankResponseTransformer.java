package com.nimi.guildbank.transformer;

import com.nimi.guildbank.domain.Bank;
import com.nimi.guildbank.dto.CreateBankResponse;


/**
 * This class transforms Bank domain object to CreateBankResponse dto
 */
public class BankToCreateBankResponseTransformer
		implements Transformer<Bank, CreateBankResponse> {

	@Override
	public CreateBankResponse transform(final Bank source) {
		return CreateBankResponse.builder().name(source.getName()).id(source.getId()).build();
	}

}
