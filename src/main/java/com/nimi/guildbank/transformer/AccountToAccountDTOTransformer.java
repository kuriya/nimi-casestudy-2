package com.nimi.guildbank.transformer;

import com.nimi.guildbank.domain.Account;
import com.nimi.guildbank.dto.AccountDTO;

import java.util.Objects;

/**
 * This class transforms Account domain class to AccountDTO
 */
public class AccountToAccountDTOTransformer implements Transformer<Account, AccountDTO> {
	@Override
	public AccountDTO transform(final Account account) {
		if (Objects.nonNull(account)) {
			return AccountDTO.builder().id(account.getId()).balance(account.getAmount())
					.creatorId(account.getCreatorId()).status(account.getStatus()).build();
		}
		return null;
	}

}
