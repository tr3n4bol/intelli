package com.example.sem2_LR1;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ohm_calculations")
public class OhmCalculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double voltage;
    private Double current;
    private Double resistance;

    @Column(name="calculation_type")
    private String calculationType;
    private LocalDateTime timestamp;

    @Column(name="username")
    private String username;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getVoltage() { return voltage; }
    public void setVoltage(Double voltage) { this.voltage = voltage; }
    public Double getCurrent() { return current; }
    public void setCurrent(Double current) { this.current = current; }
    public Double getResistance() { return resistance; }
    public void setResistance(Double resistance) { this.resistance = resistance; }
    public String getCalculationType() { return calculationType; }
    public void setCalculationType(String calculationType) { this.calculationType = calculationType; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}