package com.example.demo.service;

import com.example.demo.dto.TopicoRequestDTO;
import com.example.demo.dto.TopicoResponseDTO;
import com.example.demo.model.Curso;
import com.example.demo.model.Topico;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.TopicoRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicoService {
    
    @Autowired
    private TopicoRepository topicoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Transactional
    public TopicoResponseDTO crearTopico(TopicoRequestDTO topicoRequest) {
        // Validar que no existe un tópico duplicado
        if (topicoRepository.existsByTituloAndMensaje(topicoRequest.titulo(), topicoRequest.mensaje())) {
            throw new IllegalArgumentException("Ya existe un tópico con el mismo título y mensaje");
        }
        
        // Buscar el autor
        Usuario autor = usuarioRepository.findById(topicoRequest.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + topicoRequest.autorId()));
        
        // Buscar el curso
        Curso curso = cursoRepository.findById(topicoRequest.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado con ID: " + topicoRequest.cursoId()));
        
        // Crear el tópico
        Topico topico = new Topico();
        topico.setTitulo(topicoRequest.titulo());
        topico.setMensaje(topicoRequest.mensaje());
        topico.setAutor(autor);
        topico.setCurso(curso);
        
        // Guardar el tópico
        Topico topicoGuardado = topicoRepository.save(topico);
        
        // Convertir a DTO de respuesta
        return convertirATopicoResponseDTO(topicoGuardado);
    }
    
    // Listar todos los tópicos con paginación
    public Page<TopicoResponseDTO> listarTopicos(Pageable pageable) {
        Page<Topico> topicos = topicoRepository.findAll(pageable);
        return topicos.map(this::convertirATopicoResponseDTO);
    }
    
    // Listar los primeros 10 tópicos ordenados por fecha de creación ASC
    public List<TopicoResponseDTO> listarPrimeros10Topicos() {
        List<Topico> topicos = topicoRepository.findTop10ByOrderByFechaCreacionAsc();
        return topicos.stream()
                .map(this::convertirATopicoResponseDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar tópicos por nombre de curso
    public Page<TopicoResponseDTO> buscarPorCurso(String nombreCurso, Pageable pageable) {
        Page<Topico> topicos = topicoRepository.findByCursoNombreContaining(nombreCurso, pageable);
        return topicos.map(this::convertirATopicoResponseDTO);
    }
    
    // Buscar tópicos por año
    public Page<TopicoResponseDTO> buscarPorAnio(int anio, Pageable pageable) {
        Page<Topico> topicos = topicoRepository.findByAnio(anio, pageable);
        return topicos.map(this::convertirATopicoResponseDTO);
    }
    
    // Buscar tópicos por curso y año
    public Page<TopicoResponseDTO> buscarPorCursoYAnio(String nombreCurso, int anio, Pageable pageable) {
        Page<Topico> topicos = topicoRepository.findByCursoNombreContainingAndAnio(nombreCurso, anio, pageable);
        return topicos.map(this::convertirATopicoResponseDTO);
    }
    
    // Obtener detalle de un tópico por ID
    public TopicoResponseDTO obtenerTopicoPorId(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado con ID: " + id));
        
        return convertirATopicoResponseDTO(topico);
    }
    
    // Actualizar un tópico existente
    @Transactional
    public TopicoResponseDTO actualizarTopico(Long id, TopicoRequestDTO topicoRequest) {
        // Verificar si el tópico existe
        if (!topicoRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("Tópico no encontrado con ID: " + id);
        }
        
        // Obtener el tópico existente
        Topico topicoExistente = topicoRepository.findById(id).get();
        
        // Validar que no existe otro tópico duplicado (excluyendo el actual)
        if (topicoRepository.existsByTituloAndMensajeAndIdNot(topicoRequest.titulo(), topicoRequest.mensaje(), id)) {
            throw new IllegalArgumentException("Ya existe un tópico con el mismo título y mensaje");
        }
        
        // Buscar el autor
        Usuario autor = usuarioRepository.findById(topicoRequest.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + topicoRequest.autorId()));
        
        // Buscar el curso
        Curso curso = cursoRepository.findById(topicoRequest.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado con ID: " + topicoRequest.cursoId()));
        
        // Actualizar los campos del tópico
        topicoExistente.setTitulo(topicoRequest.titulo());
        topicoExistente.setMensaje(topicoRequest.mensaje());
        topicoExistente.setAutor(autor);
        topicoExistente.setCurso(curso);
        
        // Guardar los cambios
        Topico topicoActualizado = topicoRepository.save(topicoExistente);
        
        // Convertir a DTO de respuesta
        return convertirATopicoResponseDTO(topicoActualizado);
    }
    
    // Eliminar un tópico por ID
    @Transactional
    public void eliminarTopico(Long id) {
        // Verificar si el tópico existe
        if (!topicoRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("Tópico no encontrado con ID: " + id);
        }
        
        // Eliminar el tópico
        topicoRepository.deleteById(id);
    }
    
    private TopicoResponseDTO convertirATopicoResponseDTO(Topico topico) {
        return new TopicoResponseDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor().getNombre(),
                topico.getCurso().getNombre()
        );
    }
}
