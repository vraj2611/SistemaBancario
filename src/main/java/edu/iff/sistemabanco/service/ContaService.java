package edu.iff.sistemabanco.service;

import java.util.Calendar;
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
import edu.iff.sistemabanco.model.CustoOperacional;
import edu.iff.sistemabanco.model.Deposito;
import edu.iff.sistemabanco.model.Operador;
import edu.iff.sistemabanco.model.Rendimento;
import edu.iff.sistemabanco.model.Retirada;
import edu.iff.sistemabanco.model.Transacao;
import edu.iff.sistemabanco.model.TransacaoDto;
import edu.iff.sistemabanco.model.Transferencia;
import edu.iff.sistemabanco.repository.ContaRepository;

@Service
public class ContaService {

	@Autowired
	private ContaRepository repo;

	@Autowired
	private TransacaoService tServ;

	public List<Conta> findAll(int page, int size) {
		Pageable p = PageRequest.of(page, size);
		return repo.findAll(p).toList();
	}

	public List<Conta> findAll() {
		return repo.findAll();
	}

	public Conta findById(Long id) {
		Optional<Conta> result = repo.findById(id);
		if (result.isEmpty())
			throw new NotFoundException("Conta nao encontrada!");
		return result.get();
	}

	public List<Conta> findByClienteId(Long id){
		return repo.findByClienteId(id);
	}
	
	public List<Conta> findByPacoteId(Long id){
		return repo.findByPacoteId(id);
	}
	
	public Conta save(Conta conta) {
		try {
			conta.setId(null);
			conta.setSaldo(0);
			conta.setDatahora(Calendar.getInstance());
			return repo.save(conta);
		} catch (Exception e) {
			ValidationError.isConstraintViolation(e);
			throw new RuntimeException("Erro ao salvar Conta!");
		}
	}
	
	public Conta update(Conta conta) {
		Conta cdb = findById(conta.getId());
		try {
			cdb.setPacote(conta.getPacote());
			cdb.setNome(conta.getNome());
			return repo.save(conta);
		} catch (Exception e) {
			ValidationError.isConstraintViolation(e);
			throw new RuntimeException("Erro ao salvar Conta!");
		}
	}

	public void delete(Long id) {
		Conta c = findById(id);
		checkDeleteConta(c);
		try {
			repo.delete(c);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao excluir Conta!");
		}
	}

	private void checkDeleteConta(Conta c) {
		List<Transacao> transacoes = tServ.findByContaId(c.getId());
		if (transacoes.size() > 0)
			throw new NotDeletableException("Nao pode excluir contas que ja fizeram transacoes!");
	}

	public Conta getContaDetail(Long conta_id) {
		Conta c = findById(conta_id);
		c.setTransacoes(tServ.findAllByContaId(c.getId()));
		/*System.out.println("=== Conta "+c.getId());
		for(Transacao t:c.getTransacoes()) {
			System.out.println(t.getValorParaSaldo(c)+" "+t.getTipo()+" "+t.getId()+" "+t.isAutorizada());
		}
		System.out.println("============");*/
		return c;
	}

	public List<Transacao> getTransacoes(Long conta_id) {
		Conta c = findById(conta_id);
		return tServ.findByContaId(c.getId());
	}

	public List<Transacao> getAllTransacoes(Long conta_id) {
		Conta c = findById(conta_id);
		return tServ.findAllByContaId(c.getId());
	}

	public Deposito depositar(Long id, TransacaoDto dto) {
		Conta c = getContaDetail(id);
		Deposito d = c.depositar(dto.getValor(), dto.getDescricao());
		repo.save(c);
		return (Deposito) tServ.save(d);
	}

	public Retirada retirar(Long id, TransacaoDto dto) {
		Conta c = getContaDetail(id);
		Retirada r = c.retirar(dto.getValor(), dto.getDescricao());
		repo.save(c);
		return (Retirada) tServ.save(r);
	}

	public Transferencia transferir(Long id, TransacaoDto dto) {
		Conta c = getContaDetail(id);
		Conta c_destino = findById(dto.getId_conta_destino());
		Transferencia t = c.transferir(dto.getValor(), c_destino, dto.getDescricao());
		repo.save(c);
		return (Transferencia) tServ.save(t);
	}

	public CustoOperacional debitarCustos(Conta c, Operador o) {
		c.setTransacoes(tServ.findByContaId(c.getId()));
		CustoOperacional custo = c.debitarCustos(o, "PGTO PCT "+c.getPacote().getNome());
		repo.save(c);
		return (CustoOperacional) tServ.save(custo);
	}

	public Rendimento pagarRendimentos(Conta c, Operador o) {
		c.setTransacoes(tServ.findByContaId(c.getId()));
		Rendimento r = c.pagarRendimentos(o, "REND SOBRE SALDO "+c.getPacote().getTaxa_rendimento());
		repo.save(c);
		return (Rendimento) tServ.save(r);
	}
	
	public Transacao autorizar(Operador o, Long transacao_id) {
		Transacao t = tServ.autorizar(o, transacao_id);
		Conta c = getContaDetail(t.getConta().getId());
		c.atualizarSaldo();
		repo.save(c);
		
		if(t instanceof Transferencia) {
			Transferencia t2 = (Transferencia) t;
			Conta c_dest = getContaDetail(t2.getConta_destino().getId());
			c_dest.atualizarSaldo();
			repo.save(c_dest);
		}
		return t;
	}
	
	public Transacao bloquear(Operador o, Long transacao_id) {
		Transacao t = tServ.bloquear(o, transacao_id);
		Conta c = getContaDetail(t.getConta().getId());
		c.atualizarSaldo();
		repo.save(c);
		return t;
	}
}
