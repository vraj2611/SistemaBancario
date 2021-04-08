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

import edu.iff.sistemabanco.model.PacoteServico;
import edu.iff.sistemabanco.service.ContaService;
import edu.iff.sistemabanco.service.PacoteServicoService;

@Controller
@RequestMapping(path = "/pacotes")
public class PacoteServicoViewController {
	
	@Autowired
	PacoteServicoService serv;
	
	@Autowired
	ContaService cServ;
	
	@GetMapping
	public String getAll(Model model) {
		model.addAttribute("pacotes", serv.findAll());
		return "pacotes";
	}

	@GetMapping(path = "/pacote")
	public String cadastro(Model model) {
		model.addAttribute("pacote", new PacoteServico());
		return "formPacoteServico";
	}

	@PostMapping(path = "/pacote")
	public String save(@Valid @ModelAttribute PacoteServico pacote, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("msgErros", result.getAllErrors());
			return "formPacoteServico";
		}
		pacote.setId(null);
		try {
			serv.save(pacote);
			model.addAttribute("msgSucesso", "PacoteServico cadastrado com sucesso.");
			model.addAttribute("pacote", new PacoteServico());
			return "formPacoteServico";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("PacoteServico", e.getMessage()));
			return "formPacoteServico";
		}
	}

	@GetMapping(path = "/pacote/{id}")
	public String alterar(@PathVariable("id") Long id, Model model) {
		model.addAttribute("pacote", serv.findById(id));
		model.addAttribute("contas", cServ.findByPacoteId(id));
		return "formPacoteServico";
	}

	@PostMapping(path = "/pacote/{id}")
	public String update(@Valid @ModelAttribute PacoteServico pacote, BindingResult result, @PathVariable("id") Long id,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("msgErros", result.getAllErrors());
			return "formPacoteServico";
		}
		
		try {
			pacote.setId(id);
			serv.update(pacote);
			model.addAttribute("msgSucesso", "PacoteServico atualizado com sucesso.");
			model.addAttribute("pacote", pacote);
			model.addAttribute("contas", cServ.findByPacoteId(id));
			return "formPacoteServico";
		} catch (Exception e) {
			model.addAttribute("msgErros", new ObjectError("PacoteServico", e.getMessage()));
			return "formPacoteServico";
		}
	}

	@GetMapping(path = "/{id}/deletar")
	public String deletar(@PathVariable("id") Long id) {
		serv.delete(id);
		return "redirect:/pacotes";
	}
	
}
