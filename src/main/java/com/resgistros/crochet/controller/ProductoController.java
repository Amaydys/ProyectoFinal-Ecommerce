package com.resgistros.crochet.controller;



import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.resgistros.crochet.model.Producto;
import com.resgistros.crochet.model.Usuario;
import com.resgistros.crochet.repository.IProductoRepository;
import com.resgistros.crochet.service.IUsuarioService;
import com.resgistros.crochet.service.ProductoService;
import com.resgistros.crochet.service.UploadFileService;




@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

     @Autowired
     private IUsuarioService usuarioService;
     
    @Autowired
    private UploadFileService Upload;

    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    @GetMapping("/create")
    public String create() {
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        LOGGER.info("Este es el objeto producto {}", producto);
        Usuario u= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString() )).get();
        producto.setUsuario(u);
        
        // imagen
        if (producto.getId() == null) {
            String nombreImagen = Upload.saveImage(file);
            producto.setImagen(nombreImagen);
        } else {
            String nombreImagen = Upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }

        productoService.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Producto producto=new Producto();
        Optional<Producto> optionalProducto=productoService.get(id);
        producto = optionalProducto.get();

        LOGGER.info("producto buscado: {}", producto);
        model.addAttribute("producto", producto);

        return "productos/edit";
    }
    

    
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
    	
		productoService.delete(id);
		return "redirect:/productos";
	}
   
    
 
    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
    	 Producto p = new Producto();
         p=productoService.get(producto.getId()).get();
    	
        if (file.isEmpty()) { //editamos el producto pero no cambiamos la imagen
           
            producto.setImagen(p.getImagen());
        } else { 
        	// cuando se edita tambien la imagen
             //eliminar cuando no sea la imagen por defecto
            if (!p.getImagen().equals("default.jpg")) {
                Upload.deleteImage(p.getImagen());
            }
         String nombreImagen = Upload.saveImage(file);
         producto.setImagen(nombreImagen);
          
        }
        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        return "redirect:/productos";
    }
    

}
    
