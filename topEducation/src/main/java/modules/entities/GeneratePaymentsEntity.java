package modules.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeneratePaymentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private String tipo_pago;

    private Integer numero_cuota;

    private Float monto_total_arancel;

    private Float matricula;

    private LocalDateTime ultimo_pago;

    private Float saldo_devuelto;  // esto guarda lo que se le tiene que devolver al alumno por puntaje

    // para asociarlo con un estudiante
    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private StudentEntity estudiante;



}
