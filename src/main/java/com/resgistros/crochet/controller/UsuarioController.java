package com.resgistros.crochet.controller;


import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.resgistros.crochet.model.Orden;
import com.resgistros.crochet.model.Usuario;
import com.resgistros.crochet.service.IOrdenService;
import com.resgistros.crochet.service.IUsuarioService;



@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	BCryptPasswordEncoder passEncode= new BCryptPasswordEncoder();

	// para que me muestre la pagina de registro en la parte inferior,
	// usuario/registro
	@GetMapping("/registro")
	public String mostrarFormulario(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "usuario/registro";
	}

	// trae los datos ya mapeados Usuario usuario
	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result) {
		if(result.hasErrors()) {
			return "usuario/registro";
		}
		logger.info("Usuario registro: {}", usuario);
		usuario.setTipo("USER");
		usuario.setPassword( passEncode.encode(usuario.getPassword()));
		usuarioService.save(usuario);

		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}

	@GetMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		logger.info("Accesos: {}", usuario);
		//redireccionando como administrador
		
		Optional<Usuario> user = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()));
		//logger.info("usuario de base de datos: {}", user.get());
		if (user.isPresent()) {

			session.setAttribute("idusuario", user.get().getId());
			if (user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";

			} else {
				return "redirect:/";

			}
		} else {
			logger.info("Usuario no existe");
		}

		return "redirect:/";
	}
	@GetMapping("/compras")
	public String obtenerCompras(Model model, HttpSession session) {
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		Usuario usuario= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		List<Orden> ordenes= ordenService.findByUsuario(usuario);
		
		model.addAttribute("ordenes", ordenes);
		return "usuario/compras";
		
	}
	@GetMapping("/cerrar")
	public String cerrarSersion(HttpSession session) {
		session.removeAttribute("idusuario");
		return "redirect:/";
	}
	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
Optional<Orden> orden=ordenService.findById(id);
		
		model.addAttribute("detalles", orden.get().getDetalle());
		
		
		//session
		model.addAttribute("sesion", session.getAttribute("idusuario")); 
		return "usuario/detalleCompra";
		
	}
	
	}
	
