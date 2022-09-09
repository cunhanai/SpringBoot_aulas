package com.entra21.eventoapp.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.entra21.eventoapp.models.Convidado;
import com.entra21.eventoapp.models.Evento;
import com.entra21.eventoapp.repository.ConvidadoRepository;
import com.entra21.eventoapp.repository.EventoRepository;

@Controller
public class EventoController {

	@Autowired
	private EventoRepository er;

	@Autowired
	private ConvidadoRepository cr;

	// PEGA O FORMULARIO A PARTIT DA REQUISICAO E MOSTRA NA WEB
	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.GET)
	public String form() {
		return "evento/formEvento";
	}

	// SALVA OS DADOS PASSADOS NO FORMULARIO
	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.POST)
	public String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {

		// VERIFICA SE OS CAMPOS FORAM PREENCHIDOS
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/cadastrarEvento";
		}

		er.save(evento);
		attributes.addFlashAttribute("mensagem", "Evento adicionado com sucesso!");
		return "redirect:/cadastrarEvento";
	}

	// PEGA A LISTA DE EVENTOS DO BANCO DE DADOS
	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {
		// OBEJTO QUE RENDERIZA A PAGINA
		ModelAndView mv = new ModelAndView("evento/listaEvento");
		// PROCURA OS EVENTOS NO BANCO DE DADOS
		Iterable<Evento> eventos = er.findAll();
		mv.addObject("leventos", eventos); // leventos = atributo que esta no HTML
		return mv;
	}

	// CRIA UMA PAGINA DE DESCRICAO DO EVENTO
	@RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
	public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo) {
		Evento evento = er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("evento", evento);
		Iterable<Convidado> convidados = cr.findByEvento(evento);
		mv.addObject("convidados", convidados);
		return mv;
	}

	// SALVA OS CONVIDADOS NO FORMULARIO
	@RequestMapping(value = "/{codigo}", method = RequestMethod.POST)
	public String detalhesEventoPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado,
			BindingResult result, RedirectAttributes attributes) {

		// VERIFICA SE OS CAMPOS FORAM PREENCHIDOS
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/{codigo}";
		}

		Evento evento = er.findByCodigo(codigo);
		convidado.setEvento(evento);
		cr.save(convidado);
		attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
		return "redirect:/{codigo}";
	}

	// DELETAR EVENTOS
	@RequestMapping("/deletarEvento")
	public String deletarEvento(long codigo) {
		Evento evento = er.findByCodigo(codigo);
		er.delete(evento);
		return "redirect:/eventos";
	}

	// DELETAR CONVIDADOS
	@RequestMapping("/deletarConvidado")
	public String deletarConvidado(String rg) {
		Convidado convidado = cr.findByRg(rg);
		cr.delete(convidado);

		Evento evento = convidado.getEvento();
		long codigoLong = evento.getCodigo();
		String codigo = "" + codigoLong;

		return "redirect:/" + codigo;

	}

	// CRIA UMA PAGINA DE DESCRICAO DO EVENTO
	@RequestMapping(value = "/alterarEvento_{codigo}", method = RequestMethod.GET)
	public ModelAndView alterarEvento(@PathVariable("codigo") long codigo) {
		Evento evento = er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("evento", evento);
		return mv;
	}

	// SALVA OS DADOS PASSADOS NO FORMULARIO
	@RequestMapping(value = "/alterarEvento_{codigo}", method = RequestMethod.POST)
	public String alterarEvento(@PathVariable("codigo") long codigo, @Valid Evento evento, BindingResult result, RedirectAttributes attributes) {

		// VERIFICA SE OS CAMPOS FORAM PREENCHIDOS
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/cadastrarEvento";
		}

		er.save(evento);
		attributes.addFlashAttribute("mensagem", "Evento adicionado com sucesso!");
		return "redirect:/cadastrarEvento";
	}

}
