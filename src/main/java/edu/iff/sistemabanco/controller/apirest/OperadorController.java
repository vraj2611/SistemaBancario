package edu.iff.sistemabanco.controller.apirest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

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

import edu.iff.sistemabanco.model.Operador;
import edu.iff.sistemabanco.model.Transacao;
import edu.iff.sistemabanco.service.GeradorDadosAleatoriosService;
import edu.iff.sistemabanco.service.OperadorService;
import edu.iff.sistemabanco.service.TransacaoService;

@RestController
@RequestMapping(path = "/apirest/operadores")
public class OperadorController {

	@Autowired
	OperadorService serv;

	@Autowired
	GeradorDadosAleatoriosService gdaServ;
	
	@Autowired
	TransacaoService tServ;
	
	
	@GetMapping(path = "/preencher")
	public ResponseEntity<String> preencher(){
		// Rota para inserir dados para teste
		gdaServ.preencherBancoDeDados();
		Calendar c = Calendar.getInstance();
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String a = s.format(c.getTime());
		return ResponseEntity.ok("inserindo dados aleatorios! em "+a);
	}
	
	@GetMapping
	public ResponseEntity<List<Operador>> getAll(
			@RequestParam(name="page", defaultValue="0", required=false) int page,
			@RequestParam(name="size", defaultValue="10", required=false) int size
			) {
		return ResponseEntity.ok(serv.findAll(page, size));
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Operador> getOne(@PathVariable("id") Long id){
		return ResponseEntity.ok(serv.findById(id));
	}
	
	@PostMapping()
	public ResponseEntity<Operador> save(@Valid @RequestBody Operador operador) {
		operador.setId(null);
		serv.save(operador);
		return ResponseEntity.status(HttpStatus.CREATED).body(operador);
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<Operador> update(@PathVariable("id") Long id, @Valid @RequestBody Operador operador) {
		operador.setId(id);
		serv.save(operador);
		return ResponseEntity.ok(operador);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Operador> delete(@PathVariable("id") Long id){
		serv.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@GetMapping(path = "/{op_id}/pendentes")
	public ResponseEntity<List<Transacao>> getPendentes() {
		return ResponseEntity.ok(tServ.findPendentes());
	}
	
	@GetMapping(path = "/{op_id}/transacoes")
	public ResponseEntity<List<Transacao>> getTransacoes(@PathVariable("op_id") Long op_id) {
		return ResponseEntity.ok(tServ.findByOperadorId(op_id));
	}

	@PutMapping(path = "/{op_id}/pendentes/{id}/autorizar")
	public ResponseEntity<Transacao> autorizar(@PathVariable("op_id") Long op_id, @PathVariable("id") Long id) {
		Operador operador = serv.findById(op_id);
		return ResponseEntity.ok(tServ.autorizar(operador, id));
	}

	@PutMapping(path = "/{op_id}/pendentes/{id}/bloquear")
	public ResponseEntity<Transacao> bloquear(@PathVariable("op_id") Long op_id, @PathVariable("id") Long id) {
		Operador operador = serv.findById(op_id);
		return ResponseEntity.ok(tServ.bloquear(operador, id));
	}

	
}
