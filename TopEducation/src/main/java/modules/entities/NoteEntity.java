package modules.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/*
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "notaEstudiante")
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Double nota;

    @ManyToOne
    @JoinColumn(name = "idEstudiante", referencedColumnName = "id")
    private StudentEntity estudiante;

}
*/