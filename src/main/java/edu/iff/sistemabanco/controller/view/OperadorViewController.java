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
import org.springframework.web.bind.annotation.RequestParam;

import edu.iff.sistemabanco.model.Operador;
import edu.iff.sistemabanco.service.OperadorService;
import edu.iff.sistemabanco.service.TransacaoService;

@Controller
@RequestMapping(path = "/operadores")
public class OperadorViewController {

	@Autowired
	OperadorService serv;
	
	@Autowired
	TransacaoService tServ;

	@GetMapping
	public String getAll(Model model) {
		model.addAttribute("operadores", serv.findAll());
		return "operadores";
	}

	@GetMapping(path = "/operador")
	public String cadastro(Model model) {
		model.addAttribute("operador", new Operador());
		return "formOperador";
	}

	@GetMapping(path = "/operador/{id}")
	public String alterar(@PathVariable("id") Long id, Model model) {
		model.addAttribute("operador", serv.findById(id));
		model.addAttribute("transacoes", tServ.findByOperadorId(id));
		return "formOperador";
	}

	@PostMapping(path = "/operador")
	public String save(@Valid @ModelAttribute Operador operador, BindingResult result,
			@RequestParam("confirmarsenha") String confirmarSenha, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("msgErros", result.getAllErrors());
			return "formOperador";
		}

		if (!operador.getSenha().equals(confirmarSenha)) {
			model.addAttribute("msgErros",
					new ObjectError("operador", "Campos Senha e Confirmar Senha devem ser iguais."));
			return "formOperador";
		}

		try {
			operador.setId(null);
			serv.save(operador);
			model.addAttribute("msgSucesso", "Operador cadastrado com sucesso.");
			return "redirect:/operadores";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Operador", e.getMessage()));
			return "formOperador";
		}
	}

	@PostMapping(path = "/operador/{id}")
	public String update(@ModelAttribute Operador operador, BindingResult result, @PathVariable("id") Long id,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("msgErros", result.getAllErrors());
			return "formOperador";
		}
		
		try {
			operador.setId(id);
			serv.update(operador);
			model.addAttribute("msgSucesso", "Operador atualizado com sucesso.");
			model.addAttribute("operador", operador);
			return "formOperador";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Operador", e.getMessage()));
			return "formOperador";
		}
	}

	@GetMapping(path = "/{id}/deletar")
	public String deletar(@PathVariable("id") Long id, Model model) {
		try {
			serv.delete(id);
			model.addAttribute("msgSucesso", "Operador excluido com sucesso.");
			return "redirect:/operadores";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Operador", e.getMessage()));
			model.addAttribute("operador", serv.findById(id));
			return "formOperador";
		}
	}

	@PostMapping(path = "operador/{id}/alterarsenha")
	public String alterarSenha(@ModelAttribute Operador operador, BindingResult result,
			@RequestParam("confirma_senha") String confirmaSenha, @RequestParam("nova_senha") String novaSenha,Model model) {

		if (result.hasErrors()) {
			model.addAttribute("msgErros", result.getAllErrors());
			model.addAttribute("operador", operador);
			return "formOperador";
		}

		if (!novaSenha.equals(confirmaSenha)) {
			model.addAttribute("msgErros",
					new ObjectError("Operador", "Campos Nova Senha e Confirmar Senha devem ser iguais."));
			model.addAttribute("operador", operador);
			return "formOperador";
		}

		try {
			serv.alterarSenha(operador, novaSenha);
			model.addAttribute("msgSucesso", "Senha alterada com sucesso.");
			model.addAttribute("operador", operador);
			return "formOperador";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("Operador", e.getMessage()));
			model.addAttribute("operador", operador);
			return "formOperador";
		}
	}
}
