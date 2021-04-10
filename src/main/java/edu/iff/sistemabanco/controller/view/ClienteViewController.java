package edu.iff.sistemabanco.controller.view;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.iff.sistemabanco.model.Cliente;
import edu.iff.sistemabanco.model.Conta;
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
	
	@GetMapping(path = "/cliente/{id}")
	public String alterar(@PathVariable("id") Long id, Model model) {
		model.addAttribute("cliente", serv.findById(id));
		List<Conta> contas = contaServ.findByClienteId(id);
		double saldo_cliente = 0;
		for(Conta c :contas) saldo_cliente += c.getSaldo(); 
		model.addAttribute("saldo_cliente", saldo_cliente);
		model.addAttribute("contas", contas);
		return "formCliente";
	}

	@GetMapping(path = "/minhascontas")
	public String minhascontas(@AuthenticationPrincipal User user, Model model) {
		Cliente clt = serv.findByCpf(user.getUsername()); 
		List<Conta> contas = contaServ.findByClienteId(clt.getId());
		double saldo_cliente = 0;
		for(Conta c :contas) saldo_cliente += c.getSaldo(); 
		model.addAttribute("cliente", clt);
		model.addAttribute("saldo_cliente", saldo_cliente);
		model.addAttribute("contas", contas);
		return "formCliente";
	}
	
	@PostMapping(path = "/cliente")
	public String save(@ModelAttribute Cliente cliente, BindingResult result,
			@RequestParam("confirmarsenha") String confirmarSenha, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("msgErros", result.getAllErrors());
			return "formCliente";
		}

		if (!cliente.getSenha().equals(confirmarSenha)) {
			model.addAttribute("msgErros",
					new ObjectError("Cliente", "Campos Senha e Confirmar Senha devem ser iguais."));
			return "formCliente";
		}

		try {
			cliente.setId(null);
			serv.save(cliente);
			model.addAttribute("msgSucesso", "Cliente cadastrado com sucesso.");
			return "redirect:/clientes";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Cliente", e.getMessage()));
			return "formCliente";
		}
	}

	@PostMapping(path = "/cliente/{id}")
	public String update(@ModelAttribute Cliente cliente, BindingResult result, @PathVariable("id") Long id,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("msgErros", result.getAllErrors());
			return "formCliente";
		}
		
		try {
			cliente.setId(id);
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
	public String deletar(@PathVariable("id") Long id, Model model) {
		try {
			serv.delete(id);
			model.addAttribute("msgSucesso", "Cliente excluido com sucesso.");
			return "redirect:/clientes";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Operador", e.getMessage()));
			model.addAttribute("Cliente", serv.findById(id));
			return "formCliente";
		}
	}
	
	@PostMapping(path = "cliente/{id}/alterarsenha")
	public String alterarSenha(@ModelAttribute Cliente cliente, BindingResult result,
			@RequestParam("confirma_senha") String confirmaSenha, @RequestParam("nova_senha") String novaSenha,Model model) {

		if (result.hasErrors()) {
			model.addAttribute("msgErros", result.getAllErrors());
			model.addAttribute("cliente", cliente);
			return "formCliente";
		}

		if (!novaSenha.equals(confirmaSenha)) {
			model.addAttribute("msgErros",
					new ObjectError("Cliente", "Campos Nova Senha e Confirmar Senha devem ser iguais."));
			model.addAttribute("cliente", cliente);
			return "formCliente";
		}

		try {
			serv.alterarSenha(cliente, novaSenha);
			model.addAttribute("msgSucesso", "Senha alterada com sucesso.");
			model.addAttribute("cliente", cliente);
			return "formCliente";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Cliente", e.getMessage()));
			model.addAttribute("cliente", cliente);
			return "formCliente";
		}
	}
}
