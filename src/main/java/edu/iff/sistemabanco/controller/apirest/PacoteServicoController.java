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

import edu.iff.sistemabanco.model.PacoteServico;
import edu.iff.sistemabanco.service.PacoteServicoService;

@RestController
@RequestMapping(path = "/apirest/pacotes")
public class PacoteServicoController {

	@Autowired
	PacoteServicoService serv;
	 
	@GetMapping
	public ResponseEntity<List<PacoteServico>> getAll(
			@RequestParam(name="page", defaultValue="0", required=false) int page,
			@RequestParam(name="size", defaultValue="10", required=false) int size
			) {
		return ResponseEntity.ok(serv.findAll(page, size));
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<PacoteServico> getOne(@PathVariable("id") Long id){
		return ResponseEntity.ok(serv.findById(id));
	}
	
	@PostMapping()
	public ResponseEntity<PacoteServico> save(@Valid @RequestBody PacoteServico p) {
		p.setId(null);
		serv.save(p);
		return ResponseEntity.status(HttpStatus.CREATED).body(p);
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<PacoteServico> update(@PathVariable("id") Long id, @Valid @RequestBody PacoteServico p) {
		p.setId(id);
		serv.save(p);
		return ResponseEntity.ok(p);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<PacoteServico> delete(@PathVariable("id") Long id){
		serv.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
}
