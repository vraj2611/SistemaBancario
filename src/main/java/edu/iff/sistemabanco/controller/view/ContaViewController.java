package edu.iff.sistemabanco.controller.view;

import java.util.Calendar;

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
import edu.iff.sistemabanco.model.Conta;
import edu.iff.sistemabanco.service.ClienteService;
import edu.iff.sistemabanco.service.ContaService;
import edu.iff.sistemabanco.service.PacoteServicoService;
import edu.iff.sistemabanco.service.TransacaoService;

@Controller
@RequestMapping(path = "/contas")
public class ContaViewController {

	@Autowired
	ContaService serv;

	@Autowired
	PacoteServicoService pServ;
	
	@Autowired
	ClienteService cServ;
	
	@Autowired
	TransacaoService tServ;

	@GetMapping
	public String getAll(Model model) {
		model.addAttribute("contas", serv.findAll());
		return "contas";
	}
	
	@GetMapping(path = "/conta")
	public String cadastro(Model model) {
		Conta c = new Conta();
		c.setSaldo(0);
		c.setDatahora(Calendar.getInstance());
		model.addAttribute("conta", c);
		model.addAttribute("clientes", cServ.findAll());
		model.addAttribute("pacotes", pServ.findAll());
		return "formConta";
	}

	@GetMapping(path = "/conta/{id}")
	public String alterar(@PathVariable("id") Long id, Model model) {
		Conta c = serv.findById(id);
		model.addAttribute("conta", c);
		model.addAttribute("clientes", c.getCliente());
		model.addAttribute("pacotes", pServ.findAll());
		model.addAttribute("transacoes", tServ.findByContaId(id));
		return "formConta";
	}

	@PostMapping(path = "/conta")
	public String save(@Valid @ModelAttribute Conta conta, BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("msgErros", result.getAllErrors());
			model.addAttribute("clientes", cServ.findAll());
			model.addAttribute("pacotes", pServ.findAll());
			return "formConta";
		}

		try {
			conta.setId(null);
			serv.save(conta);
			model.addAttribute("msgSucesso", "Conta cadastrado com sucesso.");
			return "redirect:/contas";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Conta", e.getMessage()));
			model.addAttribute("clientes", cServ.findAll());
			model.addAttribute("pacotes", pServ.findAll());
			return "formConta";
		}
	}

	@PostMapping(path = "/conta/{id}")
	public String update(@ModelAttribute Conta conta, BindingResult result, @PathVariable("id") Long id,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("msgErros", result.getAllErrors());
			Conta c = serv.findById(id);
			model.addAttribute("conta", c);
			model.addAttribute("clientes", c.getCliente());
			model.addAttribute("pacotes", pServ.findAll());
			return "formConta";
		}
		
		try {
			conta.setId(id);
			Conta c = serv.update(conta);
			model.addAttribute("msgSucesso", "Cliente atualizado com sucesso.");
			model.addAttribute("conta", c);
			model.addAttribute("clientes", c.getCliente());
			model.addAttribute("pacotes", pServ.findAll());
			model.addAttribute("transacoes", tServ.findByContaId(id));
			return "formConta";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Cliente", e.getMessage()));
			Conta c = serv.findById(id);
			model.addAttribute("conta", c);
			model.addAttribute("clientes", c.getCliente());
			model.addAttribute("pacotes", pServ.findAll());
			return "formConta";
		}
	}

	@GetMapping(path = "/{id}/deletar")
	public String deletar(@PathVariable("id") Long id, Model model) {
		try {
			serv.delete(id);
			model.addAttribute("msgSucesso", "Conta excluida com sucesso.");
			return "redirect:/contas";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Conta", e.getMessage()));
			model.addAttribute("conta", serv.findById(id));
			return "contas";
		}
	}
}
