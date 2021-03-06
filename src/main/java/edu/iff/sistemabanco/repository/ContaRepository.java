package edu.iff.sistemabanco.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.iff.sistemabanco.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository < Conta, Long>{

	@Query(value ="SELECT * FROM conta WHERE cliente_id = :id", nativeQuery = true)
	public List<Conta> findByClienteId(@Param("id")Long cliente_id);

	@Query(value ="SELECT * FROM conta WHERE pacote_id = :id", nativeQuery = true)
	public List<Conta> findByPacoteId(@Param("id")Long pacote_id);
	
}
