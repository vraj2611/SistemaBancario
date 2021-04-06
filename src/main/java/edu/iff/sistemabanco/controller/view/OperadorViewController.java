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

import edu.iff.sistemabanco.model.Operador;
import edu.iff.sistemabanco.service.OperadorService;

@Controller
@RequestMapping(path = "/operadores")
public class OperadorViewController {

	@Autowired
	OperadorService serv;
		
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
    
    @PostMapping(path = "/operador")
    public String save(@Valid @ModelAttribute Operador operador, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("msgErros", result.getAllErrors());
            return "formOperador";
        }
        operador.setId(null);
        try {
            serv.save(operador);
            model.addAttribute("msgSucesso", "Operador cadastrado com sucesso.");
            model.addAttribute("operador", new Operador());
            return "formOperador";
        } catch (Exception e) {
            model.addAttribute("msgErros", new ObjectError("Operador", e.getMessage()));
            return "formOperador";
        }
    }
    
    @GetMapping(path = "/operador/{id}")
    public String alterar(@PathVariable("id") Long id,Model model) {
        model.addAttribute("operador", serv.findById(id));
        return "formOperador";
    }
    
    @PostMapping(path = "/operador/{id}")
    public String update(@Valid @ModelAttribute Operador operador, BindingResult result, @PathVariable("id") Long id, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("msgErros", result.getAllErrors());
            return "formOperador";
        }
        operador.setId(id);
        try {
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
    public String deletar(@PathVariable("id") Long id) {
        serv.delete(id);
        return "redirect:/operadores";
    }
}
