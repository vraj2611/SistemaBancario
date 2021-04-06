package edu.iff.sistemabanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.iff.sistemabanco.model.PacoteServico;

@Repository
public interface PacoteServicoRepository extends JpaRepository<PacoteServico, Long> {

	
}
