package edu.iff.sistemabanco.controller.apirest;

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

import edu.iff.sistemabanco.model.Cliente;
import edu.iff.sistemabanco.service.ClienteService;

@RestController
@RequestMapping(path = "/apirest/clientes")
public class ClienteController {

	@Autowired
	ClienteService serv;
	 
	@GetMapping
	public ResponseEntity<List<Cliente>> getAll(
			@RequestParam(name="page", defaultValue="0", required=false) int page,
			@RequestParam(name="size", defaultValue="10", required=false) int size
			) {
		return ResponseEntity.ok(serv.findAll(page, size));
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Cliente> getOne(@PathVariable("id") Long id){
		return ResponseEntity.ok(serv.findById(id));
	}
	
	@PostMapping()
	public ResponseEntity<Cliente> save(@Valid @RequestBody Cliente cliente) {
		cliente.setId(null);
		serv.save(cliente);
		return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<Cliente> update(@PathVariable("id") Long id, @Valid @RequestBody Cliente cliente) {
		cliente.setId(id);
		serv.save(cliente);
		return ResponseEntity.ok(cliente);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Cliente> delete(@PathVariable("id") Long id){
		serv.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
}
