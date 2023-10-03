package modules.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "estudiante")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String rut;

    private String nombre_estudiante;

    private String apellido_estudiante;

    private LocalDate fecha_nacimiento;   //  @RequesParam("") @DateTimeFormat(pattern = "yyyy-mm-dd") Date fechaN

    //private String fecha_nacimiento;

    private String tipo_escuela;

    private String nombre_escuela;

    private String anio_egreso;


}
