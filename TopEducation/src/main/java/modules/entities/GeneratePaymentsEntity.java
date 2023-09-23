package modules.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cuotas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeneratePaymentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String tipoPago;
    private Integer numeroCuota;
    private Float montoPago;

    // para asociarlo con un estudiante
    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private StudentEntity student;



}
