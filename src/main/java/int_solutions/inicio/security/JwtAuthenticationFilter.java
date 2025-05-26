package int_solutions.inicio.security;

import int_solutions.inicio.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;


    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService){
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                                    throws ServletException, IOException{
        String path = request.getServletPath();
        if (path.equals("/auth/login") || path.equals("/auth/register")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(request);


        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token ausente");
            return;
        }


        //si el token es válido, autenticamos al usuario
            String username = jwtUtil.extractUsername(token);
            if (!jwtUtil.validationToken(token, username)) {
             response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
             response.getWriter().write("Token inválido o expirado");
              return;
            }


            String group = jwtUtil.extractGroup(token);
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    username, "",
                    List.of(new SimpleGrantedAuthority(group))
            );


           UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
           SecurityContextHolder.getContext().setAuthentication(authentication);
           System.out.println("Autoridades del usuario: " + userDetails.getAuthorities());

        filterChain.doFilter(request, response);
    }

    private  String extractToken(HttpServletRequest request){

        String header = request.getHeader("Authorization");

        if ( header != null && header.startsWith("Bearer ")){
            return  header.substring(7);
        }
        return null;
    }
}
