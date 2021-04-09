package edu.iff.sistemabanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.iff.sistemabanco.model.Permissao;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long>{
    
}