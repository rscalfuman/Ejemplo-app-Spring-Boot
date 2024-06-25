package sust.clima.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import sust.clima.daos.UsersDao;
import sust.clima.models.User;
import sust.clima.models.UserRepository;

@Controller
public class UserController {
  
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    UserRepository userRepo;

     @Autowired
     UsersDao usersDao;

 


    @GetMapping("/login")
  public String accesoPagina() {
    return "login.html";
  }

  @GetMapping("/register")
  public String registroPagina() {
    return "register.html";
  }

  @PostMapping ("/register")  
    public String registro (@RequestParam String username, @RequestParam String name, @RequestParam String password, @RequestParam String passConfirm, RedirectAttributes redAt, HttpSession session) {
        // 1. Si las contraseñas no coinciden, mandamos una alerta y redirigimos de
    // vuelta al formulario
    if (!password.equals(passConfirm)) {
        redAt.addFlashAttribute("mal", "Las contraseñas no coinciden");
        return "redirect:/register";
      }
       // 2. Si todo esta 'OK', creamos el usuario
    String password_encriptada = encoder.encode(password);
   boolean resultado = usersDao.create(username, name, password_encriptada, redAt, session);
    if (resultado) {
     return "redirect:/";
   }
    return "redirect:/register";
  }

  @PostMapping("/login")
  public String acceso(@RequestParam String username, @RequestParam String password, RedirectAttributes redAt,
  HttpSession session) {
     User u =  userRepo.findByUsername(username);
    if (u == null) {
    redAt.addFlashAttribute("mal", "Usuario inexistente o contraseña incorrecta");
    return "redirect:/login";
    }
    String password_encriptada = u.getPassword();
    if (!encoder.matches(password, password_encriptada)){
    redAt.addFlashAttribute("mal", "Usuario inexistente o contraseña incorrecta");
    return "redirect:/login";
     }
    session.setAttribute("user", u);
    session.setAttribute("user_session_id", u.getId());
    return "redirect:/";
   }

   
   @GetMapping("/logout")
  public String logout (HttpSession session) {
  //1 Eliminar la sesion 
  session.setAttribute("user", null);
  //2, Redirigir a la pantalla del login 
  return "redirect:/login";
  }


}
