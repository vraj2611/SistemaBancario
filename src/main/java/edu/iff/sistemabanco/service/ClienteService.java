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
import edu.iff.sistemabanco.model.Cliente;
import edu.iff.sistemabanco.model.Conta;
import edu.iff.sistemabanco.repository.ClienteRepository;
import edu.iff.sistemabanco.repository.ContaRepository;

@Service
public class ClienteService {

	@Autowired
	private ContaRepository contaRepo;

	@Autowired
	private ClienteRepository repo;

	public List<Cliente> findAll(int page, int size) {
		Pageable p = PageRequest.of(page, size);
		return repo.findAll(p).toList();
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Cliente findById(Long id) {
		Optional<Cliente> result = repo.findById(id);
		if (result.isEmpty())
			throw new NotFoundException("Cliente nao encontrado!");
		return result.get();
	}
	
	public Cliente findByCpf(String cpf) {
		List<Cliente> result = repo.findByCpf(cpf);
		if (result.isEmpty())
			throw new NotFoundException("Cliente nao encontrado!");
		return result.get(0);
	}

	public Cliente save(Cliente c) {
		try {
			c.setId(null);
			c.setSenha(new BCryptPasswordEncoder().encode(c.getSenha()));
			return repo.save(c);
		} catch (Exception e) {
			ValidationError.isConstraintViolation(e);
			throw new RuntimeException("Erro ao salvar Cliente!");
		}

	}

	public Cliente update(Cliente c) {
		try {
			Cliente c2 = findById(c.getId());
			c2.setNome(c.getNome());
			return repo.save(c2);
		} catch (Exception e) {
			ValidationError.isConstraintViolation(e);
			throw new RuntimeException("Erro ao atualizar Cliente!");
		}
	}

	public void delete(Long id) {
		Cliente c = findById(id);
		checkDeleteCliente(c);
		try {
			repo.delete(c);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao excluir Cliente!");
		}
	}

	private void checkDeleteCliente(Cliente c) {
		List<Conta> contas = contaRepo.findByClienteId(c.getId());
		if (contas.size() > 0)
			throw new NotDeletableException("Nao pode excluir clientes que ja possui contas!");
	}

	public void alterarSenha(Cliente c, String nova_senha) {
		Cliente cdb = findById(c.getId());
		checkSenhaAtual(cdb, c.getSenha());
		try {
			cdb.setSenha(new BCryptPasswordEncoder().encode(nova_senha));
			repo.save(cdb);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao alterar senha!");
		}
	}

	private void checkSenhaAtual(Cliente c, String senha_atual) {
		BCryptPasswordEncoder crypt = new BCryptPasswordEncoder();
		if (!crypt.matches(senha_atual, c.getSenha()))
			throw new RuntimeException("Senha atual está incorreta!");
	}
}
