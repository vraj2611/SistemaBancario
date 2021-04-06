package edu.iff.sistemabanco.controller.view;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.iff.sistemabanco.model.Cliente;
import edu.iff.sistemabanco.service.ClienteService;
import edu.iff.sistemabanco.service.ContaService;

@Controller
@RequestMapping(path = "/clientes")
public class ClienteViewController {

	@Autowired
	ClienteService serv;
	
	@Autowired
	ContaService contaServ;

	@GetMapping
	public String getAll(Model model) {
		model.addAttribute("clientes", serv.findAll());
		return "clientes";
	}

	@GetMapping(path = "/cliente")
	public String cadastro(Model model) {
		model.addAttribute("cliente", new Cliente());
		return "formCliente";
	}

	@PostMapping(path = "/cliente")
	public String save(@Valid @ModelAttribute Cliente cliente, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("msgErros", result.getAllErrors());
			return "formCliente";
		}
		cliente.setId(null);
		try {
			serv.save(cliente);
			model.addAttribute("msgSucesso", "Cliente cadastrado com sucesso.");
			model.addAttribute("cliente", new Cliente());
			return "formCliente";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Cliente", e.getMessage()));
			return "formCliente";
		}
	}

	@GetMapping(path = "/cliente/{id}")
	public String alterar(@PathVariable("id") Long id, Model model) {
		model.addAttribute("cliente", serv.findById(id));
		model.addAttribute("contas", contaServ.findByClienteId(id));
		return "formCliente";
	}

	@PostMapping(path = "/cliente/{id}")
	public String update(@Valid @ModelAttribute Cliente cliente, BindingResult result, @PathVariable("id") Long id,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("msgErros", result.getAllErrors());
			return "formCliente";
		}
		cliente.setId(id);
		try {
			serv.update(cliente);
			model.addAttribute("msgSucesso", "Cliente atualizado com sucesso.");
			model.addAttribute("cliente", cliente);
			return "formCliente";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Cliente", e.getMessage()));
			return "formCliente";
		}
	}

	@GetMapping(path = "/{id}/deletar")
	public String deletar(@PathVariable("id") Long id) {
		serv.delete(id);
		return "redirect:/clientes";
	}
}
