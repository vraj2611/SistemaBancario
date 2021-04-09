package edu.iff.sistemabanco.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.iff.sistemabanco.model.Cliente;
import edu.iff.sistemabanco.model.Conta;
import edu.iff.sistemabanco.model.Operador;
import edu.iff.sistemabanco.model.PacoteServico;
import edu.iff.sistemabanco.model.Permissao;
import edu.iff.sistemabanco.model.Transacao;
import edu.iff.sistemabanco.model.TransacaoDto;
import edu.iff.sistemabanco.repository.PermissaoRepository;

// SERVICO PARA PRRENCHER O BANCO DE DADOS
// É CHAMADO POR UMA ROTA EM OperadorController
@Service
public class GeradorDadosAleatoriosService {

	@Autowired
	private PacoteServicoService pctServ;

	@Autowired
	private OperadorService opServ;

	@Autowired
	private ClienteService clienteServ;

	@Autowired
	private ContaService contaServ;

	@Autowired
	private TransacaoService tServ;
	
	@Autowired
	private PermissaoRepository permRepo;

	public void preencherBancoDeDados() {
		Permissao p1 = new Permissao();
        p1.setNome("ADMIN");
        Permissao p2 = new Permissao();
        p2.setNome("FUNC");
        permRepo.saveAll(List.of(p1, p2));
		
		criarOperadores(p1);
		criarClientes(p2);
		criarPacotesServicos();
		criarContas();
		criarTransacoes();
	}
	
	private void criarPacotesServicos() {
		PacoteServico pct = new PacoteServico();
		pct.setNome("BASICO");
		pct.setQtde_retiradas(1);
		pct.setDia_vencimento(15);
		pct.setTaxa_rendimento(0.05);
		pct.setValor_mensalidade(19.99);
		pct.setValor_retiradas(4.99);
		pct.setLimite_preaprovado(100);
		pctServ.save(pct);

		PacoteServico pct2 = new PacoteServico();
		pct2.setNome("MASTER");
		pct2.setQtde_retiradas(2);
		pct2.setDia_vencimento(15);
		pct2.setTaxa_rendimento(0.09);
		pct2.setValor_mensalidade(19.99);
		pct2.setValor_retiradas(14.99);
		pct2.setLimite_preaprovado(200);
		pctServ.save(pct2);

		PacoteServico pct3 = new PacoteServico();
		pct3.setNome("PREMIUM");
		pct3.setQtde_retiradas(3);
		pct3.setDia_vencimento(15);
		pct3.setTaxa_rendimento(0.15);
		pct3.setValor_mensalidade(99.99);
		pct3.setValor_retiradas(4.99);
		pct3.setLimite_preaprovado(300);
		pctServ.save(pct3);
	}

	private void criarOperadores(Permissao perm) {
		String[] nomes = { "Gabriela Rangel", "Jair Araujo", "Debora Gomes" };
		String[] cpfs = { "89322690097", "99905616055", "40644174021" };
		for (int i = 0; i < nomes.length; i++) {
			Operador op = new Operador();
			op.setPermissoes(List.of(perm));
			op.setCpf(cpfs[i]);
			op.setNome(nomes[i]);
			op.setSenha("abcde123");
			opServ.save(op);
		}
	}

	private void criarClientes(Permissao perm) {
		String[] nomes = { "Ivy Nogueira", "Carmo Barreto", "Alexandre Carvalho", "Josiane Lopes" };
		String[] cpfs = { "87962719060", "27628423019", "91847876005", "05588939026" };
		for (int i = 0; i < nomes.length; i++) {
			Cliente cl = new Cliente();
			cl.setPermissoes(List.of(perm));
			cl.setCpf(cpfs[i]);
			cl.setNome(nomes[i]);
			cl.setSenha("abcde123");
			clienteServ.save(cl);
		}

	}

	private void criarContas() {
		List<Cliente> clientes = clienteServ.findAll();
		List<PacoteServico> pcts = pctServ.findAll();
		for (Cliente c : clientes) {
			Conta c1 = Conta.criar(c, "SALARIO", pcts.get(0));
			contaServ.save(c1);
			Conta c2 = Conta.criar(c, "INVESTIMENTO", pcts.get(1));
			contaServ.save(c2);
		}
		Conta c3 = Conta.criar(clientes.get(0), "SECRETA", pcts.get(2));
		contaServ.save(c3);
	}

	private void criarTransacoes() {
		Random rnd = new Random();
		List<Conta> contas = contaServ.findAll();
		List<Operador> ops = opServ.findAll();
				
		for (Conta c : contas) {
			TransacaoDto dto = new TransacaoDto(1999 + (9 * rnd.nextDouble()), "DEP abc", null, null);
			contaServ.depositar(c.getId(), dto);
			TransacaoDto dto2 = new TransacaoDto(2999 + (9 * rnd.nextDouble()), "DEP def", null, null);
			contaServ.depositar(c.getId(), dto2);
			TransacaoDto dto3 = new TransacaoDto(4999 + (9 * rnd.nextDouble()), "DEP ghi", null, null);
			contaServ.depositar(c.getId(), dto3);
		}
		autorizarTransacoes(ops.get(0));
		
		for (Conta c : contas) {
			TransacaoDto dto = new TransacaoDto(999 + (9 * rnd.nextDouble()), "RET abc", null, null);
			contaServ.retirar(c.getId(), dto);
			TransacaoDto dto2 = new TransacaoDto(499 + (9 * rnd.nextDouble()), "RET def", null, null);
			contaServ.retirar(c.getId(), dto2);
			TransacaoDto dto3 = new TransacaoDto(499 + (9 * rnd.nextDouble()), "RET ghi", null, null);
			contaServ.retirar(c.getId(), dto3);
		}
		autorizarTransacoes(ops.get(1));

		for (Conta c : contas) {
			if(c.getId()==3) continue;
			TransacaoDto dto = new TransacaoDto(4999 + (9 * rnd.nextDouble()), "transf ok", contas.get(2).getId(), null);
			contaServ.transferir(c.getId(), dto);
		}
		autorizarTransacoes(ops.get(2));

		for (Conta c : contas) {
			TransacaoDto dto = new TransacaoDto(999 + (9 * rnd.nextDouble()), "RET bloq", null,
					null);
			contaServ.retirar(c.getId(), dto);
			contaServ.debitarCustos(c, ops.get(0));
			contaServ.pagarRendimentos(c, ops.get(1));
		}
	}

	private void autorizarTransacoes(Operador o) {
		List<Transacao> pends = tServ.findPendentes();
		for (Transacao t : pends) {
			contaServ.autorizar(o, t.getId());
		}
	}

}
