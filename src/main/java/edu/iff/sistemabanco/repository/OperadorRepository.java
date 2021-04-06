package edu.iff.sistemabanco.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.iff.sistemabanco.model.Operador;

@Repository
public interface OperadorRepository  extends JpaRepository < Operador, Long>{

	public List<Operador> findByCpf(String cpf);
}
