package com.entra21.eventoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.entra21.eventoapp.models.Evento;
import com.entra21.eventoapp.repository.EventoRepository;

@Controller
public class EventoController {

	@Autowired
	private EventoRepository er;

	// PEGA O FORMULARIO A PARTIT DA REQUISICAO E MOSTRA NA WEB
	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.GET)
	public String form() {
		return "evento/formEvento";
	}

	// SALVA OS DADOS PASSADOS NO FORMULARIO
	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.POST)
	public String form(Evento evento) {
		er.save(evento);
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
	@RequestMapping("/{codigo}")
	public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo) {
		Evento evento = er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("evento", evento);
		return mv;
	}
}
