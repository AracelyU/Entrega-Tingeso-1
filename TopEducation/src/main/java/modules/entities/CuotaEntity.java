package modules.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cuotas")
@Data
public class CuotaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Integer numero_cuota;

    private Float valor_cuota;

    private String estado_cuota;  // puede ser pendiente/pagado

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fecha_vencimiento;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fecha_pago;

    // para asociarlo a un pago
    @ManyToOne
    @JoinColumn(name = "id_pago", nullable = false)
    private GeneratePaymentsEntity pago;


}
