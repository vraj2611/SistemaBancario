package edu.iff.sistemabanco.controller.apirest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.iff.sistemabanco.model.Transacao;
import edu.iff.sistemabanco.service.TransacaoService;

@RestController
@RequestMapping(path = "/apirest/transacoes")
public class TransacaoController {

	@Autowired
	TransacaoService serv;

	@GetMapping
	public ResponseEntity<List<Transacao>> getAll(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "size", defaultValue = "10", required = false) int size) {
		return ResponseEntity.ok(serv.findAll(page, size));
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Transacao> getOne(@PathVariable("id") Long id) {
		return ResponseEntity.ok(serv.findById(id));
	}

}
