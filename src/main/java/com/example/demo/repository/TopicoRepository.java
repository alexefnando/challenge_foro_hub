package com.example.demo.repository;

import com.example.demo.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    
    @Query("SELECT COUNT(t) > 0 FROM Topico t WHERE t.titulo = :titulo AND t.mensaje = :mensaje")
    boolean existsByTituloAndMensaje(@Param("titulo") String titulo, @Param("mensaje") String mensaje);
    
    @Query("SELECT COUNT(t) > 0 FROM Topico t WHERE t.titulo = :titulo AND t.mensaje = :mensaje AND t.id != :id")
    boolean existsByTituloAndMensajeAndIdNot(@Param("titulo") String titulo, @Param("mensaje") String mensaje, @Param("id") Long id);
    
    // Método para obtener los primeros 10 tópicos ordenados por fecha de creación ASC
    List<Topico> findTop10ByOrderByFechaCreacionAsc();
    
    // Método para buscar por nombre de curso
    @Query("SELECT t FROM Topico t WHERE t.curso.nombre LIKE %:nombreCurso%")
    Page<Topico> findByCursoNombreContaining(@Param("nombreCurso") String nombreCurso, Pageable pageable);
    
    // Método para buscar por año específico
    @Query("SELECT t FROM Topico t WHERE YEAR(t.fechaCreacion) = :anio")
    Page<Topico> findByAnio(@Param("anio") int anio, Pageable pageable);
    
    // Método para buscar por nombre de curso y año
    @Query("SELECT t FROM Topico t WHERE t.curso.nombre LIKE %:nombreCurso% AND YEAR(t.fechaCreacion) = :anio")
    Page<Topico> findByCursoNombreContainingAndAnio(@Param("nombreCurso") String nombreCurso, @Param("anio") int anio, Pageable pageable);
}
