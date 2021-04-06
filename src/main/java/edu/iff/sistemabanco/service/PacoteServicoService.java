package edu.iff.sistemabanco.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.iff.sistemabanco.exception.NotDeletableException;
import edu.iff.sistemabanco.exception.NotFoundException;
import edu.iff.sistemabanco.exception.ValidationError;
import edu.iff.sistemabanco.model.Conta;
import edu.iff.sistemabanco.model.PacoteServico;
import edu.iff.sistemabanco.repository.ContaRepository;
import edu.iff.sistemabanco.repository.PacoteServicoRepository;


@Service
public class PacoteServicoService {

	@Autowired
	private ContaRepository contaRepo;
	
	@Autowired
	private PacoteServicoRepository repo;

	public List<PacoteServico> findAll(int page, int size){
		Pageable p = PageRequest.of(page, size);
		return repo.findAll(p).toList();
	}
	
	public List<PacoteServico> findAll(){
		return repo.findAll();
	}
	
	public PacoteServico findById(Long id){
		 Optional<PacoteServico> result = repo.findById(id);
		 if(result.isEmpty()) throw new NotFoundException("Pacote de Servico nao encontrado!");
		 return result.get();
	}
	
	public PacoteServico save(PacoteServico p) {
		try {
			return repo.save(p);	
		} catch(Exception e) {
			ValidationError.isConstraintViolation(e);
			throw new RuntimeException("Erro ao salvar PacoteServico!");
		}
		
	}
	
	public PacoteServico update(PacoteServico p) {
		try {
			return repo.save(p);
		} catch(Exception e) {
			ValidationError.isConstraintViolation(e);
			throw new RuntimeException("Erro ao atualizar PacoteServico!");
		}
	}
	
	public void delete(Long id) {
		PacoteServico p = findById(id);
		checkDeletePacoteServico(p);
		try {
			repo.delete(p);			
		} catch(Exception e) {
			throw new RuntimeException("Erro ao excluir PacoteServico!");
		}
		
	}
	
	private void checkDeletePacoteServico(PacoteServico p) {
		List<Conta> contas = contaRepo.findByPacoteId(p.getId());
		if(contas.size() > 0)
			throw new NotDeletableException("Nao pode excluir Pacote de Servico que está sendo utilizado contas!");
	}



}
