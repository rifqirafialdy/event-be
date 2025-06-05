package com.example.event_be.refferal.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cas_app_wallet")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CasAppWallet {

    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "cas_app_id", length = 50)
    private String casAppId;

    @Column(name = "cas_par_wallet_type_code", length = 20)
    private String walletTypeCode;

    @Column(name = "par_currency_code", length = 20)
    private String currencyCode;

    @Column(name = "fni_app_product_id", length = 50)
    private String productId;

    @Column(name = "reference_number", length = 40)
    private String referenceNumber;

    @Column(name = "cas_par_wallet_type_amount", precision = 15, scale = 3)
    private java.math.BigDecimal walletAmount;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_at")
    private java.time.ZonedDateTime createdAt;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_at")
    private java.time.ZonedDateTime updatedAt;

    private boolean approved = false;
    private boolean deleted = false;
    private boolean published = false;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_at")
    private java.time.ZonedDateTime approvedAt;

    private int version = 1;
}

