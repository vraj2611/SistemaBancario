package edu.iff.sistemabanco.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.iff.sistemabanco.model.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

	@Query(value ="SELECT * FROM transacao WHERE conta_id = :id OR conta_destino = :id", nativeQuery = true)
	public List<Transacao> findByContaId(@Param("id") Long conta_id);

	@Query(value ="SELECT * FROM transacao WHERE conta_id = :id OR conta_destino = :id", nativeQuery = true)
	public List<Transacao> findAllByContaId(@Param("id")Long conta_id);

	@Query("SELECT t FROM Transacao t JOIN Operador o ON o.id = :operador_id")
	public List<Transacao> findByOperadorId(@Param("operador_id") Long operador_id);

	@Query("SELECT t FROM Transacao t WHERE status IS 'PENDENTE'")
	public List<Transacao> findByPendentesLiberacao();

}
