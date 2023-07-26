package com.exceptioncoding.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exceptioncoding.bank.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long>{
	Account findByCbuOrAlias(String cbu, String alias);
}
