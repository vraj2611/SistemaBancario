package edu.iff.sistemabanco.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.iff.sistemabanco.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository < Conta, Long>{

	@Query("SELECT cn FROM Conta cn JOIN Cliente clt ON clt.id = :cliente_id")
	public List<Conta> findByClienteId(@Param("cliente_id")Long cliente_id);

	@Query("SELECT cn FROM Conta cn JOIN PacoteServico pct ON pct.id = :pacote_id")
	public List<Conta> findByPacoteId(@Param("pacote_id")Long pacote_id);
	
}
