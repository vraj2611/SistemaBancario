package edu.iff.sistemabanco.controller.apirest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.iff.sistemabanco.model.Cliente;
import edu.iff.sistemabanco.model.Conta;
import edu.iff.sistemabanco.model.ContaDto;
import edu.iff.sistemabanco.model.PacoteServico;
import edu.iff.sistemabanco.model.Transacao;
import edu.iff.sistemabanco.model.TransacaoDto;
import edu.iff.sistemabanco.model.Transferencia;
import edu.iff.sistemabanco.service.ClienteService;
import edu.iff.sistemabanco.service.ContaService;
import edu.iff.sistemabanco.service.PacoteServicoService;
import edu.iff.sistemabanco.service.TransacaoService;

@RestController
@RequestMapping(path = "/apirest/contas")
public class ContaController {

	@Autowired
	ContaService serv;

	@Autowired
	TransacaoService tServ;
	
	@Autowired
	ClienteService cltServ;
	
	@Autowired
	PacoteServicoService pctServ;

	@GetMapping
	public ResponseEntity<List<Conta>> getAll(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "size", defaultValue = "10", required = false) int size) {
		return ResponseEntity.ok(serv.findAll(page, size));
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Conta> getOne(@PathVariable("id") Long id) {
		return ResponseEntity.ok(serv.findById(id));
	}

	@GetMapping(path = "/{id}/transacoes")
	public ResponseEntity<List<Transacao>> getTransacoes(@PathVariable("id") Long id) {
		return ResponseEntity.ok(tServ.findAllByContaId(id));
	}

	@PostMapping()
	public ResponseEntity<Conta> save(@RequestBody ContaDto dto) {
		Cliente clt = cltServ.findById(dto.getCliente_id());
		PacoteServico pct = pctServ.findById(dto.getPacote_id());
		Conta c = Conta.criar(clt, dto.getNome(), pct);
		return ResponseEntity.status(HttpStatus.CREATED).body(serv.save(c));
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<Conta> update(@PathVariable("id") Long id, @RequestBody ContaDto dto) {
		Conta c = serv.findById(id);
		PacoteServico pct = pctServ.findById(dto.getPacote_id());
		c.setNome(dto.getNome());
		c.setPacote(pct);
		return ResponseEntity.ok(serv.save(c));
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Conta> delete(@PathVariable("id") Long id) {
		serv.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping(path = "/{id}/depositar")
	public ResponseEntity<Transacao> depositar(
			@PathVariable("id") Long id, @RequestBody TransacaoDto dto) {
		return ResponseEntity.ok(serv.depositar(id, dto));
	}
	
	@PutMapping(path = "/{id}/retirar")
	public ResponseEntity<Transacao> retirar(
			@PathVariable("id") Long id, @RequestBody TransacaoDto dto) {
		return ResponseEntity.ok(serv.retirar(id, dto));
	}
	
	@PutMapping(path = "/{id}/transferir")
	public ResponseEntity<Transferencia> transferir(
			@PathVariable("id") Long id, @RequestBody TransacaoDto dto) {
		return ResponseEntity.ok(serv.transferir(id, dto));
	}

}
