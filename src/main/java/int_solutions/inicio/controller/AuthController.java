package int_solutions.inicio.controller;

import int_solutions.inicio.entities.AuthRequest;
import int_solutions.inicio.entities.dto.UserRegisterDTO;
import int_solutions.inicio.entities.dto.UserUpdateDTO;
import int_solutions.inicio.repositories.UserRepository;
import int_solutions.inicio.security.JwtUtil;
import int_solutions.inicio.service.UserDetailsServiceImpl;
import int_solutions.inicio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import int_solutions.inicio.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import int_solutions.inicio.entities.dto.AuthResponse;

import java.util.List;

@RestController
@RequestMapping ("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/admin/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRegisterDTO dto){
        try {
            User user = userService.register(dto);
            String token = jwtUtil.generateToken(user.getUsername(), user.getGroupName());
            System.out.println("Grupo recibido: " + user.getGroupName());
            AuthResponse response = new AuthResponse("Usuario registrado con éxito: " + user.getUsername(), token, user.getGroupName());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(e.getMessage(), null,null));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){

        try {


           Authentication auth = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           authRequest.getUsername(),
                           authRequest.getPassword())
                   );
                   User user = userService.finByUsername(authRequest.getUsername());
                   String token = jwtUtil.generateToken(user.getUsername(), user.getGroupName());
                   AuthResponse response = new AuthResponse("Logueo Exitoso ", token, user.getGroupName());
                   return ResponseEntity.ok(response);
       } catch (Exception e){
           return ResponseEntity.status(401).body(new AuthResponse("Credenciales inválidas ", null, null));
       }

    }

    @GetMapping("/Allusers")
    public ResponseEntity<List<User>> getAllUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(">>>> Roles Actuales: " + auth.getAuthorities());
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/users/ur/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        try{
            userService.updateUser(id, dto);
            return ResponseEntity.ok("Usuario actualizado con exito");
        } catch (RuntimeException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el usuario");
          }
    }

    @DeleteMapping("/admin/deleteus/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuario eliminado exitosamente");
    }
}


