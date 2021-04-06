package edu.iff.sistemabanco.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.iff.sistemabanco.exception.NotFoundException;
import edu.iff.sistemabanco.exception.ValidationError;
import edu.iff.sistemabanco.model.Operador;
import edu.iff.sistemabanco.model.Transacao;
import edu.iff.sistemabanco.repository.TransacaoRepository;

@Service
public class TransacaoService {

	@Autowired
	private TransacaoRepository repo;

	public List<Transacao> findAll(int page, int size) {
		Pageable p = PageRequest.of(page, size);
		return repo.findAll(p).toList();
	}

	public List<Transacao> findAll() {
		return repo.findAll();
	}

	public Transacao findById(Long id) {
		Optional<Transacao> result = repo.findById(id);
		if (result.isEmpty())
			throw new NotFoundException("Transacao nao encontrada!");
		return result.get();
	}

	public Transacao save(Transacao t) {
		try {
			return repo.save(t);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao salvar Transacao!");
		}
	}

	public List<Transacao> findPendentes() {
		return this.repo.findByPendentesLiberacao();
	}

	public List<Transacao> findByContaId(Long id) {
		return this.repo.findByContaId(id);
	}

	public List<Transacao> findAllByContaId(Long id) {
		return this.repo.findAllByContaId(id);
	}

	public List<Transacao> findByOperadorId(Long id) {
		return this.repo.findByOperadorId(id);
	}

	public Transacao autorizar(Operador o, Long transacao_id) {
		Transacao t = findById(transacao_id);
		t.autorizar(o);
		try {
			return save(t);
		} catch (Exception e) {
			ValidationError.isConstraintViolation(e);
			throw new RuntimeException("Erro ao alterar a transacao!");
		}
	}

	public Transacao bloquear(Operador o, Long transacao_id) {
		Transacao t = findById(transacao_id);
		t.bloquear(o);
		try {
			return save(t);
		} catch (Exception e) {
			ValidationError.isConstraintViolation(e);
			throw new RuntimeException("Erro ao alterar a transacao!");
		}
	}

}
