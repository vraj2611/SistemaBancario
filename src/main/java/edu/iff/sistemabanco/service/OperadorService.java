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
import edu.iff.sistemabanco.model.Permissao;
import edu.iff.sistemabanco.model.Transacao;
import edu.iff.sistemabanco.repository.OperadorRepository;
import edu.iff.sistemabanco.repository.PermissaoRepository;
import edu.iff.sistemabanco.repository.TransacaoRepository;

@Service
public class OperadorService {
	@Autowired
	private OperadorRepository repo;

	@Autowired
	private TransacaoRepository transacaoRepo;
	
	@Autowired
	private PermissaoRepository pRepo; 

	public List<Operador> findAll(int page, int size) {
		Pageable p = PageRequest.of(page, size);
		return repo.findAll(p).toList();
	}

	public List<Operador> findAll() {
		return repo.findAll();
	}

	public Operador findById(Long id) {
		Optional<Operador> result = repo.findById(id);
		if (result.isEmpty())
			throw new NotFoundException("Operador nao encontrado!");
		return result.get();
	}

	public Operador save(Operador o) {
		try {
			o.setId(null);
			o.setSenha(new BCryptPasswordEncoder().encode(o.getSenha()));
			List<Permissao> ps = pRepo.findAll();
			o.setPermissoes(List.of(ps.get(0)));
			return repo.save(o);
		} catch (Exception e) {
			ValidationError.isConstraintViolation(e);
			throw new RuntimeException("Erro ao salvar Operador!");
		}

	}

	public Operador update(Operador o) {
		try {
			Operador o2 = findById(o.getId());
			o2.setNome(o.getNome());
			return repo.save(o2);
		} catch (Exception e) {
			ValidationError.isConstraintViolation(e);
			throw new RuntimeException("Erro ao atualizar Operador!");
		}
	}

	public void delete(Long id) {
		Operador o = findById(id);
		checkDeleteOperador(o);
		try {
			repo.delete(o);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao excluir Operador!");
		}

	}

	private void checkDeleteOperador(Operador o) {
		List<Transacao> transacoes = transacaoRepo.findByOperadorId(o.getId());
		if (transacoes.size() > 0)
			throw new NotDeletableException("Nao pode excluir operadores que ja fizeram transacoes!");
	}

	public void alterarSenha(Operador o, String nova_senha) {
		Operador odb = findById(o.getId());
		checkSenhaAtual(odb, o.getSenha());
		try {
			odb.setSenha(new BCryptPasswordEncoder().encode(nova_senha));
			repo.save(odb);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao alterar senha!");
		}
	}

	private void checkSenhaAtual(Operador o, String senha_atual) {
		BCryptPasswordEncoder crypt = new BCryptPasswordEncoder();
		if (!crypt.matches(senha_atual, o.getSenha()))
			throw new RuntimeException("Senha atual está incorreta!");
	}
}
