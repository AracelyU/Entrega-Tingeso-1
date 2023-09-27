package modules.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String rut;
    private String nombreEstudiante;
    private String apellidoEstudiante;
    //private Date fechaN;     @RequesParam("") @DateTimeFormat(pattern = "yyyy-mm-dd") Date fechaN
    private String fechaNacimiento;
    private String tipoEscuela;
    private String nombreEscuela;
    private String anioEgreso;


}
