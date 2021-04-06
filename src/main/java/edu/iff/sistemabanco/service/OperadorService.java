package edu.iff.sistemabanco.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.iff.sistemabanco.exception.NotDeletableException;
import edu.iff.sistemabanco.exception.NotFoundException;
import edu.iff.sistemabanco.exception.ValidationError;
import edu.iff.sistemabanco.model.Operador;
import edu.iff.sistemabanco.model.Transacao;
import edu.iff.sistemabanco.repository.OperadorRepository;
import edu.iff.sistemabanco.repository.TransacaoRepository;

@Service
public class OperadorService {
	@Autowired
	private OperadorRepository repo;
	
	@Autowired
	private TransacaoRepository transacaoRepo;
	

	public List<Operador> findAll(int page, int size){
		Pageable p = PageRequest.of(page, size);
		return repo.findAll(p).toList();
	}
	
	public List<Operador> findAll(){
		return repo.findAll();
	}
	
	public Operador findById(Long id){
		 Optional<Operador> result = repo.findById(id);
		 if(result.isEmpty()) throw new NotFoundException("Operador nao encontrado!");
		 return result.get();
	}
	
	public Operador save(Operador o) {
		try {
			o.setSenha(new BCryptPasswordEncoder().encode(o.getSenha()));
			return repo.save(o);	
		} catch(Exception e) {
			ValidationError.isConstraintViolation(e);
			throw new RuntimeException("Erro ao salvar Operador!");
		}
		
	}
	
	public Operador update(Operador o) {
		try {
			return repo.save(o);
		} catch(Exception e) {
			ValidationError.isConstraintViolation(e);
			throw new RuntimeException("Erro ao atualizar Operador!");
		}
	}
	
	public void delete(Long id) {
		Operador o = findById(id);
		checkDeleteOperador(o);
		try {
			repo.delete(o);			
		} catch(Exception e) {
			throw new RuntimeException("Erro ao excluir Operador!");
		}
		
	}
	
	
	private void checkDeleteOperador(Operador o) {
		List<Transacao> transacoes = transacaoRepo.findByOperadorId(o.getId());
		if(transacoes.size() > 0)
			throw new NotDeletableException("Nao pode excluir Operadores que ja fizeram transacoes!");
	}


}
