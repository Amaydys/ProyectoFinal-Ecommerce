	package com.resgistros.crochet.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.resgistros.crochet.model.Orden;
import com.resgistros.crochet.model.Producto;
import com.resgistros.crochet.model.Usuario;
import com.resgistros.crochet.service.IOrdenService;
import com.resgistros.crochet.service.IUsuarioService;
import com.resgistros.crochet.service.ProductoService;

@Controller
@RequestMapping("/administrador")

public class AdministradorController {
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordensService;
	
	private Logger logg = LoggerFactory.getLogger(AdministradorController.class);

	@GetMapping ("")
	public String home(Model model) {
		List<Producto> productos=productoService.findAll();
		model.addAttribute("productos", productos);
		return "administrador/home";
	}
	
	@GetMapping("/usuarios")
	public String usuarios(Model model) {
		model.addAttribute("usuarios", usuarioService.findAll());
		return "administrador/usuarios";
		
	}
	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		model.addAttribute("ordenes",ordensService.findAll());
		return "administrador/ordenes";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalle(Model model, @PathVariable Integer id) {
		logg.info("id  de la orden {}",id);
	    Orden orden= ordensService.findById(id).get();
	    
	    model.addAttribute("detalles", orden.getDetalle());
		return "administrador/detalleorden";
		
	}
	
	@GetMapping("/compras")
	public String obtenerCompras(Model model, HttpSession session) {
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		Usuario usuario= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		List<Orden> ordenes= ordensService.findByUsuario(usuario);
		
		model.addAttribute("ordenes", ordenes);
		return "administrador/compras";
		
	}
}

