package modules.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/*
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "cuotaEstudiante")
@Entity
public class CuoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Long arancelTotal;

    @Column(unique = true, nullable = false)
    private Long numeroCuota;

    private String estadoCuota;

    private Date fechaInicio;

    private Date fechaTermino;

    @ManyToOne
    @JoinColumn(name = "idEstudiante", referencedColumnName = "id")
    private StudentEntity estudiante;

}
*/